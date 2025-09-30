#!/bin/bash

echo "Building Notification Remover APK..."

# Убедимся, что gradlew исполняемый
chmod +x ./gradlew

# Собираем debug APK
./gradlew assembleDebug --info

echo "Build complete!"
echo "APK file location: $PWD/app/build/outputs/apk/debug/app-debug.apk"
