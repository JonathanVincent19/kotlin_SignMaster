package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
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
    levelsUnlockStatus: Map<Int, Boolean>,
    levelsCompletionStatus: Map<Int, Boolean>,
    onLevelClick: (Int) -> Unit
) {
    val levels = listOf(
        LevelInfo(
            number = 1, 
            icon = "🟢", 
            title = "Level 1", 
            subtitle = "Peragakan 1 Huruf", 
            description = "20 soal huruf A-Z",
            isUnlocked = levelsUnlockStatus[1] ?: true,
            isCompleted = levelsCompletionStatus[1] ?: false
        ),
        LevelInfo(
            number = 2, 
            icon = "🟡", 
            title = "Level 2", 
            subtitle = "Peragakan 1 Kata", 
            description = "15 soal kata (5-7 huruf)",
            isUnlocked = levelsUnlockStatus[2] ?: false,
            isCompleted = levelsCompletionStatus[2] ?: false
        ),
        LevelInfo(
            number = 3, 
            icon = "🔴", 
            title = "Level 3", 
            subtitle = "Peragakan 2 Kata", 
            description = "12 soal kalimat (2 kata)",
            isUnlocked = levelsUnlockStatus[3] ?: false,
            isCompleted = levelsCompletionStatus[3] ?: false
        )
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
    val description: String,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false
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
            containerColor = if (level.isUnlocked) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with lock icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
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
                            color = if (level.isUnlocked) Color(0xFF2C3E50) else Color(0xFF95A5A6)
                        )
                        Text(
                            text = level.subtitle,
                            fontSize = 14.sp,
                            color = Color(0xFF7F8C8D)
                        )
                    }
                }
                
                // Lock/Completed icon
                if (!level.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF95A5A6),
                        modifier = Modifier.size(28.dp)
                    )
                } else if (level.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color(0xFF27AE60),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Description
            Text(
                text = level.description,
                fontSize = 14.sp,
                color = if (level.isUnlocked) Color(0xFF5D6D7E) else Color(0xFFBDC3C7),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Status badge
            if (level.isCompleted) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "✓",
                        fontSize = 16.sp,
                        color = Color(0xFF27AE60),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "Selesai",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF27AE60)
                    )
                }
            }
            
            // Button or lock message
            if (level.isUnlocked) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (level.isCompleted) "Main Lagi" else "Mulai",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "🔒 Selesaikan Level ${level.number - 1} dulu",
                    fontSize = 13.sp,
                    color = Color(0xFF95A5A6),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

