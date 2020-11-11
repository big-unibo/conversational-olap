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
        
DROP TABLE dataset_patrick CASCADE CONSTRAINTS;
CREATE TABLE dataset_patrick (
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
INSERT INTO dataset_patrick VALUES (1,'Sales Revenue by media for Spain as Country','y','sum unit sales by media type for USA as country','sum unit_sales','media_type','country = USA','','','');
INSERT INTO dataset_patrick VALUES (2,'Sales Target Revenue by Gender','y','store sales by gender','avg store_sales','gender','','','AM','');
INSERT INTO dataset_patrick VALUES (3,'Sales Revenue by media f spain','y','sum unit sales by media type for Sheri Nowmer','sum unit_sales','media_type','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (4,'sales revenue by country by month by region for decathlon','y','sum unit_sales by country by month by province for Sheri Nowmer','sum unit_sales','country, the_month, state_province','fullname = Sheri Nowmer','','AM','');
INSERT INTO dataset_patrick VALUES (5,'sales revenue by month 2010 decathlon north america by retailer','y','store sales by month in 2010 for Atomic Mints USA by store','avg store_sales','the_month, store_id','the_year = 2010 and product_name = INSERT INTO dataset_patrick VALUES Atomic Mints','SCA ::= SCN SCA | SCN and SCA | SCN ','','');
INSERT INTO dataset_patrick VALUES (6,'Sales Revenue by Month for 2010 as Year','y','sum unit sales by month for 2010 as year','sum unit_sales','the_month','the_year = 2010','','','');
INSERT INTO dataset_patrick VALUES (7,'Sales Revenue','y','sum unit sales','sum unit_sales','','','','','');
INSERT INTO dataset_patrick VALUES (8,'Lower middle income as Country','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (9,'Lower middle income','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (10,'Sales Target Revenue by Country','y','store sales by country','avg store_sales','country','','','AM','store_sales is used with ambiguous operator');
INSERT INTO dataset_patrick VALUES (11,'Sales Revenue by Country eur','y','store sales by fullname Sheri Nowmer','avg store_sales','','fullname = Sheri Nowmer','','AM','');
INSERT INTO dataset_patrick VALUES (12,'bottom inventory by country 2015','?','','','','','bottom/top aggregator','','');
INSERT INTO dataset_patrick VALUES (13,'Sales Revenue by Genre for spain as Country','y','store sales by gender for USA as country','avg store_sales','gender','country = USA','','AM','');
INSERT INTO dataset_patrick VALUES (14,'sales revenue by genre for spain','y','store sales by gender for Sheri Nowmer','avg store_sales','gender','fullname = Sheri Nowmer','','AM','');
INSERT INTO dataset_patrick VALUES (15,'Nb Destinations by Main Airport','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (16,'Sales Revenue by media genre for Iceland as Country','y','sum unit sales by media type for USA as country','sum unit_sales','media_type','country = USA','','','');
INSERT INTO dataset_patrick VALUES (17,'Sales Revenue by "media format"""','y','sum unit sales by media type','sum unit_sales','media_type','','','','');
INSERT INTO dataset_patrick VALUES (18,'Sales Revenue by media format','y','sum unit sales for Club Chocolate Milk','sum unit_sales','','product_name = Club Chocolate Milk ','','','');
INSERT INTO dataset_patrick VALUES (19,'GDP by Country for "Lower middle income"" as Country"','y','sum store cost by country for USA as country','sum store_cost','country','country = USA','','','');
INSERT INTO dataset_patrick VALUES (20,'sales target by "media format"""','y','store sales by Club Chocolate Milk','avg store_sales','','product_name = Club Chocolate Milk ','','AM','');
INSERT INTO dataset_patrick VALUES (21,'GDP for "Lower middle income"" as Country"','y','sum store cost for Canada as country','sum store_cost','','country = Canada','','','');
INSERT INTO dataset_patrick VALUES (22,'Sales Target by year "media format"""','y','store sales by year media type','avg store_sales','the_year, media_type','','','AM','');
INSERT INTO dataset_patrick VALUES (23,'Sales Target by Year','y','store sales by year','avg store_sales','the_year','','','AM','');
INSERT INTO dataset_patrick VALUES (24,'Sales Revenue by Genre','y','sum unit sales by gender','sum unit_sales','gender','','','','');
INSERT INTO dataset_patrick VALUES (25,'supply','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (26,'by region','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (27,'GDP by Country for "Lower middle income"" as ""Income Group"""','y','store sales for Club Chocolate Milk as product name','avg store_sales','','product_name = Club Chocolate Milk ','','AM','');
INSERT INTO dataset_patrick VALUES (28,'Region','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (29,'GDP by Country for "Upper middle income"" as Country"','y','sum unit sales by country for Mexico as country','sum unit_sales','country','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (30,'GDP for "Upper middle income"" as ""Income Group"""','y','sum unit sales for Mexico as country','sum unit_sales','','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (31,'Income Group values','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (32,'Sales Quantity / "Sales Target Qty"" by Country for 2015 as Year"','y','sum unit sales by country for 2015 as year','sum unit_sales','country','the_year = 2015','','','');
INSERT INTO dataset_patrick VALUES (33,'Shortage / sales quantity by Country for 2015 as Year','y','units by country for 2015 as year','sum unit_sales','country','the_year = 2015','','','');
INSERT INTO dataset_patrick VALUES (34,'Shortage / "Sales Quantity"" by Country for 2015 as Year"','-','','','','','','','');
INSERT INTO dataset_patrick VALUES (35,'Shortage / "Sales Quantity"" by Country"','y','sum unit sales by country','sum unit_sales','country','','','','');
INSERT INTO dataset_patrick VALUES (36,'Decathlon as Retailer','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (37,'Sales Quantity / "Sales Target Qty"" by Country for quantity = 0 2015 as Year"','?','','','','','SC on measure','','');
INSERT INTO dataset_patrick VALUES (38,'Sales Quantity / "Sales Target Qty"" by Country for Quantity = 0"','?','','','','','SC on measure','','');
INSERT INTO dataset_patrick VALUES (39,'planned ordered by Country','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (40,'passengers 1990','y','count sales in 1990','count sales_fact_1997','','the_year = 1990','','','');
INSERT INTO dataset_patrick VALUES (41,'Passengers 1990 / Passengers 2010 by "Main Airport"""','y','count sales in 1990 by country','count sales_fact_1997','country','the_year = 1990','','','');
INSERT INTO dataset_patrick VALUES (42,'Sales Revenue by Country for "Europe and Central Asia (all income levels)"" as Country"','y','sum unit sales by country for Mexico as country','sum unit_sales','country','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (43,'Sales Revenue by Country','y','sum unit sales by country','sum unit_sales','country','','','','');
INSERT INTO dataset_patrick VALUES (44,'passengers 2010 Main Airport values for John F Kennedy Intl','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (45,'top 3 "Passengers 2010"" by region"','?','','','','','bottom/top aggregator','','');
INSERT INTO dataset_patrick VALUES (46,'count "Main Airport"" by country"','y','count sales by country','count sales_fact_1997','country','','','','');
INSERT INTO dataset_patrick VALUES (47,'passengers','y','count sales ','count sales_fact_1997','','','','','');
INSERT INTO dataset_patrick VALUES (48,'revenue decathlon month region 2010','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (49,'Passengers 2005/"Passengers 2010"" for ""United States"" as Country"','?','','','','','','','');
INSERT INTO dataset_patrick VALUES (50,'Sales Revenue by Country for "Europe and Central Asia"" as Region"','y','sum store costs by country for Jalisco as province','sum store_cost','country','state_province = Jalisco','','','');
INSERT INTO dataset_patrick VALUES (51,'Sales Target Revenue','y','sum store costs','sum store_cost','','','','','');
INSERT INTO dataset_patrick VALUES (52,'Passengers 2005 / nb destination','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (53,'Passengers 2005 / "Nb Destinations"""','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (54,'Retailer','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (55,'Sales Revenue by Month','y','sum store costs by month','sum store_cost','the_month','','','','');
INSERT INTO dataset_patrick VALUES (56,'gdp by country','y','sum unit sales by country','sum unit_sales','country','','','','');
INSERT INTO dataset_patrick VALUES (57,'gdp by country income group','y','sum unit sales by country product name','sum unit_sales','country, product_name','','','','');
INSERT INTO dataset_patrick VALUES (58,'GDP by year','y','sum store costs by year','sum store_cost','country','','','','');
INSERT INTO dataset_patrick VALUES (59,'GDP by Country for 2015 as Year','y','sum store costs by country for 2015 as year','sum store_cost','country','the_year = 2015','','','');
INSERT INTO dataset_patrick VALUES (60,'country','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (61,'performance by retailer for 2013','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (62,'top 1 performance by Retailer for 2013 as Year','?','','','','','bottom/top aggregator','','');
INSERT INTO dataset_patrick VALUES (63,'performance by retailer 2013','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (64,'top 3 performance by Retailer for 2013 as Year','?','','','','','bottom/top aggregator','','');
INSERT INTO dataset_patrick VALUES (65,'Sales Target','-','','','','','','','');
INSERT INTO dataset_patrick VALUES (66,'Quantity','-','','','','','','','');
INSERT INTO dataset_patrick VALUES (67,'Quantity by Country','-','','','','','','','');
INSERT INTO dataset_patrick VALUES (68,'Quantity by Country for Decathlon as Retailer','-','','','','','','','');
INSERT INTO dataset_patrick VALUES (69,'performance retailer 2013','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (70,'performance','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (71,'retailer sales revenue sales target for 2013','y','product sum store costs and sum unit sales for 2013','sum store_cost, sum unit_sales','product_id','the_year = 2013','','','');
INSERT INTO dataset_patrick VALUES (72,'bottomQuantity by Retailer','?','','','','','bottom/top aggregator','','');
INSERT INTO dataset_patrick VALUES (73,'top "Passengers 1990"""','?','','','','','bottom/top aggregator','','');
INSERT INTO dataset_patrick VALUES (74,'"Passengers 2010"" by country"','y','count sales 1997 by country','count sales_fact_1997','country','','','','');
INSERT INTO dataset_patrick VALUES (75,'Passengers 2010','y','count sales 1997','count sales_fact_1997','','','','','');
INSERT INTO dataset_patrick VALUES (76,'media format values for Spain as Country','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (77,'sales revenue by media for spain','y','sum unit sales by media type for Sheri Nowmer','sum unit_sales','media_type','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (78,'Sales Revenue by "media format"" for spain as Country"','y','sum unit sales by media type for USA as country','sum unit_sales','media_type','country = USA','','','');
INSERT INTO dataset_patrick VALUES (79,'Sales Revenue by "media format"" for spain"','y','sum unit sales by media type for Sheri Nowmer','sum unit_sales','media_type','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (80,'Sales Revenue by "media format"" for Spain as Country genre"','y','sum unit sales by media type for USA as country gender','sum unit_sales','media_type','country = USA','','','');
INSERT INTO dataset_patrick VALUES (81,'Sales Revenue by Year','y','store sales by year','avg store_sales','the_year','','','AM','');
INSERT INTO dataset_patrick VALUES (82,'Country values for "Sales Target Revenue"" < ""Sales Revenue"""','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (83,'Country values for "Sales Target Revenue"" < ""Sales Revenue"""','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (84,'country europ','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (85,'Country for "Sales Target Revenue"" < ""Sales Revenue"" for ""Europe and Central Asia"" as Region"','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (86,'Country values for Sales Target Revenue < Sales Revenue','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (87,'revenue','y','store sales','avg store_sales','','','','AM','');
INSERT INTO dataset_patrick VALUES (88,'sales revenue sales target revenue country genre','y','product sum store costs and sum unit sales for 2013','sum unit_sales, sum store_cost','country, gender','','','AM','');
INSERT INTO dataset_patrick VALUES (89,'Country values for "Sales Revenue"" < ""Sales Target Revenue"""','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (90,'Sales Target Qty','-','','','','','','','');
INSERT INTO dataset_patrick VALUES (91,'Retailer values for Iceland as Country','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (92,'Retailer values','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (93,'Retailer','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (94,'top "Sales Revenue"" by retailer for Iceland as Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (95,'Sales Revenue by Retailer for Iceland as Country','y','sum unit sales by product for USA as country','sum unit_sales','product_id','country = USA','','','');
INSERT INTO dataset_patrick VALUES (96,'Sales Revenue by retailers for Iceland as Country','y','sum unit sales per products for USA as country','sum unit_sales','product_id','country = USA','','','');
INSERT INTO dataset_patrick VALUES (97,'top "Sales Revenue"" by retailers for Iceland as Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (98,'top 10 "Sales Revenue"" by Retailer for Iceland as Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (99,'Sales Revenue for Alpen as Retailer','y','sum unit sales for Club Chocolate Milk as product name','sum unit_sales','','product_name = Club Chocolate Milk','','','');
INSERT INTO dataset_patrick VALUES (100,'Sales Quantity by Month for Decathlon as Retailer','y','sum unit sales by month for Mexico as country','sum unit_sales','the_month','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (101,'Sales Quantity by Month','y','sum unit sales by month','sum unit_sales','the_month','','','','');
INSERT INTO dataset_patrick VALUES (102,'Sales Revenue / "Sales Target Revenue"" by Retailer for 2013 as Year"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (103,'Sales Revenue by Year for Alpen as Retailer','y','store sales by year for Mexico as country','avg store_sales','the_year','country = Mexico','','AM','');
INSERT INTO dataset_patrick VALUES (104,'Sales Target by Country for 2015 as Year','y','store sales by country for 1990 as year','avg store_sales','country','the_year = 1990','','AM','');
INSERT INTO dataset_patrick VALUES (105,'Shortage by Retailer','','','','','','','','');
INSERT INTO dataset_patrick VALUES (106,'Passengers 2010 - "Passengers 1990"" by ""Main Airport"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (107,'Sales Target Revenue < "Sales Revenue"" by Country for ""Europe and Central Asia"" as Region"','?','','','','','','','');
INSERT INTO dataset_patrick VALUES (108,'Country values for Sales Target Revenue > Sales Revenue','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (109,'Passengers 2010 / "Nb Destinations"" by ""Main Airport"" for ""United States"" as Country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (110,'Low income','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (111,'Country values for "Low income"""','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (112,'Population','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (113,'sales target by genre media format for spain','y','sum store cost by gender family for Mexico','sum store_cost','gender, product_family','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (114,'target sales by genre media for spain','','','','','','','','');
INSERT INTO dataset_patrick VALUES (115,'Genre values for Sales Target < Sales Revenue euro','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (116,'Genre','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (117,'Quantity by region month country for 2010','y','sum unit sales by category month country for 1990','sum unit_sales','product_category','the_year = 1990','','','');
INSERT INTO dataset_patrick VALUES (118,'sales quantity Quantity by Region','','','','','','','','');
INSERT INTO dataset_patrick VALUES (119,'sales revenue for media spain 2015','y','store sales for type USA 1990','avg store_sales','gender, product_family','country = USA, the_year = 1990','','AM','');
INSERT INTO dataset_patrick VALUES (120,'Sales Revenue / "Sales Target Revenue"" by Retailer for 2013"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (121,'Sales Revenue / "Sales Target Revenue"" by Retailer for 2013 as ""Date Year"""','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (122,'Sales Revenueby Retailer for 2013 as Year','y','store sales by family for 1990 as year','avg store_sales','product_family','the_year = 1990','','AM','');
INSERT INTO dataset_patrick VALUES (123,'Sales Revenue growth 2014 for Alpen as Retailer','','','','','','','','');
INSERT INTO dataset_patrick VALUES (124,'Sales Revenue growth 2015 as "Date Year"" for Alpen as Retailer"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (125,'Spain as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (126,'Sales Revenue by Country for Europe and Central Asia as Region','','','','','','','','');
INSERT INTO dataset_patrick VALUES (127,'Sales Revenue below target','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (128,'Passengers 1990 + "Passengers 1995"" + ""Passengers 2000"" + ""Passengers 2005"" + ""Passengers 2010"" by Main Airport"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (129,'Inventory by Product for Colombia','','','','','','','','');
INSERT INTO dataset_patrick VALUES (130,'bottom inventor by country 2015','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (131,'Inventory by Country for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (132,'top 3 Inventory by Country for 2015 as Year','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (133,'bottom 3 Inventory by Country for 2015 as Year','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (134,'max Passengers 2010 by Region for Newark Liberty Intl as Main Airport','','','','','','','','');
INSERT INTO dataset_patrick VALUES (135,'top 5 Pass 1990-2010','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (136,'count airport by country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (137,'Passengers 2005 Passengers 2010 for United States as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (138,'Passengers 2005','','','','','','','','');
INSERT INTO dataset_patrick VALUES (139,'GDP by Year for Low income as Income Group','','','','','','','','');
INSERT INTO dataset_patrick VALUES (140,'GDP by Year country for "Low income"" as ""Income Group"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (141,'Passengers 2005 by Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (142,'top 3 Passengers 2010','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (143,'Passengers 2010 by Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (144,'Passengers 2010 for United States as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (145,'Passengers 2010 for "United States"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (146,'GDP 2012','','','','','','','','');
INSERT INTO dataset_patrick VALUES (147,'average GDP for 2012 as Year','y','average store sales for 1991 as year','avg store_sales','','the_year = 1991','','AM','');
INSERT INTO dataset_patrick VALUES (148,'Passengers 2005 nb for "United States"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (149,'GDP by Income Group for 2012 as Year','y','store sales by province for 1990 as year','avg store_sales','state_province','the_year = 1990','','AM','');
INSERT INTO dataset_patrick VALUES (150,'GDP by "Income Group"" for 2012 as Year"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (151,'GDP for Lower middle income as Income Group','y','store sales for Sheri Nowmer as customer','avg store_sales','','fullname = Sheri Nowmer','','AM','');
INSERT INTO dataset_patrick VALUES (152,'GDP by Income Group','y','sum store cost by province','sum store_cost','state_province','','','','');
INSERT INTO dataset_patrick VALUES (153,'GDP by "Income Group"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (154,'gdp Income Group','y','sum store cost per province','sum store_cost','state_province','','','','');
INSERT INTO dataset_patrick VALUES (155,'Country values by gdp','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (156,'gdp by coun','y','sum unit sales by coun','sum unit_sales','country','','','','');
INSERT INTO dataset_patrick VALUES (157,'sale revenu GDP by Country','y','sum unit sales store sales by product','sum unit_sales, avg store_sales','product_id','','','AM','');
INSERT INTO dataset_patrick VALUES (158,'film values for Spain as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (159,'film revenue','','','','','','','','');
INSERT INTO dataset_patrick VALUES (160,'Sales Revenue by film country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (161,'top "Sales Revenue"" by Retailerfor Iceland as Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (162,'top "Sales Revenue"" by Retailer"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (163,'Sales Revenue by genre for france','y','sum unit sales by family for Jalisco','sum unit_sales','product_family','state_province = Jalisco','','','');
INSERT INTO dataset_patrick VALUES (164,'Sales Revenue by Genre for austria as Country','y','sum unit salies by category for canada as Country','sum unit_sales','product_category','country = Canada','','','');
INSERT INTO dataset_patrick VALUES (165,'Sales Revenue by Genre for germa as Country','y','sum unit salies by category for cana as Country','sum unit_sales','product_category','country = Canada','','','');
INSERT INTO dataset_patrick VALUES (166,'Sales Revenue by Genre for uk as Country','y','sum unit salies by category for usa as Country','sum unit_sales','product_category','country = USA','','','');
INSERT INTO dataset_patrick VALUES (167,'Planned Qty Ordered','','','','','','','','');
INSERT INTO dataset_patrick VALUES (168,'sales revenue by genre spain','y','store sales by department Jalisco','avg store_sales','product_department','state_province = Jalisco','','AM','');
INSERT INTO dataset_patrick VALUES (169,'Planned Qty Ordered- Inventory by Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (170,'Planned Qty OrderedInventory by Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (171,'Sales Revenue / 2198921 by Genre for 2015 as Year','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (172,'Sales Revenue by Genre for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (173,'Sales Revenue / 25369 by Genre for 2015 as Year','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (174,'percent Sales Revenue by Genre for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (175,'Sales Revenue/2198921 by Genre for 2015 as Year','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (176,'Sales Revenue / 2198921 by media format for 2015 as Year','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (177,'Sales Revenue / 2198921 by "media format"" for 2015 as Year spai"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (178,'Sales Revenue by media format for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (179,'Sales Revenue/25369 by "media format"" for 2015 as Year"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (180,'Sales Revenue by Retailer','','','','','','','','');
INSERT INTO dataset_patrick VALUES (181,'Sales Revenue by Retailer year for 2013','','','','','','','','');
INSERT INTO dataset_patrick VALUES (182,'inventory-planned by country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (183,'top 3 Inventory - "Planned Qty Ordered"" by Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (184,'top 3 "Planned Qty Ordered""-Inventory by Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (185,'top 3 "Planned Qty Ordered"" - Inventory by Country products"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (186,'Planned Qty Ordered - Inventory by Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (187,'Product','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (188,'passengers by aiport 1990 2010','','','','','','','','');
INSERT INTO dataset_patrick VALUES (189,'Passengers 1990 passenger 201by Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (190,'Passengers 1990 - "Passengers 2010"" by ""Main Airport"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (191,'Passengers 1990 - "Passengers 2010"" by ""Main Airport"" region"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (192,'Passengers 1990 - "Passengers 2010"" by miami"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (193,'Genre values for Sales Target Revenue > Sales Revenue','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (194,'region for "Miami Intl"" as ""Main Airport"""','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (195,'population north america 2010','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (196,'Sales Target Revenue / "Sales Revenue"" for Iceland as Country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (197,'Sales Revenue by shop for Game as Genre','','','','','','','','');
INSERT INTO dataset_patrick VALUES (198,'Population for "North America"" as Region"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (199,'Passengers 2010 / "Passengers 1990"" for Miami Intl as Main Airport"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (200,'count airports by country','y','count sales by family','count sales_fact_1997','product_family','','','','');
INSERT INTO dataset_patrick VALUES (201,'Sales Target by shop for Game as Genre','','','','','','','','');
INSERT INTO dataset_patrick VALUES (202,'Sales Target / "Sales Revenue"" by shop for Game as Genre"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (203,'top "Sales Revenue"" by Retailer for Game as Genre"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (204,'Sales Revenue for Decathlon as Retailer','','','','','','','','');
INSERT INTO dataset_patrick VALUES (205,'quantity for Decathlon as Retailer','y','count sales for Club Chocolate Milk as product name','count sales_fact_1997','','product_name = Club Chocolate Milk','','','');
INSERT INTO dataset_patrick VALUES (206,'Quantity by Region for Decathlon as Retailer','y','count sales by city for Club Chocolate Milk as product name','count sales_fact_1997','city','product_name = Club Chocolate Milk','','','');
INSERT INTO dataset_patrick VALUES (207,'Quantity by Region','y','count sales by city','count sales_fact_1997','city','','','','');
INSERT INTO dataset_patrick VALUES (208,'Sales Revenue / "Sales Target Revenue"" by"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (209,'Sales Revenue / "Sales Target Revenue"" by Retailer 2013"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (210,'Sales Revenue by Retailer for 2015 as Date Year','y','sum unit sales by product for 2015 as year','sum unit_sales','product_id','the_year = 2015','','','');
INSERT INTO dataset_patrick VALUES (211,'Sales Revenue growth 2015 as "Date Year"" by Retailer"','y','sum unit sales growth for 2015 as year by product ','sum unit_sales','product_id','the_year = 2015','','','');
INSERT INTO dataset_patrick VALUES (212,'Sales Revenue growth 2013 as "Date Year"" by Retailer"','y','sum unit sales growth for 2013 as year by product ','sum unit_sales','product_id','the_year = 2013','','','');
INSERT INTO dataset_patrick VALUES (213,'Sales Revenue growth 2014 as "Date Year"" by Retailer"','y','sum unit sales growth for 2014 as year by product ','sum unit_sales','product_id','the_year = 2014','','','');
INSERT INTO dataset_patrick VALUES (214,'Sales Revenue growth 2014 as "Date Year"" by Color"','y','sum unit sales growth for 2014 as year by category','sum unit_sales','product_category','the_year = 2014','','','');
INSERT INTO dataset_patrick VALUES (215,'percent Sales Revenue by Color','','','','','','','','');
INSERT INTO dataset_patrick VALUES (216,'sales revenue by color for 2013 2014 2015','?','','','','','IN operator','','');
INSERT INTO dataset_patrick VALUES (217,'sales revenue color 2013 2014 2015','?','','','','','IN operator','','');
INSERT INTO dataset_patrick VALUES (218,'percent "Sales Revenue"" by Color for 2013 2014 2015"','?','','','','','IN operator','','');
INSERT INTO dataset_patrick VALUES (219,'Quantity by Country for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (220,'shortage by country 2015','','','','','','','','');
INSERT INTO dataset_patrick VALUES (221,'Shortage by Country for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (222,'bottom Shortage by Country for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (223,'bottom 3 Shortage by Country for 2015 as Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (224,'top 3 "Passengers 2010"" - ""Passengers 1990"" by ""Main Airport"""','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (225,'top 3 "Passengers 2010"" by ""Main Airport"""','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (226,'top 3 "Passengers 2010"" popu by ""Main Airport"""','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (227,'top 3 "Passengers 2010"" by airports"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (228,'Passengers 2010 by "Main Airport"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (229,'topPassengers 2010 by Main Airport','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (230,'top 3 "Passengers 2010""by ""Main Airport"""','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (231,'top passengers 2010 by airport by regio','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (232,'Passengers 2010 by "Main Airport"" by Region"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (233,'Nb Destinations by "Main Airport"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (234,'Nb Destinations','','','','','','','','');
INSERT INTO dataset_patrick VALUES (235,'United States as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (236,'Nb Destinations for "United States"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (237,'Passengers 2010 destinations for "United States"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (238,'Passengers 2005 "Passengers 2010"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (239,'"Passengers 2010"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (240,'GDP','','','','','','','','');
INSERT INTO dataset_patrick VALUES (241,'GDP for Low income as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (242,'GDP by Country for "Low income"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (243,'GDP for "Low income"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (244,'GDP by Country for "Low income"" as ""Income Group"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (245,'GDP for Somalia as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (246,'GDP by Box for Somalia as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (247,'GDP by for Somalia as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (248,'sales revenue / sales t','','','','','','','','');
INSERT INTO dataset_patrick VALUES (249,'Income Group','','','','','','','','');
INSERT INTO dataset_patrick VALUES (250,'revenue / target','','','','','','','','');
INSERT INTO dataset_patrick VALUES (251,'target revenue for spain','','','','','','','','');
INSERT INTO dataset_patrick VALUES (252,'Sales Revenue / "Sales Target Revenue"" by country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (253,'Sales Revenue / "Sales Target"" by media format"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (254,'Sales Revenue by "genre for Iceland as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (255,'sales revenue by genre for iceland','','','','','','','','');
INSERT INTO dataset_patrick VALUES (256,'Sales Revenue by genre for Iceland as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (257,'Sales Revenue / "Sales Target Revenue"""','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (258,'topSales Revenue by Genre for Iceland as Country','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (259,'Spain media shops revenue','','','','','','','','');
INSERT INTO dataset_patrick VALUES (260,'count "Main Airport"""','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (261,'Sales Revenue year media format shop Spain','','','','','','','','');
INSERT INTO dataset_patrick VALUES (262,'Sales Revenue Year','','','','','','','','');
INSERT INTO dataset_patrick VALUES (263,'film format','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (264,'Type values','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (265,'film values','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (266,'film','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (267,'film spain','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (268,'film values by','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (269,'Margin gross by film','','','','','','','','');
INSERT INTO dataset_patrick VALUES (270,'revenue by film','','','','','','','','');
INSERT INTO dataset_patrick VALUES (271,'revenue by genre','','','','','','','','');
INSERT INTO dataset_patrick VALUES (272,'country for low inc','','','','','','','','');
INSERT INTO dataset_patrick VALUES (273,'country values for lower middle income as income group','','','','','','','','');
INSERT INTO dataset_patrick VALUES (274,'Country values for "upper middle income"" as ""Income Group"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (275,'gdp by year for afgh','','','','','','','','');
INSERT INTO dataset_patrick VALUES (276,'GDP by Year for armen as country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (277,'gdp by year for albani as country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (278,'count airport by count','','','','','','','','');
INSERT INTO dataset_patrick VALUES (279,'Passengers 2005 for United States as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (280,'Passengers 2005 for "United States"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (281,'passengers 2005 united states','','','','','','','','');
INSERT INTO dataset_patrick VALUES (282,'Passengers 1990 - "Passengers 2010"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (283,'Sales Revenue>"Sales Target"""','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (284,'Sales Revenue > "Sales Target"""','?','','','','','comparing measures','','');
INSERT INTO dataset_patrick VALUES (285,'Passengers 1995 - "Passengers 2010"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (286,'Passengers 2010-Passengers 1990','','','','','','','','');
INSERT INTO dataset_patrick VALUES (287,'Passengers 1990-2010>0','','','','','','','','');
INSERT INTO dataset_patrick VALUES (288,'Passengers 1990-2010 > 0','','','','','','','','');
INSERT INTO dataset_patrick VALUES (289,'Pass 1990-2010','','','','','','','','');
INSERT INTO dataset_patrick VALUES (290,'Sales Target Revenue < Sales Revenue','','','','','','','','');
INSERT INTO dataset_patrick VALUES (291,'Nb Destinations passengers 2010 united sta','','','','','','','','');
INSERT INTO dataset_patrick VALUES (292,'increase by airpo','','','','','','','','');
INSERT INTO dataset_patrick VALUES (293,'nb destinations by airport by region','','','','','','','','');
INSERT INTO dataset_patrick VALUES (294,'population by main airport region','','','','','','','','');
INSERT INTO dataset_patrick VALUES (295,'inventory by product for slovenia','y','sum unit sales by category for mexico','sum unit_sales','product_category','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (296,'invenotry by product for san marino as country','y','sum unit sales by category for mexico as country','sum unit_sales','product_category','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (297,'Inventory by Product for "colo"" as Country"','y','sum unit sales by category for messico as Country','sum unit_sales','product_category','country = Mexico','','','');
INSERT INTO dataset_patrick VALUES (298,'sales revenue by retailer for nimb','','','','','','','','');
INSERT INTO dataset_patrick VALUES (299,'Sales Revenue by Retailer for "boliv as Product"','y','store sales by month for club choc as product','avg store_sales','the_month','product_name = Club Chocolate Milk','','AM','');
INSERT INTO dataset_patrick VALUES (300,'Sales Revenue by Retailer for "metarun"" as Product"','y','store sales by month for club chocolate milk as product','avg store_sales','the_month','product_name = Club Chocolate Milk','','AM','');
INSERT INTO dataset_patrick VALUES (301,'Sales Revenue by Retailer for faas as Product','','','','','','','','');
INSERT INTO dataset_patrick VALUES (302,'Sales Revenue by Retailer for air zoom as Product','','','','','','','','');
INSERT INTO dataset_patrick VALUES (303,'Sales Revenue by Retailer for "Air Zoom"" as Product"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (304,'Sales Revenue / "Sales Target revenue retailer 2013"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (305,'Sales Revenue for Spain as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (306,'Sales Revenue / "Sales Target"" by Country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (307,'Sales Revenue / "Sales Target"" by Genre for France as Country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (308,'Sales Revenue / "Sales Target"" by Genre for Spain as Country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (309,'revenue / target media genre iceland','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (310,'Quantity by Month for Decathlon as Retailer','y','count sales by month for Jalisco as Province','count sales_fact_1997','the_month','state_province = Jalisco','','','');
INSERT INTO dataset_patrick VALUES (311,'Quantity by Product','','','','','','','','');
INSERT INTO dataset_patrick VALUES (312,'Sales Revenue / "Sales Target"" by retailer 2013"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (313,'Sales Revenue growth 2014 as "Date Year"" for Alpen as Retailer"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (314,'Sales Revenue by Color for 2013','y','store_cost by province for 2013','sum store_cost','state_province','the_year = 2013','','','');
INSERT INTO dataset_patrick VALUES (315,'Passengers 2010 - "Passengers 1990"" by Main Airport"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (316,'Passengers 2010 - "Passengers 1990"" by Region"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (317,'Passengers 2010 - "Passengers 1990"""','','','','','','','','');
INSERT INTO dataset_patrick VALUES (318,'Nb Destinations by Main Airport for United States as Country','','','','','','','','');
INSERT INTO dataset_patrick VALUES (319,'nb destinations by airport for united st','','','','','','','','');
INSERT INTO dataset_patrick VALUES (320,'Nb Destinations by "Main Airport"" for ""United States"" as Country"','','','','','','','','');
INSERT INTO dataset_patrick VALUES (321,'Passengers 2005 / "Nb Destinations"" by Main Airport for United States as Country"','?','','','','','algebric operation','','');
INSERT INTO dataset_patrick VALUES (322,'GDP by Year for Lower middle income as Income Group','y','sum unit sales by yeat for atomc mints as product','sum unit_sales','the_year','product_name = Atomic Mints','','','');
INSERT INTO dataset_patrick VALUES (323,'GDP by Year for Upper middle income as Income Group','y','sum unit sales by yeat for atmoc mints as product name','sum unit_sales','the_year','product_name = Atomic Mints','','','');
INSERT INTO dataset_patrick VALUES (324,'media format""','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (325,'sales revenue for iceland','y','sum unit sales for Sheri Nowmer','sum unit_sales','','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (326,'sales revenue by media for iceland','y','store sales by media type for Sheri Nowmer','avg store_sales','media_type','fullname = Sheri Nowmer','','AM','');
INSERT INTO dataset_patrick VALUES (327,'Sales Revenue by "media format"" for Iceland as Country"','y','sum store cost by product name for Canada as country','sum store_cost','product_name','country = Canada','','','');
INSERT INTO dataset_patrick VALUES (328,'Sales Revenue by media for Iceland as Country','y','sum store cost by product for Canada as country','sum store_cost','product_name','country = Canada','','','');
INSERT INTO dataset_patrick VALUES (329,'planned quan','n','','','','','','','');
INSERT INTO dataset_patrick VALUES (330,'top 1 count "Main Airport"" by Country"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (331,'Sales Target by film','y','sum stpre cost by media','sum store_cost','media_type','','','','');
INSERT INTO dataset_patrick VALUES (332,'Sales Target by film for Spain as Country','y','sum store cst by media for Nowmer as customer','sum store_cost','media_type','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (333,'GDP by year canada','y','sum unit sales by year Sheri Nowmer','sum unit_sales','the_year','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (334,'GDP by Year for benin as Country','y','sum unt sls by product for Sheri Nowmer as fullname','sum unit_sales','product_id','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (335,'GDP by Year for egypt as Country','y','sum unit sales by prdct for Sheri Nowmer as fullname','sum unit_sales','product_id','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (336,'GDP by Year for cuba as Country','y','sum unit sales by product for Seri Nower as fullname','sum unit_sales','product_id','fullname = Sheri Nowmer','','','');
INSERT INTO dataset_patrick VALUES (337,'bott"Sales Target"" by film"','?','','','','','bottom/top aggregation','','');
INSERT INTO dataset_patrick VALUES (338,'','y','sum unit sales by media type for USA','sum unit_sales','media_type','country = USA','','AA','');
INSERT INTO dataset_patrick VALUES (339,'','y','sum unit sales by media type for USA and Mexico','sum unit_sales','media_type','country = USA and country = Mexico','','AA,AA','');
INSERT INTO dataset_patrick VALUES (340,'','y','sum unit sales by media type for Salem','sum unit_sales','media_type','city = Salem','','AA','');
INSERT INTO dataset_patrick VALUES (341,'','y','sum unit sales by country by month by province for USA','sum unit_sales','country, the_month, state_province','country = USA','','AA','');
INSERT INTO dataset_patrick VALUES (342,'','y','sum unit sales by month for USA','sum unit_sales','the_month','country = USA','','AA','');
INSERT INTO dataset_patrick VALUES (343,'','y','sum unit sales for country Sheri Nowmer','sum unit_sales','','country = USA','','AVM','');
INSERT INTO dataset_patrick VALUES (344,'','y','store sales for Sheri Nowmer','avg store_sales','','fullname = Sheri Nowmer','','MA','');
INSERT INTO dataset_patrick VALUES (345,'','y','store sales for USA','avg store_sales','','country = USA','','AA,MA','');
INSERT INTO dataset_patrick VALUES (346,'','y','store sales for USA and state province Sheri Nowmere','avg store_sales','','country = USA and state_province = BC','','AA,MA,AVM','');
INSERT INTO dataset_patrick VALUES (347,'','y','store sales for USA and Sheri Nowmere as province','avg store_sales','','country = USA and state_province = BC','','AA,MA,AVM','');
commit;

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
INSERT INTO language_predicate VALUES ( 7,'=','[equal, is]','cop');
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
-- select * from "MEASURE";
-- select * from groupbyoperator;
-- select * from "TABLE";
-- select * from "LEVEL";
INSERT INTO groupbyoperator_of_measure select groupbyoperator_id, measure_id from measure, groupbyoperator;
INSERT INTO "SYNONYM"(synonym_id, table_name, reference_id, term) VALUES ("FACT", (select fact_id from fact where fact_name = "LINEORDER2"), "sales");
commit;