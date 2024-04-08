/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : zedc1ang_mall

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 30/03/2024 10:26:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE,
  INDEX `ix_log_created`(`log_created` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for zedc1ang_mall_cart
-- ----------------------------
DROP TABLE IF EXISTS `zedc1ang_mall_cart`;
CREATE TABLE `zedc1ang_mall_cart`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `product_id` int NOT NULL COMMENT '商品id',
  `user_id` int NOT NULL COMMENT '用户id',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `selected` int NOT NULL DEFAULT 1 COMMENT '是否已勾选：0代表未勾选，1代表已勾选',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zedc1ang_mall_cart
-- ----------------------------

-- ----------------------------
-- Table structure for zedc1ang_mall_category
-- ----------------------------
DROP TABLE IF EXISTS `zedc1ang_mall_category`;
CREATE TABLE `zedc1ang_mall_category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '分类目录名称',
  `type` int NOT NULL COMMENT '分类目录级别，例如1代表一级，2代表二级，3代表三级',
  `parent_id` int NOT NULL COMMENT '父id，也就是上一级目录的id，如果是一级目录，那么父id为0',
  `order_num` int NOT NULL COMMENT '目录展示时的排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zedc1ang_mall_category
-- ----------------------------
INSERT INTO `zedc1ang_mall_category` VALUES (3, '新鲜水果', 1, 0, 1, '2019-12-18 01:17:00', '2019-12-28 17:11:26');
INSERT INTO `zedc1ang_mall_category` VALUES (4, '橘子橙子', 2, 3, 1, '2019-12-18 01:17:00', '2019-12-28 16:25:10');
INSERT INTO `zedc1ang_mall_category` VALUES (5, '海鲜水产', 1, 0, 2, '2019-12-18 01:17:00', '2019-12-28 16:25:20');
INSERT INTO `zedc1ang_mall_category` VALUES (6, '精选肉类', 1, 0, 3, '2019-12-18 01:17:00', '2019-12-28 16:25:21');
INSERT INTO `zedc1ang_mall_category` VALUES (7, '螃蟹', 2, 5, 1, '2019-12-18 01:17:00', '2019-12-28 16:25:15');
INSERT INTO `zedc1ang_mall_category` VALUES (8, '鱼类', 2, 5, 2, '2019-12-18 01:17:00', '2019-12-28 16:25:16');
INSERT INTO `zedc1ang_mall_category` VALUES (9, '冷饮冻食', 1, 0, 4, '2019-12-20 13:45:28', '2019-12-28 16:25:22');
INSERT INTO `zedc1ang_mall_category` VALUES (10, '蔬菜蛋品', 1, 0, 5, '2019-12-20 13:45:28', '2019-12-28 16:25:23');
INSERT INTO `zedc1ang_mall_category` VALUES (11, '草莓', 2, 3, 2, '2019-12-18 01:17:00', '2019-12-28 15:44:42');
INSERT INTO `zedc1ang_mall_category` VALUES (12, '奇异果', 2, 3, 3, '2019-12-18 01:17:00', '2019-12-28 16:25:12');
INSERT INTO `zedc1ang_mall_category` VALUES (13, '海参', 2, 5, 3, '2019-12-18 01:17:00', '2019-12-28 16:25:17');
INSERT INTO `zedc1ang_mall_category` VALUES (14, '车厘子', 2, 3, 4, '2019-12-18 01:17:00', '2019-12-28 16:25:12');
INSERT INTO `zedc1ang_mall_category` VALUES (15, '火锅食材', 2, 27, 5, '2019-12-18 01:17:00', '2020-02-11 00:42:33');
INSERT INTO `zedc1ang_mall_category` VALUES (16, '牛羊肉', 2, 6, 1, '2019-12-18 01:17:00', '2019-12-28 16:25:18');
INSERT INTO `zedc1ang_mall_category` VALUES (17, '冰淇淋', 2, 9, 1, '2019-12-18 01:17:00', '2019-12-28 16:25:18');
INSERT INTO `zedc1ang_mall_category` VALUES (18, '蔬菜综合', 2, 10, 1, '2019-12-18 01:17:00', '2020-02-11 00:48:27');
INSERT INTO `zedc1ang_mall_category` VALUES (19, '果冻橙', 3, 4, 1, '2019-12-18 01:17:00', '2020-02-11 00:37:02');
INSERT INTO `zedc1ang_mall_category` VALUES (27, '美味菌菇', 1, 0, 7, '2019-12-20 13:45:28', '2020-02-10 23:20:36');
INSERT INTO `zedc1ang_mall_category` VALUES (28, '其他水果', 2, 3, 4, '2019-12-18 01:17:00', '2019-12-28 16:25:12');

-- ----------------------------
-- Table structure for zedc1ang_mall_order
-- ----------------------------
DROP TABLE IF EXISTS `zedc1ang_mall_order`;
CREATE TABLE `zedc1ang_mall_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '订单号（非主键id）',
  `user_id` int NOT NULL COMMENT '用户id',
  `total_price` int NOT NULL COMMENT '订单总价格',
  `receiver_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名快照',
  `receiver_mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人手机号快照',
  `receiver_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '收货地址快照',
  `order_status` int NOT NULL DEFAULT 10 COMMENT '订单状态: 0用户已取消，10未付款（初始状态），20已付款，30已发货，40交易完成',
  `postage` int NULL DEFAULT 0 COMMENT '运费，默认为0',
  `payment_type` int NOT NULL DEFAULT 1 COMMENT '支付类型,1-在线支付',
  `delivery_time` timestamp NULL DEFAULT NULL COMMENT '发货时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '交易完成时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表;' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zedc1ang_mall_order
-- ----------------------------
INSERT INTO `zedc1ang_mall_order` VALUES (1, '2023122817021410000', 11, 2763, '小慕', '18888888888', '中国慕城', 40, 0, 1, '2023-12-28 21:55:35', '2023-12-28 21:29:53', '2023-12-28 22:01:48', '2023-12-28 17:02:14', '2023-12-28 17:02:14');
INSERT INTO `zedc1ang_mall_order` VALUES (4, '2023122820145210000', 11, 1143, '小慕', '18888888888', '中国慕城', 0, 0, 1, NULL, NULL, '2023-12-28 20:48:35', '2023-12-28 20:14:52', '2023-12-28 20:14:52');
INSERT INTO `zedc1ang_mall_order` VALUES (5, '2023122822033310000', 11, 222, '小慕', '18888888888', '中国慕城', 0, 0, 1, NULL, NULL, '2023-12-28 22:04:01', '2023-12-28 22:03:33', '2023-12-28 22:03:33');
INSERT INTO `zedc1ang_mall_order` VALUES (6, '2023122822045310000', 11, 699, '小慕', '18888888888', '中国慕城', 40, 0, 1, '2023-12-28 22:08:58', '2023-12-28 22:06:55', '2023-12-28 22:09:21', '2023-12-28 22:04:53', '2023-12-28 22:04:53');
INSERT INTO `zedc1ang_mall_order` VALUES (7, '2024032808584452981', 13, 444, '小慕', '18888888888', '中国慕城', 40, 0, 1, '2024-03-28 01:10:47', '2024-03-28 01:08:15', '2024-03-28 01:11:47', '2024-03-28 08:58:44', '2024-03-28 08:58:44');
INSERT INTO `zedc1ang_mall_order` VALUES (8, '2024032809090643086', 13, 222, '小慕', '18888888888', '中国慕城', 0, 0, 1, NULL, NULL, '2024-03-28 01:09:52', '2024-03-28 09:09:06', '2024-03-28 09:09:06');

-- ----------------------------
-- Table structure for zedc1ang_mall_order_item
-- ----------------------------
DROP TABLE IF EXISTS `zedc1ang_mall_order_item`;
CREATE TABLE `zedc1ang_mall_order_item`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '归属订单id',
  `product_id` int NOT NULL COMMENT '商品id',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `product_img` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品图片',
  `unit_price` int NOT NULL COMMENT '单价（下单时的快照）',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `total_price` int NOT NULL DEFAULT 0 COMMENT '商品总价',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单的商品表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zedc1ang_mall_order_item
-- ----------------------------
INSERT INTO `zedc1ang_mall_order_item` VALUES (1, '2023122817021410000', 22, '即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G', 'http://111.231.103.117:8081/images/haishen.jpg', 699, 3, 2097, '2023-12-28 17:02:14', '2023-12-28 17:02:14');
INSERT INTO `zedc1ang_mall_order_item` VALUES (2, '2023122817021410000', 27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://111.231.103.117:8081/images/yangrouchuan.jpg', 222, 3, 666, '2023-12-28 17:02:14', '2023-12-28 17:02:14');
INSERT INTO `zedc1ang_mall_order_item` VALUES (5, '2023122820145210000', 22, '即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G', 'http://111.231.103.117:8081/images/haishen.jpg', 699, 1, 699, '2023-12-28 20:14:52', '2023-12-28 20:14:52');
INSERT INTO `zedc1ang_mall_order_item` VALUES (6, '2023122820145210000', 27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://111.231.103.117:8081/images/yangrouchuan.jpg', 222, 2, 444, '2023-12-28 20:14:52', '2023-12-28 20:14:52');
INSERT INTO `zedc1ang_mall_order_item` VALUES (7, '2023122822033310000', 27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://111.231.103.117:8081/images/yangrouchuan.jpg', 222, 1, 222, '2023-12-28 22:03:33', '2023-12-28 22:03:33');
INSERT INTO `zedc1ang_mall_order_item` VALUES (8, '2023122822045310000', 22, '即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G', 'http://111.231.103.117:8081/images/haishen.jpg', 699, 1, 699, '2023-12-28 22:04:53', '2023-12-28 22:04:53');
INSERT INTO `zedc1ang_mall_order_item` VALUES (9, '2024032808584452981', 27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://111.231.103.117:8081/images/yangrouchuan.jpg', 222, 2, 444, '2024-03-28 08:58:44', '2024-03-28 08:58:44');
INSERT INTO `zedc1ang_mall_order_item` VALUES (10, '2024032809090643086', 27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://111.231.103.117:8081/images/yangrouchuan.jpg', 222, 1, 222, '2024-03-28 09:09:06', '2024-03-28 09:09:06');

-- ----------------------------
-- Table structure for zedc1ang_mall_product
-- ----------------------------
DROP TABLE IF EXISTS `zedc1ang_mall_product`;
CREATE TABLE `zedc1ang_mall_product`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品主键id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '产品图片,相对路径地址',
  `detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品详情',
  `category_id` int NOT NULL COMMENT '分类id',
  `price` int NOT NULL COMMENT '价格,单位-分',
  `stock` int NOT NULL COMMENT '库存数量',
  `status` int NOT NULL DEFAULT 1 COMMENT '商品上架状态：0-下架，1-上架',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zedc1ang_mall_product
-- ----------------------------
INSERT INTO `zedc1ang_mall_product` VALUES (2, '澳洲进口大黑车厘子大樱桃包甜黑樱桃大果多汁 500g 特大果', 'http://111.231.103.117:8081/images/chelizi2.jpg', '商品毛重：1.0kg货号：608323093445原产地：智利类别：美早热卖时间：1月，11月，12月国产/进口：进口售卖方式：单品', 14, 50, 100, 1, '2019-12-18 16:08:15', '2024-03-27 13:17:34');
INSERT INTO `zedc1ang_mall_product` VALUES (3, '茶树菇 美味菌菇 东北山珍 500g', 'http://111.231.103.117:8081/images/chashugu.jpg', '商品名：茶树菇 商品特点：美味菌菇 东北山珍 500g', 15, 1000, 6, 1, '2019-12-18 16:10:50', '2024-03-27 13:17:34');
INSERT INTO `zedc1ang_mall_product` VALUES (14, 'Zespri佳沛 新西兰阳光金奇异果 6个装', 'http://111.231.103.117:8081/images/mihoutao2.jpg', '商品编号：4635056商品毛重：0.71kg商品产地：新西兰类别：金果包装：简装国产/进口：进口原产地：新西兰', 12, 39, 77, 1, '2019-12-18 16:11:13', '2020-02-10 23:36:48');
INSERT INTO `zedc1ang_mall_product` VALUES (17, '红颜奶油草莓 约重500g/20-24颗 新鲜水果', 'http://111.231.103.117:8081/images/caomei2.jpg', '商品毛重：0.58kg商品产地：丹东/南通/武汉类别：红颜草莓包装：简装国产/进口：国产', 11, 99, 84, 1, '2019-12-18 16:11:13', '2020-02-10 23:37:48');
INSERT INTO `zedc1ang_mall_product` VALUES (21, '智利原味三文鱼排（大西洋鲑）240g/袋 4片装', 'http://111.231.103.117:8081/images/sanwenyu2.jpg', '商品毛重：260.00g商品产地：中国大陆保存状态：冷冻国产/进口：进口包装：简装类别：三文鱼海水/淡水：海水烹饪建议：煎炸，蒸菜，烧烤原产地：智利', 8, 499, 1, 1, '2019-12-28 15:13:07', '2020-02-10 23:38:46');
INSERT INTO `zedc1ang_mall_product` VALUES (22, '即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G', 'http://111.231.103.117:8081/images/haishen.jpg', '商品毛重：1.5kg商品产地：中国大陆贮存条件：冷冻重量：50-99g国产/进口：国产适用场景：养生滋补包装：袋装原产地：辽宁年限：9年以上等级：特级食品工艺：冷冻水产热卖时间：9月类别：即食海参固形物含量：70%-90%特产品类：大连海参售卖方式：单品', 13, 699, 48, 1, '2019-12-28 15:16:29', '2023-12-28 17:07:14');
INSERT INTO `zedc1ang_mall_product` VALUES (23, '澳大利亚直采鲜橙 精品澳橙12粒 单果130-180g', 'http://111.231.103.117:8081/images/chengzi.jpg', '商品毛重：2.27kg商品产地：澳大利亚类别：脐橙包装：简装国产/进口：进口原产地：澳大利亚', 4, 12, 12, 1, '2019-12-28 16:02:13', '2020-02-11 00:40:15');
INSERT INTO `zedc1ang_mall_product` VALUES (24, '智利帝王蟹礼盒装4.4-4.0斤/只 生鲜活鲜熟冻大螃蟹', 'http://111.231.103.117:8081/images/diwangxie.jpg', '商品毛重：3.0kg商品产地：智利大闸蟹售卖方式：公蟹重量：2000-4999g套餐份量：5人份以上国产/进口：进口海水/淡水：海水烹饪建议：火锅，炒菜，烧烤，刺身，加热即食包装：简装原产地：智利保存状态：冷冻公单蟹重：5.5两及以上分类：帝王蟹特产品类：其它售卖方式：单品', 7, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:05:05');
INSERT INTO `zedc1ang_mall_product` VALUES (25, '新疆库尔勒克伦生无籽红提 国产新鲜红提葡萄 提子 5斤装', 'http://111.231.103.117:8081/images/hongti.jpg', '商品毛重：2.5kg商品产地：中国大陆货号：XZL201909002重量：2000-3999g套餐份量：2人份国产/进口：国产是否有机：非有机单箱规格：3个装，4个装，5个装类别：红提包装：简装原产地：中国大陆售卖方式：单品', 28, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:44:05');
INSERT INTO `zedc1ang_mall_product` VALUES (26, '越南进口红心火龙果 4个装 红肉中果 单果约330-420g', 'http://111.231.103.117:8081/images/hongxinhuolongguo.jpg', '商品毛重：1.79kg商品产地：越南重量：1000-1999g类别：红心火龙果包装：简装国产/进口：进口', 28, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:44:11');
INSERT INTO `zedc1ang_mall_product` VALUES (27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://111.231.103.117:8081/images/yangrouchuan.jpg', '商品毛重：0.585kg商品产地：内蒙古巴彦淖尔市保存状态：冷冻重量：500-999g套餐份量：3人份国产/进口：国产烹饪建议：烧烤原产地：内蒙古品种：其它热卖时间：4月，5月，6月，7月，8月，9月，10月，11月，12月饲养方式：圈养类别：羊肉串包装：简装套餐周期：12个月', 16, 222, 216, 1, '2019-12-28 16:06:34', '2020-02-11 00:11:30');
INSERT INTO `zedc1ang_mall_product` VALUES (28, '玛琪摩尔新西兰进口冰淇淋大桶装', 'http://111.231.103.117:8081/images/bingqilin.jpg', '商品毛重：1.04kg商品产地：新西兰国产/进口：进口包装：量贩装', 17, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:10:40');
INSERT INTO `zedc1ang_mall_product` VALUES (29, '西兰花沙拉菜 350g 甜玉米粒 青豆豌豆 胡萝卜冷冻方便蔬菜', 'http://111.231.103.117:8081/images/shalacai.jpg', '商品毛重：370.00g商品产地：浙江宁波重量：500g以下套餐份量：家庭装类别：速冻玉米/豌豆包装：简装烹饪建议：炒菜，炖菜，煎炸，蒸菜售卖方式：单品', 18, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:34:01');
INSERT INTO `zedc1ang_mall_product` VALUES (36, '四川果冻橙 吹弹可破', 'http://111.231.103.117:8081/images/guodongcheng.jpg', '商品毛重：370.00g商品产地：四川 重量：1000g', 19, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:38:14');
INSERT INTO `zedc1ang_mall_product` VALUES (37, '进口牛油果 中果6粒装 单果约130-160g ', 'http://111.231.103.117:8081/images/niuyouguo.jpg', '商品名称：京觅进口牛油果 6个装商品编号：3628240商品毛重：1.2kg商品产地：秘鲁、智利、墨西哥重量：1000g以下国产/进口：进口', 28, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:47:42');
INSERT INTO `zedc1ang_mall_product` VALUES (38, '中街1946网红雪糕冰淇淋', 'http://111.231.103.117:8081/images/bingqilin2.jpg', '商品名称：中街1946网红雪糕冰淇淋乐享系列半巧*5牛乳*5阿棕*2冰激凌冷饮冰棍冰棒商品编号：52603405444店铺： 中街1946官方旗舰店商品毛重：1.3kg商品产地：中国大陆国产/进口：国产包装：量贩装售卖方式：组合', 17, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:50:54');
INSERT INTO `zedc1ang_mall_product` VALUES (39, '福建六鳌红薯5斤', 'http://111.231.103.117:8081/images/hongshu.jpg', '商品名称：京觅福建六鳌红薯5斤商品编号：4087121商品毛重：2.8kg商品产地：福建省漳浦县六鳌镇重量：2500g及以上烹饪建议：煎炸，蒸菜，烧烤包装：简装分类：地瓜/红薯售卖方式：单品', 18, 40, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:51:59');
INSERT INTO `zedc1ang_mall_product` VALUES (40, '胡萝卜', 'http://111.231.103.117:8081/images/huluobo.jpg', '商品名称：绿鲜知胡萝卜商品编号：4116192商品毛重：1.07kg商品产地：北京包装：简装分类：萝卜烹饪建议：火锅，炒菜，炖菜', 18, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:53:25');
INSERT INTO `zedc1ang_mall_product` VALUES (41, '羊肉卷 内蒙羔羊肉 鲜嫩 500g/袋 首农出品 羊排肉卷 火锅食材', 'http://111.231.103.117:8081/images/yangroujuan.jpg', '商品名称：首食惠羊排片商品编号：4836347商品毛重：0.51kg商品产地：辽宁省大连市保存状态：冷冻品种：其它国产/进口：进口饲养方式：散养类别：羊肉片/卷包装：简装烹饪建议：火锅，炒菜，炖菜原产地：新西兰', 16, 222, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:48:03');
INSERT INTO `zedc1ang_mall_product` VALUES (42, '甜玉米 切好 香甜', 'http://111.231.103.117:8081/images/tianyumi.jpg', '品牌： 绿鲜知（greenseer）\n商品名称：绿鲜知甜玉米商品编号：4983604商品毛重：1.1kg商品产地：云南玉溪类别：玉米', 18, 240, 222, 1, '2019-12-28 16:06:34', '2020-02-11 00:52:19');

-- ----------------------------
-- Table structure for zedc1ang_mall_user
-- ----------------------------
DROP TABLE IF EXISTS `zedc1ang_mall_user`;
CREATE TABLE `zedc1ang_mall_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码，MD5加密',
  `personalized_signature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '个性签名',
  `role` int NOT NULL DEFAULT 1 COMMENT '角色，1-普通用户，2-管理员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zedc1ang_mall_user
-- ----------------------------
INSERT INTO `zedc1ang_mall_user` VALUES (1, '1', '1', '666', 1, '2019-12-16 02:37:33', '2023-12-19 10:11:04');
INSERT INTO `zedc1ang_mall_user` VALUES (2, 'xiaomu', 'AWRuqaxc6iryhHuA4OnFag==', '更新了我的签名', 2, '2019-12-17 15:11:32', '2020-02-10 09:52:12');
INSERT INTO `zedc1ang_mall_user` VALUES (3, '你好', 'AWRuqaxc6iryhHuA4OnFag==', '', 1, '2019-12-20 13:41:03', '2020-02-10 09:52:15');
INSERT INTO `zedc1ang_mall_user` VALUES (4, '111', 'G72IZGCCcBXl1gXtRCUiUQ==', '', 1, '2019-12-27 19:34:56', '2019-12-27 19:34:56');
INSERT INTO `zedc1ang_mall_user` VALUES (5, '444', 'uFfu1clAXB8rmASKrlBnkg==', 'cecc', 1, '2019-12-27 19:38:03', '2019-12-28 01:04:06');
INSERT INTO `zedc1ang_mall_user` VALUES (6, '你好2', 'JdVa0oOqQAr0ZMdtcTwHrQ==', '', 1, '2020-02-08 17:47:06', '2020-02-08 17:47:06');
INSERT INTO `zedc1ang_mall_user` VALUES (7, '你好3', 'JdVa0oOqQAr0ZMdtcTwHrQ==', '', 1, '2020-02-08 17:49:15', '2020-02-08 17:49:15');
INSERT INTO `zedc1ang_mall_user` VALUES (8, '你好4', '12345678', '', 1, '2020-02-09 19:49:54', '2020-02-09 19:49:54');
INSERT INTO `zedc1ang_mall_user` VALUES (9, 'xiaomu2', 'AWRuqaxc6iryhHuA4OnFag==', '祝你今天好心情', 2, '2020-02-09 20:39:47', '2020-02-11 00:56:02');
INSERT INTO `zedc1ang_mall_user` VALUES (10, 'mumu', '12345678', '', 1, '2023-12-19 14:10:32', '2023-12-19 14:10:32');
INSERT INTO `zedc1ang_mall_user` VALUES (11, 'mumu4', 'T3nrrRHj8k5BlVdsVvdXIw==', '用户测试账号', 1, '2023-12-25 07:46:26', '2023-12-26 15:42:34');
INSERT INTO `zedc1ang_mall_user` VALUES (12, 'mumu5', 'T3nrrRHj8k5BlVdsVvdXIw==', '管理员测试账号', 2, '2023-12-25 08:47:01', '2023-12-26 15:42:56');
INSERT INTO `zedc1ang_mall_user` VALUES (13, 'mumu9', 'SMRMN9k6YmXAjbsJCMdxrQ==', '用户测试账号', 2, '2024-03-23 15:06:56', '2024-03-23 16:52:41');
INSERT INTO `zedc1ang_mall_user` VALUES (14, 'mumu10', 'SMRMN9k6YmXAjbsJCMdxrQ==', '', 1, '2024-03-23 16:23:45', '2024-03-23 16:23:45');

SET FOREIGN_KEY_CHECKS = 1;
