#!/bin/bash

# Script untuk generate TensorFlow Lite Model Class
# Usage: ./generate_model_class.sh

echo "🤖 Generating TensorFlow Lite Model Class..."
echo ""

# Check if Python is available
if ! command -v python3 &> /dev/null; then
    echo "❌ Error: Python3 not found. Please install Python3 first."
    exit 1
fi

echo "✅ Python3 found: $(python3 --version)"
echo ""

# Check if model file exists
MODEL_FILE="app/src/main/assets/sign_classifier.tflite"
if [ ! -f "$MODEL_FILE" ]; then
    echo "❌ Error: Model file not found: $MODEL_FILE"
    exit 1
fi

echo "✅ Model file found: $MODEL_FILE"
echo ""

# Create output directory
OUTPUT_DIR="app/src/main/java/com/example/finpro_mobapp/ml"
mkdir -p "$OUTPUT_DIR"

echo "✅ Output directory created: $OUTPUT_DIR"
echo ""

# Try to install tflite-support if not installed
echo "📦 Checking tflite-support package..."
if ! python3 -c "import tflite_support.codegen" 2>/dev/null; then
    echo "📥 Installing tflite-support (user install)..."
    pip3 install tflite-support --user --quiet
    
    if [ $? -ne 0 ]; then
        echo "❌ Error: Failed to install tflite-support"
        echo ""
        echo "💡 Alternative: Use Android Studio Built-in Generator"
        echo "   1. Buka Android Studio"
        echo "   2. Navigate ke: app/src/main/assets/sign_classifier.tflite"
        echo "   3. Right-click → Generate → TensorFlow Lite Model Class"
        echo "   4. Package: com.example.finpro_mobapp.ml"
        exit 1
    fi
fi

echo "✅ tflite-support package available"
echo ""

# Generate model class
echo "🚀 Generating model class..."
echo ""

# Try to generate using Python
python3 << EOF
import sys
import os
from pathlib import Path

# Try to import tflite_support
try:
    # First try normal import
    from tflite_support.codegen import codegen
except ImportError:
    # Try user site-packages
    import site
    user_site = site.getusersitepackages()
    if user_site not in sys.path:
        sys.path.insert(0, user_site)
    try:
        from tflite_support.codegen import codegen
    except ImportError:
        print("❌ Error: Cannot import tflite_support.codegen")
        print("   Try: pip3 install tflite-support --user")
        print("")
        print("💡 Alternative: Use Android Studio → Right-click sign_classifier.tflite → Generate → TensorFlow Lite Model Class")
        sys.exit(1)

try:
    model_path = "app/src/main/assets/sign_classifier.tflite"
    output_dir = "app/src/main/java/com/example/finpro_mobapp/ml"
    package_name = "com.example.finpro_mobapp.ml"
    class_name = "SignClassifier"
    
    # Create output directory
    Path(output_dir).mkdir(parents=True, exist_ok=True)
    
    print(f"📝 Generating class: {class_name}")
    print(f"📦 Package: {package_name}")
    print(f"📁 Output: {output_dir}")
    print("")
    
    # Generate using codegen
    codegen.generate_code(
        model_path=model_path,
        output_dir=output_dir,
        package_name=package_name,
        class_name=class_name
    )
    
    output_file = os.path.join(output_dir, f"{class_name}.kt")
    if os.path.exists(output_file):
        print(f"✅ Model class generated successfully!")
        print(f"📄 File: {output_file}")
        print("")
        print("📋 Next steps:")
        print("   1. Sync Gradle Project in Android Studio")
        print("   2. Rebuild project")
        print("   3. Test Quiz 2")
    else:
        print("⚠️  Warning: Output file not found, but generation may have succeeded")
        print("   Check: $OUTPUT_DIR/SignClassifier.kt")
        
except Exception as e:
    print(f"❌ Error during code generation: {e}")
    print("")
    print("💡 Alternative method (RECOMMENDED):")
    print("   Use Android Studio:")
    print("   1. Buka: app/src/main/assets/sign_classifier.tflite")
    print("   2. Right-click → Generate → TensorFlow Lite Model Class")
    print("   3. Package: com.example.finpro_mobapp.ml")
    sys.exit(1)
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SUCCESS! Model class generated."
else
    echo ""
    echo "❌ FAILED to generate model class via terminal"
    echo ""
    echo "💡 RECOMMENDED: Use Android Studio Built-in Generator (Easier!)"
    echo "   1. Buka Android Studio"
    echo "   2. Navigate ke: app/src/main/assets/sign_classifier.tflite"
    echo "   3. Right-click file → Generate → TensorFlow Lite Model Class"
    echo "   4. Pilih package: com.example.finpro_mobapp.ml"
    echo "   5. Click Generate"
fi

