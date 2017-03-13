#!/bin/bash

export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

DEPS="$ANDROID_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
    echo y | android update sdk -u -a -t build-tools-25.0.2 &&
        echo y | android update sdk -u -a -t android-25 &&
        echo y | android update sdk -u -a -t extra-android-m2repository &&
        echo y | android update sdk -u -a -t extra-google-m2repository &&
        touch $DEPS
fi