package com.example.finpro_mobapp

// Data class untuk huruf alphabet
data class AlphabetLetter(
    val letter: String,
    val description: String,
    // Placeholder untuk gambar - user akan menambahkan sendiri
    val imageResId: Int = R.drawable.ic_launcher_foreground
)

