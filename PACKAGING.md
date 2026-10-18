# MYSynthetic AI - 完整打包工作流

## 🎯 三种打包方式对比

| 方式 | 命令 | 时间 | 难度 | 适用场景 |
|------|------|------|------|---------|
| **脚本一键打包** | `build.bat` / `bash build.sh` | 5-10 分钟 | 非常简单 | ✓ 推荐 (自动化) |
| **Gradle 命令** | `./gradlew assemble*` | 5-10 分钟 | 简单 | 灵活控制 |
| **Android Studio** | UI 点击 | 5-10 分钟 | 最简单 | 初学者友好 |

---

## 📦 方式 1: 一键打包脚本 (推荐)

### Windows 用户

```bash
# 1. 打开命令提示符或 PowerShell
# 2. 进入项目目录
cd E:\MY AI\hermes\android\MYSyntheticApp

# 3. 运行打包脚本
build.bat
```

**输出:**
```
[OK] Project structure verified
[OK] Clean complete
[OK] Debug APK generated successfully
[OK] Release APK generated successfully
[OK] App bundle generated successfully
[OK] Build artifacts copied
[OK] Build report generated
Build Complete!
```

### macOS/Linux 用户

```bash
cd ~/projects/MYSyntheticApp
bash build.sh
```

### 脚本做了什么

1. ✓ 检查项目结构
2. ✓ 清理旧的构建产物
3. ✓ 构建调试 APK (app-debug.apk)
4. ✓ 构建发布 APK (app-release-unsigned.apk)
5. ✓ 构建应用包 (app-release.aab)
6. ✓ 复制产物到 `build_artifacts/` 目录
7. ✓ 生成构建报告

### 输出文件

```
build_artifacts/
├── apk/
│   ├── MYSynthetic-debug.apk              (用于测试)
│   └── MYSynthetic-release-unsigned.apk   (需签名才能发布)
├── aab/
│   └── MYSynthetic-release.aab            (Google Play)
└── build_report.txt                        (构建报告)
```

---

## 🛠️ 方式 2: Gradle 命令行打包

### 前置检查

```bash
# 进入项目目录
cd E:\MY AI\hermes\android\MYSyntheticApp

# 验证 Gradle wrapper
./gradlew --version
```

### 构建调试版本 (开发测试)

```bash
# 清理旧构建
./gradlew clean

# 构建调试 APK (用于测试和开发)
./gradlew assembleDebug
```

**输出路径:** `app/build/outputs/apk/debug/app-debug.apk`

**文件大小:** 通常 5-10 MB

**签名状态:** ✓ 自动签名 (调试密钥)

**用途:**
- 在测试设备上快速测试
- 开发和调试
- QA 测试

### 构建发布版本 (未签名)

```bash
# 构建发布 APK (未签名)
./gradlew assembleRelease
```

**输出路径:** `app/build/outputs/apk/release/app-release-unsigned.apk`

**签名状态:** ✗ 未签名 (需要手动签名)

**注意:** 生产环境使用前必须签名

### 构建应用包 (Google Play)

```bash
# 构建 AAB (用于 Google Play Store)
./gradlew bundleRelease
```

**输出路径:** `app/build/outputs/bundle/release/app-release.aab`

**用途:** 上传到 Google Play Console

**优势:**
- Google Play 自动为不同设备生成优化的 APK
- 减小应用大小
- 推荐的发布格式

### 一次构建多个产物

```bash
# 构建所有 APK 和 Bundle
./gradlew assemble bundle
```

---

## 🎨 方式 3: Android Studio UI 打包

### 构建调试版本

1. 打开 Android Studio
2. 菜单: `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
3. 或快捷键: `Ctrl+F9` (Windows/Linux) / `Cmd+B` (macOS)
4. 输出会显示在底部构建窗口

### 直接运行到设备

1. 连接 Android 设备或启动模拟器
2. 快捷键: `Shift+F10` (Windows/Linux) / `Ctrl+R` (macOS)
3. 自动构建、签名、安装并运行应用

### 在设备选择器中选择目标

1. 打开设备窗口: `View` → `Tool Windows` → `Devices`
2. 选择要运行的设备
3. 按 `Shift+F10` 运行

---

## 📲 安装到设备

### 调试 APK 安装

#### 方法 1: 使用 adb

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### 方法 2: 使用 Gradle

```bash
./gradlew installDebug
```

#### 方法 3: 使用 Android Studio

```
Run → Run 'app' (Shift+F10)
```

#### 方法 4: 文件管理器

1. 将 APK 文件传到设备
2. 在设备上打开文件管理器
3. 点击 APK 文件进行安装

### 查看安装进度

```bash
# 等待并显示详细信息
adb install -l app/build/outputs/apk/debug/app-debug.apk

# 显示帮助
adb install --help
```

---

## 🔐 签名发布版本

### 场景 1: 首次签名 (创建密钥库)

```bash
# 生成签名密钥 (第一次)
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias mysynthetic-key

# 系统会提示输入:
# - 密钥库密码 (6+ 字符)
# - 确认密码
# - 姓名: Your Name
# - 组织单位: MYSynthetic
# - 组织: MYSynthetic
# - 城市: Beijing
# - 省份: Beijing  
# - 国家代码: CN
# - 密钥别名密码 (回车使用密钥库密码)

# 输出: my-release-key.jks (妥善保管!)
```

**警告:** 丢失密钥库后将无法更新应用!

### 场景 2: 使用现有密钥签名

#### 方法 A: 配置 Gradle 自动签名

编辑 `app/build.gradle.kts`:

```kotlin
android {
    // ... 其他配置 ...
    
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.jks")
            storePassword = "your-keystore-password"
            keyAlias = "mysynthetic-key"
            keyPassword = "your-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

然后构建:

```bash
./gradlew assembleRelease
```

**输出:** `app/build/outputs/apk/release/app-release.apk` (已签名)

#### 方法 B: 使用 jarsigner 手动签名

```bash
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  mysynthetic-key

# 输入密钥库密码
```

#### 方法 C: 使用 apksigner 手动签名 (推荐)

```bash
# 如果 apksigner 在 PATH 中
apksigner sign --ks my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk

# 或指定完整路径
"C:\Android\Sdk\build-tools\34.0.0\apksigner.bat" sign \
  --ks my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk
```

### 验证签名

```bash
# 检查 APK 是否已签名
jarsigner -verify -verbose app/build/outputs/apk/release/app-release.apk

# 输出应包含:
# jar verified.
# This jar contains entries whose certificate chain is not validated.
```

---

## 🚀 发布流程

### 1. 准备发布

- [ ] 更新版本号 `versionCode` 和 `versionName`
- [ ] 测试所有功能
- [ ] 构建发布版本
- [ ] 签名应用

### 2. 上传到 Google Play

```bash
# 1. 构建 AAB (推荐用于 Play Store)
./gradlew bundleRelease

# 2. 登录 Google Play Console
# https://play.google.com/console

# 3. 创建新版本
# - 点击: 发布 → 应用版本 → 生产
# - 点击: 创建新版本
# - 上传: app/build/outputs/bundle/release/app-release.aab

# 4. 填写版本信息
# - 版本号
# - 发布说明
# - 新功能描述

# 5. 选择资源和内容
# - 设置应用商店列表
# - 上传截图
# - 应用描述

# 6. 内容分级
# - 填写内容分级问卷

# 7. 定价和发布
# - 设置价格
# - 选择目标国家
# - 点击: 审核并发布
```

### 3. 审核和发布

Google Play 审核 (通常 2-4 小时):
- 自动检查
- 内容审核
- 安全扫描
- 功能测试

---

## 📊 构建配置查看

### 查看当前版本信息

```bash
# app/build.gradle.kts 中:
android {
    defaultConfig {
        applicationId = "com.mysynthetic.agent"
        minSdk = 28
        targetSdk = 34
        versionCode = 1              // 递增数字
        versionName = "1.0.0"        // 用户可见版本
    }
}
```

### 递增版本号

发布新版本时:

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        versionCode = 2              // +1
        versionName = "1.0.1"        // 递增
    }
}
```

**规则:**
- `versionCode`: 整数，每次发布递增 (Google Play 要求)
- `versionName`: 字符串，用户看到的版本 (如 "1.0.0")

---

## 🔍 故障排除

| 问题 | 原因 | 解决方案 |
|------|------|---------|
| 找不到 JDK | Java 未安装或不在 PATH | 安装 JDK 17+ 并设置 JAVA_HOME |
| Gradle 同步失败 | 网络/代理问题 | 检查网络，或在 gradle.properties 中配置代理 |
| APK 过大 | 未启用混淆或资源收缩 | 启用 `isMinifyEnabled = true` |
| 签名失败 | 密钥库不存在或密码错误 | 检查文件路径和密码 |
| Google Play 拒绝 | APK 格式、签名或内容问题 | 查看 Play Console 的详细错误 |

---

## 📝 快速参考

```bash
# 最快打包 (推荐)
build.bat                                      # Windows
bash build.sh                                  # macOS/Linux

# 调试版本 (开发测试)
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk

# 发布版本 (需签名)
./gradlew bundleRelease
# 上传到 Google Play Console

# 直接运行
./gradlew installDebug                         # 或 Shift+F10

# 清理并重新构建
./gradlew clean assembleDebug
```

---

**版本**: 1.0.0  
**最后更新**: 2026-10-18
