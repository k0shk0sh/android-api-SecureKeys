#!/bin/bash

if [[ ! -e ~/android-ndk-$1 ]]; then
	wget http://dl.google.com/android/repository/android-ndk-$1-linux-x86_64.zip
	unzip -qq -d ~ android-ndk-$1-linux-x86_64.zip
fi