#!/bin/bash

echo "Building APK using Docker..."

# Используем официальный образ Gradle
docker run --rm -v "$PWD:/project" -w /project gradle:7.6-jdk11 ./gradlew assembleDebug

echo "Build complete!"
echo "APK file location: $PWD/app/build/outputs/apk/debug/app-debug.apk"
