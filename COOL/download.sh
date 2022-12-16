#!/bin/bash
set -exo
cd resources
curl -o foodmart-mysql.sql http://big.csr.unibo.it/projects/nosql-datasets/foodmart-mysql.sql
curl -o covid_weekly-mysql.sql http://big.csr.unibo.it/projects/nosql-datasets/covid_weekly-mysql.sql
curl -o ssb_test-mysql.sql http://big.csr.unibo.it/projects/nosql-datasets/ssb_test-mysql.sql
cd ..
