#!/bin/bash

echo "Building APK using alternative Android Docker image..."

# Используем ещё один вариант образа Android
docker run --rm -v "$PWD:/app" -w /app alvrme/alpine-android:android-33-jdk11 ./gradlew assembleDebug

echo "Build complete!"
echo "APK file location: $PWD/app/build/outputs/apk/debug/app-debug.apk"
