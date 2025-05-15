<template>
<div>
  <div class="common-layout">
    <el-container>
      <el-header style="padding: 0;">
        <el-menu
            class="el-menu-demo"
            mode="horizontal"
            text-color="#000"
            background-color="#10d8d8"
            active-text-color="#fff"
            :default-active="router.currentRoute.value.path"
            router>
          <div style="display: flex;">
            <div style="display: flex;align-items: center;min-width: 450px;justify-content: center;color: #606266;font-size: 16px;font-weight: bold;margin: 0 40px;">
              <el-image :src="logimg" style="width: 40px;height: 40px;border-radius: 50%"/>
              个性化健康饮食系统
            </div>
          </div>
          <el-menu-item index="/front/index">首页</el-menu-item>
          <el-menu-item index="/front/newslist">健康资讯</el-menu-item>
          <el-menu-item index="/front/shujulist">菜品信息</el-menu-item>
          <el-menu-item index="/front/goodslist">食物信息</el-menu-item>
          <el-menu-item index="/front/messagelist">留言建议</el-menu-item>
          <div style="display: flex;float: right;margin-left: 200px;" v-if="JSON.stringify(data.member)!=='{}'">
            <div style="display: flex;align-items: center;text-align: center;justify-content: center;font-size: 14px;font-weight: bold;">
              <el-sub-menu index="2">
                <template #title>个人中心</template>
                <el-menu-item index="/front/favlist"><el-icon><CircleCheck /></el-icon> 收藏记录</el-menu-item>
                <el-menu-item index="/front/shujulb"><el-icon><CircleCheck /></el-icon> 菜品信息管理</el-menu-item>
                <el-menu-item index="/front/cartlist"><el-icon><CircleCheck /></el-icon> 饮食清单</el-menu-item>
                <el-menu-item index="/front/jilulist"><el-icon><CircleCheck /></el-icon> 每日饮食记录</el-menu-item>
                <el-menu-item index="/front/memberpreson"><el-icon><User /></el-icon> 个人信息编辑</el-menu-item>
                <el-menu-item index="/front/memberpass"><el-icon><lock/></el-icon> 个人密码编辑</el-menu-item>
                <el-menu-item @click="logout"><el-icon><SwitchButton /></el-icon> 安全退出</el-menu-item>
              </el-sub-menu>
              <img style="width: 40px; height: 40px;margin-right: 10px; border-radius: 50%" :src="data.member.img" alt="">
              <span>{{data.member.username}} / {{data.member.name}}</span>
            </div>
          </div>

          <div style="display: flex;float: right;margin-left: 350px;" v-if="JSON.stringify(data.member)==='{}'">
            <div style="display: flex;align-items: center;text-align: center;justify-content: center;color: #ffffff;font-size: 14px;font-weight: bold;">
              <el-button type="success" size="small" @click="router.push('/front/memberLogin')">登录</el-button>
              <el-button type="warning" size="small" @click="router.push('/front/memberReg')">注册</el-button>
            </div>
          </div>

        </el-menu>

      </el-header>
      <el-main style="min-height: calc(100vh - 120px);padding: 0;background-color: #ffffff">
        <RouterView @updatemember="updatemember"/>
      </el-main>
      <el-footer style="background-color: #000;height: 60px;padding: 0;">
          <div  style="display: flex;justify-content: center;padding-top: 20px;font-size: 16px;font-weight: bold;">
            <a href="/login" target="_blank" style="color: #fff;text-decoration: none">后台管理中心</a>
          </div>

        <el-backtop :bottom="100">
          <div style=" height: 100%; width: 100%; background-color: var(--el-bg-color-overlay); box-shadow: var(--el-box-shadow-lighter); text-align: center; line-height: 40px; color: #1989fa;">
            <el-icon><ArrowUpBold /></el-icon></div>
        </el-backtop>
      </el-footer>
    </el-container>
  </div>



</div>


</template>

<script setup>
import {onUnmounted, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {ArrowDown, ArrowUpBold, CircleCheck, Lock, SwitchButton, User} from "@element-plus/icons-vue";
import {listenMsg} from "@/utils/crossTagMsg.js";
import request from "@/utils/request.js";
import qs from "qs";
import {useRouter} from "vue-router";
import logimg from '@/assets/imgs/123.png'
const router = useRouter()
const cancelListen = listenMsg(msginfo=>{
if(msginfo.type==='封禁' && msginfo.conent===data.member.id){
  logout()
}
})
onUnmounted(cancelListen)

const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}')

})

const updatemember = () => {
  data.member = JSON.parse(localStorage.getItem('member') || '{}')
}

const logout = () => {
  request.post('logoutMember', qs.stringify({
    username:data.member.username,
  })).then(res => {})

  ElMessage.success('操作成功')
  localStorage.removeItem('member')
  //router.push('/front/memberLogin')
  window.location.href = '/front/memberLogin'
}
</script>

<style scoped>

.el-menu-demo .el-menu-item.is-active {
  border-bottom: none;
}

/*鼠标移入高亮*/
.el-menu-item:hover{
  color: #fff !important;
  font-weight: bold;
  background-color: #10d8d8 !important;
}

/*设置选中菜单的背景色*/
.el-menu-item.is-active{
  font-weight: bold;
  color: #fff !important;
  background-color: #10d8d8 !important;
  --el-menu-hover-bg-color:#10d8d8;
}
/*二级目录*/
.el-sub-menu:hover{
  color: #fff !important;
  font-weight: bold !important;
 /* background-color: white !important;*/
}
.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}
</style>