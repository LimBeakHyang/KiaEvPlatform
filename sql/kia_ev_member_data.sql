-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: kiaevdb
-- ------------------------------------------------------
-- Server version	9.6.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '1867cb49-0e08-11f1-870b-b42e99e064f5:1-753';

--
-- Table structure for table `member_tbl`
--

DROP TABLE IF EXISTS `member_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_tbl` (
  `member_no` bigint NOT NULL AUTO_INCREMENT,
  `login_id` varchar(255) DEFAULT NULL,
  `member_pw` varchar(255) NOT NULL,
  `member_name` varchar(255) DEFAULT NULL,
  `birth_date` date NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `detail_address` varchar(255) DEFAULT NULL,
  `member_status` varchar(255) DEFAULT '정상회원',
  `join_date` date NOT NULL,
  `dealer_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`member_no`),
  UNIQUE KEY `login_id` (`login_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_tbl`
--

LOCK TABLES `member_tbl` WRITE;
/*!40000 ALTER TABLE `member_tbl` DISABLE KEYS */;
INSERT INTO `member_tbl` VALUES (1,'ooll93570','1234','박아연','1999-07-09','ooll93570@gmail.com','010-0551-1321','55041','전북특별자치도 전주시 완산구 은행로 39','반석빌라 ','탈퇴회원','2026-03-31',NULL),(4,'ooll935701','qkrdkdus0201!','박길순','2009-01-06','ooll93570@naver.com','010-1234-1234','55041','전북특별자치도 전주시 완산구 은행로 39 (풍남동3가)','반석빌라201','탈퇴회원','2026-03-31',NULL),(5,'ooll935702','qkrdkdus0201!@','박아연','1993-06-22','ooll935701@gmail.com','010-0551-1321','13529','경기 성남시 분당구 판교역로 166 (백현동)','skln','정상회원','2026-03-31',NULL),(6,'ooll935703','qkrdkdus0201!!','박길순','1997-10-21','ooll9357022@gmail.com','010-1234-1234','55041','전북특별자치도 전주시 완산구 은행로 39 (풍남동3가)','skln','정상회원','2026-04-01',NULL),(7,'ooll935705','qkrdkdus0201','서진교','2026-04-08','ooll935702@gmail.com','010-1234-1234','55041','전북특별자치도 전주시 완산구 풍남동3가 47-1','101호','정상회원','2026-04-06',NULL),(8,'dpdvl123','qkrdkdus0201','박아연','2001-11-06','ooll935704@gmail.com','010-1234-5678','55041','전북특별자치도 전주시 완산구 은행로 39 (풍남동3가)','101호','정상회원','2026-04-06',NULL),(9,'dkdus123','ooll93570','김진영','1997-07-22','jin7788@naver.com','010-4567-8523','07067','서울 동작구 신대방길 2 (신대방동)','우진빌라 101호','정상회원','2026-04-07',NULL),(10,'JHg0709','ghrmsghrms0201','지호근','2000-07-05','hg124@naver.com','010-1234-1234','16282','경기 수원시 장안구 조원로112번길 32 (조원동, 우진아트빌라)','우진아트빌302호','정상회원','2026-04-07',NULL),(11,'Kim Han-young11','gksdud123','김한영','1990-06-12','hy0201@naver.com','010-1234-5678','14407','경기 부천시 오정구 역곡로496번길 4 (고강동, 서진파크빌라)','서진빌라 502호','정상회원','2026-04-07',NULL),(12,'ecarplug02','dlzkvmffjr,11','김우빈','1981-06-15','ecar12@naver.com','010-2222-2422','12206','경기 남양주시 와부읍 수레로77번길 17 (삼진빌라)','삼진빌라 201호','정상회원','2026-04-07',NULL),(13,'wooch12','dncks123','권우찬','1995-02-14','wooch123@naver.com','010-0551-1321','01033','서울 강북구 삼양로111가길 5 (수유동, 한우림빌)','508호','정상회원','2026-04-07',NULL),(14,'ysy0315','tndus123','윤수연','1999-03-15','ysy0315@naver.com','010-2222-2422','12453','경기 가평군 청평면 강변로 117','하늘채 203호','정상회원','2026-04-07',NULL),(15,'hywo123','gusdn123','기현우','2000-07-05','hywo12@naver.com','010-1234-5678','10463','경기 고양시 덕양구 고양대로1395번길 67 (성사동, 형제빌라)','형제빌라201호','정상회원','2026-04-07',NULL),(16,'mingu93','alsrb123','최민규','1998-05-06','migu123@naver.com','010-1234-5678','13475','경기 성남시 분당구 판교동 514-11','한진빌라201호','정상회원','2026-04-07',NULL),(17,'ooll935707','4567','최강수','1997-05-02','ooll935705@gmail.com','010-5042-3293','01128','서울 강북구 미아동 160-2','진아 빌라201호','탈퇴회원','2026-04-17',NULL),(18,'abc','1234','철수','1998-02-21','g@mail.com','010-9512-1234','13536','경기 성남시 분당구 판교역로10번길 16 (백현동)','','정상회원','2026-04-17',NULL);
/*!40000 ALTER TABLE `member_tbl` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-17 16:23:41
