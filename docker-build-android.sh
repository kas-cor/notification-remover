#!/bin/bash

echo "Building APK using Android Docker image..."

# Используем образ с Android SDK
docker run --rm -v "$PWD:/project" -w /project androidsdk/android-30 bash -c "chmod +x ./gradlew && ./gradlew assembleDebug"

echo "Build complete!"
echo "APK file location: $PWD/app/build/outputs/apk/debug/app-debug.apk"
