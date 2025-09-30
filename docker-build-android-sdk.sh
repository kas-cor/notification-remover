#!/bin/bash

echo "🚀 Building NotificationRemover APK with full Android SDK..."
echo "============================================================"

# Проверим Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker not found. Please install Docker first."
    exit 1
fi

echo "🔨 Building Android Docker image..."
docker build -f Dockerfile.android -t android-build .

if [ $? -ne 0 ]; then
    echo "❌ Failed to build Android Docker image"
    exit 1
fi

echo "📦 Building APK with Android SDK..."
docker run --rm \
    -v "$PWD:/project" \
    -w /project \
    android-build \
    ./gradlew clean assembleDebug --no-daemon --stacktrace

if [ $? -eq 0 ]; then
    echo "✅ Build completed successfully!"
    echo "📱 APK location: $PWD/app/build/outputs/apk/debug/app-debug.apk"
    
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        APK_SIZE=$(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)
        echo "📊 APK size: $APK_SIZE"
        echo "🔍 APK info:"
        ls -la app/build/outputs/apk/debug/app-debug.apk
    else
        echo "⚠️ APK file not found at expected location"
        find app/build -name "*.apk" 2>/dev/null || echo "No APK files found"
    fi
else
    echo "❌ Build failed!"
    echo "📋 Check logs above for details"
    exit 1
fi