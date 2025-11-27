package com.example.finpro_mobapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import java.util.Calendar

/**
 * Manager untuk menyimpan dan memuat statistik belajar user
 * Termasuk tracking streak belajar
 */
class StatisticsManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("UserStatistics", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_LAST_QUIZ_DATE = "last_quiz_date"
        private const val KEY_CURRENT_STREAK = "current_streak"
        private const val KEY_STREAK_START_DATE = "streak_start_date"
    }
    
    /**
     * Update streak saat user mengerjakan quiz
     * Harus dipanggil setiap kali user menyelesaikan quiz
     */
    fun updateStreak() {
        val today = getTodayInMillis()
        val lastQuizDate = prefs.getLong(KEY_LAST_QUIZ_DATE, 0L)
        val currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)
        
        when {
            // Belum pernah quiz atau streak sudah reset
            lastQuizDate == 0L -> {
                // Mulai streak baru
                prefs.edit().apply {
                    putLong(KEY_LAST_QUIZ_DATE, today)
                    putInt(KEY_CURRENT_STREAK, 1)
                    putLong(KEY_STREAK_START_DATE, today)
                    apply()
                }
            }
            // Quiz hari yang sama (tidak increment streak)
            isSameDay(lastQuizDate, today) -> {
                // Tidak perlu update, streak tetap
            }
            // Quiz hari berikutnya (increment streak)
            isNextDay(lastQuizDate, today) -> {
                val newStreak = currentStreak + 1
                prefs.edit().apply {
                    putLong(KEY_LAST_QUIZ_DATE, today)
                    putInt(KEY_CURRENT_STREAK, newStreak)
                    // Jika streak baru dimulai, update start date
                    if (currentStreak == 0) {
                        putLong(KEY_STREAK_START_DATE, today)
                    }
                    apply()
                }
            }
            // Gap lebih dari 1 hari (reset streak)
            else -> {
                // Reset streak dan mulai baru
                prefs.edit().apply {
                    putLong(KEY_LAST_QUIZ_DATE, today)
                    putInt(KEY_CURRENT_STREAK, 1)
                    putLong(KEY_STREAK_START_DATE, today)
                    apply()
                }
            }
        }
    }
    
    /**
     * Get current streak dengan auto-check reset
     * Otomatis reset jika sudah lebih dari 24 jam dari last quiz
     */
    fun getCurrentStreak(): Int {
        val lastQuizDate = prefs.getLong(KEY_LAST_QUIZ_DATE, 0L)
        val currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)
        
        // Jika belum pernah quiz
        if (lastQuizDate == 0L) {
            return 0
        }
        
        // Cek apakah sudah lebih dari 24 jam dari last quiz
        val now = System.currentTimeMillis()
        val hoursSinceLastQuiz = (now - lastQuizDate) / (1000 * 60 * 60)
        
        if (hoursSinceLastQuiz > 24) {
            // Reset streak
            prefs.edit().apply {
                putInt(KEY_CURRENT_STREAK, 0)
                apply()
            }
            return 0
        }
        
        return currentStreak
    }
    
    /**
     * Get fire icon resource berdasarkan streak days
     */
    fun getFireIconResource(streakDays: Int): Int {
        return when {
            streakDays == 0 -> R.drawable.fire_gray
            streakDays <= 2 -> R.drawable.fire_orange
            streakDays <= 4 -> R.drawable.fire_blue
            streakDays <= 6 -> R.drawable.fire_purple
            else -> R.drawable.fire_gold
        }
    }
    
    /**
     * Get color untuk teks berdasarkan streak days (mengikuti warna ikon api)
     */
    fun getStreakTextColor(streakDays: Int): Color {
        return when {
            streakDays == 0 -> Color(0xFF9E9E9E) // Gray
            streakDays <= 2 -> Color(0xFFFF6B35) // Orange
            streakDays <= 4 -> Color(0xFF2196F3) // Blue
            streakDays <= 6 -> Color(0xFF9C27B0) // Purple
            else -> Color(0xFFFFD700) // Gold
        }
    }
    
    /**
     * Get color untuk teks berdasarkan icon type (untuk testing)
     */
    fun getStreakTextColorForTesting(iconType: String): Color {
        return when (iconType.lowercase()) {
            "gray" -> Color(0xFF9E9E9E)
            "orange" -> Color(0xFFFF6B35)
            "blue" -> Color(0xFF2196F3)
            "purple" -> Color(0xFF9C27B0)
            "gold" -> Color(0xFFFFD700)
            else -> Color(0xFF9E9E9E)
        }
    }
    
    /**
     * Helper function untuk testing - force set fire icon resource
     * Gunakan untuk melihat semua ikon api yang berbeda
     */
    fun getFireIconResourceForTesting(iconType: String): Int {
        return when (iconType.lowercase()) {
            "gray" -> R.drawable.fire_gray
            "orange" -> R.drawable.fire_orange
            "blue" -> R.drawable.fire_blue
            "purple" -> R.drawable.fire_purple
            "gold" -> R.drawable.fire_gold
            else -> R.drawable.fire_gray
        }
    }
    
    /**
     * Reset all statistics (for testing)
     */
    fun resetAllStatistics() {
        prefs.edit().clear().apply()
    }
    
    // Helper functions
    private fun getTodayInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun isSameDay(millis1: Long, millis2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = millis1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = millis2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
    
    private fun isNextDay(millis1: Long, millis2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = millis1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = millis2 }
        cal1.add(Calendar.DAY_OF_YEAR, 1)
        return isSameDay(cal1.timeInMillis, millis2)
    }
}

