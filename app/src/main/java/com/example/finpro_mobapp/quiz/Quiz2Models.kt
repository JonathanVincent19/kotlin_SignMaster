package com.example.finpro_mobapp.quiz

/**
 * Data models untuk Quiz 2: Peragakan Isyarat (Camera)
 */

// Quiz 2 Question Type
enum class Quiz2QuestionType {
    SINGLE_LETTER,    // Peragakan 1 huruf
    SINGLE_WORD,      // Peragakan 1 kata
    TWO_WORDS         // Peragakan 2 kata
}

// Quiz 2 Game State
enum class Quiz2GameState {
    SHOWING_TARGET,      // Menampilkan target yang harus diperagakan
    RECORDING,           // Sedang merekam gerakan
    PROCESSING,          // Sedang memproses dengan AI
    FEEDBACK_CORRECT,    // Feedback benar
    FEEDBACK_WRONG,      // Feedback salah
    COMPLETED            // Quiz selesai
}

// Quiz 2 Question
data class Quiz2Question(
    val id: String,
    val questionNumber: Int,
    val targetGesture: String,        // "A" atau "SAYA" atau "NAMA SAYA"
    val type: Quiz2QuestionType,
    val description: String = ""       // Deskripsi gerakan (optional)
) {
    val displayText: String
        get() = when(type) {
            Quiz2QuestionType.SINGLE_LETTER -> "Peragakan huruf: $targetGesture"
            Quiz2QuestionType.SINGLE_WORD -> "Peragakan kata: $targetGesture"
            Quiz2QuestionType.TWO_WORDS -> "Peragakan kalimat: $targetGesture"
        }
}

// Quiz 2 Answer (hasil dari AI recognition)
data class Quiz2Answer(
    val questionId: String,
    val recognizedGesture: String,     // Hasil dari AI model
    val confidence: Float,              // Confidence score (0.0 - 1.0)
    val correctAnswer: String,         // Jawaban yang benar
    val isCorrect: Boolean,
    val attempts: Int = 1,
    val timeSpent: Long = 0            // Waktu yang dihabiskan (ms)
)

// Quiz 2 Session
data class Quiz2Session(
    val questions: List<Quiz2Question>,
    var currentQuestionIndex: Int = 0,
    val userAnswers: MutableList<Quiz2Answer> = mutableListOf(),
    val startTime: Long = System.currentTimeMillis()
) {
    val currentQuestion: Quiz2Question?
        get() = questions.getOrNull(currentQuestionIndex)
    
    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= questions.size - 1
    
    val completedCount: Int
        get() = userAnswers.count { it.isCorrect }
    
    val totalQuestions: Int
        get() = questions.size
}

// AI Model Recognition Result (placeholder untuk model Anda nanti)
data class RecognitionResult(
    val gesture: String,           // Huruf/kata yang terdeteksi
    val confidence: Float,         // Confidence score
    val processingTime: Long = 0   // Waktu processing (ms)
)

