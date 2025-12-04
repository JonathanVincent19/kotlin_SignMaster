package com.example.finpro_mobapp.quiz

import android.content.Context
import android.content.SharedPreferences

/**
 * Manager untuk menyimpan dan memuat progress Quiz 2
 * Menggunakan SharedPreferences untuk persistent storage
 */
class Quiz2ProgressManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("Quiz2Progress", Context.MODE_PRIVATE)
    
    /**
     * Save level completion status
     */
    fun saveLevelCompletion(
        level: Int,
        completedQuestions: Int,
        totalQuestions: Int
    ) {
        prefs.edit().apply {
            putInt("level_${level}_completed", completedQuestions)
            putInt("level_${level}_total", totalQuestions)
            putBoolean("level_${level}_isCompleted", completedQuestions >= totalQuestions)
            apply()
        }
    }
    
    /**
     * Load level completion status
     */
    fun loadLevelCompletion(level: Int): Quiz2LevelCompletion {
        return Quiz2LevelCompletion(
            completedQuestions = prefs.getInt("level_${level}_completed", 0),
            totalQuestions = prefs.getInt("level_${level}_total", 0),
            isCompleted = prefs.getBoolean("level_${level}_isCompleted", false)
        )
    }
    
    /**
     * Save unlock status untuk level
     */
    fun saveUnlockStatus(level: Int, isUnlocked: Boolean) {
        prefs.edit().putBoolean("level_${level}_unlocked", isUnlocked).apply()
    }
    
    /**
     * Check if level is unlocked
     */
    fun isLevelUnlocked(level: Int): Boolean {
        // Level 1 always unlocked
        if (level == 1) return true
        
        // Check if saved unlock status
        return prefs.getBoolean("level_${level}_unlocked", false)
    }
    
    /**
     * Unlock next level after completing current level
     */
    fun unlockNextLevel(currentLevel: Int) {
        if (currentLevel < 3) {
            saveUnlockStatus(currentLevel + 1, true)
        }
    }
    
    /**
     * Check if level is completed
     */
    fun isLevelCompleted(level: Int): Boolean {
        return prefs.getBoolean("level_${level}_isCompleted", false)
    }
    
    /**
     * Reset all progress (for testing)
     */
    fun resetAllProgress() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Testing helper - Set unlock all levels
     * @param unlockAll true = unlock semua level, false = lock semua (kecuali level 1)
     * 
     * Example:
     * ```
     * progressManager.setTestingMode(true)  // Unlock all
     * progressManager.setTestingMode(false) // Lock all (reset)
     * ```
     */
    fun setTestingMode(unlockAll: Boolean) {
        if (unlockAll) {
            // Unlock all levels (1, 2, 3)
            prefs.edit().apply {
                for (level in 1..3) {
                    putBoolean("level_${level}_unlocked", true)
                    
                    val totalQuestions = when(level) {
                        1 -> 20
                        2 -> 15
                        3 -> 12
                        else -> 10
                    }
                    putInt("level_${level}_completed", totalQuestions)
                    putInt("level_${level}_total", totalQuestions)
                    putBoolean("level_${level}_isCompleted", true)
                }
                apply()
            }
        } else {
            // Reset all progress
            resetAllProgress()
        }
    }
}

/**
 * Data class untuk completion status Quiz 2
 */
data class Quiz2LevelCompletion(
    val completedQuestions: Int,
    val totalQuestions: Int,
    val isCompleted: Boolean
)

