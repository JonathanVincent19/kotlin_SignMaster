package com.example.finpro_mobapp.quiz

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Quiz1FinalScreen(
    subLevel: SubLevel,
    completedQuestions: Int,
    totalQuestions: Int,
    isNextSubLevelUnlocked: Boolean = false,
    nextSubLevelId: String? = null,
    onBackToHome: () -> Unit,
    onRetry: () -> Unit,
    onNextLevel: () -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF7FBFF),
            Color(0xFFE8F3FF),
            Color(0xFFFFFBF0)
        )
    )

    val cardGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2D9CDB),
            Color(0xFF4A90E2),
            Color(0xFFF2C94C)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.padding(top = 4.dp)
                )
        Text(
                    text = "YEAYY SELESAI!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A43),
            textAlign = TextAlign.Center
        )
            }
        
            Spacer(modifier = Modifier.height(24.dp))
        
        // Score Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
            ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(cardGradient, RoundedCornerShape(20.dp))
                        .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                            .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Level ${subLevel.id} - ${subLevel.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                
                        Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "$completedQuestions/$totalQuestions",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                        Spacer(modifier = Modifier.height(12.dp))
                
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFFB2FF59)
                            )
                Text(
                    text = "GOOD JOB!!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                                color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
                    }
                }
        }
        
            Spacer(modifier = Modifier.height(20.dp))
        
            // Unlock notifications (success only)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(
                        color = Color(0xFF2D9CDB).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2D9CDB)
                )
            Text(
                    text = "Sub-Level ${subLevel.id} selesai!",
                fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF102A43)
            )
            }
            
            if (isNextSubLevelUnlocked && nextSubLevelId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sub-Level $nextSubLevelId terbuka! Gas lanjut! 🎯",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFF2C94C),
                    textAlign = TextAlign.Center
                )
        }
        
            Spacer(modifier = Modifier.height(32.dp))
        
        // Navigation buttons
        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D9CDB)
            ),
                shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
            ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kembali ke Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF52616B)
            ),
                shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
            ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Coba Lagi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (isNextSubLevelUnlocked) {
            Button(
                onClick = onNextLevel,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2C94C)
                ),
                    shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 10.dp)
                ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF102A43)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Level Selanjutnya",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF102A43)
                    )
                    }
                }
            }
        }
    }
}

