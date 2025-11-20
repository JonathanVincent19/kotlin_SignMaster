# 🚀 CARA GENERATE MODEL CLASS

## ✅ **OPSI 1: Via Terminal (Android Studio)**

### **Step 1: Buka Terminal di Android Studio**
- **View** → **Tool Windows** → **Terminal**
- Atau shortcut: `Alt + F12` (Windows/Linux) atau `⌥ + F12` (Mac)

### **Step 2: Run Script**
```bash
cd /Users/jonathanvincent/AndroidStudioProjects/FINPRO_MOBAPP
./generate_model_class.sh
```

### **Script akan:**
1. ✅ Check Python3
2. ✅ Check model file
3. ✅ Install tflite-support (jika belum ada)
4. ✅ Generate model class
5. ✅ Place di `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`

---

## ✅ **OPSI 2: Android Studio Built-in Generator (LEBIH MUDAH - RECOMMENDED)**

### **Step 1: Buka Model File**
1. Buka Android Studio
2. Navigate ke: **`app/src/main/assets/sign_classifier.tflite`**
3. Double-click untuk buka file

### **Step 2: Generate Class**
1. **Right-click** file `sign_classifier.tflite`
2. Pilih **Generate** → **TensorFlow Lite Model Class**
3. Pilih package: **`com.example.finpro_mobapp.ml`**
4. Click **Generate**

### **Hasil:**
- ✅ Class akan dibuat otomatis di `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`
- ✅ Tidak perlu install Python packages
- ✅ Tidak ada permission issues

---

## 📋 **SETELAH GENERATE:**

1. ✅ **Sync Gradle Project**
   - File → Sync Project with Gradle Files
   - Atau click icon Gradle sync di toolbar

2. ✅ **Rebuild Project**
   - Build → Rebuild Project

3. ✅ **Verify Generated Class**
   - Check: `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt` exists
   - File tidak kosong

4. ✅ **Test Quiz 2**
   - Run aplikasi
   - Home → Quiz → Quiz 2
   - Test gesture recognition

---

## ⚠️ **TROUBLESHOOTING:**

### **Script Error: "tflite-support not found"**
**Solution:**
- Install manually: `pip3 install tflite-support --user`
- Atau pakai Android Studio built-in generator (lebih mudah)

### **Script Error: "Permission denied"**
**Solution:**
```bash
chmod +x generate_model_class.sh
```

### **Android Studio: "Generate → TensorFlow Lite Model Class" tidak muncul**
**Solution:**
1. Install TensorFlow Lite Code Generator plugin:
   - File → Settings → Plugins
   - Search "TensorFlow Lite"
   - Install "TensorFlow Lite Code Generator"
   - Restart Android Studio

### **Model class tidak ter-generate**
**Solution:**
- Check model file ada di `app/src/main/assets/sign_classifier.tflite`
- Check model file tidak corrupt
- Try regenerate

---

## 💡 **REKOMENDASI:**

**PAKAI ANDROID STUDIO BUILT-IN GENERATOR** (Cara termudah!)
- ✅ Tidak perlu install Python packages
- ✅ Tidak ada permission issues
- ✅ Langsung integrate dengan project
- ✅ Auto-generate dengan format yang benar

**Via Terminal hanya jika:**
- ❌ Android Studio generator tidak tersedia
- ❌ Ingin automate dalam CI/CD
- ❌ Prefer command line

---

## ✅ **CHECKLIST:**

- [ ] ✅ Model file ada: `app/src/main/assets/sign_classifier.tflite`
- [ ] ✅ Generate model class (via script atau Android Studio)
- [ ] ✅ Generated class ada: `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`
- [ ] ✅ Sync Gradle Project
- [ ] ✅ Rebuild project
- [ ] ✅ Test Quiz 2

