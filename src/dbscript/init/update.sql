-- 添加新字段：
alter table user add `nickname` varchar(32) DEFAULT NULL COMMENT '昵称';
alter table user add `gender` int(11) DEFAULT NULL COMMENT '值为1时是男性，值为2时是女性，值为0时是未知';
alter table user add `session_key` varchar(32) DEFAULT NULL COMMENT '微信session_key';