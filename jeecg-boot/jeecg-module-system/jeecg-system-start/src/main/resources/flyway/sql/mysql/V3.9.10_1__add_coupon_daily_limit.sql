-- 优惠券模块：添加每日限领字段
-- author: elinx
-- date: 20250601

ALTER TABLE `tbl_coupon` ADD COLUMN `daily_limit` int DEFAULT NULL COMMENT '每日限领数量' AFTER `per_limit`;