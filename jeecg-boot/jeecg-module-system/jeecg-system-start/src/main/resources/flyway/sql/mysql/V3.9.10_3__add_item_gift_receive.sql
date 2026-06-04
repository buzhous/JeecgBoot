CREATE TABLE `tbl_item_gift_receive` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `gift_id` varchar(64) DEFAULT NULL COMMENT '赠送记录ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '领取者用户ID',
  `quantity` int DEFAULT NULL COMMENT '领取数量',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_gift_id` (`gift_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物品赠送领取记录';