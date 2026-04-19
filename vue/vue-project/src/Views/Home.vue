<template>
  <div class="home-page">
    <div class="welcome-banner">
      <div class="banner-left">
        <h2>欢迎使用学生成绩管理系统</h2>
        <p>本系统用于学生、教师和管理员的成绩信息管理，支持基础信息维护、成绩查询、统计分析与个人中心管理。</p>
      </div>
      <div class="banner-right">
        <div class="target-title">系统适用角色：</div>
        <div class="target-text">管理员：负责基础数据、用户与公告管理</div>
        <div class="target-text">教师：负责成绩录入、查询与统计</div>
        <div class="target-text">学生：负责个人成绩与课程信息查看</div>
      </div>
    </div>

    <div class="card-row">
      <el-card class="info-card" shadow="hover">
        <template #header>
          <div class="card-title">前端技术栈</div>
        </template>
        <div class="tags-wrap">
          <el-tag class="tag-item" type="info">Vue 3</el-tag>
          <el-tag class="tag-item" type="success">Element Plus</el-tag>
          <el-tag class="tag-item" type="warning">Vue Router</el-tag>
          <el-tag class="tag-item" type="danger">Axios</el-tag>
          <el-tag class="tag-item">JavaScript</el-tag>
        </div>
      </el-card>

      <el-card class="info-card" shadow="hover">
        <template #header>
          <div class="card-title">后端技术栈</div>
        </template>
        <div class="tags-wrap">
          <el-tag class="tag-item" type="info">Spring Boot</el-tag>
          <el-tag class="tag-item" type="success">MyBatis-Plus</el-tag>
          <el-tag class="tag-item" type="warning">MySQL</el-tag>
          <el-tag class="tag-item" type="danger">JWT</el-tag>
          <el-tag class="tag-item">Maven</el-tag>
        </div>
      </el-card>
    </div>

    <el-card class="about-card" shadow="hover">
      <template #header>
        <div class="card-title">关于系统</div>
      </template>
      <div class="about-content">
        <p>学生成绩管理系统是面向学校教学管理场景设计的信息化管理平台。</p>
        <p>系统支持管理员、教师、学生三类角色登录使用，实现用户管理、课程管理、成绩管理、公告管理等常用功能。</p>
        <p>通过该系统可以提高成绩管理效率，规范教学数据维护流程，方便信息查询与统计分析。</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home-page {
  width: 100%;
  min-height: 100%;
  box-sizing: border-box;
}

.welcome-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
  padding: 34px 28px;
  border-radius: 10px;
  color: #fff;
  background: linear-gradient(90deg, #5b7cff 0%, #7d4ac7 100%);
  box-shadow: 0 8px 24px rgba(91, 124, 255, 0.18);
  margin-bottom: 24px;
}

.banner-left h2 {
  margin: 0 0 18px;
  font-size: 22px;
  font-weight: 700;
}

.banner-left p {
  margin: 0;
  font-size: 15px;
  opacity: 0.95;
  line-height: 1.8;
}

.banner-right {
  min-width: 260px;
  text-align: left;
  line-height: 2;
}

.target-title {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 6px;
}

.target-text {
  font-size: 14px;
  opacity: 0.95;
}

.card-row {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.info-card {
  flex: 1;
  border-radius: 10px;
}

.about-card {
  border-radius: 10px;
}

.card-title {
  text-align: center;
  font-size: 16px;
  font-weight: 700;
  color: #444;
}

.tags-wrap {
  min-height: 90px;
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  justify-content: center;
  align-items: center;
  padding: 10px 0;
}

.tag-item {
  padding: 0 14px;
}

.about-content {
  min-height: 120px;
  text-align: center;
  color: #666;
  font-size: 16px;
  line-height: 2.1;
  padding: 18px 10px;
}

@media (max-width: 900px) {
  .welcome-banner {
    flex-direction: column;
    align-items: flex-start;
  }

  .card-row {
    flex-direction: column;
  }

  .banner-right {
    min-width: auto;
  }
}
</style>
