# 🚀 GENERATE MODEL CLASS VIA TERMINAL

## **CARA 1: Pakai Script Otomatis (Paling Mudah)**

### **Run Script:**
```bash
cd /Users/jonathanvincent/AndroidStudioProjects/FINPRO_MOBAPP
./generate_model_class.sh
```

Script akan:
1. ✅ Check Python3
2. ✅ Check model file
3. ✅ Install tflite-support (jika belum ada)
4. ✅ Generate model class
5. ✅ Place di `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`

---

## **CARA 2: Manual Command (Step by Step)**

### **Step 1: Install tflite-support**
```bash
pip3 install tflite-support
```

### **Step 2: Create Output Directory**
```bash
mkdir -p app/src/main/java/com/example/finpro_mobapp/ml
```

### **Step 3: Generate Model Class**
```bash
python3 -m tflite_support.codegen \
  --model_path=app/src/main/assets/sign_classifier.tflite \
  --package_name=com.example.finpro_mobapp.ml \
  --class_name=SignClassifier \
  --output_dir=app/src/main/java/com/example/finpro_mobapp/ml/
```

---

## **CARA 3: Python Script (Custom)**

```python
from tflite_support.codegen import codegen

codegen.generate_code(
    model_path="app/src/main/assets/sign_classifier.tflite",
    output_dir="app/src/main/java/com/example/finpro_mobapp/ml/",
    package_name="com.example.finpro_mobapp.ml",
    class_name="SignClassifier"
)
```

Save sebagai `generate.py`, lalu:
```bash
python3 generate.py
```

---

## **VERIFIKASI:**

Setelah generate, check:
```bash
ls -lh app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt
```

File harus ada dan tidak kosong.

---

## **TROUBLESHOOTING:**

### **Error: "tflite-support not found"**
```bash
pip3 install tflite-support
```

### **Error: "codegen module not found"**
```bash
pip3 install --upgrade tflite-support
```

### **Error: "Model file not found"**
- Check: `app/src/main/assets/sign_classifier.tflite` exists
- Check: You're in correct directory

### **Error: "Permission denied"**
```bash
chmod +x generate_model_class.sh
```

---

## **SETELAH GENERATE:**

1. ✅ Check generated file: `app/src/main/java/com/example/finpro_mobapp/ml/SignClassifier.kt`
2. ✅ Sync Gradle Project in Android Studio
3. ✅ Rebuild project
4. ✅ Test Quiz 2

