package com.example.finpro_mobapp.quiz

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Real implementation of GestureRecognitionModel
 * Menggunakan MediaPipe Hand Landmarker + TensorFlow Lite Classifier
 * 
 * Flow:
 * 1. ImageProxy → HandLandmarkerHelper → Landmarks (FloatArray[126])
 * 2. Landmarks → InterpreterHelper → Classification results
 * 3. Results → Best gesture label dengan confidence
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RealGestureRecognitionModel(
    private val context: Context,
    private val isFrontCamera: Boolean = true
) : GestureRecognitionModel {

    private val interpreterHelper = InterpreterHelper(context)
    private val labels = interpreterHelper.loadLabels()
    
    // Reuse HandLandmarkerHelper untuk menghindari timeout
    // Gunakan thread-safe CompletableDeferred untuk setiap request
    @Volatile
    private var currentDeferred: CompletableDeferred<FloatArray>? = null
    
    private val handLandmarkerHelper: HandLandmarkerHelper by lazy {
        HandLandmarkerHelper(
            context = context,
            isFrontCamera = isFrontCamera,
            onResult = { landmarks ->
                // Callback dari MediaPipe (dipanggil di background thread)
                // Resume deferred dengan hasil landmarks
                val deferred = currentDeferred
                if (deferred != null && !deferred.isCompleted) {
                    deferred.complete(landmarks)
                    currentDeferred = null
                    Log.d("RealGestureRecognitionModel", "✅ Landmarks received: ${landmarks.count { it != 0f }}/126")
                }
            },
            onError = { error ->
                // Error callback - complete dengan exception
                val deferred = currentDeferred
                if (deferred != null && !deferred.isCompleted) {
                    deferred.completeExceptionally(Exception(error))
                    currentDeferred = null
                    Log.e("RealGestureRecognitionModel", "❌ HandLandmarker error: $error")
                }
            }
        )
    }
    
    init {
        Log.d("RealGestureRecognitionModel", "✅ Initialized with ${labels.size} labels")
    }

    /**
     * Recognize gesture dari ImageProxy
     * NOTE: ImageProxy akan di-close oleh HandLandmarkerHelper
     */
    override suspend fun recognizeGesture(imageData: Any): RecognitionResult {
        return try {
            val imageProxy = imageData as? ImageProxy
                ?: return RecognitionResult(
                    gesture = "",
                    confidence = 0f,
                    processingTime = 0L
                )

            val startTime = System.currentTimeMillis()

            // Get landmarks using suspendCoroutine
            val landmarks = getLandmarksFromImage(imageProxy)

            val nonZeroCount = landmarks.count { it != 0f }
            
            // Validasi lebih ketat: minimal 30 non-zero values (bukan 20) untuk memastikan ada tangan
            if (nonZeroCount < 30) {
                Log.d("RealGestureRecognitionModel", "⚠️ Tidak cukup landmarks: $nonZeroCount/126 (min: 30)")
                return RecognitionResult(
                    gesture = "",
                    confidence = 0f,
                    processingTime = System.currentTimeMillis() - startTime
                )
            }
            
            Log.d("RealGestureRecognitionModel", "✅ Landmarks OK: $nonZeroCount/126")

            // Classify landmarks
            val results = interpreterHelper.classify(landmarks)

            if (results.isEmpty()) {
                Log.d("RealGestureRecognitionModel", "❌ Classification gagal")
                return RecognitionResult(
                    gesture = "",
                    confidence = 0f,
                    processingTime = System.currentTimeMillis() - startTime
                )
            }

            val best = results.maxByOrNull { it.second }
            val labelIndex = best?.first ?: -1
            val confidence = best?.second ?: 0f
            val label = labels.getOrNull(labelIndex) ?: ""

            val processingTime = System.currentTimeMillis() - startTime

            Log.d("RealGestureRecognitionModel", "✅ Recognition: $label (${String.format("%.2f%%", confidence * 100)})")

            RecognitionResult(
                gesture = label,
                confidence = confidence,
                processingTime = processingTime
            )

        } catch (e: Exception) {
            Log.e("RealGestureRecognitionModel", "❌ Error recognizing gesture: ${e.message}", e)
            RecognitionResult(
                gesture = "",
                confidence = 0f,
                processingTime = 0L
            )
        }
    }

    /**
     * Get landmarks from ImageProxy using reusable HandLandmarkerHelper
     */
    private suspend fun getLandmarksFromImage(imageProxy: ImageProxy): FloatArray {
        return suspendCancellableCoroutine { continuation ->
            // Cancel previous request jika ada
            val previousDeferred = currentDeferred
            if (previousDeferred != null && !previousDeferred.isCompleted) {
                previousDeferred.cancel()
            }
            
            // Buat deferred baru untuk request ini
            val deferred = CompletableDeferred<FloatArray>()
            currentDeferred = deferred
            
            // Setup callback untuk deferred
            deferred.invokeOnCompletion { throwable ->
                if (throwable == null && !continuation.isCancelled) {
                    // Success - resume dengan hasil
                    try {
                        continuation.resume(deferred.getCompleted())
                    } catch (e: Exception) {
                        Log.e("RealGestureRecognitionModel", "Error resuming continuation: ${e.message}", e)
                    }
                } else if (throwable != null && !continuation.isCancelled) {
                    // Error - resume dengan exception (kecuali jika cancelled)
                    if (throwable !is CancellationException) {
                        try {
                            continuation.resumeWithException(throwable)
                        } catch (e: Exception) {
                            Log.e("RealGestureRecognitionModel", "Error resuming exception: ${e.message}", e)
                        }
                    }
                }
            }
            
            // Detect hand menggunakan reusable helper (will close imageProxy internally)
            try {
                Log.d("RealGestureRecognitionModel", "🔍 Detecting hand landmarks...")
                handLandmarkerHelper.detect(imageProxy)
            } catch (e: Exception) {
                currentDeferred = null
                if (!continuation.isCancelled) {
                    continuation.resumeWithException(e)
                }
            }
            
            // Cleanup on cancellation
            continuation.invokeOnCancellation {
                currentDeferred?.cancel()
                currentDeferred = null
            }
        }
    }

    /**
     * Close resources
     */
    fun close() {
        interpreterHelper.close()
    }
}

