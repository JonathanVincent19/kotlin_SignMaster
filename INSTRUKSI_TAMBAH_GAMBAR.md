# Instruksi Menambahkan Gambar dan Logo

## 📌 Lokasi untuk Menambahkan Gambar

Semua gambar harus ditambahkan ke folder:
```
app/src/main/res/drawable/
```

## 🖼️ Cara Menambahkan Gambar Huruf BISINDO (A-Z)

### Langkah 1: Siapkan Gambar
1. Siapkan 26 gambar untuk huruf A-Z (format: PNG atau JPG)
2. Beri nama file dengan format: `bisindo_a.png`, `bisindo_b.png`, ..., `bisindo_z.png`
3. Copy semua file gambar ke folder `app/src/main/res/drawable/`

### Langkah 2: Update Code di DictionaryScreen.kt

✅ **SUDAH OTOMATIS!** Setiap huruf A-Z sudah memiliki deskripsi unik masing-masing.

Yang perlu Anda lakukan hanya **ganti placeholder gambar**:

Cari di file `DictionaryScreen.kt` (sekitar baris 38-167), setiap huruf sudah punya struktur:

```kotlin
AlphabetLetter(
    letter = "A",
    description = "Kedua jari telunjuk dan jempol bersentuhan membentuk segitiga.",
    imageResId = R.drawable.ic_launcher_foreground  // ← GANTI INI
),
```

**Ganti semua `R.drawable.ic_launcher_foreground` menjadi:**
- Huruf A: `R.drawable.bisindo_a`
- Huruf B: `R.drawable.bisindo_b`
- Huruf C: `R.drawable.bisindo_c`
- ... dan seterusnya sampai Z

Atau gunakan **Find & Replace** (Ctrl+R / Cmd+R):
- Find: `R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_a`
- Replace: `R.drawable.bisindo_a`

Ulangi untuk setiap huruf B-Z.

### Langkah 3: Uncomment Code Image

#### Di AlphabetCard (sekitar baris 462-468):
HAPUS atau COMMENT OUT placeholder ini:
```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(text = "📷", fontSize = 40.sp)
    Text(text = "Gambar ${letter.letter}", ...)
}
```

UNCOMMENT code Image ini:
```kotlin
Image(
    painter = painterResource(id = letter.imageResId),
    contentDescription = "Huruf ${letter.letter}",
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

#### Di LetterDetailDialog (sekitar baris 537-543):
HAPUS atau COMMENT OUT placeholder ini:
```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(text = "📷", fontSize = 80.sp)
    Text(text = "Gambar Besar ${letter.letter}", ...)
}
```

UNCOMMENT code Image ini:
```kotlin
Image(
    painter = painterResource(id = letter.imageResId),
    contentDescription = "Huruf ${letter.letter}",
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

## 🏠 Cara Menambahkan Logo di Navigation Bar

### Langkah 1: Siapkan Logo
1. Siapkan file logo (format: PNG dengan background transparan lebih baik)
2. Beri nama file: `logo_paham_bisindo.png`
3. Copy file ke folder `app/src/main/res/drawable/`

### Langkah 2: Update Code di DictionaryScreen

Cari bagian kode ini di `DictionaryScreen` (sekitar baris 316-318):

```kotlin
// TODO: Tambahkan logo Anda sendiri di sini
// Contoh: Image(painter = painterResource(id = R.drawable.your_logo), ...)
Text(
    text = "paham+bisindo",
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    color = Color.White
)
```

Ganti menjadi:

```kotlin
Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start
) {
    Image(
        painter = painterResource(id = R.drawable.logo_paham_bisindo),
        contentDescription = "Logo Paham Bisindo",
        modifier = Modifier
            .height(40.dp)
            .width(150.dp),
        contentScale = ContentScale.Fit
    )
}
```

## 📝 Tips Tambahan

### Ukuran Gambar yang Disarankan:
- **Gambar huruf untuk grid**: 400x400 px atau 500x500 px
- **Logo**: Lebar 200-400 px, tinggi 80-120 px
- **Format**: PNG dengan background transparan (untuk logo)

### Jika Gambar Tidak Muncul:
1. Pastikan nama file tidak menggunakan huruf kapital atau spasi
2. Gunakan huruf kecil dan underscore: `bisindo_a.png` ✅ bukan `Bisindo A.png` ❌
3. Rebuild project: **Build > Clean Project** kemudian **Build > Rebuild Project**

### Kustomisasi Deskripsi:
Anda bisa mengganti deskripsi untuk setiap huruf sesuai dengan gerakan isyarat yang sebenarnya.

## ✅ Deskripsi Lengkap Sudah Tersedia!

Setiap huruf A-Z **sudah memiliki deskripsi gerakan BISINDO yang unik**:

- **A**: Kedua jari telunjuk dan jempol bersentuhan membentuk segitiga
- **B**: Tangan terbuka dengan semua jari rapat dan ibu jari di samping telapak tangan
- **C**: Tangan membentuk lengkungan seperti huruf C dengan jari-jari melengkung
- **D**: Jari telunjuk tegak lurus ke atas, jari-jari lain menekuk
- **E**: Semua jari menekuk ke dalam seperti cakar
- **F**: Jari telunjuk dan ibu jari saling bersentuhan membentuk lingkaran
- ... *dan seterusnya sampai Z*

📝 **Lihat file `DictionaryScreen.kt` line 38-167** untuk daftar lengkap!

---

**Selamat mencoba! Jika ada pertanyaan, silakan tanya saja! 😊**

