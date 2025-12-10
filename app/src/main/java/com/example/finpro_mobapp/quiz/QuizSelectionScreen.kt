package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSelectionScreen(
    onMenuClick: () -> Unit,
    onQuiz1Click: () -> Unit,
    onQuiz2Click: () -> Unit
) {
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF7FBFF),
            Color(0xFFE8F3FF),
            Color(0xFFFFFBF0)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Latihan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color(0xFF102A43)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Hero Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Pilih Jenis Quiz",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43)
                    )
                    Text(
                        text = "Latih kemampuanmu dengan berbagai jenis quiz interaktif",
                        fontSize = 14.sp,
                        color = Color(0xFF52616B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quiz 1 Card
                QuizCard(
                    emoji = "📷",
                    title = "Quiz 1",
                    subtitle = "Tebak Huruf dari Gambar",
                    features = listOf(
                        "Multiple levels",
                        "Tingkatkan kecepatanmu!"
                    ),
                    iconColor = Color(0xFF2D9CDB),
                    onClick = onQuiz1Click
                )

                // Quiz 2 Card
                QuizCard(
                    emoji = "🎥",
                    title = "Quiz 2",
                    subtitle = "Peragakan Isyarat (Camera)",
                    features = listOf(
                        "AI Recognition",
                        "Latih gerakanmu!"
                    ),
                    iconColor = Color(0xFFF2C94C),
                    onClick = onQuiz2Click,
                    enabled = true
                )
            }
        }
    }
}

@Composable
private fun QuizCard(
    emoji: String,
    title: String,
    subtitle: String,
    features: List<String>,
    iconColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Icon Box
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 32.sp
                    )
                }

                // Title Section
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color(0xFF52616B)
                    )
                }
            }

            // Features
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(iconColor)
                        )
                        Text(
                            text = feature,
                            fontSize = 14.sp,
                            color = Color(0xFF52616B)
                        )
                    }
                }
            }

            // Button
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (enabled) Color(0xFF2D9CDB) else Color(0xFFBDC3C7)
                ),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(
                    text = if (enabled) "Mulai Quiz" else "Coming Soon",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

