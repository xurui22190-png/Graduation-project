<template>
  <el-container class="main-container">
    <el-header class="main-header">
      <HeaderLayout />
    </el-header>

    <el-container>
      <el-aside width="200px" class="main-aside">
        <Asider />
      </el-aside>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import HeaderLayout from "../components/HeaderLayout.vue"
import Asider from "../components/Asider.vue"
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.main-header {
  padding: 0;
}

.main-aside {
  background: #545c64;
}

.main-content {
  background: #f5f5f5;
  padding: 20px;
  box-sizing: border-box;
  overflow: auto;
}
</style>
