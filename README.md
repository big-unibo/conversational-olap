# Conversational BI

[![build](https://github.com/big-unibo/Conversational-BI/actions/workflows/build.yml/badge.svg)](https://github.com/big-unibo/Conversational-BI/actions/workflows/build.yml)

# Running this project

Installing Java 14 and checking if the tests pass

    curl -sL https://github.com/shyiko/jabba/raw/master/install.sh | bash && . ~/.jabba/jabba.sh
    jabba install openjdk@1.14.0
    jabba use openjdk@1.14.0
    rm -rf COOL
    git clone git@github.com:w4bo/COOL.git
    cd COOL/COOL
    ./gradlew --stacktrace

Running the validator with the sequential scan of synonyms

    git pull; ./gradlew runValidatorSequential


Running the validator with the BKtree scan of synonyms

    git pull; ./gradlew runValidator

Enabling the web application

1. Clone the repository
2. Create a Python's `venv in COOL/src/main/python/`
3. Set `chmod 777 venv/` to make the directory accessible to anyone
    - In Windows: Properties -> Security -> Advanced -> Add -> Add `Everyone` user with `Read & Execute privileges`
4.  Remove the previous `.war` files, deploy the new version
    ```
    git pull
    rm '/path/to/tomcat9/webapps/COOL.war'
    rm -r '/path/to/tomcat9/webapps/COOL'
    ./gradlew war
    cp build/libs/COOL.war '/path/to/tomcat9/webapps/'
    ```
