# GitHub Actions Documentation for NotificationRemover

## 🚀 **CI/CD Pipeline Overview**

This project uses GitHub Actions for comprehensive CI/CD automation. Our pipeline includes testing, building, security scanning, and automated releases.

---

## 📋 **Workflow Files**

### **1. android.yml - Main CI/CD Pipeline**
**Triggers**: Push to main/develop, tags, pull requests
**Jobs**:
- 🧪 **Unit Tests**: Runs all unit tests with reporting
- 🔍 **Lint**: Code quality checks and static analysis  
- 🔒 **Security**: Vulnerability scanning with Trivy
- 📱 **Debug Build**: Creates debug APK for testing
- 🚀 **Release Build**: Production APK for tagged releases
- 📦 **Play Store**: Automated deployment (with secrets)

### **2. pr-check.yml - Pull Request Validation**
**Triggers**: Pull request events
**Jobs**:
- ✅ **Quality Gate**: Fast tests and lint checks
- 📊 **PR Statistics**: File/line change analysis
- 💬 **Auto Comments**: Status updates on PRs
- 🔐 **Security Check**: Dependency vulnerability scan

### **3. nightly.yml - Nightly Builds**
**Triggers**: Daily at 02:00 UTC, manual dispatch
**Jobs**:
- 🌙 **Nightly Build**: Automated daily builds from develop
- 📅 **Version Stamping**: Date-based version naming
- 📦 **Artifact Upload**: 7-day retention for testing
- 🔔 **Team Notifications**: Slack/Discord alerts

### **4. release.yml - Release Management**
**Triggers**: Version tags (v*.*.*), manual dispatch
**Jobs**:
- ✔️ **Release Validation**: Version format and changelog check
- 🏗️ **Release Build**: Production-ready APK creation
- 📢 **GitHub Release**: Automated release page creation
- 🔔 **Notifications**: Discord/team announcements

---

## 🔧 **Setup Instructions**

### **Required Secrets**
Add these secrets in your GitHub repository settings:

#### **Basic Secrets**
```bash
# Android signing (optional for debug builds)
SIGNING_KEY                 # Base64 encoded keystore
ALIAS                      # Keystore alias
KEY_STORE_PASSWORD         # Keystore password
KEY_PASSWORD              # Key password

# Security scanning (optional)
SNYK_TOKEN                # Snyk security scanner
```

#### **Advanced Secrets** (for full automation)
```bash
# Play Store deployment
GOOGLE_PLAY_SERVICE_ACCOUNT # Service account JSON

# Notifications
DISCORD_WEBHOOK            # Discord webhook URL
SLACK_WEBHOOK_URL         # Slack webhook URL
```

### **Repository Settings**
1. **Enable Actions**: Go to Settings → Actions → Allow all actions
2. **Branch Protection**: Protect main branch with required status checks
3. **Secrets**: Add required secrets in Settings → Secrets and variables

---

## 🎯 **Workflow Triggers**

### **Automatic Triggers**
| Event | Workflows | Description |
|-------|-----------|-------------|
| **Push to main/develop** | `android.yml` | Full CI/CD pipeline |
| **Pull Request** | `pr-check.yml`, `android.yml` | Quality gates |
| **Tag push (v*.*.*)** | `release.yml`, `android.yml` | Release creation |
| **Daily 02:00 UTC** | `nightly.yml` | Automated nightly builds |

### **Manual Triggers**
| Workflow | How to Trigger | Use Case |
|----------|----------------|----------|
| **android.yml** | Actions tab → Run workflow | Manual build/test |
| **nightly.yml** | Actions tab → Run workflow | On-demand nightly |
| **release.yml** | Actions tab → Run workflow | Manual release |

---

## 📱 **Artifacts & Outputs**

### **Debug Builds** (Every push)
- **APK**: `NotificationRemover-{version}-{timestamp}.apk`
- **Retention**: 90 days
- **Signed**: Yes (if secrets configured)

### **Release Builds** (Tagged versions)
- **APK**: `NotificationRemover-{version}-release.apk`
- **Debug APK**: `NotificationRemover-{version}-debug.apk`
- **Checksums**: SHA256 verification file
- **Retention**: Permanent (GitHub Releases)

### **Nightly Builds** (Daily)
- **APK**: `NotificationRemover-nightly-{YYYYMMDD}.apk`
- **Retention**: 7 days
- **Branch**: develop

### **Test Reports**
- **Unit Tests**: JUnit XML reports
- **Lint**: HTML and XML reports
- **Security**: SARIF format for GitHub Security tab

---

## 🔄 **Release Process**

### **Automatic Release** (Recommended)
1. **Update release notes**: Create `RELEASE_NOTES_v1.x.x.md`
2. **Commit changes**: Push to develop branch
3. **Create tag**: `git tag v1.x.x && git push origin v1.x.x`
4. **Wait**: GitHub Actions creates release automatically

### **Manual Release**
1. **Go to Actions tab**
2. **Select "Release Management"**
3. **Click "Run workflow"**
4. **Enter version** (e.g., v1.3.0)
5. **Choose pre-release** if needed

### **Pre-release Process**
```bash
# For beta releases
git tag v1.3.0-beta.1
git push origin v1.3.0-beta.1

# For release candidates  
git tag v1.3.0-rc.1
git push origin v1.3.0-rc.1
```

---

## 📊 **Monitoring & Notifications**

### **GitHub Notifications**
- ✅ **PR Comments**: Automatic quality gate results
- 📢 **Release Announcements**: Issues created for stable releases
- 🔔 **Action Failures**: Email notifications to repository owners

### **External Notifications** (Optional)
- **Discord**: Release announcements and build status
- **Slack**: Nightly build results and failures

### **Security Monitoring**
- **GitHub Security**: Vulnerability alerts in Security tab
- **Trivy Scans**: File system vulnerability scanning
- **Dependency Checks**: Automated security updates

---

## 🎛️ **Customization Options**

### **Build Variants**
```yaml
# Add to android.yml for different build types
- name: Build release APK
  run: ./gradlew assembleRelease bundleRelease --no-daemon
```

### **Testing Strategies**
```yaml
# Add integration tests
- name: Run instrumentation tests
  uses: reactivecircus/android-emulator-runner@v2
  with:
    api-level: 29
    script: ./gradlew connectedAndroidTest
```

### **Custom Notifications**
```yaml
# Add custom notification steps
- name: Notify team
  if: failure()
  run: |
    curl -X POST -H 'Content-type: application/json' \
    --data '{"text":"Build failed for NotificationRemover"}' \
    ${{ secrets.SLACK_WEBHOOK_URL }}
```

---

## 🔍 **Troubleshooting**

### **Common Issues**

#### **Build Failures**
```bash
# Check Gradle wrapper permissions
chmod +x gradlew

# Clear Gradle cache
./gradlew clean --no-daemon

# Check Java version
java -version  # Should be 17+
```

#### **Signing Issues**
```bash
# Verify keystore base64 encoding
base64 -i your-keystore.jks | tr -d '\n'

# Check alias exists
keytool -list -keystore your-keystore.jks
```

#### **Missing Secrets**
- Go to repository Settings → Secrets and variables → Actions
- Verify all required secrets are set
- Check secret names match workflow files exactly

### **Debug Workflow Issues**
1. **Check Actions logs**: Go to Actions tab → Failed workflow → View logs
2. **Enable debug logging**: Add `ACTIONS_STEP_DEBUG: true` to workflow
3. **Test locally**: Use `act` tool to run workflows locally

---

## 📈 **Performance Optimization**

### **Cache Strategy**
- **Gradle cache**: Shared across all workflows
- **Dependencies**: Cached for 30 days
- **Build cache**: Improves build times by 60%+

### **Parallel Execution**
- **Tests and Lint**: Run in parallel
- **Multiple APK builds**: Parallel artifact creation
- **Security scans**: Non-blocking for normal builds

### **Resource Usage**
- **Build time**: ~5-8 minutes for full pipeline
- **Storage**: ~50MB per build (artifacts)
- **Concurrent jobs**: Up to 4 parallel jobs

---

## 🎯 **Best Practices**

### **Branch Strategy**
- **main**: Stable, production-ready code
- **develop**: Integration branch for features
- **feature/***: Individual feature development
- **hotfix/***: Critical bug fixes

### **Commit Strategy**
- **Conventional Commits**: Required by pre-commit hooks
- **Signed commits**: Recommended for security
- **Atomic commits**: One logical change per commit

### **Release Strategy**
- **Semantic Versioning**: v1.2.3 format
- **Release Notes**: Always include detailed notes
- **Pre-releases**: Use for beta testing
- **Hotfixes**: Immediate patches for critical issues

---

*This documentation is automatically updated with pipeline changes.*