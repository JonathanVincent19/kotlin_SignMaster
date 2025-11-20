package com.example.finpro_mobapp.quiz

import androidx.compose.runtime.*

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
    var currentScreen by remember { mutableStateOf(Quiz2Screen.LEVEL_SELECTION) }
    var selectedLevel by remember { mutableStateOf(1) }
    var gameQuestions by remember { mutableStateOf<List<Quiz2Question>>(emptyList()) }
    var completeQuestionsCount by remember { mutableStateOf(0) }

    when (currentScreen) {
        Quiz2Screen.LEVEL_SELECTION -> {
            Quiz2LevelSelectionScreen(
                onBackClick = onBackToQuizSelection,
                onLevelClick = { level ->
                    selectedLevel = level
                    gameQuestions = Quiz2QuestionBank.getQuestionsForLevel(level)
                    currentScreen = Quiz2Screen.GAME
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
                isNextLevelUnlocked = selectedLevel < 3
            )
        }
    }
}
