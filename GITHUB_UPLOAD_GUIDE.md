# GitHub 上传和自动打包步骤

## 账号信息

用户名: ccgtt
邮箱: 3298349@qq.com

## 第一步：注册 GitHub 账号 (如果没有)

1. 打开: https://github.com/signup
2. 邮箱输入: 3298349@qq.com
3. 密码: 自设安全密码 (记住!)
4. 用户名: ccgtt
5. 点击 Create account
6. 验证邮箱 (收邮件，点击链接)

## 第二步：创建新仓库

1. 登录 GitHub: https://github.com/login
2. 点击右上角 + 号 → New repository
3. Repository name: `MYSyntheticApp`
4. Description: `MYSynthetic AI - Android App to control Hermes`
5. 选择 Public (这样 GitHub Actions 免费)
6. ✓ Add a README file
7. ✓ Add .gitignore (选 Android)
8. 点击 Create repository

## 第三步：上传项目代码

### 方式 A: 用网页上传 (最简单)

1. 进入你的仓库 https://github.com/ccgtt/MYSyntheticApp
2. 点击 Add file → Upload files
3. 把桌面 MYSyntheticApp 文件夹中的所有文件拖上去
   (除了 build_artifacts、.gradle、.idea 目录)
4. Commit message: "Initial commit: Add MYSynthetic AI Android project"
5. 点击 Commit changes

### 方式 B: 用 Git 命令 (更快)

```bash
# 如果没装 Git，先装: https://git-scm.com/download/win

cd Desktop/MYSyntheticApp

git init
git add .
git commit -m "Initial commit: Add MYSynthetic AI Android project"
git branch -M main
git remote add origin https://github.com/ccgtt/MYSyntheticApp.git
git push -u origin main
```

(第一次会要求输入 GitHub 用户名和密码或 token)

## 第四步：启用 GitHub Actions 自动打包

1. 进入仓库 https://github.com/ccgtt/MYSyntheticApp
2. 点击 Actions 标签
3. 左侧会显示 "Build Android APK" workflow
4. 点击 Enable

## 第五步：等待自动打包

1. 回到 Code 标签
2. 看到绿色对勾 ✓ 表示构建成功
3. 如果是黄点 ⊙ 表示正在构建
4. 如果是红叉 ✗ 表示构建失败

## 第六步：下载 APK

### 方式 A: 从 Actions 下载 (实时)

1. 点击 Actions 标签
2. 点击最新的 "Build Android APK" workflow run
3. 在 Artifacts 下看到:
   - debug-apk
   - release-apk
   - app-bundle
4. 点击下载

### 方式 B: 从 Releases 下载 (正式版)

1. 点击 Releases (目前没有)
2. 创建 Release 后会有下载链接

## 预期结果

构建成功后会生成:
  • app-debug.apk (10-15 MB) - 开发版本，可直接安装
  • app-release-unsigned.apk (5-8 MB) - 发布版本，需签名
  • app-release.aab (更小) - Google Play 版本

## 安装到手机

1. 下载 app-debug.apk 到电脑
2. 用 USB 连接手机到电脑
3. 启用手机的 USB 调试 (设置 → 开发者选项)
4. 手机会提示 "允许 USB 调试？" → 允许
5. 用 adb 安装:
   adb install app-debug.apk
   
   或直接复制 APK 到手机，用文件管理器打开安装

## 常见问题

Q: GitHub Actions 构建需要多久？
A: 首次 10-15 分钟 (下载依赖)，之后 5-10 分钟

Q: 免费账号有限制吗？
A: 免费账号每月有 2000 分钟的 Actions，足够用

Q: 如果构建失败怎么办？
A: 检查 Actions 日志，或修改代码后重新推送

## 需要帮助？

参考官方文档:
- GitHub 账号注册: https://docs.github.com/en/get-started/signing-up-for-github
- 上传代码: https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template
- GitHub Actions: https://docs.github.com/en/actions

---

准备好了吗？开始注册 GitHub 账号吧！
