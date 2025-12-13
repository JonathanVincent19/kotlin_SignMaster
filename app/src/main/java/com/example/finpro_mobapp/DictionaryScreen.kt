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
                description = "Bentuk segitiga dengan ujung jari telunjuk dan jempol saling menyentuh, membentuk huruf A yang elegan.",
                imageResId = R.drawable.bisindo_a
            ),
            AlphabetLetter(
                letter = "B",
                description = "Telapak tangan terbuka lebar dengan kelima jari rapat dan lurus, sementara ibu jari dilipat ke samping menempel pada telapak tangan.",
                imageResId = R.drawable.bisindo_b
            ),
            AlphabetLetter(
                letter = "C",
                description = "Jari-jari melengkung membentuk setengah lingkaran yang menyerupai bentuk huruf C, dengan telapak tangan menghadap ke depan.",
                imageResId = R.drawable.bisindo_c
            ),
            AlphabetLetter(
                letter = "D",
                description = "Jari telunjuk berdiri tegak ke atas seperti menunjuk langit, sementara jari lainnya menekuk dengan ibu jari menempel pada jari tengah.",
                imageResId = R.drawable.bisindo_d
            ),
            AlphabetLetter(
                letter = "E",
                description = "Semua jari ditekuk ke dalam menyerupai cakar, dengan ujung-ujung jari menyentuh bagian tengah telapak tangan membentuk kepalan yang rapat.",
                imageResId = R.drawable.bisindo_e
            ),
            AlphabetLetter(
                letter = "F",
                description = "Jari telunjuk dan ibu jari saling bertemu membentuk lingkaran sempurna, sementara tiga jari lainnya (tengah, manis, kelingking) berdiri tegak dan rapat.",
                imageResId = R.drawable.bisindo_f
            ),
            AlphabetLetter(
                letter = "G",
                description = "Jari telunjuk dan ibu jari direntangkan ke samping secara horizontal, membentuk garis lurus seperti sedang menunjuk ke kiri dan kanan secara bersamaan.",
                imageResId = R.drawable.bisindo_g
            ),
            AlphabetLetter(
                letter = "H",
                description = "Jari telunjuk dan jari tengah direntangkan ke samping secara horizontal dan sejajar, menciptakan bentuk seperti dua batang yang berdampingan.",
                imageResId = R.drawable.bisindo_h
            ),
            AlphabetLetter(
                letter = "I",
                description = "Hanya kelingking yang berdiri tegak ke atas, sementara jari-jari lainnya menekuk dengan ibu jari diletakkan di atas jari tengah seperti sedang memegang sesuatu.",
                imageResId = R.drawable.bisindo_i
            ),
            AlphabetLetter(
                letter = "J",
                description = "Dimulai dengan kelingking yang berdiri tegak, kemudian tangan bergerak membentuk lengkungan ke bawah dan ke kanan, menelusuri bentuk huruf J di udara.",
                imageResId = R.drawable.bisindo_j
            ),
            AlphabetLetter(
                letter = "K",
                description = "Jari telunjuk berdiri tegak seperti tiang, sementara jari tengah ditekuk menyentuh bagian dalam ibu jari, dan jari lainnya menekuk rapat.",
                imageResId = R.drawable.bisindo_k
            ),
            AlphabetLetter(
                letter = "L",
                description = "Jari telunjuk dan ibu jari membentuk sudut siku-siku 90 derajat, menciptakan bentuk L yang jelas dengan telunjuk ke atas dan ibu jari ke samping.",
                imageResId = R.drawable.bisindo_l
            ),
            AlphabetLetter(
                letter = "M",
                description = "Tiga jari terdepan (telunjuk, tengah, dan manis) ditekuk dan diletakkan di atas permukaan ibu jari, membentuk pola seperti tiga puncak gunung.",
                imageResId = R.drawable.bisindo_m
            ),
            AlphabetLetter(
                letter = "N",
                description = "Dua jari terdepan (telunjuk dan tengah) ditekuk dan diletakkan di atas ibu jari, menciptakan bentuk seperti dua puncak yang lebih sederhana dari huruf M.",
                imageResId = R.drawable.bisindo_n
            ),
            AlphabetLetter(
                letter = "O",
                description = "Semua ujung jari dari kedua tangan atau satu tangan saling bertemu membentuk lingkaran sempurna, menyerupai bentuk bulat huruf O yang utuh.",
                imageResId = R.drawable.bisindo_o
            ),
            AlphabetLetter(
                letter = "P",
                description = "Mirip dengan huruf K namun posisi tangan menghadap ke bawah, dengan jari telunjuk dan tengah direntangkan ke bawah membentuk garis vertikal.",
                imageResId = R.drawable.bisindo_p
            ),
            AlphabetLetter(
                letter = "Q",
                description = "Jari telunjuk dan ibu jari direntangkan ke bawah membentuk sudut, sementara jari lainnya menekuk ke dalam, menciptakan bentuk seperti kait yang mengarah ke bawah.",
                imageResId = R.drawable.bisindo_q
            ),
            AlphabetLetter(
                letter = "R",
                description = "Jari telunjuk dan jari tengah saling menyilang dengan jari telunjuk berada di atas jari tengah, membentuk pola silang yang khas seperti huruf R.",
                imageResId = R.drawable.bisindo_r
            ),
            AlphabetLetter(
                letter = "S",
                description = "Tangan mengepal rapat dengan semua jari menekuk ke dalam, sementara ibu jari diletakkan di depan menutupi jari-jari yang menekuk, seperti kepalan tinju yang rapat.",
                imageResId = R.drawable.bisindo_s
            ),
            AlphabetLetter(
                letter = "T",
                description = "Ibu jari dijepit dengan rapat di antara jari telunjuk dan jari tengah, menciptakan bentuk seperti huruf T dengan garis horizontal dan vertikal.",
                imageResId = R.drawable.bisindo_t
            ),
            AlphabetLetter(
                letter = "U",
                description = "Jari telunjuk dan jari tengah berdiri tegak ke atas dan rapat berdampingan seperti dua batang yang sejajar, sementara jari lainnya menekuk.",
                imageResId = R.drawable.bisindo_u
            ),
            AlphabetLetter(
                letter = "V",
                description = "Jari telunjuk dan jari tengah berdiri tegak membentuk sudut seperti huruf V, dengan kedua jari terpisah membentuk bentuk kemenangan yang klasik.",
                imageResId = R.drawable.bisindo_v
            ),
            AlphabetLetter(
                letter = "W",
                description = "Tiga jari (telunjuk, tengah, dan manis) berdiri tegak dengan jarak yang terpisah, menciptakan pola seperti tiga puncak yang membentuk huruf W.",
                imageResId = R.drawable.bisindo_w
            ),
            AlphabetLetter(
                letter = "X",
                description = "Jari telunjuk ditekuk membentuk kait dengan ujung jari menghadap ke atas, menciptakan bentuk seperti huruf X dengan garis yang menyilang.",
                imageResId = R.drawable.bisindo_x
            ),
            AlphabetLetter(
                letter = "Y",
                description = "Ibu jari dan kelingking direntangkan ke samping membentuk garis horizontal, sementara tiga jari tengah menekuk ke dalam, seperti bentuk Y yang unik.",
                imageResId = R.drawable.bisindo_y
            ),
            AlphabetLetter(
                letter = "Z",
                description = "Jari telunjuk mulai dari posisi tegak, kemudian bergerak membentuk tiga garis yang saling terhubung: horizontal, diagonal, dan horizontal lagi, menelusuri bentuk zigzag huruf Z di udara.",
                imageResId = R.drawable.bisindo_z
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
                            .clip(RoundedCornerShape(18.dp))
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

