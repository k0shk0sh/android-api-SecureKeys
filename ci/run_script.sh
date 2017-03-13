#!/bin/bash

STDOUT_FILTERS=$TRAVIS_BUILD_DIR'/ci/stdout_filters.sed'

CYAN='\033[0;36m'
NO_COLOR='\033[0m'

echo -e "${CYAN}Running: ./gradlew $TEST_SUITE ${NO_COLOR}"

./gradlew $TEST_SUITE | sed -f $STDOUT_FILTERS

if [ ${PIPESTATUS[0]} -ne 0 ]; then
    echo "Error while running ./gradlew $TEST_SUITE"
    exit 1
fi