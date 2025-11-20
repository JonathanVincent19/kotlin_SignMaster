package com.example.finpro_mobapp

import androidx.compose.runtime.*
import com.example.finpro_mobapp.quiz.*

enum class QuizScreenState {
    QUIZ_SELECTION,
    QUIZ_1,
    QUIZ_2
}

@Composable
fun QuizScreen(
    onMenuClick: () -> Unit,
    onBackToHome: () -> Unit
) {
    var screenState by remember { mutableStateOf(QuizScreenState.QUIZ_SELECTION) }
    
    when (screenState) {
        QuizScreenState.QUIZ_SELECTION -> {
            QuizSelectionScreen(
                onMenuClick = onMenuClick,
                onQuiz1Click = {
                    screenState = QuizScreenState.QUIZ_1
                },
                onQuiz2Click = {
                    screenState = QuizScreenState.QUIZ_2
                }
            )
        }
        
        QuizScreenState.QUIZ_1 -> {
            Quiz1Container(
                onBackToQuizSelection = {
                    screenState = QuizScreenState.QUIZ_SELECTION
                },
                onBackToHome = onBackToHome
            )
        }
        
        QuizScreenState.QUIZ_2 -> {
            Quiz2Container(
                onBackToQuizSelection = {
                    screenState = QuizScreenState.QUIZ_SELECTION
                },
                onBackToHome = onBackToHome
            )
        }
    }
}

