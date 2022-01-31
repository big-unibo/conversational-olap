cp .env.example .env
cp src/main/resources/config.example.yml src/main/resources/config.yml
P=$(pwd)
sed -i "s+\!HOME\!+${P}+g" src/main/resources/config.yml

cd src/main/python
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
cd -