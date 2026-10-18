# MYSynthetic AI Android App - 打包指南

## 快速开始

### 方式 1：使用 Android Studio (推荐)

这是最简单、最可靠的方式。

**步骤：**

1. **打开项目**
   - 打开 Android Studio
   - `File` → `Open` → 选择 `C:\Users\Administrator\Desktop\MYSyntheticApp`

2. **等待 Gradle 同步**
   - 项目会自动下载所有依赖
   - 首次同步可能需要 5-10 分钟
   - 等待看到 "Gradle sync finished"

3. **构建调试版本**
   - `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - 或按快捷键：`Ctrl+Shift+B` (Windows)

4. **构建发布版本**
   - `Build` → `Generate Signed Bundle / APK...`
   - 选择 `APK`
   - 创建或选择签名密钥
   - 选择 `release` build variant

5. **输出文件**
   - 调试 APK: `app/build/outputs/apk/debug/app-debug.apk`
   - 发布 APK: `app/build/outputs/apk/release/app-release.apk`
   - App Bundle: `app/build/outputs/bundle/release/app-release.aab`

---

### 方式 2：使用命令行 (需要 JDK + Gradle)

**前置条件：**
- JDK 17+ 已安装 (`java -version` 可正常运行)
- Gradle 8.1+ 已安装 (`gradle -v` 可正常运行)

**步骤：**

```cmd
cd C:\Users\Administrator\Desktop\MYSyntheticApp

# 调试 APK
./gradlew assembleDebug

# 发布 APK (未签名)
./gradlew assembleRelease

# App Bundle (Google Play)
./gradlew bundleRelease

# 全部构建
./gradlew build
```

**输出文件：**
```
app/build/outputs/
├── apk/
│   ├── debug/
│   │   └── app-debug.apk
│   └── release/
│       └── app-release-unsigned.apk
└── bundle/
    └── release/
        └── app-release.aab
```

---

### 方式 3：使用 build.bat / build.sh

本项目提供了一键打包脚本。

**Windows:**
```cmd
cd C:\Users\Administrator\Desktop\MYSyntheticApp
build.bat
```

**macOS/Linux:**
```bash
cd ~/Desktop/MYSyntheticApp
bash build.sh
```

**注意：** 这种方式需要系统已正确安装 Gradle 和 JDK，且 gradle-wrapper.jar 已配置。

---

## 签名发布版本

### 生成签名密钥 (首次)

**使用 Android Studio UI：**

1. `Build` → `Generate Signed Bundle / APK...`
2. 点击 `Create new...`
3. 填写信息：
   - Keystore path: `C:\Users\Administrator\Desktop\MYSyntheticApp\release.jks`
   - Password: (自设)
   - Alias: `mysynthetic`
   - Validity: 25 年 (或更长)
   - 填写组织信息

4. 完成后会自动使用该密钥签名

**或使用命令行：**

```bash
keytool -genkey -v -keystore release.jks \
  -keyalg RSA -keysize 2048 -validity 9125 \
  -alias mysynthetic
```

### 生成签名 APK/AAB

**Android Studio:**
1. `Build` → `Generate Signed Bundle / APK...`
2. 选择 `APK` 或 `Bundle`
3. 选择签名密钥 (release.jks)
4. 输入密钥密码
5. 选择 `release` build variant
6. 完成

**命令行：**
```bash
./gradlew assembleRelease -Pandroid.injected.signing.store.file=release.jks \
  -Pandroid.injected.signing.store.password=YOUR_PASSWORD \
  -Pandroid.injected.signing.key.alias=mysynthetic \
  -Pandroid.injected.signing.key.password=YOUR_PASSWORD
```

---

## APK vs AAB

| 格式 | 用途 | 说明 |
|------|------|------|
| **APK** | 直接安装 | 可以通过 USB 或邮件直接安装到手机 |
| **AAB** | Google Play | 提交到 Google Play Store 进行分发 |

---

## 文件位置一览

| 类型 | 位置 | 说明 |
|------|------|------|
| 项目 | `C:\Users\Administrator\Desktop\MYSyntheticApp` | 项目根目录 |
| 源代码 | `app/src/main/java/com/mysynthetic/agent/` | Kotlin 源文件 |
| 资源 | `app/src/main/res/` | 图标、字符串、颜色等 |
| 输出 | `app/build/outputs/` | 构建输出目录 |
| Gradle | `gradle/wrapper/` | Gradle Wrapper 配置 |
| 配置 | `build.gradle.kts` | 构建配置 |

---

## 故障排除

### 问题 1: "JDK not found"
**解决方案：**
- 安装 JDK 17+
- 在 Android Studio 中设置 JDK 路径：`File` → `Project Structure` → `SDK Location`

### 问题 2: "Gradle sync failed"
**解决方案：**
- 清除缓存：`Build` → `Clean Project`
- 重新同步：`File` → `Sync Now`
- 检查网络连接（需要下载依赖）

### 问题 3: "assembleRelease 找不到"
**解决方案：**
- 等待 Gradle 完全同步
- 检查 `settings.gradle.kts` 是否正确包含 `app` 模块

### 问题 4: APK 文件很大（>50MB）
**解决方案：**
- 这是正常的（首次构建包含所有依赖）
- 发布版本可以通过混淆和 shrinking 减小体积
- AAB 格式会自动优化大小

---

## 验证构建

### 检查 APK 内容

```bash
# 列出 APK 中的文件
unzip -l app/build/outputs/apk/debug/app-debug.apk

# 提取 AndroidManifest.xml
unzip app/build/outputs/apk/debug/app-debug.apk AndroidManifest.xml
```

### 安装到设备或模拟器

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 常用 Gradle 命令

```bash
# 清除所有构建产物
./gradlew clean

# 只编译不生成 APK
./gradlew assemble

# 调试版本
./gradlew assembleDebug

# 发布版本 (未签名)
./gradlew assembleRelease

# App Bundle (Google Play)
./gradlew bundleRelease

# 完整构建 (运行测试)
./gradlew build

# 检查依赖
./gradlew dependencies

# 查看任务列表
./gradlew tasks
```

---

## 下一步

### 发布到 Google Play
1. 创建 Google Play Developer 账号 (~25 USD)
2. 创建应用项目
3. 准备应用截图、描述、隐私政策
4. 上传签名的 AAB 文件
5. 设置价格和分发国家
6. 提交审核

### 直接分享 APK
1. 生成签名 APK
2. 上传到文件分享服务 (如 Google Drive, Dropbox)
3. 分享下载链接
4. 用户可直接安装

---

## 文档

- `README.md` — 项目介绍
- `FEATURES.md` — 完整功能清单
- `BUILD_GUIDE.md` — 构建指南
- `PACKAGING.md` — 打包工作流

---

**最后更新:** 2026-10-18  
**应用版本:** 1.0.0  
**最小 SDK:** Android 9 (API 28)  
**目标 SDK:** Android 14 (API 34)
