-- 添加新字段：
alter table `lock_info` add `qr_code_no` varchar(64) DEFAULT NULL COMMENT '二维码编号';
alter table `lock_info` add `device_no` varchar(64) DEFAULT NULL COMMENT '床编号';
alter table `lock_info` add `latitude` varchar(16) DEFAULT NULL COMMENT '纬度';
alter table `lock_info` add `longitude` varchar(16) DEFAULT NULL COMMENT '经度';
alter table `lock_info` add `lock_pwd` varchar(12) DEFAULT NULL COMMENT '锁密码';
alter table `lock_info` add `lock_key` varchar(32) DEFAULT NULL COMMENT '锁秘钥';
alter table `lock_info` add `lock_mac` varchar(32) DEFAULT NULL COMMENT '锁mac地址';
alter table `lock_info` add `hospital` varchar(32) DEFAULT NULL COMMENT '所属医院';
alter table `lock_info` add `user_id` int(11) DEFAULT NULL COMMENT '所属用户(即经销商)';
alter table `lock_info` add `type` int(11) DEFAULT 0 COMMENT '0:蓝牙,1:GPS';

alter table `user` add `score` int(11) DEFAULT 0 COMMENT '分';
alter table `user` add `type` int(11) DEFAULT 0 COMMENT '0:普通用户，1：后台管理员，2：经销商管理员';

alter table `lock_order` add `total_time` varchar(16) DEFAULT 0 COMMENT '使用总时间（秒）';