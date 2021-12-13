-- CREATE USER ssb_test IDENTIFIED BY thisis_ssb_test;
-- GRANT ALL PRIVILEGES TO ssb_test;

--------------------------------------------------------
--  DDL for Table LINEORDER
--------------------------------------------------------
DROP TABLE SSB_TEST_LINEORDER;
CREATE TABLE SSB_TEST_LINEORDER (
                                    CUSTKEY NUMBER,
                                    PARTKEY NUMBER,
                                    QUANTITY FLOAT,
                                    EXTENDEDPRICE FLOAT,
                                    ORDTOTALPRICE FLOAT
);
--------------------------------------------------------
--  DDL for Table PART
--------------------------------------------------------
DROP TABLE SSB_TEST_PART;
CREATE TABLE SSB_TEST_PART (
                               PARTKEY NUMBER,
                               PRODUCT VARCHAR2(22 BYTE),
                               CATEGORY VARCHAR2(22 BYTE)
);
--------------------------------------------------------
--  DDL for Table CUSTOMER
--------------------------------------------------------
DROP TABLE SSB_TEST_CUSTOMER;
CREATE TABLE SSB_TEST_CUSTOMER (
                                   CUSTKEY NUMBER,
                                   CUSTOMER VARCHAR2(25 BYTE),
                                   CITY VARCHAR2(10 BYTE)
);

insert into SSB_TEST_CUSTOMER values (01, 'c01', 'Italy');
insert into SSB_TEST_CUSTOMER values (02, 'c02', 'Italy');
insert into SSB_TEST_CUSTOMER values (03, 'c03', 'Italy');
insert into SSB_TEST_CUSTOMER values (04, 'c04', 'Italy');
insert into SSB_TEST_CUSTOMER values (05, 'c05', 'Italy');
insert into SSB_TEST_CUSTOMER values (06, 'c06', 'France');
insert into SSB_TEST_CUSTOMER values (07, 'c07', 'France');
insert into SSB_TEST_CUSTOMER values (08, 'c08', 'France');
insert into SSB_TEST_CUSTOMER values (09, 'c09', 'France');
insert into SSB_TEST_CUSTOMER values (10, 'c10', 'France');
insert into SSB_TEST_PART values (01, 'Beer', 'Beverages');
insert into SSB_TEST_PART values (02, 'Wine', 'Beverages');
insert into SSB_TEST_PART values (03, 'Cola', 'Beverages');
insert into SSB_TEST_PART values (04, 'Pizza', 'Food');
insert into SSB_TEST_PART values (05, 'Bread', 'Food');
insert into SSB_TEST_PART values (06, 'p06', 'France');
insert into SSB_TEST_PART values (07, 'p07', 'France');
insert into SSB_TEST_PART values (08, 'p08', 'France');
insert into SSB_TEST_PART values (09, 'p09', 'France');
insert into SSB_TEST_PART values (10, 'p10', 'France');
insert into SSB_TEST_LINEORDER values (01, 01, 20, 10, 100);
insert into SSB_TEST_LINEORDER values (02, 01, 15, 10, 100);
insert into SSB_TEST_LINEORDER values (02, 02, 17, 10, 100);
insert into SSB_TEST_LINEORDER values (03, 02, 15, 10, 100);
insert into SSB_TEST_LINEORDER values (03, 03, 15, 10, 100);
insert into SSB_TEST_LINEORDER values (04, 03, 15, 10, 100);
insert into SSB_TEST_LINEORDER values (04, 04, 06, 10, 100);
insert into SSB_TEST_LINEORDER values (05, 05, 05, 10, 100);
commit;

DROP TABLE database CASCADE CONSTRAINTS;
CREATE TABLE database (
                          database_id varchar2(255) NOT NULL,
                          database_name varchar2(255) NOT NULL,
                          IPaddress varchar2(16) NOT NULL,
                          port NUMBER NOT NULL,
                          PRIMARY KEY (database_id),
                          UNIQUE(database_name, IPaddress, port)
);

DROP TABLE groupbyoperator CASCADE CONSTRAINTS;
CREATE TABLE groupbyoperator (
                                 groupbyoperator_id varchar2(255) NOT NULL,
                                 groupbyoperator_name varchar2(255) NOT NULL UNIQUE,
                                 groupbyoperator_synonyms varchar2(1000),
                                 PRIMARY KEY (groupbyoperator_id)
);

DROP TABLE hierarchy CASCADE CONSTRAINTS;
CREATE TABLE hierarchy (
                           hierarchy_id varchar2(255) NOT NULL,
                           hierarchy_name varchar2(255) NOT NULL UNIQUE,
                           hierarchy_synonyms varchar2(1000),
                           PRIMARY KEY (hierarchy_id)
);

DROP TABLE fact CASCADE CONSTRAINTS;
CREATE TABLE fact (
                      fact_id varchar2(255) NOT NULL,
                      fact_name varchar2(255) NOT NULL UNIQUE,
                      fact_synonyms varchar2(1000),
                      database_id varchar2(255) NULL REFERENCES database (database_id) ON DELETE CASCADE,
                      PRIMARY KEY (fact_id)
);

DROP TABLE "TABLE" CASCADE CONSTRAINTS;
CREATE TABLE "TABLE" (
                         table_id varchar2(255) NOT NULL,
                         table_name varchar2(255) NOT NULL UNIQUE,
                         table_type varchar2(255) NOT NULL,
                         fact_id varchar2(255) DEFAULT NULL REFERENCES fact (fact_id),
                         hierarchy_id varchar2(255) DEFAULT NULL REFERENCES hierarchy (hierarchy_id) ON DELETE CASCADE,
                         PRIMARY KEY (table_id)
);

DROP TABLE relationship CASCADE CONSTRAINTS;
CREATE TABLE relationship (
                              relationship_id varchar2(255) NOT NULL,
                              table1 varchar2(255) NOT NULL REFERENCES "TABLE" (table_id) ON DELETE CASCADE,
                              table2 varchar2(255) NOT NULL REFERENCES "TABLE" (table_id) ON DELETE CASCADE,
                              PRIMARY KEY (relationship_id)
);

DROP TABLE "COLUMN" CASCADE CONSTRAINTS;
CREATE TABLE "COLUMN" (
                          column_id varchar2(255) NOT NULL,
                          column_name varchar2(255) NOT NULL,
                          column_type varchar2(255) NOT NULL,
                          isKey number(1)  NOT NULL,
                          relationship_id varchar2(255) DEFAULT NULL,
                          table_id varchar2(255) NOT NULL REFERENCES "TABLE"(table_id) ON DELETE CASCADE,
                          PRIMARY KEY (column_id) -- , UNIQUE (column_name, table_id)
);

DROP TABLE "LEVEL" CASCADE CONSTRAINTS;
CREATE TABLE "LEVEL" (
                         level_id varchar2(255) NOT NULL,
                         level_type varchar2(255) NOT NULL,
                         level_description varchar2(200),
                         level_name varchar2(255) NOT NULL UNIQUE,
                         cardinality NUMBER DEFAULT NULL,
                         hierarchy_id varchar2(255) NOT NULL REFERENCES "HIERARCHY" (hierarchy_id) ON DELETE CASCADE,
                         level_synonyms varchar2(1000),
                         column_id varchar2(255) NOT NULL REFERENCES "COLUMN"(column_id),
                         "MIN" DOUBLE PRECISION DEFAULT NULL,
                         "MAX" DOUBLE PRECISION DEFAULT NULL,
                         "AVG" DOUBLE PRECISION DEFAULT NULL,
                         isDescriptive NUMBER(1) DEFAULT 0,
                         mindate DATE DEFAULT NULL,
                         maxdate DATE DEFAULT NULL,
                         PRIMARY KEY (level_id)
);

DROP TABLE hierarchy_in_fact CASCADE CONSTRAINTS;
CREATE TABLE hierarchy_in_fact (
                                   fact_id varchar2(255) NOT NULL REFERENCES fact (fact_id),
                                   hierarchy_id varchar2(255) NOT NULL REFERENCES hierarchy (hierarchy_id) ON DELETE CASCADE,
                                   PRIMARY KEY (fact_id, hierarchy_id)
);

DROP TABLE language_predicate CASCADE CONSTRAINTS;
CREATE TABLE language_predicate (
                                    language_predicate_id varchar2(255) NOT NULL,
                                    language_predicate_name varchar2(255) NOT NULL UNIQUE,
                                    language_predicate_synonyms varchar2(1000) DEFAULT NULL,
                                    language_predicate_type varchar2(255) DEFAULT NULL,
                                    PRIMARY KEY (language_predicate_id)
);

DROP TABLE language_operator CASCADE CONSTRAINTS;
CREATE TABLE language_operator (
                                   language_operator_id varchar2(255) NOT NULL,
                                   language_operator_name varchar2(255) NOT NULL UNIQUE,
                                   language_operator_synonyms varchar2(1000) DEFAULT NULL,
                                   language_operator_type varchar2(255) DEFAULT NULL,
                                   PRIMARY KEY (language_operator_id)
);

DROP TABLE measure CASCADE CONSTRAINTS;
CREATE TABLE measure (
                         measure_id varchar2(255) NOT NULL,
                         measure_name varchar2(255) NOT NULL,
                         fact_id varchar2(255) NOT NULL REFERENCES fact (fact_id),
                         measure_synonyms varchar2(1000),
                         column_id varchar2(255) NOT NULL REFERENCES "COLUMN" (column_id) ON DELETE CASCADE,
                         PRIMARY KEY (measure_id),
                         UNIQUE(measure_name, fact_id)
);

DROP TABLE member CASCADE CONSTRAINTS;
CREATE TABLE member (
                        member_id varchar2(255) NOT NULL,
                        member_name varchar2(255) NOT NULL,
                        level_id varchar2(255) NOT NULL REFERENCES "LEVEL" (level_id) ON DELETE CASCADE,
                        member_synonyms varchar2(1000),
                        PRIMARY KEY (member_id),
                        UNIQUE(member_name, level_id)
);

DROP TABLE groupbyoperator_of_measure CASCADE CONSTRAINTS;
CREATE TABLE groupbyoperator_of_measure (
                                            groupbyoperator_id varchar2(255) NOT NULL REFERENCES groupbyoperator (groupbyoperator_id) ON DELETE CASCADE,
                                            measure_id varchar2(255) NOT NULL REFERENCES measure (measure_id) ON DELETE CASCADE,
                                            PRIMARY KEY (groupbyoperator_id, measure_id)
);

DROP TABLE "SYNONYM" CASCADE CONSTRAINTS;
CREATE TABLE "SYNONYM" (
                           synonym_id varchar2(255) NOT NULL,
                           table_name varchar2(255) NOT NULL,
                           reference_id varchar2(255) NOT NULL, -- id of the Entity in the given table
                           "TERM" varchar2(255) NOT NULL,
                           PRIMARY KEY (synonym_id),
                           UNIQUE(term, reference_id, table_name)
);

DROP TABLE OLAPSESSION CASCADE CONSTRAINTS;
CREATE TABLE OLAPSESSION (
                             "TIMESTAMP" NUMBER,
                             session_id varchar2(255),
                             annotation_id varchar2(255),
                             value_en varchar2(1000),
                             value_ita varchar2(1000),
                             limit long,
                             fullquery_serialized blob,
                             fullquery_tree varchar2(1000),
                             olapoperator_serialized blob
);

commit;

-- RENAME SSB_TEST_LINEORDER TO lineorder6;
-- create table SSB_TEST_LINEORDER as select * from lineorder6 where rownum <= 30000000;

-- After populating the database
ALTER TABLE DATE15 add MONTH varchar(7);
ALTER TABLE DATE15 add "DATE" varchar(10);
update DATE15 set month = SUBSTR(yearmonthnum, 1, 4) || '-' || SUBSTR(yearmonthnum, 5, 2);
update DATE15 set "DATE" = SUBSTR(datekey, 1, 4) || '-' || SUBSTR(datekey, 5, 2) || '-' || SUBSTR(datekey, 7, 2);

alter table SSB_TEST_CUSTOMER add population number;
update SSB_TEST_CUSTOMER set population = 37000000 where nation = 'MOROCCO';
update SSB_TEST_CUSTOMER set population = 1000000 where nation = 'JORDAN';
update SSB_TEST_CUSTOMER set population = 102000000 where nation = 'EGYPT';
update SSB_TEST_CUSTOMER set population = 45000000 where nation = 'ARGENTINA';
update SSB_TEST_CUSTOMER set population = 37742154 where nation = 'CANADA';
update SSB_TEST_CUSTOMER set population = 34813871 where nation = 'SAUDI ARABIA';
update SSB_TEST_CUSTOMER set population = 1439323776 where nation = 'CHINA';
update SSB_TEST_CUSTOMER set population = 32971854 where nation = 'PERU';
update SSB_TEST_CUSTOMER set population = 1380004385 where nation = 'INDIA';
update SSB_TEST_CUSTOMER set population = 67886011 where nation = 'UNITED KINGDOM';
update SSB_TEST_CUSTOMER set population = 83992949 where nation = 'IRAN';
update SSB_TEST_CUSTOMER set population = 212559417 where nation = 'BRAZIL';
update SSB_TEST_CUSTOMER set population = 65273511 where nation = 'FRANCE';
update SSB_TEST_CUSTOMER set population = 146000000 where nation = 'RUSSIA';
update SSB_TEST_CUSTOMER set population = 126000000 where nation = 'JAPAN';
update SSB_TEST_CUSTOMER set population = 44000000 where nation = 'ALGERIA';
update SSB_TEST_CUSTOMER set population = 97000000 where nation = 'VIETNAM';
update SSB_TEST_CUSTOMER set population = 19237691 where nation = 'ROMANIA';
update SSB_TEST_CUSTOMER set population = 3100000 where nation = 'MOZAMBIQUE';
update SSB_TEST_CUSTOMER set population = 274000000 where nation = 'INDONESIA';
update SSB_TEST_CUSTOMER set population = 40000000 where nation = 'IRAQ';
update SSB_TEST_CUSTOMER set population = 84000000 where nation = 'GERMANY';
update SSB_TEST_CUSTOMER set population = 54000000 where nation = 'KENYA';
update SSB_TEST_CUSTOMER set population = 33100000 where nation = 'UNITED STATES';
update SSB_TEST_CUSTOMER set population = 109000000 where nation = 'ETHIOPIA';
commit;

ALTER TABLE SSB_TEST_PART ADD PRIMARY KEY (partkey);
ALTER TABLE SSB_TEST_CUSTOMER ADD PRIMARY KEY (custkey);
ALTER TABLE SSB_TEST_LINEORDER ADD PRIMARY KEY (custkey, partkey);
ALTER TABLE SSB_TEST_LINEORDER ADD CONSTRAINT part_FK Foreign Key (partkey) REFERENCES SSB_TEST_PART (partkey);
ALTER TABLE SSB_TEST_LINEORDER ADD CONSTRAINT customer_FK Foreign Key (custkey) REFERENCES SSB_TEST_CUSTOMER (custkey);
CREATE BITMAP INDEX SSB_TEST_LINEORDER_partkey ON SSB_TEST_LINEORDER(partkey);
CREATE BITMAP INDEX SSB_TEST_LINEORDER_custkey ON SSB_TEST_LINEORDER(custkey);
commit;


select * from ssb_test_lineorder;