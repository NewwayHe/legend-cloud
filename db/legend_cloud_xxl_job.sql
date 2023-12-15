/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.17 : Database - legend_cloud_xxl_job
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`legend_cloud_xxl_job` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `legend_cloud_xxl_job`;

/*Table structure for table `xxl_job_group` */

DROP TABLE IF EXISTS `xxl_job_group`;

CREATE TABLE `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_group` */

insert  into `xxl_job_group`(`id`,`app_name`,`title`,`address_type`,`address_list`,`update_time`) values 
(18,'task-job-executor','定时任务服务',0,'http://10.0.1.61:9998/','2023-12-14 10:00:27');

/*Table structure for table `xxl_job_info` */

DROP TABLE IF EXISTS `xxl_job_info`;

CREATE TABLE `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_info` */

insert  into `xxl_job_info`(`id`,`job_group`,`job_desc`,`add_time`,`update_time`,`author`,`alarm_email`,`schedule_type`,`schedule_conf`,`misfire_strategy`,`executor_route_strategy`,`executor_handler`,`executor_param`,`executor_block_strategy`,`executor_timeout`,`executor_fail_retry_count`,`glue_type`,`glue_source`,`glue_remark`,`glue_updatetime`,`child_jobid`,`trigger_status`,`trigger_last_time`,`trigger_next_time`) values 
(15,18,'优惠券到点下线','2020-11-05 14:52:38','2023-11-17 13:05:17','admin','','CRON','0 0 3 1/1 1/1 ?','DO_NOTHING','FIRST','couponOffLine','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2020-11-05 14:52:38','',1,1702494000000,1702580400000),
(16,18,'优惠券到点上线','2020-11-05 14:52:49','2023-11-17 13:05:02','admin','','CRON','0 0 3 1/1 1/1 ?','DO_NOTHING','FIRST','couponOnLine','','SERIAL_EXECUTION',0,1,'BEAN','','GLUE代码初始化','2020-11-05 14:52:49','',1,1702494000000,1702580400000),
(17,18,'用户优惠券到点下线','2020-11-05 14:52:58','2023-11-17 13:04:48','admin','','CRON','0 0 3 1/1 1/1 ?','DO_NOTHING','FIRST','userCouponInvalid','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2020-11-05 14:52:58','',1,1702494000000,1702580400000),
(18,18,'用户优惠券到点上线','2020-11-05 14:53:06','2023-11-17 13:04:27','admin','','CRON','0 0 3 1/1 1/1 ?','DO_NOTHING','FIRST','userCouponValid','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2020-11-05 14:53:06','',1,1702494000000,1702580400000),
(19,18,'账单结算','2020-11-27 14:01:06','2022-08-23 17:36:19','jzh','','NONE',NULL,'DO_NOTHING','FIRST','calculateBill','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2020-11-27 14:01:06','',0,0,0),
(32,18,'同步用户地址','2021-06-16 21:41:59','2021-06-16 21:41:59','jzh','','NONE',NULL,'DO_NOTHING','FIRST','userAddress','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-06-16 21:41:59','',0,0,0),
(33,18,'用户数据统计','2021-06-18 15:03:34','2021-07-13 17:54:23','wendong','','NONE',NULL,'DO_NOTHING','FIRST','setUserAmount','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-06-18 15:03:34','',0,0,0),
(34,18,'百度统计归档','2021-06-19 14:36:56','2021-07-13 17:54:15','jzh','','NONE',NULL,'DO_NOTHING','FIRST','baiduStatisticsArchive','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-06-19 14:36:56','',0,0,0),
(35,18,'百度移动统计token刷新','2021-06-21 15:31:52','2021-06-28 15:10:05','jzh','','NONE',NULL,'DO_NOTHING','FIRST','baiduViewRefreshToken','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-06-21 15:31:52','',0,0,0),
(37,18,'缺货率定时任务','2021-06-28 10:59:29','2021-07-13 17:54:29','jzh','','NONE',NULL,'DO_NOTHING','FIRST','shopOutStockRate','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-06-28 10:59:29','',0,0,0),
(38,18,'用户购买力数据表数据清洗（只需要执行一次）','2021-06-29 17:59:04','2021-06-29 17:59:04','jzh','','NONE',NULL,'DO_NOTHING','FIRST','userPurchasingDataCleaning','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-06-29 17:59:04','',0,0,0),
(39,18,'营销活动统计定时器','2021-07-06 09:57:25','2021-07-06 09:57:41','wendong','','NONE',NULL,'DO_NOTHING','FIRST','setActivityCollect','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-07-06 09:57:25','',0,0,0),
(40,18,'初始化平台,商家根目录','2021-07-15 20:33:27','2021-07-15 20:36:41','admin','','NONE',NULL,'DO_NOTHING','FIRST','attachmentInit','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-07-15 20:33:27','',0,0,0),
(43,18,'通知商家未处理订单售后数量','2021-09-01 15:43:12','2023-11-23 11:37:28','杨灿','','NONE','','DO_NOTHING','FIRST','shopMessageSend','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2021-09-01 15:43:12','',0,0,0),
(47,18,'处理支付成功但处于待支付的订单','2022-03-24 09:41:59','2022-03-24 09:41:59','jzh','','NONE',NULL,'DO_NOTHING','FIRST','paySuccessCompensate','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-03-24 09:41:59','',0,0,0),
(51,18,'处理用户钱包中间表','2022-04-29 13:52:49','2022-04-29 13:52:49','jzh','','NONE',NULL,'DO_NOTHING','FIRST','dealWithUserWalletCentre','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-04-29 13:52:49','',0,0,0),
(53,18,'延时队列到点执行','2022-05-10 09:32:25','2022-05-10 09:32:31','jzh','','NONE',NULL,'DO_NOTHING','FIRST','execAmqpTask','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-05-10 09:32:25','',0,0,0),
(58,18,'自动收货定时器','2022-08-23 17:37:19','2022-08-23 17:39:46','xwp','','NONE',NULL,'DO_NOTHING','FIRST','autoConfirmReceipt','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-08-23 17:37:19','',0,0,0);

/*Table structure for table `xxl_job_lock` */

DROP TABLE IF EXISTS `xxl_job_lock`;

CREATE TABLE `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_lock` */

insert  into `xxl_job_lock`(`lock_name`) values 
('schedule_lock');

/*Table structure for table `xxl_job_log` */

DROP TABLE IF EXISTS `xxl_job_log`;

CREATE TABLE `xxl_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB AUTO_INCREMENT=281169 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_log` */

/*Table structure for table `xxl_job_log_report` */

DROP TABLE IF EXISTS `xxl_job_log_report`;

CREATE TABLE `xxl_job_log_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=771 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_log_report` */

/*Table structure for table `xxl_job_logglue` */

DROP TABLE IF EXISTS `xxl_job_logglue`;

CREATE TABLE `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_logglue` */

/*Table structure for table `xxl_job_registry` */

DROP TABLE IF EXISTS `xxl_job_registry`;

CREATE TABLE `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB AUTO_INCREMENT=1378705 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_registry` */

insert  into `xxl_job_registry`(`id`,`registry_group`,`registry_key`,`registry_value`,`update_time`) values 
(1378704,'EXECUTOR','task-job-executor','http://10.0.1.61:9998/','2023-12-14 10:00:36');

/*Table structure for table `xxl_job_user` */

DROP TABLE IF EXISTS `xxl_job_user`;

CREATE TABLE `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `xxl_job_user` */

insert  into `xxl_job_user`(`id`,`username`,`password`,`role`,`permission`) values 
(1,'admin','21232f297a57a5a743894a0e4a801fc3',1,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
