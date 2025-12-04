package com.example.finpro_mobapp.quiz

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.finpro_mobapp.StatisticsManager

/**
 * container pengelola navigasi quiz 2
 */
enum class Quiz2Screen {
    LEVEL_SELECTION,
    GAME,
    FINAL
}

@Composable
fun Quiz2Container(
    onBackToQuizSelection: () -> Unit,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val statisticsManager = remember { StatisticsManager(context) }
    val progressManager = remember { Quiz2ProgressManager(context) }
    
    var currentScreen by remember { mutableStateOf(Quiz2Screen.LEVEL_SELECTION) }
    var selectedLevel by remember { mutableStateOf(1) }
    var gameQuestions by remember { mutableStateOf<List<Quiz2Question>>(emptyList()) }
    var completeQuestionsCount by remember { mutableStateOf(0) }
    
    // Load unlock status for all levels
    val levelsUnlockStatus = remember {
        mutableStateOf(mapOf(
            1 to progressManager.isLevelUnlocked(1),
            2 to progressManager.isLevelUnlocked(2),
            3 to progressManager.isLevelUnlocked(3)
        ))
    }

    when (currentScreen) {
        Quiz2Screen.LEVEL_SELECTION -> {
            // Reload unlock status when returning to level selection
            LaunchedEffect(currentScreen) {
                levelsUnlockStatus.value = mapOf(
                    1 to progressManager.isLevelUnlocked(1),
                    2 to progressManager.isLevelUnlocked(2),
                    3 to progressManager.isLevelUnlocked(3)
                )
            }
            
            Quiz2LevelSelectionScreen(
                onBackClick = onBackToQuizSelection,
                levelsUnlockStatus = levelsUnlockStatus.value,
                levelsCompletionStatus = mapOf(
                    1 to progressManager.isLevelCompleted(1),
                    2 to progressManager.isLevelCompleted(2),
                    3 to progressManager.isLevelCompleted(3)
                ),
                onLevelClick = { level ->
                    // Only allow if level is unlocked
                    if (levelsUnlockStatus.value[level] == true) {
                        selectedLevel = level
                        gameQuestions = Quiz2QuestionBank.getQuestionsForLevel(level)
                        currentScreen = Quiz2Screen.GAME
                    }
                }
            )
        }

        Quiz2Screen.GAME -> {
            Quiz2GameScreen(
                level = selectedLevel,
                questions = gameQuestions,
                onComplete = { completed ->
                    completeQuestionsCount = completed
                    currentScreen = Quiz2Screen.FINAL
                },
                onExit = {
                    currentScreen = Quiz2Screen.LEVEL_SELECTION
                }
            )
        }
        
        Quiz2Screen.FINAL -> {
            // Update streak saat quiz selesai
            LaunchedEffect(Unit) {
                statisticsManager.updateStreak()
                
                // Save completion status
                progressManager.saveLevelCompletion(
                    level = selectedLevel,
                    completedQuestions = completeQuestionsCount,
                    totalQuestions = gameQuestions.size
                )
                
                // Unlock next level if current level is completed
                if (completeQuestionsCount >= gameQuestions.size) {
                    progressManager.unlockNextLevel(selectedLevel)
                }
            }
            
            Quiz2FinalScreen(
                level = selectedLevel,
                completedQuestions = completeQuestionsCount,
                totalQuestions = gameQuestions.size,
                onBackToHome = onBackToHome,
                onRetry = {
                    currentScreen = Quiz2Screen.GAME
                },
                onNextLevel = {
                    if (selectedLevel < 3) {
                        selectedLevel++
                        gameQuestions = Quiz2QuestionBank.getQuestionsForLevel(selectedLevel)
                        currentScreen = Quiz2Screen.GAME
                    } else {
                        currentScreen = Quiz2Screen.LEVEL_SELECTION
                    }
                },
                isNextLevelUnlocked = selectedLevel < 3 && progressManager.isLevelUnlocked(selectedLevel + 1)
            )
        }
    }
}
