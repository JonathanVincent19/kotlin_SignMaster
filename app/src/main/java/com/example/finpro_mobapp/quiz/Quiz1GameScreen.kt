package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finpro_mobapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class GameState {
    DISPLAYING_SEQUENCE,  // Menampilkan gambar huruf
    WAITING_INPUT,        // Menunggu user input
    FEEDBACK_CORRECT,     // Menampilkan feedback benar
    FEEDBACK_WRONG,       // Menampilkan feedback salah
    COMPLETED             // Quiz selesai
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Quiz1GameScreen(
    subLevel: SubLevel,
    questions: List<Question>,
    savedProgress: SavedProgress? = null,
    progressManager: QuizProgressManager,
    onComplete: (Int) -> Unit,  // Pass completed questions count
    onExit: (currentIndex: Int, completedCount: Int, answers: List<UserAnswer>) -> Unit
) {
    var gameState by remember { mutableStateOf(GameState.DISPLAYING_SEQUENCE) }
    var currentQuestionIndex by remember { 
        mutableStateOf(savedProgress?.currentQuestionIndex ?: 0) 
    }
    var currentLetterIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var completedQuestions by remember { 
        mutableStateOf(savedProgress?.completedQuestions ?: 0) 
    }
    val userAnswers = remember { 
        mutableStateListOf<UserAnswer>().apply {
            savedProgress?.userAnswers?.let { addAll(it) }
        }
    }
    
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val scope = rememberCoroutineScope()
    
    // Start sequence display when question changes
    LaunchedEffect(currentQuestionIndex) {
        if (currentQuestion != null && gameState != GameState.FEEDBACK_CORRECT 
            && gameState != GameState.FEEDBACK_WRONG) {
            gameState = GameState.DISPLAYING_SEQUENCE
            currentLetterIndex = 0
            userInput = ""
            
            // Display letter sequence
            currentQuestion.letters.forEachIndexed { index, _ ->
                currentLetterIndex = index
                delay(subLevel.displayDuration.toLong())
            }
            
            // After all letters shown, wait for input
            gameState = GameState.WAITING_INPUT
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Level ${subLevel.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Soal ${currentQuestionIndex + 1}/${questions.size}",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "🔴●",
                            fontSize = 16.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        onExit(currentQuestionIndex, completedQuestions, userAnswers.toList())
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A90E2)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (gameState) {
                GameState.DISPLAYING_SEQUENCE -> {
                    DisplayingSequenceScreen(
                        question = currentQuestion!!,
                        currentLetterIndex = currentLetterIndex
                    )
                }
                GameState.WAITING_INPUT -> {
                WaitingInputScreen(
                    question = currentQuestion!!,
                    userInput = userInput,
                    onInputChange = { userInput = it },
                    onSubmit = {
                        val isCorrect = userInput.trim().uppercase() == currentQuestion.answer.uppercase()
                        
                        if (isCorrect) {
                            // Save correct answer
                            userAnswers.add(
                                UserAnswer(
                                    questionId = currentQuestion.id,
                                    userAnswer = userInput.trim().uppercase(),
                                    correctAnswer = currentQuestion.answer,
                                    isCorrect = true
                                )
                            )
                            completedQuestions++
                            
                            // Save progress immediately (real-time update!)
                            progressManager.saveSubLevelCompletion(
                                subLevelId = subLevel.id,
                                completedQuestions = completedQuestions,
                                totalQuestions = questions.size
                            )
                            
                            gameState = GameState.FEEDBACK_CORRECT
                        } else {
                            gameState = GameState.FEEDBACK_WRONG
                        }
                    },
                        onReplay = {
                            scope.launch {
                                gameState = GameState.DISPLAYING_SEQUENCE
                                currentLetterIndex = 0
                                
                                // Replay sequence
                                currentQuestion.letters.forEachIndexed { index, _ ->
                                    currentLetterIndex = index
                                    delay(subLevel.displayDuration.toLong())
                                }
                                gameState = GameState.WAITING_INPUT
                            }
                        }
                    )
                }
                GameState.FEEDBACK_CORRECT -> {
                    FeedbackCorrectScreen(
                        onNext = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                gameState = GameState.DISPLAYING_SEQUENCE
                            } else {
                                gameState = GameState.COMPLETED
                            }
                        }
                    )
                }
                GameState.FEEDBACK_WRONG -> {
                    FeedbackWrongScreen(
                        onRetry = {
                            scope.launch {
                                gameState = GameState.DISPLAYING_SEQUENCE
                                currentLetterIndex = 0
                                userInput = ""
                                
                                // Replay sequence
                                currentQuestion!!.letters.forEachIndexed { index, _ ->
                                    currentLetterIndex = index
                                    delay(subLevel.displayDuration.toLong())
                                }
                                gameState = GameState.WAITING_INPUT
                            }
                        }
                    )
                }
                GameState.COMPLETED -> {
                    // Navigate to final screen
                    LaunchedEffect(Unit) {
                        onComplete(completedQuestions)
                    }
                }
            }
        }
    }
}

@Composable
private fun DisplayingSequenceScreen(
    question: Question,
    currentLetterIndex: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Instruction
        Text(
            text = when(question.type) {
                LevelType.SINGLE_LETTER -> "Huruf apa ini? Ingat baik-baik!"
                LevelType.SINGLE_WORD -> "Kata apa ini? Ingat huruf-hurufnya!"
                LevelType.TWO_WORDS -> "Kalimat apa ini? Ingat huruf-hurufnya!"
            },
            fontSize = 16.sp,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Big image display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                val currentImageId = question.imageSequence.getOrNull(currentLetterIndex)
                if (currentImageId != null) {
                    // TODO: Uncomment when real images available
                    // Image(
                    //     painter = painterResource(id = currentImageId),
                    //     contentDescription = "Letter ${question.letters[currentLetterIndex]}",
                    //     modifier = Modifier.fillMaxSize(),
                    //     contentScale = ContentScale.Crop
                    // )
                    
                    // Placeholder
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📷",
                            fontSize = 80.sp
                        )
                        Text(
                            text = "Huruf ${question.letters[currentLetterIndex]}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun WaitingInputScreen(
    question: Question,
    userInput: String,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onReplay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Instruction
        Text(
            text = when(question.type) {
                LevelType.SINGLE_LETTER -> "Huruf apa ini? Ingat baik-baik!"
                LevelType.SINGLE_WORD -> "Kata apa ini? Ingat huruf-hurufnya!"
                LevelType.TWO_WORDS -> "Kalimat apa ini? Ingat huruf-hurufnya!"
            },
            fontSize = 16.sp,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Empty box (same size as image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
                .background(Color(0xFFFAFAFA))
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Input field
        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Jawaban...") },
            singleLine = question.type == LevelType.SINGLE_LETTER,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A90E2),
                unfocusedBorderColor = Color(0xFFBDC3C7)
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Submit button
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = userInput.isNotBlank()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "📤",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Kirim Jawaban",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Replay button (text style)
        if (question.type != LevelType.SINGLE_LETTER) {
            TextButton(onClick = onReplay) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Replay",
                        tint = Color(0xFF4A90E2),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ulangi Tayangan",
                        fontSize = 14.sp,
                        color = Color(0xFF4A90E2)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedbackCorrectScreen(
    onNext: () -> Unit
) {
    // Auto continue after 1.5 seconds
    LaunchedEffect(Unit) {
        delay(1500)
        onNext()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Modern check icon
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Correct",
            tint = Color(0xFF27AE60),
            modifier = Modifier.size(72.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "YEYY KAMU BENAR!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF27AE60),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Auto continuing indicator
        CircularProgressIndicator(
            color = Color(0xFF27AE60),
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
private fun FeedbackWrongScreen(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "❌ SALAH",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE74C3C),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "NETNOT, COBA LAGI!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF5D6D7E),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Retry button
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE74C3C)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Retry",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ulangi Soal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

