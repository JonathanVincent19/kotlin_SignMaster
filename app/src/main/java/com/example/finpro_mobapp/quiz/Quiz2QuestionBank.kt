package com.example.finpro_mobapp.quiz

/**
 * Question Bank untuk Quiz 2: Peragakan Isyarat
 * Soal-soal untuk latihan gerakan tangan
 */
object Quiz2QuestionBank {
    
    // Level 1: Peragakan 1 Huruf (20 soal)
    val level1_letters: List<String> = ('A'..'Z').map { it.toString() }.shuffled().take(20)
    
    // Level 2: Peragakan 1 Kata (15 soal - 5-7 huruf yang agak susah)
    val level2_words: List<String> = listOf(
        "BELAJAR",   // 7 huruf
        "KELUARGA",  // 7 huruf
        "MENGAJAR",  // 7 huruf
        "BERMAIN",   // 7 huruf
        "MAKANAN",   // 7 huruf
        "MINUMAN",   // 7 huruf
        "SEKOLAH",   // 7 huruf
        "LAPANGAN",  // 7 huruf
        "PINTU",     // 5 huruf
        "KAMAR",     // 5 huruf
        "PENSIL",    // 6 huruf
        "KUNING",    // 6 huruf
        "HIJAU",     // 5 huruf
        "MERAH",     // 5 huruf
        "BESAR",      // 5 huruf
        "QURBAN",
        "VAKSIN",
        "WANITA",
        "XENON",
        "EXIT",
        "ZEBRA"
    ).map { it.toString() }.shuffled().take(15)
    
    // Level 3: Peragakan 2 Kata (12 soal - kalimat yang lebih susah)
    val level3_phrases: List<String> = listOf(
        "SELAMAT PAGI",
        "SELAMAT SIANG",
        "SELAMAT MALAM",
        "TERIMA KASIH",
        "SAMA SAMA",
        "KEMBALI LAGI",
        "HARI INI",
        "BESOK LAGI",
        "SENANG SEKALI",
        "MAKASIH BANYAK",
        "SAYA SUKA",
        "KAMU BAIK",
        "QUOTA KELAS",
        "VIDEO VIRAL",
        "WARNA BARU",
        "WAKTU BELAJAR",
        "ZAT BESI",
        "ZONA HUJAN",
        "BOX KECIL",
        "EXTRA PEDAS"
    ).map { it.toString() }.shuffled().take(12)
    
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

