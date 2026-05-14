/*
 Navicat/MySQL 追加数据脚本

 使用方式：
 1. 先导入 grade-management.sql。
 2. 再在同一个数据库中执行本脚本。

 说明：
 - 本脚本只追加演示数据，不删除、不覆盖原有记录。
 - 通过账号、编号、课程代码、班级代码等自然键去重，可重复执行。
 - 如已在客户端中选中 grade-management 数据库，可保留或注释掉 USE 语句。
*/

SET NAMES utf8mb4;
USE `grade-management`;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 学期数据
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_terms;
CREATE TEMPORARY TABLE tmp_seed_terms (
  yYear varchar(30),
  yTerm varchar(20),
  yAll varchar(50)
);

INSERT INTO tmp_seed_terms VALUES
('2027', '2', '2026-2027 第二学期');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_terms;
CREATE TEMPORARY TABLE tmp_existing_terms AS
SELECT yAll FROM yearterm;

INSERT INTO yearterm (yYear, yTerm, yAll)
SELECT t.yYear, t.yTerm, t.yAll
FROM tmp_seed_terms t
LEFT JOIN tmp_existing_terms e ON e.yAll = t.yAll
WHERE e.yAll IS NULL;

-- ----------------------------
-- 公告数据
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_notices;
CREATE TEMPORARY TABLE tmp_seed_notices (
  nTitle varchar(200),
  nContent text,
  nType varchar(50),
  nTop int,
  nState int,
  nCreateUid int,
  nCreateName varchar(50),
  nCreateTime datetime
);

INSERT INTO tmp_seed_notices VALUES
('2025-2026 第二学期成绩录入通知', '请各任课教师在考试结束后一周内完成平时成绩、测试成绩和期末成绩录入，成绩提交后请及时核对班级名单与课程信息。', '教学通知', 1, 1, 107, '管理员', '2026-05-12 09:00:00'),
('学业预警帮扶工作安排', '系统将根据课程成绩、知识点掌握情况和综合表现生成学业预警，请辅导员与任课教师对高风险学生开展一对一帮扶。', '预警通知', 1, 1, 107, '管理员', '2026-05-12 10:00:00'),
('公共课补考报名提醒', '大学英语、高等数学、大学物理等公共课程补考报名已开放，请需要补考的学生在规定时间内完成报名。', '考试通知', 0, 1, 107, '管理员', '2026-05-13 09:30:00'),
('课程知识点诊断功能上线', '教师可在成绩详情中查看学生知识点得分分布，学生可根据薄弱知识点生成复习建议。', '系统公告', 0, 1, 107, '管理员', '2026-05-14 14:00:00'),
('创新创业实践周安排', '创新创业实践周将围绕项目路演、商业计划书撰写和团队协作训练展开，请相关班级按课表参加。', '活动通知', 0, 1, 107, '管理员', '2026-05-15 08:30:00'),
('毕业设计过程材料提交提醒', '毕业班学生需按学院要求提交开题报告、中期检查表和阶段性成果，指导教师请及时审核。', '教学通知', 0, 1, 107, '管理员', '2026-05-16 11:00:00');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_notice_titles;
CREATE TEMPORARY TABLE tmp_existing_notice_titles AS
SELECT nTitle FROM noticeinfo;

INSERT INTO noticeinfo (nTitle, nContent, nType, nTop, nState, nCreateUid, nCreateName, nCreateTime, nUpdateTime, nIsDelete)
SELECT n.nTitle, n.nContent, n.nType, n.nTop, n.nState, n.nCreateUid, n.nCreateName, n.nCreateTime, n.nCreateTime, 0
FROM tmp_seed_notices n
LEFT JOIN tmp_existing_notice_titles e ON e.nTitle = n.nTitle
WHERE e.nTitle IS NULL;

-- ----------------------------
-- 学院、专业、班级数据
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_colleges;
CREATE TEMPORARY TABLE tmp_seed_colleges (
  cName varchar(50),
  cCode varchar(20),
  cParentCode varchar(20)
);

INSERT INTO tmp_seed_colleges VALUES
('体育教学部', 'PE003', NULL),
('创新创业学院', 'IE004', NULL);

DROP TEMPORARY TABLE IF EXISTS tmp_existing_college_codes;
CREATE TEMPORARY TABLE tmp_existing_college_codes AS
SELECT cCode FROM collegeinfo;

INSERT INTO collegeinfo (cName, cCode, cParentId)
SELECT c.cName, c.cCode, IFNULL(parent.CID, 0)
FROM tmp_seed_colleges c
LEFT JOIN collegeinfo parent ON parent.cCode = c.cParentCode
LEFT JOIN tmp_existing_college_codes e ON e.cCode = c.cCode
WHERE e.cCode IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_seed_majors;
CREATE TEMPORARY TABLE tmp_seed_majors (
  mName varchar(50),
  mCode varchar(20),
  collegeCode varchar(20)
);

INSERT INTO tmp_seed_majors VALUES
('数据科学与大数据技术', 'DS', 'CS001'),
('网络工程', 'NET', 'CS001'),
('电子信息工程', 'EIE', 'EI002'),
('国际经济与贸易', 'IET', 'BA003');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_major_codes;
CREATE TEMPORARY TABLE tmp_existing_major_codes AS
SELECT mCode FROM majorinfo;

INSERT INTO majorinfo (mName, mCode, mCollegeId)
SELECT m.mName, m.mCode, c.CID
FROM tmp_seed_majors m
JOIN collegeinfo c ON c.cCode = m.collegeCode
LEFT JOIN tmp_existing_major_codes e ON e.mCode = m.mCode
WHERE e.mCode IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_seed_classes;
CREATE TEMPORARY TABLE tmp_seed_classes (
  majorCode varchar(20),
  cGrade int,
  cName varchar(50),
  cCode varchar(20)
);

INSERT INTO tmp_seed_classes VALUES
('SE', 2022, '软件2201班', 'SE2201'),
('SE', 2022, '软件2202班', 'SE2202'),
('CST', 2023, '计科2301班', 'CST2301'),
('AI', 2023, '人工智能2302班', 'AI2302'),
('DS', 2023, '大数据2301班', 'DS2301'),
('NET', 2023, '网络工程2301班', 'NET2301'),
('TE', 2022, '通信2201班', 'TE2201'),
('ACC', 2022, '会计2201班', 'ACC2201');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_class_codes;
CREATE TEMPORARY TABLE tmp_existing_class_codes AS
SELECT cCode FROM classinfo;

INSERT INTO classinfo (cMajorId, cGrade, cName, cCode)
SELECT m.mid, c.cGrade, c.cName, c.cCode
FROM tmp_seed_classes c
JOIN majorinfo m ON m.mCode = c.majorCode
LEFT JOIN tmp_existing_class_codes e ON e.cCode = c.cCode
WHERE e.cCode IS NULL;

-- ----------------------------
-- 课程与知识点数据
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_courses;
CREATE TEMPORARY TABLE tmp_seed_courses (
  crCode varchar(20),
  crName varchar(30),
  majorCode varchar(20),
  crCredit decimal(4, 1),
  crPeriod int,
  crType varchar(20),
  crExamType varchar(20),
  crRemark varchar(255)
);

INSERT INTO tmp_seed_courses VALUES
('CS003', 'Python程序设计', 'SE', 3.0, 48, '必修', '考试', '程序设计基础课程'),
('CS004', '数据库系统', 'SE', 3.5, 56, '必修', '考试', '覆盖SQL、事务与数据库设计'),
('CS005', '计算机网络', 'NET', 3.0, 48, '必修', '考试', '网络体系结构与协议分析'),
('CS006', '操作系统', 'CST', 3.5, 56, '必修', '考试', '进程、内存、文件系统与并发'),
('CS007', '软件工程导论', 'SE', 2.5, 40, '必修', '考查', '需求分析、设计、测试与项目管理'),
('AI001', '人工智能导论', 'AI', 3.0, 48, '必修', '考试', '人工智能基本思想与典型应用'),
('AI002', '机器学习基础', 'AI', 3.5, 56, '必修', '考试', '监督学习、模型评估与实践'),
('DS001', '数据分析与可视化', 'DS', 3.0, 48, '必修', '考查', '数据清洗、统计分析与图表表达'),
('DS002', '大数据平台基础', 'DS', 3.0, 48, '必修', '考试', 'Hadoop、Spark与分布式计算基础'),
('MA003', '概率论与数理统计', 'MATH', 3.0, 48, '必修', '考试', '公共数学基础课程'),
('MA004', '离散数学', 'CST', 3.0, 48, '必修', '考试', '集合、图论、组合与逻辑'),
('PE101', '大学体育', 'SE', 1.0, 32, '必修', '考查', '体能训练与专项运动'),
('IE101', '创新创业基础', 'SE', 2.0, 32, '必修', '考查', '商业计划书与项目路演训练'),
('EI101', '电路分析基础', 'TE', 3.0, 48, '必修', '考试', '电路模型、定律与分析方法'),
('BA101', '管理学原理', 'ACC', 2.5, 40, '必修', '考试', '管理职能、组织行为与案例分析');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_course_codes;
CREATE TEMPORARY TABLE tmp_existing_course_codes AS
SELECT crCode FROM courseinfo;

INSERT INTO courseinfo (crCode, crName, crMajorId, crCredit, crPeriod, crType, crExamType, crRemark, crState, createTime, updateTime)
SELECT c.crCode, c.crName, m.mid, c.crCredit, c.crPeriod, c.crType, c.crExamType, c.crRemark, 1, NOW(), NOW()
FROM tmp_seed_courses c
JOIN majorinfo m ON m.mCode = c.majorCode
LEFT JOIN tmp_existing_course_codes e ON e.crCode = c.crCode
WHERE e.crCode IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_seed_knowledge;
CREATE TEMPORARY TABLE tmp_seed_knowledge (
  courseCode varchar(20),
  pointName varchar(100)
);

INSERT INTO tmp_seed_knowledge VALUES
('CS003', 'Python基础语法与数据类型'),
('CS003', '函数设计与模块化编程'),
('CS003', '文件处理与异常控制'),
('CS003', '面向对象程序设计'),
('CS003', '第三方库与综合实践'),
('CS004', '关系模型与数据库设计'),
('CS004', 'SQL查询与多表连接'),
('CS004', '索引优化与执行计划'),
('CS004', '事务管理与并发控制'),
('CS004', '数据库安全与备份恢复'),
('CS005', '网络体系结构与分层模型'),
('CS005', 'IP地址规划与路由协议'),
('CS005', 'TCP与UDP协议机制'),
('CS005', '局域网技术与交换原理'),
('CS005', '网络安全与抓包分析'),
('CS006', '进程线程与调度算法'),
('CS006', '同步互斥与死锁处理'),
('CS006', '内存管理与虚拟存储'),
('CS006', '文件系统与磁盘调度'),
('CS006', '操作系统综合实验'),
('CS007', '软件过程模型'),
('CS007', '需求分析与用例建模'),
('CS007', '软件设计原则'),
('CS007', '测试方法与缺陷管理'),
('CS007', '项目计划与团队协作'),
('AI001', '搜索算法与问题求解'),
('AI001', '知识表示与推理'),
('AI001', '机器学习基本概念'),
('AI001', '自然语言处理基础'),
('AI001', '人工智能伦理与应用'),
('AI002', '监督学习与特征工程'),
('AI002', '回归与分类模型'),
('AI002', '模型评估与交叉验证'),
('AI002', '聚类与降维方法'),
('AI002', '机器学习项目实践'),
('DS001', '数据清洗与缺失值处理'),
('DS001', '描述性统计分析'),
('DS001', '数据可视化设计'),
('DS001', 'Python数据分析工具'),
('DS001', '分析报告撰写'),
('DS002', '分布式文件系统'),
('DS002', 'MapReduce计算模型'),
('DS002', 'Spark RDD与DataFrame'),
('DS002', '数据仓库与ETL流程'),
('DS002', '大数据平台部署实践'),
('MA003', '随机事件与概率计算'),
('MA003', '随机变量及其分布'),
('MA003', '数字特征与大数定律'),
('MA003', '参数估计与假设检验'),
('MA003', '统计建模应用'),
('MA004', '命题逻辑与谓词逻辑'),
('MA004', '集合关系与映射'),
('MA004', '图论基本概念'),
('MA004', '组合计数方法'),
('MA004', '递推关系与生成函数'),
('PE101', '身体素质测试'),
('PE101', '专项技术动作'),
('PE101', '运动安全与健康管理'),
('PE101', '团队协作与竞赛规则'),
('PE101', '日常训练过程表现'),
('IE101', '创业机会识别'),
('IE101', '商业模式设计'),
('IE101', '市场调研与用户画像'),
('IE101', '财务预算与风险评估'),
('IE101', '项目路演与答辩表达'),
('EI101', '电路基本变量与定律'),
('EI101', '电阻电路等效变换'),
('EI101', '节点电压与网孔电流法'),
('EI101', '正弦稳态电路分析'),
('EI101', '一阶动态电路响应'),
('BA101', '管理思想发展'),
('BA101', '计划与决策方法'),
('BA101', '组织结构设计'),
('BA101', '领导与激励理论'),
('BA101', '控制过程与绩效评价');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_knowledge;
CREATE TEMPORARY TABLE tmp_existing_knowledge AS
SELECT course_id, point_name FROM knowledge_point;

INSERT INTO knowledge_point (course_id, point_name, create_time)
SELECT c.crId, k.pointName, NOW()
FROM tmp_seed_knowledge k
JOIN courseinfo c ON c.crCode = k.courseCode
LEFT JOIN tmp_existing_knowledge e ON e.course_id = c.crId AND e.point_name = k.pointName
WHERE e.point_name IS NULL;

-- ----------------------------
-- 教师账号与教师资料
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_teachers;
CREATE TEMPORARY TABLE tmp_seed_teachers (
  tNo varchar(20),
  tName varchar(30),
  tSex varchar(4),
  tTel varchar(11),
  tEduLevel varchar(30),
  tSchool varchar(50),
  tAddress varchar(50),
  collegeCode varchar(20)
);

INSERT INTO tmp_seed_teachers VALUES
('T2509', '周数据库', '男', '18126002509', '博士', '哈尔滨工业大学', '厦门市集美区理工路9号', 'CS001'),
('T2510', '孙网络', '男', '18126002510', '硕士', '北京邮电大学', '厦门市集美区理工路10号', 'CS001'),
('T2511', '赵智研', '女', '18126002511', '博士', '中国科学技术大学', '厦门市集美区理工路11号', 'CS001'),
('T2512', '钱英语', '女', '18126002512', '硕士', '上海外国语大学', '厦门市集美区理工路12号', 'FL001'),
('T2513', '吴体育', '男', '18126002513', '本科', '北京体育大学', '厦门市集美区理工路13号', 'PE003'),
('T2514', '郑创新', '女', '18126002514', '硕士', '厦门大学', '厦门市集美区理工路14号', 'IE004'),
('T2515', '陈电路', '男', '18126002515', '博士', '电子科技大学', '厦门市集美区理工路15号', 'EI002'),
('T2516', '林管理', '女', '18126002516', '硕士', '中山大学', '厦门市集美区理工路16号', 'BA003');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_user_logs;
CREATE TEMPORARY TABLE tmp_existing_user_logs AS
SELECT uLog FROM userinfo;

INSERT INTO userinfo (uLog, uPwd, uName, uSex, uPhoto, uRole, uCreateDate)
SELECT t.tNo, '123456', t.tName, t.tSex, NULL, 2, NOW()
FROM tmp_seed_teachers t
LEFT JOIN tmp_existing_user_logs e ON e.uLog = t.tNo
WHERE e.uLog IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_existing_teacher_nos;
CREATE TEMPORARY TABLE tmp_existing_teacher_nos AS
SELECT tNo FROM teacherinfo;

INSERT INTO teacherinfo (tNo, tName, tSex, tTel, tEduLevel, tSchool, tAddress, tAccountId, tCollegeId)
SELECT t.tNo, t.tName, t.tSex, t.tTel, t.tEduLevel, t.tSchool, t.tAddress, u.uid, c.CID
FROM tmp_seed_teachers t
JOIN userinfo u ON u.uLog = t.tNo
JOIN collegeinfo c ON c.cCode = t.collegeCode
LEFT JOIN tmp_existing_teacher_nos e ON e.tNo = t.tNo
WHERE e.tNo IS NULL;

-- ----------------------------
-- 学生账号与学生资料
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_students;
CREATE TEMPORARY TABLE tmp_seed_students (
  sNo varchar(20),
  sName varchar(30),
  sSex varchar(4),
  classCode varchar(20),
  sTel varchar(11),
  sIdcard varchar(18),
  sAddress varchar(50),
  sIntent varchar(50)
);

INSERT INTO tmp_seed_students VALUES
('S2026301', '许文博', '男', 'SE2201', '13926001001', '350200200501010001', '厦门市思明区软件园1号', '考研'),
('S2026302', '林雨桐', '女', 'SE2201', '13926001002', '350200200501010002', '厦门市湖里区创新路2号', '就业'),
('S2026303', '陈嘉豪', '男', 'SE2201', '13926001003', '350200200501010003', '泉州市丰泽区东海街3号', '考公'),
('S2026304', '黄诗涵', '女', 'SE2201', '13926001004', '350200200501010004', '漳州市芗城区胜利路4号', '考研'),
('S2026305', '吴俊杰', '男', 'SE2201', '13926001005', '350200200501010005', '福州市鼓楼区五四路5号', '创业'),
('S2026306', '周若曦', '女', 'SE2201', '13926001006', '350200200501010006', '龙岩市新罗区莲庄路6号', '就业'),
('S2026307', '郑宇航', '男', 'SE2202', '13926001007', '350200200501010007', '厦门市集美区杏林湾7号', '就业'),
('S2026308', '叶子萱', '女', 'SE2202', '13926001008', '350200200501010008', '莆田市城厢区荔城路8号', '考研'),
('S2026309', '蒋明轩', '男', 'SE2202', '13926001009', '350200200501010009', '宁德市蕉城区东侨路9号', '就业'),
('S2026310', '何思琪', '女', 'SE2202', '13926001010', '350200200501010010', '南平市延平区江滨路10号', '考公'),
('S2026311', '邱泽楷', '男', 'SE2202', '13926001011', '350200200501010011', '三明市梅列区列东街11号', '就业'),
('S2026312', '谢安妮', '女', 'SE2202', '13926001012', '350200200501010012', '厦门市海沧区海景路12号', '出国'),
('S2026313', '罗启航', '男', 'CST2301', '13926001013', '350200200501010013', '福州市仓山区金山路13号', '考研'),
('S2026314', '曾语嫣', '女', 'CST2301', '13926001014', '350200200501010014', '厦门市同安区银湖路14号', '就业'),
('S2026315', '彭子墨', '男', 'CST2301', '13926001015', '350200200501010015', '泉州市鲤城区中山路15号', '考研'),
('S2026316', '苏沐晴', '女', 'CST2301', '13926001016', '350200200501010016', '漳州市龙文区迎宾路16号', '考公'),
('S2026317', '唐景行', '男', 'CST2301', '13926001017', '350200200501010017', '厦门市翔安区新店路17号', '就业'),
('S2026318', '马清妍', '女', 'CST2301', '13926001018', '350200200501010018', '福清市音西街18号', '创业'),
('S2026319', '高远辰', '男', 'AI2302', '13926001019', '350200200501010019', '厦门市思明区大学路19号', '考研'),
('S2026320', '梁婉宁', '女', 'AI2302', '13926001020', '350200200501010020', '厦门市湖里区枋湖路20号', '就业'),
('S2026321', '宋一鸣', '男', 'AI2302', '13926001021', '350200200501010021', '泉州市洛江区万安路21号', '考研'),
('S2026322', '袁可馨', '女', 'AI2302', '13926001022', '350200200501010022', '漳州市龙海区人民路22号', '出国'),
('S2026323', '韩知远', '男', 'AI2302', '13926001023', '350200200501010023', '莆田市荔城区文献路23号', '就业'),
('S2026324', '邓舒雅', '女', 'AI2302', '13926001024', '350200200501010024', '宁德市福安市新华路24号', '考公'),
('S2026325', '范星河', '男', 'DS2301', '13926001025', '350200200501010025', '厦门市集美区诚毅路25号', '就业'),
('S2026326', '白芷晴', '女', 'DS2301', '13926001026', '350200200501010026', '福州市晋安区福新路26号', '考研'),
('S2026327', '陆嘉树', '男', 'DS2301', '13926001027', '350200200501010027', '泉州市晋江市世纪大道27号', '就业'),
('S2026328', '顾念瑶', '女', 'DS2301', '13926001028', '350200200501010028', '厦门市海沧区滨湖北路28号', '创业'),
('S2026329', '姜承泽', '男', 'DS2301', '13926001029', '350200200501010029', '南安市柳城街29号', '考研'),
('S2026330', '夏若琳', '女', 'DS2301', '13926001030', '350200200501010030', '厦门市同安区环城路30号', '就业'),
('S2026331', '魏晨阳', '男', 'NET2301', '13926001031', '350200200501010031', '厦门市翔安区翔安南路31号', '就业'),
('S2026332', '沈梦洁', '女', 'NET2301', '13926001032', '350200200501010032', '漳州市平和县小溪路32号', '考研'),
('S2026333', '熊浩然', '男', 'NET2301', '13926001033', '350200200501010033', '龙海市石码街33号', '就业'),
('S2026334', '谭雨菲', '女', 'NET2301', '13926001034', '350200200501010034', '福州市马尾区儒江路34号', '考公'),
('S2026335', '方奕辰', '男', 'NET2301', '13926001035', '350200200501010035', '厦门市集美区后溪镇35号', '考研'),
('S2026336', '杜欣怡', '女', 'NET2301', '13926001036', '350200200501010036', '泉州市惠安县螺城路36号', '就业'),
('S2026337', '石皓宇', '男', 'TE2201', '13926001037', '350200200501010037', '厦门市思明区环岛路37号', '就业'),
('S2026338', '尹曼妮', '女', 'TE2201', '13926001038', '350200200501010038', '莆田市涵江区涵华路38号', '考研'),
('S2026339', '钟凯文', '男', 'TE2201', '13926001039', '350200200501010039', '福州市台江区五一路39号', '就业'),
('S2026340', '崔晓彤', '女', 'TE2201', '13926001040', '350200200501010040', '厦门市湖里区殿前路40号', '考公'),
('S2026341', '廖铭泽', '男', 'TE2201', '13926001041', '350200200501010041', '泉州市南安市成功街41号', '考研'),
('S2026342', '潘语晨', '女', 'TE2201', '13926001042', '350200200501010042', '漳州市长泰区人民路42号', '就业'),
('S2026343', '秦思远', '男', 'ACC2201', '13926001043', '350200200501010043', '厦门市集美区印斗路43号', '考公'),
('S2026344', '贺婧怡', '女', 'ACC2201', '13926001044', '350200200501010044', '福州市鼓楼区湖东路44号', '就业'),
('S2026345', '毛俊熙', '男', 'ACC2201', '13926001045', '350200200501010045', '莆田市秀屿区笏石路45号', '创业'),
('S2026346', '丁雅雯', '女', 'ACC2201', '13926001046', '350200200501010046', '厦门市同安区城南路46号', '考研'),
('S2026347', '任泽宇', '男', 'ACC2201', '13926001047', '350200200501010047', '泉州市安溪县凤城路47号', '就业'),
('S2026348', '姚心语', '女', 'ACC2201', '13926001048', '350200200501010048', '漳州市云霄县云陵路48号', '考公');

DROP TEMPORARY TABLE IF EXISTS tmp_existing_student_user_logs;
CREATE TEMPORARY TABLE tmp_existing_student_user_logs AS
SELECT uLog FROM userinfo;

INSERT INTO userinfo (uLog, uPwd, uName, uSex, uPhoto, uRole, uCreateDate)
SELECT s.sNo, '123456', s.sName, s.sSex, NULL, 1, NOW()
FROM tmp_seed_students s
LEFT JOIN tmp_existing_student_user_logs e ON e.uLog = s.sNo
WHERE e.uLog IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_existing_student_nos;
CREATE TEMPORARY TABLE tmp_existing_student_nos AS
SELECT sNo FROM studentinfo;

INSERT INTO studentinfo (sNo, sName, sSex, sClassId, sTel, sIdcard, sAddress, sCreateDate, sAccountId, sIntent)
SELECT s.sNo, s.sName, s.sSex, c.cId, s.sTel, s.sIdcard, s.sAddress, NOW(), u.uid, s.sIntent
FROM tmp_seed_students s
JOIN classinfo c ON c.cCode = s.classCode
JOIN userinfo u ON u.uLog = s.sNo
LEFT JOIN tmp_existing_student_nos e ON e.sNo = s.sNo
WHERE e.sNo IS NULL;

-- ----------------------------
-- 教学任务、课程成绩、知识点成绩
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_seed_teaching;
CREATE TEMPORARY TABLE tmp_seed_teaching (
  termAll varchar(50),
  classCode varchar(20),
  courseCode varchar(20),
  teacherNo varchar(20),
  w_exam double(3, 2),
  w_regular double(3, 2),
  w_test double(3, 2)
);

INSERT INTO tmp_seed_teaching VALUES
('2025-2026 第二学期', 'SE2201', 'CS003', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'SE2201', 'CS004', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'SE2201', 'CS007', 'T2506', 0.40, 0.40, 0.20),
('2025-2026 第二学期', 'SE2201', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'SE2201', 'PE101', 'T2513', 0.30, 0.50, 0.20),
('2025-2026 第二学期', 'SE2201', 'IE101', 'T2514', 0.30, 0.40, 0.30),
('2025-2026 第二学期', 'SE2202', 'CS003', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'SE2202', 'CS004', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'SE2202', 'CS007', 'T2506', 0.40, 0.40, 0.20),
('2025-2026 第二学期', 'SE2202', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'SE2202', 'PE101', 'T2513', 0.30, 0.50, 0.20),
('2025-2026 第二学期', 'SE2202', 'IE101', 'T2514', 0.30, 0.40, 0.30),
('2025-2026 第二学期', 'CST2301', 'CS003', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'CST2301', 'CS006', 'T2505', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'CST2301', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'CST2301', 'MA004', 'T2504', 0.60, 0.20, 0.20),
('2025-2026 第二学期', 'CST2301', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'AI2302', 'AI001', 'T2511', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'AI2302', 'CS003', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'AI2302', 'MA003', 'T2504', 0.60, 0.20, 0.20),
('2025-2026 第二学期', 'AI2302', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'DS2301', 'DS001', 'T2511', 0.40, 0.40, 0.20),
('2025-2026 第二学期', 'DS2301', 'DS002', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'DS2301', 'MA003', 'T2504', 0.60, 0.20, 0.20),
('2025-2026 第二学期', 'DS2301', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'NET2301', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'NET2301', 'CS006', 'T2505', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'NET2301', 'CS004', 'T2509', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'NET2301', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'TE2201', 'EI101', 'T2515', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'TE2201', 'PHY101', 'T2508', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'TE2201', 'MA003', 'T2504', 0.60, 0.20, 0.20),
('2025-2026 第二学期', 'TE2201', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'ACC2201', 'BA101', 'T2516', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'ACC2201', 'MA003', 'T2504', 0.60, 0.20, 0.20),
('2025-2026 第二学期', 'ACC2201', 'ENG101', 'T2512', 0.50, 0.30, 0.20),
('2025-2026 第二学期', 'ACC2201', 'IE101', 'T2514', 0.30, 0.40, 0.30),
('2026-2027 第一学期', 'SE2201', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'SE2201', 'CS006', 'T2505', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'SE2201', 'AI001', 'T2511', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'SE2202', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'SE2202', 'CS006', 'T2505', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'SE2202', 'AI001', 'T2511', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'CST2301', 'CS004', 'T2509', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'CST2301', 'AI001', 'T2511', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'CST2301', 'DS001', 'T2511', 0.40, 0.40, 0.20),
('2026-2027 第一学期', 'AI2302', 'AI002', 'T2511', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'AI2302', 'DS001', 'T2511', 0.40, 0.40, 0.20),
('2026-2027 第一学期', 'AI2302', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'DS2301', 'AI002', 'T2511', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'DS2301', 'CS004', 'T2509', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'DS2301', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'NET2301', 'AI001', 'T2511', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'NET2301', 'CS003', 'T2509', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'NET2301', 'MA004', 'T2504', 0.60, 0.20, 0.20),
('2026-2027 第一学期', 'TE2201', 'CS003', 'T2509', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'TE2201', 'CS005', 'T2510', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'TE2201', 'IE101', 'T2514', 0.30, 0.40, 0.30),
('2026-2027 第一学期', 'ACC2201', 'DS001', 'T2511', 0.40, 0.40, 0.20),
('2026-2027 第一学期', 'ACC2201', 'BA101', 'T2516', 0.50, 0.30, 0.20),
('2026-2027 第一学期', 'ACC2201', 'PE101', 'T2513', 0.30, 0.50, 0.20);

DROP TEMPORARY TABLE IF EXISTS tmp_existing_teaching;
CREATE TEMPORARY TABLE tmp_existing_teaching AS
SELECT tcTermId, tcClassId, tcCourseId FROM teaching;

INSERT INTO teaching (tcTermId, tcClassId, tcCourseId, tcTeacherId, w_exam, w_regular, w_test)
SELECT y.yId, c.cId, cr.crId, t.tId, st.w_exam, st.w_regular, st.w_test
FROM tmp_seed_teaching st
JOIN yearterm y ON y.yAll = st.termAll
JOIN classinfo c ON c.cCode = st.classCode
JOIN courseinfo cr ON cr.crCode = st.courseCode
JOIN teacherinfo t ON t.tNo = st.teacherNo
LEFT JOIN tmp_existing_teaching e ON e.tcTermId = y.yId AND e.tcClassId = c.cId AND e.tcCourseId = cr.crId
WHERE e.tcTermId IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_existing_scores;
CREATE TEMPORARY TABLE tmp_existing_scores AS
SELECT scStudentId, scTermId, scCourseId FROM scoreinfo;

INSERT INTO scoreinfo (scStudentId, scTermId, scClassId, scCourseId, scScore, scStatus, scCreateDate, scTeacherId, sc_regular, sc_test, sc_exam)
SELECT q.sid,
       q.yId,
       q.cId,
       q.crId,
       ROUND(q.w_regular * q.regular_score + q.w_test * q.test_score + q.w_exam * q.exam_score, 1) AS final_score,
       1,
       '2026-05-12 20:30:00',
       q.tId,
       q.regular_score,
       q.test_score,
       q.exam_score
FROM (
  SELECT s.sid,
         y.yId,
         c.cId,
         cr.crId,
         t.tId,
         st.w_exam,
         st.w_regular,
         st.w_test,
         ROUND(58 + MOD(s.sid * 13 + cr.crId * 7 + y.yId * 5, 43), 2) AS regular_score,
         ROUND(52 + MOD(s.sid * 11 + cr.crId * 9 + y.yId * 3, 49), 2) AS test_score,
         ROUND(45 + MOD(s.sid * 17 + cr.crId * 5 + y.yId * 7, 56), 2) AS exam_score
  FROM tmp_seed_teaching st
  JOIN yearterm y ON y.yAll = st.termAll
  JOIN classinfo c ON c.cCode = st.classCode
  JOIN courseinfo cr ON cr.crCode = st.courseCode
  JOIN teacherinfo t ON t.tNo = st.teacherNo
  JOIN studentinfo s ON s.sClassId = c.cId
  JOIN tmp_seed_students ss ON ss.sNo = s.sNo
) q
LEFT JOIN tmp_existing_scores e ON e.scStudentId = q.sid AND e.scTermId = q.yId AND e.scCourseId = q.crId
WHERE e.scStudentId IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_existing_score_details;
CREATE TEMPORARY TABLE tmp_existing_score_details AS
SELECT student_id, course_id, point_id FROM score_detail;

INSERT INTO score_detail (student_id, course_id, point_id, max_score, actual_score, create_time)
SELECT q.student_id,
       q.course_id,
       q.point_id,
       100,
       ROUND(GREATEST(30, LEAST(100, q.base_score + MOD(q.student_id * 7 + q.point_id * 3 + q.course_id, 23) - 11)), 1) AS actual_score,
       '2026-05-12 20:35:00'
FROM (
  SELECT si.scStudentId AS student_id,
         si.scCourseId AS course_id,
         kp.id AS point_id,
         MIN(si.scScore) AS base_score
  FROM scoreinfo si
  JOIN yearterm y ON y.yId = si.scTermId
  JOIN classinfo c ON c.cId = si.scClassId
  JOIN courseinfo cr ON cr.crId = si.scCourseId
  JOIN tmp_seed_teaching st ON st.termAll = y.yAll AND st.classCode = c.cCode AND st.courseCode = cr.crCode
  JOIN knowledge_point kp ON kp.course_id = si.scCourseId
  GROUP BY si.scStudentId, si.scCourseId, kp.id
) q
LEFT JOIN tmp_existing_score_details e ON e.student_id = q.student_id AND e.course_id = q.course_id AND e.point_id = q.point_id
WHERE e.student_id IS NULL;

-- ----------------------------
-- 学业预警数据
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_existing_warnings;
CREATE TEMPORARY TABLE tmp_existing_warnings AS
SELECT wStudentId, wCourseId FROM academic_warning;

INSERT INTO academic_warning (wStudentId, wCourseId, wRiskLevel, wProbability, wReason, wStatus, wCreateDate)
SELECT q.student_id,
       q.course_id,
       CASE
         WHEN q.score < 55 THEN '高风险'
         WHEN q.score < 62 THEN '中风险'
         ELSE '低风险'
       END AS risk_level,
       ROUND(LEAST(0.95, GREATEST(0.35, (68 - q.score) / 30 + 0.45)), 2) AS probability,
       CONCAT('综合成绩', q.score, '分，建议进行学习帮扶与知识点复盘') AS reason,
       0,
       '2026-05-12 20:40:00'
FROM (
  SELECT si.scStudentId AS student_id,
         si.scCourseId AS course_id,
         MIN(si.scScore) AS score
  FROM scoreinfo si
  JOIN yearterm y ON y.yId = si.scTermId
  JOIN classinfo c ON c.cId = si.scClassId
  JOIN courseinfo cr ON cr.crId = si.scCourseId
  JOIN tmp_seed_teaching st ON st.termAll = y.yAll AND st.classCode = c.cCode AND st.courseCode = cr.crCode
  GROUP BY si.scStudentId, si.scCourseId
) q
LEFT JOIN tmp_existing_warnings e ON e.wStudentId = q.student_id AND e.wCourseId = q.course_id
WHERE q.score < 68
  AND e.wStudentId IS NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_seed_terms;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_terms;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_notices;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_notice_titles;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_colleges;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_college_codes;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_majors;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_major_codes;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_classes;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_class_codes;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_courses;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_course_codes;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_knowledge;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_knowledge;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_teachers;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_user_logs;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_teacher_nos;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_students;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_student_user_logs;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_student_nos;
DROP TEMPORARY TABLE IF EXISTS tmp_seed_teaching;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_teaching;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_scores;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_score_details;
DROP TEMPORARY TABLE IF EXISTS tmp_existing_warnings;

SET FOREIGN_KEY_CHECKS = 1;
