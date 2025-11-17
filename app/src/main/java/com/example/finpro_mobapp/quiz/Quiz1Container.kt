package com.example.finpro_mobapp.quiz

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Container untuk mengelola navigasi Quiz 1
 */

enum class Quiz1Screen {
    MAIN_LEVEL_SELECTION,
    SUB_LEVEL_SELECTION,
    GAME,
    FINAL
}

@Composable
fun Quiz1Container(
    onBackToQuizSelection: () -> Unit,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val progressManager = remember { QuizProgressManager(context) }
    
    var currentScreen by remember { mutableStateOf(Quiz1Screen.MAIN_LEVEL_SELECTION) }
    var levels by remember { mutableStateOf(loadLevelsWithProgress(progressManager)) }
    var selectedLevel by remember { mutableStateOf<MainLevel?>(null) }
    var selectedSubLevel by remember { mutableStateOf<SubLevel?>(null) }
    var gameQuestions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var completedQuestionsCount by remember { mutableStateOf(0) }
    var savedProgress by remember { mutableStateOf<SavedProgress?>(null) }
    
    when (currentScreen) {
        Quiz1Screen.MAIN_LEVEL_SELECTION -> {
            Quiz1MainLevelScreen(
                levels = levels,
                onBackClick = onBackToQuizSelection,
                onLevelClick = { level ->
                    if (level.isUnlocked) {
                        selectedLevel = level
                        currentScreen = Quiz1Screen.SUB_LEVEL_SELECTION
                    }
                }
            )
        }
        
        Quiz1Screen.SUB_LEVEL_SELECTION -> {
            // Reload levels to get latest progress
            LaunchedEffect(currentScreen) {
                levels = loadLevelsWithProgress(progressManager)
            }
            
            selectedLevel?.let { level ->
                // Get updated level data
                val updatedLevel = levels.find { it.id == level.id } ?: level
                
                Quiz1SubLevelScreen(
                    level = updatedLevel,
                    progressManager = progressManager,
                    onBackClick = {
                        currentScreen = Quiz1Screen.MAIN_LEVEL_SELECTION
                    },
                    onSubLevelClick = { subLevel ->
                        if (subLevel.isUnlocked) {
                            selectedSubLevel = subLevel
                            
                            // Check for saved progress
                            savedProgress = progressManager.loadInProgressQuiz(subLevel.id)
                            
                            // Prepare questions
                            val questionStrings = QuestionBank.getQuestionsForSubLevel(subLevel.id)
                            gameQuestions = questionStrings.mapIndexed { index, answer ->
                                Question(
                                    id = "${subLevel.id}_q${index + 1}",
                                    subLevelId = subLevel.id,
                                    questionNumber = index + 1,
                                    answer = answer,
                                    type = level.type
                                )
                            }
                            
                            currentScreen = Quiz1Screen.GAME
                        }
                    }
                )
            }
        }
        
        Quiz1Screen.GAME -> {
            selectedSubLevel?.let { subLevel ->
                Quiz1GameScreen(
                    subLevel = subLevel,
                    questions = gameQuestions,
                    savedProgress = savedProgress,
                    progressManager = progressManager,
                    onComplete = { completed ->
                        completedQuestionsCount = completed
                        
                        // Clear saved progress
                        progressManager.clearProgress(subLevel.id)
                        
                        // Update progress
                        updateSubLevelProgress(
                            levels = levels,
                            progressManager = progressManager,
                            subLevelId = subLevel.id,
                            completedQuestions = completed
                        )
                        
                        currentScreen = Quiz1Screen.FINAL
                    },
                    onExit = { currentIndex, completed, answers ->
                        // Save progress if not completed
                        if (completed < gameQuestions.size) {
                            progressManager.saveInProgressQuiz(
                                subLevelId = subLevel.id,
                                currentQuestionIndex = currentIndex,
                                completedQuestions = completed,
                                userAnswers = answers
                            )
                        }
                        
                        // Reload levels to reflect progress
                        levels = loadLevelsWithProgress(progressManager)
                        
                        currentScreen = Quiz1Screen.SUB_LEVEL_SELECTION
                    }
                )
            }
        }
        
        Quiz1Screen.FINAL -> {
            selectedSubLevel?.let { subLevel ->
                // Find updated sub-level data
                val updatedSubLevel = levels
                    .find { it.id == subLevel.parentLevelId }
                    ?.subLevels
                    ?.find { it.id == subLevel.id }
                    ?: subLevel
                
                // Check if next sub-level unlocked
                val nextSubLevel = levels
                    .find { it.id == subLevel.parentLevelId }
                    ?.subLevels
                    ?.find { it.id == getNextSubLevelId(subLevel.id) }
                
                Quiz1FinalScreen(
                    subLevel = updatedSubLevel,
                    completedQuestions = completedQuestionsCount,
                    totalQuestions = gameQuestions.size,
                    isNextSubLevelUnlocked = nextSubLevel?.isUnlocked == true,
                    nextSubLevelId = nextSubLevel?.id,
                    onBackToHome = onBackToHome,
                    onRetry = {
                        // Restart same sub-level
                        currentScreen = Quiz1Screen.GAME
                    },
                    onNextLevel = {
                        // Go back to sub-level selection
                        currentScreen = Quiz1Screen.SUB_LEVEL_SELECTION
                    }
                )
            }
        }
    }
}

/**
 * Load levels dengan progress dari storage
 */
private fun loadLevelsWithProgress(progressManager: QuizProgressManager): List<MainLevel> {
    val levels = LevelData.getInitialLevels()
    
    levels.forEach { level ->
        level.subLevels.forEach { subLevel ->
            // Load completion status
            val completion = progressManager.loadSubLevelCompletion(subLevel.id)
            subLevel.completedQuestions = completion.completedQuestions
            
            // Load unlock status
            subLevel.isUnlocked = progressManager.isSubLevelUnlocked(subLevel.id)
            
            // Update status based on completion
            subLevel.status = when {
                completion.isCompleted -> SubLevelStatus.COMPLETED
                completion.completedQuestions > 0 -> SubLevelStatus.ON_PROGRESS
                progressManager.hasProgress(subLevel.id) -> SubLevelStatus.ON_PROGRESS
                subLevel.isUnlocked -> SubLevelStatus.NOT_STARTED
                else -> SubLevelStatus.LOCKED
            }
        }
        
        // Update main level status
        LevelData.updateLevelStatus(level)
        
        // Check if level should be unlocked
        if (level.id > 1) {
            val previousLevel = levels.find { it.id == level.id - 1 }
            if (previousLevel?.status == LevelStatus.COMPLETED) {
                level.isUnlocked = true
                level.status = if (level.subLevels.any { it.status != SubLevelStatus.LOCKED }) 
                    LevelStatus.ON_PROGRESS else LevelStatus.ON_PROGRESS
            }
        }
    }
    
    return levels
}

/**
 * Update sub-level progress and unlock next levels
 */
private fun updateSubLevelProgress(
    levels: List<MainLevel>,
    progressManager: QuizProgressManager,
    subLevelId: String,
    completedQuestions: Int
) {
    levels.forEach { level ->
        level.subLevels.forEach { subLevel ->
            if (subLevel.id == subLevelId) {
                subLevel.completedQuestions = completedQuestions
                
                // Update status
                subLevel.status = when {
                    completedQuestions >= subLevel.totalQuestions -> SubLevelStatus.COMPLETED
                    completedQuestions > 0 -> SubLevelStatus.ON_PROGRESS
                    else -> SubLevelStatus.NOT_STARTED
                }
                
                // Save completion status
                progressManager.saveSubLevelCompletion(
                    subLevelId = subLevel.id,
                    completedQuestions = completedQuestions,
                    totalQuestions = subLevel.totalQuestions
                )
                
                // If completed, unlock next sub-level
                if (subLevel.status == SubLevelStatus.COMPLETED) {
                    val nextSubLevelId = getNextSubLevelId(subLevel.id)
                    
                    // Find and unlock next sub-level
                    levels.forEach { lvl ->
                        lvl.subLevels.forEach { sub ->
                            if (sub.id == nextSubLevelId) {
                                sub.isUnlocked = true
                                sub.status = SubLevelStatus.NOT_STARTED
                                progressManager.saveUnlockStatus(sub.id, true)
                            }
                        }
                    }
                    
                    // Check if all sub-levels completed, unlock next main level
                    if (level.subLevels.all { it.status == SubLevelStatus.COMPLETED }) {
                        val nextLevel = levels.find { it.id == level.id + 1 }
                        nextLevel?.let {
                            it.isUnlocked = true
                            it.status = LevelStatus.ON_PROGRESS
                            it.subLevels.firstOrNull()?.let { firstSub ->
                                firstSub.isUnlocked = true
                                firstSub.status = SubLevelStatus.NOT_STARTED
                                progressManager.saveUnlockStatus(firstSub.id, true)
                            }
                        }
                    }
                }
            }
        }
        
        // Update main level status
        LevelData.updateLevelStatus(level)
    }
}

/**
 * Get next sub-level ID
 */
private fun getNextSubLevelId(currentId: String): String {
    val parts = currentId.split(".")
    val levelNum = parts[0].toInt()
    val subNum = parts[1].toInt()
    return if (subNum < 3) {
        "$levelNum.${subNum + 1}"
    } else {
        "${levelNum + 1}.1"
    }
}

