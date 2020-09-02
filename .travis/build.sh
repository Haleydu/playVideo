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

jq -n \
        --argjson versionCode "$VERSIONNAME" \
        --arg versionName "$VERSIONNAME" \
        --arg md5 $MD5 \
        '{versionCode:$versionCode, versionName:$versionName, md5:$md5}'

jq -sr '[.[]]' > index.json

cat index.json

pwd