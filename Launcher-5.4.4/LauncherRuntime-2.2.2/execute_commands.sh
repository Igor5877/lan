#!/bin/bash

# Перевірка наявності вхідного аргументу
if [ -z "$1" ]; then
    echo "No command specified"
    exit 1
fi

case "$1" in
    "gradlew")
        sh /home/igor/Стільниця/оноввлення\ лаучера/Launcher-5.4.4/LauncherRuntime-2.2.2/gradlew
        ;;
    "gradlew-run")
        sh /home/igor/Стільниця/оноввлення\ лаучера/Launcher-5.4.4/LauncherRuntime-2.2.2/gradlew run
        ;;
    # Додайте інші команди, які вам потрібні
    *)
        echo "Unknown command: $1"
        exit 1
        ;;
esac

