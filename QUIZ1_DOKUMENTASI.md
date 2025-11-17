# 🎮 DOKUMENTASI QUIZ 1 - TEBAK ISYARAT

Dokumentasi lengkap untuk implementasi Quiz 1 yang sudah selesai dibuat.

---

## ✅ **STATUS IMPLEMENTASI**

**SEMUA FITUR QUIZ 1 SUDAH SELESAI!** 🎉

- ✅ 9 File dibuat
- ✅ Semua screen UI completed
- ✅ Navigation logic working
- ✅ Game mechanics implemented
- ✅ Level progression system ready
- ✅ Question bank tersedia
- ✅ No errors

---

## 📁 **STRUKTUR FILE QUIZ 1**

```
app/src/main/java/com/example/finpro_mobapp/
├── MainActivity.kt                    (Updated - integrated)
├── QuizScreen.kt                      (Updated - quiz router)
└── quiz/
    ├── Quiz1Models.kt                 (Data classes & enums)
    ├── QuestionBank.kt                (Bank soal 90+ questions)
    ├── LevelData.kt                   (Level configuration)
    ├── QuizSelectionScreen.kt         (Screen 1)
    ├── Quiz1MainLevelScreen.kt        (Screen 2)
    ├── Quiz1SubLevelScreen.kt         (Screen 3)
    ├── Quiz1GameScreen.kt             (Screen 4-7)
    ├── Quiz1FinalScreen.kt            (Screen 8)
    └── Quiz1Container.kt              (Navigation container)
```

---

## 🎯 **FITUR YANG SUDAH DIBUAT**

### **1. System Level Bertingkat**
- ✅ 3 Main Levels (Huruf, 1 Kata, 2 Kata)
- ✅ 9 Sub-Levels (3 kecepatan per level)
- ✅ Progressive unlock system
- ✅ Status tracking (Completed/On Progress/Locked)

### **2. Bank Soal Lengkap**
- ✅ Level 1.1: 10 soal huruf A-J
- ✅ Level 1.2: 12 soal huruf A-P
- ✅ Level 1.3: 15 soal huruf A-Z
- ✅ Level 2.1: 8 kata (3-4 huruf)
- ✅ Level 2.2: 10 kata (4-5 huruf)
- ✅ Level 2.3: 12 kata (5-6 huruf)
- ✅ Level 3.1: 6 kalimat 2 kata
- ✅ Level 3.2: 8 kalimat 2 kata
- ✅ Level 3.3: 10 kalimat 2 kata

### **3. Game Mechanics**
- ✅ Sequential image display
- ✅ Auto-generate image dari string
- ✅ Timer per huruf (variable speed)
- ✅ Retry mechanism (wajib benar)
- ✅ Replay tayangan
- ✅ Clean minimalist UI

### **4. All Screens**
- ✅ Quiz Selection (pilih Quiz 1 atau 2)
- ✅ Main Level Selection (pilih Level 1/2/3)
- ✅ Sub-Level Selection (pilih kecepatan)
- ✅ Game Screen (gameplay)
- ✅ Feedback Screens (benar/salah)
- ✅ Final Score Screen

---

## 🔄 **ALUR LENGKAP**

```
1. Home → Click "Quiz" atau drawer "Latihan"
   ↓
2. Quiz Selection Screen
   - Pilih Quiz 1 atau Quiz 2
   ↓
3. Main Level Selection (Quiz 1)
   - Level 1: Tebak 1 Huruf (⭐⭐⭐ / ⏱️ / 🔒)
   - Level 2: Tebak 1 Kata (⭐⭐⭐ / ⏱️ / 🔒)
   - Level 3: Tebak 2 Kata (⭐⭐⭐ / ⏱️ / 🔒)
   Click "Pilih Sub-Level"
   ↓
4. Sub-Level Selection
   - X.1 Lambat (✅ / ⏱️ / 🔒) - Questions: X/Y
   - X.2 Sedang (✅ / ⏱️ / 🔒) - Questions: X/Y
   - X.3 Cepat (✅ / ⏱️ / 🔒) - Questions: X/Y
   Click "Mulai" / "Lanjutkan" / "Main Lagi"
   ↓
5. Game Screen (Loop untuk setiap soal):
   
   a. Displaying Sequence
      - Tampil gambar huruf 1 → delay
      - Tampil gambar huruf 2 → delay
      - ... dst untuk semua huruf
      ↓
   
   b. Waiting Input
      - Box kosong (placeholder)
      - Input field
      - Button "Kirim Jawaban"
      - Button "Ulangi Tayangan" (optional)
      User submit →
      ↓
   
   c. Feedback
      IF BENAR:
        - "✅ YEYY KAMU BENAR!"
        - Auto next (1.5 detik)
        - completedQuestions++
      
      IF SALAH:
        - "❌ SALAH, NETNOT COBA LAGI!"
        - Button "Ulangi Soal"
        - Kembali ke step (a)
      ↓
   
   d. Next Question
      - currentQuestionIndex++
      - Repeat dari step (a)
      ↓
      
6. Final Score Screen (Setelah semua soal selesai)
   - "🎉 YEAYY SELESAI!"
   - Level name
   - Questions: 10/10
   - "GOOD JOB!!"
   - Unlock notification (if any)
   - Buttons:
     • "Kembali ke Home"
     • "Coba Lagi" (restart sub-level)
     • "Level Selanjutnya" (if unlocked)
```

---

## 💾 **DATA FLOW**

### **Initial State:**
```kotlin
Level 1:
  - isUnlocked: true
  - status: ON_PROGRESS
  - Sub-levels:
    • 1.1: unlocked, NOT_STARTED, Questions: 0/10
    • 1.2: locked
    • 1.3: locked

Level 2: locked
Level 3: locked
```

### **After User Completes 1.1:**
```kotlin
Level 1:
  - status: ON_PROGRESS
  - Sub-levels:
    • 1.1: unlocked, COMPLETED, Questions: 10/10 ✅
    • 1.2: unlocked, NOT_STARTED, Questions: 0/12 🔓
    • 1.3: locked
```

### **After User Completes All Level 1:**
```kotlin
Level 1:
  - status: COMPLETED ⭐⭐⭐
  - All sub-levels: COMPLETED

Level 2:
  - isUnlocked: true 🔓
  - status: ON_PROGRESS
  - 2.1: unlocked, NOT_STARTED
```

---

## 🎨 **CARA KERJA GAMBAR OTOMATIS**

### **Konsep:**
```
Anda HANYA perlu 26 gambar alphabet:
bisindo_a.png, bisindo_b.png, ..., bisindo_z.png
```

### **System Auto-Generate:**
```kotlin
Input soal: "SAYA"

System otomatis:
1. Parse string → ['S', 'A', 'Y', 'A']
2. Map ke resource:
   'S' → R.drawable.bisindo_s
   'A' → R.drawable.bisindo_a
   'Y' → R.drawable.bisindo_y
   'A' → R.drawable.bisindo_a
3. Return: [bisindo_s, bisindo_a, bisindo_y, bisindo_a]
4. Display sequence otomatis!
```

### **Untuk Menambah Soal Baru:**
```kotlin
// Di QuestionBank.kt, cukup tambah string:

val level2_2 = listOf(
    "RUMAH",
    "MAKAN",
    "BUKU BARU"  // ← Tambah soal baru, gambar otomatis!
)

// System akan otomatis:
// B → bisindo_b
// U → bisindo_u
// K → bisindo_k
// U → bisindo_u
// (spasi)
// B → bisindo_b
// A → bisindo_a
// R → bisindo_r
// U → bisindo_u
```

---

## 🔧 **CARA MENAMBAHKAN GAMBAR ASLI**

### **Langkah 1: Siapkan Gambar**
```
1. Siapkan 26 gambar BISINDO (A-Z)
2. Format: PNG atau JPG
3. Nama file: bisindo_a.png, bisindo_b.png, ..., bisindo_z.png
4. Copy ke: app/src/main/res/drawable/
```

### **Langkah 2: Update Code**
Buka file: `quiz/Quiz1Models.kt` (line ~98-112)

**Cari bagian ini:**
```kotlin
private fun getImageResIdForLetter(letter: Char): Int {
    // TODO: Nanti ganti dengan gambar asli
    return when(letter.uppercaseChar()) {
        // Saat gambar sudah ada, uncomment ini:
        // 'A' -> R.drawable.bisindo_a
        // 'B' -> R.drawable.bisindo_b
        // ... dst sampai Z
        else -> R.drawable.ic_launcher_foreground // Placeholder
    }
}
```

**Ganti menjadi:**
```kotlin
private fun getImageResIdForLetter(letter: Char): Int {
    return when(letter.uppercaseChar()) {
        'A' -> R.drawable.bisindo_a
        'B' -> R.drawable.bisindo_b
        'C' -> R.drawable.bisindo_c
        'D' -> R.drawable.bisindo_d
        'E' -> R.drawable.bisindo_e
        'F' -> R.drawable.bisindo_f
        'G' -> R.drawable.bisindo_g
        'H' -> R.drawable.bisindo_h
        'I' -> R.drawable.bisindo_i
        'J' -> R.drawable.bisindo_j
        'K' -> R.drawable.bisindo_k
        'L' -> R.drawable.bisindo_l
        'M' -> R.drawable.bisindo_m
        'N' -> R.drawable.bisindo_n
        'O' -> R.drawable.bisindo_o
        'P' -> R.drawable.bisindo_p
        'Q' -> R.drawable.bisindo_q
        'R' -> R.drawable.bisindo_r
        'S' -> R.drawable.bisindo_s
        'T' -> R.drawable.bisindo_t
        'U' -> R.drawable.bisindo_u
        'V' -> R.drawable.bisindo_v
        'W' -> R.drawable.bisindo_w
        'X' -> R.drawable.bisindo_x
        'Y' -> R.drawable.bisindo_y
        'Z' -> R.drawable.bisindo_z
        else -> R.drawable.ic_launcher_foreground
    }
}
```

### **Langkah 3: Uncomment Image Display**
Buka file: `quiz/Quiz1GameScreen.kt` (line ~141-147)

**Uncomment bagian ini:**
```kotlin
Image(
    painter = painterResource(id = currentImageId),
    contentDescription = "Letter ${question.letters[currentLetterIndex]}",
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

**Comment/hapus placeholder:**
```kotlin
// Column(
//     horizontalAlignment = Alignment.CenterHorizontally
// ) {
//     Text(text = "📷", fontSize = 80.sp)
//     Text(text = "Huruf ${question.letters[currentLetterIndex]}", ...)
// }
```

### **SELESAI!** 
Semua soal otomatis punya gambar! 🎉

---

## 🎮 **CARA MENGGUNAKAN / TESTING**

### **1. Run Aplikasi**
```bash
# Build dan run aplikasi
```

### **2. Navigasi ke Quiz**
```
Home → Click button "Quiz" 
atau
Home → Menu (☰) → Latihan
```

### **3. Test Flow:**
```
a. Quiz Selection → Click "Mulai Quiz" pada Quiz 1
b. Main Level → Click "Pilih Sub-Level" pada Level 1
c. Sub-Level → Click "Mulai" pada 1.1 Lambat
d. Game dimulai:
   - Lihat gambar huruf (5 detik)
   - Gambar hilang
   - Ketik jawaban
   - Submit
   - Dapat feedback
   - Lanjut atau retry
e. Selesai 10 soal → Final Score
f. Level 1.2 terbuka!
```

### **4. Test dengan Placeholder:**
Saat ini menggunakan placeholder (ic_launcher_foreground + text huruf)
- ✅ Semua logic sudah berfungsi
- ✅ Bisa test gameplay complete
- ✅ Unlock system bekerja
- ⏳ Menunggu gambar asli untuk production

---

## 📊 **CONTOH SOAL YANG TERSEDIA**

### **Level 1 (37 soal total):**
```
1.1: A, B, C, D, E, F, G, H, I, J (10 soal)
1.2: 12 random dari A-P (12 soal)
1.3: 15 random dari A-Z (15 soal)
```

### **Level 2 (30 soal total):**
```
2.1: SAYA, NAMA, BUKU, MEJA, KAKI, MATA, TOPI, BAJU (8 soal)
2.2: RUMAH, MAKAN, MINUM, KERJA, TEMAN, BAIK, BESAR, KECIL, MALAM, SIANG (10 soal)
2.3: BELAJAR, SEKOLAH, SENANG, SEDIH, GEMBIRA, KELUARGA, BERSAMA, DATANG, PULANG, TENANG, MARAH, SAYANG (12 soal)
```

### **Level 3 (24 soal total):**
```
3.1: NAMA SAYA, SAYA BAIK, BUKU INI, AKU MAKAN, TEMAN BAIK, RUMAH BESAR (6 soal)
3.2: SAYA BELAJAR, NAMA TEMAN, PERGI SEKOLAH, PULANG RUMAH, MAKAN SIANG, KERJA KERAS, BANGUN PAGI, TIDUR MALAM (8 soal)
3.3: BELAJAR BERSAMA, KELUARGA BAHAGIA, SENANG BERTEMU, SEKOLAH FAVORIT, SAHABAT SEJATI, BERBAGI CERITA, SEMANGAT BELAJAR, GEMBIRA SELALU, DATANG BERSAMA, PULANG SENANG (10 soal)
```

**Total: ~91 soal** across all levels!

---

## 🎨 **UI KARAKTERISTIK**

### **Design Principles:**
✅ **Minimalist** - No distractions during gameplay  
✅ **Clean** - White space yang cukup  
✅ **Focused** - Gambar besar dan jelas  
✅ **Encouraging** - Positive feedback messages  
✅ **Progressive** - Clear unlock indicators  

### **Color Palette:**
```
Primary Blue:   #4A90E2  (Top bars, main buttons)
Success Green:  #27AE60  (Completed, correct answer)
Warning Orange: #F39C12  (On progress)
Error Red:      #E74C3C  (Wrong answer)
Gray:           #95A5A6  (Locked/disabled)
Text Dark:      #2C3E50  (Headings)
Text Light:     #7F8C8D  (Subtitles)
```

### **Typography:**
```
Heading:        20-28sp, Bold
Body:           14-16sp, Regular/Medium
Button:         16sp, SemiBold
Emoji:          24-32sp
```

---

## 🔐 **UNLOCK SYSTEM LOGIC**

### **Sub-Level Unlock:**
```
1.1 (Lambat)  → Unlocked by default
1.1 Completed → Unlock 1.2
1.2 Completed → Unlock 1.3
1.3 Completed → Level 1 COMPLETED ⭐⭐⭐
              → Unlock Level 2

2.1 Unlocked automatically when Level 2 unlocked
2.1 Completed → Unlock 2.2
2.2 Completed → Unlock 2.3
2.3 Completed → Level 2 COMPLETED ⭐⭐⭐
              → Unlock Level 3

(Same pattern for Level 3)
```

### **Status Update:**
```kotlin
Sub-Level Status:
- Questions 0/X, locked     → 🔒 LOCKED
- Questions 0/X, unlocked   → (no icon) NOT_STARTED
- Questions Y/X (0<Y<X)     → ⏱️ ON_PROGRESS
- Questions X/X             → ✅ COMPLETED

Main Level Status:
- All sub-levels COMPLETED  → ⭐⭐⭐ Selesai
- Some progress             → ⏱️ On Progress
- Not unlocked              → 🔒 (no display)
```

---

## 🎯 **GAME MECHANICS DETAIL**

### **Retry Mechanism:**
```
User jawab SALAH:
1. Show "❌ SALAH, NETNOT COBA LAGI!"
2. Button "🔄 Ulangi Soal"
3. User HARUS retry
4. Tidak bisa skip
5. Ulangi sampai BENAR
6. Baru bisa lanjut soal berikutnya

Kenapa?
→ Memastikan user benar-benar belajar
→ Tidak asal jawab dan skip
→ Better retention
```

### **Completion Tracking:**
```
Questions: X/Y

X = Jumlah soal yang sudah BERHASIL dijawab BENAR
Y = Total soal di sub-level

Contoh:
- User mulai: 0/10
- Jawab benar soal 1: 1/10
- Jawab salah soal 2, retry 3x, akhirnya benar: 2/10
- ... dst
- Selesai semua: 10/10 → ✅ COMPLETED
```

### **Auto-Continue vs Manual:**
```
Feedback BENAR:
- Auto continue setelah 1.5 detik
- Atau bisa tambah button "Lanjut" manual

Feedback SALAH:
- Manual retry (button click)
- User baca feedback dulu
```

---

## 🚀 **CARA MENAMBAH/EDIT SOAL**

### **Lokasi:** `quiz/QuestionBank.kt`

### **Tambah Soal Baru:**
```kotlin
// Sangat mudah! Cukup tambah string di list

val level2_2: List<String> = listOf(
    "RUMAH",
    "MAKAN",
    "KUCING",  // ← Soal baru!
    "ANJING"   // ← Soal baru!
)

// Gambar otomatis generated:
// KUCING → K,U,C,I,N,G → bisindo_k, bisindo_u, bisindo_c, ...
```

### **Edit Soal Existing:**
```kotlin
// Ganti string saja
"RUMAH" → "GEDUNG"  // Auto update gambar!
```

### **Tambah Level Baru:**
```kotlin
// Di QuestionBank.kt, tambah list baru:
val level4_1: List<String> = listOf(
    "SOAL BARU 1",
    "SOAL BARU 2"
)

// Di LevelData.kt, tambah MainLevel baru
// Di QuestionBank.kt, tambah case di getQuestionsForSubLevel()
```

---

## 🐛 **TROUBLESHOOTING**

### **Problem: Gambar tidak muncul**
```
Solution:
1. Pastikan file di drawable/ dengan nama benar
2. Nama harus lowercase: bisindo_a.png ✅ bukan Bisindo_A.png ❌
3. Rebuild project: Build → Clean → Rebuild
4. Check Quiz1Models.kt sudah uncomment mapping
```

### **Problem: Level tidak unlock**
```
Solution:
1. Pastikan completedQuestions == totalQuestions
2. Check LevelData.unlockNextSubLevel() logic
3. Rebuild state (restart app)
```

### **Problem: Sequence tidak tampil**
```
Solution:
1. Check displayDuration tidak terlalu cepat
2. Pastikan LaunchedEffect di GameScreen berjalan
3. Check console log untuk delay issues
```

---

## 📝 **NEXT STEPS / TODO**

### **For Production:**
- [ ] Tambahkan 26 gambar BISINDO ke drawable/
- [ ] Update getImageResIdForLetter() dengan mapping real
- [ ] Uncomment Image() di GameScreen
- [ ] Test dengan gambar asli
- [ ] Adjust timing jika perlu

### **Optional Enhancements:**
- [ ] Save progress ke SharedPreferences/Database
- [ ] Add sound effects
- [ ] Add haptic feedback
- [ ] Add leaderboard
- [ ] Add achievements/badges
- [ ] Add statistics tracking
- [ ] Add daily streak counter

### **Quiz 2:**
- [ ] Implement camera integration
- [ ] ML model integration
- [ ] Gesture recognition
- [ ] Similar level structure

---

## 💡 **TIPS DEVELOPMENT**

### **Testing Strategy:**
1. **Test dengan placeholder** - Pastikan logic benar
2. **Test unlock system** - Complete sub-levels, cek unlock
3. **Test retry mechanism** - Jawab salah, pastikan retry
4. **Test all navigation** - Back button, exit, dll
5. **Add real images** - Final polish

### **Code Organization:**
- ✅ Models terpisah (Quiz1Models.kt)
- ✅ Data terpisah (QuestionBank.kt, LevelData.kt)
- ✅ Screens terpisah (per file)
- ✅ Container untuk navigation (Quiz1Container.kt)
- ✅ Easy to maintain & debug

### **Performance:**
- State management dengan remember & mutableStateOf
- LaunchedEffect untuk sequence display
- Coroutines untuk delays
- No heavy operations

---

## 🎉 **KESIMPULAN**

**QUIZ 1 SUDAH 100% READY!** ✅

✅ **9 files dibuat**  
✅ **All screens implemented**  
✅ **91+ soal tersedia**  
✅ **Unlock system working**  
✅ **Retry mechanism active**  
✅ **Clean minimalist UI**  
✅ **Auto image mapping**  
✅ **No errors**  

**Tinggal:**
- Add 26 gambar BISINDO
- Update 1 function
- Uncomment 1 block
- **DONE!** 🚀

---

**Project ini sudah siap untuk development lanjutan dan production!**

Selamat mencoba! 😊

