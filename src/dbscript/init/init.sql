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
   `money` int(11) DEFAULT '0' COMMENT '余额',
   `deposit` int(11) DEFAULT '0' COMMENT '押金',
   `status` int(11) DEFAULT '0' COMMENT '0:有效，1:删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
   `session_key` varchar(32) DEFAULT NULL COMMENT '微信session_key',
   `openid` varchar(64) DEFAULT NULL,
   `unionId` varchar(64) DEFAULT NULL,
   `gender` int(11) DEFAULT NULL COMMENT '值为1时是男性，值为2时是女性，值为0时是未知',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=100000001 DEFAULT CHARSET=utf8mb4 AVG_ROW_LENGTH=1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户';

CREATE TABLE `file` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `key` varchar(32) DEFAULT NULL COMMENT '文件唯一标识',
   `name` varchar(128) DEFAULT NULL COMMENT '本地文件名',
   `file_name` varchar(128) DEFAULT NULL COMMENT '文件名',
   `url` varchar(1024) DEFAULT NULL COMMENT '文件地址',
   `size` int(11) DEFAULT NULL COMMENT '文件大小 KB',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1：删除',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT '0',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT '0',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='文件';

CREATE TABLE `lock_info` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `lock_no` varchar(64) DEFAULT NULL COMMENT '锁编号',
   `state` int(11) DEFAULT '0' COMMENT '0:正常,1:维修中,2:禁用,3:使用中',
   `address` varchar(32) DEFAULT NULL COMMENT '设备所在地址',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1：删除',
   `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='锁信息';

CREATE TABLE `lock_order` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `order_no` varchar(32) DEFAULT NULL COMMENT '订单编号',
   `lock_no` varchar(64) DEFAULT NULL COMMENT '锁编号',
   `start_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '使用开始时间',
   `end_time` timestamp NULL DEFAULT NULL COMMENT '使用结束时间',
   `user_id` int(11) DEFAULT NULL COMMENT '使用用户id',
   `fee` int(11) DEFAULT '0' COMMENT '费用：单位：分',
   `type` int(11) DEFAULT '0' COMMENT '0:未支付，1：已支付，2：免单',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1：删除',
   `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='锁使用记录';

CREATE TABLE `wx_pay_info` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `appid` varchar(64) DEFAULT NULL COMMENT '小程序ID',
   `mch_id` varchar(64) DEFAULT NULL COMMENT '商户号',
   `nonce_str` varchar(64) DEFAULT NULL COMMENT '随机字符串',
   `sign` varchar(64) DEFAULT NULL COMMENT '签名',
   `body` varchar(256) DEFAULT NULL COMMENT '商品描述',
   `out_trade_no` varchar(64) DEFAULT NULL COMMENT '商户订单号',
   `total_fee` int(11) DEFAULT NULL COMMENT '订单总金额，单位为分',
   `spbill_create_ip` varchar(32) DEFAULT NULL COMMENT 'APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。',
   `time_expire` varchar(16) DEFAULT NULL COMMENT '订单失效时间，格式为yyyyMMddHHmmss',
   `notify_url` varchar(512) DEFAULT NULL COMMENT '通知地址',
   `trade_type` varchar(32) DEFAULT NULL COMMENT '交易类型,小程序取值如下：JSAPI',
   `openid` varchar(256) DEFAULT NULL COMMENT '用户标识,trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识',
   `result` varchar(16) DEFAULT NULL COMMENT '支付结果：SUCCESS/FAIL',
   `time_end` varchar(16) DEFAULT NULL COMMENT '支付完成时间，格式为yyyyMMddHHmmss',
   `transaction_id` varchar(64) DEFAULT NULL COMMENT '微信支付订单号',
   `user_id` int(11) DEFAULT NULL COMMENT '用户id',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='微信支付信息';

CREATE TABLE `trans_flow_info` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `type` int(11) DEFAULT '0' COMMENT '0:消费，1：退费，2：押金，3：退押金，4：充值',
   `fee` int(11) DEFAULT '0' COMMENT '金额：单位：分',
   `desc` varchar(256) DEFAULT NULL COMMENT '描述',
   `status` int(11) DEFAULT '0' COMMENT '0:有效，1：删除',
   `user_id` int(11) DEFAULT NULL COMMENT '用户id',
   `insert_author` int(11) DEFAULT NULL,
   `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='交易流水';

CREATE TABLE `fault_feedback` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` int(11) DEFAULT NULL,
   `type` int(11) DEFAULT '0' COMMENT '处理状态，0：未处理，1：已处理',
   `result` varchar(512) DEFAULT NULL COMMENT '处理结果描述',
   `fault_type` int(11) DEFAULT NULL COMMENT '0：其他，1：床坏了，2：锁坏了，3：还床失败',
   `device_no` varchar(64) DEFAULT NULL COMMENT '设备编号',
   `imgs` varchar(512) DEFAULT NULL COMMENT '图片',
   `desc` varchar(512) DEFAULT NULL COMMENT '备注',
   `status` int(11) DEFAULT '0' COMMENT '0：有效，1：删除',
   `insert_author` int(11) DEFAULT NULL,
   `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='故障反馈';

