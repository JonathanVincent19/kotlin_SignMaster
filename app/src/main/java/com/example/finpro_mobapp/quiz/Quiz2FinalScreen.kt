package com.example.finpro_mobapp.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Quiz2FinalScreen(
    level: Int,
    completedQuestions: Int,
    totalQuestions: Int,
    isNextLevelUnlocked: Boolean,
    onBackToHome: () -> Unit,
    onRetry: () -> Unit,
    onNextLevel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "🎉 YEAYY SELESAI!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF27AE60),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Score Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Level $level",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7F8C8D),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "$completedQuestions/$totalQuestions",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A90E2),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "GOOD JOB!!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27AE60),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Unlock notification
        if (isNextLevelUnlocked) {
            Text(
                text = "🔓 Level ${level + 1} Terbuka!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFF39C12),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
        } else {
            Spacer(modifier = Modifier.height(40.dp))
        }
        
        // Navigation buttons
        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "🏠", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
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
                containerColor = Color(0xFF95A5A6)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "🔄", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = "Coba Lagi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        if (isNextLevelUnlocked) {
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onNextLevel,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27AE60)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "⬆️", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(
                        text = "Level Selanjutnya",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

