#!/bin/bash
cd "$(dirname "$0")"
if [ -f gradle/wrapper/gradle-wrapper.jar ]; then
    java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain "$@"
else
    echo "Error: gradle-wrapper.jar not found"
    exit 1
fi
