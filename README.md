# MYSynthetic AI Android 应用

一个现代化的安卓应用，用于控制和与 MYSynthetic AI 进行自然语言对话。

## 功能特性

- **流畅的对话界面** — 使用 Jetpack Compose 构建的现代化 UI
- **持久化聊天历史** — Room 数据库本地存储对话记录
- **多会话支持** — 可以创建和管理多个独立的聊天会话
- **即时消息传递** — 通过 MY AI API 网关实时通信
- **离线支持** — 本地缓存确保即使网络不稳定也能查看历史
- **深色主题支持** — 自动适应系统主题设置

## 技术架构

### 技术栈

- **Kotlin** — 现代化的 Android 开发语言
- **Jetpack Compose** — 声明式 UI 框架
- **Room Database** — 本地数据持久化
- **OkHttp** — 网络请求
- **Coroutines** — 异步编程
- **MVVM 架构** — 清晰的代码结构和分层

### 目录结构

```
src/main/
├── java/com/mysynthetic/agent/
│   ├── MainActivity.kt              # 应用入口
│   ├── data/
│   │   ├── db/                     # 数据库层
│   │   │   ├── AppDatabase.kt
│   │   │   └── Daos.kt
│   │   ├── model/                  # 数据模型
│   │   │   └── ChatModels.kt
│   │   ├── network/                # 网络层
│   │   │   └── ApiService.kt
│   │   └── repository/             # 仓库层
│   │       └── ChatRepository.kt
│   └── ui/
│       ├── screen/                 # UI 屏幕
│       │   ├── ChatScreen.kt
│       │   └── SettingsScreen.kt
│       ├── theme/                  # 主题配置
│       │   ├── Theme.kt
│       │   └── Type.kt
│       └── viewmodel/              # ViewModel
│           └── ChatViewModel.kt
└── res/
    └── values/
        └── strings.xml             # 字符串资源
```

## 快速开始

### 前提条件

- Android Studio Giraffe 或更新版本
- JDK 17 或更新版本
- 有效的 MY AI API Token

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd MYSyntheticApp
   ```

2. **在 Android Studio 中打开**
   - 打开 Android Studio
   - 选择 "Open Project"
   - 选择项目目录

3. **配置 API Token**
   - 运行应用后，进入设置页面
   - 输入你的 MY AI API Token
   - 输入 API 端点（默认：https://api.my-ai.cn/v1/chat/completions）
   - 点击保存

4. **构建并运行**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

   或在 Android Studio 中点击 "Run"

## 使用指南

### 开始对话

1. 应用启动时会自动创建或加载最后一个会话
2. 在输入框中输入你的消息
3. 点击发送按钮或按 Enter 键
4. 等待 AI 响应

### 管理会话

- **新建会话** — 点击顶部栏的 "+" 按钮
- **查看会话列表** — 点击顶部栏的菜单按钮，显示会话数量
- **删除会话** — 在会话列表中向右滑动或长按会话项

### 设置

进入设置页面可以：
- 修改 API Token
- 修改 API 端点
- 查看应用版本和调试信息

## API 集成

应用通过以下方式与 MY AI 通信：

```
POST /v1/chat/completions
Headers:
  - Authorization: Bearer <your-api-token>
  - Content-Type: application/json

Body:
{
  "messages": [
    {"role": "user", "content": "message"},
    {"role": "assistant", "content": "response"}
  ],
  "model": "MY-Worker",
  "max_tokens": 4096,
  "temperature": 0.7
}
```

## 数据存储

### Room 数据库架构

**messages 表**
- messageId: 唯一标识符
- role: "user" 或 "assistant"
- content: 消息内容
- timestamp: 消息时间戳
- sessionId: 所属会话 ID

**sessions 表**
- sessionId: 唯一标识符
- title: 会话标题
- createdAt: 创建时间
- updatedAt: 最后更新时间
- messageCount: 消息总数

## 权限

应用需要以下权限：

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
```

- **INTERNET** — 连接到 MY AI API
- **RECORD_AUDIO** — 语音输入功能（预留）
- **MODIFY_AUDIO_SETTINGS** — 语音输出控制（预留）

## 开发注意事项

### 添加新功能

1. 在相应的层（Model/Repository/ViewModel/Screen）添加代码
2. 遵循现有的 MVVM 架构模式
3. 使用 Jetpack Compose 构建 UI

### 错误处理

应用使用 Kotlin Result 类型处理错误：

```kotlin
when (val result = repository.sendMessage(...)) {
    is Result.Success -> // 处理成功
    is Result.Failure -> // 处理失败
}
```

### 测试

```bash
# 运行单元测试
./gradlew test

# 运行 UI 测试
./gradlew connectedAndroidTest
```

## 故障排除

### 应用无法连接到 API

1. 检查网络连接
2. 验证 API Token 是否正确
3. 检查 API 端点 URL
4. 查看 Logcat 中的错误信息

### 消息无法保存

1. 确保应用有存储权限
2. 检查设备存储空间
3. 清除应用缓存并重试

### UI 渲染问题

1. 确保 Compose 版本与 Kotlin 版本兼容
2. 在设备上清除应用数据
3. 重新构建项目

## 性能优化

- 使用 Flow 进行响应式数据绑定
- 消息列表使用 LazyColumn 实现虚拟滚动
- 数据库查询使用 suspend 函数避免阻塞主线程
- API 请求使用 OkHttp 连接池

## 安全性

- API Token 使用加密存储（SharedPreferences）
- 所有网络请求使用 HTTPS
- 敏感信息不会记录到日志

## 未来功能规划

- [ ] 语音输入/输出支持
- [ ] 文件/图片分享
- [ ] 消息搜索功能
- [ ] 主题自定义
- [ ] 本地模型支持
- [ ] 离线模式增强
- [ ] 云同步功能

## 许可证

MIT License

## 贡献

欢迎提交 Issue 和 Pull Request！

## 联系方式

- 官网：https://my-ai.cn
- 文档：https://hermes-agent.nousresearch.com

---

**版本**: 1.0.0  
**最后更新**: 2026-10-18
