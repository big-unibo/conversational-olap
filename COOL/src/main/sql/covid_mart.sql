/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

drop database covid_mart;
create database covid_mart;
use covid_mart;
--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `country` (
  `geoId` varchar(255),
  `countriesAndTerritories` varchar(255),
  `countryterritoryCode` varchar(255),
  `popData2018` double DEFAULT NULL,
  `continentExp` text,
  primary key(`countriesAndTerritories`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `date` (
  `dateRep` varchar(10),
  `month` varchar(7),
  `year` varchar(4) DEFAULT NULL,
  primary key(`dateRep`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `fact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fact` (
  `dateRep` varchar(10),
  `countriesAndTerritories` varchar(255),
  `cases` bigint(20) DEFAULT NULL,
  `cases14` double DEFAULT NULL,
  `cases100K` double DEFAULT NULL,
  `cases1M` double DEFAULT NULL,
  `deaths` bigint(20) DEFAULT NULL,
  `deaths14` double DEFAULT NULL,
  `deaths100K` double DEFAULT NULL,
  `deaths1M` double DEFAULT NULL,
  CONSTRAINT `country` FOREIGN KEY (`countriesAndTerritories`) REFERENCES `country` (`countriesAndTerritories`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `date` FOREIGN KEY (`dateRep`) REFERENCES `date` (`dateRep`) ON DELETE CASCADE ON UPDATE CASCADE,
  primary key(`countriesAndTerritories`, `dateRep`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Data are loaded from src/main/python/Describe/covid_mart.py