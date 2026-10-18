@echo off
REM MYSynthetic AI - GitHub 自动上传脚本
REM 使用前需要你手动创建 GitHub 账号和仓库

setlocal enabledelayedexpansion

echo ========================================
echo MYSynthetic AI - GitHub Upload Script
echo ========================================
echo.

REM 检查 Git
echo [1] Checking Git...
git --version >nul 2>&1
if errorlevel 1 (
    echo Error: Git not found. Please install from https://git-scm.com/download/win
    endlocal
    exit /b 1
)
echo OK: Git is installed

REM 项目信息
set PROJECT_DIR=C:\Users\Administrator\Desktop\MYSyntheticApp
set GITHUB_USER=ccgtt
set GITHUB_REPO=MYSyntheticApp
set GITHUB_EMAIL=3298349@qq.com

echo.
echo [2] Project Information:
echo     Directory: %PROJECT_DIR%
echo     GitHub User: %GITHUB_USER%
echo     Repository: %GITHUB_REPO%
echo     Email: %GITHUB_EMAIL%
echo.

REM 进入项目目录
cd /d "%PROJECT_DIR%"
if errorlevel 1 (
    echo Error: Cannot access project directory
    endlocal
    exit /b 1
)

REM 初始化 Git
echo [3] Initializing Git repository...
if exist .git (
    echo Git repository already initialized
) else (
    git init
    git config user.name "%GITHUB_USER%"
    git config user.email "%GITHUB_EMAIL%"
    echo OK: Git initialized
)
echo.

REM 检查远程
echo [4] Configuring remote...
git remote remove origin >nul 2>&1
set REPO_URL=https://github.com/%GITHUB_USER%/%GITHUB_REPO%.git
git remote add origin %REPO_URL%
echo OK: Remote configured: %REPO_URL%
echo.

REM 添加文件
echo [5] Staging files...
git add .
echo OK: Files staged
echo.

REM 检查状态
echo [6] Git status:
git status
echo.

REM 提交
echo [7] Creating commit...
set COMMIT_MSG=Initial commit: Add MYSynthetic AI Android project
git commit -m "%COMMIT_MSG%"
if errorlevel 1 (
    echo Error: Git commit failed
    endlocal
    exit /b 1
)
echo OK: Commit created
echo.

REM 推送
echo [8] Pushing to GitHub...
echo Please enter your GitHub credentials when prompted.
echo (If using personal access token, use it as password)
echo.
git branch -M main
git push -u origin main

if errorlevel 1 (
    echo.
    echo Error: Push failed
    echo Possible reasons:
    echo   1. GitHub credentials incorrect
    echo   2. Repository does not exist (create it on GitHub first)
    echo   3. Network error
    echo.
    echo Please verify and try again.
    endlocal
    exit /b 1
)

echo.
echo ========================================
echo SUCCESS!
echo ========================================
echo.
echo Project uploaded to:
echo   https://github.com/%GITHUB_USER%/%GITHUB_REPO%
echo.
echo Next steps:
echo   1. Go to Actions tab to see build progress
echo   2. Wait 10-15 minutes for first build
echo   3. Download APK from Artifacts
echo.
echo ========================================

endlocal
pause
