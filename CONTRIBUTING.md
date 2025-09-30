# Contributing to NotificationRemover

Thank you for your interest in contributing to NotificationRemover! This document provides guidelines and information for contributors.

## 🤝 Code of Conduct

Please read and follow our [Code of Conduct](CODE_OF_CONDUCT.md) to ensure a welcoming environment for all contributors.

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Arctic Fox (2020.3.1) or later
- **JDK 17** or later
- **Git** for version control
- **Basic knowledge** of Kotlin and Android development

### Development Setup

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/your-username/NotificationRemover.git
   cd NotificationRemover
   ```

3. **Add the upstream remote**:
   ```bash
   git remote add upstream https://github.com/original-owner/NotificationRemover.git
   ```

4. **Open in Android Studio**:
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory

5. **Sync the project** and ensure it builds successfully:
   ```bash
   ./gradlew clean build
   ```

## 📋 Types of Contributions

We welcome several types of contributions:

### 🐛 Bug Reports
- Use the GitHub issue template
- Include device info, Android version, and app version
- Provide clear steps to reproduce
- Include screenshots or logs if applicable

### ✨ Feature Requests
- Search existing issues first
- Describe the problem you're trying to solve
- Explain why this feature would benefit users
- Consider the scope and complexity

### 🔧 Code Contributions
- Bug fixes
- New features
- Performance improvements
- Test coverage improvements
- Documentation updates

### 📚 Documentation
- README improvements
- Code comments and KDoc
- API documentation
- User guides and tutorials

## 🛠️ Development Guidelines

### Code Style

We follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) with these additions:

```kotlin
// ✅ Good: Descriptive names
private fun validateRemovalTime(minutes: Int): Boolean

// ❌ Bad: Abbreviated names  
private fun valTime(m: Int): Boolean

// ✅ Good: Clear intent
val isNotificationAccessGranted = checkNotificationPermission()

// ❌ Bad: Unclear purpose
val flag = checkPerm()
```

### Architecture Principles

1. **MVVM Pattern**: Use ViewModel for business logic, View for UI
2. **Repository Pattern**: Single source of truth for data access
3. **Separation of Concerns**: Each class has a single responsibility
4. **Dependency Injection**: Ready for Hilt implementation

### Commit Message Format

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code formatting (no logic changes)
- `refactor`: Code restructuring (no feature changes)
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Examples:**
```
feat(ui): add swipe-to-delete for app settings

fix(service): resolve memory leak in notification handler

docs(readme): update installation instructions

test(data): add unit tests for AppSettingEntity validation
```

## 🧪 Testing

### Running Tests

```bash
# Unit tests
./gradlew test

# Connected tests (requires device/emulator)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

### Writing Tests

- **Unit tests** for business logic and utilities
- **Integration tests** for database operations
- **UI tests** for critical user flows

```kotlin
// Example unit test
@Test
fun `validateRemovalTime returns false for negative values`() {
    val result = ValidationUtils.isValidRemovalTime(-1)
    assertFalse(result)
}

// Example UI test
@Test
fun testAddAppSettingFlow() {
    onView(withId(R.id.addAppSettingButton)).perform(click())
    onView(withText("WhatsApp")).perform(click())
    onView(withId(R.id.removalTimeInput)).perform(typeText("30"))
    onView(withText("Add")).perform(click())
    onView(withText("WhatsApp")).check(matches(isDisplayed()))
}
```

## 🔄 Pull Request Process

### Before Submitting

1. **Sync with upstream**:
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   ```

2. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes** following the guidelines above

4. **Test thoroughly**:
   ```bash
   ./gradlew check
   ./gradlew connectedAndroidTest
   ```

5. **Update documentation** if needed

### Submitting the PR

1. **Push your branch**:
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Create a Pull Request** on GitHub with:
   - Clear title describing the change
   - Detailed description of what was changed and why
   - Link to any related issues
   - Screenshots for UI changes
   - Test results

3. **Complete the PR template** checklist

### PR Review Process

- All PRs require at least one review
- Address review feedback promptly
- Keep discussions focused and respectful
- Be open to suggestions and alternatives

## 📝 Issue Guidelines

### Bug Reports

Use this template:
```markdown
**Bug Description**
A clear description of the bug.

**Steps to Reproduce**
1. Open the app
2. Click on '...'
3. See error

**Expected Behavior**
What should happen.

**Actual Behavior**
What actually happens.

**Environment**
- Device: [e.g. Pixel 5]
- Android Version: [e.g. 13]
- App Version: [e.g. 1.1.0]

**Additional Context**
Screenshots, logs, or other context.
```

### Feature Requests

Use this template:
```markdown
**Is your feature request related to a problem?**
A clear description of what the problem is.

**Describe the solution you'd like**
What you want to happen.

**Describe alternatives you've considered**
Other solutions you've thought about.

**Additional context**
Screenshots, mockups, or examples.
```

## 🎯 Good First Issues

Look for issues labeled `good first issue` or `help wanted`. These are designed for new contributors and include:

- Documentation improvements
- Simple bug fixes
- Adding unit tests
- UI text updates
- Small feature additions

## 📞 Getting Help

- **GitHub Discussions**: For questions and general discussion
- **GitHub Issues**: For bug reports and feature requests
- **Code Reviews**: For technical guidance on PRs

## 🏆 Recognition

Contributors are recognized in:
- Release notes for significant contributions
- Contributors section in README
- GitHub contributor graphs

## 📄 License

By contributing, you agree that your contributions will be licensed under the same MIT License that covers the project.

---

Thank you for contributing to NotificationRemover! 🎉