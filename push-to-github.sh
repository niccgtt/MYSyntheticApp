#!/bin/bash

# 这个脚本会将代码推送到 GitHub
# 使用方法: bash push-to-github.sh <token>

set -e

TOKEN="$1"
if [ -z "$TOKEN" ]; then
    echo "错误: 需要提供 GitHub token"
    echo "用法: bash push-to-github.sh <your-github-token>"
    exit 1
fi

cd /c/Users/Administrator/Desktop/MYSyntheticApp

echo "=========================================="
echo "推送代码到 GitHub"
echo "=========================================="
echo

# 创建仓库 URL（使用 token）
REPO_URL="https://niccgtt:${TOKEN}@github.com/niccgtt/MYSyntheticApp.git"

# 配置 remote
echo "[1] 配置 GitHub 远程仓库..."
git remote remove origin 2>/dev/null || true
git remote add origin "$REPO_URL"
echo "✓ 远程仓库已配置"
echo

# 重命名分支为 main
echo "[2] 重命名分支..."
git branch -M main
echo "✓ 分支已重命名为 main"
echo

# 推送代码
echo "[3] 推送代码到 GitHub..."
git push -u origin main
echo "✓ 代码已推送"
echo

echo "=========================================="
echo "成功！"
echo "=========================================="
echo
echo "仓库地址: https://github.com/niccgtt/MYSyntheticApp"
echo
echo "后续步骤:"
echo "1. 登录 GitHub 创建仓库"
echo "2. GitHub Actions 会自动开始构建"
echo "3. 等待 10-15 分钟完成 APK 打包"
echo "4. 在 Actions 标签页下载 APK 文件"
echo

