#!/bin/bash

# MYSynthetic AI Android APK 打包脚本
# 用途: 编译并生成调试和发布版本 APK

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo "=========================================="
echo "MYSynthetic AI - APK 打包工具"
echo "=========================================="
echo ""

# 检查必要的工具
check_requirements() {
    echo "[*] 检查构建环境..."
    
    # 检查 Gradle wrapper
    if [ ! -f "gradlew" ]; then
        echo "[!] 错误: gradlew 不存在"
        echo "    请确保在项目根目录运行此脚本"
        exit 1
    fi
    
    # 检查 build.gradle.kts
    if [ ! -f "app/build.gradle.kts" ]; then
        echo "[!] 错误: app/build.gradle.kts 不存在"
        exit 1
    fi
    
    echo "[✓] 项目结构检查通过"
}

# 清理旧的构建产物
clean_build() {
    echo ""
    echo "[*] 清理旧的构建产物..."
    ./gradlew clean --quiet
    echo "[✓] 清理完成"
}

# 构建调试版本 APK
build_debug_apk() {
    echo ""
    echo "[*] 构建调试版本 APK..."
    ./gradlew assembleDebug --quiet
    
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        APK_SIZE=$(du -h "app/build/outputs/apk/debug/app-debug.apk" | cut -f1)
        echo "[✓] 调试 APK 生成成功"
        echo "    文件: app/build/outputs/apk/debug/app-debug.apk"
        echo "    大小: $APK_SIZE"
        DEBUG_APK_PATH="$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"
    else
        echo "[!] 调试 APK 生成失败"
        exit 1
    fi
}

# 构建发布版本 APK
build_release_apk() {
    echo ""
    echo "[*] 构建发布版本 APK..."
    ./gradlew assembleRelease --quiet
    
    if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
        APK_SIZE=$(du -h "app/build/outputs/apk/release/app-release-unsigned.apk" | cut -f1)
        echo "[✓] 发布 APK 生成成功"
        echo "    文件: app/build/outputs/apk/release/app-release-unsigned.apk"
        echo "    大小: $APK_SIZE"
        RELEASE_APK_PATH="$PROJECT_DIR/app/build/outputs/apk/release/app-release-unsigned.apk"
    else
        echo "[!] 发布 APK 生成失败"
        exit 1
    fi
}

# 构建应用包 (AAB)
build_app_bundle() {
    echo ""
    echo "[*] 构建应用包 (AAB)..."
    ./gradlew bundleRelease --quiet
    
    if [ -f "app/build/outputs/bundle/release/app-release.aab" ]; then
        AAB_SIZE=$(du -h "app/build/outputs/bundle/release/app-release.aab" | cut -f1)
        echo "[✓] AAB 生成成功"
        echo "    文件: app/build/outputs/bundle/release/app-release.aab"
        echo "    大小: $AAB_SIZE"
        AAB_PATH="$PROJECT_DIR/app/build/outputs/bundle/release/app-release.aab"
    else
        echo "[!] AAB 生成失败"
        exit 1
    fi
}

# 生成报告
generate_report() {
    echo ""
    echo "=========================================="
    echo "打包报告"
    echo "=========================================="
    
    mkdir -p "build_artifacts"
    
    REPORT_FILE="build_artifacts/build_report_$(date +%Y%m%d_%H%M%S).txt"
    
    {
        echo "MYSynthetic AI - 构建报告"
        echo "生成时间: $(date)"
        echo ""
        echo "项目信息:"
        echo "  名称: MYSynthetic AI Android"
        echo "  包名: com.mysynthetic.agent"
        echo "  版本: 1.0.0"
        echo "  构建工具: Gradle"
        echo ""
        echo "生成的产物:"
        
        if [ -n "$DEBUG_APK_PATH" ] && [ -f "$DEBUG_APK_PATH" ]; then
            DEBUG_SIZE=$(du -h "$DEBUG_APK_PATH" | cut -f1)
            echo "  [✓] 调试 APK"
            echo "      路径: $DEBUG_APK_PATH"
            echo "      大小: $DEBUG_SIZE"
        fi
        
        if [ -n "$RELEASE_APK_PATH" ] && [ -f "$RELEASE_APK_PATH" ]; then
            RELEASE_SIZE=$(du -h "$RELEASE_APK_PATH" | cut -f1)
            echo "  [✓] 发布 APK (未签名)"
            echo "      路径: $RELEASE_APK_PATH"
            echo "      大小: $RELEASE_SIZE"
        fi
        
        if [ -n "$AAB_PATH" ] && [ -f "$AAB_PATH" ]; then
            AAB_SIZE=$(du -h "$AAB_PATH" | cut -f1)
            echo "  [✓] 应用包 (AAB)"
            echo "      路径: $AAB_PATH"
            echo "      大小: $AAB_SIZE"
        fi
        
        echo ""
        echo "安装说明:"
        echo "  调试版本: adb install app/build/outputs/apk/debug/app-debug.apk"
        echo "  或在 Android Studio 中点击 'Run'"
        echo ""
        echo "发布版本:"
        echo "  需要签名后才能在设备上安装"
        echo "  使用 jarsigner 或 apksigner 进行签名"
        
    } > "$REPORT_FILE"
    
    echo "[✓] 报告已生成: $REPORT_FILE"
}

# 复制 APK 到输出目录
copy_artifacts() {
    echo ""
    echo "[*] 复制构建产物..."
    
    mkdir -p "build_artifacts/apk"
    mkdir -p "build_artifacts/aab"
    
    if [ -n "$DEBUG_APK_PATH" ] && [ -f "$DEBUG_APK_PATH" ]; then
        cp "$DEBUG_APK_PATH" "build_artifacts/apk/MYSynthetic-debug-$(date +%Y%m%d).apk"
        echo "[✓] 调试 APK 已复制"
    fi
    
    if [ -n "$RELEASE_APK_PATH" ] && [ -f "$RELEASE_APK_PATH" ]; then
        cp "$RELEASE_APK_PATH" "build_artifacts/apk/MYSynthetic-release-unsigned-$(date +%Y%m%d).apk"
        echo "[✓] 发布 APK 已复制"
    fi
    
    if [ -n "$AAB_PATH" ] && [ -f "$AAB_PATH" ]; then
        cp "$AAB_PATH" "build_artifacts/aab/MYSynthetic-$(date +%Y%m%d).aab"
        echo "[✓] AAB 已复制"
    fi
}

# 显示最终信息
show_summary() {
    echo ""
    echo "=========================================="
    echo "打包完成 ✓"
    echo "=========================================="
    echo ""
    echo "构建产物位置:"
    echo "  调试 APK: app/build/outputs/apk/debug/"
    echo "  发布 APK: app/build/outputs/apk/release/"
    echo "  AAB 包:   app/build/outputs/bundle/release/"
    echo ""
    echo "快速安装 (调试版):"
    echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "或在 Android Studio 中:"
    echo "  按 Shift+F10 直接运行"
    echo ""
}

# 主函数
main() {
    check_requirements
    clean_build
    build_debug_apk
    build_release_apk
    build_app_bundle
    copy_artifacts
    generate_report
    show_summary
}

main
