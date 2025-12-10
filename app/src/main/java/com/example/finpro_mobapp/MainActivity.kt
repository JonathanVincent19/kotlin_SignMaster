package com.example.finpro_mobapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.finpro_mobapp.auth.AuthManager
import com.example.finpro_mobapp.quiz.QuizProgressManager
import com.example.finpro_mobapp.quiz.Quiz2ProgressManager
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
    var userPhotoUrl by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // 🧪 TESTING MODE -
    // Hapus atau komentari bagian ini jika ingin progress tersimpan
    // Jika perlu reset progress, panggil manual dari kode lain
     LaunchedEffect(Unit) {
         val quiz1Progress = QuizProgressManager(context)
         val quiz2Progress = Quiz2ProgressManager(context)

         // Reset dulu biar bersih, baru unlock all
         quiz1Progress.resetAllProgress()
         quiz2Progress.resetAllProgress()

         // Sekarang unlock semua
         quiz1Progress.setTestingMode(true)   // true = unlock all
         quiz2Progress.setTestingMode(true)   // true = unlock all
     }
    
    // Activity Result Launcher for Google Sign-In
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        scope.launch {
            isLoading = true
            
            val signInResult = authManager.handleSignInResult(result.data)
            
            signInResult.onSuccess { user ->
                userName = user.displayName ?: "Pengguna"
                userPhotoUrl = user.photoUrl?.toString()
                android.util.Log.d("Auth", "photoUrl after login: ${user.photoUrl}")
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
            userPhotoUrl = authManager.photoUrl
            android.util.Log.d("Auth", "photoUrl from cached session: ${authManager.photoUrl}")
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
                userPhotoUrl = userPhotoUrl,
                onLogout = {
                    authManager.signOut()
                    userName = "Pengguna"
                    userPhotoUrl = null
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
    userPhotoUrl: String? = null,
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

    data class NavItem(val label: String, val screen: Screen, val icon: androidx.compose.ui.graphics.vector.ImageVector)

    val navItems = listOf(
        NavItem("Beranda", Screen.HOME, Icons.Filled.Home),
        NavItem("Alfabet", Screen.DICTIONARY, Icons.Filled.MenuBook),
        NavItem("Latihan", Screen.QUIZ, Icons.Filled.EmojiEvents)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF4A90E2)
            ) {
                DrawerHeader(userName = userName, userPhotoUrl = userPhotoUrl)

                navItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (currentScreen == item.screen) Color(0xFF0F172A) else Color.White
                            )
                        },
                        label = {
                            Text(
                                item.label,
                                color = if (currentScreen == item.screen) Color(0xFF0F172A) else Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = currentScreen == item.screen,
                        onClick = {
                            currentScreen = item.screen
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.White.copy(alpha = 0.2f),
                            unselectedContainerColor = Color.Transparent
                        )
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Logout button
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.ExitToApp, contentDescription = "Keluar", tint = Color.White) },
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

@Composable
private fun DrawerHeader(userName: String, userPhotoUrl: String?) {
    val firstName = userName.split(" ").firstOrNull().orEmpty().ifEmpty { "Pengguna" }
    val initials = firstName.take(2).uppercase()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFEEF4FF), Color(0xFFDCEBFF))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!userPhotoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = userPhotoUrl,
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = initials,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1D4ED8)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Halo, $firstName",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Ayo lanjut belajar hari ini",
                    fontSize = 14.sp,
                    color = Color(0xFF334155)
                )
            }
        }
    }
}
