#!/bin/bash

if [ ! -d "/usr/local/android-sdk-linux/platforms/android-25" ]; then
	echo y | android update sdk --no-ui --all --filter "android-25"
fi

if [ ! -d "/usr/local/android-sdk-linux/build-tools/25.0.2" ]; then
	echo y | android update sdk --no-ui --all --filter "build-tools-25.0.2"
fi

if [ ! -d "/usr/local/android_sdk/extras/android/m2repository/com/android/support/support-core-utils/25.1.0" ]; then
	echo y | android update sdk --no-ui --all --filter "extra-android-m2repository"
fi
