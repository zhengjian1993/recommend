CREATE TABLE `dim_mine_click_record` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
     `target_id` int(11) DEFAULT NULL COMMENT '目标id',
     `name` varchar(128) DEFAULT NULL COMMENT '操作类型',
     `gmt_create` datetime NOT NULL COMMENT '创建日期',
     `gmt_modified` datetime NOT NULL COMMENT '更新日期',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件埋点数据表';

CREATE TABLE `dim_mine_page_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `target_id` int(11) DEFAULT NULL COMMENT '目标id',
    `start_time` bigint(20) DEFAULT NULL COMMENT '打开时间(毫秒数) ',
    `end_time` bigint(20) DEFAULT NULL COMMENT '关闭时间(毫秒数)',
    `duration` bigint(20) DEFAULT NULL COMMENT '打开时间和关闭时间的差时（毫秒数）',
    `gmt_create` datetime NOT NULL COMMENT '创建日期',
    `gmt_modified` datetime NOT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面停留时长数据表';

CREATE TABLE `dim_target_auto_tag_relation` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `target_id` int(11) NOT NULL COMMENT '文体id',
    `tag_name` varchar(16) NOT NULL COMMENT '标签名字',
    `weight` decimal(12,4) DEFAULT NULL COMMENT '标签权重',
    `status` tinyint(4) DEFAULT '1' COMMENT '是否有效：0 无效 1有效',
    `gmt_create` datetime NOT NULL COMMENT '创建日期',
    `gmt_modified` datetime NOT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文体标签关联表（基于自动分词得到）';
