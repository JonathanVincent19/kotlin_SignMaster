package com.example.finpro_mobapp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

/**
 * AuthManager - Handles Google Sign-In with Firebase Authentication
 * 
 * Uses Legacy Google Sign-In API (more stable on emulators)
 */
class AuthManager(private val context: Context) {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient
    
    companion object {
        private const val TAG = "AuthManager"
        
        // Web Client ID from Firebase Console
        const val WEB_CLIENT_ID = "771596650876-hscctpkoi86tn08uesq8jm319robguii.apps.googleusercontent.com"
    }
    
    init {
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    /**
     * Get current signed-in user
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    
    /**
     * Check if user is signed in
     */
    val isSignedIn: Boolean
        get() = currentUser != null
    
    /**
     * Get display name of current user
     */
    val displayName: String
        get() = currentUser?.displayName ?: "Pengguna"
    
    /**
     * Get email of current user
     */
    val email: String?
        get() = currentUser?.email
    
    /**
     * Get profile photo URL of current user
     */
    val photoUrl: String?
        get() = currentUser?.photoUrl?.toString()
    
    /**
     * Get the sign-in intent to launch
     */
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    /**
     * Handle the result from Google Sign-In activity
     * Call this in your Activity's onActivityResult or ActivityResultLauncher
     */
    suspend fun handleSignInResult(data: Intent?): Result<FirebaseUser> {
        return try {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            
            // Got Google account, now authenticate with Firebase
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e(TAG, "Google sign in failed: ${e.statusCode} - ${e.message}")
            Result.failure(Exception("Google Sign-In gagal: ${getErrorMessage(e.statusCode)}"))
        } catch (e: Exception) {
            Log.e(TAG, "Sign in error: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Authenticate with Firebase using Google account
     */
    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            
            val user = authResult.user
            if (user != null) {
                Log.d(TAG, "Firebase auth successful: ${user.displayName}")
                Result.success(user)
            } else {
                Result.failure(Exception("User null setelah login"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Firebase auth failed: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
        Log.d(TAG, "User signed out")
    }
    
    /**
     * Get user-friendly error message
     */
    private fun getErrorMessage(statusCode: Int): String {
        return when (statusCode) {
            12501 -> "Login dibatalkan"
            12502 -> "Login sedang diproses"
            7 -> "Tidak ada koneksi internet"
            10 -> "Konfigurasi tidak valid. Cek SHA-1 dan package name."
            else -> "Error code: $statusCode"
        }
    }
}

/**
 * Data class to hold user information
 */
data class UserInfo(
    val displayName: String,
    val email: String?,
    val photoUrl: String?,
    val uid: String
)

/**
 * Extension function to convert FirebaseUser to UserInfo
 */
fun FirebaseUser.toUserInfo(): UserInfo {
    return UserInfo(
        displayName = displayName ?: "Pengguna",
        email = email,
        photoUrl = photoUrl?.toString(),
        uid = uid
    )
}
