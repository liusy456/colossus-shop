/*
Navicat MySQL Data Transfer

Source Server         : 47.52.174.241
Source Server Version : 50720
Source Host           : 47.52.174.241:3306
Source Database       : colossus_shop

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-12-13 15:35:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '品牌名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='品牌表';

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` varchar(32) NOT NULL,
  `sku_id` varchar(32) DEFAULT NULL,
  `spu_id` varchar(32) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `shop_id` varchar(32) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_count` int(5) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类表';

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) NOT NULL,
  `sku_id` varchar(32) DEFAULT NULL,
  `spu_id` varchar(32) DEFAULT NULL,
  `shop_id` varchar(32) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_count` int(5) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for logistics
-- ----------------------------
DROP TABLE IF EXISTS `logistics`;
CREATE TABLE `logistics` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) DEFAULT NULL,
  `logistics_company_id` varchar(32) DEFAULT NULL,
  `logistics_company_no` varchar(32) DEFAULT NULL,
  `tracking_no` varchar(55) DEFAULT NULL,
  `delivery_time` datetime DEFAULT NULL,
  `arrival_time` datetime DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for logistics_tracking
-- ----------------------------
DROP TABLE IF EXISTS `logistics_tracking`;
CREATE TABLE `logistics_tracking` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) DEFAULT NULL,
  `logistics_company_no` varchar(32) DEFAULT NULL,
  `tracking_no` varchar(32) DEFAULT NULL,
  `remark` varchar(5000) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) DEFAULT NULL,
  `item_count` int(5) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `shop_id` varchar(32) DEFAULT NULL,
  `pay_type` varchar(10) DEFAULT NULL,
  `order_time` datetime DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  `trade_no` varchar(32) DEFAULT NULL,
  `ship_type` varchar(10) DEFAULT NULL,
  `
expect_arrive_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `ship_amount` decimal(10,2) DEFAULT NULL,
  `actual_amount` decimal(10,2) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pay_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_log`;
CREATE TABLE `pay_log` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) DEFAULT NULL,
  `pay_type` varchar(10) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `actual_amount` decimal(10,2) DEFAULT NULL,
  `remark` varchar(5000) DEFAULT NULL,
  `pay_info` varchar(5000) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` varchar(32) NOT NULL,
  `sku_id` varchar(32) DEFAULT NULL,
  `spu_id` varchar(32) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `like_count` int(5) DEFAULT NULL,
  `comment` longtext,
  `star` decimal(2,1) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for safe_guard
-- ----------------------------
DROP TABLE IF EXISTS `safe_guard`;
CREATE TABLE `safe_guard` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '保障名称',
  `price` decimal(9,2) NOT NULL COMMENT '保障价格',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='增值保障';

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '店铺名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='店铺表';

-- ----------------------------
-- Table structure for sku
-- ----------------------------
DROP TABLE IF EXISTS `sku`;
CREATE TABLE `sku` (
  `id` varchar(32) NOT NULL,
  `sku_no` varchar(50) NOT NULL COMMENT 'sku编号,唯一',
  `name` varchar(50) NOT NULL COMMENT 'sku名称(冗余spu_name)',
  `price` decimal(9,2) NOT NULL COMMENT '售价',
  `stock` int(11) NOT NULL COMMENT '库存',
  `shop_id` varchar(32) NOT NULL COMMENT '商铺id,为0表示自营',
  `spu_id` varchar(32) NOT NULL COMMENT 'spu_id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sku表';

-- ----------------------------
-- Table structure for sku_safe_guard
-- ----------------------------
DROP TABLE IF EXISTS `sku_safe_guard`;
CREATE TABLE `sku_safe_guard` (
  `id` varchar(32) NOT NULL,
  `sku_id` varchar(32) NOT NULL COMMENT 'sku_id',
  `safe_guard_id` varchar(32) NOT NULL COMMENT 'safeguard_id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sku增值保障';

-- ----------------------------
-- Table structure for sku_spec
-- ----------------------------
DROP TABLE IF EXISTS `sku_spec`;
CREATE TABLE `sku_spec` (
  `id` varchar(32) NOT NULL,
  `spu_id` varchar(32) NOT NULL COMMENT 'sku_id',
  `spec_value_id` varchar(32) NOT NULL COMMENT '规格值id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sku规格值';

-- ----------------------------
-- Table structure for spec
-- ----------------------------
DROP TABLE IF EXISTS `spec`;
CREATE TABLE `spec` (
  `id` varchar(32) NOT NULL,
  `spec_no` varchar(50) NOT NULL COMMENT '规格编号',
  `name` varchar(50) NOT NULL COMMENT '规格名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='规格表';

-- ----------------------------
-- Table structure for spec_value
-- ----------------------------
DROP TABLE IF EXISTS `spec_value`;
CREATE TABLE `spec_value` (
  `id` varchar(32) NOT NULL,
  `spec_id` varchar(32) NOT NULL COMMENT '规格id',
  `spec_value` varchar(50) NOT NULL COMMENT '规格值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='规格值表';

-- ----------------------------
-- Table structure for spu
-- ----------------------------
DROP TABLE IF EXISTS `spu`;
CREATE TABLE `spu` (
  `id` varchar(32) NOT NULL,
  `spu_no` varchar(50) NOT NULL COMMENT '商品编号，唯一',
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `low_price` decimal(9,2) NOT NULL COMMENT '最低售价',
  `category_id` varchar(32) NOT NULL COMMENT '分类id',
  `brand_id` varchar(32) NOT NULL COMMENT '品牌id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_spu_no` (`spu_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='spu表';

-- ----------------------------
-- Table structure for spu_spec
-- ----------------------------
DROP TABLE IF EXISTS `spu_spec`;
CREATE TABLE `spu_spec` (
  `id` varchar(32) NOT NULL,
  `spu_id` varchar(32) NOT NULL COMMENT 'spu_id',
  `spec_id` varchar(32) NOT NULL COMMENT 'spec_id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='spu规格表';
