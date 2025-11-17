package com.example.finpro_mobapp.quiz

/**
 * Initial configuration untuk semua levels
 * Nanti bisa diganti dengan data dari database/SharedPreferences
 */
object LevelData {
    
    fun getInitialLevels(): List<MainLevel> {
        return listOf(
            // Level 1: Tebak 1 Huruf
            MainLevel(
                id = 1,
                name = "Tebak 1 Huruf",
                icon = "🟢",
                type = LevelType.SINGLE_LETTER,
                subLevels = listOf(
                    SubLevel(
                        id = "1.1",
                        parentLevelId = 1,
                        name = "LAMBAT",
                        icon = "🐢",
                        speedType = SpeedType.SLOW,
                        displayDuration = 5000,  // 5 detik
                        totalQuestions = 10,
                        completedQuestions = 0,
                        status = SubLevelStatus.NOT_STARTED,
                        isUnlocked = true  // First sub-level unlocked
                    ),
                    SubLevel(
                        id = "1.2",
                        parentLevelId = 1,
                        name = "SEDANG",
                        icon = "🐇",
                        speedType = SpeedType.MEDIUM,
                        displayDuration = 3000,  // 3 detik
                        totalQuestions = 12,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    ),
                    SubLevel(
                        id = "1.3",
                        parentLevelId = 1,
                        name = "CEPAT",
                        icon = "⚡",
                        speedType = SpeedType.FAST,
                        displayDuration = 1500,  // 1.5 detik
                        totalQuestions = 15,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    )
                ),
                status = LevelStatus.ON_PROGRESS,
                isUnlocked = true
            ),
            
            // Level 2: Tebak 1 Kata
            MainLevel(
                id = 2,
                name = "Tebak 1 Kata",
                icon = "🟡",
                type = LevelType.SINGLE_WORD,
                subLevels = listOf(
                    SubLevel(
                        id = "2.1",
                        parentLevelId = 2,
                        name = "LAMBAT",
                        icon = "🐢",
                        speedType = SpeedType.SLOW,
                        displayDuration = 3000,  // 3 detik per huruf
                        totalQuestions = 8,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    ),
                    SubLevel(
                        id = "2.2",
                        parentLevelId = 2,
                        name = "SEDANG",
                        icon = "🐇",
                        speedType = SpeedType.MEDIUM,
                        displayDuration = 2000,  // 2 detik per huruf
                        totalQuestions = 10,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    ),
                    SubLevel(
                        id = "2.3",
                        parentLevelId = 2,
                        name = "CEPAT",
                        icon = "⚡",
                        speedType = SpeedType.FAST,
                        displayDuration = 1000,  // 1 detik per huruf
                        totalQuestions = 12,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    )
                ),
                status = LevelStatus.LOCKED,
                isUnlocked = false
            ),
            
            // Level 3: Tebak 2 Kata
            MainLevel(
                id = 3,
                name = "Tebak 2 Kata",
                icon = "🔴",
                type = LevelType.TWO_WORDS,
                subLevels = listOf(
                    SubLevel(
                        id = "3.1",
                        parentLevelId = 3,
                        name = "LAMBAT",
                        icon = "🐢",
                        speedType = SpeedType.SLOW,
                        displayDuration = 3000,  // 3 detik per huruf
                        totalQuestions = 6,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    ),
                    SubLevel(
                        id = "3.2",
                        parentLevelId = 3,
                        name = "SEDANG",
                        icon = "🐇",
                        speedType = SpeedType.MEDIUM,
                        displayDuration = 2000,  // 2 detik per huruf
                        totalQuestions = 8,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    ),
                    SubLevel(
                        id = "3.3",
                        parentLevelId = 3,
                        name = "CEPAT",
                        icon = "⚡",
                        speedType = SpeedType.FAST,
                        displayDuration = 1000,  // 1 detik per huruf
                        totalQuestions = 10,
                        completedQuestions = 0,
                        status = SubLevelStatus.LOCKED,
                        isUnlocked = false
                    )
                ),
                status = LevelStatus.LOCKED,
                isUnlocked = false
            )
        )
    }
    
    /**
     * Helper function untuk update status level berdasarkan sub-level completion
     */
    fun updateLevelStatus(level: MainLevel) {
        val allCompleted = level.subLevels.all { it.status == SubLevelStatus.COMPLETED }
        val anyStarted = level.subLevels.any { 
            it.status == SubLevelStatus.ON_PROGRESS || it.status == SubLevelStatus.COMPLETED 
        }
        
        level.status = when {
            allCompleted -> LevelStatus.COMPLETED
            anyStarted -> LevelStatus.ON_PROGRESS
            else -> if (level.isUnlocked) LevelStatus.ON_PROGRESS else LevelStatus.LOCKED
        }
    }
    
    /**
     * Helper function untuk unlock sub-level berikutnya
     */
    fun unlockNextSubLevel(levels: List<MainLevel>, completedSubLevelId: String) {
        // Find current sub-level
        levels.forEach { level ->
            val currentIndex = level.subLevels.indexOfFirst { it.id == completedSubLevelId }
            if (currentIndex != -1) {
                // Unlock next sub-level in same level
                if (currentIndex + 1 < level.subLevels.size) {
                    level.subLevels[currentIndex + 1].isUnlocked = true
                    level.subLevels[currentIndex + 1].status = SubLevelStatus.NOT_STARTED
                }
                
                // Check if all sub-levels completed, then unlock next main level
                if (level.subLevels.all { it.status == SubLevelStatus.COMPLETED }) {
                    val nextLevel = levels.find { it.id == level.id + 1 }
                    nextLevel?.let {
                        it.isUnlocked = true
                        it.status = LevelStatus.ON_PROGRESS
                        it.subLevels.firstOrNull()?.let { firstSub ->
                            firstSub.isUnlocked = true
                            firstSub.status = SubLevelStatus.NOT_STARTED
                        }
                    }
                }
                
                updateLevelStatus(level)
            }
        }
    }
}

