@echo off
setlocal
cd "%~dp0"
if exist gradle\wrapper\gradle-wrapper.jar (
    java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain %*
) else (
    echo Error: gradle-wrapper.jar not found
    exit /b 1
)
