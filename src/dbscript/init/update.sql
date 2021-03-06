﻿-- 添加新字段：
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
alter table `lock_info` add `unit_price` int(11) DEFAULT 300 COMMENT '锁单位价格，单位：分/小时';
alter table `lock_info` add `battery` int(11) DEFAULT 0 COMMENT '锁剩余电量';
alter table `lock_info` add `wifi` int(11) DEFAULT 0 COMMENT '锁信号';
alter table `lock_info` add `bind_user` varchar(128) DEFAULT null COMMENT '最多绑定8个用户';
alter table `lock_info` add `department` varchar(32) DEFAULT null COMMENT '科室';
alter table `lock_info` add `line` varchar(32) DEFAULT null COMMENT '设备是否在线 1:在线，0:离线';

alter table `user` add `score` int(11) DEFAULT 0 COMMENT '分';
alter table `user` add `type` int(11) DEFAULT 0 COMMENT '0:普通用户，1：后台管理员，2：经销商管理员';

alter table `lock_order` add `total_time` varchar(16) DEFAULT 0 COMMENT '使用总时间（秒）';
alter table `lock_order` add `diff_fee` varchar(16) DEFAULT 0 COMMENT '需要补的钱';