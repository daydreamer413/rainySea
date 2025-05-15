<template>
<div>
  <div class="common-layout">
    <el-container>

<!-- 头部区域开始-->
      <el-header  style="height: 60px;border-bottom: 1px solid #edf0f8;background-color: #10d8d8" >
        <div style="display: flex;">
          <div style="flex: 1;display: flex;align-items: center;">
            <div style="font-size: 18px;font-weight: bold;">后台管理中心</div>
          </div>
          <div style="flex: 1;text-align: left;padding-left: 15px;padding-top: 20px;">
            <el-icon style="font-size: 20px;" @click="handleCollapse"><Switch /></el-icon>
          </div>
          <div style="flex: 3"></div>
          <div style="display: flex;text-align: right;padding-right: 10px; align-items: center;">
            <img style="margin-top: 10px;width: 40px; height: 40px;margin-right: 10px; border-radius: 50%" :src="data.admin.img" alt="">
            <el-dropdown role="navigation">
                <span class="el-dropdown-link">
                  <b>{{data.admin.role}} / {{data.admin.name}}
                  <el-icon class="el-icon--right"><arrow-down /></el-icon></b>
                </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout"><el-icon><SwitchButton /></el-icon> 安全退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

          </div>

        </div>
      </el-header >
      <el-container>
        <!-- 左侧导航区域开始-->
        <el-aside :width="data.isWidth" >

          <el-menu
                   active-text-color="#000"
                   background-color="#c6e2ff"
                   class="el-menu-vertical-demo"
                   :default-active="router.currentRoute.value.path"
                   text-color="#000"
                   router
                   :collapse="data.isCollapse"
                   :collapse-transition="false" >
            <el-menu-item index="/manager/home"><el-icon><Grid /></el-icon><span>系统首页</span></el-menu-item>
            <el-sub-menu index="1">
              <template #title><el-icon><Monitor /></el-icon><span>基础信息管理</span></template>
              <template v-if="data.admin.role==='管理员'">
                <el-menu-item index="/manager/imgadv"><el-icon><Tickets /></el-icon> 轮播图管理</el-menu-item>
                <el-menu-item index="/manager/news"><el-icon><Tickets /></el-icon> 健康资讯管理</el-menu-item>
                <el-menu-item index="/manager/message"><el-icon><Tickets /></el-icon> 留言信息管理</el-menu-item>
                <el-menu-item index="/manager/goods"><el-icon><Tickets /></el-icon> 食物信息管理</el-menu-item>
                <el-menu-item index="/manager/jilu"><el-icon><Tickets /></el-icon> 用户饮食记录</el-menu-item>
              </template>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title><el-icon><Folder /></el-icon><span>菜品信息管理</span></template>
                <el-menu-item index="/manager/category"><el-icon><Tickets /></el-icon> 分类信息管理</el-menu-item>
                <el-menu-item index="/manager/protype"><el-icon><Tickets /></el-icon> 口味分类管理</el-menu-item>
              <el-menu-item index="/manager/shuju"><el-icon><Tickets /></el-icon> 菜品信息管理</el-menu-item>
              <el-menu-item index="/manager/evaluate"><el-icon><Tickets /></el-icon> 菜品评论管理</el-menu-item>

            </el-sub-menu>
            <el-sub-menu index="3" v-if="data.admin.role==='管理员'">
              <template #title><el-icon><UserFilled /></el-icon><span>用户信息管理</span></template>
              <el-menu-item index="/manager/admin"><el-icon><User /></el-icon> 管理员信息管理</el-menu-item>
              <el-menu-item index="/manager/member"><el-icon><User /></el-icon> 用户信息管理</el-menu-item>
              <el-menu-item index="/manager/rizhi"><el-icon><User /></el-icon> 日志信息管理</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="4">
              <template #title><el-icon><Avatar/></el-icon><span>个人信息管理</span></template>
              <el-menu-item index="/manager/person"><el-icon><User /></el-icon> 个人信息</el-menu-item>
              <el-menu-item index="/manager/passuedit"><el-icon><Unlock /></el-icon> 个人密码</el-menu-item>
            </el-sub-menu>

          </el-menu>
        </el-aside>
        <!-- 主体区域开始-->
        <el-main class="content-container" style="background-color: #f8f8ff; padding: 10px">
        <RouterView @updateUser="updateUser"/>
        </el-main>
      </el-container>
    </el-container>
  </div>

</div>


</template>

<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {
  ArrowDown,
  Connection,
  ChatSquare,
  Grid,
  House,
  Reading,
  Operation,
  SwitchButton,
  UserFilled,
  View, Switch, Folder, FolderChecked, Monitor, Tickets, Avatar, Unlock, User
} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import request from "@/utils/request.js";
import qs from "qs";
const route = new useRouter()
const data = reactive({
  isCollapse:false,
  isWidth:'250px',
  admin: JSON.parse(localStorage.getItem('admin') || '{}')
})
const handleCollapse =()=>{
  data.isCollapse = !data.isCollapse
  data.isWidth = data.isCollapse ? '64px' : '250px'
}
const updateUser = () => {
  data.admin = JSON.parse(localStorage.getItem('admin') || '{}')
}

const logout = () => {
  request.post('logoutAdmin', qs.stringify({
    username:data.admin.username,
  })).then(res => {})
  ElMessage.success('操作成功')
  localStorage.removeItem('admin')
  route.push('/login')
}
</script>

<style scoped>
/*取消原有边框*/
.el-menu{
  border-right: none !important;
}

/*设置设置鼠标移入字体高亮*/
.el-menu-item:hover{
  color: #fff !important;
  background-color: #a0cfff !important;
  font-weight: bolder;
}
/*设置选中菜单的背景色*/
.el-menu-item.is-active{
  background-color:#a0cfff !important;
  border-right: 3px solid #10d8d8;
  color: #fff;
  font-weight: bold;
}
.el-aside{
  transition: width .5s;/*设置淡出效果*/
  //box-shadow: 2px 0 6px rgba(0,21,41,.35);/*设置边框阴影*/
}
.el-dropdown-link {
  cursor: pointer !important;
}
.el-menu-vertical-demo, .content-container {
  overflow: auto; /* 使得内容过多时可滚动 */
  height: calc(100vh - 60px); /* 假设顶部有60px的其他内容，调整数值以匹配实际情况 */
}

</style>