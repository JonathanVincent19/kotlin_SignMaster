package com.example.finpro_mobapp.quiz

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * Helper class untuk TensorFlow Lite Model
 * Classify hand landmarks menjadi gesture (A, B, C, dll)
 * 
 * NOTE: Pastikan model file ada di assets:
 * - sign_classifier.tflite
 * - labels.txt (format: satu label per baris)
 */
class InterpreterHelper(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val modelFileName = "sign_classifier.tflite"

    init {
        try {
            val modelBuffer = loadModelFile(modelFileName)
            interpreter = Interpreter(modelBuffer)
            Log.d("InterpreterHelper", "✅ Model loaded successfully: $modelFileName")
            
            // Log model info for debugging
            val inputTensor = interpreter?.getInputTensor(0)
            val outputTensor = interpreter?.getOutputTensor(0)
            Log.d("InterpreterHelper", "Input shape: ${inputTensor?.shape()?.contentToString()}")
            Log.d("InterpreterHelper", "Output shape: ${outputTensor?.shape()?.contentToString()}")
        } catch (e: Exception) {
            Log.e("InterpreterHelper", "❌ Error loading model: ${e.message}", e)
            Log.e("InterpreterHelper", "Pastikan $modelFileName ada di assets/")
        }
    }

    /**
     * Load model file dari assets
     */
    private fun loadModelFile(filename: String): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Classify landmarks menjadi gesture label
     * @param landmarks FloatArray dengan 126 elemen (2 tangan x 21 landmarks x 3 coords)
     * @return List<Pair<Int, Float>> dengan (index, confidence) sorted by confidence
     */
    fun classify(landmarks: FloatArray): List<Pair<Int, Float>> {
        if (landmarks.size != 126) {
            Log.e("InterpreterHelper", "Invalid landmark data size: ${landmarks.size}. Expected 126.")
            return emptyList()
        }

        return try {
            val modelInstance = interpreter ?: run {
                Log.e("InterpreterHelper", "Model interpreter is null")
                return emptyList()
            }

            // Get output shape from model
            val outputTensor = modelInstance.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            val numClasses = outputShape[1] // Assume shape is [1, num_classes]
            
            Log.d("InterpreterHelper", "Output shape: ${outputShape.contentToString()}, numClasses: $numClasses")

            // Prepare input buffer (1, 126)
            val inputBuffer = ByteBuffer.allocateDirect(4 * 126) // 126 floats * 4 bytes
            inputBuffer.order(ByteOrder.nativeOrder())

            // Copy landmarks to input buffer
            for (value in landmarks) {
                inputBuffer.putFloat(value)
            }
            inputBuffer.rewind()

            // Prepare output buffer (1, num_classes)
            val outputArray = FloatArray(numClasses)
            val outputBuffer = ByteBuffer.allocateDirect(4 * numClasses)
            outputBuffer.order(ByteOrder.nativeOrder())

            // Run inference
            modelInstance.run(inputBuffer, outputBuffer)

            // Read output probabilities
            outputBuffer.rewind()
            for (i in 0 until numClasses) {
                outputArray[i] = outputBuffer.float
            }

            Log.d("InterpreterHelper", "✅ Inference complete. Output size: ${outputArray.size}")
            
            // Log top 3 for debugging
            val top3 = outputArray.mapIndexed { index, score ->
                index to score
            }.sortedByDescending { it.second }.take(3)
            Log.d("InterpreterHelper", "Top 3: ${top3.joinToString { "(${it.first}, ${String.format("%.3f", it.second)})" }}")

            // Return top 5 predictions
            outputArray.mapIndexed { index, score ->
                index to score
            }.sortedByDescending { it.second }.take(5)

        } catch (e: Exception) {
            Log.e("InterpreterHelper", "❌ Classification error: ${e.message}", e)
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Load labels dari assets/labels.txt
     */
    fun loadLabels(): List<String> {
        return try {
            context.assets.open("labels.txt").bufferedReader().readLines()
        } catch (e: Exception) {
            Log.e("InterpreterHelper", "Error loading labels", e)
            Log.e("InterpreterHelper", "Pastikan labels.txt ada di assets/")
            // Fallback: A-Z
            ('A'..'Z').map { it.toString() }
        }
    }

    /**
     * Get model input shape (for debugging)
     */
    fun getInputShape(): IntArray? {
        return try {
            interpreter?.getInputTensor(0)?.shape()
        } catch (e: Exception) {
            Log.e("InterpreterHelper", "Error getting input shape: ${e.message}", e)
            null
        }
    }

    /**
     * Get model output shape (for debugging)
     */
    fun getOutputShape(): IntArray? {
        return try {
            interpreter?.getOutputTensor(0)?.shape()
        } catch (e: Exception) {
            Log.e("InterpreterHelper", "Error getting output shape: ${e.message}", e)
            null
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}

