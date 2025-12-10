package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
fun Quiz1MainLevelScreen(
    levels: List<MainLevel>,
    onBackClick: () -> Unit,
    onLevelClick: (MainLevel) -> Unit
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
                        "Quiz 1: Tebak Isyarat",
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
                        text = "Pilih Level Utama",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43)
                    )
            Text(
                        text = "Mulai dari dasar hingga mahir, pilih level yang sudah terbuka.",
                        fontSize = 14.sp,
                        color = Color(0xFF52616B)
            )
                }
            
            levels.forEach { level ->
                MainLevelCard(
                    level = level,
                    onClick = { onLevelClick(level) }
                )
                }
            }
        }
    }
}

@Composable
private fun MainLevelCard(
    level: MainLevel,
    onClick: () -> Unit
) {
    val containerColor = Color.White
    val borderColor = when {
        level.status == LevelStatus.COMPLETED -> Color(0xFF27AE60).copy(alpha = 0.24f)
        level.status == LevelStatus.ON_PROGRESS -> Color(0xFFF2C94C).copy(alpha = 0.35f)
        else -> Color(0xFFB0BEC5).copy(alpha = 0.35f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        border = BorderStroke(1.dp, borderColor)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = level.icon,
                        fontSize = 30.sp
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "LEVEL ${level.id}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF102A43)
                        )
                        Text(
                            text = level.name,
                            fontSize = 14.sp,
                            color = Color(0xFF52616B)
                        )
                    }
                }
                
                if (!level.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF9EABB3)
                    )
                }
            }
            
            // Status chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val statusColor = when (level.status) {
                    LevelStatus.COMPLETED -> Color(0xFF27AE60)
                    LevelStatus.ON_PROGRESS -> Color(0xFFF2C94C)
                    LevelStatus.LOCKED -> Color(0xFF9EABB3)
                }
                val statusText = when (level.status) {
                    LevelStatus.COMPLETED -> "Selesai"
                    LevelStatus.ON_PROGRESS -> "On Progress"
                    LevelStatus.LOCKED -> "Locked"
                }
                val iconVector = when (level.status) {
                    LevelStatus.COMPLETED -> Icons.Filled.CheckCircle
                    LevelStatus.ON_PROGRESS -> null
                    LevelStatus.LOCKED -> Icons.Filled.Lock
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(
                            color = statusColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (iconVector != null) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = statusColor
                        )
                    } else {
                        Text(
                            text = "⏱️",
                            fontSize = 16.sp
                        )
                    }
                Text(
                        text = statusText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor
                    )
                    }
            }
            
            // Button or lock message
            if (level.isUnlocked) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D9CDB)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Pilih Sub-Level",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "Selesaikan Level ${level.id - 1} dulu",
                    fontSize = 13.sp,
                    color = Color(0xFF9EABB3),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

