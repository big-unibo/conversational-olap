#!/bin/bash
set -e
set -xo

if [ -f .env ]; then
  export $(cat .env | sed 's/#.*//g' | xargs)
fi
docker-compose down
docker-compose up --build -d
./wait-for-it.sh ${MYSQL_URL}:${MYSQL_PORT} --strict --timeout=10 -- echo "MySQL is up"
