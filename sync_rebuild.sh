#!/bin/bash

# Script untuk Sync & Rebuild Project
# Usage: ./sync_rebuild.sh

echo "🔄 Syncing & Rebuilding Project..."
echo ""

# Navigate to project directory
cd /Users/jonathanvincent/AndroidStudioProjects/FINPRO_MOBAPP

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found"
    exit 1
fi

# Make gradlew executable
chmod +x gradlew

echo "📦 Step 1: Cleaning project..."
./gradlew clean

if [ $? -ne 0 ]; then
    echo "❌ Clean failed"
    exit 1
fi

echo ""
echo "🔄 Step 2: Syncing dependencies..."
./gradlew build --refresh-dependencies

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SUCCESS! Project synced and rebuilt."
    echo ""
    echo "📋 Next steps:"
    echo "   1. Run aplikasi di Android Studio"
    echo "   2. Test Quiz 2 dengan model AI"
else
    echo ""
    echo "❌ Build failed. Check errors above."
    exit 1
fi

