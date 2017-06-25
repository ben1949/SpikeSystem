-- MySQL dump 10.13  Distrib 5.5.39, for Win64 (x86)
--
-- Host: localhost    Database: seckill
-- ------------------------------------------------------
-- Server version	5.5.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `seckill`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `seckill` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `seckill`;

--
-- Table structure for table `seckill`
--

DROP TABLE IF EXISTS `seckill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀开启时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1009 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seckill`
--

LOCK TABLES `seckill` WRITE;
/*!40000 ALTER TABLE `seckill` DISABLE KEYS */;
INSERT INTO `seckill` VALUES (1000,'1000 iphone6',93,'2017-06-11 12:01:41','2020-12-30 16:00:00','2017-05-21 02:21:23'),(1001,'500 ipad',93,'2017-06-18 07:38:30','2020-12-30 16:00:00','2017-05-21 02:21:23'),(1002,'300 xiaomi4',97,'2017-06-11 07:27:09','2020-12-30 16:00:00','2017-05-21 02:21:23'),(1003,'200 hongminote',98,'2017-06-11 11:53:13','2020-12-30 16:00:00','2017-05-21 02:21:23'),(1004,'2000 iphone7',94,'2017-06-11 07:27:32','2020-12-30 16:00:00','2017-05-21 11:31:14'),(1005,'1000 iphone6',100,'2017-04-30 16:00:00','2017-05-01 16:00:00','2017-06-18 07:46:26'),(1006,'500 ipad',100,'2017-04-30 16:00:00','2017-05-01 16:00:00','2017-06-18 07:46:26'),(1007,'300 xiaomi4',100,'2017-04-30 16:00:00','2017-05-01 16:00:00','2017-06-18 07:46:26'),(1008,'200 hongminote',100,'2017-04-30 16:00:00','2017-05-01 16:00:00','2017-06-18 07:46:26');
/*!40000 ALTER TABLE `seckill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `success_killed`
--

DROP TABLE IF EXISTS `success_killed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态表示-1 无效 0 成功 1 已付款 2 已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `success_killed`
--

LOCK TABLES `success_killed` WRITE;
/*!40000 ALTER TABLE `success_killed` DISABLE KEYS */;
INSERT INTO `success_killed` VALUES (1000,13533221122,-1,'2017-06-11 12:01:41'),(1001,13730655837,-1,'2017-06-18 07:38:30');
/*!40000 ALTER TABLE `success_killed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'seckill'
--
/*!50003 DROP PROCEDURE IF EXISTS `execute_seckill` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `execute_seckill`(in v_seckill_id bigint,in v_phone bigint,in v_kill_time TIMESTAMP,out r_result int)
BEGIN
   DECLARE insert_count int DEFAULT  0;
   START TRANSACTION;
   insert ignore into success_killed(seckill_id,user_phone,create_time) values(v_seckill_id,v_phone,v_kill_time);
     select row_count() into insert_count;
     IF (insert_count = 0) THEN
        ROLLBACK;
        set r_result = -1;
     ELSEIF(insert_count < 0) THEN
        ROLLBACK;
        set r_result = -2;
     ELSE
        update seckill set number = number - 1 WHERE seckill_id = v_seckill_id and end_time > v_kill_time and start_time < v_kill_time and number > 0;
        select row_count() into insert_count;
        IF(insert_count = 0) THEN
          ROLLBACK;
          set r_result = 0;
        ELSEIF (insert_count < 0) THEN
         ROLLBACK;
         set r_result = -2;
        ELSE
         COMMIT;
         set r_result = 1;
        END IF;
     END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-25 14:27:20
