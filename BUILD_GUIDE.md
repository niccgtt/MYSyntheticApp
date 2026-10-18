# MYSynthetic AI - APK 打包指南

## 快速开始

### 方式 1: 使用打包脚本 (推荐)

#### Windows:
```bash
build.bat
```

#### macOS/Linux:
```bash
bash build.sh
```

脚本会自动生成调试 APK、发布 APK 和应用包（AAB）。

### 方式 2: 使用 Gradle 命令

#### 同步 Gradle
```bash
./gradlew sync
```

#### 清理构建
```bash
./gradlew clean
```

#### 构建调试 APK
```bash
./gradlew assembleDebug
```

输出: `app/build/outputs/apk/debug/app-debug.apk`

#### 构建发布 APK (未签名)
```bash
./gradlew assembleRelease
```

输出: `app/build/outputs/apk/release/app-release-unsigned.apk`

#### 构建应用包 (AAB - 用于 Google Play)
```bash
./gradlew bundleRelease
```

输出: `app/build/outputs/bundle/release/app-release.aab`

#### 构建并运行 (需要连接设备)
```bash
./gradlew installDebug
```

或在 Android Studio 中按 `Shift+F10`

### 方式 3: Android Studio

1. 打开项目
2. 点击菜单: `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
3. 或按 `Ctrl+F9` 构建，`Shift+F10` 运行

---

## 📦 APK 类型说明

| 类型 | 文件 | 用途 | 签名 |
|------|------|------|------|
| **Debug APK** | app-debug.apk | 开发测试 | 自动签名 |
| **Release APK** | app-release-unsigned.apk | 发布前准备 | 需要手动签名 |
| **App Bundle** | app-release.aab | Google Play 发布 | 需要手动签名 |

---

## 🔧 调试版本安装

### 前提
- 已连接 Android 设备或启动模拟器
- ADB 在 PATH 中

### 方法 1: 使用 adb
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 方法 2: 使用 Android Studio
```bash
./gradlew installDebug
```

### 方法 3: 直接运行
```bash
./gradlew run
```

或在 Android Studio 中按 `Shift+F10`

---

## 🔐 发布版本签名

### 前提条件

需要一个有效的签名密钥库 (.keystore 或 .jks)

### 生成签名密钥 (首次)

```bash
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias my-key-alias
```

系统会要求输入:
- 密码
- 姓名、组织、位置等信息

### 使用 Gradle 签名

编辑 `app/build.gradle.kts`:

```kotlin
android {
    // ... 其他配置 ...
    
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.jks")
            storePassword = "your-store-password"
            keyAlias = "my-key-alias"
            keyPassword = "your-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

然后构建签名的发布 APK:

```bash
./gradlew assembleRelease
```

输出: `app/build/outputs/apk/release/app-release.apk` (已签名)

### 使用命令行工具签名

使用 `jarsigner`:

```bash
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  my-key-alias
```

使用 `apksigner` (推荐):

```bash
apksigner sign --ks my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## 📊 构建配置参考

### SDK 版本
```
Min SDK:      28 (Android 9)
Target SDK:   34 (Android 14)
Compile SDK:  34
```

### 代码混淆

启用 ProGuard 混淆 (release 构建):

编辑 `app/build.gradle.kts`:

```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

ProGuard 规则已在 `proguard-rules.pro` 中配置。

---

## 🚀 上传到 Google Play

### 前提
- Google Play 开发者账户
- 签名的 APK 或 AAB
- 应用信息 (名称、描述、截图等)

### 步骤

1. **创建应用包**
   ```bash
   ./gradlew bundleRelease
   ```

2. **登录 Google Play Console**
   https://play.google.com/console

3. **创建新应用或选择现有应用**

4. **上传 AAB 文件**
   - 转到: 发布 → 应用版本 → 生产
   - 点击: 创建新版本
   - 上传: `app/build/outputs/bundle/release/app-release.aab`

5. **填写应用信息**
   - 版本号
   - 发布说明
   - 内容分级

6. **审核和发布**
   - Google 会审核应用 (通常 2-4 小时)
   - 审核通过后发布到生产环境

---

## 📋 常见问题

### Q: 构建失败，说找不到 JDK
**A:** 
- 确保 JDK 17+ 已安装
- 设置 `JAVA_HOME` 环境变量:
  ```bash
  # Windows
  set JAVA_HOME=C:\Program Files\Java\jdk-17
  
  # macOS/Linux
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
  ```

### Q: 构建很慢
**A:**
- 第一次构建会下载依赖，很慢是正常的
- 后续构建会快很多
- 可以使用 `--build-cache` 加快增量构建:
  ```bash
  ./gradlew assemble --build-cache
  ```

### Q: APK 体积很大
**A:**
- 启用代码混淆:
  ```kotlin
  isMinifyEnabled = true
  ```
- 启用 resource shrinking:
  ```kotlin
  shrinkResources = true
  ```
- 分离 ABI:
  ```kotlin
  splits {
      abi {
          enable = true
          reset()
          include("armeabi-v7a", "arm64-v8a")
          universalApk = true
      }
  }
  ```

### Q: 如何测试签名的 APK
**A:**
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### Q: 忘记了签名密钥密码
**A:**
- 需要重新生成签名密钥
- 旧版本应用将无法更新到新版本
- 建议保存好密钥和密码

---

## 🔗 参考资源

- [Android 官方文档 - 构建应用](https://developer.android.com/studio/build)
- [Gradle 构建指南](https://gradle.org/guides/building-android-apps/)
- [Google Play 应用发布](https://developer.android.com/distribute)
- [APK 签名指南](https://developer.android.com/studio/publish/app-signing)

---

## 📝 版本信息

- **应用名称**: MYSynthetic AI
- **包名**: com.mysynthetic.agent
- **当前版本**: 1.0.0
- **最后更新**: 2026-10-18

