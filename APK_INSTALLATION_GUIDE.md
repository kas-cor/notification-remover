# 📱 APK Installation Guide

## 🔒 Проблема с установкой

**Почему APK не устанавливается:**
- Unsigned debug APK не проходит Android security checks
- Play Protect блокирует установку неподписанных приложений
- Отсутствуют разрешения на установку из неизвестных источников

## 🛠️ Решения для установки

### 1️⃣ **Быстрое решение (для тестирования)**

#### На Android устройстве:
1. **Включить Developer Options:**
   - Settings → About Phone → Build Number (тапнуть 7 раз)
   
2. **Разрешить установку APK:**
   - Settings → Apps → Special Access → Install Unknown Apps
   - Найти браузер/файловый менеджер → Enable

3. **Отключить Play Protect (временно):**
   - Play Store → Profile → Play Protect → Settings
   - Отключить "Scan apps with Play Protect"

#### Установка через ADB:
```bash
# Подключить устройство по USB
adb install app-debug.apk

# Или принудительная установка
adb install -r -t app-debug.apk
```

### 2️⃣ **Правильное решение (self-signed APK)**

Создадим debug keystore для подписания:

```bash
# Создать debug keystore
keytool -genkey -v -keystore debug.keystore \
    -alias androiddebugkey \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -dname "CN=Android Debug,O=Android,C=US" \
    -storepass android -keypass android

# Подписать APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
    -keystore debug.keystore \
    -storepass android -keypass android \
    app-debug.apk androiddebugkey

# Выровнять APK
zipalign -v 4 app-debug.apk app-debug-aligned.apk
```

### 3️⃣ **Production решение (CI/CD подписание)**

Добавим автоматическое подписание в GitHub Actions.

## 📋 Пошаговая инструкция установки

### Метод 1: ADB (Рекомендуется для разработчиков)
1. Скачать APK из GitHub Actions artifacts
2. Включить USB Debugging на устройстве
3. Подключить устройство к компьютеру
4. Выполнить: `adb install -r app-debug.apk`

### Метод 2: Ручная установка
1. Скачать APK на устройство
2. Включить "Install Unknown Apps" для файлового менеджера
3. Отключить Play Protect (временно)
4. Открыть APK через файловый менеджер
5. Разрешить установку

### Метод 3: Подписанный APK (Лучшее решение)
Мы настроим автоматическое создание подписанного APK в CI/CD.

## ⚠️ Безопасность

**Помните:**
- Включайте установку неизвестных APK только для тестирования
- Отключайте Play Protect только временно
- После тестирования верните настройки безопасности
- Используйте подписанные APK для production

## 🔧 Что делаем дальше

1. Создадим debug keystore для CI/CD
2. Настроим автоматическое подписание APK
3. Добавим инструкции по установке в README
4. Создадим signed APK для легкой установки