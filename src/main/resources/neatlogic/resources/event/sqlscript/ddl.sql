-- ----------------------------
-- Table structure for event
-- ----------------------------
CREATE TABLE IF NOT EXISTS `event`  (
  `id` bigint NOT NULL COMMENT '主键',
  `event_type_id` bigint NOT NULL COMMENT '事件类型ID',
  `event_solution_id` bigint NULL DEFAULT NULL COMMENT '解决方案ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件表';

-- ----------------------------
-- Table structure for event_solution
-- ----------------------------
CREATE TABLE IF NOT EXISTS `event_solution`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '解决方案名称',
  `fcu` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `lcu` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人ID',
  `is_active` tinyint NULL DEFAULT NULL COMMENT '是否启用',
  `fcd` timestamp(3) NULL DEFAULT NULL COMMENT '创建时间',
  `lcd` timestamp(3) NULL DEFAULT NULL COMMENT '更新时间',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件解决方案表';

-- ----------------------------
-- Table structure for event_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `event_type`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '事件类型名称',
  `parent_id` bigint NOT NULL COMMENT '父类型ID',
  `lft` int NULL DEFAULT NULL COMMENT '左编码',
  `rht` int NULL DEFAULT NULL COMMENT '右编码',
  `layer` int NULL DEFAULT NULL COMMENT '节点所在层级',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_lft_rht`(`lft`, `rht`) USING BTREE,
  INDEX `idx_rht_lft`(`rht`, `lft`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件类型表';

-- ----------------------------
-- Table structure for event_type_authority
-- ----------------------------
CREATE TABLE IF NOT EXISTS `event_type_authority`  (
  `event_type_id` bigint NOT NULL COMMENT '事件类型ID',
  `type` enum('common','user','team','role') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权对象类型',
  `uuid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权对象ID',
  PRIMARY KEY (`event_type_id`, `type`, `uuid`) USING BTREE,
  INDEX `idx_event_type_id`(`event_type_id`) USING BTREE,
  INDEX `idx_uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件类型授权表';

-- ----------------------------
-- Table structure for event_type_solution
-- ----------------------------
CREATE TABLE IF NOT EXISTS `event_type_solution`  (
  `event_type_id` bigint NOT NULL COMMENT '事件类型ID',
  `solution_id` bigint NOT NULL COMMENT '解决方案ID',
  PRIMARY KEY (`event_type_id`, `solution_id`) USING BTREE,
  INDEX `idx_solution_id`(`solution_id`) USING BTREE,
  INDEX `idx_event_type_id`(`event_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件类型-解决方案关联表';