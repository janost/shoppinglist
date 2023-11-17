#!/usr/bin/env bash

docker run --rm --name shopping-mysql8 --net host -e MYSQL_ROOT_PASSWORD=supersecret -e MYSQL_DATABASE=shopping -e MYSQL_USER=shopping -e MYSQL_PASSWORD=shopping mysql:8.0-debian

