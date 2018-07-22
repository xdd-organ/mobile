CREATE TABLE `address` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `user_id` int(11) DEFAULT NULL COMMENT '�û�����',
   `country` int(11) DEFAULT '86' COMMENT '��������',
   `country_name` varchar(32) DEFAULT '�й�' COMMENT '��������',
   `province` int(11) DEFAULT NULL COMMENT 'ʡ����',
   `province_name` varchar(32) DEFAULT NULL COMMENT 'ʡ����',
   `city` int(11) DEFAULT NULL COMMENT '��������',
   `city_name` varchar(32) DEFAULT NULL COMMENT '��������',
   `district` int(11) DEFAULT NULL COMMENT '������',
   `district_name` varchar(32) DEFAULT NULL COMMENT '������',
   `detailed` varchar(64) DEFAULT NULL COMMENT '��ϸ��ַ',
   `consignee` varchar(32) DEFAULT NULL COMMENT '�ջ���',
   `telphone` varchar(16) DEFAULT NULL COMMENT '�绰',
   `status` int(11) DEFAULT '0' COMMENT '0:��Ч��1:ɾ��',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='�ջ���ַ';
 
 CREATE TABLE `cash_deposit` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `user_id` int(11) DEFAULT NULL COMMENT '������',
   `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
   `amount` int(11) DEFAULT NULL COMMENT '���',
   `status` int(11) DEFAULT NULL COMMENT '0:��Ч,1:ɾ��',
   `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='cash_deposit';
 
 CREATE TABLE `category` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `parent_id` int(11) DEFAULT '0' COMMENT '������',
   `name` varchar(32) DEFAULT NULL COMMENT '����',
   `cover` varchar(128) DEFAULT NULL COMMENT '����',
   `operation` varchar(64) DEFAULT NULL COMMENT '1:����ͬ��,2:�����Ӽ�,3:ɾ������,4:����,5:���ơ�(���ŷָ�)',
   `status` int(11) DEFAULT '0' COMMENT '0����Ч��1��ɾ��',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='��Ŀ����';
 
 CREATE TABLE `commodity` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `name` varchar(32) DEFAULT NULL COMMENT '��Ʒ����',
   `price` int(11) DEFAULT NULL COMMENT '��Ʒ�۸�(��λ����)',
   `commodity_property` varchar(1024) DEFAULT NULL COMMENT '��Ʒ����',
   `quality_report` varchar(1024) DEFAULT NULL COMMENT '�ʼ챨��',
   `is_shelf` int(11) DEFAULT '0' COMMENT '0��δ�ϼܣ�1���ϼ�',
   `sale_status` int(11) DEFAULT '0' COMMENT '0��δ���ۣ�1������',
   `status` int(11) DEFAULT '0' COMMENT '0��δɾ����1��ɾ��',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='��Ʒ����';
 
 CREATE TABLE `commodity_auction` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `name` varchar(32) DEFAULT NULL COMMENT '��Ʒ����',
   `price` int(11) DEFAULT NULL COMMENT '��Ʒһ�ڼ۸�(��λ����)',
   `cover` varchar(128) DEFAULT NULL COMMENT '����',
   `carousel_img` varchar(1024) DEFAULT NULL COMMENT '�ֲ�ͼ',
   `market_price` int(11) DEFAULT NULL COMMENT '��Ʒ�г��۸�(��λ����)',
   `commodity_type` varchar(32) DEFAULT NULL COMMENT '��Ʒ����',
   `commodity_property` varchar(1024) DEFAULT NULL COMMENT '��Ʒ����',
   `quality_report` varchar(1024) DEFAULT NULL COMMENT '�ʼ챨��',
   `is_shelf` int(11) DEFAULT '0' COMMENT '0��δ�ϼܣ�1���ϼ�',
   `sale_status` int(11) DEFAULT '0' COMMENT '0��δ���ۣ�1������',
   `status` int(11) DEFAULT '0' COMMENT '0��δɾ����1��ɾ��',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='��Ʒ����-����';
 
 CREATE TABLE `commodity_property` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `property_name` varchar(32) DEFAULT NULL COMMENT '��Ʒ��������',
   `commodity_type_name` varchar(32) DEFAULT NULL COMMENT '��Ʒ��������',
   `commodity_type_value` varchar(32) DEFAULT NULL COMMENT '��Ʒ����ֵ',
   `property` varchar(64) DEFAULT NULL COMMENT '���ԣ�1������ϵͳ��2�����磬3����ɫ��4����ɫ',
   `operation` varchar(64) DEFAULT NULL COMMENT '������1�����ӣ�2��ɾ����3���޸�',
   `status` int(11) DEFAULT '0',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='��Ʒ���Թ���';
 
 CREATE TABLE `dictionary` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `name` varchar(32) DEFAULT NULL COMMENT '�ֵ�����',
   `key` varchar(32) DEFAULT NULL COMMENT '�ֵ�key',
   `value` varchar(1024) DEFAULT NULL COMMENT '�ֵ�value',
   `status` int(11) DEFAULT '0' COMMENT '0����Ч��1ɾ��',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='�����ֵ�';
 
 CREATE TABLE `quality_template` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `template_name` varchar(32) DEFAULT NULL COMMENT 'ģ������',
   `commodity_type_name` varchar(32) DEFAULT NULL COMMENT '��Ʒ��������',
   `commodity_type_value` varchar(32) DEFAULT NULL COMMENT '��Ʒ����ֵ',
   `property` varchar(64) DEFAULT NULL COMMENT '�ʼ���Ŀ��1����Դ����2����������3����أ�4��iCloud�����5�������',
   `operation` varchar(64) DEFAULT NULL COMMENT '������1�����ӣ�2ɾ��',
   `status` int(11) DEFAULT '0' COMMENT '0����Ч��1��ɾ��',
   `insert_author` int(11) DEFAULT NULL,
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='�ʼ챨��ģ�����';
 
 CREATE TABLE `user` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����',
   `username` varchar(32) DEFAULT NULL COMMENT '�û���',
   `password` varchar(64) DEFAULT NULL COMMENT '����',
   `telphone` varchar(16) DEFAULT NULL COMMENT '�绰',
   `avatar` varchar(64) DEFAULT NULL COMMENT 'ͷ��',
   `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'ע��ʱ��',
   `is_disable` int(11) DEFAULT '0' COMMENT '1�����ã�0������',
   `status` int(11) DEFAULT '0' COMMENT '0:��Ч��1:ɾ��',
   `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `insert_author` int(11) DEFAULT NULL,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_author` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='�û�';