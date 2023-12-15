/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.17 : Database - legend_cloud_nacos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`legend_cloud_nacos` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `legend_cloud_nacos`;

/*Table structure for table `config_info` */

DROP TABLE IF EXISTS `config_info`;

CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/*Data for the table `config_info` */

insert  into `config_info`(`id`,`data_id`,`group_id`,`content`,`md5`,`gmt_create`,`gmt_modified`,`src_user`,`src_ip`,`app_name`,`tenant_id`,`c_desc`,`c_use`,`effect`,`type`,`c_schema`,`encrypted_data_key`) values 
(102,'application-dev.yml','DEFAULT_GROUP','useLocalCache: false\njasypt:\n  encryptor:\n    password: legendshop\nspring: \n  main: \n    allow-circular-references: true\n  autoconfigure:\n    exclude: org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration\n  cloud:\n    config:\n      allow-override: true\n      override-none: true\n      override-system-properties: false\n    sentinel:\n      eager: true\n      filter:\n        url-patterns: /** \n      transport:\n        dashboard: ${SENTINEL_HOST:http://localhost}:${SENTINEL_PORT:5020}\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    type: com.alibaba.druid.pool.DruidDataSource\n    username: root\n    password: Ls@12345678@\n    url: jdbc:mysql://192.168.0.142:3386/${DATABASE:legend_cloud}?Unicode=true&characterEncoding=UTF-8\n    druid:\n      initial-size: 1\n      max-active: 2000\n      min-idle: 3\n      max-wait: 60000\n      pool-prepared-statements: true\n      max-pool-prepared-statement-per-connection-size: 20\n      filters: stat,wall,slf4j\n      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000\n      # 配置监控服务器\n      stat-view-servlet:\n        # 这里一定要配置true,不配置则是使用默认值false,空白页面\n        enabled: true\n        reset-enable: false\n        url-pattern: /druid/*\n        allow: \"\"\n  servlet:\n    multipart:\n      max-file-size: 30MB\n      max-request-size: 30MB\n  data:    \n    redis:\n      host: ${REDIS_HOST:192.168.0.141}\n      port: ${REDIS_PORT:6379}\n      password: ${REDIS_PASSWORD:12345GESAFsfdtewS4G6789}\n      database: ${REDIS_DATABASE:9}\n      expired-time-type: random\n      timeout: 10s\n      lettuce:\n        pool:\n          max-active: 8\n          max-wait: -1\n          max-idle: 8\n          min-idle: 0\n    redisson:\n      config:\n        singleServerConfig:\n          address: \"redis://192.168.0.141:6379\"\n          password: 12345GESAFsfdtewS4G6789\n  ## 指定sprig cache 类型        \n  cache:\n    type: redis\n\n  rabbitmq:\n    host: 192.168.0.13\n    port: 5672\n    virtual-host: ${VIRTUAL_HOST:/legend-cloud}\n    username: admin\n    password: admin@123\n    template:\n      receive-timeout: 3000\n      reply-timeout: 3000\n      retry:\n        enabled: false\n    listener:\n      simple:\n        acknowledge-mode: manual\n        prefetch: 1 #每次处理1条消息\n        retry.max-attempts: 3 # 最大重试次数\n        retry.enabled: true # 是否开启消费者重试（为false时关闭消费者重试，这时消费端代码异常会一直重复收到消息）\n        retry.initial-interval: 2000 # 重试间隔时间（单位毫秒）\n        retry.max-interval: 20000ms #最大间隔时间\n        retry.multiplier: 2 # 乘子  重试间隔*乘子得出下次重试间隔  3s  6s  12s  24s  此处24s>20s  走20s\n        default-requeue-rejected: false # 重试次数超过上面的设置之后是否丢弃（false不丢弃时需要写相应代码将该消息加入死信队列）\n\n  boot:\n    admin:\n      client:\n        # 注册中心地址\n        url:\n          - http://localhost:5001\n  application:\n    name: monitor-client\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  # 注意此处使用，访问的时候必须访问/actuator/prometheus,如不配置则访问/prometheus\n  #      base-path: /actuator\n  metrics:\n    tags:\n      application: ${spring.application.name}\n      \n  endpoint:\n    health:\n      show-details: ALWAYS\n#feign clinet配置\nfeign:\n  sentinel:\n    enabled: false\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n    max-connections: 50\n    time-to-live: 600 \n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n#ribbon配置\nribbon:\n  ReadTimeout: 10000\n  ConnectTimeout: 10000\n  \n#legendshop的配置\nlegendshop:\n  global:\n    environment:\n      ## debug: true 不真实发送短信\n      debug: true\n      sqlDebug: true\n  xss:\n    enabled: false\n    path-patterns:\n      - /s/**\n      - /p/**\n  swagger:\n    title: legend-cloud接口文档\n    licenseUrl:  https://code.legendmall.cn\n    terms-of-service-url:  https://code.legendmall.cn\n  common:\n    # 后端服务\n    service-domain-name: ${SERVICE_DOMAIN_NAME:http://192.168.0.112:9999}\n    # 用户前端PC端域名\n    user-pc-domain-name: ${USER_PC_DOMAIN_NAME:http://192.168.0.104:8080}\n    # 商家前端域名\n    shop-pc-domain-name: ${SHOP_PC_DOMAIN_NAME:http://192.168.0.104:8087}\n    # 后台服务域名\n    admin-pc-domain-name: ${ADMIN_PC_DOMAIN_NAME:http://192.168.0.104:8086}\n    # 用户前端移动端域名\n    user-mobile-domain-name: ${USER_MOBILE_DOMAIN_NAME:http://192.168.0.104:8080}\n    # 图片服务器\n    photo-server: http://192.168.0.16:9000/legend-cloud/\n    # 静态图片服务器\n    static-server: http://192.168.0.13:9000/miniprogram/\n  oss:\n    enable: true\n    endpoint: http://192.168.0.16:9000\n    access-key: legendshop\n    secret-key: legendshop\n    bucket-name: legend-cloud\n    private-bucket-name: legend-cloud-private\n  captcha:\n    cache-type: redis\n    type: BLOCKPUZZLE\n  sms:\n    sms-type: aliyun\n    aliyun:\n      access-key: xxxxxxxxx\n      secret-key: xxxxxxxxx\n      sign-name: xxxxxxxxxx\n  job:\n    enabled: true\n    admin:\n      address: ${JOB_ADDRESSES:http://192.168.0.16:8800}\n    executor:\n      ip: \'\'\n      logpath: /data/applogs/xxl-job/jobhandler\n      logretentiondays: -1\n\nseata:\n  enabled: ${SEATA_ENABLED:false}\n  tx-service-group: my_test_tx_group\n  config:\n    type: nacos\n    nacos:\n      server-addr: ${spring.cloud.nacos.discovery.server-addr}\n      namespace: seata\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWORD:nacos}     \n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${spring.cloud.nacos.discovery.server-addr}\n      namespace: seata\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWORD:nacos}     \n  service:\n    vgroup-mapping:\n      my_test_tx_group: my_test_tx_group\n    grouplist:\n      default: ${NACOS_HOST:192.168.0.16}:${NACOS_PORT:8868}\n\n## 百度统计相关配置      \nBaiDu:\n  BD_USERNAME: xxxxx\n  BD_PASSWORD: xxxxx\n  SITE_ID: xxxxx\n  BD_TOKEN: xxxx\n  BD_AK:\n  BD_TJ_URL: https://api.baidu.com/json/tongji/v1/ReportService/getData\n  BD_DT_URL:\n  PROJECT_START_DATE:\n  MINI_APP_KEY: xxxxx\n  APP_APP_KEY: xxxxxx\n  BD_MOBILE_TJ_URL: https://openapi.baidu.com/rest/2.0/mtj/svc/app/getDataByKey\n\n# Swagger配置\nspringdoc:\n  swagger-ui:\n    path: /swagger-ui.html\n    tags-sorter: alpha\n    operations-sorter: alpha\n  api-docs:\n    path: /v3/api-docs\n\nknife4j:\n  enable: true # 开启增强模式\n  basic:\n    enable: true\n    username: admin\n    password: admin\n  production: false # 开启生产环境屏蔽','e50a9ff75983bcc6683b3083510f8971','2023-12-13 20:34:25','2023-12-14 18:10:34','nacos','192.168.0.112','','legend-cloud','','','','yaml','',''),
(103,'basic-degraderule-rule','DEFAULT_GROUP','[\r\n    {\r\n        \"resource\": \"basic\",\r\n        \"count\": 1,\r\n        \"grade\": 0,\r\n        \"timeWindow\": 1,\r\n        \"minRequestAmount\": 1,\r\n        \"slowRatioThreshold\": 1\r\n    }\r\n]','127e6513305172769c0ce4d7e7da6f01','2023-12-13 20:34:25','2023-12-13 20:34:25',NULL,'192.168.0.112','','legend-cloud',NULL,NULL,NULL,'text',NULL,''),
(104,'legendshop-basic-service-dev.yml','DEFAULT_GROUP','legendshop:  \n  basic:\n    sensword:\n      enable: true\n    allowed-file-types:\n      - JPEG\n      - GIF\n      - PNG\n      - TIFF\n      - WEBP\n      - BMP\n      - WAV\n      - WMA\n      - RA\n      - MKA\n      - APE\n      - AC3\n      - AAC\n      - OGG\n      - FLAC\n      - LPCM\n      - PCM\n      - M4A\n      - RAM\n      - DTS\n      - AMR\n      - MID\n      - MIDI\n      - AIF\n      - AIFF\n      - AU\n      - VQF\n      - SND\n      - MP3\n      - AVI\n      - MPG\n      - MPEG\n      - DAT\n      - VOB\n      - MP4\n      - 3GP\n      - 3G2\n      - 3GPP\n      - WMV\n      - ASF\n      - RM\n      - RMVB\n      - M2TS\n      - MOV\n      - MKV\n      - TS\n      - TP\n      - TRP\n      - M2T\n      - M2P\n      - M4V\n      - MTS\n      - MOD\n      - WEBM\n      - FLV\n      - F4V\n      - SWF\n      - ISO\n      - ASX\n      - DIVX\n      - PS\n      - OGM\n      - QT\n      - MQV\n      - JPG','cfaa7444d0c3868e8996c2c6f7183586','2023-12-13 20:34:25','2023-12-13 20:37:15','nacos','192.168.0.112','','legend-cloud','','','','yaml','',''),
(106,'legendshop-id-service-dev.yml','DEFAULT_GROUP','leaf:\n  name:  idService\n  segment:\n    enable: true\n  jdbc:\n    url: jdbc:mysql://192.168.0.142:3386/legend_cloud?Unicode=true&characterEncoding=UTF-8\n    username: root\n    password: Ls@12345678@\n  snowflake:\n    enable: false\n    zk:\n      address: 192.168.0.16\n    port: 2181\n\nspring:\n  freemarker:\n    cache: false\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    expose-request-attributes: true\n    expose-session-attributes: true\n    request-context-attribute: request\n    template-loader-path: classpath:/templates/\n    suffix: .ftl','006146d0fe4e9e157a60af10d9a89f9e','2023-12-13 20:34:25','2023-12-14 17:31:03','nacos','192.168.0.112','','legend-cloud','','','','yaml','',''),
(107,'legendshop-product-service-dev.yml','DEFAULT_GROUP','elasticsearch:\n  host: 192.168.0.16:9200\n\nlegendshop:\n  search:\n    productIndexName: dev70_code_sr1_new_product\n    shopIndexName: dev70_code_sr1_new_shop\n    activityIndexName: dev70_code_sr1_new_activity\n    couponIndexName: dev70_code_sr1_new_coupon\n    host: ${elasticsearch.host}\n    ## 生产需要放开\n    # username: elastic\n    # password: 5gcHxmqzHmz92vOM8Zvm\n\nspring: \n  elasticsearch:\n    uris: ${elasticsearch.host}\n  ','571e21732fb6b6ae9590cb3453b187cc','2023-12-13 20:34:25','2023-12-13 20:34:25',NULL,'192.168.0.112','','legend-cloud',NULL,NULL,NULL,'yaml',NULL,''),
(109,'transport.type','SEATA_GROUP','TCP','b136ef5f6a01d816991fe3cf7a6ac763','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(110,'transport.server','SEATA_GROUP','NIO','b6d9dfc0fb54277321cebc0fff55df2f','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(111,'transport.heartbeat','SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(112,'transport.enableClientBatchSendRequest','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(113,'transport.threadFactory.bossThreadPrefix','SEATA_GROUP','NettyBoss','0f8db59a3b7f2823f38a70c308361836','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(114,'transport.threadFactory.workerThreadPrefix','SEATA_GROUP','NettyServerNIOWorker','a78ec7ef5d1631754c4e72ae8a3e9205','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(115,'transport.threadFactory.serverExecutorThreadPrefix','SEATA_GROUP','NettyServerBizHandler','11a36309f3d9df84fa8b59cf071fa2da','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(116,'transport.threadFactory.shareBossWorker','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(117,'transport.threadFactory.clientSelectorThreadPrefix','SEATA_GROUP','NettyClientSelector','cd7ec5a06541e75f5a7913752322c3af','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(118,'transport.threadFactory.clientSelectorThreadSize','SEATA_GROUP','1','c4ca4238a0b923820dcc509a6f75849b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(119,'transport.threadFactory.clientWorkerThreadPrefix','SEATA_GROUP','NettyClientWorkerThread','61cf4e69a56354cf72f46dc86414a57e','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(120,'transport.threadFactory.bossThreadSize','SEATA_GROUP','1','c4ca4238a0b923820dcc509a6f75849b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(121,'transport.threadFactory.workerThreadSize','SEATA_GROUP','default','c21f969b5f03d33d43e04f8f136e7682','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(122,'transport.shutdown.wait','SEATA_GROUP','3','eccbc87e4b5ce2fe28308fd9f2a7baf3','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(123,'service.vgroupMapping.my_test_tx_group','SEATA_GROUP','default','c21f969b5f03d33d43e04f8f136e7682','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(124,'service.default.grouplist','SEATA_GROUP','192.168.0.66:8091','e63dfa1a2e4e9f8367b8087f7e82009b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(125,'service.enableDegrade','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(126,'service.disableGlobalTransaction','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(127,'client.rm.asyncCommitBufferLimit','SEATA_GROUP','10000','b7a782741f667201b54880c925faec4b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(128,'client.rm.lock.retryInterval','SEATA_GROUP','10','d3d9446802a44259755d38e6d163e820','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(129,'client.rm.lock.retryTimes','SEATA_GROUP','30','34173cb38f07f89ddbebc2ac9128303f','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(130,'client.rm.lock.retryPolicyBranchRollbackOnConflict','SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(131,'client.rm.reportRetryCount','SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(132,'client.rm.tableMetaCheckEnable','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(133,'client.rm.sqlParserType','SEATA_GROUP','druid','3d650fb8a5df01600281d48c47c9fa60','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(134,'client.rm.reportSuccessEnable','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(135,'client.rm.sagaBranchRegisterEnable','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(136,'client.tm.commitRetryCount','SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(137,'client.tm.rollbackRetryCount','SEATA_GROUP','5','e4da3b7fbbce2345d7772b0674a318d5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(138,'client.tm.defaultGlobalTransactionTimeout','SEATA_GROUP','60000','2b4226dd7ed6eb2d419b881f3ae9c97c','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(139,'client.tm.degradeCheck','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(140,'client.tm.degradeCheckAllowTimes','SEATA_GROUP','10','d3d9446802a44259755d38e6d163e820','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(141,'client.tm.degradeCheckPeriod','SEATA_GROUP','2000','08f90c1a417155361a5c4b8d297e0d78','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(142,'store.mode','SEATA_GROUP','file','8c7dd922ad47494fc02c388e12c00eac','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(143,'store.file.dir','SEATA_GROUP','file_store/data','6a8dec07c44c33a8a9247cba5710bbb2','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(144,'store.file.maxBranchSessionSize','SEATA_GROUP','16384','c76fe1d8e08462434d800487585be217','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(145,'store.file.maxGlobalSessionSize','SEATA_GROUP','512','10a7cdd970fe135cf4f7bb55c0e3b59f','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(146,'store.file.fileWriteBufferCacheSize','SEATA_GROUP','16384','c76fe1d8e08462434d800487585be217','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(147,'store.file.flushDiskMode','SEATA_GROUP','async','0df93e34273b367bb63bad28c94c78d5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(148,'store.file.sessionReloadReadSize','SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(149,'store.db.datasource','SEATA_GROUP','druid','3d650fb8a5df01600281d48c47c9fa60','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(150,'store.db.dbType','SEATA_GROUP','mysql','81c3b080dad537de7e10e0987a4bf52e','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(151,'store.db.driverClassName','SEATA_GROUP','com.mysql.cj.jdbc.Driver','33763409bb7f4838bde4fae9540433e4','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(152,'store.db.url','SEATA_GROUP','jdbc:mysql://192.168.0.17:3306/legend_cloud_seata?useUnicode=true','4347fc5bb676df6086cf9efbb6e82be6','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(153,'store.db.user','SEATA_GROUP','root','63a9f0ea7bb98050796b649e85481845','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(154,'store.db.password','SEATA_GROUP','Ls@12345678@','380e8f34f5ed81203e25f1d6ef121e07','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(155,'store.db.minConn','SEATA_GROUP','15','9bf31c7ff062936a96d3c8bd1f8f2ff3','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(156,'store.db.maxConn','SEATA_GROUP','300','94f6d7e04a4d452035300f18b984988c','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(157,'store.db.globalTable','SEATA_GROUP','global_table','8b28fb6bb4c4f984df2709381f8eba2b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(158,'store.db.branchTable','SEATA_GROUP','branch_table','54bcdac38cf62e103fe115bcf46a660c','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(159,'store.db.queryLimit','SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(160,'store.db.lockTable','SEATA_GROUP','lock_table','55e0cae3b6dc6696b768db90098b8f2f','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(161,'store.db.maxWait','SEATA_GROUP','10000','b7a782741f667201b54880c925faec4b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(162,'store.redis.host','SEATA_GROUP','127.0.0.1','f528764d624db129b32c21fbca0cb8d6','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(163,'store.redis.port','SEATA_GROUP','6379','92c3b916311a5517d9290576e3ea37ad','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(164,'store.redis.maxConn','SEATA_GROUP','10','d3d9446802a44259755d38e6d163e820','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(165,'store.redis.minConn','SEATA_GROUP','1','c4ca4238a0b923820dcc509a6f75849b','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(166,'store.redis.database','SEATA_GROUP','0','cfcd208495d565ef66e7dff9f98764da','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(167,'store.redis.password','SEATA_GROUP','null','37a6259cc0c1dae299a7866489dff0bd','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(168,'store.redis.queryLimit','SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(169,'server.recovery.committingRetryPeriod','SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(170,'server.recovery.asynCommittingRetryPeriod','SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(171,'server.recovery.rollbackingRetryPeriod','SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(172,'server.recovery.timeoutRetryPeriod','SEATA_GROUP','1000','a9b7ba70783b617e9998dc4dd82eb3c5','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(173,'server.maxCommitRetryTimeout','SEATA_GROUP','-1','6bb61e3b7bce0931da574d19d1d82c88','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(174,'server.maxRollbackRetryTimeout','SEATA_GROUP','-1','6bb61e3b7bce0931da574d19d1d82c88','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(175,'server.rollbackRetryTimeoutUnlockEnable','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(176,'client.undo.dataValidation','SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(177,'client.undo.logSerialization','SEATA_GROUP','jackson','b41779690b83f182acc67d6388c7bac9','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(178,'client.undo.onlyCareUpdateColumns','SEATA_GROUP','true','b326b5062b2f0e69046810717534cb09','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(179,'server.undo.logSaveDays','SEATA_GROUP','7','8f14e45fceea167a5a36dedd4bea2543','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(180,'server.undo.logDeletePeriod','SEATA_GROUP','86400000','f4c122804fe9076cb2710f55c3c6e346','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(181,'client.undo.logTable','SEATA_GROUP','undo_log','2842d229c24afe9e61437135e8306614','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(182,'client.log.exceptionRate','SEATA_GROUP','100','f899139df5e1059396431415e770c6dd','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(183,'transport.serialization','SEATA_GROUP','seata','b943081c423b9a5416a706524ee05d40','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(184,'transport.compressor','SEATA_GROUP','none','334c4a4c42fdb79d7ebc3e73b517e6f8','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(185,'metrics.enabled','SEATA_GROUP','false','68934a3e9455fa72420237eb05902327','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(186,'metrics.registryType','SEATA_GROUP','compact','7cf74ca49c304df8150205fc915cd465','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(187,'metrics.exporterList','SEATA_GROUP','prometheus','e4f00638b8a10e6994e67af2f832d51c','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,''),
(188,'metrics.exporterPrometheusPort','SEATA_GROUP','9898','7b9dc501afe4ee11c56a4831e20cee71','2023-12-14 09:39:50','2023-12-14 09:39:50',NULL,'192.168.0.112','','seata',NULL,NULL,NULL,'text',NULL,'');

/*Table structure for table `config_info_aggr` */

DROP TABLE IF EXISTS `config_info_aggr`;

CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

/*Data for the table `config_info_aggr` */

/*Table structure for table `config_info_beta` */

DROP TABLE IF EXISTS `config_info_beta`;

CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/*Data for the table `config_info_beta` */

/*Table structure for table `config_info_tag` */

DROP TABLE IF EXISTS `config_info_tag`;

CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/*Data for the table `config_info_tag` */

/*Table structure for table `config_tags_relation` */

DROP TABLE IF EXISTS `config_tags_relation`;

CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/*Data for the table `config_tags_relation` */

/*Table structure for table `group_capacity` */

DROP TABLE IF EXISTS `group_capacity`;

CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/*Data for the table `group_capacity` */

/*Table structure for table `his_config_info` */

DROP TABLE IF EXISTS `his_config_info`;

CREATE TABLE `his_config_info` (
  `id` bigint(20) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

/*Data for the table `his_config_info` */

/*Table structure for table `permissions` */

DROP TABLE IF EXISTS `permissions`;

CREATE TABLE `permissions` (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `permissions` */

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `roles` */

insert  into `roles`(`username`,`role`) values 
('nacos','ROLE_ADMIN');

/*Table structure for table `tenant_capacity` */

DROP TABLE IF EXISTS `tenant_capacity`;

CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

/*Data for the table `tenant_capacity` */

/*Table structure for table `tenant_info` */

DROP TABLE IF EXISTS `tenant_info`;

CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

/*Data for the table `tenant_info` */

insert  into `tenant_info`(`id`,`kp`,`tenant_id`,`tenant_name`,`tenant_desc`,`create_source`,`gmt_create`,`gmt_modified`) values 
(5,'1','legend-cloud','legend-cloud','legend cloud空间','nacos',1702470852968,1702470852968),
(6,'1','seata','seata','seata','nacos',1702517981814,1702517981814);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `users` */

insert  into `users`(`username`,`password`,`enabled`) values 
('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
