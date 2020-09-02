#!/bin/bash

set -e

TOOLS="$(ls -d ${ANDROID_HOME}/build-tools/* | tail -1)"

printf $TOOLS
printf "\n"

mkdir -p repo/apk
mkdir -p repo/icon

cp -f apk/* repo/apk

cd repo

APKS=( ../apk/*".apk"* )

for APK in ${APKS[@]}; do
  
    printf APKS
    MD5=$(md5sum APK)
    printf $MD5
    printf "\n"

done


