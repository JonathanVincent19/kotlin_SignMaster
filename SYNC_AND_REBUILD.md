# 🔄 SYNC & REBUILD PROJECT

## ✅ **CARA 1: Via Terminal (Android Studio)**

### **Step 1: Buka Terminal di Android Studio**
- **View** → **Tool Windows** → **Terminal**
- Atau shortcut: `⌥ + F12` (Mac) / `Alt + F12` (Windows/Linux)

### **Step 2: Run Commands**

```bash
# Navigate ke project directory (jika belum)
cd /Users/jonathanvincent/AndroidStudioProjects/FINPRO_MOBAPP

# Sync Gradle Project
./gradlew clean build --refresh-dependencies

# Atau sync saja tanpa clean:
./gradlew build
```

### **Alternative: Sync saja (lebih cepat)**
```bash
./gradlew build --refresh-dependencies
```

---

## ✅ **CARA 2: Via Android Studio GUI**

### **Sync Gradle Project:**
1. **File** → **Sync Project with Gradle Files**
   - Atau click icon **🔄 Gradle Sync** di toolbar (icon dengan panah melingkar)
   - Shortcut: Tidak ada default shortcut, tapi bisa assign di Settings → Keymap

### **Rebuild Project:**
1. **Build** → **Rebuild Project**
   - Atau shortcut: Tidak ada default shortcut

### **Clean Project (Opsional):**
1. **Build** → **Clean Project**

---

## ✅ **CARA 3: Script All-in-One**

Buat script untuk sync + rebuild sekaligus:

```bash
#!/bin/bash
cd /Users/jonathanvincent/AndroidStudioProjects/FINPRO_MOBAPP
echo "🔄 Syncing Gradle..."
./gradlew clean build --refresh-dependencies
echo "✅ Sync & Rebuild complete!"
```

---

## 📋 **COMMAND REFERENCE:**

### **Sync Gradle:**
```bash
./gradlew build --refresh-dependencies
```

### **Rebuild Project:**
```bash
./gradlew clean build
```

### **Clean Only:**
```bash
./gradlew clean
```

### **Build Only:**
```bash
./gradlew build
```

### **Assemble APK:**
```bash
./gradlew assembleDebug
```

---

## ⚠️ **TROUBLESHOOTING:**

### **Error: "Permission denied"**
```bash
chmod +x gradlew
```

### **Error: "Gradle not found"**
- Pastikan project directory benar
- Check: `ls -la gradlew` harus ada

### **Error: "Build failed"**
- Check linter errors
- Check dependencies
- Try: `./gradlew clean` dulu, lalu `./gradlew build`

---

## 💡 **REKOMENDASI:**

**Untuk Development:**
```bash
./gradlew build --refresh-dependencies
```

**Untuk Production:**
```bash
./gradlew clean build
```

