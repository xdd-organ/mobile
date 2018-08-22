-- 添加新字段：
alter table user add `nickname` varchar(32) DEFAULT NULL COMMENT '昵称';
alter table user add `username` varchar(32) DEFAULT NULL COMMENT '用户名';
alter table user add `session_key` varchar(32) DEFAULT NULL COMMENT '微信session_key';