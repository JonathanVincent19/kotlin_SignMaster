# 📁 Struktur Project - FINPRO MOBAPP

Dokumentasi ini menjelaskan struktur file project setelah refactoring untuk kemudahan debugging dan maintenance.

## 🗂️ Struktur File

```
app/src/main/java/com/example/finpro_mobapp/
├── MainActivity.kt          # Activity utama + Navigation logic
├── AlphabetModels.kt        # Data class untuk model
├── SplashScreen.kt          # Halaman splash screen
├── HomeScreen.kt            # Halaman beranda/home
├── DictionaryScreen.kt      # Halaman kamus BISINDO (A-Z)
└── QuizScreen.kt            # Halaman quiz/latihan
```

---

## 📄 Penjelasan Setiap File

### 1. **MainActivity.kt**
**Fungsi:** Entry point aplikasi dan navigation logic
- `MainActivity` class - Activity utama
- `Screen` enum - State untuk navigasi
- `AppNavigation()` - Composable untuk navigation drawer dan routing

**Code ringkas:** ~138 baris

---

### 2. **AlphabetModels.kt**
**Fungsi:** Data models untuk fitur kamus
- `AlphabetLetter` data class - Model untuk setiap huruf alphabet

**Code ringkas:** ~9 baris

---

### 3. **SplashScreen.kt**
**Fungsi:** Tampilan splash screen saat app pertama kali dibuka
- `SplashScreen()` composable
- Animasi fade-in
- Auto navigate setelah 5 detik

**Code ringkas:** ~125 baris

---

### 4. **HomeScreen.kt**
**Fungsi:** Halaman beranda/home dengan banner dan kategori
- `HomeScreen()` composable
- Header dengan logo dan hamburger menu
- Banner belajar bahasa isyarat
- Button navigasi ke Quiz dan Kamus
- Donation banner

**Code ringkas:** ~299 baris

---

### 5. **DictionaryScreen.kt**
**Fungsi:** Halaman kamus BISINDO dengan grid alphabet A-Z
- `DictionaryScreen()` - Main screen composable
- `AlphabetCard()` - Card component untuk setiap huruf
- `LetterDetailDialog()` - Modal popup untuk detail huruf
- Grid layout 2 kolom untuk alphabet A-Z

**Code ringkas:** ~298 baris

**Fitur:**
- Top bar dengan hamburger menu
- Grid alphabet A-Z (2 kolom)
- Click pada huruf → tampil modal detail
- Modal menampilkan gambar besar + deskripsi
- Tombol close (X) merah

---

### 6. **QuizScreen.kt**
**Fungsi:** Halaman quiz/latihan (placeholder untuk development selanjutnya)
- `QuizScreen()` composable
- Top bar dengan hamburger menu
- Coming soon message

**Code ringkas:** ~54 baris

---

## 🔄 Alur Navigasi

```
SplashScreen (5 detik)
    ↓
AppNavigation
    ├── HomeScreen (default)
    │   ├── Button → DictionaryScreen
    │   └── Button → QuizScreen
    │
    ├── Navigation Drawer (hamburger menu)
    │   ├── 🏠 Beranda → HomeScreen
    │   ├── Aa Alfabet → DictionaryScreen
    │   └── 📚 Latihan → QuizScreen
    │
    ├── DictionaryScreen
    │   ├── Hamburger menu → Open drawer
    │   └── Click huruf → LetterDetailDialog
    │
    └── QuizScreen
        └── Hamburger menu → Open drawer
```

---

## 🎯 Keuntungan Struktur Ini

### ✅ **Mudah di-debug**
- Setiap screen terpisah dalam file sendiri
- Lebih mudah menemukan bug karena scope code lebih kecil
- Error message menunjukkan file spesifik yang bermasalah

### ✅ **Mudah di-maintain**
- Perubahan pada satu screen tidak mempengaruhi yang lain
- Code lebih terorganisir dan readable
- Gampang menambah fitur baru

### ✅ **Team collaboration**
- Bisa kerja parallel di file berbeda
- Mengurangi conflict saat merge code
- Mudah untuk code review

### ✅ **Scalability**
- Mudah menambah screen baru
- Struktur sudah siap untuk project yang lebih besar
- Bisa dengan mudah diubah ke architecture pattern lain (MVVM, MVI, dll)

---

## 🛠️ Cara Menambah Screen Baru

1. **Buat file baru** (misalnya: `ProfileScreen.kt`)
```kotlin
package com.example.finpro_mobapp

import androidx.compose.runtime.*
// ... imports lainnya

@Composable
fun ProfileScreen(
    onMenuClick: () -> Unit
) {
    // Your screen content here
}
```

2. **Tambahkan ke enum Screen** di `MainActivity.kt`
```kotlin
enum class Screen {
    HOME, DICTIONARY, QUIZ, PROFILE  // ← Tambah PROFILE
}
```

3. **Tambahkan drawer item** di `AppNavigation()`
```kotlin
NavigationDrawerItem(
    icon = { Text("👤", fontSize = 24.sp) },
    label = { Text("Profile", ...) },
    selected = currentScreen == Screen.PROFILE,
    onClick = {
        currentScreen = Screen.PROFILE
        scope.launch { drawerState.close() }
    },
    ...
)
```

4. **Tambahkan routing** di `when (currentScreen)`
```kotlin
Screen.PROFILE -> ProfileScreen(
    onMenuClick = { scope.launch { drawerState.open() } }
)
```

---

## 📝 Tips Development

### Saat Debugging:
1. Buka file screen yang sedang bermasalah
2. Gunakan `Log.d("TAG", "message")` untuk debug
3. Gunakan Android Studio Logcat untuk melihat output

### Saat Menambah Fitur:
1. Identifikasi screen mana yang perlu diubah
2. Edit hanya file screen tersebut
3. Test screen tersebut secara isolated

### Saat Code Review:
1. Review per file lebih mudah
2. Bisa fokus pada logic spesifik
3. Lebih mudah memberi feedback

---

## 🚀 Next Steps

Untuk development selanjutnya, pertimbangkan untuk:
1. ✅ Tambahkan ViewModel untuk business logic
2. ✅ Implementasi Repository pattern untuk data management
3. ✅ Pisahkan components ke file terpisah jika makin kompleks
4. ✅ Tambahkan Unit Tests untuk setiap screen
5. ✅ Implementasi Dependency Injection (Hilt/Koin)

---

**Dibuat pada:** November 2025  
**Project:** FINPRO MOBAPP - Sign Language Learning App

