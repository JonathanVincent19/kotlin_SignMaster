# 🤖 CARA GENERATE MODEL CLASS DARI .tflite

## **METODE 1: TensorFlow Lite Code Generator (Android Studio)**

### **Step 1: Install Code Generator Plugin**
1. Buka Android Studio
2. File → Settings → Plugins
3. Search "TensorFlow Lite"
4. Install "TensorFlow Lite Code Generator" plugin
5. Restart Android Studio

### **Step 2: Generate Model Class**
1. Buka `app/src/main/assets/sign_classifier.tflite`
2. Right-click file → Generate → TensorFlow Lite Model Class
3. Pilih package: `com.example.finpro_mobapp.ml`
4. Generate
5. Class akan dibuat di `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`

---

## **METODE 2: Manual via Terminal**

### **Step 1: Install TensorFlow Lite Code Generator Tool**
```bash
pip install tflite-support
```

### **Step 2: Generate Class**
```bash
python -m tflite_support.codegen \
  --model_path=app/src/main/assets/sign_classifier.tflite \
  --package_name=com.example.finpro_mobapp.ml \
  --class_name=SignClassifier \
  --output_dir=app/src/main/java/com/example/finpro_mobapp/ml/
```

---

## **METODE 3: Online Code Generator**

1. Upload `sign_classifier.tflite` ke:
   - https://www.tensorflow.org/lite/models/convert/metadata
2. Generate code
3. Download generated class
4. Place di `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`

---

## **SETELAH GENERATE:**

1. ✅ Class `SignClassifier` akan ada di `com.example.finpro_mobapp.ml` package
2. ✅ `InterpreterHelper.kt` bisa load model
3. ✅ Ready untuk testing!

---

## **VERIFIKASI:**

Setelah generate, pastikan:
- ✅ `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt` ada
- ✅ Build project → No errors
- ✅ Model bisa di-load

