#!/bin/bash

docker start notices_board_db
mvn surefire:test
docker stop notices_board_db
