# MYSynthetic AI 安卓应用 - 完整安装指南

## 项目概述

这是一个完整的安卓 Jetpack Compose 应用，用于与 MYSynthetic AI 进行自然语言对话。

## 项目结构

```
MYSyntheticApp/
├── build.gradle.kts                           # 顶级构建配置
├── settings.gradle.kts                        # Gradle 设置
├── gradle.properties                          # Gradle 属性
├── proguard-rules.pro                         # 混淆规则
├── README.md                                  # 使用指南
├── SETUP.md                                   # 此文件
└── src/
    └── main/
        ├── AndroidManifest.xml
        ├── java/com/mysynthetic/agent/
        │   ├── MainActivity.kt
        │   ├── data/
        │   │   ├── db/
        │   │   │   ├── AppDatabase.kt        # Room 数据库
        │   │   │   └── Daos.kt               # 数据访问对象
        │   │   ├── model/
        │   │   │   └── ChatModels.kt         # 数据模型
        │   │   ├── network/
        │   │   │   └── ApiService.kt         # API 服务
        │   │   └── repository/
        │   │       └── ChatRepository.kt     # 仓库模式
        │   └── ui/
        │       ├── screen/
        │       │   ├── ChatScreen.kt         # 主聊天屏幕
        │       │   └── SettingsScreen.kt     # 设置屏幕
        │       ├── theme/
        │       │   ├── Theme.kt              # 主题定义
        │       │   └── Type.kt               # 排版配置
        │       └── viewmodel/
        │           └── ChatViewModel.kt      # ViewModel
        └── res/
            └── values/
                └── strings.xml               # 字符串资源
```

## 核心功能模块

### 1. 数据层 (Data Layer)

#### 模型 (ChatModels.kt)
- `ChatMessage` — 聊天消息对象
- `ToolCall` — 工具调用
- `ToolResult` — 工具结果
- `ApiRequest/Response` — API 通信格式
- `MessageEntity/SessionEntity` — 数据库实体

#### 数据库 (AppDatabase.kt, Daos.kt)
- Room ORM 集成
- 消息表 — 存储所有聊天消息
- 会话表 — 管理聊天会话
- 支持异步 Flow 查询

#### 网络服务 (ApiService.kt)
- OkHttp 客户端
- JSON 序列化/反序列化
- Bearer Token 认证
- 错误处理和日志记录

#### 仓库 (ChatRepository.kt)
- 数据访问的统一接口
- 实现 MVVM 架构中的数据层
- 协调数据库和网络操作

### 2. 表现层 (Presentation Layer)

#### ViewModel (ChatViewModel.kt)
- 管理 UI 状态
- 协调用户交互
- 处理异步操作
- 状态流 (StateFlow) 实现

#### 屏幕 (ChatScreen.kt)
- 主聊天界面
- 消息列表展示
- 输入框和发送按钮
- 错误提示

#### 主题 (Theme.kt, Type.kt)
- Material 3 设计系统
- 深色/浅色主题切换
- 自定义排版

### 3. 应用入口 (MainActivity.kt)

- 初始化数据库
- 创建 API 服务
- 实例化 Repository
- 设置 Compose 内容树

## 依赖关系

### 核心依赖
- **Jetpack Compose 1.5.3** — 声明式 UI 框架
- **Material 3** — Material Design 3 组件库
- **Room 2.6.0** — 本地数据库
- **OkHttp 4.11.0** — HTTP 客户端
- **Kotlin Coroutines** — 异步编程
- **Kotlin Serialization** — JSON 序列化

### 版本要求
- **Kotlin**: 1.9.10
- **Gradle**: 8.1.1
- **minSdk**: 28
- **targetSdk**: 34
- **compileSdk**: 34

## 工作流程

### 发送消息流程

```
1. 用户输入消息并点击发送
   ↓
2. ViewModel 接收输入
   ↓
3. Repository 保存用户消息到数据库
   ↓
4. ApiService 发送请求到 MY AI
   ↓
5. 接收 AI 响应
   ↓
6. 保存 AI 消息到数据库
   ↓
7. UI 自动更新显示新消息
```

### 数据流

```
UI (Compose) ← StateFlow ← ViewModel ← Repository ← Data Sources
                                          ↓
                                      Database (Room)
                                      Network (OkHttp)
```

## 构建和运行

### 前提条件
- Android Studio Giraffe+
- JDK 17+
- Gradle 8.1+

### 步骤

1. **打开项目**
   ```bash
   cd MYSyntheticApp
   ```

2. **同步 Gradle**
   - Android Studio 会自动提示
   - 或执行：`./gradlew sync`

3. **配置 API Token**
   - 编辑 `MainActivity.kt`
   - 将 `"your-api-token-here"` 替换为实际 token

4. **构建 APK**
   ```bash
   ./gradlew assembleDebug    # 调试版本
   ./gradlew assembleRelease  # 发布版本
   ```

5. **运行应用**
   ```bash
   ./gradlew installDebug
   ./gradlew installRelease
   ```

   或在 Android Studio 中点击 "Run"

## 关键技术亮点

### MVVM 架构
- 清晰的关注点分离
- ViewModel 持有 UI 状态
- Repository 隐藏数据源细节

### 响应式编程
- StateFlow 用于状态管理
- Flow 用于异步数据流
- Coroutines 用于后台操作

### 现代 UI
- Jetpack Compose 声明式 UI
- Material Design 3 组件
- 主题系统支持

### 本地存储
- Room 数据库持久化
- 支持离线查看历史消息

### 网络通信
- OkHttp 高性能 HTTP 客户端
- JSON 序列化支持
- Token-based 认证

## API 集成详解

### 端点配置

```kotlin
val apiGatewayUrl = "https://api.my-ai.cn/v1/chat/completions"
```

### 请求格式

```json
{
  "messages": [
    {"role": "user", "content": "用户消息"},
    {"role": "assistant", "content": "AI 回复"}
  ],
  "model": "MY-Worker",
  "max_tokens": 4096,
  "temperature": 0.7
}
```

### 响应格式

```json
{
  "id": "xxx",
  "choices": [
    {
      "message": {
        "role": "assistant",
        "content": "AI 回复内容"
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 10,
    "completion_tokens": 50,
    "total_tokens": 60
  }
}
```

## 数据库架构

### messages 表
| 列名 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 自增主键 |
| messageId | TEXT | 消息唯一标识 |
| role | TEXT | "user" 或 "assistant" |
| content | TEXT | 消息内容 |
| timestamp | LONG | 时间戳 |
| sessionId | TEXT | 所属会话 ID |
| toolCalls | TEXT | JSON 工具调用 |
| toolResults | TEXT | JSON 工具结果 |

### sessions 表
| 列名 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 自增主键 |
| sessionId | TEXT | 会话唯一标识 |
| title | TEXT | 会话标题 |
| createdAt | LONG | 创建时间 |
| updatedAt | LONG | 更新时间 |
| messageCount | INT | 消息数量 |

## 扩展指南

### 添加新的 UI 屏幕

1. 在 `ui/screen/` 创建新的 Compose 函数
2. 在 ViewModel 中添加相应状态
3. 在 MainActivity 中导航到新屏幕

### 添加新的数据模型

1. 在 `data/model/` 定义数据类
2. 在 `data/db/` 创建 Entity 和 Dao
3. 在 `data/repository/` 添加访问方法

### 添加新的 API 端点

1. 在 `data/network/` 扩展 ApiService
2. 定义请求和响应模型
3. 在 Repository 中调用

## 常见问题

### Q: 如何修改 API Token？
A: 在应用设置页面输入 API Token 并保存即可。

### Q: 如何处理网络错误？
A: 应用会自动显示错误提示，用户可以点击重试。

### Q: 消息数据会丢失吗？
A: 不会。所有消息都保存到本地数据库，支持离线查看。

### Q: 如何增加自定义主题？
A: 编辑 `ui/theme/Theme.kt` 中的颜色方案。

## 性能优化建议

1. 使用 LazyColumn 显示大消息列表
2. 消息限制查询（limit: 100）
3. 使用 OkHttp 连接池
4. 启用 Gradle 并行构建
5. 使用 ProGuard 代码混淆

## 调试技巧

### 查看网络请求
```kotlin
// ApiService.kt 中已配置 HttpLoggingInterceptor
// 查看 Logcat：
// adb logcat | grep ApiService
```

### 查看数据库内容
```bash
# 使用 Android Studio Database Inspector
# 右键点击设备 → Device File Explorer → data/data/com.mysynthetic.agent/databases
```

### 性能分析
```bash
# 使用 Android Studio Profiler
# 查看 CPU、内存、网络使用情况
```

## 部署和发布

### 准备发布版本

1. 更新版本号
   ```gradle
   versionCode = 2
   versionName = "1.1.0"
   ```

2. 生成签名 APK
   ```bash
   ./gradlew bundleRelease
   ```

3. 签名应用
   - 在 Android Studio：Build → Generate Signed Bundle/APK

4. 上传到 Google Play Store

## 维护清单

- [ ] 定期更新依赖版本
- [ ] 运行单元测试和 UI 测试
- [ ] 检查代码质量工具输出
- [ ] 性能基准测试
- [ ] 安全扫描

---

**版本**: 1.0.0  
**最后更新**: 2026-10-18
