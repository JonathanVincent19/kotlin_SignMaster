# Aplikasi Belajar Bahasa Isyarat - SignMaster

Aplikasi mobile Kotlin untuk belajar bahasa isyarat dengan tampilan yang menarik dan user-friendly.

## Struktur PNG Placeholder

Aplikasi ini telah disiapkan dengan placeholder untuk 3 PNG yang perlu Anda masukkan:

### 1. Logo MOSVONT Academy (Header Kiri Atas)
- **File**: `logoImageView` di `activity_main.xml`
- **Lokasi**: Header section, sebelah kiri
- **Ukuran**: 120dp x 40dp
- **Deskripsi**: Logo dengan teks "MOSVONT academy" dalam warna orange dan biru, dengan ikon buku dan topi wisuda

### 2. Background Banner Biru (Section Utama)
- **File**: `bannerBackgroundImageView` di `activity_main.xml`
- **Lokasi**: Section banner biru utama
- **Ukuran**: Match parent dengan tinggi 200dp
- **Deskripsi**: Background dengan pola titik-titik putih, bunting warna-warni, awan, pesawat kertas, dan tanaman

### 3. Ilustrasi Orang-orang (Section Donasi)
- **File**: `peopleImageView` di `activity_main.xml`
- **Lokasi**: Section donation banner di bawah
- **Ukuran**: 120dp x 60dp
- **Deskripsi**: 5 orang dengan berbagai karakteristik, memegang dokumen dan folder

## Cara Mengganti PNG Placeholder

1. **Siapkan file PNG** dengan nama yang sesuai:
   - `logo_mosvont.png` untuk logo
   - `banner_background.png` untuk background banner
   - `people_donation.png` untuk ilustrasi orang

2. **Copy file PNG** ke folder:
   ```
   app/src/main/res/drawable/
   ```

3. **Update referensi** di `MainActivity.kt`:
   ```kotlin
   // Logo (line 60)
   painter = painterResource(id = R.drawable.logo_mosvont)
   
   // Banner Background (line 88)
   painter = painterResource(id = R.drawable.banner_background)
   
   // People Illustration (line 245)
   painter = painterResource(id = R.drawable.people_donation)
   ```

## Fitur Aplikasi

- **Header**: Logo MOSVONT Academy dengan greeting
- **Banner Utama**: Promosi belajar bahasa isyarat dengan tombol CTA
- **Popular Categories**: Card untuk Quiz dan Kamus SIBI
- **Section Donasi**: Ajakan donasi dengan ilustrasi orang-orang

## Teknologi yang Digunakan

- **Language**: Kotlin
- **UI**: Jetpack Compose dengan Material3
- **Styling**: Compose styling dengan custom colors
- **Architecture**: Modern Android Compose Architecture

## Struktur File

```
app/src/main/
├── java/com/example/finpro_mobapp/
│   └── MainActivity.kt (Compose UI)
├── res/
│   ├── drawable/
│   │   ├── banner_background.xml
│   │   ├── button_background.xml
│   │   └── card_background.xml
│   └── values/
│       └── colors.xml
```

## Menjalankan Aplikasi

1. Buka project di Android Studio
2. Sync project dengan Gradle
3. Jalankan aplikasi di emulator atau device
4. Ganti PNG placeholder sesuai kebutuhan

## Catatan Penting

- Pastikan file PNG memiliki resolusi yang sesuai untuk berbagai density screen
- Gunakan format PNG dengan transparansi jika diperlukan
- Optimasi ukuran file PNG untuk performa aplikasi yang lebih baik
