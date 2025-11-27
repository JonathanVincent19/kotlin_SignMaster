package com.example.finpro_mobapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finpro_mobapp.auth.AuthManager
import com.example.finpro_mobapp.ui.theme.FINPRO_MOBAPPTheme
import kotlinx.coroutines.launch

// App Navigation State
enum class AppScreen {
    SPLASH,
    LOGIN,
    MAIN
}

class MainActivity : ComponentActivity() {
    
    private lateinit var authManager: AuthManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize AuthManager
        authManager = AuthManager(this)
        
        enableEdgeToEdge()
        setContent {
            FINPRO_MOBAPPTheme {
                MainApp(authManager = authManager)
            }
        }
    }
}

@Composable
fun MainApp(authManager: AuthManager) {
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Get username for HomeScreen
    var userName by remember { mutableStateOf("Pengguna") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Activity Result Launcher for Google Sign-In
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        scope.launch {
            isLoading = true
            
            val signInResult = authManager.handleSignInResult(result.data)
            
            signInResult.onSuccess { user ->
                userName = user.displayName ?: "Pengguna"
                Toast.makeText(context, "Selamat datang, $userName!", Toast.LENGTH_SHORT).show()
                currentScreen = AppScreen.MAIN
                errorMessage = null
            }.onFailure { exception ->
                errorMessage = exception.message ?: "Login gagal"
            }
            
            isLoading = false
        }
    }
    
    // Check initial auth state after splash
    fun checkAuthAndNavigate() {
        // Build Flag: Skip login in debug mode
        if (BuildConfig.SKIP_LOGIN) {
            userName = "Developer" // Default name for testing
            currentScreen = AppScreen.MAIN
            return
        }
        
        // Check if user is already signed in
        if (authManager.isSignedIn) {
            userName = authManager.displayName
            currentScreen = AppScreen.MAIN
        } else {
            currentScreen = AppScreen.LOGIN
        }
    }
    
    // Handle Google Sign In - launch the sign-in intent
    fun handleGoogleSignIn() {
        errorMessage = null
        isLoading = true
        signInLauncher.launch(authManager.getSignInIntent())
    }
    
    // Render current screen
    when (currentScreen) {
        AppScreen.SPLASH -> {
            SplashScreen(onTimeout = { checkAuthAndNavigate() })
        }
        
        AppScreen.LOGIN -> {
            LoginScreen(
                onGoogleSignIn = { handleGoogleSignIn() },
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }
        
        AppScreen.MAIN -> {
            AppNavigation(
                userName = userName,
                onLogout = {
                    authManager.signOut()
                    userName = "Pengguna"
                    currentScreen = AppScreen.LOGIN
                }
            )
        }
    }
}

// Navigation State for Main App
enum class Screen {
    HOME, DICTIONARY, QUIZ
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    userName: String = "Pengguna",
    onLogout: () -> Unit = {}
) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // State for logout confirmation dialog
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Text("🚪", fontSize = 32.sp)
            },
            title = {
                Text(
                    text = "Keluar dari Akun?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin keluar?",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    OutlinedButton(
                        onClick = { showLogoutDialog = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }
                    
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Keluar")
                    }
                }
            },
            dismissButton = null
        )
    }

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
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Logout button
                NavigationDrawerItem(
                    icon = { Text("🚪", fontSize = 24.sp) },
                    label = { 
                        Text(
                            "Keluar", 
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showLogoutDialog = true  // Show confirmation dialog
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent,
                        unselectedContainerColor = Color.Transparent
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                userName = userName,
                onNavigateToDictionary = { currentScreen = Screen.DICTIONARY },
                onNavigateToQuiz = { currentScreen = Screen.QUIZ },
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            Screen.DICTIONARY -> DictionaryScreen(
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            Screen.QUIZ -> QuizScreen(
                onMenuClick = { scope.launch { drawerState.open() } },
                onBackToHome = { currentScreen = Screen.HOME }
            )
        }
    }
}
