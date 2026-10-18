@echo off
REM MYSynthetic AI Android APK Build Script
REM Purpose: Compile and generate debug and release APK

setlocal enabledelayedexpansion

cd /d "%~dp0"
set PROJECT_DIR=%cd%

echo.
echo ==========================================
echo MYSynthetic AI - APK Build Tool
echo ==========================================
echo.

REM Check requirements
echo [*] Checking build environment...

if not exist "gradlew.bat" (
    echo [ERROR] gradlew.bat not found
    echo         Please run this script from project root directory
    exit /b 1
)

if not exist "app\build.gradle.kts" (
    echo [ERROR] app\build.gradle.kts not found
    exit /b 1
)

echo [OK] Project structure verified
echo.

REM Clean build
echo [*] Cleaning old build artifacts...
call gradlew.bat clean --quiet 2>nul
if errorlevel 1 (
    echo [WARNING] Clean failed, continuing anyway...
)
echo [OK] Clean complete
echo.

REM Build debug APK
echo [*] Building debug APK...
call gradlew.bat assembleDebug --quiet 2>nul

if exist "app\build\outputs\apk\debug\app-debug.apk" (
    for /f %%A in ('powershell -command "[math]::Round((Get-Item app\build\outputs\apk\debug\app-debug.apk).length/1MB, 2)" 2^>nul') do set DEBUG_SIZE=%%A
    echo [OK] Debug APK generated successfully
    echo     File: app\build\outputs\apk\debug\app-debug.apk
    echo     Size: !DEBUG_SIZE! MB
    set DEBUG_APK=app\build\outputs\apk\debug\app-debug.apk
) else (
    echo [ERROR] Debug APK generation failed
    exit /b 1
)
echo.

REM Build release APK
echo [*] Building release APK (unsigned)...
call gradlew.bat assembleRelease --quiet 2>nul

if exist "app\build\outputs\apk\release\app-release-unsigned.apk" (
    for /f %%A in ('powershell -command "[math]::Round((Get-Item app\build\outputs\apk\release\app-release-unsigned.apk).length/1MB, 2)" 2^>nul') do set RELEASE_SIZE=%%A
    echo [OK] Release APK generated successfully
    echo     File: app\build\outputs\apk\release\app-release-unsigned.apk
    echo     Size: !RELEASE_SIZE! MB
    set RELEASE_APK=app\build\outputs\apk\release\app-release-unsigned.apk
) else (
    echo [ERROR] Release APK generation failed
    exit /b 1
)
echo.

REM Build app bundle (AAB)
echo [*] Building app bundle (AAB)...
call gradlew.bat bundleRelease --quiet 2>nul

if exist "app\build\outputs\bundle\release\app-release.aab" (
    for /f %%A in ('powershell -command "[math]::Round((Get-Item app\build\outputs\bundle\release\app-release.aab).length/1MB, 2)" 2^>nul') do set AAB_SIZE=%%A
    echo [OK] App bundle generated successfully
    echo     File: app\build\outputs\bundle\release\app-release.aab
    echo     Size: !AAB_SIZE! MB
    set AAB=app\build\outputs\bundle\release\app-release.aab
) else (
    echo [WARNING] AAB generation skipped or failed
)
echo.

REM Create artifacts directory
if not exist "build_artifacts" mkdir build_artifacts
if not exist "build_artifacts\apk" mkdir build_artifacts\apk
if not exist "build_artifacts\aab" mkdir build_artifacts\aab

REM Copy artifacts
echo [*] Copying build artifacts...
if defined DEBUG_APK (
    copy "!DEBUG_APK!" "build_artifacts\apk\MYSynthetic-debug.apk" >nul
    echo [OK] Debug APK copied
)

if defined RELEASE_APK (
    copy "!RELEASE_APK!" "build_artifacts\apk\MYSynthetic-release-unsigned.apk" >nul
    echo [OK] Release APK copied
)

if defined AAB (
    copy "!AAB!" "build_artifacts\aab\MYSynthetic-release.aab" >nul
    echo [OK] App bundle copied
)
echo.

REM Generate build report
echo [*] Generating build report...
setlocal enabledelayedexpansion

(
    echo MYSynthetic AI - Build Report
    echo Generated: %date% %time%
    echo.
    echo Project Information:
    echo   Name: MYSynthetic AI Android
    echo   Package: com.mysynthetic.agent
    echo   Version: 1.0.0
    echo   Build Tool: Gradle
    echo.
    echo Build Artifacts:
    if defined DEBUG_APK echo   [OK] Debug APK: !DEBUG_APK!
    if defined RELEASE_APK echo   [OK] Release APK (unsigned): !RELEASE_APK!
    if defined AAB echo   [OK] App Bundle: !AAB!
    echo.
    echo Installation Instructions:
    echo   Debug (Recommended for testing):
    echo     adb install app\build\outputs\apk\debug\app-debug.apk
    echo     Or: Shift+F10 in Android Studio
    echo.
    echo   Release (requires signing):
    echo     1. Sign with jarsigner or apksigner
    echo     2. Upload to Google Play Store
) > build_artifacts\build_report.txt

echo [OK] Build report generated: build_artifacts\build_report.txt
echo.

REM Final summary
echo ==========================================
echo Build Complete!
echo ==========================================
echo.
echo Output Locations:
echo   Debug APK:   app\build\outputs\apk\debug\
echo   Release APK: app\build\outputs\apk\release\
echo   AAB Bundle:  app\build\outputs\bundle\release\
echo   Artifacts:   build_artifacts\
echo.
echo Quick Install (Debug):
echo   adb install app\build\outputs\apk\debug\app-debug.apk
echo.
echo Or in Android Studio:
echo   Press Shift+F10 to run directly
echo.
echo ==========================================

endlocal
