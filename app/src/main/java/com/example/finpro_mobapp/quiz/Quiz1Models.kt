package com.example.finpro_mobapp.quiz

import com.example.finpro_mobapp.R

// Enums
enum class LevelType {
    SINGLE_LETTER,    // Level 1
    SINGLE_WORD,      // Level 2
    TWO_WORDS         // Level 3
}

enum class LevelStatus {
    COMPLETED,        // ⭐⭐⭐ Selesai
    ON_PROGRESS,      // ⏱️ On Progress
    LOCKED            // 🔒
}

enum class SubLevelStatus {
    COMPLETED,        // ✅
    ON_PROGRESS,      // ⏱️
    NOT_STARTED,      // unlocked but not started
    LOCKED            // 🔒
}

enum class SpeedType {
    SLOW,      // 🐢
    MEDIUM,    // 🐇
    FAST       // ⚡
}

// Main Level Data
data class MainLevel(
    val id: Int,                    // 1, 2, 3
    val name: String,               // "Tebak 1 Huruf"
    val icon: String,               // "🟢", "🟡", "🔴"
    val type: LevelType,
    val subLevels: List<SubLevel>,
    var status: LevelStatus,
    var isUnlocked: Boolean
)

// Sub Level Data
data class SubLevel(
    val id: String,              // "1.1", "2.2", etc
    val parentLevelId: Int,      // 1, 2, or 3
    val name: String,            // "LAMBAT", "SEDANG", "CEPAT"
    val icon: String,            // "🐢", "🐇", "⚡"
    val speedType: SpeedType,
    val displayDuration: Int,    // milliseconds per letter
    val totalQuestions: Int,     // total questions in this sub-level
    var completedQuestions: Int = 0, // how many completed
    var status: SubLevelStatus,
    var isUnlocked: Boolean
)

// Question Data
data class Question(
    val id: String,
    val subLevelId: String,
    val questionNumber: Int,
    val answer: String,          // "M" or "SAYA" or "NAMA SAYA"
    val type: LevelType
) {
    // Auto-generate letter sequence and image sequence
    val letters: List<Char>
        get() = answer.uppercase().filter { it.isLetter() }.toList()
    
    val imageSequence: List<Int>
        get() = letters.map { getImageResIdForLetter(it) }
    
    val hasPause: Boolean
        get() = answer.contains(" ")
    
    val pauseIndices: List<Int>
        get() {
            val indices = mutableListOf<Int>()
            var count = 0
            answer.forEachIndexed { index, char ->
                if (char == ' ') {
                    indices.add(count)
                } else if (char.isLetter()) {
                    count++
                }
            }
            return indices
        }
    
    private fun getImageResIdForLetter(letter: Char): Int {
        // TODO: Nanti ganti dengan gambar asli
        // Untuk sekarang pakai placeholder
        return when(letter.uppercaseChar()) {
            // Saat gambar sudah ada, uncomment ini:
            // 'A' -> R.drawable.bisindo_a
            // 'B' -> R.drawable.bisindo_b
            // ... dst sampai Z
            else -> R.drawable.ic_launcher_foreground // Placeholder
        }
    }
}

// Quiz Session (runtime state)
data class QuizSession(
    val subLevelId: String,
    val questions: List<Question>,
    var currentQuestionIndex: Int = 0,
    val userAnswers: MutableList<UserAnswer> = mutableListOf(),
    var isSequenceDisplaying: Boolean = false,
    var currentLetterIndex: Int = 0,
    val startTime: Long = System.currentTimeMillis()
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)
    
    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= questions.size - 1
    
    val completedCount: Int
        get() = userAnswers.count { it.isCorrect }
    
    val totalQuestions: Int
        get() = questions.size
}

// User Answer
data class UserAnswer(
    val questionId: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val attempts: Int = 1,
    val timeSpent: Long = 0
)

