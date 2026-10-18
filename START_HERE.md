# GitHub 自动打包 - 5 分钟快速指南

## 你的账号信息

用户名: **ccgtt**
邮箱: **3298349@qq.com**

## 5 个快速步骤

### 1️⃣ 注册 GitHub (2 分钟)

打开: https://github.com/signup

填写:
- Email: 3298349@qq.com
- Password: (自设，要复杂)
- Username: ccgtt

验证邮箱后完成。

---

### 2️⃣ 创建仓库 (1 分钟)

登录: https://github.com/login

1. 右上角 + 号 → New repository
2. 仓库名: MYSyntheticApp
3. Public (勾选)
4. ✓ Add a README
5. ✓ Add .gitignore → Android
6. Create repository

---

### 3️⃣ 上传代码 (2 分钟)

#### 简单方式 (网页上传)

1. 你的仓库页面: https://github.com/ccgtt/MYSyntheticApp
2. 点 Add file → Upload files
3. 把桌面 MYSyntheticApp 文件夹的所有文件拖上去
   (忽略: build_artifacts, .gradle, .idea, build 文件夹)
4. Commit message: Initial commit
5. Commit changes

#### 快速方式 (Git 命令)

如果装了 Git:

```bash
cd Desktop/MYSyntheticApp
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/ccgtt/MYSyntheticApp.git
git push -u origin main
```

---

### 4️⃣ 启用自动打包 (0.5 分钟)

1. 仓库页面 → Actions 标签
2. 会看到 "Build Android APK" workflow
3. 点 Enable

完成！GitHub Actions 会自动运行。

---

### 5️⃣ 下载 APK (1 分钟)

等待 Actions 完成 (绿色对勾):

1. 仓库页面 → Actions 标签
2. 点最新的 workflow run
3. Artifacts 下载:
   - debug-apk (推荐，可直接装手机)
   - release-apk
   - app-bundle

---

## ⏱️ 时间表

首次构建:
- 上传代码: 2-3 分钟
- GitHub Actions 编译: 10-15 分钟
- 下载 APK: 1 分钟

总计: **20-25 分钟内有 APK**

之后每次推送代码都会自动重新构建。

---

## 📱 安装到手机

下载 app-debug.apk 后:

1. USB 连接手机
2. 启用 USB 调试 (设置 → 关于手机 → 开发者选项)
3. 运行: adb install app-debug.apk
4. 或复制 APK 到手机，文件管理器中打开

---

## ✅ 就这么简单

现在就注册 GitHub，5 分钟内开始自动打包！

需要帮助？看详细版: GITHUB_UPLOAD_GUIDE.md
