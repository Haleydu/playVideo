#!/bin/bash

set -e

TOOLS="$(ls -d ${ANDROID_HOME}/build-tools/* | tail -1)"
printf $TOOLS
printf "\n"

MD5=$(md5sum $TOOLS)

printf $MD5
printf "\n"
