package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun Quiz2LevelSelectionScreen(
    onBackClick: () -> Unit,
    onLevelClick: (Int) -> Unit
) {
    val levels = listOf(
        LevelInfo(1, "🟢", "Level 1", "Peragakan 1 Huruf", "10 soal huruf A-J"),
        LevelInfo(2, "🟡", "Level 2", "Peragakan 1 Kata", "8 soal kata sederhana"),
        LevelInfo(3, "🔴", "Level 3", "Peragakan 2 Kata", "6 soal kalimat pendek")
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quiz 2: Peragakan Isyarat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
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
                text = "Pilih Level",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            levels.forEach { level ->
                LevelCard(
                    level = level,
                    onClick = { onLevelClick(level.number) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

private data class LevelInfo(
    val number: Int,
    val icon: String,
    val title: String,
    val subtitle: String,
    val description: String
)

@Composable
private fun LevelCard(
    level: LevelInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = level.icon,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = level.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = level.subtitle,
                        fontSize = 14.sp,
                        color = Color(0xFF7F8C8D)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = level.description,
                fontSize = 14.sp,
                color = Color(0xFF5D6D7E),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Mulai",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

