yes | sudo apt-get install unzip

wget https://dl.google.com/android/repository/android-ndk-r14-linux-x86_64.zip -O ndk.zip

unzip -qq ndk.zip

export ANDROID_NDK_HOME=$PWD/android-ndk-r14

export PATH=${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools:${ANDROID_NDK_HOME}
