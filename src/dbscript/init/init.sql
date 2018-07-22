CREATE TABLE `address` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` int(11) DEFAULT NULL COMMENT '用户主键',
   `country` int(11) DEFAULT '86' COMMENT '国家主键',
   `country_name` varchar(32) DEFAULT '中国' COMMENT '国家名称',
   `province` int(11) DEFAULT NULL COMMENT '省主键',
   `province_name` varchar(32) DEFAULT NULL COMMENT '省名称',
   `city` int(11) DEFAULT NULL COMMENT '城市主键',
   `city_name` varchar(32) DEFAULT NULL COMMENT '城市名称',
   `district` int(11) DEFAULT NULL COMMENT '区主键',
   `district_name` varchar(32) DEFAULT NULL COMMENT '区名称',
   `detailed` varchar(64) DEFAULT NULL COMMENT '详细地址',
   `consignee` varchar(32) DEFAULT NULL COMMENT '收货人',
   `telphone` varchar(16) DEFAULT NULL COMMENT '电话',
   `status` int(11) DEFAULT '0' COMMENT '0:有效，1:删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='收货地址';
 
 CREATE TABLE `cash_deposit` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` int(11) DEFAULT NULL COMMENT '缴纳人',
   `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '缴纳时间',
   `amount` int(11) DEFAULT NULL COMMENT '金额',
   `status` int(11) DEFAULT NULL COMMENT '0:有效,1:删除',
   `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='cash_deposit';
 
 CREATE TABLE `category` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `parent_id` int(11) DEFAULT '0' COMMENT '父主键',
   `name` varchar(32) DEFAULT NULL COMMENT '名称',
   `cover` varchar(128) DEFAULT NULL COMMENT '封面',
   `operation` varchar(64) DEFAULT NULL COMMENT '1:增加同级,2:增加子集,3:删除本身,4:上移,5:下移。(逗号分割)',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1：删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='类目管理';
 
 CREATE TABLE `commodity` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `name` varchar(32) DEFAULT NULL COMMENT '商品名称',
   `price` int(11) DEFAULT NULL COMMENT '商品价格(单位：分)',
   `commodity_property` varchar(1024) DEFAULT NULL COMMENT '商品属性',
   `quality_report` varchar(1024) DEFAULT NULL COMMENT '质检报告',
   `is_shelf` int(11) DEFAULT '0' COMMENT '0：未上架，1：上架',
   `sale_status` int(11) DEFAULT '0' COMMENT '0：未出售，1：出售',
   `status` int(11) DEFAULT '0' COMMENT '0：未删除，1：删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='商品管理';
 
 CREATE TABLE `commodity_auction` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `name` varchar(32) DEFAULT NULL COMMENT '商品名称',
   `price` int(11) DEFAULT NULL COMMENT '商品一口价格(单位：分)',
   `cover` varchar(128) DEFAULT NULL COMMENT '封面',
   `carousel_img` varchar(1024) DEFAULT NULL COMMENT '轮播图',
   `market_price` int(11) DEFAULT NULL COMMENT '商品市场价格(单位：分)',
   `commodity_type` varchar(32) DEFAULT NULL COMMENT '商品类型',
   `commodity_property` varchar(1024) DEFAULT NULL COMMENT '商品属性',
   `quality_report` varchar(1024) DEFAULT NULL COMMENT '质检报告',
   `is_shelf` int(11) DEFAULT '0' COMMENT '0：未上架，1：上架',
   `sale_status` int(11) DEFAULT '0' COMMENT '0：未出售，1：出售',
   `status` int(11) DEFAULT '0' COMMENT '0：未删除，1：删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='商品管理-拍卖';
 
 CREATE TABLE `commodity_property` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `property_name` varchar(32) DEFAULT NULL COMMENT '商品属性名称',
   `commodity_type_name` varchar(32) DEFAULT NULL COMMENT '商品类型名称',
   `commodity_type_value` varchar(32) DEFAULT NULL COMMENT '商品类型值',
   `property` varchar(64) DEFAULT NULL COMMENT '属性：1：操作系统，2：网络，3：颜色，4：成色',
   `operation` varchar(64) DEFAULT NULL COMMENT '操作：1：增加，2：删除，3：修改',
   `status` int(11) DEFAULT '0',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='商品属性管理';
 
 CREATE TABLE `dictionary` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `name` varchar(32) DEFAULT NULL COMMENT '字典名称',
   `key` varchar(32) DEFAULT NULL COMMENT '字典key',
   `value` varchar(1024) DEFAULT NULL COMMENT '字典value',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='数据字典';
 
 CREATE TABLE `quality_template` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `template_name` varchar(32) DEFAULT NULL COMMENT '模板名称',
   `commodity_type_name` varchar(32) DEFAULT NULL COMMENT '商品类型名称',
   `commodity_type_value` varchar(32) DEFAULT NULL COMMENT '商品类型值',
   `property` varchar(64) DEFAULT NULL COMMENT '质检项目：1：电源键，2：音量键，3：电池，4：iCloud解除，5：照相机',
   `operation` varchar(64) DEFAULT NULL COMMENT '操作：1：增加，2删除',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1：删除',
   `insert_author` int(11) DEFAULT NULL,
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='质检报告模板管理';
 
 CREATE TABLE `user` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `username` varchar(32) DEFAULT NULL COMMENT '用户名',
   `password` varchar(64) DEFAULT NULL COMMENT '密码',
   `telphone` varchar(16) DEFAULT NULL COMMENT '电话',
   `avatar` varchar(64) DEFAULT NULL COMMENT '头像',
   `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
   `is_disable` int(11) DEFAULT '0' COMMENT '1：禁用，0：启用',
   `status` int(11) DEFAULT '0' COMMENT '0:有效，1:删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户';