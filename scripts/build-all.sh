#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
for version in 1.20.1 1.21.1 1.21.11; do
    ./gradlew build "-Pminecraft_version=$version" "$@"
done
