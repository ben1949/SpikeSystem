-- 数据库初始化脚本
-- 创建数据库
create DATABASE seckill;

-- 使用数据库
use seckill;

--创建秒杀库存表

create table seckill (
`seckill_id` bigint not NULL auto_increment comment '商品库存id',
`name` varchar(120) not NULL comment '商品名称',
`number` int not NULL comment '库存数量',
`start_time` timestamp not NULL comment '秒杀开启时间',
`end_time` timestamp not NULL comment '秒杀结束时间',
`create_time` timestamp not NULL comment '创建时间',
primary key(seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB auto_increment=1000 default charset=utf8 comment '秒杀库存表';

insert into
    seckill(name,number,start_time,end_time,create_time)
values
    ('1000 iphone6',100,'2017-05-01 00:00:00','2017-05-02 00:00:00',now()),
    ('500 ipad',100,'2017-05-01 00:00:00','2017-05-02 00:00:00',now()),
    ('300 xiaomi4',100,'2017-05-01 00:00:00','2017-05-02 00:00:00',now()),
    ('200 hongminote',100,'2017-05-01 00:00:00','2017-05-02 00:00:00',now());

-- 秒杀成功明细表
-- 用户登录认证相关信息
create table success_killed(
`seckill_id` bigint not NULL comment '秒杀商品id',
`user_phone` bigint not NULL comment '用户手机号',
`state` tinyint not NULL default -1 comment '状态表示-1 无效 0 成功 1 已付款 2 已发货',
`create_time` TIMESTAMP not NULL comment '创建时间',
primary key(seckill_id,user_phone),/*联合主键*/
key idx_create_time(create_time)
)ENGINE=InnoDB default charset=utf8 comment '秒杀成功明细表';


























-- 从数据库中拿出schema
mysql> show create table seckill \G;
*************************** 1. row ***************************
       Table: seckill
Create Table: CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TI
MESTAMP COMMENT '秒杀开启时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束
时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时
间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1009 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表'
1 row in set (0.00 sec)

ERROR:
No query specified

mysql> show create table success_killed\G;
*************************** 1. row ***************************
       Table: success_killed
Create Table: CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态表示-1 无效 0 成功 1 已
付款 2 已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_T
IMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表'
1 row in set (0.00 sec)

ERROR:
No query specified

mysql>


mysql> show create procedure execute_seckill\G;
*************************** 1. row ***************************
           Procedure: execute_seckill
            sql_mode: STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITU
TION
    Create Procedure: CREATE DEFINER=`root`@`localhost` PROCEDURE `execute_secki
ll`(in v_seckill_id bigint,in v_phone bigint,in v_kill_time TIMESTAMP,out r_resu
lt int)
BEGIN
   DECLARE insert_count int DEFAULT  0;
   START TRANSACTION;
   insert ignore into success_killed(seckill_id,user_phone,create_time) values(v
_seckill_id,v_phone,v_kill_time);
     select row_count() into insert_count;
     IF (insert_count = 0) THEN
        ROLLBACK;
        set r_result = -1;
     ELSEIF(insert_count < 0) THEN
        ROLLBACK;
        set r_result = -2;
     ELSE
        update seckill set number = number - 1 WHERE seckill_id = v_seckill_id a
nd end_time > v_kill_time and start_time < v_kill_time and number > 0;
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
END
character_set_client: utf8
collation_connection: utf8_general_ci
  Database Collation: latin1_swedish_ci
1 row in set (0.06 sec)

ERROR:
No query specified


