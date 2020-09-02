#!/bin/bash

set -e

MD5=$(md5sum app/build/outputs/apk/release/PlayVideo*.apk)

printf $MD5
printf "\n"


