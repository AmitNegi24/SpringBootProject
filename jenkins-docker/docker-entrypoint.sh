#!/bin/bash

DOCKER_SOCKET=/var/run/docker.sock

if [ -S "$DOCKER_SOCKET" ]; then
    DOCKER_GID=$(stat -c '%g' "$DOCKER_SOCKET")

    if ! getent group "$DOCKER_GID" > /dev/null 2>&1; then
        groupadd -g "$DOCKER_GID" docker-host
    fi

    DOCKER_GROUP=$(getent group "$DOCKER_GID" | cut -d: -f1)

    usermod -aG "$DOCKER_GROUP" jenkins
fi

exec /usr/bin/tini -- /usr/local/bin/jenkins.sh