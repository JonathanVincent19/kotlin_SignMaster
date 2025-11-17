package com.example.finpro_mobapp.quiz

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSelectionScreen(
    onMenuClick: () -> Unit,
    onQuiz1Click: () -> Unit,
    onQuiz2Click: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Latihan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Pilih Jenis Quiz",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Quiz 1 Card
            QuizCard(
                emoji = "📷",
                title = "Quiz 1",
                subtitle = "Tebak Huruf dari Gambar",
                features = listOf(
                    "Multiple levels",
                    "Tingkatkan kecepatanmu!"
                ),
                onClick = onQuiz1Click
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Quiz 2 Card
            QuizCard(
                emoji = "🎥",
                title = "Quiz 2",
                subtitle = "Peragakan Isyarat (Camera)",
                features = listOf(
                    "AI Recognition",
                    "Latih gerakanmu!"
                ),
                onClick = onQuiz2Click,
                enabled = false // Coming soon
            )
        }
    }
}

@Composable
private fun QuizCard(
    emoji: String,
    title: String,
    subtitle: String,
    features: List<String>,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = emoji,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color(0xFF7F8C8D)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Features
            features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "• ",
                        color = Color(0xFF4A90E2),
                        fontSize = 14.sp
                    )
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        color = Color(0xFF5D6D7E)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Button
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    disabledContainerColor = Color(0xFFBDC3C7)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (enabled) "Mulai Quiz" else "Coming Soon",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

