package com.example.finpro_mobapp.quiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.core.graphics.createBitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker.HandLandmarkerOptions

/**
 * Helper class untuk MediaPipe Hand Landmarker
 * Mendeteksi landmark tangan dari ImageProxy (CameraX)
 */
class HandLandmarkerHelper(
    private val context: Context,
    private val onResult: (FloatArray) -> Unit,
    private val onError: (String) -> Unit,
    private val isFrontCamera: Boolean = true
) {
    private val yuvConverter = YuvToRgbConverter()

    private val handLandmarker: HandLandmarker by lazy {
        setupHandLandmarker()
    }

    private fun setupHandLandmarker(): HandLandmarker {
        return try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("hand_landmarker.task")
                .build()

            val options = HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(RunningMode.LIVE_STREAM)  // ⚡ LIVE_STREAM mode untuk real-time
                .setNumHands(2)
                .setMinHandDetectionConfidence(0.5f)
                .setMinHandPresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setResultListener { result, _ ->
                    // Callback otomatis dari MediaPipe
                    handleLandmarkResult(result.landmarks())
                }
                .setErrorListener { error ->
                    Log.e("HandLandmarker", "LiveStream error: ${error.message}")
                    onError("MediaPipe error: ${error.message}")
                }
                .build()

            HandLandmarker.createFromOptions(context, options).also {
                Log.d("HandLandmarker", "✅ MediaPipe initialized successfully (LIVE_STREAM mode)")
            }
        } catch (e: Exception) {
            Log.e("HandLandmarker", "❌ Error initializing MediaPipe", e)
            onError("Error loading hand landmarker: ${e.message}")
            throw e
        }
    }

    /**
     * Detect hand dari ImageProxy (langsung dari CameraX)
     * Mirip dengan HandsDetector.detect()
     */
    fun detect(imageProxy: ImageProxy) {
        try {
            // 1. Convert YUV ke RGB Bitmap
            val bitmap = createBitmap(imageProxy.width, imageProxy.height)
            yuvConverter.yuvToRgb(imageProxy, bitmap)

            // 2. Rotate & Mirror untuk front camera
            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                if (isFrontCamera) {
                    postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
                }
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )

            // 3. Kirim ke MediaPipe (async)
            val mpImage = BitmapImageBuilder(rotatedBitmap).build()
            handLandmarker.detectAsync(mpImage, System.currentTimeMillis())

        } catch (e: Exception) {
            Log.e("HandLandmarker", "Error detecting hand: ${e.message}", e)
            onError("Error: ${e.message}")
        } finally {
            imageProxy.close()
        }
    }

    /**
     * Handle hasil deteksi dari MediaPipe (dipanggil otomatis via listener)
     */
    private fun handleLandmarkResult(allHands: List<List<NormalizedLandmark>?>?) {
        val handCount = allHands?.count { !it.isNullOrEmpty() } ?: 0

        if (handCount == 0) {
            Log.d("HandLandmarker", "No hands detected")
            // Return array kosong
            onResult(FloatArray(126))
            return
        }

        Log.d("HandLandmarker", "Hands detected: $handCount")

        // Ekstrak raw coordinates (2 tangan)
        val keypoints = extractTwoHandsKeypoints(allHands ?: emptyList())

        val nonZeroCount = keypoints.count { it != 0f }
        Log.d("HandLandmarker", "Non-zero values: $nonZeroCount/126")

        if (nonZeroCount > 0) {
            val first9 = keypoints.take(9).joinToString(", ") { "%.3f".format(it) }
            Log.d("HandLandmarker", "First 9 (Hand 1): [$first9]")

            if (nonZeroCount > 63) {
                val hand2Start = keypoints.drop(63).take(9).joinToString(", ") { "%.3f".format(it) }
                Log.d("HandLandmarker", "First 9 (Hand 2): [$hand2Start]")
            }
        }

        onResult(keypoints)
    }

    /**
     * Ekstrak RAW coordinates untuk 2 tangan (concat berurutan)
     * Sesuai dengan Python: coords_all[0] + coords_all[1]
     */
    private fun extractTwoHandsKeypoints(allHands: List<List<NormalizedLandmark>?>): FloatArray {
        val maxHands = 2
        val landmarksPerHand = 21
        val coordsPerLandmark = 3
        val totalSize = maxHands * landmarksPerHand * coordsPerLandmark  // 126
        val result = FloatArray(totalSize)

        for (i in 0 until maxHands) {
            val hand = allHands.getOrNull(i)

            if (!hand.isNullOrEmpty()) {
                for (j in hand.indices) {
                    val lm = hand[j]
                    val base = i * landmarksPerHand * coordsPerLandmark + j * coordsPerLandmark

                    // Mirror X jika front camera (sudah di-mirror di bitmap, tapi mirror lagi di coords)
                    val x = if (isFrontCamera) 1f - lm.x() else lm.x()

                    result[base + 0] = x
                    result[base + 1] = lm.y()
                    result[base + 2] = lm.z()
                }
                Log.d("HandLandmarker", "Hand $i extracted: ${hand.size} landmarks")
            } else {
                Log.d("HandLandmarker", "Hand $i: null/empty (padded with zeros)")
            }
        }

        return result
    }

    fun close() {
        handLandmarker.close()
    }
}

/**
 * Helper class untuk convert YUV ImageProxy ke RGB Bitmap
 * (Lebih cepat dari compress JPEG)
 */
class YuvToRgbConverter {
    fun yuvToRgb(image: ImageProxy, output: Bitmap) {
        val imageY = image.planes[0]
        val imageU = image.planes[1]
        val imageV = image.planes[2]

        val yBuffer = imageY.buffer
        val uBuffer = imageU.buffer
        val vBuffer = imageV.buffer

        val yRowStride = imageY.rowStride
        val uvRowStride = imageU.rowStride
        val uvPixelStride = imageU.pixelStride

        val width = image.width
        val height = image.height

        val pixels = IntArray(width * height)

        val y = ByteArray(yBuffer.remaining())
        val u = ByteArray(uBuffer.remaining())
        val v = ByteArray(vBuffer.remaining())

        yBuffer.get(y)
        uBuffer.get(u)
        vBuffer.get(v)

        var yp = 0
        for (j in 0 until height) {
            val pY = yRowStride * j
            val uvRow = uvRowStride * (j shr 1)
            for (i in 0 until width) {
                val uvOffset = uvRow + (i shr 1) * uvPixelStride
                val yVal = (y[pY + i].toInt() and 0xff)
                val uVal = (u[uvOffset].toInt() and 0xff) - 128
                val vVal = (v[uvOffset].toInt() and 0xff) - 128

                val r = (yVal + 1.370705f * vVal).toInt().coerceIn(0, 255)
                val g = (yVal - 0.337633f * uVal - 0.698001f * vVal).toInt().coerceIn(0, 255)
                val b = (yVal + 1.732446f * uVal).toInt().coerceIn(0, 255)

                pixels[yp++] = -0x1000000 or (r shl 16) or (g shl 8) or b
            }
        }

        output.setPixels(pixels, 0, width, 0, 0, width, height)
    }
}

