# MYSynthetic AI Android App - 完整功能清单

## 概述

MYSynthetic AI 是一个用 Kotlin 编写的、基于 Jetpack Compose 的现代 Android 应用，用于与 MY AI Hermes 后端进行实时聊天交互和任务协作。

---

## 核心功能

### 1. 聊天交互 💬

**主要特性：**
- 实时双向消息收发
- 消息按发送者区分样式（用户消息 vs AI 消息）
- 自动滚动到最新消息
- 消息历史永久保存
- 支持长消息和富文本展示

**文件：**
- `ui/screen/ChatScreen.kt` — 聊天界面 UI
- `ui/viewmodel/ChatViewModel.kt` — 聊天状态管理
- `data/model/ChatModels.kt` — 消息数据模型

---

### 2. API 集成 🌐

**主要特性：**
- Retrofit + OkHttp 网络客户端
- 可配置的 API 端点（默认：`http://localhost:8000`）
- 支持流式响应 (Streaming) 用于实时消息
- 自动重试机制（最多 3 次）
- 连接超时配置（30 秒）
- 完整的错误处理和日志

**API 端点：**
```
POST /v1/chat/completions
  - 发送聊天消息
  - 支持流式响应
  - 支持自定义系统提示词
  - 支持模型选择
```

**文件：**
- `data/network/ApiService.kt` — API 定义和网络配置

---

### 3. 本地数据存储 💾

**主要特性：**
- SQLite 数据库（通过 Room）
- 持久化所有聊天消息
- 快速查询和离线支持
- 消息备份和恢复

**数据库架构：**
- `Message` 表
  - `id` (主键)
  - `content` (消息内容)
  - `role` (user/assistant)
  - `timestamp` (时间戳)
  - `sessionId` (会话 ID)

- `ChatSettings` 表
  - `apiEndpoint` (API 地址)
  - `selectedModel` (选中的模型)
  - `systemPrompt` (系统提示词)
  - `theme` (应用主题)

**文件：**
- `data/db/AppDatabase.kt` — 数据库配置
- `data/db/Daos.kt` — 数据访问对象

---

### 4. 用户设置 ⚙️

**设置项目：**

| 设置项 | 说明 | 默认值 |
|-------|------|--------|
| API 端点 | Hermes 后端地址 | http://localhost:8000 |
| 模型选择 | 使用的 AI 模型 | claude-sonnet-4 |
| 系统提示词 | AI 的行为指导 | 自定义 |
| 应用主题 | 深色/浅色模式 | 系统跟随 |
| 消息字体大小 | 消息展示的字体大小 | 14sp |
| 自动保存 | 自动保存聊天历史 | 开启 |

**文件：**
- `ui/screen/SettingsScreen.kt` — 设置界面

---

### 5. UI 设计 🎨

**框架和库：**
- **Jetpack Compose** — 声明式 UI 框架
- **Material Design 3** — 最新的 Google Material 设计规范
- **Material3** 依赖 — MD3 主题、颜色、组件库

**主要组件：**
- 聊天列表（LazyColumn）
- 消息输入框（TextField）
- 发送按钮（Button）
- 设置面板（NavigationBar）
- 对话框和弹窗

**主题系统：**
- 浅色主题（Light）
- 深色主题（Dark）
- 动态颜色适配（API 31+）
- 自定义字体（Product Sans）

**文件：**
- `ui/theme/Theme.kt` — 主题定义
- `ui/theme/Type.kt` — 排版配置

---

### 6. MVVM 架构 🏗️

**架构层级：**

```
Presentation Layer (UI)
  ├─ ChatScreen (UI 组件)
  ├─ SettingsScreen (设置 UI)
  └─ MainActivity (入口)
  
ViewModel Layer
  └─ ChatViewModel (状态管理)
  
Repository Layer
  └─ ChatRepository (数据聚合)
  
Data Layer
  ├─ ApiService (网络)
  ├─ AppDatabase (数据库)
  └─ Daos (数据操作)
```

**状态管理：**
- 使用 `StateFlow` 管理 UI 状态
- 消息列表、输入框状态、加载状态
- 配置变化自动保存和恢复

**文件：**
- `ui/viewmodel/ChatViewModel.kt` — ViewModel 实现

---

### 7. 异步处理 ⏱️

**技术栈：**
- Kotlin Coroutines — 轻量级异步
- Flow — 响应式数据流
- 后台线程任务处理

**应用场景：**
- 网络请求不阻塞 UI
- 数据库操作异步执行
- 消息发送不卡顿

---

### 8. 安全性 🔒

**实施措施：**
- **ProGuard 代码混淆** — 生产版本代码保护
- **HTTPS 网络连接** — 数据传输加密
- **本地数据加密** — 敏感信息加密存储
- **权限管理** — 最小权限原则
  - `INTERNET` — 网络通信
  - `ACCESS_NETWORK_STATE` — 网络状态检测

**文件：**
- `app/proguard-rules.pro` — ProGuard 配置

---

## 用户交互流程

### 首次启动

```
1. 应用启动 → MainActivity
2. 初始化数据库和 Compose UI
3. 显示聊天界面和欢迎消息
4. 加载本地聊天历史
```

### 发送消息

```
1. 用户在输入框输入文字
2. 点击"发送"按钮
3. 消息保存到本地数据库
4. 消息通过 API 发送到 Hermes 后端
5. 显示加载状态
6. 接收 AI 响应（流式）
7. 实时显示响应文本
8. 响应保存到数据库
```

### 修改设置

```
1. 打开设置界面
2. 修改 API 端点/模型/提示词等
3. 更改立即生效
4. 设置自动保存到数据库
5. 新消息使用新设置
```

---

## 技术栈详细列表

### 依赖库

```gradle
// Jetpack & Android Framework
androidx.core:core-ktx:1.12.0
androidx.lifecycle:lifecycle-runtime-ktx:2.6.2
androidx.activity:activity-compose:1.8.0
androidx.compose.ui:ui:1.5.4
androidx.compose.material3:material3:1.1.2
androidx.compose.material:material-icons-extended:1.5.4

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Networking
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0
com.squareup.okhttp3:okhttp:4.11.0
com.squareup.okhttp3:logging-interceptor:4.11.0

// Serialization
com.google.code.gson:gson:2.10.1

// Testing
junit:junit:4.13.2
androidx.test.ext:junit:1.1.5
androidx.test.espresso:espresso-core:3.5.1
androidx.compose.ui:ui-test-junit4:1.5.4
```

### 构建环境

```
Android SDK: 34 (Android 14)
Gradle: 8.1.1
JDK: 17+
Kotlin: 1.9.10
Compile SDK: 34
Min SDK: 28 (Android 9)
Target SDK: 34 (Android 14)
```

---

## 应用信息

| 属性 | 值 |
|------|-----|
| **应用名称** | MYSynthetic AI |
| **包名** | com.mysynthetic.agent |
| **版本号** | 1.0.0 (v1) |
| **版本名** | 1.0.0 |
| **应用 ID** | com.mysynthetic.agent |
| **最小 SDK** | 28 (Android 9) |
| **目标 SDK** | 34 (Android 14) |
| **编译 SDK** | 34 |

---

## 构建和打包

### 快速打包

**Windows:**
```cmd
build.bat
```

**macOS/Linux:**
```bash
bash build.sh
```

### 输出产物

- `build_artifacts/apk/MYSynthetic-debug.apk` — 调试版本（开发测试）
- `build_artifacts/apk/MYSynthetic-release-unsigned.apk` — 发布预准备版本
- `build_artifacts/aab/MYSynthetic-release.aab` — Google Play 发布包

### 手动构建

```bash
./gradlew assembleDebug      # 调试 APK
./gradlew assembleRelease    # 发布 APK
./gradlew bundleRelease      # App Bundle
```

---

## 项目结构

```
MYSyntheticApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/mysynthetic/agent/
│   │   │   ├── MainActivity.kt
│   │   │   ├── ui/
│   │   │   │   ├── screen/
│   │   │   │   │   ├── ChatScreen.kt
│   │   │   │   │   └── SettingsScreen.kt
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── ChatViewModel.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   └── data/
│   │   │       ├── repository/
│   │   │       │   └── ChatRepository.kt
│   │   │       ├── network/
│   │   │       │   └── ApiService.kt
│   │   │       ├── db/
│   │   │       │   ├── AppDatabase.kt
│   │   │       │   └── Daos.kt
│   │   │       └── model/
│   │   │           └── ChatModels.kt
│   │   ├── res/
│   │   │   ├── drawable/ (图标)
│   │   │   ├── values/ (字符串、颜色)
│   │   │   └── mipmap/ (应用图标)
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.bat
├── build.sh
└── README.md
```

---

## 下一步（可选功能）

- [ ] 消息搜索功能
- [ ] 聊天记录导出
- [ ] 本地 TTS（文字转语音）
- [ ] 语音输入（语音转文字）
- [ ] 消息编辑/删除
- [ ] 多会话支持
- [ ] 插件系统
- [ ] 主题市场

---

## 许可证

MIT License

---

**文档更新时间：** 2026年10月18日  
**应用版本：** 1.0.0  
**最后修改：** 完成打包验证
