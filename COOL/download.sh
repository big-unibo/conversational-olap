#!/bin/bash
set -exo
cd resources
curl -oLC foodmart-mysql.sql https://big.csr.unibo.it/projects/nosql-datasets/foodmart-mysql.sql
curl -oLC covid_weekly-mysql.sql https://big.csr.unibo.it/projects/nosql-datasets/covid_weekly-mysql.sql
curl -oLC ssb_test-mysql.sql https://big.csr.unibo.it/projects/nosql-datasets/ssb_test-mysql.sql
cd ..
