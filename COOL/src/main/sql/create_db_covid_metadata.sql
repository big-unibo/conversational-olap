DROP DATABASE IF EXISTS `covid_metadata`;
CREATE DATABASE `covid_metadata`;
USE `covid_metadata`;

DROP TABLE IF EXISTS `database` CASCADE;
CREATE TABLE `database` (
  `database_id` int(11) NOT NULL AUTO_INCREMENT,
  `database_name` varchar(45) NOT NULL UNIQUE,
  `IPaddress` varchar(12) NOT NULL,
  `port` int(11) NOT NULL,
  PRIMARY KEY (`database_id`),
  UNIQUE(`database_name`, `IPaddress`, `port`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `groupbyoperator` CASCADE;
CREATE TABLE `groupbyoperator` (
  `groupbyoperator_id` int(11) NOT NULL AUTO_INCREMENT,
  `groupbyoperator_name` varchar(45) NOT NULL UNIQUE,
  `groupbyoperator_synonyms` text,
  PRIMARY KEY (`groupbyoperator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hierarchy` CASCADE;
CREATE TABLE `hierarchy` (
  `hierarchy_id` int(11) NOT NULL AUTO_INCREMENT,
  `hierarchy_name` varchar(45) NOT NULL UNIQUE,
  `hierarchy_synonyms` text,
  PRIMARY KEY (`hierarchy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fact` CASCADE;
CREATE TABLE `fact` (
  `fact_id` int(11) NOT NULL AUTO_INCREMENT,
  `fact_name` varchar(45) NOT NULL UNIQUE,
  `fact_synonyms` text,
  `database_id` int(11) NOT NULL,
  PRIMARY KEY (`fact_id`),
  KEY `fkIdx_214` (`database_id`),
  CONSTRAINT `FK_214` FOREIGN KEY (`database_id`) REFERENCES `database` (`database_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `table` CASCADE;
CREATE TABLE `table` (
  `table_id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(45) NOT NULL UNIQUE,
  `table_type` varchar(45) NOT NULL,
  `fact_id` int(11) DEFAULT NULL,
  `hierarchy_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`table_id`),
  KEY `fkIdx_115` (`fact_id`),
  KEY `fkIdx_160` (`hierarchy_id`),
  CONSTRAINT `FK_115` FOREIGN KEY (`fact_id`) REFERENCES `fact` (`fact_id`),
  CONSTRAINT `FK_160` FOREIGN KEY (`hierarchy_id`) REFERENCES `hierarchy` (`hierarchy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `relationship` CASCADE;
CREATE TABLE `relationship` (
  `relationship_id` int(11) NOT NULL AUTO_INCREMENT,
  `table1` int(11) NOT NULL,
  `table2` int(11) NOT NULL,
  PRIMARY KEY (`relationship_id`),
  KEY `fkIdx_72` (`table1`),
  KEY `fkIdx_75` (`table2`),
  CONSTRAINT `FK_72` FOREIGN KEY (`table1`) REFERENCES `table` (`table_id`),
  CONSTRAINT `FK_75` FOREIGN KEY (`table2`) REFERENCES `table` (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `column` CASCADE;
CREATE TABLE `column` (
  `column_id` int(11) NOT NULL AUTO_INCREMENT,
  `column_name` varchar(45) NOT NULL,
  `column_type` varchar(45) NOT NULL,
  `isKey` tinyint(1) NOT NULL,
  `relationship_id` int(11) DEFAULT NULL,
  `table_id` int(11) NOT NULL,
  PRIMARY KEY (`column_id`),
  KEY `fkIdx_100` (`table_id`),
  KEY `fkIdx_166` (`relationship_id`),
  CONSTRAINT `FK_100` FOREIGN KEY (`table_id`) REFERENCES `table` (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `level` CASCADE;
CREATE TABLE `level` (
  `level_id` int(11) NOT NULL AUTO_INCREMENT,
  `level_type` varchar(45) NOT NULL,
  `level_description` varchar(200),
  `level_name` varchar(45) NOT NULL UNIQUE,
  `cardinality` int(11) DEFAULT NULL,
  `hierarchy_id` int(11) NOT NULL,
  `level_synonyms` text,
  `column_id` int(11) NOT NULL,
  `min` bigint(10) DEFAULT NULL,
  `max` bigint(10) DEFAULT NULL,
  `avg` bigint(10) DEFAULT NULL,
  `isDescriptive` tinyint(1) NOT NULL DEFAULT '0',
  `mindate` datetime DEFAULT NULL,
  `maxdate` datetime DEFAULT NULL,
  PRIMARY KEY (`level_id`),
  KEY `fkIdx_108` (`column_id`),
  KEY `fkIdx_56` (`hierarchy_id`),
  CONSTRAINT `FK_108` FOREIGN KEY (`column_id`) REFERENCES `column` (`column_id`),
  CONSTRAINT `FK_56` FOREIGN KEY (`hierarchy_id`) REFERENCES `hierarchy` (`hierarchy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `hierarchy_in_fact` CASCADE;
CREATE TABLE `hierarchy_in_fact` (
  `fact_id` int(11) NOT NULL,
  `hierarchy_id` int(11) NOT NULL,
  PRIMARY KEY (`fact_id`, `hierarchy_id`),
  KEY `fkIdx_44` (`fact_id`, `hierarchy_id`),
  KEY `fkIdx_48` (`hierarchy_id`),
  CONSTRAINT `FK_44` FOREIGN KEY (`fact_id`) REFERENCES `fact` (`fact_id`),
  CONSTRAINT `FK_48` FOREIGN KEY (`hierarchy_id`) REFERENCES `hierarchy` (`hierarchy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `language_predicate` CASCADE;
CREATE TABLE `language_predicate` (
  `language_predicate_id` int(11) NOT NULL AUTO_INCREMENT,
  `language_predicate_name` varchar(45) NOT NULL UNIQUE,
  `language_predicate_synonyms` text DEFAULT NULL,
  `language_predicate_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`language_predicate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=539 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `language_operator` CASCADE;
CREATE TABLE `language_operator` (
  `language_operator_id` int(11) NOT NULL AUTO_INCREMENT,
  `language_operator_name` varchar(45) NOT NULL UNIQUE,
  `language_operator_synonyms` text DEFAULT NULL,
  `language_operator_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`language_operator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=539 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `level_rollup` CASCADE;
CREATE TABLE `level_rollup` (
  `level_rollup_id` int(11) NOT NULL AUTO_INCREMENT,
  `level_start` int(11) NOT NULL,
  `level_to` int(11) NOT NULL,
  PRIMARY KEY (`level_rollup_id`),
  KEY `fkIdx_137` (`level_start`),
  KEY `fkIdx_142` (`level_to`),
  CONSTRAINT `FK_137` FOREIGN KEY (`level_start`) REFERENCES `level` (`level_id`),
  CONSTRAINT `FK_142` FOREIGN KEY (`level_to`) REFERENCES `level` (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `measure` CASCADE;
CREATE TABLE `measure` (
  `measure_id` int(11) NOT NULL AUTO_INCREMENT,
  `measure_name` varchar(45) NOT NULL,
  `fact_id` int(11) NOT NULL,
  `measure_synonyms` text,
  `column_id` int(11) NOT NULL,
  PRIMARY KEY (`measure_id`),
  UNIQUE(`measure_name`, `fact_id`),
  KEY `fkIdx_157` (`column_id`),
  KEY `fkIdx_23` (`fact_id`),
  CONSTRAINT `FK_157` FOREIGN KEY (`column_id`) REFERENCES `column` (`column_id`),
  CONSTRAINT `FK_23` FOREIGN KEY (`fact_id`) REFERENCES `fact` (`fact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `member` CASCADE;
CREATE TABLE `member` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT,
  `member_name` varchar(45) NOT NULL,
  `level_id` int(11) NOT NULL,
  `member_synonyms` text,
  PRIMARY KEY (`member_id`),
  UNIQUE(`member_name`, `level_id`),
  KEY `fkIdx_133` (`level_id`),
  CONSTRAINT `FK_133` FOREIGN KEY (`level_id`) REFERENCES `level` (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=86090 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `groupbyoperator_of_measure` CASCADE;
CREATE TABLE `groupbyoperator_of_measure` (
  `groupbyoperator_id` int(11) NOT NULL,
  `measure_id` int(11) NOT NULL,
  PRIMARY KEY (`groupbyoperator_id`, `measure_id`),
  KEY `fkIdx_30` (`groupbyoperator_id`),
  KEY `fkIdx_34` (`measure_id`),
  CONSTRAINT `FK_30` FOREIGN KEY (`groupbyoperator_id`) REFERENCES `groupbyoperator` (`groupbyoperator_id`),
  CONSTRAINT `FK_34` FOREIGN KEY (`measure_id`) REFERENCES `measure` (`measure_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `synonym` CASCADE;
CREATE TABLE `synonym` (
  `synonym_id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(45) NOT NULL,
  `reference_id` int(11) NOT NULL, -- id of the Entity in the given table
  `term` varchar(45) NOT NULL,
  PRIMARY KEY (`synonym_id`),
  UNIQUE(`term`, `reference_id`, `table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=86416 DEFAULT CHARSET=utf8;

INSERT INTO `groupbyoperator` VALUES
  (1, 'sum', '[\"total\", \"number\", \"amount\", \"how much\"]'),
  (2, 'avg', '[\"average\", \"medium\", \"mean\"]'),
  (3, 'max', '[\"maximum\", \"highest\", \"top\"]'),
  (4, 'min', '[\"minimum\", \"lowest\", \"bottom\"]'),
  (5, 'stdev', '[\"deviation\", \"standard deviation\"]');

INSERT INTO `language_predicate` VALUES 
  ( 1,'where','[\"filter\", \"filter on\", \"for\", \"in\", \"on\"]','where'),
  -- ( 3,'select','[\"show\", \"return\", \"tell\", \"find\", \"get\"]','SELECT'),
  ( 4,'by','[\"group by\", \"grouped by\", \"grouping by\", \"for\", \"for each\", \"for every\", \"per\"]','by'),
  ( 5,'>=','[\"greater equal\", \"greater equal than\"]','cop'),
  ( 6,'<=','[\"lower equal\", \"lower equal than\"]','cop'),
  ( 7,'=','[\"equal\", \"is\"]','cop'),
  ( 8,'<','[\"lower than\", \"below\", \"less than\", \"before\", \"until\"]','cop'),
  ( 9,'>','[\"greater than\", \"above\", \"more than\", \"after\", \"from\"]','cop'),
  (10,'and',NULL,'and'),
  (11,'or',NULL,'or'),
  ( 2,'not',NULL,'not'),
  -- (12,'between',NULL,'between'),
  (13,'count','[\"number\", \"amount\", \"how many\", \"how many times\"]','count');
  -- (14,'distinct',NULL,'COUNTOPERATOR');

INSERT INTO `language_operator` VALUES
  (15,'drill','[\"drill down\",\"specialize\"]','drill'),
  (16,'rollup','[\"roll up\",\"generalize\"]','rollup'),
  (17,'replace','[\"substitute\"]','replace'),
  (18,'add',NULL,'add'),
  (19,'drop','[\"remove\"]','drop'),
  (20,'slice','[\"filter\", \"filter on\", \"slice and dice\", \"dice\"]','sad'),
  (21,'to','[\"up to\", \"down to\"]','accessory'),
  (22,'with',NULL,'accessory');

-- --------------------------------------------------------------------------------------------------------------------------------
-- TO EXECUTE AFTER DBreader.java
-- --------------------------------------------------------------------------------------------------------------------------------
-- Add rollup dependencies: TO BE DONE BY HAND
-- INSERT INTO `level_rollup` VALUES 
--   (product, type),
--   (type, category);

-- Add operators appliable to each measure: TO BE DONE BY HAND
-- INSERT INTO `groupbyoperator_of_measure` VALUES 
--   (OP_ID, MEA_ID),             OP_ID from `groupbyoperator` table, MEA_ID from `measure` table
--   (OP_ID, MEA_ID);
-- ... Or populate it with all the operators
INSERT INTO `groupbyoperator_of_measure` select groupbyoperator_id, measure_id from measure, groupbyoperator;
UPDATE fact SET fact_synonyms = '[\"sales\"]' WHERE fact_name = 'sales_fact_1997';
UPDATE `level` SET level_description = 'Numeric product identifier' WHERE level_name = 'product_id';
INSERT INTO synonym(`table_name`, `reference_id`, `term`) VALUES ("fact", (select fact_id from fact where fact_name = "sales_fact_1997"), "sales");
-- INSERT INTO synonym(`table_name`, `reference_id`, `term`) select "language_operator", (select language_operator_id from language_operator where language_operator_name = "all"), concat("all ", hierarchy_name) from hierarchy h;
