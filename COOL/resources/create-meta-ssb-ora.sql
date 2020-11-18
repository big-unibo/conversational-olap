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

-- to retrieve the members fast
CREATE MATERIALIZED VIEW ssb_members
     BUILD IMMEDIATE
     REFRESH COMPLETE
     ENABLE QUERY REWRITE
     AS select m.MEMBER_ID, m.MEMBER_NAME, l.LEVEL_ID, l.LEVEL_NAME, l.LEVEL_TYPE, t.TABLE_ID, t.TABLE_NAME, c.COLUMN_NAME
        from "LEVEL" l JOIN "COLUMN" c ON(l.COLUMN_ID = c.COLUMN_ID) JOIN "TABLE" t ON(c.TABLE_ID = t.TABLE_ID) LEFT JOIN "MEMBER" m on (l.LEVEL_ID = m.LEVEL_ID);

DROP TABLE dataset_patrick_ssb CASCADE CONSTRAINTS;
CREATE TABLE dataset_patrick_ssb (
  id number NOT NULL,
  origin varchar2(255) DEFAULT NULL,
  gpsj varchar2(1) DEFAULT NULL,
  query varchar2(255) DEFAULT NULL,
  mc varchar2(255) DEFAULT NULL,
  gc varchar2(255) DEFAULT NULL,
  sc varchar2(255) DEFAULT NULL,
  missing varchar2(255) DEFAULT NULL,
  ambiguity varchar2(255) DEFAULT NULL,
  notes varchar2(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO groupbyoperator VALUES (1, 'sum', '[total, number, amount, how much]');
INSERT INTO groupbyoperator VALUES (2, 'avg', '[average, medium, mean]');
INSERT INTO groupbyoperator VALUES (3, 'max', '[maximum, highest, top]');
INSERT INTO groupbyoperator VALUES (4, 'min', '[minimum, lowest, bottom]');
INSERT INTO groupbyoperator VALUES (5, 'stdev', '[deviation, standard deviation]');


INSERT INTO language_predicate VALUES ( 1,'where','[filter, filter on, for, in, on]','where');
-- INSERT INTO language_predicate VALUES ( 3,'select','[show, return, tell, find, get]','SELECT');
INSERT INTO language_predicate VALUES ( 4,'by','[group by, grouped by, grouping by, for, for each, for every, per]','by');
INSERT INTO language_predicate VALUES ( 5,'>=','[greater equal, greater equal than]','cop');
INSERT INTO language_predicate VALUES ( 6,'<=','[lower equal, lower equal than]','cop');
INSERT INTO language_predicate VALUES ( 7,'=','[equal, is, as]','cop');
INSERT INTO language_predicate VALUES ( 8,'<','[lower than, below, less than, before, until]','cop');
INSERT INTO language_predicate VALUES ( 9,'>','[greater than, above, more than, after, from]','cop');
INSERT INTO language_predicate VALUES (10,'and',NULL,'and');
INSERT INTO language_predicate VALUES (11,'or',NULL,'or');
INSERT INTO language_predicate VALUES ( 2,'not',NULL,'not');
-- INSERT INTO language_predicate VALUES (12,'between',NULL,'between');
INSERT INTO language_predicate VALUES (13,'count','[number, amount, how many, how many times]','count');
-- INSERT INTO language_predicate VALUES (14,'distinct',NULL,'COUNTOPERATOR');


INSERT INTO language_operator VALUES (15,'drill','[drill down,specialize]','drill');
INSERT INTO language_operator VALUES (16,'rollup','[roll up,generalize]','rollup');
INSERT INTO language_operator VALUES (17,'replace','[substitute]','replace');
INSERT INTO language_operator VALUES (18,'add',NULL,'add');
INSERT INTO language_operator VALUES (19,'drop','[remove]','drop');
INSERT INTO language_operator VALUES (20,'slice','[filter, filter on, slice and dice, dice]','sad');
INSERT INTO language_operator VALUES (21,'to','[up to, down to]','accessory');
INSERT INTO language_operator VALUES (22,'with',NULL,'accessory');
commit;

-- --------------------------------------------------------------------------------------------------------------------------------
-- TO EXECUTE AFTER DBreader.java
-- --------------------------------------------------------------------------------------------------------------------------------
-- Add rollup dependencies: TO BE DONE BY HAND
-- INSERT INTO level_rollup VALUES
--   (product, type),
--   (type, category);

-- Add operators appliable to each measure: TO BE DONE BY HAND
-- INSERT INTO groupbyoperator_of_measure VALUES
--   (OP_ID, MEA_ID),             OP_ID from groupbyoperator table, MEA_ID from measure table
--   (OP_ID, MEA_ID);
-- ... Or populate it with all the operators
-- select * from "MEASURE";q
-- select * from groupbyoperator;
-- select * from "TABLE";
-- select * from "LEVEL";
INSERT INTO groupbyoperator_of_measure select groupbyoperator_id, measure_id from measure, groupbyoperator;
INSERT INTO "SYNONYM"(synonym_id, table_name, reference_id, term) VALUES ('-2', 'FACT', (select fact_id from fact where fact_name = 'LINEORDER2'), 'sales');
INSERT INTO "SYNONYM"(synonym_id, table_name, reference_id, term) VALUES ('-3', 'MEASURE', (select measure_id from "MEASURE" where lower(measure_name) = lower('supplycost')), 'supply cost');
INSERT INTO "SYNONYM"(synonym_id, table_name, reference_id, term) VALUES ('-4', 'MEASURE', (select measure_id from "MEASURE" where lower(measure_name) = lower('extendedprice')), 'extended price');
DELETE FROM "SYNONYM" where term = 'a a';
DELETE FROM "MEMBER" where member_name = 'a a';
INSERT INTO "SYNONYM"(synonym_id, table_name, reference_id, term) VALUES ('-6', 'LANGUAGE_OPERATOR', (select language_operator_id from "LANGUAGE_OPERATOR" where lower(language_operator_name) = lower('=')), 'as');
commit;