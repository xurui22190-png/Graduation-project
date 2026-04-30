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

 Date: 29/04/2026 14:39:13
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
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '班级信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classinfo
-- ----------------------------
INSERT INTO `classinfo` VALUES (1, 2, 2021, '计科2101班', 'CS2101');

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '院系信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collegeinfo
-- ----------------------------
INSERT INTO `collegeinfo` VALUES (1, '1001', '计算机学院', 0);
INSERT INTO `collegeinfo` VALUES (2, '100101', '计算机科学与技术', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courseinfo
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程知识点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of knowledge_point
-- ----------------------------
INSERT INTO `knowledge_point` VALUES (1, 1, '基础概念与定义', '2026-04-23 16:55:46');
INSERT INTO `knowledge_point` VALUES (2, 1, '核心公式与定理', '2026-04-23 16:55:46');
INSERT INTO `knowledge_point` VALUES (3, 1, '综合应用与计算', '2026-04-23 16:55:46');
INSERT INTO `knowledge_point` VALUES (4, 1, '拓展与创新思维', '2026-04-23 16:55:46');
INSERT INTO `knowledge_point` VALUES (5, 2, '基础语法与数据类型', '2026-04-23 17:57:54');
INSERT INTO `knowledge_point` VALUES (6, 2, '面向对象编程思想', '2026-04-23 17:57:54');
INSERT INTO `knowledge_point` VALUES (7, 2, '集合框架与泛型', '2026-04-23 17:57:54');
INSERT INTO `knowledge_point` VALUES (8, 2, '异常处理与多线程', '2026-04-23 17:57:54');
INSERT INTO `knowledge_point` VALUES (9, 4, '软件生命周期与过程模型', '2026-04-24 15:09:11');
INSERT INTO `knowledge_point` VALUES (10, 4, '需求分析与建模', '2026-04-24 15:09:11');
INSERT INTO `knowledge_point` VALUES (11, 4, '软件架构与系统设计', '2026-04-24 15:09:11');
INSERT INTO `knowledge_point` VALUES (12, 4, '软件测试与质量保证', '2026-04-24 15:09:11');

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
) ENGINE = InnoDB AUTO_INCREMENT = 5204 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of majorinfo
-- ----------------------------
INSERT INTO `majorinfo` VALUES (1001, '软件工程', 'SE01', 11);
INSERT INTO `majorinfo` VALUES (1002, '软件测试', 'SE02', 11);
INSERT INTO `majorinfo` VALUES (1003, '移动应用开发', 'SE03', 11);
INSERT INTO `majorinfo` VALUES (1101, '计算机科学与技术', 'CS01', 12);
INSERT INTO `majorinfo` VALUES (1102, '大数据技术', 'CS02', 12);
INSERT INTO `majorinfo` VALUES (1103, '云计算技术', 'CS03', 12);
INSERT INTO `majorinfo` VALUES (1201, '网络工程', 'NE01', 13);
INSERT INTO `majorinfo` VALUES (1202, '信息安全', 'NE02', 13);
INSERT INTO `majorinfo` VALUES (1203, '物联网工程', 'NE03', 13);
INSERT INTO `majorinfo` VALUES (1301, '人工智能', 'AI01', 14);
INSERT INTO `majorinfo` VALUES (1302, '智能科学与技术', 'AI02', 14);
INSERT INTO `majorinfo` VALUES (1303, '数据科学与大数据技术', 'AI03', 14);
INSERT INTO `majorinfo` VALUES (2001, '工商管理', 'BA01', 21);
INSERT INTO `majorinfo` VALUES (2002, '市场营销', 'BA02', 21);
INSERT INTO `majorinfo` VALUES (2003, '人力资源管理', 'BA03', 21);
INSERT INTO `majorinfo` VALUES (2101, '会计学', 'AC01', 22);
INSERT INTO `majorinfo` VALUES (2102, '财务管理', 'AC02', 22);
INSERT INTO `majorinfo` VALUES (2103, '审计学', 'AC03', 22);
INSERT INTO `majorinfo` VALUES (2201, '电子商务', 'EC01', 23);
INSERT INTO `majorinfo` VALUES (2202, '跨境电子商务', 'EC02', 23);
INSERT INTO `majorinfo` VALUES (2203, '现代物流管理', 'EC03', 23);
INSERT INTO `majorinfo` VALUES (2301, '金融学', 'FN01', 24);
INSERT INTO `majorinfo` VALUES (2302, '投资学', 'FN02', 24);
INSERT INTO `majorinfo` VALUES (2303, '互联网金融', 'FN03', 24);
INSERT INTO `majorinfo` VALUES (3001, '英语', 'EN01', 31);
INSERT INTO `majorinfo` VALUES (3002, '英语教育', 'EN02', 31);
INSERT INTO `majorinfo` VALUES (3003, '翻译', 'EN03', 31);
INSERT INTO `majorinfo` VALUES (3101, '日语', 'JP01', 32);
INSERT INTO `majorinfo` VALUES (3102, '商务日语', 'JP02', 32);
INSERT INTO `majorinfo` VALUES (3201, '商务英语', 'BE01', 33);
INSERT INTO `majorinfo` VALUES (3202, '国际商务英语', 'BE02', 33);
INSERT INTO `majorinfo` VALUES (4001, '视觉传达设计', 'VD01', 41);
INSERT INTO `majorinfo` VALUES (4002, '广告设计与制作', 'VD02', 41);
INSERT INTO `majorinfo` VALUES (4101, '环境设计', 'ED01', 42);
INSERT INTO `majorinfo` VALUES (4102, '室内艺术设计', 'ED02', 42);
INSERT INTO `majorinfo` VALUES (4201, '数字媒体艺术', 'DM01', 43);
INSERT INTO `majorinfo` VALUES (4202, '动画', 'DM02', 43);
INSERT INTO `majorinfo` VALUES (4203, '影视摄影与制作', 'DM03', 43);
INSERT INTO `majorinfo` VALUES (5001, '机械设计制造及其自动化', 'ME01', 51);
INSERT INTO `majorinfo` VALUES (5002, '机械电子工程', 'ME02', 51);
INSERT INTO `majorinfo` VALUES (5101, '工业机器人技术', 'RB01', 52);
INSERT INTO `majorinfo` VALUES (5102, '智能机器人技术', 'RB02', 52);
INSERT INTO `majorinfo` VALUES (5201, '自动化', 'AU01', 53);
INSERT INTO `majorinfo` VALUES (5202, '智能控制技术', 'AU02', 53);
INSERT INTO `majorinfo` VALUES (5203, '电气工程及其自动化', 'AU03', 53);

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of noticeinfo
-- ----------------------------
INSERT INTO `noticeinfo` VALUES (1, '关于期中考试安排的通知', '请各班学生于第八周关注期中考试具体安排，按时参加考试。', '考试通知', 1, 1, 1, '管理员', '2026-03-23 17:26:31', '2026-03-23 17:26:31', 0);
INSERT INTO `noticeinfo` VALUES (2, '系统维护公告', '本周六晚22:00至23:30系统将进行维护，期间部分功能可能无法使用。', '系统公告', 0, 1, 1, '管理员', '2026-03-23 17:26:31', '2026-03-23 17:26:31', 0);
INSERT INTO `noticeinfo` VALUES (3, '关于提交课程作业的提醒', '请相关课程学生于本周日前完成作业提交，逾期将影响平时成绩。', '课程通知', 0, 1, 2, '张老师', '2026-03-23 17:26:31', '2026-03-23 17:26:31', 0);
INSERT INTO `noticeinfo` VALUES (4, '测试', '测试', '系统公告', 1, 1, 1, '管理员', '2026-03-23 09:35:49', '2026-03-23 09:35:49', 0);
INSERT INTO `noticeinfo` VALUES (5, '【专属推送】【学业预警】请关注您的近期学习状态', '亲爱的 胡雪 同学：\n\n系统检测到您在近期学习中存在一定风险。\nAI 诊断分析如下：\nAI模型检测到该生平时成绩呈下滑趋势，且出勤率低于安全阈值，预估不及格风险达 16%\n\n请您务必引起重视，并尽快与任课老师或辅导员取得联系，及时调整学习计划！', '学业预警', 1, 1, 0, 'AI学业指导中心', '2026-04-27 15:39:20', '2026-04-27 15:39:19', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 369 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生知识点成绩明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score_detail
-- ----------------------------
INSERT INTO `score_detail` VALUES (129, 33, 2, 5, 100, 84, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (130, 33, 2, 6, 100, 82.6, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (131, 33, 2, 7, 100, 68.4, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (132, 33, 2, 8, 100, 90.1, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (133, 34, 2, 5, 100, 62.8, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (134, 34, 2, 6, 100, 72.6, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (135, 34, 2, 7, 100, 86.5, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (136, 34, 2, 8, 100, 67.4, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (137, 35, 2, 5, 100, 59.7, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (138, 35, 2, 6, 100, 58.8, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (139, 35, 2, 7, 100, 48.9, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (140, 35, 2, 8, 100, 38.7, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (141, 36, 2, 5, 100, 52, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (142, 36, 2, 6, 100, 62.6, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (143, 36, 2, 7, 100, 58.1, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (144, 36, 2, 8, 100, 43.3, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (145, 37, 2, 5, 100, 53.8, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (146, 37, 2, 6, 100, 69.4, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (147, 37, 2, 7, 100, 52, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (148, 37, 2, 8, 100, 38.8, '2026-04-24 22:25:03');
INSERT INTO `score_detail` VALUES (329, 6, 2, 5, 100, 46.1, '2026-04-25 14:42:11');
INSERT INTO `score_detail` VALUES (330, 6, 2, 6, 100, 51.3, '2026-04-25 14:42:11');
INSERT INTO `score_detail` VALUES (331, 6, 2, 7, 100, 42.9, '2026-04-25 14:42:11');
INSERT INTO `score_detail` VALUES (332, 6, 2, 8, 100, 61.3, '2026-04-25 14:42:11');
INSERT INTO `score_detail` VALUES (333, 12, 4, 9, 100, 65.1, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (334, 12, 4, 10, 100, 91.6, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (335, 12, 4, 11, 100, 87.7, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (336, 12, 4, 12, 100, 87.1, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (337, 13, 4, 9, 100, 52.3, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (338, 13, 4, 10, 100, 44.9, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (339, 13, 4, 11, 100, 47, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (340, 13, 4, 12, 100, 53.6, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (341, 14, 4, 9, 100, 75.8, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (342, 14, 4, 10, 100, 82.5, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (343, 14, 4, 11, 100, 87.8, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (344, 14, 4, 12, 100, 68, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (345, 39, 4, 9, 100, 78.3, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (346, 39, 4, 10, 100, 77.7, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (347, 39, 4, 11, 100, 89.6, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (348, 39, 4, 12, 100, 72.1, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (349, 40, 4, 9, 100, 82, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (350, 40, 4, 10, 100, 76, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (351, 40, 4, 11, 100, 79.2, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (352, 40, 4, 12, 100, 68.6, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (353, 42, 4, 9, 100, 87.3, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (354, 42, 4, 10, 100, 89.3, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (355, 42, 4, 11, 100, 87.9, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (356, 42, 4, 12, 100, 88.8, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (357, 43, 4, 9, 100, 71.4, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (358, 43, 4, 10, 100, 56.6, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (359, 43, 4, 11, 100, 42.4, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (360, 43, 4, 12, 100, 53.4, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (361, 44, 4, 9, 100, 47.5, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (362, 44, 4, 10, 100, 63.9, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (363, 44, 4, 11, 100, 39.4, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (364, 44, 4, 12, 100, 51.8, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (365, 48, 4, 9, 100, 68.5, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (366, 48, 4, 10, 100, 66.7, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (367, 48, 4, 11, 100, 29.7, '2026-04-25 18:19:27');
INSERT INTO `score_detail` VALUES (368, 48, 4, 12, 100, 40.4, '2026-04-25 18:19:27');

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
  PRIMARY KEY (`scId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 170 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '成绩信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scoreinfo
-- ----------------------------
INSERT INTO `scoreinfo` VALUES (1, 8, 1, 2, 1, 85.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (2, 9, 1, 2, 1, 78.5, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (4, 8, 1, 2, 2, 80.0, 1, '2026-03-21 14:59:01', 2);
INSERT INTO `scoreinfo` VALUES (5, 9, 1, 2, 2, 74.5, 1, '2026-03-21 14:59:01', 2);
INSERT INTO `scoreinfo` VALUES (8, 12, 2, 3, 1, 84.5, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (9, 13, 2, 3, 1, 91.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (10, 14, 2, 3, 1, 67.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (12, 12, 2, 3, 3, 79.5, 1, '2026-03-21 14:59:01', 3);
INSERT INTO `scoreinfo` VALUES (13, 13, 2, 3, 3, 95.0, 1, '2026-03-21 14:59:01', 3);
INSERT INTO `scoreinfo` VALUES (14, 14, 2, 3, 3, 70.0, 1, '2026-03-21 14:59:01', 3);
INSERT INTO `scoreinfo` VALUES (30, 30, 2, 1, 2, 70.0, 1, '2026-03-23 17:21:50', 2);
INSERT INTO `scoreinfo` VALUES (31, 14, 3, 3, 3, 89.0, 1, '2026-03-23 17:22:01', 1);
INSERT INTO `scoreinfo` VALUES (32, 14, 3, 3, 2, 0.0, 0, '2026-03-25 14:17:20', 2);
INSERT INTO `scoreinfo` VALUES (33, 33, 3, 4, 1, 86.0, 1, '2026-03-25 11:00:00', 1);
INSERT INTO `scoreinfo` VALUES (34, 33, 3, 4, 2, 82.5, 1, '2026-03-25 11:00:00', 2);
INSERT INTO `scoreinfo` VALUES (35, 33, 3, 4, 3, 90.0, 1, '2026-03-25 11:00:00', 10);
INSERT INTO `scoreinfo` VALUES (36, 34, 3, 4, 1, 78.0, 1, '2026-03-25 11:00:00', 1);
INSERT INTO `scoreinfo` VALUES (37, 34, 3, 4, 2, 81.0, 1, '2026-03-25 11:00:00', 2);
INSERT INTO `scoreinfo` VALUES (38, 34, 3, 4, 3, 84.5, 1, '2026-03-25 11:00:00', 10);
INSERT INTO `scoreinfo` VALUES (39, 35, 3, 4, 1, 91.5, 1, '2026-03-25 11:00:00', 1);
INSERT INTO `scoreinfo` VALUES (40, 35, 3, 4, 2, 89.0, 1, '2026-03-25 11:00:00', 2);
INSERT INTO `scoreinfo` VALUES (41, 35, 3, 4, 3, 93.0, 1, '2026-03-25 11:00:00', 10);
INSERT INTO `scoreinfo` VALUES (42, 36, 3, 4, 1, 69.0, 1, '2026-03-25 11:00:00', 1);
INSERT INTO `scoreinfo` VALUES (43, 36, 3, 4, 2, 73.5, 1, '2026-03-25 11:00:00', 2);
INSERT INTO `scoreinfo` VALUES (44, 36, 3, 4, 3, 76.0, 1, '2026-03-25 11:00:00', 10);
INSERT INTO `scoreinfo` VALUES (45, 37, 3, 4, 1, 62.0, 1, '2026-03-25 11:00:00', 1);
INSERT INTO `scoreinfo` VALUES (46, 37, 3, 4, 2, 65.0, 1, '2026-03-25 11:00:00', 2);
INSERT INTO `scoreinfo` VALUES (47, 37, 3, 4, 3, 70.5, 1, '2026-03-25 11:00:00', 10);
INSERT INTO `scoreinfo` VALUES (48, 38, 3, 4, 1, 88.0, 1, '2026-03-25 11:00:00', 1);
INSERT INTO `scoreinfo` VALUES (49, 38, 3, 4, 2, 86.5, 1, '2026-03-25 11:00:00', 2);
INSERT INTO `scoreinfo` VALUES (50, 38, 3, 4, 3, 91.0, 1, '2026-03-25 11:00:00', 10);
INSERT INTO `scoreinfo` VALUES (51, 39, 2, 3, 1, 77.5, 1, '2026-03-25 11:15:00', 1);
INSERT INTO `scoreinfo` VALUES (52, 39, 2, 3, 2, 79.0, 1, '2026-03-25 11:15:00', 2);
INSERT INTO `scoreinfo` VALUES (53, 39, 2, 3, 3, 83.0, 1, '2026-03-25 11:15:00', 3);
INSERT INTO `scoreinfo` VALUES (54, 40, 2, 3, 1, 85.0, 1, '2026-03-25 11:15:00', 1);
INSERT INTO `scoreinfo` VALUES (55, 40, 2, 3, 2, 87.5, 1, '2026-03-25 11:15:00', 2);
INSERT INTO `scoreinfo` VALUES (56, 40, 2, 3, 3, 89.0, 1, '2026-03-25 11:15:00', 3);
INSERT INTO `scoreinfo` VALUES (60, 42, 2, 3, 1, 90.0, 1, '2026-03-25 11:15:00', 1);
INSERT INTO `scoreinfo` VALUES (61, 42, 2, 3, 2, 92.0, 1, '2026-03-25 11:15:00', 2);
INSERT INTO `scoreinfo` VALUES (62, 42, 2, 3, 3, 88.0, 1, '2026-03-25 11:15:00', 3);
INSERT INTO `scoreinfo` VALUES (63, 43, 2, 3, 1, 66.0, 1, '2026-03-25 11:15:00', 1);
INSERT INTO `scoreinfo` VALUES (64, 43, 2, 3, 2, 70.5, 1, '2026-03-25 11:15:00', 2);
INSERT INTO `scoreinfo` VALUES (65, 43, 2, 3, 3, 72.0, 1, '2026-03-25 11:15:00', 3);
INSERT INTO `scoreinfo` VALUES (66, 44, 2, 3, 1, 81.5, 1, '2026-03-25 11:15:00', 1);
INSERT INTO `scoreinfo` VALUES (67, 44, 2, 3, 2, 84.0, 1, '2026-03-25 11:15:00', 2);
INSERT INTO `scoreinfo` VALUES (68, 44, 2, 3, 3, 86.5, 1, '2026-03-25 11:15:00', 3);
INSERT INTO `scoreinfo` VALUES (69, 45, 1, 2, 1, 87.0, 1, '2026-03-25 11:30:00', 1);
INSERT INTO `scoreinfo` VALUES (70, 45, 1, 2, 2, 84.5, 1, '2026-03-25 11:30:00', 2);
INSERT INTO `scoreinfo` VALUES (71, 45, 1, 2, 3, 80.0, 1, '2026-03-25 11:30:00', 3);
INSERT INTO `scoreinfo` VALUES (72, 46, 1, 2, 1, 93.0, 1, '2026-03-25 11:30:00', 1);
INSERT INTO `scoreinfo` VALUES (73, 46, 1, 2, 2, 91.5, 1, '2026-03-25 11:30:00', 2);
INSERT INTO `scoreinfo` VALUES (74, 46, 1, 2, 3, 94.0, 1, '2026-03-25 11:30:00', 3);
INSERT INTO `scoreinfo` VALUES (75, 47, 1, 2, 1, 75.5, 1, '2026-03-25 11:30:00', 1);
INSERT INTO `scoreinfo` VALUES (76, 47, 1, 2, 2, 71.0, 1, '2026-03-25 11:30:00', 2);
INSERT INTO `scoreinfo` VALUES (77, 47, 1, 2, 3, 73.5, 1, '2026-03-25 11:30:00', 3);
INSERT INTO `scoreinfo` VALUES (78, 48, 1, 2, 1, 68.0, 1, '2026-03-25 11:30:00', 1);
INSERT INTO `scoreinfo` VALUES (79, 48, 1, 2, 2, 72.5, 1, '2026-03-25 11:30:00', 2);
INSERT INTO `scoreinfo` VALUES (80, 48, 1, 2, 3, 77.0, 1, '2026-03-25 11:30:00', 3);
INSERT INTO `scoreinfo` VALUES (81, 33, 4, 4, 3, 0.0, 0, '2026-03-25 11:40:00', 10);
INSERT INTO `scoreinfo` VALUES (82, 34, 4, 4, 3, 0.0, 0, '2026-03-25 11:40:00', 10);
INSERT INTO `scoreinfo` VALUES (83, 35, 4, 4, 3, 0.0, 0, '2026-03-25 11:40:00', 10);
INSERT INTO `scoreinfo` VALUES (84, 39, 4, 3, 2, 0.0, 0, '2026-03-25 11:40:00', 2);
INSERT INTO `scoreinfo` VALUES (85, 40, 4, 3, 2, 0.0, 0, '2026-03-25 11:40:00', 2);
INSERT INTO `scoreinfo` VALUES (87, 1, 2, 1, 1, 89.0, 1, '2026-03-26 10:00:00', 1);
INSERT INTO `scoreinfo` VALUES (88, 1, 2, 1, 2, 86.5, 1, '2026-03-26 10:00:00', 2);
INSERT INTO `scoreinfo` VALUES (89, 1, 2, 1, 14, 82.0, 1, '2026-03-26 10:00:00', 3);
INSERT INTO `scoreinfo` VALUES (90, 1, 2, 1, 20, 84.5, 1, '2026-03-26 10:00:00', 10);
INSERT INTO `scoreinfo` VALUES (91, 1, 2, 1, 24, 87.0, 1, '2026-03-26 10:00:00', 12);
INSERT INTO `scoreinfo` VALUES (92, 2, 2, 1, 3, 91.0, 1, '2026-03-26 10:05:00', 3);
INSERT INTO `scoreinfo` VALUES (93, 2, 2, 1, 8, 85.5, 1, '2026-03-26 10:05:00', 2);
INSERT INTO `scoreinfo` VALUES (94, 2, 2, 1, 16, 79.0, 1, '2026-03-26 10:05:00', 10);
INSERT INTO `scoreinfo` VALUES (95, 2, 2, 1, 20, 88.0, 1, '2026-03-26 10:05:00', 10);
INSERT INTO `scoreinfo` VALUES (96, 2, 2, 1, 25, 93.0, 1, '2026-03-26 10:05:00', 12);
INSERT INTO `scoreinfo` VALUES (102, 12, 3, 3, 2, 92.0, 1, '2026-03-26 10:15:00', 2);
INSERT INTO `scoreinfo` VALUES (103, 12, 3, 3, 9, 89.5, 1, '2026-03-26 10:15:00', 3);
INSERT INTO `scoreinfo` VALUES (104, 12, 3, 3, 16, 90.0, 1, '2026-03-26 10:15:00', 10);
INSERT INTO `scoreinfo` VALUES (105, 12, 3, 3, 21, 94.0, 1, '2026-03-26 10:15:00', 10);
INSERT INTO `scoreinfo` VALUES (106, 12, 3, 3, 24, 88.5, 1, '2026-03-26 10:15:00', 12);
INSERT INTO `scoreinfo` VALUES (107, 30, 2, 1, 1, 72.0, 1, '2026-03-26 10:20:00', 1);
INSERT INTO `scoreinfo` VALUES (108, 30, 2, 1, 2, 68.5, 1, '2026-03-26 10:20:00', 2);
INSERT INTO `scoreinfo` VALUES (109, 30, 2, 1, 14, 74.0, 1, '2026-03-26 10:20:00', 3);
INSERT INTO `scoreinfo` VALUES (110, 30, 2, 1, 20, 70.5, 1, '2026-03-26 10:20:00', 10);
INSERT INTO `scoreinfo` VALUES (111, 30, 2, 1, 24, 76.0, 1, '2026-03-26 10:20:00', 12);
INSERT INTO `scoreinfo` VALUES (112, 14, 4, 3, 3, 87.5, 1, '2026-03-26 10:25:00', 3);
INSERT INTO `scoreinfo` VALUES (113, 14, 4, 3, 8, 82.0, 1, '2026-03-26 10:25:00', 2);
INSERT INTO `scoreinfo` VALUES (114, 14, 4, 3, 15, 79.5, 1, '2026-03-26 10:25:00', 3);
INSERT INTO `scoreinfo` VALUES (115, 14, 4, 3, 20, 85.0, 1, '2026-03-26 10:25:00', 10);
INSERT INTO `scoreinfo` VALUES (116, 14, 4, 3, 25, 91.0, 1, '2026-03-26 10:25:00', 12);
INSERT INTO `scoreinfo` VALUES (117, 1, 4, 1, 1, 88.0, 1, '2026-03-26 14:00:00', 1);
INSERT INTO `scoreinfo` VALUES (118, 1, 4, 1, 2, 84.5, 1, '2026-03-26 14:00:00', 2);
INSERT INTO `scoreinfo` VALUES (119, 1, 4, 1, 3, 91.0, 1, '2026-03-26 14:00:00', 3);
INSERT INTO `scoreinfo` VALUES (120, 1, 4, 1, 4, 86.0, 1, '2026-03-26 14:00:00', 1);
INSERT INTO `scoreinfo` VALUES (121, 1, 4, 1, 5, 82.5, 1, '2026-03-26 14:00:00', 1);
INSERT INTO `scoreinfo` VALUES (122, 1, 4, 1, 6, 87.5, 1, '2026-03-26 14:00:00', 2);
INSERT INTO `scoreinfo` VALUES (123, 1, 4, 1, 7, 89.0, 1, '2026-03-26 14:00:00', 2);
INSERT INTO `scoreinfo` VALUES (131, 14, 4, 3, 14, 81.0, 1, '2026-03-26 14:10:00', 3);
INSERT INTO `scoreinfo` VALUES (132, 14, 4, 3, 16, 85.0, 1, '2026-03-26 14:10:00', 10);
INSERT INTO `scoreinfo` VALUES (133, 14, 4, 3, 17, 82.0, 1, '2026-03-26 14:10:00', 10);
INSERT INTO `scoreinfo` VALUES (134, 14, 4, 3, 18, 78.5, 1, '2026-03-26 14:10:00', 12);
INSERT INTO `scoreinfo` VALUES (135, 14, 4, 3, 19, 80.0, 1, '2026-03-26 14:10:00', 12);
INSERT INTO `scoreinfo` VALUES (138, 2, 4, 1, 20, 92.0, 1, '2026-03-26 14:15:00', 10);
INSERT INTO `scoreinfo` VALUES (139, 2, 4, 1, 21, 90.5, 1, '2026-03-26 14:15:00', 10);
INSERT INTO `scoreinfo` VALUES (140, 2, 4, 1, 22, 87.0, 1, '2026-03-26 14:15:00', 10);
INSERT INTO `scoreinfo` VALUES (141, 2, 4, 1, 23, 89.0, 1, '2026-03-26 14:15:00', 10);
INSERT INTO `scoreinfo` VALUES (142, 2, 4, 1, 24, 91.5, 1, '2026-03-26 14:15:00', 12);
INSERT INTO `scoreinfo` VALUES (143, 2, 4, 1, 25, 93.0, 1, '2026-03-26 14:15:00', 12);
INSERT INTO `scoreinfo` VALUES (145, 47, 4, 2, 1, 80.0, 1, '2026-04-21 20:17:39', 1);
INSERT INTO `scoreinfo` VALUES (146, 12, 3, 3, 4, 79.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (147, 13, 3, 3, 4, 62.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (148, 14, 3, 3, 4, 77.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (149, 39, 3, 3, 4, 77.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (150, 40, 3, 3, 4, 79.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (152, 42, 3, 3, 4, 93.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (153, 43, 3, 3, 4, 66.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (154, 44, 3, 3, 4, 67.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (155, 48, 3, 3, 4, 57.0, 1, '2026-04-23 14:16:15', 1);
INSERT INTO `scoreinfo` VALUES (156, 45, 4, 2, 1, 80.0, 1, '2026-04-23 14:30:23', 1);
INSERT INTO `scoreinfo` VALUES (157, 33, 2, 4, 2, 77.0, 1, '2026-04-23 17:54:09', 1);
INSERT INTO `scoreinfo` VALUES (158, 34, 2, 4, 2, 77.0, 1, '2026-04-23 17:54:09', 1);
INSERT INTO `scoreinfo` VALUES (159, 35, 2, 4, 2, 60.0, 1, '2026-04-23 17:54:09', 1);
INSERT INTO `scoreinfo` VALUES (160, 36, 2, 4, 2, 61.0, 1, '2026-04-23 17:54:09', 1);
INSERT INTO `scoreinfo` VALUES (161, 37, 2, 4, 2, 66.0, 1, '2026-04-23 17:54:09', 1);
INSERT INTO `scoreinfo` VALUES (162, 6, 2, 2, 2, 61.0, 1, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (163, 7, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (164, 8, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (165, 9, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (166, 38, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (167, 45, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (168, 46, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);
INSERT INTO `scoreinfo` VALUES (169, 47, 2, 2, 2, 0.0, 0, '2026-04-24 14:53:09', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of studentinfo
-- ----------------------------
INSERT INTO `studentinfo` VALUES (1, '202300101', '张伟', '男', 1, '13800010001', '410101200501011231', '河南省郑州市中原区', '2026-03-20 22:48:14', 28, '未设定');
INSERT INTO `studentinfo` VALUES (2, '202300102', '王芳', '女', 1, '13800010002', '410101200502022248', '河南省郑州市金水区', '2026-03-20 22:48:14', 29, '未设定');
INSERT INTO `studentinfo` VALUES (3, '202300103', '李娜', '女', 1, '13800010003', '410101200503032426', '河南省洛阳市西工区', '2026-03-20 22:48:14', 30, '未设定');
INSERT INTO `studentinfo` VALUES (4, '202300104', '刘洋', '男', 1, '13800010004', '410101200504041257', '河南省开封市龙亭区', '2026-03-20 22:48:14', 31, '未设定');
INSERT INTO `studentinfo` VALUES (5, '202300105', '陈杰', '男', 1, '13800010005', '410101200505052913', '河南省新乡市红旗区', '2026-03-20 22:48:14', 32, '未设定');
INSERT INTO `studentinfo` VALUES (6, '202300106', '赵敏', '女', 2, '13800010006', '410101200506063629', '河南省安阳市文峰区', '2026-03-20 22:48:14', 33, '未设定');
INSERT INTO `studentinfo` VALUES (7, '202300107', '黄涛', '男', 2, '13800010007', '410101200507074511', '河南省焦作市解放区', '2026-03-20 22:48:14', 34, '未设定');
INSERT INTO `studentinfo` VALUES (8, '202300108', '周洁', '女', 2, '13800010008', '410101200508085026', '河南省许昌市魏都区', '2026-03-20 22:48:14', 35, '未设定');
INSERT INTO `studentinfo` VALUES (9, '202300109', '吴昊', '男', 2, '13800010009', '410101200509095438', '河南省平顶山市新华区', '2026-03-20 22:48:14', 36, '未设定');
INSERT INTO `studentinfo` VALUES (12, '202300112', '马琳', '女', 3, '13800010012', '410101200512128462', '河南省信阳市浉河区', '2026-03-20 22:48:14', 39, '未设定');
INSERT INTO `studentinfo` VALUES (13, '202300113', '朱强', '男', 3, '13800010013', '410101200601139274', '河南省周口市川汇区', '2026-03-20 22:48:14', 40, '未设定');
INSERT INTO `studentinfo` VALUES (14, '202300114', '胡雪', '女', 3, '13800010014', '410101200602140685', '河南省驻马店市驿城区', '2026-03-20 22:48:14', 41, '未设定');
INSERT INTO `studentinfo` VALUES (30, '202300110', '徐静', '女', 1, '13800010010', '410101200510106644', '河南省南阳市卧龙区', '2026-03-20 22:48:36', 37, '未设定');
INSERT INTO `studentinfo` VALUES (32, '22', '许墨', '男', 1, '22', '222', '2222', '2026-03-24 23:23:36', 79, '未设定');
INSERT INTO `studentinfo` VALUES (33, '202300115', '郭晨', '男', 4, '13800010015', '410101200603151896', '河南省漯河市源汇区', '2026-03-25 10:00:00', 81, '未设定');
INSERT INTO `studentinfo` VALUES (34, '202300116', '何丽', '女', 4, '13800010016', '410101200604162107', '河南省三门峡市湖滨区', '2026-03-25 10:00:00', 82, '未设定');
INSERT INTO `studentinfo` VALUES (35, '202300117', '高飞', '男', 4, '13800010017', '410101200605173318', '河南省鹤壁市淇滨区', '2026-03-25 10:00:00', 83, '未设定');
INSERT INTO `studentinfo` VALUES (36, '202300118', '罗静', '女', 4, '13800010018', '410101200606184529', '河南省驻马店市驿城区', '2026-03-25 10:00:00', 84, '未设定');
INSERT INTO `studentinfo` VALUES (37, '202300119', '郑凯', '男', 4, '13800010019', '410101200607195730', '河南省南阳市宛城区', '2026-03-25 10:00:00', 85, '未设定');
INSERT INTO `studentinfo` VALUES (38, '202300120', '宋雨欣', '女', 2, '13800010020', '410101200608206941', '河南省商丘市梁园区', '2026-03-25 10:00:00', 86, '未设定');
INSERT INTO `studentinfo` VALUES (39, '202300121', '杜浩', '男', 3, '13800010021', '410101200609218152', '河南省信阳市浉河区', '2026-03-25 10:00:00', 87, '未设定');
INSERT INTO `studentinfo` VALUES (40, '202300122', '潘婷', '女', 3, '13800010022', '410101200610229363', '河南省周口市川汇区', '2026-03-25 10:00:00', 88, '未设定');
INSERT INTO `studentinfo` VALUES (42, '202300124', '曹雪', '女', 3, '13800010024', '410101200612241785', '河南省焦作市解放区', '2026-03-25 10:00:00', 90, '未设定');
INSERT INTO `studentinfo` VALUES (43, '202300125', '彭宇', '男', 3, '13800010025', '410101200701251996', '河南省安阳市文峰区', '2026-03-25 10:00:00', 91, '未设定');
INSERT INTO `studentinfo` VALUES (44, '202300126', '苏雯', '女', 3, '13800010026', '410101200702262108', '河南省开封市龙亭区', '2026-03-25 10:00:00', 92, '未设定');
INSERT INTO `studentinfo` VALUES (45, '202300127', '蒋涛', '男', 2, '13800010027', '410101200703273319', '河南省郑州市中原区', '2026-03-25 10:00:00', 93, '未设定');
INSERT INTO `studentinfo` VALUES (46, '202300128', '许倩', '女', 2, '13800010028', '410101200704284520', '河南省郑州市金水区', '2026-03-25 10:00:00', 94, '未设定');
INSERT INTO `studentinfo` VALUES (47, '202300129', '黎明', '男', 2, '13800010029', '410101200705295731', '河南省洛阳市西工区', '2026-03-25 10:00:00', 95, '未设定');
INSERT INTO `studentinfo` VALUES (48, '202300130', '唐宁', '女', 3, '13800010030', '410101200706306942', '河南省新乡市红旗区', '2026-03-25 10:00:00', 96, '未设定');

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacherinfo
-- ----------------------------
INSERT INTO `teacherinfo` VALUES (1, 'T2024001', '杨老师', '男', '13800020001', '硕士', '华中科技大学', '武汉市洪山区', 59, 11);
INSERT INTO `teacherinfo` VALUES (2, 'T2024002', '孙老师', '女', '13800020002', '博士', '武汉大学', '武汉市武昌区', 60, 12);
INSERT INTO `teacherinfo` VALUES (3, 'T2024003', '郑老师', '男', '13800020003', '本科', '湖北大学', '武汉市江夏区', 61, 13);
INSERT INTO `teacherinfo` VALUES (4, 'T2024004', '马老师', '女', '13800020004', '硕士', '华中师范大学', '武汉市洪山区', 62, 14);
INSERT INTO `teacherinfo` VALUES (5, 'T2024005', '朱老师', '男', '13800020005', '博士', '中南财经政法大学', '武汉市汉阳区', 63, 21);
INSERT INTO `teacherinfo` VALUES (6, 'T2024006', '胡老师', '女', '13800020006', '硕士', '华中农业大学', '武汉市洪山区', 64, 22);
INSERT INTO `teacherinfo` VALUES (7, 'T2024007', '郭老师', '男', '13800020007', '本科', '武汉理工大学', '武汉市洪山区', 65, 11);
INSERT INTO `teacherinfo` VALUES (8, 'T2024008', '何老师', '女', '13800020008', '硕士', '湖北工业大学', '武汉市洪山区', 66, 12);
INSERT INTO `teacherinfo` VALUES (9, 'T2024009', '高老师', '男', '13800020009', '博士', '中国地质大学', '武汉市光谷', 67, 13);
INSERT INTO `teacherinfo` VALUES (10, 'T2024010', '林老师', '女', '13800020010', '硕士', '江汉大学', '武汉市沌口', 68, 14);
INSERT INTO `teacherinfo` VALUES (12, 'T2025001', '黄教授', '男', '17720276774', '硕士', '厦理', '后溪镇厦门理工学院', 80, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '授课安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching
-- ----------------------------
INSERT INTO `teaching` VALUES (1, 1, 1, 1, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (2, 1, 1, 1, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (3, 1, 1, 2, 2, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (4, 1, 2, 1, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (5, 1, 2, 3, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (6, 1, 3, 2, 2, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (7, 1, 3, 4, 4, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (8, 1, 4, 1, 2, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (9, 1, 4, 3, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (11, 2, 1, 3, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (12, 2, 2, 2, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (13, 2, 2, 4, 4, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (14, 2, 3, 1, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (15, 2, 3, 3, 2, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (16, 2, 4, 2, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (17, 2, 4, 4, 4, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (18, 3, 1, 2, 3, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (19, 3, 2, 3, 2, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (20, 3, 3, 4, 1, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (21, 3, 4, 1, 4, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (22, 4, 2, 2, 10, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (23, 4, 2, 3, 12, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (24, 3, 2, 3, 10, 0.50, 0.30, 0.20);
INSERT INTO `teaching` VALUES (25, 3, 1, 13, 12, 0.50, 0.30, 0.20);

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
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userinfo
-- ----------------------------

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
INSERT INTO `yearterm` VALUES (1, '2025', '第一学期', '2025-2026 第一学期');

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
