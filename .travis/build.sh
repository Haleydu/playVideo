#!/bin/bash

set -e

./gradlew --no-daemon clean assembleRelease

TOOLS="$(ls -d ${ANDROID_HOME}/build-tools/* | tail -1)"

TOOLS="$(ls -d build-tools/* | tail -1)"
printf $TOOLS
printf "\n"


MD5=$(md5sum $TOOLS)


printf $MD5
printf "\n"
