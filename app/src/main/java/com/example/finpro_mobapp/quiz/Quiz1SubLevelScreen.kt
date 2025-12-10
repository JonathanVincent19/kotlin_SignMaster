package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF7FBFF),
            Color(0xFFE8F3FF)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Level ${level.id}: ${level.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Pilih Sub-Level",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43)
                    )
            Text(
                        text = "Atur kecepatan dan jumlah soal sesuai target belajarmu.",
                        fontSize = 14.sp,
                        color = Color(0xFF52616B)
            )
                }
            
            level.subLevels.forEach { subLevel ->
                SubLevelCard(
                    subLevel = subLevel,
                    hasInProgressQuiz = progressManager.hasProgress(subLevel.id),
                    onClick = { onSubLevelClick(subLevel) }
                )
                }
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
    val baseBorder = when (subLevel.status) {
        SubLevelStatus.COMPLETED -> Color(0xFF27AE60).copy(alpha = 0.25f)
        SubLevelStatus.ON_PROGRESS -> Color(0xFFF2C94C).copy(alpha = 0.35f)
        SubLevelStatus.LOCKED -> Color(0xFFB0BEC5).copy(alpha = 0.35f)
        SubLevelStatus.NOT_STARTED -> Color(0xFF2D9CDB).copy(alpha = 0.2f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        border = BorderStroke(1.dp, baseBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = subLevel.icon,
                        fontSize = 26.sp
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "${subLevel.id} ${subLevel.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                            color = Color(0xFF102A43)
                    )
                        Text(
                            text = "${subLevel.totalQuestions} soal • ${subLevel.displayDuration / 1000}s per huruf",
                            fontSize = 13.sp,
                            color = Color(0xFF52616B)
                        )
                    }
                }

                when (subLevel.status) {
                    SubLevelStatus.COMPLETED -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color(0xFF27AE60)
                        )
                    }
                    SubLevelStatus.ON_PROGRESS -> {
                        Text(
                            text = "⏱️",
                            fontSize = 20.sp
                        )
                    }
                    SubLevelStatus.LOCKED -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color(0xFF9EABB3)
                        )
                    }
                    SubLevelStatus.NOT_STARTED -> {}
                }
            }
            
            // Details line
                Text(
                text = getWordLength(subLevel),
                    fontSize = 14.sp,
                color = Color(0xFF52616B)
                )
            
            // Progress info
            if (subLevel.isUnlocked && subLevel.status != SubLevelStatus.NOT_STARTED) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statusColor = if (subLevel.status == SubLevelStatus.COMPLETED) Color(0xFF27AE60) else Color(0xFFF2C94C)
                    Box(
                        modifier = Modifier
                            .background(
                                color = statusColor.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                Text(
                            text = "Progress ${subLevel.completedQuestions}/${subLevel.totalQuestions}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = statusColor
                )
                    }
                }
            }
            
            // Button or lock message
            when {
                !subLevel.isUnlocked -> {
                    Text(
                        text = "Selesaikan ${getPreviousSubLevelId(subLevel.id)} dulu",
                        fontSize = 13.sp,
                        color = Color(0xFF9EABB3),
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
                            containerColor = Color(0xFFF2C94C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjutkan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = Color(0xFF102A43)
                        )
                    }
                }
                else -> {
                    Button(
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D9CDB)
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

