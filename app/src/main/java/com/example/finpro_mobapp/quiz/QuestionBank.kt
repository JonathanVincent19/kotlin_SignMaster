package com.example.finpro_mobapp.quiz

/**
 * Question Bank untuk Quiz 1
 * Hanya perlu define string jawaban, system akan auto-generate image sequence
 */
object QuestionBank {
    
    // ========== LEVEL 1: TEBAK 1 HURUF ==========
    
    // 1.1 Lambat - 10 soal, huruf A-J
    val level1_1: List<String> = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
    )
    
    // 1.2 Sedang - 12 soal, huruf A-P (random)
    val level1_2: List<String> = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"
    ).shuffled().take(12)
    
    // 1.3 Cepat - 15 soal, huruf A-Z (random)
    val level1_3: List<String> = ('A'..'Z').map { it.toString() }.shuffled().take(15)
    
    // ========== LEVEL 2: TEBAK 1 KATA ==========
    
    // 2.1 Lambat - 8 soal, kata 3-4 huruf
    val level2_1: List<String> = listOf(
        "SAYA",  // 4 huruf
        "NAMA",  // 4 huruf
        "BUKU",  // 4 huruf
        "MEJA",  // 4 huruf
        "KAKI",  // 4 huruf
        "MATA",  // 4 huruf
        "TOPI",  // 4 huruf
        "BAJU"   // 4 huruf
    )
    
    // 2.2 Sedang - 10 soal, kata 4-5 huruf
    val level2_2: List<String> = listOf(
        "RUMAH",  // 5 huruf
        "MAKAN",  // 5 huruf
        "MINUM",  // 5 huruf
        "KERJA",  // 5 huruf
        "TEMAN",  // 5 huruf
        "BAIK",   // 4 huruf
        "BESAR",  // 5 huruf
        "KECIL",  // 5 huruf
        "MALAM",  // 5 huruf
        "SIANG"   // 5 huruf
    )
    
    // 2.3 Cepat - 12 soal, kata 5-6 huruf
    val level2_3: List<String> = listOf(
        "BELAJAR", // 7 huruf - too long, but okay
        "SEKOLAH", // 7 huruf
        "SENANG",  // 6 huruf
        "SEDIH",   // 5 huruf
        "GEMBIRA", // 7 huruf
        "KELUARGA",// 8 huruf - challenging!
        "BERSAMA", // 7 huruf
        "DATANG",  // 6 huruf
        "PULANG",  // 6 huruf
        "TENANG",  // 6 huruf
        "MARAH",   // 5 huruf
        "SAYANG"   // 6 huruf
    )
    
    // ========== LEVEL 3: TEBAK 2 KATA ==========
    
    // 3.1 Lambat - 6 soal, 2 kata pendek
    val level3_1: List<String> = listOf(
        "NAMA SAYA",     // 4+4 = 8 huruf
        "SAYA BAIK",     // 4+4 = 8 huruf
        "BUKU INI",      // 4+3 = 7 huruf
        "AKU MAKAN",     // 3+5 = 8 huruf
        "TEMAN BAIK",    // 5+4 = 9 huruf
        "RUMAH BESAR"    // 5+5 = 10 huruf
    )
    
    // 3.2 Sedang - 8 soal, 2 kata sedang
    val level3_2: List<String> = listOf(
        "SAYA BELAJAR",   // 4+7 = 11 huruf
        "NAMA TEMAN",     // 4+5 = 9 huruf
        "PERGI SEKOLAH",  // 5+7 = 12 huruf
        "PULANG RUMAH",   // 6+5 = 11 huruf
        "MAKAN SIANG",    // 5+5 = 10 huruf
        "KERJA KERAS",    // 5+5 = 10 huruf
        "BANGUN PAGI",    // 6+4 = 10 huruf
        "TIDUR MALAM"     // 5+5 = 10 huruf
    )
    
    // 3.3 Cepat - 10 soal, 2 kata panjang
    val level3_3: List<String> = listOf(
        "BELAJAR BERSAMA",    // 7+7 = 14 huruf
        "KELUARGA BAHAGIA",   // 8+7 = 15 huruf
        "SENANG BERTEMU",     // 6+7 = 13 huruf
        "SEKOLAH FAVORIT",    // 7+7 = 14 huruf
        "SAHABAT SEJATI",     // 7+6 = 13 huruf
        "BERBAGI CERITA",     // 7+6 = 13 huruf
        "SEMANGAT BELAJAR",   // 8+7 = 15 huruf
        "GEMBIRA SELALU",     // 7+6 = 13 huruf
        "DATANG BERSAMA",     // 6+7 = 13 huruf
        "PULANG SENANG"       // 6+6 = 12 huruf
    )
    
    /**
     * Get questions for specific sub-level
     */
    fun getQuestionsForSubLevel(subLevelId: String): List<String> {
        return when(subLevelId) {
            "1.1" -> level1_1
            "1.2" -> level1_2
            "1.3" -> level1_3
            "2.1" -> level2_1
            "2.2" -> level2_2
            "2.3" -> level2_3
            "3.1" -> level3_1
            "3.2" -> level3_2
            "3.3" -> level3_3
            else -> emptyList()
        }
    }
}

