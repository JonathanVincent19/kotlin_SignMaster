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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finpro_mobapp.ui.theme.FINPRO_MOBAPPTheme
import java.util.*

// Daily tips data
private val dailyTips = listOf(
    "Latihan 10 menit setiap hari lebih efektif dari 1 jam seminggu sekali! 💪",
    "Mulai dari huruf vokal A, I, U, E, O - lebih mudah diingat! 🎯",
    "Jangan takut salah! Kesalahan adalah bagian dari belajar 🌟",
    "Praktikkan di depan cermin untuk melihat gerakan tangan Anda 🪞",
    "Ajak teman atau keluarga untuk belajar bersama! 👥",
    "Konsistensi adalah kunci - belajar sedikit tapi rutin! 🔑",
    "Tonton video bahasa isyarat untuk melihat gerakan yang natural 📹",
    "Buat catatan kecil untuk huruf yang sulit diingat 📝",
    "Istirahat sejenak jika merasa lelah, jangan dipaksakan! 😌",
    "Gunakan lagu favorit sebagai cara mengingat huruf 🎵",
    "Setiap orang punya kecepatan belajar sendiri, santai saja! 🐢",
    "Rayakan progress kecilmu, sekecil apapun itu! 🎉",
    "Bergabung dengan komunitas bahasa isyarat untuk praktik lebih banyak 🤝",
    "Jangan lupa review huruf yang sudah dipelajari kemarin 🔄",
    "Fokus pada kualitas gerakan, bukan kecepatan 🎯",
    "Manfaatkan waktu luang untuk latihan singkat 5 menit ⏰",
    "Belajar bahasa isyarat adalah bentuk empati yang indah 💝",
    "Kesalahan hari ini adalah pembelajaran untuk besok! 📈",
    "Buatlah target kecil yang realistis setiap hari 🎯",
    "Bahasa isyarat membuka dunia baru untuk berkomunikasi! 🌍",
    "Latihan di berbagai situasi membuat ingatan lebih kuat 💪",
    "Jangan bandingkan progress-mu dengan orang lain 🙏",
    "Apresiasi diri sendiri untuk setiap usaha yang kamu lakukan! ⭐",
    "Belajar dengan hati yang senang akan lebih mudah diserap 😊",
    "Ingat: kamu sedang melakukan hal yang luar biasa! 🚀"
)

// Get daily tip based on day of year
private fun getDailyTip(): String {
    val calendar = Calendar.getInstance()
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    return dailyTips[dayOfYear % dailyTips.size]
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDictionary: () -> Unit = {},
    onNavigateToQuiz: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    // Background with visible gradient
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),  // Light blue top - more visible
                        Color(0xFFFFFFFF),  // White middle
                        Color(0xFFFFF3E0)   // Light warm bottom
                    ),
                    startY = 0f,
                    endY = 2000f
                )
            )
    ) {
        // Subtle pattern overlay
        Image(
            painter = painterResource(id = R.drawable.bg3),
            contentDescription = "Background Pattern",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.03f),  // Slightly visible
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Placeholder
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(35.dp),
                    contentScale = ContentScale.Fit
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Hamburger Menu
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF2C3E50),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            // Greeting Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "👋 Hai Jojo!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sudah belajar hari ini?",
                    fontSize = 16.sp,
                    color = Color(0xFF7F8C8D)
                )
            }
            
            // Main Banner
            HeroBanner(onNavigateToDictionary = onNavigateToDictionary)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Akses Cepat Section
            Text(
                text = "🚀 Akses Cepat",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QuickAccessCard(
                    title = "Quiz",
                    emoji = "✏️",
                    onClick = onNavigateToQuiz,
                    modifier = Modifier.weight(1f)
                )
                
                QuickAccessCard(
                    title = "Kamus",
                    emoji = "📚",
                    onClick = onNavigateToDictionary,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Statistics Section
            StatisticsSection()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Daily Tip Section
            DailyTipSection()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Donation Banner
            DonationBanner()
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeroBanner(onNavigateToDictionary: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF4A90E2),
                            Color(0xFF357ABD)
                        )
                    )
                )
        ) {
            // Background pattern
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "Banner background",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.15f),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                // Text Section
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎯",
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ayo Mulai",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Petualangan\nBelajarmu!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 38.sp,
                        letterSpacing = 0.5.sp
                    )
                }
                
                // Button Section
                Button(
                    onClick = onNavigateToDictionary,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Mulai Belajar",
                        color = Color(0xFF4A90E2),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Arrow",
                        tint = Color(0xFF4A90E2),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    title: String,
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50)
            )
        }
    }
}

@Composable
fun StatisticsSection() {
    Column {
        Text(
            text = "⭐ Statistik Belajarmu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Level Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "📊",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    
                    Column {
                        Text(
                            text = "Level Sekarang",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2C3E50)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coming Soon",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A90E2)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fitur level akan tersedia saat quiz sudah siap!",
                            fontSize = 13.sp,
                            color = Color(0xFF7F8C8D),
                            lineHeight = 18.sp
                        )
                    }
                }
                
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color(0xFFE0E0E0)
                )
                
                // Streak Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    
                    Column {
                        Text(
                            text = "Streak Belajar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2C3E50)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coming Soon",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B35)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mulai streak-mu setelah quiz tersedia!",
                            fontSize = 13.sp,
                            color = Color(0xFF7F8C8D),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyTipSection() {
    Column {
        Text(
            text = "💡 Tips Hari Ini",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF8E1)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "💡",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 12.dp, top = 2.dp)
                )
                
                Text(
                    text = getDailyTip(),
                    fontSize = 15.sp,
                    color = Color(0xFF5D4037),
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DonationBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.orang),
                contentDescription = "Donation",
                modifier = Modifier
                    .width(100.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "💝 Dukung Kami",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Cerahkan dunia dengan ramah disabilitas, yuk donasi untuk bantu kami!",
                    fontSize = 13.sp,
                    color = Color(0xFF7F8C8D),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    FINPRO_MOBAPPTheme {
        HomeScreen()
    }
}

