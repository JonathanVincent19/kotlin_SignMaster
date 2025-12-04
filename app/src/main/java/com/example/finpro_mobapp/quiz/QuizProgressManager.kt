package com.example.finpro_mobapp.quiz

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

/**
 * Manager untuk menyimpan dan memuat progress quiz
 * Menggunakan SharedPreferences untuk persistent storage
 */
class QuizProgressManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("Quiz1Progress", Context.MODE_PRIVATE)
    
    /**
     * Save progress saat user exit di tengah quiz
     */
    fun saveInProgressQuiz(
        subLevelId: String,
        currentQuestionIndex: Int,
        completedQuestions: Int,
        userAnswers: List<UserAnswer>
    ) {
        val json = JSONObject().apply {
            put("subLevelId", subLevelId)
            put("currentQuestionIndex", currentQuestionIndex)
            put("completedQuestions", completedQuestions)
            put("timestamp", System.currentTimeMillis())
            
            // Save user answers
            val answersArray = JSONArray()
            userAnswers.forEach { answer ->
                val answerObj = JSONObject().apply {
                    put("questionId", answer.questionId)
                    put("userAnswer", answer.userAnswer)
                    put("correctAnswer", answer.correctAnswer)
                    put("isCorrect", answer.isCorrect)
                }
                answersArray.put(answerObj)
            }
            put("answers", answersArray)
        }
        
        prefs.edit().putString("progress_$subLevelId", json.toString()).apply()
    }
    
    /**
     * Load progress untuk sub-level tertentu
     */
    fun loadInProgressQuiz(subLevelId: String): SavedProgress? {
        val jsonString = prefs.getString("progress_$subLevelId", null) ?: return null
        
        return try {
            val json = JSONObject(jsonString)
            val answersArray = json.getJSONArray("answers")
            val answers = mutableListOf<UserAnswer>()
            
            for (i in 0 until answersArray.length()) {
                val answerObj = answersArray.getJSONObject(i)
                answers.add(
                    UserAnswer(
                        questionId = answerObj.getString("questionId"),
                        userAnswer = answerObj.getString("userAnswer"),
                        correctAnswer = answerObj.getString("correctAnswer"),
                        isCorrect = answerObj.getBoolean("isCorrect")
                    )
                )
            }
            
            SavedProgress(
                subLevelId = json.getString("subLevelId"),
                currentQuestionIndex = json.getInt("currentQuestionIndex"),
                completedQuestions = json.getInt("completedQuestions"),
                timestamp = json.getLong("timestamp"),
                userAnswers = answers
            )
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Clear progress setelah sub-level complete
     */
    fun clearProgress(subLevelId: String) {
        prefs.edit().remove("progress_$subLevelId").apply()
    }
    
    /**
     * Check if ada progress tersimpan
     */
    fun hasProgress(subLevelId: String): Boolean {
        return prefs.contains("progress_$subLevelId")
    }
    
    /**
     * Save sub-level completion status
     */
    fun saveSubLevelCompletion(
        subLevelId: String,
        completedQuestions: Int,
        totalQuestions: Int
    ) {
        prefs.edit().apply {
            putInt("${subLevelId}_completed", completedQuestions)
            putInt("${subLevelId}_total", totalQuestions)
            putBoolean("${subLevelId}_isCompleted", completedQuestions >= totalQuestions)
            apply()
        }
    }
    
    /**
     * Load sub-level completion status
     */
    fun loadSubLevelCompletion(subLevelId: String): SubLevelCompletion {
        return SubLevelCompletion(
            completedQuestions = prefs.getInt("${subLevelId}_completed", 0),
            totalQuestions = prefs.getInt("${subLevelId}_total", 0),
            isCompleted = prefs.getBoolean("${subLevelId}_isCompleted", false)
        )
    }
    
    /**
     * Save unlock status untuk sub-level
     */
    fun saveUnlockStatus(subLevelId: String, isUnlocked: Boolean) {
        prefs.edit().putBoolean("${subLevelId}_unlocked", isUnlocked).apply()
    }
    
    /**
     * Load unlock status
     */
    fun isSubLevelUnlocked(subLevelId: String): Boolean {
        // Default: 1.1 always unlocked
        return if (subLevelId == "1.1") {
            true
        } else {
            prefs.getBoolean("${subLevelId}_unlocked", false)
        }
    }
    
    /**
     * Reset all progress (for testing)
     */
    fun resetAllProgress() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Testing helper - Set unlock all levels
     * @param unlockAll true = unlock semua level, false = lock semua (kecuali 1.1)
     * 
     * Example:
     * ```
     * progressManager.setTestingMode(true)  // Unlock all
     * progressManager.setTestingMode(false) // Lock all (reset)
     * ```
     */
    fun setTestingMode(unlockAll: Boolean) {
        if (unlockAll) {
            // Map jumlah soal yang benar untuk setiap sub-level
            val questionsPerSubLevel = mapOf(
                "1.1" to 10, "1.2" to 12, "1.3" to 15,
                "2.1" to 8,  "2.2" to 10, "2.3" to 12,
                "3.1" to 6,  "3.2" to 8,  "3.3" to 10
            )
            
            // Unlock ALL sub-levels dan set completed dengan jumlah soal yang benar
            prefs.edit().apply {
                questionsPerSubLevel.forEach { (subLevelId, totalQuestions) ->
                    putBoolean("${subLevelId}_unlocked", true)
                    putInt("${subLevelId}_completed", totalQuestions)
                    putInt("${subLevelId}_total", totalQuestions)
                    putBoolean("${subLevelId}_isCompleted", true)
                }
                
                // CRITICAL: Force unlock main levels 2 dan 3
                // Karena system check previousLevel.status == COMPLETED
                // Kita perlu pastikan semua sub-level dari level 1 & 2 sudah completed
                // Ini akan trigger LevelData.updateLevelStatus() untuk set main level sebagai COMPLETED
                
                // Set flag khusus untuk testing mode (untuk bypass logic di Container)
                putBoolean("testing_mode_active", true)
                
                apply()
            }
        } else {
            // Reset all progress
            resetAllProgress()
        }
    }
    
    /**
     * Check if testing mode is active
     */
    fun isTestingModeActive(): Boolean {
        return prefs.getBoolean("testing_mode_active", false)
    }
}

/**
 * Data class untuk saved progress
 */
data class SavedProgress(
    val subLevelId: String,
    val currentQuestionIndex: Int,
    val completedQuestions: Int,
    val timestamp: Long,
    val userAnswers: List<UserAnswer>
)

/**
 * Data class untuk completion status
 */
data class SubLevelCompletion(
    val completedQuestions: Int,
    val totalQuestions: Int,
    val isCompleted: Boolean
)

