package com.example.finpro_mobapp.quiz

import android.util.Log

/**
 * Backend validator untuk menilai apakah gerakan tangan benar atau tidak
 * 
 * Fitur:
 * - Confidence threshold validation
 * - Exact matching untuk huruf tunggal
 * - Fuzzy matching untuk kata-kata dan kalimat
 * - Case-insensitive comparison
 * - Whitespace normalization
 */
object GestureValidator {
    
    // Confidence threshold minimum untuk validasi
    private const val MIN_CONFIDENCE_THRESHOLD = 0.5f  // 50% minimum
    
    // Confidence threshold untuk kata-kata (lebih rendah karena lebih sulit)
    private const val MIN_CONFIDENCE_WORD_THRESHOLD = 0.4f  // 40% untuk kata
    
    // Confidence threshold untuk kalimat (paling rendah)
    private const val MIN_CONFIDENCE_SENTENCE_THRESHOLD = 0.35f  // 35% untuk kalimat
    
    /**
     * Validate gesture recognition result
     * 
     * @param recognizedGesture Gesture yang terdeteksi AI
     * @param targetGesture Jawaban yang benar
     * @param confidence Confidence score dari AI (0.0 - 1.0)
     * @param questionType Tipe pertanyaan (huruf/kata/kalimat)
     * @return ValidationResult dengan isCorrect dan detail
     */
    fun validate(
        recognizedGesture: String,
        targetGesture: String,
        confidence: Float,
        questionType: Quiz2QuestionType
    ): ValidationResult {
        // Check confidence threshold
        val minThreshold = when (questionType) {
            Quiz2QuestionType.SINGLE_LETTER -> MIN_CONFIDENCE_THRESHOLD
            Quiz2QuestionType.SINGLE_WORD -> MIN_CONFIDENCE_WORD_THRESHOLD
            Quiz2QuestionType.TWO_WORDS -> MIN_CONFIDENCE_SENTENCE_THRESHOLD
        }
        
        if (confidence < minThreshold) {
            Log.d("GestureValidator", "❌ Confidence too low: $confidence < $minThreshold")
            return ValidationResult(
                isCorrect = false,
                confidence = confidence,
                reason = ValidationReason.CONFIDENCE_TOO_LOW,
                matchedText = recognizedGesture
            )
        }
        
        // Normalize strings (trim, uppercase, remove extra spaces)
        val normalizedRecognized = normalizeText(recognizedGesture)
        val normalizedTarget = normalizeText(targetGesture)
        
        Log.d("GestureValidator", "Comparing: '$normalizedRecognized' vs '$normalizedTarget' (confidence: ${confidence * 100}%)")
        
        // Exact match check
        if (normalizedRecognized == normalizedTarget) {
            Log.d("GestureValidator", "✅ Exact match!")
            return ValidationResult(
                isCorrect = true,
                confidence = confidence,
                reason = ValidationReason.EXACT_MATCH,
                matchedText = normalizedRecognized
            )
        }
        
        // Fuzzy match for words and sentences
        when (questionType) {
            Quiz2QuestionType.SINGLE_LETTER -> {
                // For single letters, only exact match is accepted
                Log.d("GestureValidator", "❌ Single letter mismatch")
                return ValidationResult(
                    isCorrect = false,
                    confidence = confidence,
                    reason = ValidationReason.MISMATCH,
                    matchedText = normalizedRecognized
                )
            }
            
            Quiz2QuestionType.SINGLE_WORD,
            Quiz2QuestionType.TWO_WORDS -> {
                // Fuzzy matching for words and sentences
                val similarity = calculateSimilarity(normalizedRecognized, normalizedTarget)
                val similarityThreshold = 0.7f  // 70% similarity required
                
                if (similarity >= similarityThreshold) {
                    Log.d("GestureValidator", "✅ Fuzzy match! Similarity: ${similarity * 100}%")
                    return ValidationResult(
                        isCorrect = true,
                        confidence = confidence,
                        reason = ValidationReason.FUZZY_MATCH,
                        matchedText = normalizedRecognized,
                        similarity = similarity
                    )
                } else {
                    Log.d("GestureValidator", "❌ Similarity too low: ${similarity * 100}% < ${similarityThreshold * 100}%")
                    return ValidationResult(
                        isCorrect = false,
                        confidence = confidence,
                        reason = ValidationReason.MISMATCH,
                        matchedText = normalizedRecognized,
                        similarity = similarity
                    )
                }
            }
        }
    }
    
    /**
     * Normalize text for comparison
     * - Trim whitespace
     * - Convert to uppercase
     * - Remove extra spaces
     */
    private fun normalizeText(text: String): String {
        return text
            .trim()
            .uppercase()
            .replace(Regex("\\s+"), " ")  // Replace multiple spaces with single space
    }
    
    /**
     * Calculate similarity between two strings (Levenshtein distance based)
     * Returns value between 0.0 (no similarity) and 1.0 (identical)
     */
    private fun calculateSimilarity(str1: String, str2: String): Float {
        if (str1 == str2) return 1.0f
        if (str1.isEmpty() || str2.isEmpty()) return 0.0f
        
        val maxLen = maxOf(str1.length, str2.length)
        val distance = levenshteinDistance(str1, str2)
        
        return 1.0f - (distance.toFloat() / maxLen.toFloat())
    }
    
    /**
     * Calculate Levenshtein distance between two strings
     */
    private fun levenshteinDistance(str1: String, str2: String): Int {
        val len1 = str1.length
        val len2 = str2.length
        
        val dp = Array(len1 + 1) { IntArray(len2 + 1) }
        
        // Initialize base cases
        for (i in 0..len1) dp[i][0] = i
        for (j in 0..len2) dp[0][j] = j
        
        // Fill dp array
        for (i in 1..len1) {
            for (j in 1..len2) {
                val cost = if (str1[i - 1] == str2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // Deletion
                    dp[i][j - 1] + 1,      // Insertion
                    dp[i - 1][j - 1] + cost // Substitution
                )
            }
        }
        
        return dp[len1][len2]
    }
}

/**
 * Result dari validasi gesture
 */
data class ValidationResult(
    val isCorrect: Boolean,
    val confidence: Float,
    val reason: ValidationReason,
    val matchedText: String,
    val similarity: Float? = null
) {
    val confidencePercentage: Int
        get() = (confidence * 100).toInt()
    
    val similarityPercentage: Int?
        get() = similarity?.let { (it * 100).toInt() }
}

/**
 * Alasan validasi
 */
enum class ValidationReason {
    EXACT_MATCH,          // Exact match dengan target
    FUZZY_MATCH,          // Fuzzy match (similarity tinggi)
    CONFIDENCE_TOO_LOW,   // Confidence terlalu rendah
    MISMATCH              // Tidak match dengan target
}

