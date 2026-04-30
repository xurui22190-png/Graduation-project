/*
 Navicat Premium Data Transfer

 Source Server         : xyh
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : grade-management

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 30/04/2026 17:11:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academic_warning
-- ----------------------------
DROP TABLE IF EXISTS `academic_warning`;
CREATE TABLE `academic_warning`  (
  `wId` int NOT NULL AUTO_INCREMENT,
  `wStudentId` int NULL DEFAULT NULL COMMENT '学生ID',
  `wCourseId` int NULL DEFAULT NULL COMMENT '课程ID',
  `wRiskLevel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风险等级：高风险(红)、中风险(橙)、低风险(黄)',
  `wProbability` double NULL DEFAULT NULL COMMENT '不及格预测概率(0.0-1.0)',
  `wReason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预警原因（如：平时分过低、出勤率不足）',
  `wStatus` int NULL DEFAULT 0 COMMENT '处理状态：0-未处理，1-已谈话，2-已消除',
  `wCreateDate` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`wId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of academic_warning
-- ----------------------------

-- ----------------------------
-- Table structure for classinfo
-- ----------------------------
DROP TABLE IF EXISTS `classinfo`;
CREATE TABLE `classinfo`  (
  `cId` int NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `cMajorId` int NULL DEFAULT NULL COMMENT '所属专业ID',
  `cGrade` int NULL DEFAULT NULL COMMENT '年级',
  `cName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级名称',
  `cCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级编号',
  PRIMARY KEY (`cId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '班级信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classinfo
-- ----------------------------
INSERT INTO `classinfo` VALUES (1, 1, 2021, '软件2101班', 'SE2101');
INSERT INTO `classinfo` VALUES (2, 1, 2021, '软件2102班', 'SE2102');
INSERT INTO `classinfo` VALUES (3, 2, 2022, '计科2201班', 'CST2201');
INSERT INTO `classinfo` VALUES (4, 3, 2023, '人工智能2301班', 'AI2301');
INSERT INTO `classinfo` VALUES (5, 6, 2022, '思政2201班', 'SZ2201');
INSERT INTO `classinfo` VALUES (6, 8, 2022, '数学2201班', 'MA2201');

-- ----------------------------
-- Table structure for collegeinfo
-- ----------------------------
DROP TABLE IF EXISTS `collegeinfo`;
CREATE TABLE `collegeinfo`  (
  `CID` int NOT NULL AUTO_INCREMENT COMMENT '学院ID',
  `cName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '院系名称',
  `cCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '院系编码',
  `cParentId` int NULL DEFAULT 0 COMMENT '父级ID',
  PRIMARY KEY (`CID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '院系信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collegeinfo
-- ----------------------------
INSERT INTO `collegeinfo` VALUES (1, '计算机学院', 'XMUT', 0);
INSERT INTO `collegeinfo` VALUES (2, '计算机与信息工程学院', 'CS001', 1);
INSERT INTO `collegeinfo` VALUES (3, '光电与通信工程学院', 'EI002', 1);
INSERT INTO `collegeinfo` VALUES (4, '商学院', 'BA003', 1);
INSERT INTO `collegeinfo` VALUES (5, '马克思主义学院', 'MY005', 1);
INSERT INTO `collegeinfo` VALUES (6, '数学与统计学院', 'SX006', 1);
INSERT INTO `collegeinfo` VALUES (101, '外国语学院', 'FL001', 0);
INSERT INTO `collegeinfo` VALUES (102, '物理与电子工程学院', 'PHY002', 0);

-- ----------------------------
-- Table structure for courseinfo
-- ----------------------------
DROP TABLE IF EXISTS `courseinfo`;
CREATE TABLE `courseinfo`  (
  `crId` int NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `crCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程代码',
  `crName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程名称',
  `crMajorId` int NULL DEFAULT NULL COMMENT '隶属专业ID',
  `crCredit` decimal(4, 1) NULL DEFAULT 0.0 COMMENT '学分',
  `crPeriod` int NULL DEFAULT 0 COMMENT '总学时',
  `crType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '必修' COMMENT '课程类型',
  `crExamType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '考试' COMMENT '考核方式',
  `crRemark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `crState` int NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`crId`) USING BTREE,
  UNIQUE INDEX `uk_course_code`(`crCode` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courseinfo
-- ----------------------------
INSERT INTO `courseinfo` VALUES (1, 'CS001', 'Java程序设计', 1, 4.0, 48, '必修', '考试', '', 1, '2026-04-29 15:13:58', '2026-04-29 23:27:20');
INSERT INTO `courseinfo` VALUES (2, 'CS002', '数据结构', 1, 3.0, 48, '必修', '考试', '', 1, '2026-04-29 15:13:58', '2026-04-29 23:27:15');
INSERT INTO `courseinfo` VALUES (3, 'MY001', '马克思主义基本原理', 6, 3.0, 48, '必修', '考试', NULL, 1, '2026-04-29 23:25:05', '2026-04-29 23:25:05');
INSERT INTO `courseinfo` VALUES (4, 'MY002', '思想道德修养与法律基础', 6, 2.0, 32, '必修', '考试', NULL, 1, '2026-04-29 23:25:05', '2026-04-29 23:25:05');
INSERT INTO `courseinfo` VALUES (5, 'SX001', '高等数学(上)', 8, 5.0, 80, '必修', '考试', NULL, 1, '2026-04-29 23:25:09', '2026-04-29 23:25:09');
INSERT INTO `courseinfo` VALUES (6, 'SX002', '线性代数', 8, 3.0, 48, '必修', '考试', NULL, 1, '2026-04-29 23:25:09', '2026-04-29 23:25:09');
INSERT INTO `courseinfo` VALUES (7, 'ENG101', '大学英语', 1, 3.0, 48, '必修', '考试', '', 1, '2026-04-30 15:52:30', '2026-04-30 15:52:30');
INSERT INTO `courseinfo` VALUES (8, 'PHY101', '大学物理', 1, 3.0, 48, '必修', '考试', '', 1, '2026-04-30 15:52:51', '2026-04-30 15:52:51');

-- ----------------------------
-- Table structure for knowledge_point
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_point`;
CREATE TABLE `knowledge_point`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` int NOT NULL COMMENT '所属课程ID (关联 courseinfo表)',
  `point_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识点名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 421 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程知识点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of knowledge_point
-- ----------------------------
INSERT INTO `knowledge_point` VALUES (1, 1, 'Java基础语法', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (2, 1, '面向对象编程封装', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (3, 1, '继承与多态特征', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (4, 1, '异常处理机制', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (5, 1, '集合框架应用', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (6, 1, 'IO流与文件操作', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (7, 1, '多线程编程', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (8, 1, 'JDBC数据库连接', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (9, 2, '线性表与链表', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (10, 2, '栈与队列应用', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (11, 2, '二叉树遍历算法', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (12, 2, '图的深度/广度搜索', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (13, 2, '快速排序与归并排序', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (14, 2, '散列表(Hash)设计', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (15, 2, '时间/空间复杂度分析', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (16, 2, '动态规划基础', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (17, 3, '唯物辩证法核心', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (18, 3, '认识论与实践观', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (19, 3, '历史唯物主义原理', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (20, 3, '劳动价值论', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (21, 3, '剩余价值生产', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (22, 3, '资本主义矛盾分析', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (23, 3, '科学社会主义理论', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (24, 3, '共产主义远大理想', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (25, 4, '理想信念的树立', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (26, 4, '中国精神与爱国主义', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (27, 4, '社会主义核心价值观', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (28, 4, '道德规范与修养', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (29, 4, '宪法基本权利', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (30, 4, '民法典基础常识', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (31, 4, '刑法原则与犯罪论', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (32, 4, '法治思维的培养', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (33, 5, '函数极限与连续性', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (34, 5, '导数与微分运算', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (35, 5, '微分中值定理', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (36, 5, '一元函数导数应用', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (37, 5, '不定积分计算', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (38, 5, '定积分定义与性质', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (39, 5, '定积分的几何应用', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (40, 5, '常微分方程基础', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (41, 6, '行列式性质与计算', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (42, 6, '矩阵基本运算', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (43, 6, '矩阵的逆与秩', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (44, 6, '线性方程组解的判断', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (45, 6, '向量组的线性相关性', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (46, 6, '特征值与特征向量', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (47, 6, '相似矩阵与对角化', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (48, 6, '二次型及其标准化', '2026-04-29 23:29:54');
INSERT INTO `knowledge_point` VALUES (401, 301, '词汇与高级语法', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (402, 301, '长难句阅读理解', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (403, 301, '听力信息捕捉', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (404, 301, '跨文化翻译思维', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (405, 301, '学术英语写作表达', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (406, 302, '质点运动学与动力学', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (407, 302, '刚体力学基础', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (408, 302, '机械振动与机械波', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (409, 302, '热力学定律', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (410, 302, '真空中的静电场', '2026-04-30 16:04:41');
INSERT INTO `knowledge_point` VALUES (411, 8, '质点运动学与动力学', '2026-04-30 16:07:18');
INSERT INTO `knowledge_point` VALUES (412, 8, '刚体力学基础', '2026-04-30 16:07:22');
INSERT INTO `knowledge_point` VALUES (413, 8, '机械振动与机械波', '2026-04-30 16:07:25');
INSERT INTO `knowledge_point` VALUES (414, 8, '热力学定律', '2026-04-30 16:07:28');
INSERT INTO `knowledge_point` VALUES (415, 8, '真空中的静电场', '2026-04-30 16:07:34');
INSERT INTO `knowledge_point` VALUES (416, 7, '词汇与高级语法', '2026-04-30 16:07:42');
INSERT INTO `knowledge_point` VALUES (417, 7, '长难句阅读理解', '2026-04-30 16:07:44');
INSERT INTO `knowledge_point` VALUES (418, 7, '听力信息捕捉', '2026-04-30 16:07:48');
INSERT INTO `knowledge_point` VALUES (419, 7, '跨文化翻译思维', '2026-04-30 16:07:51');
INSERT INTO `knowledge_point` VALUES (420, 7, '学术英语写作表达', '2026-04-30 16:07:55');

-- ----------------------------
-- Table structure for majorinfo
-- ----------------------------
DROP TABLE IF EXISTS `majorinfo`;
CREATE TABLE `majorinfo`  (
  `mid` int NOT NULL AUTO_INCREMENT COMMENT '专业ID',
  `mName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业名称',
  `mCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业编码',
  `mCollegeId` int NULL DEFAULT 0 COMMENT '院系ID',
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of majorinfo
-- ----------------------------
INSERT INTO `majorinfo` VALUES (1, '软件工程', 'SE', 2);
INSERT INTO `majorinfo` VALUES (2, '计算机科学与技术', 'CST', 2);
INSERT INTO `majorinfo` VALUES (3, '人工智能', 'AI', 2);
INSERT INTO `majorinfo` VALUES (4, '通信工程', 'TE', 3);
INSERT INTO `majorinfo` VALUES (5, '会计学', 'ACC', 4);
INSERT INTO `majorinfo` VALUES (6, '思想政治教育', 'SZ', 5);
INSERT INTO `majorinfo` VALUES (7, '马克思主义理论', 'ML', 5);
INSERT INTO `majorinfo` VALUES (8, '数学与应用数学', 'MATH', 6);
INSERT INTO `majorinfo` VALUES (9, '信息与计算科学', 'ICS', 6);

-- ----------------------------
-- Table structure for noticeinfo
-- ----------------------------
DROP TABLE IF EXISTS `noticeinfo`;
CREATE TABLE `noticeinfo`  (
  `nId` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `nTitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `nContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容',
  `nType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '普通公告' COMMENT '公告类型',
  `nTop` int NULL DEFAULT 0 COMMENT '是否置顶：0否 1是',
  `nState` int NULL DEFAULT 1 COMMENT '状态：0下线 1发布',
  `nCreateUid` int NULL DEFAULT 0 COMMENT '创建人用户ID',
  `nCreateName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建人姓名',
  `nCreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `nUpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `nIsDelete` int NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
  PRIMARY KEY (`nId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of noticeinfo
-- ----------------------------

-- ----------------------------
-- Table structure for score_detail
-- ----------------------------
DROP TABLE IF EXISTS `score_detail`;
CREATE TABLE `score_detail`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int NOT NULL COMMENT '学生ID (关联 studentinfo)',
  `course_id` int NOT NULL COMMENT '课程ID (关联 courseinfo)',
  `point_id` int NOT NULL COMMENT '知识点ID (关联 knowledge_point)',
  `max_score` double NULL DEFAULT 100 COMMENT '该知识点基准满分(默认100)',
  `actual_score` double NOT NULL COMMENT '学生该知识点实际得分',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 675 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生知识点成绩明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score_detail
-- ----------------------------
INSERT INTO `score_detail` VALUES (337, 1, 3, 17, 100, 80.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (338, 1, 3, 18, 100, 97.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (339, 1, 3, 19, 100, 95.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (340, 1, 3, 20, 100, 87.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (341, 1, 3, 21, 100, 89.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (342, 1, 3, 22, 100, 86.8, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (343, 1, 3, 23, 100, 83.6, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (344, 1, 3, 24, 100, 66.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (345, 2, 3, 17, 100, 91.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (346, 2, 3, 18, 100, 72.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (347, 2, 3, 19, 100, 86.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (348, 2, 3, 20, 100, 87.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (349, 2, 3, 21, 100, 99.9, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (350, 2, 3, 22, 100, 95.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (351, 2, 3, 23, 100, 100, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (352, 2, 3, 24, 100, 84.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (353, 3, 3, 17, 100, 60.9, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (354, 3, 3, 18, 100, 74.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (355, 3, 3, 19, 100, 65.8, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (356, 3, 3, 20, 100, 63.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (357, 3, 3, 21, 100, 64.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (358, 3, 3, 22, 100, 70, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (359, 3, 3, 23, 100, 76.1, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (360, 3, 3, 24, 100, 46.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (361, 4, 3, 17, 100, 81.1, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (362, 4, 3, 18, 100, 75.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (363, 4, 3, 19, 100, 65.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (364, 4, 3, 20, 100, 68.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (365, 4, 3, 21, 100, 67.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (366, 4, 3, 22, 100, 75.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (367, 4, 3, 23, 100, 50.6, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (368, 4, 3, 24, 100, 64.6, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (369, 17, 3, 17, 100, 69.9, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (370, 17, 3, 18, 100, 69.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (371, 17, 3, 19, 100, 68.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (372, 17, 3, 20, 100, 67, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (373, 17, 3, 21, 100, 74.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (374, 17, 3, 22, 100, 70.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (375, 17, 3, 23, 100, 62.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (376, 17, 3, 24, 100, 46.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (377, 21, 3, 17, 100, 71.9, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (378, 21, 3, 18, 100, 79.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (379, 21, 3, 19, 100, 68.6, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (380, 21, 3, 20, 100, 74.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (381, 21, 3, 21, 100, 80.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (382, 21, 3, 22, 100, 47, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (383, 21, 3, 23, 100, 82.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (384, 21, 3, 24, 100, 71.6, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (385, 25, 3, 17, 100, 71.8, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (386, 25, 3, 18, 100, 70.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (387, 25, 3, 19, 100, 61.9, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (388, 25, 3, 20, 100, 72.7, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (389, 25, 3, 21, 100, 57.1, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (390, 25, 3, 22, 100, 56.1, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (391, 25, 3, 23, 100, 49.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (392, 25, 3, 24, 100, 60.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (393, 29, 3, 17, 100, 76.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (394, 29, 3, 18, 100, 76.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (395, 29, 3, 19, 100, 82.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (396, 29, 3, 20, 100, 63.2, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (397, 29, 3, 21, 100, 76.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (398, 29, 3, 22, 100, 75.4, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (399, 29, 3, 23, 100, 82.5, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (400, 29, 3, 24, 100, 75.3, '2026-04-30 14:48:21');
INSERT INTO `score_detail` VALUES (417, 31, 3, 17, 100, 79.5, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (418, 31, 3, 18, 100, 94.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (419, 31, 3, 19, 100, 89.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (420, 31, 3, 20, 100, 95.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (421, 31, 3, 21, 100, 71, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (422, 31, 3, 22, 100, 85.7, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (423, 31, 3, 23, 100, 94.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (424, 31, 3, 24, 100, 88.6, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (425, 32, 3, 17, 100, 100, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (426, 32, 3, 18, 100, 100, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (427, 32, 3, 19, 100, 75.5, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (428, 32, 3, 20, 100, 98.8, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (429, 32, 3, 21, 100, 100, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (430, 32, 3, 22, 100, 97.3, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (431, 32, 3, 23, 100, 100, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (432, 32, 3, 24, 100, 95.6, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (433, 33, 3, 17, 100, 81.3, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (434, 33, 3, 18, 100, 71.6, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (435, 33, 3, 19, 100, 69.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (436, 33, 3, 20, 100, 66.1, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (437, 33, 3, 21, 100, 70.2, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (438, 33, 3, 22, 100, 52.1, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (439, 33, 3, 23, 100, 70, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (440, 33, 3, 24, 100, 76.5, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (441, 34, 3, 17, 100, 50.1, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (442, 34, 3, 18, 100, 67, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (443, 34, 3, 19, 100, 58.5, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (444, 34, 3, 20, 100, 57.4, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (445, 34, 3, 21, 100, 48.4, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (446, 34, 3, 22, 100, 39.3, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (447, 34, 3, 23, 100, 53.3, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (448, 34, 3, 24, 100, 67.5, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (449, 35, 3, 17, 100, 74.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (450, 35, 3, 18, 100, 72, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (451, 35, 3, 19, 100, 87.1, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (452, 35, 3, 20, 100, 87.9, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (453, 35, 3, 21, 100, 82.2, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (454, 35, 3, 22, 100, 85.7, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (455, 35, 3, 23, 100, 62.2, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (456, 35, 3, 24, 100, 83.7, '2026-04-30 14:48:26');
INSERT INTO `score_detail` VALUES (577, 9, 4, 25, 100, 31.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (578, 9, 4, 26, 100, 60, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (579, 9, 4, 27, 100, 67.7, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (580, 9, 4, 28, 100, 62.9, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (581, 9, 4, 29, 100, 68.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (582, 9, 4, 30, 100, 56.9, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (583, 9, 4, 31, 100, 50.2, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (584, 9, 4, 32, 100, 62.4, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (585, 10, 4, 25, 100, 90.4, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (586, 10, 4, 26, 100, 88.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (587, 10, 4, 27, 100, 85.3, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (588, 10, 4, 28, 100, 79.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (589, 10, 4, 29, 100, 88, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (590, 10, 4, 30, 100, 80.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (591, 10, 4, 31, 100, 97.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (592, 10, 4, 32, 100, 69.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (593, 11, 4, 25, 100, 74.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (594, 11, 4, 26, 100, 98.9, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (595, 11, 4, 27, 100, 88.9, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (596, 11, 4, 28, 100, 87.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (597, 11, 4, 29, 100, 100, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (598, 11, 4, 30, 100, 94, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (599, 11, 4, 31, 100, 87.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (600, 11, 4, 32, 100, 94, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (601, 12, 4, 25, 100, 51.3, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (602, 12, 4, 26, 100, 68.1, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (603, 12, 4, 27, 100, 76.1, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (604, 12, 4, 28, 100, 66.8, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (605, 12, 4, 29, 100, 65.2, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (606, 12, 4, 30, 100, 69.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (607, 12, 4, 31, 100, 80.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (608, 12, 4, 32, 100, 68.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (609, 19, 4, 25, 100, 62.4, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (610, 19, 4, 26, 100, 62.9, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (611, 19, 4, 27, 100, 34.8, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (612, 19, 4, 28, 100, 60.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (613, 19, 4, 29, 100, 66.1, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (614, 19, 4, 30, 100, 65.2, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (615, 19, 4, 31, 100, 53.9, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (616, 19, 4, 32, 100, 56.2, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (617, 23, 4, 25, 100, 74.7, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (618, 23, 4, 26, 100, 88.4, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (619, 23, 4, 27, 100, 90.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (620, 23, 4, 28, 100, 56.6, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (621, 23, 4, 29, 100, 80.1, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (622, 23, 4, 30, 100, 78.3, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (623, 23, 4, 31, 100, 73.4, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (624, 23, 4, 32, 100, 77.5, '2026-04-30 14:55:11');
INSERT INTO `score_detail` VALUES (625, 2, 5, 33, 100, 90.7, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (626, 2, 5, 34, 100, 98.9, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (627, 2, 5, 35, 100, 96.2, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (628, 2, 5, 36, 100, 94.3, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (629, 2, 5, 37, 100, 79.6, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (630, 2, 5, 38, 100, 91.5, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (631, 2, 5, 39, 100, 90.3, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (632, 2, 5, 40, 100, 90.9, '2026-04-30 17:01:21');
INSERT INTO `score_detail` VALUES (641, 2, 2, 9, 100, 92.9, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (642, 2, 2, 10, 100, 80.6, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (643, 2, 2, 11, 100, 85.7, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (644, 2, 2, 12, 100, 80.1, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (645, 2, 2, 13, 100, 64.4, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (646, 2, 2, 14, 100, 94.2, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (647, 2, 2, 15, 100, 89.2, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (648, 2, 2, 16, 100, 86, '2026-04-30 17:05:24');
INSERT INTO `score_detail` VALUES (649, 2, 6, 41, 100, 68.3, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (650, 2, 6, 42, 100, 72.2, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (651, 2, 6, 43, 100, 71.4, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (652, 2, 6, 44, 100, 72.2, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (653, 2, 6, 45, 100, 57.2, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (654, 2, 6, 46, 100, 77.4, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (655, 2, 6, 47, 100, 68.9, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (656, 2, 6, 48, 100, 75.4, '2026-04-30 17:05:38');
INSERT INTO `score_detail` VALUES (657, 2, 1, 1, 100, 68.5, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (658, 2, 1, 2, 100, 62.9, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (659, 2, 1, 3, 100, 64.3, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (660, 2, 1, 4, 100, 50.5, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (661, 2, 1, 5, 100, 55, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (662, 2, 1, 6, 100, 57, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (663, 2, 1, 7, 100, 63.3, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (664, 2, 1, 8, 100, 34.8, '2026-04-30 17:07:34');
INSERT INTO `score_detail` VALUES (665, 2, 7, 416, 100, 76.9, '2026-04-30 17:07:56');
INSERT INTO `score_detail` VALUES (666, 2, 7, 417, 100, 50, '2026-04-30 17:07:56');
INSERT INTO `score_detail` VALUES (667, 2, 7, 418, 100, 82.7, '2026-04-30 17:07:56');
INSERT INTO `score_detail` VALUES (668, 2, 7, 419, 100, 77.3, '2026-04-30 17:07:56');
INSERT INTO `score_detail` VALUES (669, 2, 7, 420, 100, 86.4, '2026-04-30 17:07:56');
INSERT INTO `score_detail` VALUES (670, 2, 8, 411, 100, 63.5, '2026-04-30 17:08:23');
INSERT INTO `score_detail` VALUES (671, 2, 8, 412, 100, 94.4, '2026-04-30 17:08:23');
INSERT INTO `score_detail` VALUES (672, 2, 8, 413, 100, 83.8, '2026-04-30 17:08:23');
INSERT INTO `score_detail` VALUES (673, 2, 8, 414, 100, 96.5, '2026-04-30 17:08:23');
INSERT INTO `score_detail` VALUES (674, 2, 8, 415, 100, 91.8, '2026-04-30 17:08:23');

-- ----------------------------
-- Table structure for scoreinfo
-- ----------------------------
DROP TABLE IF EXISTS `scoreinfo`;
CREATE TABLE `scoreinfo`  (
  `scId` int NOT NULL AUTO_INCREMENT COMMENT '成绩ID',
  `scStudentId` int NULL DEFAULT NULL COMMENT '学生ID',
  `scTermId` int NULL DEFAULT NULL COMMENT '学期ID',
  `scClassId` int NULL DEFAULT NULL COMMENT '班级ID',
  `scCourseId` int NULL DEFAULT NULL COMMENT '课程ID',
  `scScore` decimal(5, 1) NULL DEFAULT 0.0 COMMENT '分数',
  `scStatus` int NULL DEFAULT 0 COMMENT '设置状态(0:未设置 1:已设置)',
  `scCreateDate` datetime NULL DEFAULT NULL COMMENT '登记时间',
  `scTeacherId` int NULL DEFAULT NULL COMMENT '登记老师ID',
  `sc_regular` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '平时成绩',
  `sc_test` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '测试成绩',
  `sc_exam` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '期末成绩',
  PRIMARY KEY (`scId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '成绩信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scoreinfo
-- ----------------------------
INSERT INTO `scoreinfo` VALUES (1, 5, 1, 2, 1, 67.0, 1, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (2, 6, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (3, 7, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (4, 8, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (5, 18, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (6, 22, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (7, 26, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (8, 30, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (9, 34, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (10, 35, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (11, 42, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (12, 45, 1, 2, 1, 0.0, 0, '2026-04-29 18:04:37', 1, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (13, 1, 1, 1, 3, 88.0, 1, '2026-04-30 13:17:32', 3, 88.90, 91.20, 86.20);
INSERT INTO `scoreinfo` VALUES (14, 2, 1, 1, 3, 93.0, 1, '2026-04-30 13:17:32', 3, 91.30, 95.10, 93.20);
INSERT INTO `scoreinfo` VALUES (15, 3, 1, 1, 3, 70.0, 1, '2026-04-30 13:17:32', 3, 71.50, 69.10, 69.50);
INSERT INTO `scoreinfo` VALUES (16, 4, 1, 1, 3, 72.0, 1, '2026-04-30 13:17:32', 3, 73.30, 74.00, 70.40);
INSERT INTO `scoreinfo` VALUES (17, 17, 1, 1, 3, 66.0, 1, '2026-04-30 13:17:32', 3, 73.70, 68.70, 60.30);
INSERT INTO `scoreinfo` VALUES (18, 21, 1, 1, 3, 76.0, 1, '2026-04-30 13:17:32', 3, 75.00, 75.40, 76.80);
INSERT INTO `scoreinfo` VALUES (19, 25, 1, 1, 3, 65.0, 1, '2026-04-30 13:17:32', 3, 64.30, 67.30, 64.50);
INSERT INTO `scoreinfo` VALUES (20, 29, 1, 1, 3, 80.0, 1, '2026-04-30 13:17:32', 3, 85.40, 83.40, 75.40);
INSERT INTO `scoreinfo` VALUES (21, 31, 1, 1, 3, 90.0, 1, '2026-04-30 13:17:32', 3, 90.30, 85.10, 91.80);
INSERT INTO `scoreinfo` VALUES (22, 32, 1, 1, 3, 100.0, 1, '2026-04-30 13:17:32', 3, 100.00, 100.00, 100.00);
INSERT INTO `scoreinfo` VALUES (23, 33, 1, 1, 3, 0.0, 0, '2026-04-30 13:17:32', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (24, 41, 1, 1, 3, 0.0, 0, '2026-04-30 13:17:32', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (25, 9, 1, 3, 4, 59.0, 1, '2026-04-30 13:42:21', 3, 66.60, 60.10, 54.00);
INSERT INTO `scoreinfo` VALUES (26, 10, 1, 3, 4, 88.5, 1, '2026-04-30 14:55:09', 3, 87.00, 90.70, 88.50);
INSERT INTO `scoreinfo` VALUES (27, 11, 1, 3, 4, 95.0, 1, '2026-04-30 14:55:09', 3, 100.00, 92.20, 93.10);
INSERT INTO `scoreinfo` VALUES (28, 12, 1, 3, 4, 72.0, 1, '2026-04-30 14:55:09', 3, 76.30, 74.10, 68.60);
INSERT INTO `scoreinfo` VALUES (29, 19, 1, 3, 4, 58.0, 1, '2026-04-30 14:55:09', 3, 60.00, 57.50, 57.00);
INSERT INTO `scoreinfo` VALUES (30, 23, 1, 3, 4, 81.5, 1, '2026-04-30 14:55:09', 3, 88.40, 77.00, 79.20);
INSERT INTO `scoreinfo` VALUES (31, 27, 1, 3, 4, 0.0, 0, '2026-04-30 13:42:21', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (32, 36, 1, 3, 4, 0.0, 0, '2026-04-30 13:42:21', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (33, 37, 1, 3, 4, 0.0, 0, '2026-04-30 13:42:21', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (34, 38, 1, 3, 4, 0.0, 0, '2026-04-30 13:42:21', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (35, 43, 1, 3, 4, 0.0, 0, '2026-04-30 13:42:21', 3, 0.00, 0.00, 0.00);
INSERT INTO `scoreinfo` VALUES (36, 2, 1, 1, 5, 98.0, 1, NULL, NULL, 100.00, 98.90, 96.40);
INSERT INTO `scoreinfo` VALUES (37, 2, 1, 1, 6, 77.0, 1, NULL, NULL, 77.80, 75.00, 77.30);
INSERT INTO `scoreinfo` VALUES (38, 2, 1, 1, 2, 87.0, 1, NULL, NULL, 87.80, 82.10, 88.50);
INSERT INTO `scoreinfo` VALUES (39, 2, 1, 1, 1, 59.0, 1, NULL, NULL, 57.10, 61.50, 59.10);
INSERT INTO `scoreinfo` VALUES (40, 2, 1, 1, 7, 77.0, 1, NULL, NULL, 81.40, 75.40, 75.00);
INSERT INTO `scoreinfo` VALUES (41, 2, 1, 1, 8, 89.0, 1, NULL, NULL, 90.90, 84.60, 89.60);

-- ----------------------------
-- Table structure for studentinfo
-- ----------------------------
DROP TABLE IF EXISTS `studentinfo`;
CREATE TABLE `studentinfo`  (
  `sid` int NOT NULL AUTO_INCREMENT COMMENT '学生ID',
  `sNo` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学号',
  `sName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `sSex` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `sClassId` int NULL DEFAULT NULL COMMENT '班级',
  `sTel` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `sIdcard` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号',
  `sAddress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `sCreateDate` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `sAccountId` int NULL DEFAULT 0 COMMENT '关联账号ID',
  `sIntent` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未设定' COMMENT '学业意向：考研/就业/考公',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of studentinfo
-- ----------------------------
INSERT INTO `studentinfo` VALUES (1, 'S2026101', '周杰伦', '男', 1, '13811110001', '350202200101010000', '台北市忠孝东路1号', NULL, 56, '未设定');
INSERT INTO `studentinfo` VALUES (2, 'S2026102', '迪丽热巴', '女', 1, '13811110002', '350202200101010000', '乌鲁木齐市天山区2号', NULL, 57, '未设定');
INSERT INTO `studentinfo` VALUES (3, 'S2026103', '肖战', '男', 1, '13811110003', '350202200101010000', '重庆市渝中区3号', NULL, 58, '未设定');
INSERT INTO `studentinfo` VALUES (4, 'S2026104', '王一博', '男', 1, '13811110004', '350202200101010000', '洛阳市西工区4号', NULL, 59, '未设定');
INSERT INTO `studentinfo` VALUES (5, 'S2026105', '赵丽颖', '女', 2, '13811110005', '350202200101010000', '廊坊市安次区5号', NULL, 60, '未设定');
INSERT INTO `studentinfo` VALUES (6, 'S2026106', '杨幂', '女', 2, '13811110006', '350202200101010000', '北京市宣武区6号', NULL, 61, '未设定');
INSERT INTO `studentinfo` VALUES (7, 'S2026107', '易烊千玺', '男', 2, '13811110007', '350202200101010000', '怀化市洪江区7号', NULL, 62, '未设定');
INSERT INTO `studentinfo` VALUES (8, 'S2026108', '邓紫棋', '女', 2, '13811110008', '350202200101010000', '上海市静安区8号', NULL, 63, '未设定');
INSERT INTO `studentinfo` VALUES (9, 'S2026109', '华晨宇', '男', 3, '13811110009', '350202200101010000', '十堰市茅箭区9号', NULL, 64, '未设定');
INSERT INTO `studentinfo` VALUES (10, 'S2026110', '蔡徐坤', '男', 3, '13811110010', '350202200101010000', '温州市鹿城区10号', NULL, 65, '未设定');
INSERT INTO `studentinfo` VALUES (11, 'S2026111', '沈腾', '男', 3, '13811110011', '350202200101010000', '齐齐哈尔市11号', NULL, 66, '未设定');
INSERT INTO `studentinfo` VALUES (12, 'S2026112', '马丽', '女', 3, '13811110012', '350202200101010000', '丹东市宽甸县12号', NULL, 67, '未设定');
INSERT INTO `studentinfo` VALUES (13, 'S2026113', '贾玲', '女', 4, '13811110013', '350202200101010000', '襄阳市宜城市13号', NULL, 68, '未设定');
INSERT INTO `studentinfo` VALUES (14, 'S2026114', '张小斐', '女', 4, '13811110014', '350202200101010000', '鞍山市铁东区14号', NULL, 69, '未设定');
INSERT INTO `studentinfo` VALUES (15, 'S2026115', '吴京', '男', 4, '13811110015', '350202200101010000', '北京市朝阳区15号', NULL, 70, '未设定');
INSERT INTO `studentinfo` VALUES (16, 'S2026116', '甄子丹', '男', 4, '13811110016', '350202200101010000', '广州市越秀区16号', NULL, 71, '未设定');
INSERT INTO `studentinfo` VALUES (17, 'S2026117', '成龙', '男', 1, '13811110017', '350202200101010000', '香港九龙17号', NULL, 72, '未设定');
INSERT INTO `studentinfo` VALUES (18, 'S2026118', '刘德华', '男', 2, '13811110018', '350202200101010000', '新界18号', NULL, 73, '未设定');
INSERT INTO `studentinfo` VALUES (19, 'S2026119', '郭富城', '男', 3, '13811110019', '350202200101010000', '屯门19号', NULL, 74, '未设定');
INSERT INTO `studentinfo` VALUES (20, 'S2026120', '黎明', '男', 4, '13811110020', '350202200101010000', '观塘20号', NULL, 75, '未设定');
INSERT INTO `studentinfo` VALUES (21, 'S2026121', '林青霞', '女', 1, '13811110021', '350202200101010000', '嘉义县21号', NULL, 76, '未设定');
INSERT INTO `studentinfo` VALUES (22, 'S2026122', '张曼玉', '女', 2, '13811110022', '350202200101010000', '铜锣湾22号', NULL, 77, '未设定');
INSERT INTO `studentinfo` VALUES (23, 'S2026123', '朱茵', '女', 3, '13811110023', '350202200101010000', '尖沙咀23号', NULL, 78, '未设定');
INSERT INTO `studentinfo` VALUES (24, 'S2026124', '王祖贤', '女', 4, '13811110024', '350202200101010000', '台北市24号', NULL, 79, '未设定');
INSERT INTO `studentinfo` VALUES (25, 'S2026125', '李连杰', '男', 1, '13811110025', '350202200101010000', '北京市25号', NULL, 80, '未设定');
INSERT INTO `studentinfo` VALUES (26, 'S2026126', '洪金宝', '男', 2, '13811110027', '350202200101010000', '宁波市26号', NULL, 81, '未设定');
INSERT INTO `studentinfo` VALUES (27, 'S2026127', '曾志伟', '男', 3, '13811110027', '350202200101010000', '梅州市27号', NULL, 82, '未设定');
INSERT INTO `studentinfo` VALUES (28, 'S2026128', '张学友', '男', 4, '13811110028', '350202200101010000', '天津市28号', NULL, 83, '未设定');
INSERT INTO `studentinfo` VALUES (29, 'S2026129', '陈奕迅', '男', 1, '13811110029', '350202200101010000', '广州市29号', NULL, 84, '未设定');
INSERT INTO `studentinfo` VALUES (30, 'S2026130', '莫文蔚', '女', 2, '13811110030', '350202200101010000', '澳门30号', NULL, 85, '未设定');
INSERT INTO `studentinfo` VALUES (31, 'S2026201', '胡歌', '男', 1, '13200000001', '350202200101010000', '上海市徐汇区', NULL, 86, '未设定');
INSERT INTO `studentinfo` VALUES (32, 'S2026202', '刘亦菲', '女', 1, '13200000002', '350202200101010000', '武汉市江岸区', NULL, 87, '未设定');
INSERT INTO `studentinfo` VALUES (33, 'S2026203', '彭于晏', '男', 1, '13200000003', '350202200101010000', '台北市松山区', NULL, 88, '未设定');
INSERT INTO `studentinfo` VALUES (34, 'S2026204', '唐嫣', '女', 2, '13200000004', '350202200101010000', '上海市黄浦区', NULL, 89, '未设定');
INSERT INTO `studentinfo` VALUES (35, 'S2026205', '霍建华', '男', 2, '13200000005', '350202200101010000', '台北市大安区', NULL, 90, '未设定');
INSERT INTO `studentinfo` VALUES (36, 'S2026206', '杨紫', '女', 3, '13200000006', '350202200101010000', '北京市海淀区', NULL, 91, '未设定');
INSERT INTO `studentinfo` VALUES (37, 'S2026207', '张若昀', '男', 3, '13200000007', '350202200101010000', '北京市西城区', NULL, 92, '未设定');
INSERT INTO `studentinfo` VALUES (38, 'S2026208', '李沁', '女', 3, '13200000008', '350202200101010000', '苏州市昆山市', NULL, 93, '未设定');
INSERT INTO `studentinfo` VALUES (39, 'S2026209', '郭麒麟', '男', 4, '13200000009', '350202200101010000', '天津市和平区', NULL, 94, '未设定');
INSERT INTO `studentinfo` VALUES (40, 'S2026210', '宋轶', '女', 4, '13200000010', '350202200101010000', '荆门市东宝区', NULL, 95, '未设定');
INSERT INTO `studentinfo` VALUES (41, 'S2026211', '吴磊', '男', 1, '13200000011', '350202200101010000', '上海市静安区', NULL, 96, '未设定');
INSERT INTO `studentinfo` VALUES (42, 'S2026212', '赵露思', '女', 2, '13200000012', '350202200101010000', '成都市都江堰', NULL, 97, '未设定');
INSERT INTO `studentinfo` VALUES (43, 'S2026213', '王凯', '男', 3, '13200000013', '350202200101010000', '武汉市武昌区', NULL, 98, '未设定');
INSERT INTO `studentinfo` VALUES (44, 'S2026214', '江疏影', '女', 4, '13200000014', '350202200101010000', '上海市虹口区', NULL, 99, '未设定');
INSERT INTO `studentinfo` VALUES (45, 'S2026215', '朱一龙', '男', 2, '13200000015', '350202200101010000', '武汉市汉阳区', NULL, 100, '未设定');

-- ----------------------------
-- Table structure for teacherinfo
-- ----------------------------
DROP TABLE IF EXISTS `teacherinfo`;
CREATE TABLE `teacherinfo`  (
  `tId` int NOT NULL AUTO_INCREMENT COMMENT '教师ID',
  `tNo` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工号',
  `tName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `tSex` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `tTel` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `tEduLevel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教育程度',
  `tSchool` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `tAddress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '现居地址',
  `tAccountId` int NULL DEFAULT 0 COMMENT '关联账号ID',
  `tCollegeId` int NULL DEFAULT 0 COMMENT '所属院系ID',
  PRIMARY KEY (`tId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 203 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacherinfo
-- ----------------------------
INSERT INTO `teacherinfo` VALUES (2, 'T2502', '杨教授', '男', '18153971785', '本科', '北大', '后溪镇厦门理工学院', 101, 1);
INSERT INTO `teacherinfo` VALUES (3, 'T2503', '李马克', '男', NULL, '博士', '中国人民大学', NULL, 102, 5);
INSERT INTO `teacherinfo` VALUES (4, 'T2504', '王数学', '女', NULL, '博士', '复旦大学', NULL, 103, 6);
INSERT INTO `teacherinfo` VALUES (5, 'T2505', '陈算法', '男', NULL, '硕士', '厦门大学', NULL, 104, 2);
INSERT INTO `teacherinfo` VALUES (6, 'T2506', '刘架构', '男', NULL, '硕士', '浙江大学', NULL, 105, 2);
INSERT INTO `teacherinfo` VALUES (201, 'T2507', '张雪', '女', NULL, '硕士', NULL, NULL, 1, 101);
INSERT INTO `teacherinfo` VALUES (202, 'T2508', '李量子', '男', NULL, '硕士', NULL, NULL, 106, 102);

-- ----------------------------
-- Table structure for teaching
-- ----------------------------
DROP TABLE IF EXISTS `teaching`;
CREATE TABLE `teaching`  (
  `tcId` int NOT NULL AUTO_INCREMENT COMMENT '授课ID',
  `tcTermId` int NULL DEFAULT NULL COMMENT '学期',
  `tcClassId` int NULL DEFAULT NULL COMMENT '班级ID',
  `tcCourseId` int NULL DEFAULT NULL COMMENT '课程ID',
  `tcTeacherId` int NULL DEFAULT NULL COMMENT '教师ID',
  `w_exam` double(3, 2) NULL DEFAULT 0.50 COMMENT '考试权重',
  `w_regular` double(3, 2) NULL DEFAULT 0.30 COMMENT '平时权重',
  `w_test` double(3, 2) NULL DEFAULT 0.20 COMMENT '测试权重',
  PRIMARY KEY (`tcId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '授课安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching
-- ----------------------------
INSERT INTO `teaching` VALUES (1, 1, 1, 1, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (2, 1, 1, 2, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (3, 1, 2, 1, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (4, 1, 4, 3, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (5, 1, 3, 4, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (6, 1, 1, 2, 5, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (7, 1, 2, 2, 5, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (8, 1, 3, 1, 6, 0.60, 0.20, 0.20);
INSERT INTO `teaching` VALUES (9, 1, 4, 1, 6, 0.60, 0.20, 0.20);
INSERT INTO `teaching` VALUES (10, 1, 1, 3, 3, 0.00, 0.00, 0.00);
INSERT INTO `teaching` VALUES (11, 1, 2, 5, 4, 0.70, 0.20, 0.10);
INSERT INTO `teaching` VALUES (12, 1, 6, 6, 4, 0.60, 0.20, 0.20);
INSERT INTO `teaching` VALUES (13, 1, 3, 7, 201, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (14, 1, 1, 8, 202, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (15, 1, 1, 7, 201, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (16, 1, 1, 5, 4, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (17, 1, 1, 6, 5, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (18, 1, 1, 1, 6, 0.50, 0.30, 0.20);

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `uLog` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `uPwd` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `uName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `uSex` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `uPhoto` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `uRole` int NULL DEFAULT 0 COMMENT '角色(1:学生 2:教师 3:管理员)',
  `uCreateDate` datetime NULL DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES (1, 'T2507', '123456', '张雪', '女', NULL, 2, NULL);
INSERT INTO `userinfo` VALUES (3, '2502', '123456', '学生一', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (4, '2501', '123456', '学生二', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (26, 'S2026010', '123456', '张伟', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (27, 'S2026011', '123456', '李娜', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (28, 'S2026012', '888888', '王强', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (29, 'S2026013', '123456', '赵敏', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (30, 'S2026014', 'abc1234', '陈杰', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (31, 'S2026015', '123456', '刘洋', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (32, 'S2026016', '123456', '林涛', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (33, 'S2026017', '654321', '周杰', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (34, 'S2026018', '123456', '吴敏', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (35, 'S2026019', '123456', '郑爽', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (36, 'S2026020', 'admin123', '杨凡', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (37, 'S2026021', '123456', '赵云', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (38, 'S2026022', '111111', '钱峰', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (39, 'S2026023', '123456', '孙亮', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (40, 'S2026024', '123456', '李明', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (41, 'S2026025', '666888', '王刚', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (42, 'S2026026', '123456', '冯秀', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (43, 'S2026027', '123456', '褚健', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (44, 'S2026028', 'test999', '卫东', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (45, 'S2026029', '123456', '蒋华', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (46, 'S2026030', '123456', '沈悦', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (47, 'S2026031', '123456', '韩梅', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (48, 'S2026032', 'pass123', '郭晶', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (49, 'S2026033', '123456', '马腾', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (50, 'S2026034', '123456', '梁坤', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (51, 'S2026035', 'abc888', '宋青', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (52, 'S2026036', '123456', '邓超', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (53, 'S2026037', '123456', '彭于', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (54, 'S2026038', 'top777', '袁泉', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (55, 'S2026039', '123456', '范伟', NULL, NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (56, 'S2026101', '123456', '周杰伦', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (57, 'S2026102', '123456', '迪丽热巴', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (58, 'S2026103', '123456', '肖战', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (59, 'S2026104', '123456', '王一博', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (60, 'S2026105', '123456', '赵丽颖', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (61, 'S2026106', '123456', '杨幂', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (62, 'S2026107', '123456', '易烊千玺', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (63, 'S2026108', '123456', '邓紫棋', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (64, 'S2026109', '123456', '华晨宇', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (65, 'S2026110', '123456', '蔡徐坤', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (66, 'S2026111', '123456', '沈腾', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (67, 'S2026112', '123456', '马丽', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (68, 'S2026113', '123456', '贾玲', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (69, 'S2026114', '123456', '张小斐', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (70, 'S2026115', '123456', '吴京', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (71, 'S2026116', '123456', '甄子丹', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (72, 'S2026117', '123456', '成龙', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (73, 'S2026118', '123456', '刘德华', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (74, 'S2026119', '123456', '郭富城', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (75, 'S2026120', '123456', '黎明', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (76, 'S2026121', '123456', '林青霞', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (77, 'S2026122', '123456', '张曼玉', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (78, 'S2026123', '123456', '朱茵', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (79, 'S2026124', '123456', '王祖贤', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (80, 'S2026125', '123456', '李连杰', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (81, 'S2026126', '123456', '洪金宝', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (82, 'S2026127', '123456', '曾志伟', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (83, 'S2026128', '123456', '张学友', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (84, 'S2026129', '123456', '陈奕迅', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (85, 'S2026130', '123456', '莫文蔚', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (86, 'S2026201', '123456', '胡歌', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (87, 'S2026202', '123456', '刘亦菲', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (88, 'S2026203', '123456', '彭于晏', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (89, 'S2026204', '123456', '唐嫣', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (90, 'S2026205', 'abc123', '霍建华', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (91, 'S2026206', '123456', '杨紫', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (92, 'S2026207', '123456', '张若昀', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (93, 'S2026208', '123456', '李沁', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (94, 'S2026209', '666888', '郭麒麟', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (95, 'S2026210', '123456', '宋轶', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (96, 'S2026211', '123456', '吴磊', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (97, 'S2026212', '123456', '赵露思', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (98, 'S2026213', '123456', '王凯', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (99, 'S2026214', '123456', '江疏影', '女', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (100, 'S2026215', 'test888', '朱一龙', '男', NULL, 1, NULL);
INSERT INTO `userinfo` VALUES (101, 'T2502', '123456', '杨教授', '男', NULL, 2, '2026-04-29 17:59:51');
INSERT INTO `userinfo` VALUES (102, 'T2503', '123456', '李马克', '男', NULL, 2, NULL);
INSERT INTO `userinfo` VALUES (103, 'T2504', '123456', '王数学', '女', NULL, 2, NULL);
INSERT INTO `userinfo` VALUES (104, 'T2505', '123456', '陈算法', '男', NULL, 2, NULL);
INSERT INTO `userinfo` VALUES (105, 'T2506', '123456', '刘架构', '男', NULL, 2, NULL);
INSERT INTO `userinfo` VALUES (106, 'T2508', '123456', '李量子', '男', NULL, 2, '2026-04-30 15:53:21');
INSERT INTO `userinfo` VALUES (107, 'admin', '123456', '管理员', '男', '', 3, NULL);

-- ----------------------------
-- Table structure for yearterm
-- ----------------------------
DROP TABLE IF EXISTS `yearterm`;
CREATE TABLE `yearterm`  (
  `yId` int NOT NULL AUTO_INCREMENT COMMENT '学期ID',
  `yYear` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学年',
  `yTerm` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学期',
  `yAll` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '全称',
  PRIMARY KEY (`yId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学年学期表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yearterm
-- ----------------------------
INSERT INTO `yearterm` VALUES (1, '2025', '1', '2025-2026 第一学期');

-- ----------------------------
-- View structure for vwclass
-- ----------------------------
DROP VIEW IF EXISTS `vwclass`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwclass` AS select `ta`.`cId` AS `cId`,`ta`.`cMajorId` AS `cMajorId`,`ta`.`cGrade` AS `cGrade`,`ta`.`cName` AS `cName`,`ta`.`cCode` AS `cCode`,`tb`.`mName` AS `mName`,`tb`.`mCode` AS `mCode`,`tb`.`mCollegeId` AS `mCollegeId`,`tb`.`cName` AS `collegeName`,`tb`.`cCode` AS `collegeCode`,`tb`.`cParentId` AS `cParentId`,`tb`.`cNamet` AS `cNamet`,`tb`.`cCodet` AS `cCodet`,`tb`.`cParentIdt` AS `cParentIdt` from (`classinfo` `ta` join `vwmajor` `tb` on((`ta`.`cMajorId` = `tb`.`mid`)));

-- ----------------------------
-- View structure for vwcollege
-- ----------------------------
DROP VIEW IF EXISTS `vwcollege`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwcollege` AS select `ta`.`CID` AS `CID`,`ta`.`cName` AS `cName`,`ta`.`cCode` AS `cCode`,`ta`.`cParentId` AS `cParentId`,`tb`.`cName` AS `cNamet`,`tb`.`cCode` AS `cCodet`,`tb`.`cParentId` AS `cParentIdt` from (`collegeinfo` `ta` left join `collegeinfo` `tb` on((`ta`.`cParentId` = `tb`.`CID`)));

-- ----------------------------
-- View structure for vwcourse
-- ----------------------------
DROP VIEW IF EXISTS `vwcourse`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwcourse` AS select `ta`.`crId` AS `crId`,`ta`.`crCode` AS `crCode`,`ta`.`crName` AS `crName`,`ta`.`crMajorId` AS `crMajorId`,`tb`.`mName` AS `mName`,`tb`.`mCode` AS `mCode`,`tb`.`mCollegeId` AS `mCollegeId`,`tb`.`cName` AS `collegeName`,`tb`.`cCode` AS `collegeCode`,`tb`.`cParentId` AS `cParentId`,`tb`.`cNamet` AS `cNamet`,`tb`.`cCodet` AS `cCodet`,`tb`.`cParentIdt` AS `cParentIdt` from (`courseinfo` `ta` join `vwmajor` `tb` on((`ta`.`crMajorId` = `tb`.`mid`)));

-- ----------------------------
-- View structure for vwmajor
-- ----------------------------
DROP VIEW IF EXISTS `vwmajor`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwmajor` AS select `ta`.`mid` AS `mid`,`ta`.`mName` AS `mName`,`ta`.`mCode` AS `mCode`,`ta`.`mCollegeId` AS `mCollegeId`,`tb`.`cName` AS `cName`,`tb`.`cCode` AS `cCode`,`tb`.`cParentId` AS `cParentId`,`tb`.`cNamet` AS `cNamet`,`tb`.`cCodet` AS `cCodet`,`tb`.`cParentIdt` AS `cParentIdt` from (`majorinfo` `ta` join `vwcollege` `tb` on((`ta`.`mCollegeId` = `tb`.`CID`)));

-- ----------------------------
-- View structure for vwscore
-- ----------------------------
DROP VIEW IF EXISTS `vwscore`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwscore` AS select `ta`.`scId` AS `scId`,`ta`.`scStudentId` AS `scStudentId`,`ta`.`scTermId` AS `scTermId`,`ta`.`scClassId` AS `scClassId`,`ta`.`scCourseId` AS `scCourseId`,`ta`.`scScore` AS `scScore`,`ta`.`scStatus` AS `scStatus`,`ta`.`scCreateDate` AS `scCreateDate`,`ta`.`scTeacherId` AS `scTeacherId`,`tb`.`sid` AS `sId`,`tb`.`sNo` AS `sNo`,`tb`.`sName` AS `sName`,`tb`.`sSex` AS `sSex`,`tb`.`sTel` AS `sTel`,`tb`.`sIdcard` AS `sIdcard`,`tb`.`sAddress` AS `sAddress`,`tb`.`sCreateDate` AS `sCreateDate`,`tb`.`sAccountId` AS `sAccountId`,`tb`.`className` AS `className`,`tb`.`classCode` AS `classCode`,`tb`.`mName` AS `majorName`,`tb`.`collegeName` AS `collegeName`,`tc`.`yYear` AS `yYear`,`tc`.`yTerm` AS `yTerm`,`tc`.`yAll` AS `yAll`,`td`.`crCode` AS `crCode`,`td`.`crName` AS `crName`,`te`.`tNo` AS `tNo`,`te`.`tName` AS `tName`,`te`.`tSex` AS `tSex`,`te`.`tTel` AS `tTel`,`te`.`tEduLevel` AS `tEduLevel`,`te`.`tSchool` AS `tSchool`,`te`.`tAddress` AS `tAddress` from ((((`scoreinfo` `ta` join `vwstudent` `tb` on((`ta`.`scStudentId` = `tb`.`sid`))) join `yearterm` `tc` on((`ta`.`scTermId` = `tc`.`yId`))) join `courseinfo` `td` on((`ta`.`scCourseId` = `td`.`crId`))) left join `teacherinfo` `te` on((`ta`.`scTeacherId` = `te`.`tId`)));

-- ----------------------------
-- View structure for vwscores
-- ----------------------------
DROP VIEW IF EXISTS `vwscores`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwscores` AS select `ta`.`scId` AS `scId`,`ta`.`scStudentId` AS `scStudentId`,`ta`.`scTermId` AS `scTermId`,`ta`.`scClassId` AS `scClassId`,`ta`.`scCourseId` AS `scCourseId`,`ta`.`scScore` AS `scScore`,`ta`.`scStatus` AS `scStatus`,`ta`.`scCreateDate` AS `scCreateDate`,`ta`.`scTeacherId` AS `scTeacherId`,`tb`.`sid` AS `sid`,`tb`.`sNo` AS `sNo`,`tb`.`sName` AS `sName`,`tb`.`sSex` AS `sSex`,`tb`.`sTel` AS `sTel`,`tb`.`sIdcard` AS `sIdcard`,`tb`.`sAddress` AS `sAddress`,`tb`.`sCreateDate` AS `sCreateDate`,`tb`.`sAccountId` AS `sAccountId`,`tb`.`sClassId` AS `sClassId`,`tb`.`cGrade` AS `cGrade`,`tb`.`className` AS `className`,`tb`.`classCode` AS `classCode`,`tc`.`yYear` AS `yYear`,`tc`.`yTerm` AS `yTerm`,`tc`.`yAll` AS `yAll`,`td`.`crCode` AS `crCode`,`td`.`crName` AS `crName`,`te`.`tNo` AS `tNo`,`te`.`tName` AS `tName`,`te`.`tSex` AS `tSex`,`te`.`tTel` AS `tTel`,`te`.`tEduLevel` AS `tEduLevel`,`te`.`tSchool` AS `tSchool`,`te`.`tAddress` AS `tAddress` from ((((`scoreinfo` `ta` join `vwstudent` `tb` on((`ta`.`scStudentId` = `tb`.`sid`))) join `yearterm` `tc` on((`ta`.`scTermId` = `tc`.`yId`))) join `courseinfo` `td` on((`ta`.`scCourseId` = `td`.`crId`))) left join `teacherinfo` `te` on((`ta`.`scTeacherId` = `te`.`tId`)));

-- ----------------------------
-- View structure for vwstudent
-- ----------------------------
DROP VIEW IF EXISTS `vwstudent`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwstudent` AS select `ta`.`sid` AS `sid`,`ta`.`sNo` AS `sNo`,`ta`.`sName` AS `sName`,`ta`.`sSex` AS `sSex`,`ta`.`sClassId` AS `sClassId`,`ta`.`sTel` AS `sTel`,`ta`.`sIdcard` AS `sIdcard`,`ta`.`sAddress` AS `sAddress`,`ta`.`sCreateDate` AS `sCreateDate`,`ta`.`sAccountId` AS `sAccountId`,`tb`.`cMajorId` AS `cMajorId`,`tb`.`cGrade` AS `cGrade`,`tb`.`cName` AS `className`,`tb`.`cCode` AS `classCode`,`tc`.`mName` AS `mName`,`tc`.`mCode` AS `mCode`,`tc`.`mCollegeId` AS `mCollegeId`,`td`.`cName` AS `collegeName`,`td`.`cCode` AS `collegeCode` from (((`studentinfo` `ta` left join `classinfo` `tb` on((`ta`.`sClassId` = `tb`.`cId`))) left join `majorinfo` `tc` on((`tb`.`cMajorId` = `tc`.`mid`))) left join `collegeinfo` `td` on((`tc`.`mCollegeId` = `td`.`CID`)));

-- ----------------------------
-- View structure for vwteacher
-- ----------------------------
DROP VIEW IF EXISTS `vwteacher`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwteacher` AS select `ta`.`tId` AS `tId`,`ta`.`tNo` AS `tNo`,`ta`.`tName` AS `tName`,`ta`.`tSex` AS `tSex`,`ta`.`tTel` AS `tTel`,`ta`.`tEduLevel` AS `tEduLevel`,`ta`.`tSchool` AS `tSchool`,`ta`.`tAddress` AS `tAddress`,`ta`.`tAccountId` AS `tAccountId`,`ta`.`tCollegeId` AS `tCollegeId`,`tb`.`cName` AS `collegeName`,`tb`.`cCode` AS `collegeCode`,`tb`.`cParentId` AS `cParentId`,`tc`.`cName` AS `cNamet`,`tc`.`cCode` AS `cCodet`,`tc`.`cParentId` AS `cParentIdt` from ((`teacherinfo` `ta` left join `collegeinfo` `tb` on((`ta`.`tCollegeId` = `tb`.`CID`))) left join `collegeinfo` `tc` on((`tb`.`cParentId` = `tc`.`CID`)));

-- ----------------------------
-- View structure for vwteaching
-- ----------------------------
DROP VIEW IF EXISTS `vwteaching`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `vwteaching` AS select `ta`.`tcId` AS `tcId`,`ta`.`tcTermId` AS `tcTermId`,`ta`.`tcClassId` AS `tcClassId`,`ta`.`tcCourseId` AS `tcCourseId`,`ta`.`tcTeacherId` AS `tcTeacherId`,`tb`.`yYear` AS `yYear`,`tb`.`yTerm` AS `yTerm`,`tb`.`yAll` AS `yAll`,`tc`.`cMajorId` AS `cMajorId`,`tc`.`cGrade` AS `cGrade`,`tc`.`cName` AS `className`,`tc`.`cCode` AS `classCode`,`td`.`crCode` AS `crCode`,`td`.`crName` AS `crName`,`te`.`tNo` AS `tNo`,`te`.`tName` AS `tName`,`te`.`tSex` AS `tSex`,`te`.`tTel` AS `tTel`,`te`.`tEduLevel` AS `tEduLevel`,`te`.`tSchool` AS `tSchool`,`te`.`tAddress` AS `tAddress` from ((((`teaching` `ta` join `yearterm` `tb` on((`ta`.`tcTermId` = `tb`.`yId`))) join `vwclass` `tc` on((`ta`.`tcClassId` = `tc`.`cId`))) join `courseinfo` `td` on((`ta`.`tcCourseId` = `td`.`crId`))) join `teacherinfo` `te` on((`ta`.`tcTeacherId` = `te`.`tId`)));

SET FOREIGN_KEY_CHECKS = 1;
