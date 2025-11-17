# 📂 STRUKTUR FILE PROJECT - FINPRO MOBAPP

Dokumentasi lengkap struktur file setelah implementasi Quiz 1.

---

## 🗂️ **STRUCTURE OVERVIEW**

```
app/src/main/java/com/example/finpro_mobapp/
│
├── 🎯 CORE FILES
│   ├── MainActivity.kt              (138 lines) - Entry point & navigation
│   ├── AlphabetModels.kt           (11 lines)  - Data model untuk Dictionary
│   └── ui/
│       └── theme/                   - Theme files
│
├── 📱 SCREEN FILES
│   ├── SplashScreen.kt             (125 lines) - Splash screen
│   ├── HomeScreen.kt               (557 lines) - Home page (updated!)
│   ├── DictionaryScreen.kt         (298 lines) - Kamus BISINDO A-Z
│   └── QuizScreen.kt               (45 lines)  - Quiz router
│
└── 🎮 QUIZ SYSTEM
    └── quiz/
        ├── Quiz1Models.kt          (134 lines) - Data classes, enums
        ├── QuestionBank.kt         (125 lines) - 91+ soal lengkap
        ├── LevelData.kt            (150 lines) - Level config & logic
        ├── QuizSelectionScreen.kt  (155 lines) - Screen 1: Pilih quiz
        ├── Quiz1MainLevelScreen.kt (160 lines) - Screen 2: Main levels
        ├── Quiz1SubLevelScreen.kt  (175 lines) - Screen 3: Sub-levels
        ├── Quiz1GameScreen.kt      (220 lines) - Screen 4-7: Gameplay
        ├── Quiz1FinalScreen.kt     (140 lines) - Screen 8: Final score
        └── Quiz1Container.kt       (120 lines) - Navigation container
```

**Total:** 15 files, ~2,400 lines of code

---

## 📊 **FILE CATEGORIES**

### **🎯 Core (3 files)**
- MainActivity.kt - App entry, drawer navigation
- AlphabetModels.kt - Dictionary data model
- Theme files

### **📱 Screens (4 files)**
- SplashScreen.kt - Splash animation
- HomeScreen.kt - Homepage with stats, tips, banner
- DictionaryScreen.kt - Alphabet A-Z dictionary
- QuizScreen.kt - Quiz type router

### **🎮 Quiz System (9 files)**
Organized dalam package `quiz/`
- **Models**: Data structures
- **Data**: Questions & levels
- **Screens**: All quiz UI screens
- **Container**: Navigation logic

---

## 🔍 **FILE DETAILS & RESPONSIBILITIES**

### **MainActivity.kt**
```kotlin
Tanggung Jawab:
- Setup Activity
- Manage splash screen
- Navigation drawer (Beranda, Alfabet, Latihan)
- Screen routing (HOME, DICTIONARY, QUIZ)

Key Components:
- MainActivity class
- Screen enum
- AppNavigation() composable
```

### **HomeScreen.kt** ⭐ NEW & IMPROVED
```kotlin
Tanggung Jawab:
- Homepage UI
- Hero banner dengan CTA
- Quick access cards (Quiz, Kamus)
- Statistics section (placeholder)
- Daily tips (25 quotes, auto-rotate)
- Donation banner

Key Components:
- HomeScreen()
- HeroBanner()
- QuickAccessCard()
- StatisticsSection()
- DailyTipSection()
- DonationBanner()
- getDailyTip() - smart rotation

Features:
- Gradient background
- Modern card design
- Daily rotating tips
- Navigation callbacks
```

### **DictionaryScreen.kt**
```kotlin
Tanggung Jawab:
- Display alphabet A-Z grid
- Letter detail modal
- Each letter has unique description

Key Components:
- DictionaryScreen()
- AlphabetCard()
- LetterDetailDialog()

Features:
- 26 unique descriptions
- Placeholder untuk 26 gambar
- Modal popup on click
```

### **QuizScreen.kt**
```kotlin
Tanggung Jawab:
- Route ke Quiz 1 atau Quiz 2
- Manage quiz navigation state

Key Components:
- QuizScreen()
- QuizScreenState enum
```

---

### **quiz/Quiz1Models.kt**
```kotlin
Tanggung Jawab:
- Define semua data structures
- Auto-generate image sequence dari string
- Enums untuk status & types

Key Classes:
- MainLevel
- SubLevel
- Question (with auto image mapping!)
- QuizSession
- UserAnswer

Key Enums:
- LevelType, LevelStatus, SubLevelStatus, SpeedType

Smart Features:
- Auto parse "SAYA" → ['S','A','Y','A']
- Auto map huruf → gambar resource
- Auto detect pause untuk spasi
```

### **quiz/QuestionBank.kt**
```kotlin
Tanggung Jawab:
- Bank soal untuk semua levels
- 91+ questions ready

Data:
- level1_1 to level1_3 (huruf)
- level2_1 to level2_3 (kata)
- level3_1 to level3_3 (2 kata)

Function:
- getQuestionsForSubLevel(id) → return questions
```

### **quiz/LevelData.kt**
```kotlin
Tanggung Jawab:
- Initial level configuration
- Unlock logic
- Status update logic

Functions:
- getInitialLevels() → 3 main levels
- updateLevelStatus() → auto update
- unlockNextSubLevel() → progression
```

### **quiz/QuizSelectionScreen.kt**
```kotlin
Tanggung Jawab:
- Screen pilih Quiz 1 atau Quiz 2
- Top bar dengan menu

Components:
- QuizSelectionScreen()
- QuizCard()

UI:
- 2 cards untuk 2 quiz types
- Quiz 2 disabled (coming soon)
```

### **quiz/Quiz1MainLevelScreen.kt**
```kotlin
Tanggung Jawab:
- Display 3 main levels
- Show status (⭐⭐⭐ / ⏱️ / 🔒)
- Navigate to sub-level selection

Components:
- Quiz1MainLevelScreen()
- MainLevelCard()

Logic:
- Only unlocked levels clickable
- Status dynamic based on progress
```

### **quiz/Quiz1SubLevelScreen.kt**
```kotlin
Tanggung Jawab:
- Display 3 sub-levels per main level
- Show questions progress
- Show status (✅ / ⏱️ / 🔒)
- Different buttons based on status

Components:
- Quiz1SubLevelScreen()
- SubLevelCard()

Features:
- "Mulai" for new
- "Lanjutkan" for in progress
- "Main Lagi" for completed
- No button for locked
```

### **quiz/Quiz1GameScreen.kt** ⭐ CORE GAMEPLAY
```kotlin
Tanggung Jawab:
- Main game logic
- Display letter sequence
- Handle user input
- Feedback screens
- Retry mechanism

Components:
- Quiz1GameScreen() - main coordinator
- DisplayingSequenceScreen() - show images
- WaitingInputScreen() - input & replay
- FeedbackCorrectScreen() - success feedback
- FeedbackWrongScreen() - retry prompt

State Management:
- GameState enum
- LaunchedEffect untuk sequence
- Coroutines untuk timing

Features:
- Auto display sequence
- Auto transition states
- Replay functionality
- Clean minimal UI
```

### **quiz/Quiz1FinalScreen.kt**
```kotlin
Tanggung Jawab:
- Show completion celebration
- Display final score (X/Y format)
- Show unlock notifications
- Navigation options

Components:
- Quiz1FinalScreen()

Features:
- Celebration message
- Score card
- 3 navigation buttons
- Dynamic unlock messages
```

### **quiz/Quiz1Container.kt** ⭐ NAVIGATION HUB
```kotlin
Tanggung Jawab:
- Manage all Quiz 1 navigation
- State management untuk screens
- Coordinate screen transitions
- Update progress

State Management:
- currentScreen (Main/Sub/Game/Final)
- levels (all level data)
- selectedLevel, selectedSubLevel
- gameQuestions, completedCount

Functions:
- updateSubLevelProgress()
- getNextSubLevelId()

Flow Control:
- Screen transitions
- Data passing between screens
- Progress persistence (in-memory for now)
```

---

## 🎯 **NAVIGATION FLOW CHART**

```
MainActivity
    ├─ SplashScreen (5s)
    └─ AppNavigation
        ├─ HomeScreen
        │   ├─ Click "Quiz" → QuizScreen
        │   └─ Click "Kamus" → DictionaryScreen
        │
        ├─ DictionaryScreen
        │   └─ Click huruf → LetterDetailDialog
        │
        └─ QuizScreen
            ├─ QuizSelectionScreen
            │   ├─ Quiz 1 → Quiz1Container
            │   └─ Quiz 2 → (Coming Soon)
            │
            └─ Quiz1Container
                ├─ Quiz1MainLevelScreen
                │   └─ Click level → Quiz1SubLevelScreen
                │
                ├─ Quiz1SubLevelScreen
                │   └─ Click sub-level → Quiz1GameScreen
                │
                ├─ Quiz1GameScreen
                │   ├─ Display sequence
                │   ├─ Wait input
                │   ├─ Feedback (benar/salah)
                │   └─ Complete → Quiz1FinalScreen
                │
                └─ Quiz1FinalScreen
                    ├─ Home → HomeScreen
                    ├─ Retry → Quiz1GameScreen
                    └─ Next → Quiz1SubLevelScreen
```

---

## 📏 **CODE METRICS**

```
Total Lines of Code:    ~2,400
Total Files:            15
Quiz System Files:      9
Screen Components:      12+
Data Models:            10+
Enums:                  6
Questions Available:    91+

Complexity:
- Low:    Models, Data files
- Medium: Screen files
- High:   GameScreen (state machine), Container (navigation)
```

---

## 🎨 **DESIGN PATTERNS USED**

### **1. Separation of Concerns**
- Models ≠ UI ≠ Data ≠ Logic
- Each file has single responsibility

### **2. Composition**
- Small reusable components
- QuizCard, MainLevelCard, SubLevelCard
- Easy to modify individually

### **3. State Management**
- Remember & MutableState
- Unidirectional data flow
- State hoisting

### **4. Navigation**
- Enum-based routing
- Container pattern
- Callback navigation

### **5. Data Driven**
- Questions from data file
- Config from LevelData
- Easy to add/modify

---

## 🚀 **QUICK START GUIDE**

### **Untuk Testing:**
```
1. Open Android Studio
2. Run app
3. Skip splash screen
4. Click "Quiz" di homepage
5. Test flow:
   Quiz Selection → Level 1 → 1.1 Lambat → Play
6. Test gameplay dengan placeholder
7. Complete dan lihat unlock
```

### **Untuk Production:**
```
1. Siapkan 26 gambar (bisindo_a.png - bisindo_z.png)
2. Copy ke drawable/
3. Update Quiz1Models.kt (1 function)
4. Uncomment Image() di Quiz1GameScreen.kt
5. Build & test
6. Ready to publish!
```

---

## 📞 **FILE DEPENDENCIES**

```
MainActivity.kt
  └─ uses → QuizScreen.kt
              └─ uses → Quiz1Container.kt
                          └─ uses → All Quiz screens
                                      └─ uses → Quiz1Models.kt
                                                QuestionBank.kt
                                                LevelData.kt
```

**Clean dependency tree, no circular dependencies!**

---

**Dibuat:** November 2025  
**Project:** FINPRO MOBAPP - BISINDO Learning App  
**Status:** ✅ Quiz 1 Complete, Ready for Testing

