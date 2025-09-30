# NotificationRemover

[![Android CI](https://github.com/username/NotificationRemover/workflows/Android%20CI/badge.svg)](https://github.com/username/NotificationRemover/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)

An Android application that automatically removes notifications after a configurable time period for each application.

## ✨ Features

- 🔄 **Automatic notification removal** - Monitor incoming notifications and remove them automatically
- ⏰ **Configurable time periods** - Set different removal times for different applications
- 🎯 **App-specific settings** - Customize notification behavior per app
- 🛡️ **Safe operation** - Only removes non-system notifications
- 🎨 **Clean interface** - Simple and user-friendly Material Design UI
- 🌙 **Dark mode support** - Follows system theme preferences
- 📱 **Modern architecture** - Built with MVVM pattern and latest Android libraries

## 📋 Requirements

- **Android 8.0 (API level 26) or higher**
- **Notification access permission** - Required to monitor and remove notifications
- **At least 50MB of free storage**

## 🚀 Installation

### From Source

1. **Clone the repository**
   ```bash
   git clone https://github.com/username/NotificationRemover.git
   cd NotificationRemover
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

### Using Docker (Alternative)

```bash
# Build using Docker
./docker-build.sh

# APK will be available in app/build/outputs/apk/debug/
```

## 🛠️ Setup

1. **Install the app** on your Android device
2. **Open the app** and grant notification access permission when prompted
3. **Configure default time** for notification removal (default: 15 minutes)
4. **Add app-specific settings** for apps that need different timing
5. **The app runs in background** and handles notifications automatically

## 📱 Usage

### Basic Configuration

1. **Set Default Time**: Configure how long notifications should stay before being removed
2. **Grant Permission**: Allow the app to access notifications through system settings
3. **Monitor Status**: Check if the service is running on the main screen

### Advanced Configuration

1. **Add App Settings**: Tap the + button to configure specific apps
2. **Select Apps**: Choose from installed non-system apps
3. **Set Custom Times**: Define different removal times per app
4. **Manage Settings**: View, edit, or delete app-specific configurations

### Example Scenarios

- **Social Media**: Set 5 minutes for Twitter, 10 minutes for Instagram
- **Messaging**: Set 30 minutes for WhatsApp, 60 minutes for Telegram  
- **News Apps**: Set 120 minutes for news notifications
- **Email**: Set 480 minutes (8 hours) for work emails

## 🏗️ Architecture

The app follows **Clean Architecture** principles with **MVVM pattern**:

```
├── ui/                 # Presentation layer
│   ├── MainActivity    # Main activity
│   ├── ViewModels      # Business logic
│   └── Adapters        # RecyclerView adapters
├── service/            # Background services
│   └── NotificationListenerService
├── data/               # Data layer
│   ├── entities/       # Room database entities
│   ├── dao/           # Data access objects
│   └── repository/    # Repository pattern
└── util/              # Utility classes
```

### Tech Stack

- **Language**: Kotlin 100%
- **UI**: Material Design Components, ViewBinding
- **Architecture**: MVVM with Repository pattern
- **Database**: Room (SQLite)
- **Async**: Coroutines + LiveData
- **DI**: (Planned) Hilt
- **Testing**: JUnit, Espresso

## ⚡ Performance

- **Memory efficient**: Uses ConcurrentHashMap for thread-safe operations
- **Battery optimized**: Minimal background processing
- **Storage light**: < 10MB app size
- **Responsive UI**: All database operations on background threads

## 🔒 Privacy & Security

- **No network access**: App works completely offline
- **No data collection**: Zero telemetry or analytics
- **Local storage only**: All data stays on your device
- **Minimal permissions**: Only notification access required
- **Safe filtering**: Never removes system or critical notifications

## 🐛 Known Issues

- Some OEMs may kill the service aggressively (solution: add to battery optimization whitelist)
- Persistent notifications (music, navigation) are intentionally not removed
- First-time setup requires manual permission grant

## 🤝 Contributing

Contributions are welcome! Please read our [Contributing Guidelines](CONTRIBUTING.md) first.

### Development Setup

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes and add tests
4. Run the test suite: `./gradlew test`
5. Submit a pull request

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Maintain test coverage above 80%

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Android Open Source Project for the foundation
- Material Design team for the UI guidelines
- Kotlin team for the amazing language
- All contributors and testers

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/username/NotificationRemover/issues)
- **Discussions**: [GitHub Discussions](https://github.com/username/NotificationRemover/discussions)
- **Email**: support@example.com

---

**Made with ❤️ for Android users who want cleaner notification trays**
