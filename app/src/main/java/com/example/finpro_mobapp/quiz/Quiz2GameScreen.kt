package com.example.finpro_mobapp.quiz

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * AI Model Recognition Interface
 * TODO: Implement dengan model AI Anda nanti
 * 
 * Parameter bisa disesuaikan dengan model Anda:
 * - Bisa menggunakan Bitmap, ByteArray, atau ImageProxy
 * - Sesuaikan dengan format input model AI Anda
 */
interface GestureRecognitionModel {
    suspend fun recognizeGesture(imageData: Any): RecognitionResult
}

/**
 * Placeholder implementation - ganti dengan model AI Anda
 * 
 * TODO: Ganti implementasi ini dengan model AI Anda yang sebenarnya
 * Contoh:
 * - Load TensorFlow Lite model
 * - Preprocess image
 * - Run inference
 * - Return result
 */
class PlaceholderGestureRecognitionModel : GestureRecognitionModel {
    override suspend fun recognizeGesture(imageData: Any): RecognitionResult {
        // Simulasi processing time
        delay(500)
        
        // TODO: Ganti dengan model AI Anda
        // Untuk sekarang return random result sebagai placeholder
        val randomGestures = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
        val randomGesture = randomGestures[(0 until randomGestures.size).random()]
        
        val randomConfidence = 0.5f + (Math.random() * 0.45).toFloat()
        
        return RecognitionResult(
            gesture = randomGesture,
            confidence = randomConfidence,
            processingTime = 500L
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Quiz2GameScreen(
    level: Int,
    questions: List<Quiz2Question>,
    onComplete: (Int) -> Unit,
    onExit: () -> Unit,
    recognitionModel: GestureRecognitionModel = PlaceholderGestureRecognitionModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    
    // Permissions
    val cameraPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )
    
    // Game state
    var gameState by remember { mutableStateOf(Quiz2GameState.SHOWING_TARGET) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var completedQuestions by remember { mutableStateOf(0) }
    var recognizedGesture by remember { mutableStateOf<String?>(null) }
    var confidence by remember { mutableStateOf(0f) }
    val userAnswers = remember { mutableStateListOf<Quiz2Answer>() }
    
    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    
    // Show target for 3 seconds before recording
    LaunchedEffect(currentQuestionIndex) {
        if (currentQuestion != null) {
            gameState = Quiz2GameState.SHOWING_TARGET
            delay(3000) // Show target for 3 seconds
            gameState = Quiz2GameState.RECORDING
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quiz 2 - Level $level",
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
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onExit) {
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
                Quiz2GameState.SHOWING_TARGET -> {
                    ShowingTargetScreen(
                        question = currentQuestion!!,
                        onTimeout = { gameState = Quiz2GameState.RECORDING }
                    )
                }
                
                Quiz2GameState.RECORDING -> {
                    if (cameraPermissions.allPermissionsGranted) {
                        RecordingScreen(
                            question = currentQuestion!!,
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                            recognitionModel = recognitionModel,
                            onGestureRecognized = { result ->
                                recognizedGesture = result.gesture
                                confidence = result.confidence
                                gameState = Quiz2GameState.PROCESSING
                                
                                // Check if correct
                                val isCorrect = result.gesture.uppercase() == 
                                    currentQuestion.targetGesture.uppercase()
                                
                                scope.launch {
                                    delay(1000) // Show processing
                                    
                                    if (isCorrect) {
                                        userAnswers.add(
                                            Quiz2Answer(
                                                questionId = currentQuestion.id,
                                                recognizedGesture = result.gesture,
                                                confidence = result.confidence,
                                                correctAnswer = currentQuestion.targetGesture,
                                                isCorrect = true,
                                                attempts = 1
                                            )
                                        )
                                        completedQuestions++
                                        gameState = Quiz2GameState.FEEDBACK_CORRECT
                                    } else {
                                        gameState = Quiz2GameState.FEEDBACK_WRONG
                                    }
                                }
                            }
                        )
                    } else {
                        PermissionRequestScreen(
                            onRequestPermission = {
                                cameraPermissions.launchMultiplePermissionRequest()
                            }
                        )
                    }
                }
                
                Quiz2GameState.PROCESSING -> {
                    ProcessingScreen(
                        recognizedGesture = recognizedGesture ?: "",
                        confidence = confidence
                    )
                }
                
                Quiz2GameState.FEEDBACK_CORRECT -> {
                    FeedbackCorrectScreen(
                        onNext = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                recognizedGesture = null
                                confidence = 0f
                                gameState = Quiz2GameState.SHOWING_TARGET
                            } else {
                                gameState = Quiz2GameState.COMPLETED
                            }
                        }
                    )
                }
                
                Quiz2GameState.FEEDBACK_WRONG -> {
                    FeedbackWrongScreen(
                        recognizedGesture = recognizedGesture ?: "",
                        correctAnswer = currentQuestion?.targetGesture ?: "",
                        onRetry = {
                            recognizedGesture = null
                            confidence = 0f
                            gameState = Quiz2GameState.RECORDING
                        }
                    )
                }
                
                Quiz2GameState.COMPLETED -> {
                    LaunchedEffect(Unit) {
                        onComplete(completedQuestions)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowingTargetScreen(
    question: Quiz2Question,
    onTimeout: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📋",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Peragakan:",
            fontSize = 18.sp,
            color = Color(0xFF7F8C8D),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4A90E2)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = question.targetGesture,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = question.description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        CircularProgressIndicator(
            color = Color(0xFF4A90E2),
            modifier = Modifier.size(40.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Siapkan kamera...",
            fontSize = 16.sp,
            color = Color(0xFF7F8C8D)
        )
    }
}

@Composable
private fun RecordingScreen(
    question: Quiz2Question,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    recognitionModel: GestureRecognitionModel,
    onGestureRecognized: (RecognitionResult) -> Unit
) {
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isCapturing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Camera Preview
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                    
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Overlay UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Target display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Peragakan:",
                        fontSize = 14.sp,
                        color = Color(0xFF7F8C8D),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = question.targetGesture,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Capture button
            Button(
                onClick = {
                    if (!isCapturing && imageCapture != null) {
                        isCapturing = true
                        scope.launch {
                            // TODO: Implement actual image capture
                            // Contoh implementasi:
                            // 1. Capture image menggunakan imageCapture.takePicture()
                            // 2. Convert ke format yang dibutuhkan model (Bitmap/ByteArray)
                            // 3. Pass ke recognitionModel.recognizeGesture()
                            
                            // Untuk sekarang, simulate dengan delay
                            delay(500)
                            
                            // Simulate recognition result
                            val result = recognitionModel.recognizeGesture(
                                // Placeholder - ganti dengan image data sebenarnya
                                "placeholder_image_data"
                            )
                            onGestureRecognized(result)
                            isCapturing = false
                        }
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCapturing) Color(0xFF95A5A6) else Color(0xFFE74C3C)
                ),
                enabled = !isCapturing
            ) {
                if (isCapturing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Text(
                        text = "📸",
                        fontSize = 32.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isCapturing) "Memproses..." else "Tekan untuk capture",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun ProcessingScreen(
    recognizedGesture: String,
    confidence: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF4A90E2),
            modifier = Modifier.size(64.dp),
            strokeWidth = 4.dp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Memproses gerakan...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C3E50)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Terdeteksi: $recognizedGesture",
            fontSize = 16.sp,
            color = Color(0xFF7F8C8D)
        )
        
        Text(
            text = "Confidence: ${(confidence * 100).toInt()}%",
            fontSize = 14.sp,
            color = Color(0xFF7F8C8D)
        )
    }
}

@Composable
private fun FeedbackCorrectScreen(
    onNext: () -> Unit
) {
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
        
        CircularProgressIndicator(
            color = Color(0xFF27AE60),
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
private fun FeedbackWrongScreen(
    recognizedGesture: String,
    correctAnswer: String,
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
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Terdeteksi: $recognizedGesture",
                    fontSize = 14.sp,
                    color = Color(0xFF7F8C8D)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Jawaban benar: $correctAnswer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
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

@Composable
private fun PermissionRequestScreen(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📷",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Izin Kamera Diperlukan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Quiz 2 memerlukan akses kamera untuk mengenali gerakan tangan Anda.",
            fontSize = 16.sp,
            color = Color(0xFF7F8C8D),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = onRequestPermission,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Berikan Izin",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

