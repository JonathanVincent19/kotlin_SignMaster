package com.example.finpro_mobapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.wrapContentHeight
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
    val primaryBlue = Color(0xFF2D9CDB)
    val deepBlue = Color(0xFF0F172A)
    val softYellow = Color(0xFFF2C94C)
    
    // Data alphabet A-Z dengan deskripsi unik untuk setiap huruf
    val alphabetList = remember {
        listOf(
            AlphabetLetter(
                letter = "A",
                description = "Kedua jari telunjuk dan jempol bersentuhan membentuk segitiga.",
                imageResId = R.drawable.bisindo_a  // Ganti dengan R.drawable.bisindo_a
            ),
            AlphabetLetter(
                letter = "B",
                description = "Tangan terbuka dengan semua jari rapat dan ibu jari di samping telapak tangan.",
                imageResId = R.drawable.bisindo_b  // Ganti dengan R.drawable.bisindo_b
            ),
            AlphabetLetter(
                letter = "C",
                description = "Tangan membentuk lengkungan seperti huruf C dengan jari-jari melengkung.",
                imageResId = R.drawable.bisindo_c  // Ganti dengan R.drawable.bisindo_c
            ),
            AlphabetLetter(
                letter = "D",
                description = "Jari telunjuk tegak lurus ke atas, jari-jari lain menekuk dengan ibu jari menyentuh jari tengah.",
                imageResId = R.drawable.bisindo_d  // Ganti dengan R.drawable.bisindo_d
            ),
            AlphabetLetter(
                letter = "E",
                description = "Semua jari menekuk ke dalam seperti cakar dengan ujung jari menyentuh telapak tangan.",
                imageResId = R.drawable.bisindo_e  // Ganti dengan R.drawable.bisindo_e
            ),
            AlphabetLetter(
                letter = "F",
                description = "Jari telunjuk dan ibu jari saling bersentuhan membentuk lingkaran, jari lainnya tegak.",
                imageResId = R.drawable.bisindo_f  // Ganti dengan R.drawable.bisindo_f
            ),
            AlphabetLetter(
                letter = "G",
                description = "Jari telunjuk dan ibu jari membentang horizontal seperti menunjuk ke samping.",
                imageResId = R.drawable.bisindo_g  // Ganti dengan R.drawable.bisindo_g
            ),
            AlphabetLetter(
                letter = "H",
                description = "Jari telunjuk dan jari tengah membentang horizontal berdampingan.",
                imageResId = R.drawable.bisindo_h  // Ganti dengan R.drawable.bisindo_h
            ),
            AlphabetLetter(
                letter = "I",
                description = "Kelingking tegak lurus ke atas, jari-jari lain menekuk dengan ibu jari di atas jari tengah.",
                imageResId = R.drawable.bisindo_i  // Ganti dengan R.drawable.bisindo_i
            ),
            AlphabetLetter(
                letter = "J",
                description = "Kelingking tegak lurus kemudian bergerak membentuk huruf J di udara.",
                imageResId = R.drawable.bisindo_j  // Ganti dengan R.drawable.bisindo_j
            ),
            AlphabetLetter(
                letter = "K",
                description = "Jari telunjuk tegak, jari tengah menyentuh ibu jari, jari lainnya menekuk.",
                imageResId = R.drawable.bisindo_k  // Ganti dengan R.drawable.bisindo_k
            ),
            AlphabetLetter(
                letter = "L",
                description = "Jari telunjuk dan ibu jari membentuk huruf L dengan sudut 90 derajat.",
                imageResId = R.drawable.bisindo_l // Ganti dengan R.drawable.bisindo_l
            ),
            AlphabetLetter(
                letter = "M",
                description = "Tiga jari pertama (telunjuk, tengah, manis) diletakkan di atas ibu jari.",
                imageResId = R.drawable.bisindo_m  // Ganti dengan R.drawable.bisindo_m
            ),
            AlphabetLetter(
                letter = "N",
                description = "Dua jari pertama (telunjuk dan tengah) diletakkan di atas ibu jari.",
                imageResId = R.drawable.bisindo_n  // Ganti dengan R.drawable.bisindo_n
            ),
            AlphabetLetter(
                letter = "O",
                description = "Semua ujung jari bertemu membentuk lingkaran seperti huruf O.",
                imageResId = R.drawable.bisindo_o  // Ganti dengan R.drawable.bisindo_o
            ),
            AlphabetLetter(
                letter = "P",
                description = "Seperti K tetapi tangan mengarah ke bawah dengan jari telunjuk dan tengah membentang.",
                imageResId = R.drawable.bisindo_p  // Ganti dengan R.drawable.bisindo_p
            ),
            AlphabetLetter(
                letter = "Q",
                description = "Jari telunjuk dan ibu jari mengarah ke bawah dengan jari lainnya menekuk.",
                imageResId = R.drawable.bisindo_q  // Ganti dengan R.drawable.bisindo_q
            ),
            AlphabetLetter(
                letter = "R",
                description = "Jari telunjuk dan jari tengah menyilang dengan jari telunjuk di atas.",
                imageResId = R.drawable.bisindo_r  // Ganti dengan R.drawable.bisindo_r
            ),
            AlphabetLetter(
                letter = "S",
                description = "Tangan mengepal dengan ibu jari berada di depan jari-jari yang menekuk.",
                imageResId = R.drawable.bisindo_s  // Ganti dengan R.drawable.bisindo_s
            ),
            AlphabetLetter(
                letter = "T",
                description = "Ibu jari dijepit di antara jari telunjuk dan jari tengah.",
                imageResId = R.drawable.bisindo_t  // Ganti dengan R.drawable.bisindo_t
            ),
            AlphabetLetter(
                letter = "U",
                description = "Jari telunjuk dan jari tengah tegak lurus ke atas, rapat berdampingan.",
                imageResId = R.drawable.bisindo_u  // Ganti dengan R.drawable.bisindo_u
            ),
            AlphabetLetter(
                letter = "V",
                description = "Jari telunjuk dan jari tengah tegak membentuk huruf V dengan jari terpisah.",
                imageResId = R.drawable.bisindo_v  // Ganti dengan R.drawable.bisindo_v
            ),
            AlphabetLetter(
                letter = "W",
                description = "Tiga jari (telunjuk, tengah, manis) tegak membentuk huruf W dengan jari terpisah.",
                imageResId = R.drawable.bisindo_w  // Ganti dengan R.drawable.bisindo_w
            ),
            AlphabetLetter(
                letter = "X",
                description = "Jari telunjuk menekuk seperti kait dengan ujung jari menghadap ke atas.",
                imageResId = R.drawable.bisindo_x  // Ganti dengan R.drawable.bisindo_x
            ),
            AlphabetLetter(
                letter = "Y",
                description = "Ibu jari dan kelingking tegak membentang, jari-jari lain menekuk.",
                imageResId = R.drawable.bisindo_y  // Ganti dengan R.drawable.bisindo_y
            ),
            AlphabetLetter(
                letter = "Z",
                description = "Jari telunjuk tegak kemudian bergerak membentuk zigzag seperti huruf Z di udara.",
                imageResId = R.drawable.bisindo_z  // Ganti dengan R.drawable.bisindo_z
            )
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7FBFF),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar dengan hamburger menu
            TopAppBar(
                title = {
                    Text(
                        text = "BISINDO Learn",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = deepBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                // Hero
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(primaryBlue, Color(0xFF6EC3FF))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Daftar Huruf BISINDO",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "(A–Z)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "Ketuk kartu untuk melihat isyarat dan penjelasan.",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BadgePill(text = "26 Huruf", background = Color.White.copy(alpha = 0.18f))
                                BadgePill(text = "BISINDO", background = Color(0xFFFFF6DF), textColor = Color(0xFF8A6B00))
                            }
                        }
                    }
                }
                
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
    val primaryBlue = Color(0xFF2D9CDB)
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { onClick(letter) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = letter.letter,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                BadgePill(text = "Tap", background = primaryBlue.copy(alpha = 0.12f), textColor = primaryBlue)
            }
            
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE3ECF5), RoundedCornerShape(8.dp))
                    .background(Color(0xFFF7FBFF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = letter.imageResId),
                    contentDescription = "Huruf ${letter.letter}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun LetterDetailDialog(
    letter: AlphabetLetter,
    onDismiss: () -> Unit
) {
    val primaryBlue = Color(0xFF2D9CDB)
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        colors = listOf(Color.White, Color(0xFFF7FBFF))
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
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
                            .border(2.dp, Color(0xFFE3ECF5), RoundedCornerShape(12.dp))
                            .background(Color(0xFFF7FBFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = letter.imageResId),
                            contentDescription = "Huruf ${letter.letter}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title
                    Text(
                        text = "Huruf ${letter.letter}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Description
                    Text(
                        text = letter.description,
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgePill(
    text: String,
    background: Color,
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .background(background, shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

