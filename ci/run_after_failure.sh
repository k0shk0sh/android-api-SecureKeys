#!/bin/bash

MODULE_DIR=$1

if [ -d "MODULE_DIR" ]; then
    if [[ "$TRAVIS_PULL_REQUEST" == "false" ]]; then
        TRAVIS_PR="testing"
    else
        TRAVIS_PR="$TRAVIS_PULL_REQUEST"
    fi

    TRAVIS_JOB_AND_NUMBER=$(echo "$TRAVIS_JOB_NUMBER" | sed "s/\./_/g")

    FILE_NAME="mobile-reports-${TRAVIS_PR}-${TRAVIS_JOB_AND_NUMBER}.tar.gz"
    tar -zcf "$FILE_NAME" "MODULE_DIR" 1>/dev/null

    echo -e "${CYAN}Reports found! Uploading...${NO_COLOR}"

    RESPONSE=$(curl --progress-bar -F "file=@${FILE_NAME}" https://file.io)
    OUTPUT=$(echo $RESPONSE | python -c "import sys, json; print json.load(sys.stdin)['link']")

    echo -e "${CYAN}You can find a tar with the reports in: $OUTPUT ${NO_COLOR}"

    rm -f "$FILE_NAME"
fi