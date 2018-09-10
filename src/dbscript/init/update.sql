-- 添加新字段：
alter table `lock_info` add `qr_code_no` varchar(64) DEFAULT NULL COMMENT '二维码编号';
alter table `lock_info` add `device_no` varchar(64) DEFAULT NULL COMMENT '床编号';
alter table `lock_info` add `latitude` varchar(16) DEFAULT NULL COMMENT '纬度';
alter table `lock_info` add `longitude` varchar(16) DEFAULT NULL COMMENT '经度';

alter table `user` add `score` int(11) DEFAULT 0 COMMENT '分';