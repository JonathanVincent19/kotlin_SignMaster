package com.example.finpro_mobapp.quiz

/**
 * Question Bank untuk Quiz 2: Peragakan Isyarat
 * Soal-soal untuk latihan gerakan tangan
 */
object Quiz2QuestionBank {
    
    // Level 1: Peragakan 1 Huruf (10 soal)
    val level1_letters: List<String> = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
    )
    
    // Level 2: Peragakan 1 Kata (2 soal)
    val level2_words: List<String> = listOf(
        "SAYA",
        "NAMA"
    )
    
    // Level 3: Peragakan 2 Kata (1 soal - kalimat pendek)
    val level3_phrases: List<String> = listOf(
        "NAMA SAYA"
    )
    
    /**
     * Get questions untuk level tertentu
     */
    fun getQuestionsForLevel(level: Int): List<Quiz2Question> {
        return when(level) {
            1 -> level1_letters.mapIndexed { index, letter ->
                Quiz2Question(
                    id = "q2_l1_${index + 1}",
                    questionNumber = index + 1,
                    targetGesture = letter,
                    type = Quiz2QuestionType.SINGLE_LETTER,
                    description = "Peragakan huruf $letter dengan benar"
                )
            }
            2 -> level2_words.mapIndexed { index, word ->
                Quiz2Question(
                    id = "q2_l2_${index + 1}",
                    questionNumber = index + 1,
                    targetGesture = word,
                    type = Quiz2QuestionType.SINGLE_WORD,
                    description = "Peragakan kata $word dengan benar"
                )
            }
            3 -> level3_phrases.mapIndexed { index, phrase ->
                Quiz2Question(
                    id = "q2_l3_${index + 1}",
                    questionNumber = index + 1,
                    targetGesture = phrase,
                    type = Quiz2QuestionType.TWO_WORDS,
                    description = "Peragakan kalimat $phrase dengan benar"
                )
            }
            else -> emptyList()
        }
    }
    
    /**
     * Get all questions (untuk testing)
     */
    fun getAllQuestions(): List<Quiz2Question> {
        return getQuestionsForLevel(1) + 
               getQuestionsForLevel(2) + 
               getQuestionsForLevel(3)
    }
}

