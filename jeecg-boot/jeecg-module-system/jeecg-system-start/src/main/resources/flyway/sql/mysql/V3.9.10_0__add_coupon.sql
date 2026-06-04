-- 优惠券模块建表脚本
-- author: elinx
-- date: 20250601

-- 创建优惠券表
DROP TABLE IF EXISTS `tbl_coupon`;
CREATE TABLE `tbl_coupon` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `coupon_name` varchar(100) DEFAULT NULL COMMENT '优惠券名称',
  `coupon_type` int DEFAULT NULL COMMENT '优惠券类型: 1-满减券, 2-折扣券, 3-兑换券',
  `discount_value` decimal(10,2) DEFAULT NULL COMMENT '折扣金额或折扣率',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低消费金额',
  `description` varchar(500) DEFAULT NULL COMMENT '优惠券描述',
  `valid_start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `total_count` int DEFAULT NULL COMMENT '发放总数量',
  `remain_count` int DEFAULT NULL COMMENT '剩余数量',
  `used_count` int DEFAULT NULL COMMENT '已使用数量',
  `per_limit` int DEFAULT NULL COMMENT '每人限领数量',
  `daily_limit` int DEFAULT NULL COMMENT '每日限领数量',
  `status` int DEFAULT '0' COMMENT '状态: 0-未发布, 1-已发布, 2-已下架',
  `sort` int DEFAULT '0' COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券表';

-- 创建用户优惠券表
DROP TABLE IF EXISTS `tbl_user_coupon`;
CREATE TABLE `tbl_user_coupon` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `coupon_id` bigint DEFAULT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(100) DEFAULT NULL COMMENT '优惠券名称',
  `coupon_type` int DEFAULT NULL COMMENT '优惠券类型: 1-满减券, 2-折扣券, 3-兑换券',
  `discount_value` decimal(10,2) DEFAULT NULL COMMENT '折扣金额或折扣率',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低消费金额',
  `valid_start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `obtain_type` int DEFAULT NULL COMMENT '获取方式: 1-主动领取, 2-系统发放, 3-活动奖励',
  `status` int DEFAULT '0' COMMENT '状态: 0-未使用, 1-已使用, 2-已过期, 3-已作废',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `order_id` varchar(36) DEFAULT NULL COMMENT '订单ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_user_status` (`user_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券表';

-- 插入测试数据
INSERT INTO `tbl_coupon` (`id`, `create_time`, `update_time`, `coupon_name`, `coupon_type`, `discount_value`, `min_amount`, `description`, `valid_start_time`, `valid_end_time`, `total_count`, `remain_count`, `used_count`, `per_limit`, `daily_limit`, `status`, `sort`, `remark`) VALUES
(1, NOW(), NOW(), '新人专享满减券', 1, 10.00, 50.00, '新人专享，满50减10', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1000, 1000, 0, 1, 1, 1, 1, '测试优惠券'),
(2, NOW(), NOW(), '95折折扣券', 2, 0.95, 100.00, '满100元可享95折', NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), 500, 500, 0, 2, 2, 1, 2, '测试折扣券');