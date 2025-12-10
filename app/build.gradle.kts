plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.finpro_mobapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.finpro_mobapp"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Build Flag: Set to false to test login, true to skip login during development
            buildConfigField("Boolean", "SKIP_LOGIN", "false")
        }
        release {
            isMinifyEnabled = false
            // In release, always require login
            buildConfigField("Boolean", "SKIP_LOGIN", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    
    // CameraX for Quiz 2 - Updated to support 16KB page size
    // Removed camera-extensions to fix 16KB alignment warning (not used in Quiz2)
    val cameraxVersion = "1.4.0"
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-view:${cameraxVersion}")
    // camera-extensions removed - contains libimage_processing_util_jni.so that causes 16KB alignment warning
    
    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    
    // MediaPipe for hand landmark detection
    val mediapipeVersion = "0.10.11"
    implementation("com.google.mediapipe:tasks-vision:${mediapipeVersion}")
    
    // TensorFlow Lite (untuk model classifier)
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("io.coil-kt:coil-compose:2.6.0")
    
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    
    // Credential Manager for Google Sign-In (new recommended API)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.googleid)
    
    // Legacy Google Sign-In (more stable on emulators)
    implementation(libs.play.services.auth)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}