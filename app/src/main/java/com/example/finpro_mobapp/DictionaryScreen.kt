package com.example.finpro_mobapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    onMenuClick: () -> Unit
) {
    var selectedLetter by remember { mutableStateOf<AlphabetLetter?>(null) }
    
    // Data alphabet A-Z dengan deskripsi unik untuk setiap huruf
    val alphabetList = remember {
        listOf(
            AlphabetLetter(
                letter = "A",
                description = "Kedua jari telunjuk dan jempol bersentuhan membentuk segitiga.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_a
            ),
            AlphabetLetter(
                letter = "B",
                description = "Tangan terbuka dengan semua jari rapat dan ibu jari di samping telapak tangan.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_b
            ),
            AlphabetLetter(
                letter = "C",
                description = "Tangan membentuk lengkungan seperti huruf C dengan jari-jari melengkung.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_c
            ),
            AlphabetLetter(
                letter = "D",
                description = "Jari telunjuk tegak lurus ke atas, jari-jari lain menekuk dengan ibu jari menyentuh jari tengah.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_d
            ),
            AlphabetLetter(
                letter = "E",
                description = "Semua jari menekuk ke dalam seperti cakar dengan ujung jari menyentuh telapak tangan.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_e
            ),
            AlphabetLetter(
                letter = "F",
                description = "Jari telunjuk dan ibu jari saling bersentuhan membentuk lingkaran, jari lainnya tegak.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_f
            ),
            AlphabetLetter(
                letter = "G",
                description = "Jari telunjuk dan ibu jari membentang horizontal seperti menunjuk ke samping.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_g
            ),
            AlphabetLetter(
                letter = "H",
                description = "Jari telunjuk dan jari tengah membentang horizontal berdampingan.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_h
            ),
            AlphabetLetter(
                letter = "I",
                description = "Kelingking tegak lurus ke atas, jari-jari lain menekuk dengan ibu jari di atas jari tengah.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_i
            ),
            AlphabetLetter(
                letter = "J",
                description = "Kelingking tegak lurus kemudian bergerak membentuk huruf J di udara.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_j
            ),
            AlphabetLetter(
                letter = "K",
                description = "Jari telunjuk tegak, jari tengah menyentuh ibu jari, jari lainnya menekuk.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_k
            ),
            AlphabetLetter(
                letter = "L",
                description = "Jari telunjuk dan ibu jari membentuk huruf L dengan sudut 90 derajat.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_l
            ),
            AlphabetLetter(
                letter = "M",
                description = "Tiga jari pertama (telunjuk, tengah, manis) diletakkan di atas ibu jari.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_m
            ),
            AlphabetLetter(
                letter = "N",
                description = "Dua jari pertama (telunjuk dan tengah) diletakkan di atas ibu jari.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_n
            ),
            AlphabetLetter(
                letter = "O",
                description = "Semua ujung jari bertemu membentuk lingkaran seperti huruf O.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_o
            ),
            AlphabetLetter(
                letter = "P",
                description = "Seperti K tetapi tangan mengarah ke bawah dengan jari telunjuk dan tengah membentang.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_p
            ),
            AlphabetLetter(
                letter = "Q",
                description = "Jari telunjuk dan ibu jari mengarah ke bawah dengan jari lainnya menekuk.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_q
            ),
            AlphabetLetter(
                letter = "R",
                description = "Jari telunjuk dan jari tengah menyilang dengan jari telunjuk di atas.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_r
            ),
            AlphabetLetter(
                letter = "S",
                description = "Tangan mengepal dengan ibu jari berada di depan jari-jari yang menekuk.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_s
            ),
            AlphabetLetter(
                letter = "T",
                description = "Ibu jari dijepit di antara jari telunjuk dan jari tengah.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_t
            ),
            AlphabetLetter(
                letter = "U",
                description = "Jari telunjuk dan jari tengah tegak lurus ke atas, rapat berdampingan.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_u
            ),
            AlphabetLetter(
                letter = "V",
                description = "Jari telunjuk dan jari tengah tegak membentuk huruf V dengan jari terpisah.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_v
            ),
            AlphabetLetter(
                letter = "W",
                description = "Tiga jari (telunjuk, tengah, manis) tegak membentuk huruf W dengan jari terpisah.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_w
            ),
            AlphabetLetter(
                letter = "X",
                description = "Jari telunjuk menekuk seperti kait dengan ujung jari menghadap ke atas.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_x
            ),
            AlphabetLetter(
                letter = "Y",
                description = "Ibu jari dan kelingking tegak membentang, jari-jari lain menekuk.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_y
            ),
            AlphabetLetter(
                letter = "Z",
                description = "Jari telunjuk tegak kemudian bergerak membentuk zigzag seperti huruf Z di udara.",
                imageResId = R.drawable.ic_launcher_foreground  // Ganti dengan R.drawable.bisindo_z
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar dengan hamburger menu
            TopAppBar(
                title = {
                    // Space untuk logo - user akan menambahkan logo sendiri
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // TODO: Tambahkan logo Anda sendiri di sini
                        // Contoh: Image(painter = painterResource(id = R.drawable.your_logo), ...)
                        Text(
                            text = "paham+bisindo",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A90E2)
                )
            )
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "Daftar Huruf BISINDO\n(A–Z)",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                
                // Subtitle
                Text(
                    text = "Klik gambar untuk melihat isyarat dan penjelasannya",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
                
                // Grid Alphabet
                Column {
                    alphabetList.chunked(2).forEach { rowLetters ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowLetters.forEach { letter ->
                                AlphabetCard(
                                    letter = letter,
                                    onClick = { selectedLetter = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Add empty space if odd number
                            if (rowLetters.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
        
        // Modal Dialog when letter is clicked
        selectedLetter?.let { letter ->
            LetterDetailDialog(
                letter = letter,
                onDismiss = { selectedLetter = null }
            )
        }
    }
}

@Composable
fun AlphabetCard(
    letter: AlphabetLetter,
    onClick: (AlphabetLetter) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { onClick(letter) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Letter label
            Text(
                text = letter.letter,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: User akan mengganti dengan gambar isyarat asli
                // Placeholder untuk gambar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📷",
                        fontSize = 40.sp
                    )
                    Text(
                        text = "Gambar ${letter.letter}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
                // Uncomment ini ketika Anda sudah menambahkan gambar:
                // Image(
                //     painter = painterResource(id = letter.imageResId),
                //     contentDescription = "Huruf ${letter.letter}",
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Crop
                // )
            }
        }
    }
}

@Composable
fun LetterDetailDialog(
    letter: AlphabetLetter,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFFE53935)
                            )
                        }
                    }
                    
                    // Large image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, Color.LightGray, RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        // TODO: User akan mengganti dengan gambar isyarat asli
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📷",
                                fontSize = 80.sp
                            )
                            Text(
                                text = "Gambar Besar ${letter.letter}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        // Uncomment ini ketika Anda sudah menambahkan gambar:
                        // Image(
                        //     painter = painterResource(id = letter.imageResId),
                        //     contentDescription = "Huruf ${letter.letter}",
                        //     modifier = Modifier.fillMaxSize(),
                        //     contentScale = ContentScale.Crop
                        // )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title
                    Text(
                        text = "Huruf ${letter.letter}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Description
                    Text(
                        text = letter.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

