# Conversational OLAP

[![build](https://github.com/big-unibo/Conversational-BI/actions/workflows/build.yml/badge.svg)](https://github.com/big-unibo/Conversational-BI/actions/workflows/build.yml)

## Research papers

- Matteo Francia, Enrico Gallinucci, Matteo Golfarelli, Stefano Rizzi: VOOL: A modular insight-based framework for vocalizing OLAP sessions. **Information Systems** 129: 102496 (2025). DOI: https://doi.org/10.1016/j.is.2024.102496
- Matteo Francia, Enrico Gallinucci, Matteo Golfarelli: COOL: A framework for conversational OLAP. **Information Systems** 104: 101752 (2022). DOI: https://doi.org/10.1016/j.is.2021.101752
- Matteo Francia, Enrico Gallinucci, Matteo Golfarelli: Conversational OLAP in Action. **Proc. of EDBT** 2021: 646-649. DOI: https://doi.org/10.5441/002/edbt.2021.74

## Running this project

Installing Java 14 and checking if the tests pass

    curl -sL https://github.com/shyiko/jabba/raw/master/install.sh | bash && . ~/.jabba/jabba.sh
    jabba install openjdk@1.14.0
    jabba use openjdk@1.14.0
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
