#!/bin/bash

echo "🔧 Creating minimal buildable Android project..."
echo "==============================================="

# Создаем minimal local.properties
cat > local.properties << EOF
sdk.dir=/opt/android-sdk-linux
ndk.dir=/opt/android-ndk
EOF

# Создаем minimal gradle.properties
cat > gradle.properties << EOF
android.useAndroidX=true
android.enableJetifier=true
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.daemon=false
EOF

# Создаем simplified build.gradle
cat > build.gradle << 'EOF'
buildscript {
    ext.kotlin_version = '1.8.22'
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.0'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
EOF

# Упрощаем app/build.gradle
cat > app/build.gradle << 'EOF'
plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    namespace 'com.example.notificationremover'
    compileSdk 33

    defaultConfig {
        applicationId "com.example.notificationremover"
        minSdk 26
        targetSdk 33
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.8.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
EOF

echo "✅ Minimal project configuration created"
echo "🚀 Attempting build with simplified setup..."

# Пробуем собрать с упрощенной конфигурацией
docker run --rm \
    -v "$PWD:/workspace" \
    -w /workspace \
    alvrme/alpine-android:android-33-jdk11 \
    ./gradlew assembleDebug --no-daemon --stacktrace

echo "Build attempt completed!"