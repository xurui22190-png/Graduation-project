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

 Date: 23/04/2026 13:42:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '班级信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classinfo
-- ----------------------------
INSERT INTO `classinfo` VALUES (1, 1001, 2024, '软件工程1班', 'SE2024-1');
INSERT INTO `classinfo` VALUES (2, 1001, 2024, '软件工程2班', 'SE2024-2');
INSERT INTO `classinfo` VALUES (3, 1101, 2023, '计算机科学1班', 'CS2023-1');
INSERT INTO `classinfo` VALUES (4, 1101, 2023, '计算机科学2班', 'CS2023-2');
INSERT INTO `classinfo` VALUES (5, 1301, 2024, '人工智能1班', 'AI2024-1');
INSERT INTO `classinfo` VALUES (6, 1301, 2024, '人工智能2班', 'AI2024-2');
INSERT INTO `classinfo` VALUES (7, 1303, 2022, '数据科学1班', 'DS2022-1');
INSERT INTO `classinfo` VALUES (8, 1303, 2022, '数据科学2班', 'DS2022-2');
INSERT INTO `classinfo` VALUES (9, 1201, 2023, '网络工程1班', 'NE2023-1');
INSERT INTO `classinfo` VALUES (10, 1201, 2023, '网络工程2班', 'NE2023-2');
INSERT INTO `classinfo` VALUES (11, 1001, 2023, '软件工程2301班', 'SE2301');
INSERT INTO `classinfo` VALUES (12, 1001, 2023, '软件工程2302班', 'SE2302');
INSERT INTO `classinfo` VALUES (13, 1001, 2024, '软件工程2401班', 'SE2401');
INSERT INTO `classinfo` VALUES (14, 1101, 2023, '计算机科学2301班', 'CS2301');
INSERT INTO `classinfo` VALUES (15, 1101, 2024, '计算机科学2401班', 'CS2401');
INSERT INTO `classinfo` VALUES (16, 1201, 2023, '网络工程2301班', 'NE2301');
INSERT INTO `classinfo` VALUES (17, 1201, 2024, '网络工程2401班', 'NE2401');
INSERT INTO `classinfo` VALUES (18, 1202, 2023, '信息安全2301班', 'IS2301');
INSERT INTO `classinfo` VALUES (19, 1202, 2024, '信息安全2401班', 'IS2401');
INSERT INTO `classinfo` VALUES (20, 1301, 2023, '人工智能2301班', 'AI2301');

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
) ENGINE = InnoDB AUTO_INCREMENT = 526 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '院系信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collegeinfo
-- ----------------------------
INSERT INTO `collegeinfo` VALUES (1, '软件学院', '01', 0);
INSERT INTO `collegeinfo` VALUES (2, '经济管理学院', '02', 0);
INSERT INTO `collegeinfo` VALUES (3, '外国语学院', '03', 0);
INSERT INTO `collegeinfo` VALUES (4, '艺术设计学院', '04', 0);
INSERT INTO `collegeinfo` VALUES (5, '智能制造学院', '05', 0);
INSERT INTO `collegeinfo` VALUES (11, '软件工程系', '0101', 1);
INSERT INTO `collegeinfo` VALUES (12, '计算机科学与技术系', '0102', 1);
INSERT INTO `collegeinfo` VALUES (13, '网络工程系', '0103', 1);
INSERT INTO `collegeinfo` VALUES (14, '人工智能系', '0104', 1);
INSERT INTO `collegeinfo` VALUES (21, '工商管理系', '0201', 2);
INSERT INTO `collegeinfo` VALUES (22, '会计系', '0202', 2);
INSERT INTO `collegeinfo` VALUES (23, '电子商务系', '0203', 2);
INSERT INTO `collegeinfo` VALUES (24, '金融系', '0204', 2);
INSERT INTO `collegeinfo` VALUES (31, '英语系', '0301', 3);
INSERT INTO `collegeinfo` VALUES (32, '日语系', '0302', 3);
INSERT INTO `collegeinfo` VALUES (33, '商务英语系', '0303', 3);
INSERT INTO `collegeinfo` VALUES (41, '视觉传达设计系', '0401', 4);
INSERT INTO `collegeinfo` VALUES (42, '环境设计系', '0402', 4);
INSERT INTO `collegeinfo` VALUES (43, '数字媒体艺术系', '0403', 4);
INSERT INTO `collegeinfo` VALUES (51, '机械设计制造系', '0501', 5);
INSERT INTO `collegeinfo` VALUES (52, '工业机器人系', '0502', 5);
INSERT INTO `collegeinfo` VALUES (53, '智能控制系', '0503', 5);
INSERT INTO `collegeinfo` VALUES (111, 'Java开发方向', '010101', 11);
INSERT INTO `collegeinfo` VALUES (112, '前端开发方向', '010102', 11);
INSERT INTO `collegeinfo` VALUES (113, '移动开发方向', '010103', 11);
INSERT INTO `collegeinfo` VALUES (121, '大数据方向', '010201', 12);
INSERT INTO `collegeinfo` VALUES (122, '云计算方向', '010202', 12);
INSERT INTO `collegeinfo` VALUES (141, '机器学习方向', '010401', 14);
INSERT INTO `collegeinfo` VALUES (142, '自然语言处理方向', '010402', 14);
INSERT INTO `collegeinfo` VALUES (221, '财务会计方向', '020201', 22);
INSERT INTO `collegeinfo` VALUES (222, '审计方向', '020202', 22);
INSERT INTO `collegeinfo` VALUES (311, '英语教育方向', '030101', 31);
INSERT INTO `collegeinfo` VALUES (312, '翻译方向', '030102', 31);
INSERT INTO `collegeinfo` VALUES (431, '交互设计方向', '040301', 43);
INSERT INTO `collegeinfo` VALUES (432, '影视动画方向', '040302', 43);
INSERT INTO `collegeinfo` VALUES (521, '机器人应用方向', '050201', 52);
INSERT INTO `collegeinfo` VALUES (522, '机器人运维方向', '050202', 52);

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
INSERT INTO `courseinfo` VALUES (1, 'KC001', 'Java程序设计', 1001, 4.0, 64, '必修', '考试', '软件工程基础课程', 1, '2026-03-21 13:38:28', '2026-03-21 14:01:34');
INSERT INTO `courseinfo` VALUES (2, 'KC002', '数据库原理', 1001, 4.0, 64, '必修', '考试', '核心课程', 1, '2026-03-21 13:38:28', '2026-03-21 14:01:29');
INSERT INTO `courseinfo` VALUES (3, 'KC003', 'Web前端开发', 1001, 3.0, 48, '选修', '考查', '前端方向课程', 1, '2026-03-21 13:38:28', '2026-03-21 14:01:12');
INSERT INTO `courseinfo` VALUES (4, 'KC004', '软件工程导论', 1001, 3.0, 48, '必修', '考试', '软件工程基础课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (5, 'KC005', '软件测试', 1001, 3.0, 48, '选修', '考查', '软件质量保障课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (6, 'KC006', '移动应用开发基础', 1003, 4.0, 64, '必修', '考试', '移动开发入门课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (7, 'KC007', 'Android程序设计', 1003, 4.0, 64, '选修', '考试', '移动开发核心课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (8, 'KC008', '计算机组成原理', 1101, 4.0, 64, '必修', '考试', '计算机专业核心课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (9, 'KC009', '操作系统', 1101, 4.0, 64, '必修', '考试', '系统方向核心课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (10, 'KC010', 'Python程序设计', 1102, 4.0, 64, '必修', '考试', '大数据基础编程课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (11, 'KC011', '数据分析基础', 1102, 3.0, 48, '必修', '考试', '数据处理基础课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (12, 'KC012', '云平台部署与运维', 1103, 3.0, 48, '选修', '考查', '云计算实践课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (13, 'KC013', '虚拟化技术', 1103, 3.0, 48, '必修', '考试', '云基础设施课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (14, 'KC014', '计算机网络', 1201, 4.0, 64, '必修', '考试', '网络工程核心课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (15, 'KC015', '路由与交换技术', 1201, 3.0, 48, '选修', '考查', '网络设备配置课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (16, 'KC016', '网络安全基础', 1202, 4.0, 64, '必修', '考试', '信息安全核心课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (17, 'KC017', '密码学基础', 1202, 3.0, 48, '选修', '考试', '安全方向基础课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (18, 'KC018', '传感器与检测技术', 1203, 3.0, 48, '必修', '考试', '物联网感知层课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (19, 'KC019', '嵌入式系统开发', 1203, 4.0, 64, '选修', '考试', '物联网设备开发课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (20, 'KC020', '机器学习导论', 1301, 4.0, 64, '必修', '考试', '人工智能基础课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (21, 'KC021', '深度学习基础', 1301, 4.0, 64, '选修', '考试', '智能算法方向课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (22, 'KC022', '认知科学概论', 1302, 3.0, 48, '必修', '考查', '智能科学基础课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (23, 'KC023', '智能系统设计', 1302, 4.0, 64, '选修', '考试', '智能系统综合课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (24, 'KC024', '大数据技术基础', 1303, 4.0, 64, '必修', '考试', '数据科学核心课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (25, 'KC025', '数据可视化', 1303, 2.0, 32, '选修', '考查', '数据展示课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (26, 'KC026', '管理学原理', 2001, 3.0, 48, '必修', '考试', '工商管理基础课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');
INSERT INTO `courseinfo` VALUES (27, 'KC027', '市场营销学', 2001, 3.0, 48, '选修', '考查', '企业营销方向课程', 1, '2026-03-21 14:08:46', '2026-03-21 14:08:46');

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of noticeinfo
-- ----------------------------
INSERT INTO `noticeinfo` VALUES (1, '关于期中考试安排的通知', '请各班学生于第八周关注期中考试具体安排，按时参加考试。', '考试通知', 1, 1, 1, '管理员', '2026-03-23 17:26:31', '2026-03-23 17:26:31', 0);
INSERT INTO `noticeinfo` VALUES (2, '系统维护公告', '本周六晚22:00至23:30系统将进行维护，期间部分功能可能无法使用。', '系统公告', 0, 1, 1, '管理员', '2026-03-23 17:26:31', '2026-03-23 17:26:31', 0);
INSERT INTO `noticeinfo` VALUES (3, '关于提交课程作业的提醒', '请相关课程学生于本周日前完成作业提交，逾期将影响平时成绩。', '课程通知', 0, 1, 2, '张老师', '2026-03-23 17:26:31', '2026-03-23 17:26:31', 0);
INSERT INTO `noticeinfo` VALUES (4, '测试', '测试', '系统公告', 1, 1, 1, '管理员', '2026-03-23 09:35:49', '2026-03-23 09:35:49', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '成绩信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scoreinfo
-- ----------------------------
INSERT INTO `scoreinfo` VALUES (1, 8, 1, 2, 1, 85.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (2, 9, 1, 2, 1, 78.5, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (3, 10, 1, 2, 1, 92.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (4, 8, 1, 2, 2, 80.0, 1, '2026-03-21 14:59:01', 2);
INSERT INTO `scoreinfo` VALUES (5, 9, 1, 2, 2, 74.5, 1, '2026-03-21 14:59:01', 2);
INSERT INTO `scoreinfo` VALUES (6, 10, 1, 2, 2, 88.0, 1, '2026-03-21 14:59:01', 2);
INSERT INTO `scoreinfo` VALUES (7, 11, 2, 3, 1, 76.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (8, 12, 2, 3, 1, 84.5, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (9, 13, 2, 3, 1, 91.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (10, 14, 2, 3, 1, 67.0, 1, '2026-03-21 14:59:01', 1);
INSERT INTO `scoreinfo` VALUES (11, 11, 2, 3, 3, 82.0, 1, '2026-03-21 14:59:01', 3);
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
INSERT INTO `scoreinfo` VALUES (57, 41, 2, 3, 1, 72.0, 1, '2026-03-25 11:15:00', 1);
INSERT INTO `scoreinfo` VALUES (58, 41, 2, 3, 2, 74.0, 1, '2026-03-25 11:15:00', 2);
INSERT INTO `scoreinfo` VALUES (59, 41, 2, 3, 3, 68.5, 1, '2026-03-25 11:15:00', 3);
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
INSERT INTO `scoreinfo` VALUES (86, 41, 4, 3, 2, 0.0, 0, '2026-03-25 11:40:00', 2);
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
INSERT INTO `scoreinfo` VALUES (97, 11, 3, 3, 1, 78.0, 1, '2026-03-26 10:10:00', 1);
INSERT INTO `scoreinfo` VALUES (98, 11, 3, 3, 4, 81.5, 1, '2026-03-26 10:10:00', 4);
INSERT INTO `scoreinfo` VALUES (99, 11, 3, 3, 14, 76.0, 1, '2026-03-26 10:10:00', 3);
INSERT INTO `scoreinfo` VALUES (100, 11, 3, 3, 20, 83.0, 1, '2026-03-26 10:10:00', 10);
INSERT INTO `scoreinfo` VALUES (101, 11, 3, 3, 24, 80.0, 1, '2026-03-26 10:10:00', 12);
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
INSERT INTO `scoreinfo` VALUES (124, 11, 4, 3, 8, 85.0, 1, '2026-03-26 14:05:00', 2);
INSERT INTO `scoreinfo` VALUES (125, 11, 4, 3, 9, 83.5, 1, '2026-03-26 14:05:00', 2);
INSERT INTO `scoreinfo` VALUES (126, 11, 4, 3, 10, 90.0, 1, '2026-03-26 14:05:00', 3);
INSERT INTO `scoreinfo` VALUES (127, 11, 4, 3, 11, 88.0, 1, '2026-03-26 14:05:00', 3);
INSERT INTO `scoreinfo` VALUES (128, 11, 4, 3, 12, 84.0, 1, '2026-03-26 14:05:00', 3);
INSERT INTO `scoreinfo` VALUES (129, 11, 4, 3, 13, 86.5, 1, '2026-03-26 14:05:00', 3);
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
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of studentinfo
-- ----------------------------
INSERT INTO `studentinfo` VALUES (1, '202300101', '张伟', '男', 1, '13800010001', '410101200501011231', '河南省郑州市中原区', '2026-03-20 22:48:14', 28);
INSERT INTO `studentinfo` VALUES (2, '202300102', '王芳', '女', 1, '13800010002', '410101200502022248', '河南省郑州市金水区', '2026-03-20 22:48:14', 29);
INSERT INTO `studentinfo` VALUES (3, '202300103', '李娜', '女', 1, '13800010003', '410101200503032426', '河南省洛阳市西工区', '2026-03-20 22:48:14', 30);
INSERT INTO `studentinfo` VALUES (4, '202300104', '刘洋', '男', 1, '13800010004', '410101200504041257', '河南省开封市龙亭区', '2026-03-20 22:48:14', 31);
INSERT INTO `studentinfo` VALUES (5, '202300105', '陈杰', '男', 1, '13800010005', '410101200505052913', '河南省新乡市红旗区', '2026-03-20 22:48:14', 32);
INSERT INTO `studentinfo` VALUES (6, '202300106', '赵敏', '女', 2, '13800010006', '410101200506063629', '河南省安阳市文峰区', '2026-03-20 22:48:14', 33);
INSERT INTO `studentinfo` VALUES (7, '202300107', '黄涛', '男', 2, '13800010007', '410101200507074511', '河南省焦作市解放区', '2026-03-20 22:48:14', 34);
INSERT INTO `studentinfo` VALUES (8, '202300108', '周洁', '女', 2, '13800010008', '410101200508085026', '河南省许昌市魏都区', '2026-03-20 22:48:14', 35);
INSERT INTO `studentinfo` VALUES (9, '202300109', '吴昊', '男', 2, '13800010009', '410101200509095438', '河南省平顶山市新华区', '2026-03-20 22:48:14', 36);
INSERT INTO `studentinfo` VALUES (11, '202300111', '孙磊', '男', 3, '13800010011', '410101200511117851', '河南省商丘市梁园区', '2026-03-20 22:48:14', 38);
INSERT INTO `studentinfo` VALUES (12, '202300112', '马琳', '女', 3, '13800010012', '410101200512128462', '河南省信阳市浉河区', '2026-03-20 22:48:14', 39);
INSERT INTO `studentinfo` VALUES (13, '202300113', '朱强', '男', 3, '13800010013', '410101200601139274', '河南省周口市川汇区', '2026-03-20 22:48:14', 40);
INSERT INTO `studentinfo` VALUES (14, '202300114', '胡雪', '女', 3, '13800010014', '410101200602140685', '河南省驻马店市驿城区', '2026-03-20 22:48:14', 41);
INSERT INTO `studentinfo` VALUES (30, '202300110', '徐静', '女', 1, '13800010010', '410101200510106644', '河南省南阳市卧龙区', '2026-03-20 22:48:36', 37);
INSERT INTO `studentinfo` VALUES (32, '22', '许墨', '男', 1, '22', '222', '2222', '2026-03-24 23:23:36', 79);
INSERT INTO `studentinfo` VALUES (33, '202300115', '郭晨', '男', 4, '13800010015', '410101200603151896', '河南省漯河市源汇区', '2026-03-25 10:00:00', 81);
INSERT INTO `studentinfo` VALUES (34, '202300116', '何丽', '女', 4, '13800010016', '410101200604162107', '河南省三门峡市湖滨区', '2026-03-25 10:00:00', 82);
INSERT INTO `studentinfo` VALUES (35, '202300117', '高飞', '男', 4, '13800010017', '410101200605173318', '河南省鹤壁市淇滨区', '2026-03-25 10:00:00', 83);
INSERT INTO `studentinfo` VALUES (36, '202300118', '罗静', '女', 4, '13800010018', '410101200606184529', '河南省驻马店市驿城区', '2026-03-25 10:00:00', 84);
INSERT INTO `studentinfo` VALUES (37, '202300119', '郑凯', '男', 4, '13800010019', '410101200607195730', '河南省南阳市宛城区', '2026-03-25 10:00:00', 85);
INSERT INTO `studentinfo` VALUES (38, '202300120', '宋雨欣', '女', 2, '13800010020', '410101200608206941', '河南省商丘市梁园区', '2026-03-25 10:00:00', 86);
INSERT INTO `studentinfo` VALUES (39, '202300121', '杜浩', '男', 3, '13800010021', '410101200609218152', '河南省信阳市浉河区', '2026-03-25 10:00:00', 87);
INSERT INTO `studentinfo` VALUES (40, '202300122', '潘婷', '女', 3, '13800010022', '410101200610229363', '河南省周口市川汇区', '2026-03-25 10:00:00', 88);
INSERT INTO `studentinfo` VALUES (41, '202300123', '韩磊', '男', 3, '13800010023', '410101200611230574', '河南省平顶山市新华区', '2026-03-25 10:00:00', 89);
INSERT INTO `studentinfo` VALUES (42, '202300124', '曹雪', '女', 3, '13800010024', '410101200612241785', '河南省焦作市解放区', '2026-03-25 10:00:00', 90);
INSERT INTO `studentinfo` VALUES (43, '202300125', '彭宇', '男', 3, '13800010025', '410101200701251996', '河南省安阳市文峰区', '2026-03-25 10:00:00', 91);
INSERT INTO `studentinfo` VALUES (44, '202300126', '苏雯', '女', 3, '13800010026', '410101200702262108', '河南省开封市龙亭区', '2026-03-25 10:00:00', 92);
INSERT INTO `studentinfo` VALUES (45, '202300127', '蒋涛', '男', 2, '13800010027', '410101200703273319', '河南省郑州市中原区', '2026-03-25 10:00:00', 93);
INSERT INTO `studentinfo` VALUES (46, '202300128', '许倩', '女', 2, '13800010028', '410101200704284520', '河南省郑州市金水区', '2026-03-25 10:00:00', 94);
INSERT INTO `studentinfo` VALUES (47, '202300129', '黎明', '男', 2, '13800010029', '410101200705295731', '河南省洛阳市西工区', '2026-03-25 10:00:00', 95);
INSERT INTO `studentinfo` VALUES (48, '202300130', '唐宁', '女', 3, '13800010030', '410101200706306942', '河南省新乡市红旗区', '2026-03-25 10:00:00', 96);

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
  PRIMARY KEY (`tcId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '授课安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching
-- ----------------------------
INSERT INTO `teaching` VALUES (1, 1, 1, 1, 1);
INSERT INTO `teaching` VALUES (2, 1, 1, 1, 1);
INSERT INTO `teaching` VALUES (3, 1, 1, 2, 2);
INSERT INTO `teaching` VALUES (4, 1, 2, 1, 3);
INSERT INTO `teaching` VALUES (5, 1, 2, 3, 1);
INSERT INTO `teaching` VALUES (6, 1, 3, 2, 2);
INSERT INTO `teaching` VALUES (7, 1, 3, 4, 4);
INSERT INTO `teaching` VALUES (8, 1, 4, 1, 2);
INSERT INTO `teaching` VALUES (9, 1, 4, 3, 3);
INSERT INTO `teaching` VALUES (11, 2, 1, 3, 3);
INSERT INTO `teaching` VALUES (12, 2, 2, 2, 1);
INSERT INTO `teaching` VALUES (13, 2, 2, 4, 4);
INSERT INTO `teaching` VALUES (14, 2, 3, 1, 3);
INSERT INTO `teaching` VALUES (15, 2, 3, 3, 2);
INSERT INTO `teaching` VALUES (16, 2, 4, 2, 1);
INSERT INTO `teaching` VALUES (17, 2, 4, 4, 4);
INSERT INTO `teaching` VALUES (18, 3, 1, 2, 3);
INSERT INTO `teaching` VALUES (19, 3, 2, 3, 2);
INSERT INTO `teaching` VALUES (20, 3, 3, 4, 1);
INSERT INTO `teaching` VALUES (21, 3, 4, 1, 4);
INSERT INTO `teaching` VALUES (22, 4, 2, 2, 10);
INSERT INTO `teaching` VALUES (23, 4, 2, 3, 12);
INSERT INTO `teaching` VALUES (24, 3, 2, 3, 10);
INSERT INTO `teaching` VALUES (25, 3, 1, 13, 12);

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
INSERT INTO `userinfo` VALUES (1, 'admin', '123456', '管理员', '男', '15328da3-17ef-4d8a-a94b-b4b00823b0bb.jpg', 3, NULL);
INSERT INTO `userinfo` VALUES (2, 'xiaozhang', '123456', '校长1', '男', 'string', 3, '2026-03-13 06:38:07');
INSERT INTO `userinfo` VALUES (38, '202300111', '123456', '孙磊', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (39, '202300112', '123456', '马琳', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (40, '202300113', '123456', '朱强', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (41, '202300114', '123456', '胡雪', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (42, '202300101', '123456', '张伟', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (43, '202300102', '123456', '王芳', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (44, '202300103', '123456', '李娜', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (45, '202300104', '123456', '刘洋', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (46, '202300105', '123456', '陈杰', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (47, '202300106', '123456', '赵敏', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (48, '202300107', '123456', '黄涛', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (49, '202300108', '123456', '周洁', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (50, '202300109', '123456', '吴昊', NULL, NULL, 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (51, '202300110', '123456', '徐静', '女', '', 1, '2026-03-21 15:31:07');
INSERT INTO `userinfo` VALUES (59, 'T2024001', '123456', '杨老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (60, 'T2024002', '123456', '孙老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (61, 'T2024003', '123456', '郑老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (62, 'T2024004', '123456', '马老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (63, 'T2024005', '123456', '朱老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (64, 'T2024006', '123456', '胡老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (65, 'T2024007', '123456', '郭老师', NULL, NULL, 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (66, 'T2024008', '123456', '何老师', '男', '', 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (67, 'T2024009', '123456', '高老师', '女', '', 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (68, 'T2024010', '123456', '林老师', '男', '', 2, '2026-03-21 15:33:06');
INSERT INTO `userinfo` VALUES (75, '202600100', '123456', '黄仨', '男', '', 1, '2026-03-21 15:53:01');
INSERT INTO `userinfo` VALUES (77, 'T2024011', '123456', '黄某', '男', NULL, 2, '2026-03-21 16:43:46');
INSERT INTO `userinfo` VALUES (79, '22', '123456', '许墨', '男', NULL, 1, '2026-03-24 23:23:36');
INSERT INTO `userinfo` VALUES (80, 'T2025001', '123456', '黄教授', '男', NULL, 2, '2026-03-24 23:55:11');
INSERT INTO `userinfo` VALUES (81, '202300115', '123456', '郭晨', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (82, '202300116', '123456', '何丽', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (83, '202300117', '123456', '高飞', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (84, '202300118', '123456', '罗静', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (85, '202300119', '123456', '郑凯', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (86, '202300120', '123456', '宋雨欣', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (87, '202300121', '123456', '杜浩', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (88, '202300122', '123456', '潘婷', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (89, '202300123', '123456', '韩磊', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (90, '202300124', '123456', '曹雪', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (91, '202300125', '123456', '彭宇', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (92, '202300126', '123456', '苏雯', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (93, '202300127', '123456', '蒋涛', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (94, '202300128', '123456', '许倩', '女', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (95, '202300129', '123456', '黎明', '男', NULL, 1, '2026-03-25 10:05:00');
INSERT INTO `userinfo` VALUES (96, '202300130', '123456', '唐宁', '男', '', 1, '2026-03-25 10:05:00');

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学年学期表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yearterm
-- ----------------------------
INSERT INTO `yearterm` VALUES (1, '2025-2026', '1', '2025-2026-1');
INSERT INTO `yearterm` VALUES (2, '2025-2026', '2', '2025-2026-2');
INSERT INTO `yearterm` VALUES (3, '2026-2027', '1', '2026-2027-1');
INSERT INTO `yearterm` VALUES (4, '2026-2027', '2', '2026-2027-2');

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
