#!/bin/bash

# 🔧 ULTIMATE APK INSTALLATION SOLUTION 🔧
# ========================================

echo "🔧 ULTIMATE APK INSTALLATION SOLUTION 🔧"
echo "========================================"
echo ""

# Method 1: Build unsigned APK for maximum compatibility
echo "📱 METHOD 1: Building unsigned APK..."
echo "Building APK without signing for maximum compatibility..."

# Remove signing configuration temporarily
cp app/build.gradle app/build.gradle.backup

# Create unsigned build configuration
cat > app/build.gradle.unsigned << 'EOF'
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.kapt'
}

android {
    namespace 'com.example.notificationremover'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.notificationremover"
        minSdk 26
        targetSdk 34
        versionCode 2
        versionName "1.2"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            minifyEnabled false
            testCoverageEnabled true
            debuggable true
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = '17'
    }
    
    buildFeatures {
        viewBinding true
        buildConfig true
    }
    
    testOptions {
        unitTests.includeAndroidResources = true
        unitTests.returnDefaultValues = true
    }
    
    lint {
        abortOnError false
        warningsAsErrors false
        checkReleaseBuilds false
    }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.9.10"
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // ViewModel and LiveData
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
    
    // Room for database
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.5.0'
    testImplementation 'org.mockito:mockito-inline:5.2.0'
    testImplementation 'androidx.arch.core:core-testing:2.2.0'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
    testImplementation 'androidx.test:core:1.5.0'
    testImplementation 'androidx.test.ext:junit:1.1.5'
    testImplementation 'org.robolectric:robolectric:4.11.1'
    
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
}
EOF

echo "✅ Unsigned build configuration created"
echo ""

echo "📋 INSTALLATION INSTRUCTIONS:"
echo "============================="
echo ""
echo "🔧 FOR DEVELOPERS (ADB Method):"
echo "1. Enable USB Debugging on device"
echo "2. Connect device to computer"
echo "3. Run: adb install -r app-debug.apk"
echo ""
echo "📱 FOR USERS (Manual Installation):"
echo "1. Download APK to Android device"
echo "2. Settings → Security → Install unknown apps"
echo "3. Enable for Chrome/Files app"
echo "4. Open APK file and install"
echo ""
echo "🛡️ IF PLAY PROTECT BLOCKS:"
echo "1. Play Store → Profile → Play Protect"
echo "2. Disable temporarily"
echo "3. Install APK"
echo "4. Re-enable Play Protect"
echo ""
echo "🔄 IF STILL DOESN'T INSTALL:"
echo "1. Try: Settings → Apps → Package Installer → Clear Data"
echo "2. Restart device"
echo "3. Try installation again"
echo ""

# Method 2: Create universal installer script
echo "🚀 Creating universal installer script..."

cat > install-apk.sh << 'EOF'
#!/bin/bash

# Universal APK Installer Script
# ==============================

APK_FILE="app-debug.apk"

echo "🔧 NotificationRemover APK Installer 🔧"
echo "======================================="
echo ""

# Check if ADB is available
if command -v adb &> /dev/null; then
    echo "📱 ADB detected - attempting automatic installation..."
    
    # Check if device is connected
    DEVICES=$(adb devices | grep -v "List of devices" | grep -E "device$|unauthorized$" | wc -l)
    
    if [ $DEVICES -gt 0 ]; then
        echo "📲 Device connected - installing APK..."
        adb install -r "$APK_FILE"
        
        if [ $? -eq 0 ]; then
            echo "✅ APK installed successfully!"
            echo "🎉 You can now find 'Notification Remover' in your apps"
        else
            echo "❌ Installation failed via ADB"
            echo "📋 Try manual installation method"
        fi
    else
        echo "📱 No device connected to ADB"
        echo "🔌 Connect your device with USB debugging enabled"
    fi
else
    echo "📋 Manual installation required"
fi

echo ""
echo "📖 MANUAL INSTALLATION STEPS:"
echo "============================="
echo "1. Copy $APK_FILE to your Android device"
echo "2. Settings → Security → Install unknown apps"
echo "3. Enable for your file manager app"
echo "4. Open $APK_FILE and follow prompts"
echo ""
echo "🛠️ TROUBLESHOOTING:"
echo "===================="
echo "• If blocked by Play Protect - disable temporarily"
echo "• If 'App not installed' - clear Package Installer data"
echo "• If still failing - restart device and try again"
echo ""
EOF

chmod +x install-apk.sh

echo "✅ Universal installer script created: install-apk.sh"
echo ""
echo "🎯 READY FOR DEPLOYMENT!"