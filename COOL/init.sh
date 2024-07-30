#!/bin/bash
set -exo
cp .env.example .env
cp src/main/resources/config.example.yml src/main/resources/config.yml
P=$(pwd)
sed -i "s+\!HOME\!+${P}+g" src/main/resources/config.yml
chmod +x ./gradlew

cd src/main/python
if [ -d venv ]; then
    echo "venv already exists"
else
    if [[ "$(python -V)" =~ "Python 3" ]]; then
        echo "python found"
        python -m venv venv
    fi
    if [[ "$(python3 -V)" =~ "Python 3" ]]; then
        echo "python3 found"
        python3 -m venv venv
    fi
fi

if [ -d venv/bin ]; then
    source venv/bin/activate
else
    source venv/Scripts/activate
fi

pip install -r requirements.txt
cd -