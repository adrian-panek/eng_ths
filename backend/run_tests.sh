#!/bin/bash

docker start notices-board-db
mvn surefire:test
docker stop notices-board-db
