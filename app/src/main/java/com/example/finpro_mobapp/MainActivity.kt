package com.example.finpro_mobapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finpro_mobapp.ui.theme.FINPRO_MOBAPPTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FINPRO_MOBAPPTheme {
                var showSplash by remember { mutableStateOf(true) }
                
                if (showSplash) {
                    SplashScreen(onTimeout = { showSplash = false })
                } else {
                    AppNavigation()
                }
            }
        }
    }
}

// Navigation State
enum class Screen {
    HOME, DICTIONARY, QUIZ
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF4A90E2)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Beranda
                NavigationDrawerItem(
                    icon = { Text("🏠", fontSize = 24.sp) },
                    label = { 
                        Text(
                            "Beranda", 
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    selected = currentScreen == Screen.HOME,
                    onClick = {
                        currentScreen = Screen.HOME
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.White.copy(alpha = 0.2f),
                        unselectedContainerColor = Color.Transparent
                    )
                )
                
                // Alfabet
                NavigationDrawerItem(
                    icon = { Text("Aa", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold) },
                    label = { 
                        Text(
                            "Alfabet", 
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    selected = currentScreen == Screen.DICTIONARY,
                    onClick = {
                        currentScreen = Screen.DICTIONARY
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.White.copy(alpha = 0.2f),
                        unselectedContainerColor = Color.Transparent
                    )
                )
                
                // Latihan (Quiz)
                NavigationDrawerItem(
                    icon = { Text("📚", fontSize = 24.sp) },
                    label = { 
                        Text(
                            "Latihan", 
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    selected = currentScreen == Screen.QUIZ,
                    onClick = {
                        currentScreen = Screen.QUIZ
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.White.copy(alpha = 0.2f),
                        unselectedContainerColor = Color.Transparent
                    )
                )
            }
        }
    ) {
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                onNavigateToDictionary = { currentScreen = Screen.DICTIONARY },
                onNavigateToQuiz = { currentScreen = Screen.QUIZ },
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            Screen.DICTIONARY -> DictionaryScreen(
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            Screen.QUIZ -> QuizScreen(
                onMenuClick = { scope.launch { drawerState.open() } }
            )
        }
    }
}