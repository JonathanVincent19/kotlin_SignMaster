package com.example.finpro_mobapp.quiz

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.Executors
import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.layout.offset

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
                recognitionModel: GestureRecognitionModel? = null
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
                        // Use RealGestureRecognitionModel if not provided
                        val model = recognitionModel ?: remember {
                            RealGestureRecognitionModel(context, isFrontCamera = true)
                        }
                        
                        // Untuk level 2 dan 3, gunakan letter-by-letter validation
                        if (currentQuestion!!.type == Quiz2QuestionType.SINGLE_WORD || 
                            currentQuestion.type == Quiz2QuestionType.TWO_WORDS) {
                            RecordingScreenLetterByLetter(
                                question = currentQuestion,
                                context = context,
                                lifecycleOwner = lifecycleOwner,
                                recognitionModel = model,
                                onComplete = { result ->
                                    recognizedGesture = result.gesture
                                    confidence = result.confidence
                                    
                                    scope.launch {
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
                                    }
                                }
                            )
                        } else {
                            // Level 1: validation biasa (huruf per huruf, masing-masing soal 1 huruf)
                        RecordingScreen(
                                question = currentQuestion,
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                                recognitionModel = model,
                            onGestureRecognized = { result ->
                                recognizedGesture = result.gesture
                                confidence = result.confidence
                                gameState = Quiz2GameState.PROCESSING
                                
                                    // Backend validation menggunakan GestureValidator
                                    val validationResult = GestureValidator.validate(
                                        recognizedGesture = result.gesture,
                                        targetGesture = currentQuestion.targetGesture,
                                        confidence = result.confidence,
                                        questionType = currentQuestion.type
                                    )
                                
                                scope.launch {
                                        delay(1500) // Show processing dengan animasi
                                    
                                        if (validationResult.isCorrect) {
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
                        }
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
                // Flexible layout: coba horizontal dulu dengan font lebih kecil jika perlu
                // Jika benar-benar panjang baru wrap ke atas-bawah
                val textLength = question.targetGesture.length
                val hasSpace = question.targetGesture.contains(" ")
                
                if (hasSpace && textLength > 12) {
                    // Hanya jika benar-benar panjang (>12 karakter) baru split atas-bawah
                    val words = question.targetGesture.split(" ", limit = 2)
                    words.forEachIndexed { index, word ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        Text(
                            text = word,
                            fontSize = if (word.length > 6) 32.sp else 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Horizontal layout dengan font size dinamis - lebih kecil jika panjang
                    val dynamicFontSize = when {
                        textLength > 10 -> 30.sp  // Sangat panjang (11+ huruf): font lebih kecil untuk muat horizontal
                        textLength > 8 -> 34.sp   // Panjang (9-10 huruf): font sedang-kecil
                        textLength > 6 -> 40.sp   // Sedang (7-8 huruf): font sedang (misalnya "HARI INI")
                        textLength > 5 -> 42.sp   // Sedang (6 huruf)
                        else -> 48.sp             // Pendek (≤5 huruf): font besar
                    }
                    Text(
                        text = question.targetGesture,
                        fontSize = dynamicFontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                
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

/**
 * RecordingScreen untuk Level 2 dan 3: Validasi huruf per huruf
 * Setiap huruf yang benar diberi design hijau dan di-keep, lanjut ke huruf berikutnya
 */
@Composable
private fun RecordingScreenLetterByLetter(
    question: Quiz2Question,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    recognitionModel: GestureRecognitionModel,
    onComplete: (RecognitionResult) -> Unit
) {
    var isProcessing by remember { mutableStateOf(false) }
    var detectedGesture by remember { mutableStateOf<String?>(null) }
    var detectionConfidence by remember { mutableStateOf(0f) }
    var lastProcessTime by remember { mutableStateOf(0L) }
    
    // Track huruf yang sudah benar (index)
    var completedLetters by remember { mutableStateOf(0) }
    // Flag untuk prevent double detection setelah huruf benar
    var isLetterJustCompleted by remember { mutableStateOf(false) }
    var lastCompletedLetterTime by remember { mutableStateOf(0L) }
    // Cache target letter untuk mencegah perubahan selama processing
    var currentTargetLetterState by remember { mutableStateOf<String?>(null) }
    
    val targetWord = question.targetGesture.uppercase()
    
    // Update currentTargetLetterState ketika completedLetters berubah
    LaunchedEffect(completedLetters) {
        val currentLetterIndex = completedLetters
        currentTargetLetterState = if (currentLetterIndex < targetWord.length) {
            val letter = targetWord[currentLetterIndex]
            // Handle spasi khusus
            if (letter == ' ') " " else letter.toString()
        } else {
            null
        }
    }
    
    // Initialize currentTargetLetterState
    val currentTargetLetter = currentTargetLetterState ?: if (completedLetters < targetWord.length) {
        val letter = targetWord[completedLetters]
        if (letter == ' ') " " else letter.toString()
    } else {
        null
    }
    
    val scope = rememberCoroutineScope()
    val cameraExecutor = remember { java.util.concurrent.Executors.newSingleThreadExecutor() }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
    
    // Jangan auto-complete langsung
    // Biarkan SuccessAnimationOverlay yang handle animasi dan trigger onComplete
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Camera Preview (Full Screen)
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor) { imageProxy ->
                                val currentTime = System.currentTimeMillis()
                                
                                // Cooldown setelah huruf benar: 800ms untuk mencegah double detection
                                val timeSinceLastCompletion = currentTime - lastCompletedLetterTime
                                val shouldSkipDueToCooldown = isLetterJustCompleted && timeSinceLastCompletion < 800
                                
                                // Throttle: Process setiap 500ms dan skip jika masih cooldown
                                if (!isProcessing && !shouldSkipDueToCooldown && (currentTime - lastProcessTime) > 500) {
                                    isProcessing = true
                                    lastProcessTime = currentTime
                                    
                                    scope.launch {
                                        try {
                                            // Get current target letter (harus di dalam launch scope untuk avoid stale value)
                                            val targetLetterNow = if (completedLetters < targetWord.length) {
                                                val letter = targetWord[completedLetters]
                                                if (letter == ' ') " " else letter.toString()
                                            } else null
                                            
                                            // Handle spasi: skip validasi dan langsung lanjut
                                            if (targetLetterNow == " ") {
                                                android.util.Log.d("RecordingScreenLetterByLetter", "⏭️ Spasi - skip ke huruf berikutnya (${completedLetters + 1}/${targetWord.length})")
                                                
                                                // Mark sebagai completed dan set cooldown
                                                isLetterJustCompleted = true
                                                lastCompletedLetterTime = System.currentTimeMillis()
                                                
                                                // Update IMMEDIATELY untuk prevent double detection
                                                completedLetters++
                                                
                                                // Reset state
                                                detectedGesture = null
                                                detectionConfidence = 0f
                                                
                                                // Delay untuk visual feedback
                                                delay(500)
                                                
                                                // Reset cooldown
                                                isLetterJustCompleted = false
                                                
                                                imageProxy.close()
                                                isProcessing = false
                                                return@launch
                                            }
                                            
                                            val result = withTimeoutOrNull(5000) {
                                                recognitionModel.recognizeGesture(imageProxy)
                                            } ?: run {
                                                detectedGesture = null
                                                detectionConfidence = 0f
                                                isProcessing = false
                                                imageProxy.close()
                                                return@launch
                                            }
                                            
                                            if (result.gesture.isNotEmpty() && result.confidence >= 0.2f) {
                                                detectedGesture = result.gesture
                                                detectionConfidence = result.confidence
                                                
                                                // Validasi huruf per huruf (hanya jika masih huruf target yang sama)
                                                if (targetLetterNow != null && result.confidence >= 0.4f) {
                                                    val detectedLetter = result.gesture.uppercase().trim()
                                                    
                                                    // Double-check: pastikan target letter masih sama (untuk prevent race condition)
                                                    val currentTarget = if (completedLetters < targetWord.length) {
                                                        val letter = targetWord[completedLetters]
                                                        if (letter == ' ') " " else letter.toString()
                                                    } else null
                                                    
                                                    // Check apakah huruf yang dideteksi adalah huruf target saat ini
                                                    if (currentTarget == targetLetterNow && detectedLetter == targetLetterNow) {
                                                        android.util.Log.d("RecordingScreenLetterByLetter", "✅ Huruf '$targetLetterNow' benar! Progress: ${completedLetters + 1}/${targetWord.length}")
                                                        
                                                        // Set cooldown flag SEBELUM update completedLetters
                                                        isLetterJustCompleted = true
                                                        lastCompletedLetterTime = System.currentTimeMillis()
                                                        
                                                        // Update completed letters IMMEDIATELY untuk mencegah deteksi ulang
                                                        completedLetters++
                                                        
                                                        // Reset detection state IMMEDIATELY
                                                        detectedGesture = null
                                                        detectionConfidence = 0f
                                                        
                                                        // Delay untuk visual feedback (setelah state update)
                                                        delay(1000) // Cooldown lebih lama (1 detik)
                                                        
                                                        // Reset cooldown setelah delay
                                                        isLetterJustCompleted = false
                                                        
                                                        val nextLetter = if (completedLetters < targetWord.length) {
                                                            val letter = targetWord[completedLetters]
                                                            if (letter == ' ') " " else letter.toString()
                                                        } else "SELESAI"
                                                        android.util.Log.d("RecordingScreenLetterByLetter", "🔄 Lanjut ke huruf berikutnya: $nextLetter")
                                                    } else {
                                                        android.util.Log.d("RecordingScreenLetterByLetter", "❌ Salah: '$detectedLetter' != '$targetLetterNow' (Expected: $targetLetterNow, Current: $currentTarget, Progress: ${completedLetters}/${targetWord.length})")
                                                    }
                                                }
                                            } else {
                                                detectedGesture = null
                                                detectionConfidence = 0f
                                            }
                                            
                                            isProcessing = false
                                        } catch (e: Exception) {
                                            android.util.Log.e("RecordingScreenLetterByLetter", "❌ Error: ${e.message}", e)
                                            detectedGesture = null
                                            detectionConfidence = 0f
                                            isProcessing = false
                                        }
                                    }
                                } else {
                                    imageProxy.close()
                                }
                            }
                        }
                    
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Status Indicator (Top Center)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        ) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                    Text(
                        text = when {
                            completedLetters >= targetWord.length -> "✅ Selesai!"
                            isProcessing -> "Memproses gerakan..."
                            currentTargetLetter != null -> {
                                if (currentTargetLetter == " ") {
                                    "Spasi - Lanjut ke huruf berikutnya (${completedLetters + 1}/${targetWord.length})"
                                } else {
                                    "Peragakan: $currentTargetLetter (${completedLetters + 1}/${targetWord.length})"
                                }
                            }
                            else -> "Arahkan tangan ke kamera..."
                        },
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        // Soal Quiz Bisindo (Bottom) dengan huruf per huruf
        Box(
                modifier = Modifier
                .align(Alignment.BottomCenter)
                    .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Soal ${question.questionNumber}",
                        fontSize = 12.sp,
                        color = Color(0xFF7F8C8D),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "Peragakan dengan gerakan:",
                        fontSize = 14.sp,
                        color = Color(0xFF5D6D7E),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Display huruf per huruf dengan design hijau untuk yang sudah benar
                    // Flexible layout: 1 kata per baris jika multi-word, font fleksibel
                    val hasSpaces = targetWord.contains(" ")
                    val words = if (hasSpaces) targetWord.split(" ") else listOf(targetWord)
                    val longestWordLength = words.maxOfOrNull { it.length } ?: targetWord.length
                    val totalLength = targetWord.length
                    
                    // Ukuran dinamis - lebih kecil dan lebih fleksibel
                    val letterBoxSize = when {
                        totalLength > 14 -> 32.dp  // Sangat panjang (14+ huruf): box sangat kecil
                        totalLength > 12 -> 35.dp  // Panjang (13-14 huruf): box kecil
                        totalLength > 10 -> 38.dp  // Sedang-panjang (11-12 huruf): box sedang-kecil
                        totalLength > 8 -> 40.dp   // Sedang (9-10 huruf): box kecil-sedang (misalnya "SAMA SAMA")
                        totalLength > 6 -> 42.dp   // Sedang (7-8 huruf): box sedang
                        totalLength > 5 -> 45.dp   // Sedang (6 huruf)
                        else -> 48.dp              // Pendek (≤5 huruf): box normal
                    }
                    val letterFontSize = when {
                        totalLength > 14 -> 18.sp  // Sangat panjang: font sangat kecil
                        totalLength > 12 -> 20.sp  // Panjang: font kecil
                        totalLength > 10 -> 22.sp  // Sedang-panjang: font kecil-sedang
                        totalLength > 8 -> 24.sp   // Sedang (9-10 huruf): font sedang-kecil (misalnya "SAMA SAMA")
                        totalLength > 6 -> 26.sp   // Sedang (7-8 huruf): font sedang
                        totalLength > 5 -> 28.sp   // Sedang (6 huruf)
                        else -> 30.sp              // Pendek (≤5 huruf): font normal
                    }
                    val letterPadding = when {
                        totalLength > 14 -> 1.dp   // Sangat panjang: padding minimal
                        totalLength > 12 -> 1.dp   // Panjang: padding minimal
                        totalLength > 10 -> 2.dp   // Sedang-panjang: padding kecil
                        totalLength > 8 -> 2.dp    // Sedang (9-10 huruf): padding kecil
                        totalLength > 6 -> 3.dp    // Sedang (7-8 huruf): padding sedang
                        else -> 3.dp               // Pendek: padding sedang
                    }
                    
                    // Untuk multi-word: 1 kata per baris (lebih menarik)
                    // Untuk single-word: horizontal jika muat, wrap jika sangat panjang
                    val shouldWrapPerWord = hasSpaces && words.size > 1
                    val shouldWrapLongWord = !hasSpaces && totalLength > 10
                    
                    if (shouldWrapPerWord) {
                        // Multi-line layout: 1 kata per baris (untuk "MAKASIH BANYAK" dll)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var currentIndex = 0
                            words.forEachIndexed { wordIndex, word ->
                                if (wordIndex > 0) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                                
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    word.forEachIndexed { letterIndexInWord, letter ->
                                        val index = currentIndex + letterIndexInWord
                                        val isCompleted = index < completedLetters
                                        val isCurrent = index == completedLetters
                                        
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = letterPadding)
                                                .width(letterBoxSize)
                                                .height(letterBoxSize)
                                                .background(
                                                    color = when {
                                                        isCompleted -> Color(0xFF27AE60)
                                                        isCurrent -> Color(0xFF3498DB)
                                                        else -> Color(0xFFECF0F1)
                                                    },
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    width = if (isCurrent) 2.dp else 0.dp,
                                                    color = Color(0xFFE74C3C),
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = letter.toString(),
                                                fontSize = letterFontSize,
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isCompleted -> Color.White
                                                    isCurrent -> Color.White
                                                    else -> Color(0xFF95A5A6)
                                                }
                                            )
                                        }
                                    }
                                }
                                
                                currentIndex += word.length + 1 // +1 for space
                            }
                        }
                    } else if (shouldWrapLongWord) {
                        // Wrap untuk single word yang sangat panjang (>10 huruf)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Baris pertama: maksimal 6 huruf per baris
                            var currentRowIndex = 0
                            while (currentRowIndex < targetWord.length) {
                                val endIndex = minOf(currentRowIndex + 6, targetWord.length)
                                val rowLetters = targetWord.substring(currentRowIndex until endIndex)
                                
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    rowLetters.forEachIndexed { rowIdx, letter ->
                                        val index = currentRowIndex + rowIdx
                                        val isCompleted = index < completedLetters
                                        val isCurrent = index == completedLetters
                                        
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = letterPadding)
                                                .width(letterBoxSize)
                                                .height(letterBoxSize)
                                                .background(
                                                    color = when {
                                                        isCompleted -> Color(0xFF27AE60)
                                                        isCurrent -> Color(0xFF3498DB)
                                                        else -> Color(0xFFECF0F1)
                                                    },
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    width = if (isCurrent) 2.dp else 0.dp,
                                                    color = Color(0xFFE74C3C),
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = letter.toString(),
                                                fontSize = letterFontSize,
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isCompleted -> Color.White
                                                    isCurrent -> Color.White
                                                    else -> Color(0xFF95A5A6)
                                                }
                                            )
                                        }
                                    }
                                }
                                
                                currentRowIndex = endIndex
                            }
                        }
                    } else {
                        // Single-line layout untuk kata normal
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            targetWord.forEachIndexed { index, letter ->
                                val isCompleted = index < completedLetters
                                val isCurrent = index == completedLetters
                                val isSpace = letter == ' '
                                
                                // Untuk spasi, tampilkan sebagai kotak kosong yang lebih kecil
                                if (isSpace) {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp)
                                            .width(20.dp)
                                            .height(letterBoxSize),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Spasi ditampilkan sebagai garis vertikal
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                        .height(letterBoxSize * 0.6f)
                                                .background(
                                                    color = when {
                                                        isCompleted -> Color(0xFF27AE60)
                                                        isCurrent -> Color(0xFF3498DB)
                                                        else -> Color(0xFFBDC3C7)
                                                    },
                                                    shape = RoundedCornerShape(1.dp)
                                                )
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = letterPadding)
                                            .width(letterBoxSize)
                                            .height(letterBoxSize)
                                            .background(
                                                color = when {
                                                    isCompleted -> Color(0xFF27AE60) // Hijau untuk huruf yang sudah benar
                                                    isCurrent -> Color(0xFF3498DB) // Biru untuk huruf yang sedang ditargetkan
                                                    else -> Color(0xFFECF0F1) // Abu-abu untuk huruf yang belum
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .border(
                                                width = if (isCurrent) 2.dp else 0.dp,
                                                color = Color(0xFFE74C3C),
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = letter.toString(),
                                            fontSize = letterFontSize,
                                            fontWeight = FontWeight.Bold,
                                            color = when {
                                                isCompleted -> Color.White
                                                isCurrent -> Color.White
                                                else -> Color(0xFF95A5A6)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Progress indicator
                    if (completedLetters > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Progress: ${completedLetters}/${targetWord.length}",
                            fontSize = 12.sp,
                            color = Color(0xFF27AE60),
                            fontWeight = FontWeight.SemiBold
                    )
                }
                    
                    // Feedback indicator
                    if (detectedGesture != null && !isProcessing && currentTargetLetter != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val isCorrect = detectedGesture?.uppercase() == currentTargetLetter
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isCorrect) "✅" else "❌",
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (isCorrect) "Benar!" else "Salah: $detectedGesture",
                                fontSize = 14.sp,
                                color = if (isCorrect) Color(0xFF27AE60) else Color(0xFFE74C3C),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
        
        // Success Animation Overlay - Muncul ketika semua huruf sudah benar (seperti level 1)
        if (completedLetters >= targetWord.length) {
            SuccessAnimationOverlay(
                onAnimationComplete = {
                    // Trigger completion setelah animasi selesai
                            delay(500)
                    onComplete(
                        RecognitionResult(
                            gesture = targetWord,
                            confidence = 1.0f,
                            processingTime = 0L
                        )
                    )
                }
            )
        }
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
    var isProcessing by remember { mutableStateOf(false) }
    var detectedGesture by remember { mutableStateOf<String?>(null) }
    var detectionConfidence by remember { mutableStateOf(0f) }
    var lastProcessTime by remember { mutableStateOf(0L) }
    
    val scope = rememberCoroutineScope()
    val cameraExecutor = remember { java.util.concurrent.Executors.newSingleThreadExecutor() }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Camera Preview (Full Screen)
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    // ImageAnalysis untuk real-time detection (tanpa capture button)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor) { imageProxy ->
                                // Throttle: Process setiap 500ms untuk efisiensi (jangan terlalu cepat)
                                val currentTime = System.currentTimeMillis()
                                if (!isProcessing && (currentTime - lastProcessTime) > 500) {
                                    isProcessing = true
                                    lastProcessTime = currentTime
                                    
                        scope.launch {
                                        try {
                                            // Real-time recognition dengan timeout lebih pendek
                                            // Timeout lebih lama (5 detik) karena MediaPipe LIVE_STREAM butuh waktu untuk callback
                                            val result = withTimeoutOrNull(5000) {
                                                recognitionModel.recognizeGesture(imageProxy)
                                            } ?: run {
                                                // Timeout: reset state
                                                android.util.Log.d("RecordingScreen", "⏱️ Timeout - no response from model")
                                                detectedGesture = null
                                                detectionConfidence = 0f
                                                isProcessing = false
                                                return@launch
                                            }
                                            
                                            // Hanya update UI jika ada gesture yang terdeteksi dengan confidence minimal
                                            // Threshold 0.2f (20%) untuk menghindari false positive
                                            if (result.gesture.isNotEmpty() && result.confidence >= 0.2f) {
                                                android.util.Log.d("RecordingScreen", "✅ Detected: ${result.gesture} (${(result.confidence * 100).toInt()}%)")
                                                
                                                detectedGesture = result.gesture
                                                detectionConfidence = result.confidence
                                                
                                                // Auto-validate jika confidence cukup (>= 40% untuk lebih akurat)
                                                if (result.confidence >= 0.4f) {
                                                    val validationResult = GestureValidator.validate(
                                                        recognizedGesture = result.gesture,
                                                        targetGesture = question.targetGesture,
                                                        confidence = result.confidence,
                                                        questionType = question.type
                                                    )
                                                    
                                                    if (validationResult.isCorrect) {
                                                        android.util.Log.d("RecordingScreen", "🎉 CORRECT! Moving to next question")
                                                        // Jika benar, trigger callback setelah delay kecil untuk visual feedback
                                                        kotlinx.coroutines.delay(500)
                            onGestureRecognized(result)
                                                        return@launch
                                                    } else {
                                                        android.util.Log.d("RecordingScreen", "❌ Wrong gesture: ${result.gesture} != ${question.targetGesture}")
                                                    }
                                                }
                                            } else {
                                                // Reset jika detection gagal, confidence terlalu rendah, atau tidak ada gesture
                                                if (result.gesture.isEmpty()) {
                                                    android.util.Log.d("RecordingScreen", "⚠️ No gesture detected (confidence: ${(result.confidence * 100).toInt()}%)")
                                                } else {
                                                    android.util.Log.d("RecordingScreen", "⚠️ Low confidence: ${result.gesture} (${(result.confidence * 100).toInt()}%)")
                                                }
                                                detectedGesture = null
                                                detectionConfidence = 0f
                                            }
                                            
                                            isProcessing = false
                                        } catch (e: Exception) {
                                            android.util.Log.e("RecordingScreen", "❌ Error processing gesture: ${e.message}", e)
                                            detectedGesture = null
                                            detectionConfidence = 0f
                                            isProcessing = false
                        }
                    }
                                } else {
                                    // Close image jika tidak di-process (throttled)
                                    imageProxy.close()
                                }
                            }
                        }
                    
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Status Indicator (Top Center)
        Box(
                modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        ) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                    if (isProcessing) {
                    CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                        color = Color.White,
                            strokeWidth = 2.dp
                    )
                    }
                    Text(
                        text = when {
                            isProcessing -> "Memproses gerakan..."
                            detectedGesture != null && detectionConfidence >= 0.2f -> {
                                val confidencePercent = (detectionConfidence * 100).toInt()
                                val status = if (detectionConfidence >= 0.4f) {
                                    val isCorrect = detectedGesture?.uppercase() == question.targetGesture.uppercase()
                                    if (isCorrect) "✅ Benar!" else "Terdeteksi: $detectedGesture ($confidencePercent%)"
                                } else {
                                    "Terdeteksi: $detectedGesture ($confidencePercent%)"
                                }
                                status
                            }
                            else -> "Arahkan tangan ke kamera..."
                        },
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        // Soal Quiz Bisindo (Bottom)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Soal ${question.questionNumber}",
                        fontSize = 12.sp,
                        color = Color(0xFF7F8C8D),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
            
            Text(
                        text = "Peragakan gerakan Bisindo:",
                fontSize = 14.sp,
                        color = Color(0xFF5D6D7E),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Text(
                        text = question.targetGesture,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        textAlign = TextAlign.Center
                    )
                    
                    // Feedback indicator (hanya muncul jika ada detection)
                    if (detectedGesture != null && !isProcessing) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val isCorrect = detectedGesture?.uppercase() == question.targetGesture.uppercase()
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isCorrect) "✅" else "❌",
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (isCorrect) "Benar!" else "$detectedGesture",
                                fontSize = 14.sp,
                                color = if (isCorrect) Color(0xFF27AE60) else Color(0xFFE74C3C),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProcessingScreen(
    recognizedGesture: String,
    confidence: Float
) {
    // Animation untuk loading indicator
    val infiniteTransition = rememberInfiniteTransition(label = "processing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    // Animation untuk text fade in
    var textVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(300)
        textVisible = true
    }
    
    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(500),
        label = "textAlpha"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated loading indicator
        CircularProgressIndicator(
            color = Color(0xFF4A90E2),
            modifier = Modifier
                .size(64.dp)
                .scale(scale)
                .alpha(alpha),
            strokeWidth = 4.dp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Animated text
        Text(
            text = "Memproses gerakan...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C3E50),
            modifier = Modifier.alpha(textAlpha)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Terdeteksi: $recognizedGesture",
            fontSize = 16.sp,
            color = Color(0xFF7F8C8D),
            modifier = Modifier.alpha(textAlpha)
        )
        
        Text(
            text = "Confidence: ${(confidence * 100).toInt()}%",
            fontSize = 14.sp,
            color = Color(0xFF7F8C8D),
            modifier = Modifier.alpha(textAlpha)
        )
    }
}

@Composable
private fun FeedbackCorrectScreen(
    onNext: () -> Unit
) {
    // Success animation: scale up + fade in
    var animate by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        animate = true
        delay(2000)
        onNext()
    }
    
    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
    // Pulsing animation untuk check icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success icon dengan animasi
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Correct",
            tint = Color(0xFF27AE60),
            modifier = Modifier
                .size(120.dp)
                .scale(scale * pulseScale)
                .alpha(alpha)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Success text dengan animasi
        Text(
            text = "YEYY KAMU BENAR!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF27AE60),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Lanjut ke soal berikutnya...",
            fontSize = 16.sp,
            color = Color(0xFF7F8C8D),
            modifier = Modifier.alpha(alpha)
        )
    }
}

/**
 * Success Animation Overlay untuk Level 2 & 3
 * Muncul ketika semua huruf sudah benar (kata sudah hijau semua)
 * Animasi sama seperti FeedbackCorrectScreen di level 1
 */
@Composable
private fun SuccessAnimationOverlay(
    onAnimationComplete: suspend () -> Unit
) {
    // Success animation: scale up + fade in
    var animate by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100) // Small delay untuk smooth transition
        animate = true
        delay(2000) // Show animation for 2 seconds
        onAnimationComplete()
    }
    
    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
    // Pulsing animation untuk check icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    // Overlay background dengan semi-transparent
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success icon dengan animasi
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Correct",
                tint = Color(0xFF27AE60),
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale * pulseScale)
                    .alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Success text dengan animasi
            Text(
                text = "YEYY KAMU BENAR!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Lanjut ke soal berikutnya...",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}

@Composable
private fun FeedbackWrongScreen(
    recognizedGesture: String,
    correctAnswer: String,
    onRetry: () -> Unit
) {
    // Error animation: shake + fade in
    var animate by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        animate = true
    }
    
    // Shake animation untuk error icon (shake 3x)
    var shakeAnimation by remember { mutableStateOf(0f) }
    
    LaunchedEffect(animate) {
        if (animate) {
            // Shake left-right 3 times
            repeat(6) {
                shakeAnimation = if (it % 2 == 0) 10f else -10f
                delay(50)
            }
            shakeAnimation = 0f // Return to center
        }
    }
    
    val alpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(400),
        label = "alpha"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        // Error icon dengan shake animation
        val shakeOffset by animateFloatAsState(
            targetValue = shakeAnimation,
            animationSpec = tween<Float>(50, easing = LinearEasing),
            label = "shakeOffset"
        )
        
        Text(
            text = "❌",
            fontSize = 80.sp,
            modifier = Modifier
                .offset(x = shakeOffset.dp)
                .scale(scale)
                .alpha(alpha)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "SALAH",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE74C3C),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "NETNOT, COBA LAGI!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF5D6D7E),
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(alpha)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Card dengan animasi fade in
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha)
                .scale(scale),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Jawaban benar: $correctAnswer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Retry button dengan animasi
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE74C3C)
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
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
        
        Spacer(modifier = Modifier.height(32.dp))
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

