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
fun Quiz1SubLevelScreen(
    level: MainLevel,
    progressManager: QuizProgressManager,
    onBackClick: () -> Unit,
    onSubLevelClick: (SubLevel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Level ${level.id}: ${level.name}",
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
                text = "Pilih Kecepatan",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            level.subLevels.forEach { subLevel ->
                SubLevelCard(
                    subLevel = subLevel,
                    hasInProgressQuiz = progressManager.hasProgress(subLevel.id),
                    onClick = { onSubLevelClick(subLevel) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SubLevelCard(
    subLevel: SubLevel,
    hasInProgressQuiz: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (subLevel.isUnlocked) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = subLevel.icon,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "${subLevel.id} ${subLevel.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                }
                
                // Status icon - Modern Material Icons
                when (subLevel.status) {
                    SubLevelStatus.COMPLETED -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color(0xFF27AE60),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    SubLevelStatus.ON_PROGRESS -> {
                        Text(
                            text = "⏱️",
                            fontSize = 24.sp
                        )
                    }
                    SubLevelStatus.LOCKED -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color(0xFF95A5A6),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    SubLevelStatus.NOT_STARTED -> {
                        // No icon for not started
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Details
            listOf(
                "• ${subLevel.totalQuestions} soal ${getQuestionType(subLevel.parentLevelId)}",
                "• ${subLevel.displayDuration / 1000} detik per huruf",
                "• ${getWordLength(subLevel)}"
            ).forEach { detail ->
                Text(
                    text = detail,
                    fontSize = 14.sp,
                    color = Color(0xFF5D6D7E),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Questions progress
            if (subLevel.isUnlocked && subLevel.status != SubLevelStatus.NOT_STARTED) {
                Text(
                    text = "Questions: ${subLevel.completedQuestions}/${subLevel.totalQuestions}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (subLevel.status == SubLevelStatus.COMPLETED) 
                        Color(0xFF27AE60) else Color(0xFFF39C12)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Button or lock message
            when {
                !subLevel.isUnlocked -> {
                    Text(
                        text = "Selesaikan ${getPreviousSubLevelId(subLevel.id)} dulu",
                        fontSize = 13.sp,
                        color = Color(0xFF95A5A6),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                subLevel.status == SubLevelStatus.COMPLETED -> {
                    Button(
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF27AE60)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Main Lagi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
                hasInProgressQuiz || subLevel.status == SubLevelStatus.ON_PROGRESS -> {
                    Button(
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF39C12)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjutkan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
                else -> {
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
    }
}

private fun getQuestionType(parentLevelId: Int): String {
    return when(parentLevelId) {
        1 -> "huruf"
        2 -> "kata"
        3 -> "kalimat"
        else -> ""
    }
}

private fun getWordLength(subLevel: SubLevel): String {
    return when(subLevel.id) {
        "1.1" -> "Huruf A-J"
        "1.2" -> "Huruf A-P"
        "1.3" -> "Huruf A-Z"
        "2.1" -> "Kata 3-4 huruf"
        "2.2" -> "Kata 4-5 huruf"
        "2.3" -> "Kata 5-6 huruf"
        "3.1" -> "2 kata pendek"
        "3.2" -> "2 kata sedang"
        "3.3" -> "2 kata panjang"
        else -> ""
    }
}

private fun getPreviousSubLevelId(currentId: String): String {
    val parts = currentId.split(".")
    val levelNum = parts[0].toInt()
    val subNum = parts[1].toInt()
    return if (subNum > 1) {
        "$levelNum.${subNum - 1}"
    } else {
        "${levelNum - 1}.3"
    }
}

