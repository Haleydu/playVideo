#!/bin/bash

set -e
MD5=$(md5sum app/build/outputs/apk/release/PlayVideo*.apk)
VERSIONNAME=$(git describe --tags)

printf "{"
printf "\n"
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

echo versionCode:$TRAVIS_BUILD_NUMBER > index.json
echo versionName:"$VERSIONNAME" > index.json
echo md5:"$MD5" > index.json
cat index.json

pwd