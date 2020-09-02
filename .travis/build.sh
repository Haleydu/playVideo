#!/bin/bash

set -e

printf "{"
printf "\n"
MD5=$(md5sum app/build/outputs/apk/release/PlayVideo*.apk)
VERSIONNAME=$(git describe --tags)

printf "versionCode:"
printf $TRAVIS_BUILD_NUMBER
printf "\n"

printf "versionName:"
printf $VERSIONNAME
printf "\n"

printf "md5:"
printf $MD5
printf "\n"
printf "}"


