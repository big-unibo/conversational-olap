# Conversational BI

![Build pass](https://travis-ci.org/w4bo/CBI.svg?branch=master)

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

Plotting the charts

    sudo apt-get install python3-venv (in linux)
    cd COOL/src/main/python 
    python3 -m venv venv
    source venv/bin/activate (in linux; in windows source venv/Scripts/activate)
    pip install -r requirements.txt
    git pull; python3 plot.py --dataset test_dataset_patrick_ssb.csv

Enabling the web application

1. Clone the repository
2. Create a Python's `venv in COOL/src/main/python/`
3. Set `chmod 777 venv/` to make the directory accessible to anyone
    - In Windows: Properties -> Security -> Advanced -> Add -> Add `Everyone` user with `Read & Execute privileges`
4.  Remove the previous `.war` files, deploy the new version
    ```
    rm '/c/Program Files/Apache Software Foundation/Tomcat 9.0_Tomcat9-8083/webapps/COOL.war'
    rm -r '/c/Program Files/Apache Software Foundation/Tomcat 9.0_Tomcat9-8083/webapps/COOL'
    ./gradlew war
    cp build/libs/COOL.war '/c/Program Files/Apache Software Foundation/Tomcat 9.0_Tomcat9-8083/webapps/'
    ```

## Working on this project

- This project is organized according to the [gitflow](http://nvie.com/posts/a-successful-git-branching-model/) model
    - Before opening a pull request, be sure to integrate `upstream`
    - [Configuring a remote for a fork](https://help.github.com/articles/configuring-a-remote-for-a-fork/)
    - [Syncing a fork](https://help.github.com/articles/syncing-a-fork/)
- Make separate commits for logically separate changes [(see this guideline)](https://git.kernel.org/pub/scm/git/git.git/tree/Documentation/SubmittingPatches?id=HEAD)
     - Commit's body uses the imperative, present tense: "change", not "changed" or "changes
- Configure Git as follows
     - Windows `git config --global core.autocrlf true`
     - Linux `git config --global core.autocrlf input`
- Use [UTF-8](https://en.wikipedia.org/wiki/UTF-8) encoding
     - IntelliJ/PyCharm: Settings -> Editor -> File encoding -> Select `Project encoding: UTF-8`
     - Eclipse: Window -> Preferences -> General -> Workspace -> Text file encoding -> Select `UTF-8`
- Use Unix [EOL](https://en.wikipedia.org/wiki/Newline)
     - IntelliJ/PyCharm: Settings -> Editor -> Code Style -> Select `Scheme: Project` -> Select `Line separator: Unix (\n)`
     - Eclipse: Window -> Preferences -> General -> Workspace -> New text file line delimiter -> Select `Unix`
- Use 4 spaces instead of `tab (\t)`
    - IntelliJ/PyCharm: File -> Settings -> Editor -> Code Style -> Java -> Tabs and Indents -> Untoggle `Use tab character`
        - Be sure of using spaces by enabling visualization of hidden characters 
        - Settings -> Editor -> General -> Appearance -> Toggle `Show whitespaces`
     - Eclipse: Window -> Preferences -> General -> Editors -> Text Editors -> Toggle `Insert spaces for tabs`
        - Be sure of using spaces by enabling visualization of hidden characters 
        - Window -> Preferences -> General -> Editors -> Text Editors -> Toggle `Show whitespaces characters`

In Eclipse also:
- Checkstyle plugin
    - From Eclipse Markeplace
    - Enable it in Window -> Preferences -> Checkstyle -> Select `config/checkstyle/checkstyle.xml` configuration
- Import formatter from `config\eclipse-formatter.xml`
     - Window -> Preferences -> Java -> Code Style -> Formatter
- Import cleanup from `config\eclipse-cleanup.xml`
     - Window -> Preferences -> Java -> Code Style -> Clean up
- To get Eclipse to not flag the `@SuppressWarnings("checkstyle:magicnumber")` annotation
     - Window -> Preferences -> Java -> Compiler -> Errors/Warnings -> Annotations -> Unhandled Token in `@SuppressWarnings` and set it to ignore.

## COOL - Conversational OLap

### Running this project

- `DBreader.java`: to create the meta-database (e.g., `conversational`) from a star-schema cube (e.g., `foodmart`)
- `Validator.java`: to run the validation on Patrick's dataset (`conversational.dataset_patrick` table)

NB: make to have the proper settings in `src/main/resources/credentials.txt`

### Creazione db

Connessione

- IP-port: 137.204.74.2:3307
- User: ...
- Data DB: foodmart
- Meta DB: conversational

Per creare il db:

0. Connettersi a MySQL 
    0. Eseguire script `src/main/sql/create_db.sql`
    0. Per aggiungere sinonimi "statici" a misure e operatori fare riferimento a `language_predicate`
    0. Occorre popolare a mano le tabelle `groupbyoperator_of_measure` (nel db non so quali operatori posso applicare alle misure)
    0. Occorre popolare a mano le tabelle `level_rollup` (nel db non sono presenti le dipendenze funzionali)
        0. I primi livelli devono essere popolati a mano, la costruzione dei transitivi si può fare in `DBLoader`
0. DBReader legge le tuple del db
0. DBLoader carica i metadati/dati (i.e., i membri) nel meta-schema
    0. In DBLoader c'è il metodo per caricare i sinonimi di ogni tupla (delle tabelle che hanno i sinonimi)

### API Protocollo di interazione con il web server

NB: le ambiguità Unparsed Clause **devono** essere gestite per ultime!

**L'unica modalità** per segnalare errori è quella di riportare la sequenza delle operazioni fatte in modo che sia possibile riprodurle nei test.

#### Input

- sessionid
- ambiguityid
- value
- describe

(* = valore ignorato)

#### Describe di un attributo

- describe = "attributo"

#### Invio di una nuova query:

- sessionid = ””           (sessionid vuoto / sessionid inviato non esiste)
- ambiguityid = *          (* = valore ignorato)
- value = ”nlp query”

#####  Esempio 01 - No ambiguità

Ogni volta che la query **non** contiene ambiguità, il server restituisce anche l'SQL generato e il risultato della query.

    http://127.0.0.1:8080/COOL/Conversational?value=sum%20unit%20sales%20for%20country%20usa

#####  Esempio 02 - Con ambiguità

    http://127.0.0.1:8080/COOL/Conversational?value=sum%20unit%20sales%20for%20usa

#### Invio disambiguazione

Quando la sessione è in stato "ENGAGE"

- sessionid = ”sessionid”
- ambiguityid = ”ambiguityid”
- value = ”valore” (valore scelto / drop)

##### Esempio 03 - Continua Esempio 02 (risultato uguale a Esempio 01)

    http://127.0.0.1:8080/COOL/Conversational?sessionid=s0&ambiguityid=i1&value=country

#### Invio di un operatore OLAP

Quando la sessione è in stato "NAVIGATE"

- sessionid = ”sessionid”
- ambiguityid = *
- value = ”valore”

#####  Esempio 04 - No ambiguità

    http://127.0.0.1:8080/COOL/Conversational?sessionid=s0&value=add product subcategory

#####  Esempio 05 - Con ambiguità

    http://127.0.0.1:8080/COOL/Conversational?sessionid=s0&value=add store sales

#### Disambiguazione di un operatore OLAP

Quando Sessione è in stato "NAVIGATE"

- sessionid = ”sessionid”
- ambiguityid = ”ambiguityid”
- value = ”valore”

##### Esempio 06

    http://127.0.0.1:8080/COOL/Conversational?sessionid=s0&ambiguityid=i0&value=min

#### Invio abort/reset

- sessionid = ”sessionid” 
- ambiguityid = *
- value = ”reset”

### Servlet configuration

- Install `Eclipse Java EE Developer Tools Eclipse Project`
- Project properties -> Project Facets -> Convert to faceted form -> Check `Dynamic Web Module`
- Right click on the project `Run as` -> `Run on Server`
    - Check `Manually define a new server`
    - Select `Apache` -> `Tomcat v7.0` -> Next
    - Install Apache Tomcat and the JRE 1.8 -> Finish
- Deployment Assembly -> Add -> Java Build Path Entries -> Project Dependencies
- Tomcat 7 is ok for deploying in Eclipse, but it is better to deploy on Tomcat 9

### Export `COOL.war`

NB: the name of the `.war` file must match the name of the project (and it is case sensitive)

Tell Eclipse to export also the libraries (required only the first time)
- Right Click on the project -> properties. On properties window
- Select "Deployment Assembly" on the left hand side
- Click "add"
- Select "Java Build Path Entries".
- Select all the required libraries

Right Click on the project -> Export -> `.war`

## Describe

Deploying the application on Tomcat 9:

- Make sure to add the path to the Python virtual environment in `credentials.txt`
- Export `COOL.war` and deploy it to Tomcat 9 `webapps` folder
- Make sure to modify the access rights of the project source folder (so that Tomcat can access the Python virtual environment)
    - Linux `chmod 777 conversational-experiments`
    - Windows `cacls .\conversational-experiments /t /g everyone:f`
- In Windows it is also necessary to set the environment variable `PYTHONHOME` that refers to the generic python installation. E.g., `PYTHONHOME=C:\Python3.6\`

### Input

- sessionid (to handle a sequence of `describe` intents)
- value     (the describe string)

#### Issuing a Describe request

    http://137.204.72.88:8083/COOL/Describe?sessionid=1&value=with SALES describe storeCost by month
