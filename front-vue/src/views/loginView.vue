<template>
<div>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #0dadad">
        后 台 管 理 中 心
      </div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="verCode">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.verCode" style="width: 45%" placeholder="请输入验证码"/>
          <img :src="data.captchaUrl" @click="clickimg" style="width: 150px;height: 33px;">
        </el-form-item>
<!--        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option value="" label="请选择用户角色"></el-option>
            <el-option value="管理员" label="管理员"></el-option>
          </el-select>
        </el-form-item>-->
        <el-form-item>
          <el-button size="default" type="info"  style="width: 100%" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
<!--      <div style="text-align: right;">
        还没有账号？请 <a href="javascript:void (0)" @click="router.push('/register')" style="color: red;">注册</a>
      </div>-->
    </div>

  </div>

</div>
</template>

<script setup>
import { User, Lock } from "@element-plus/icons-vue";
import {onMounted, reactive, ref} from "vue";
import qs from "qs";
import request from '@/utils/request.js'
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

const router = useRouter()
//表单校验
const formRef = ref()
// 后台端口
const uploadimgUrl = import.meta.env.VITE_BASE_URL
const data = reactive({
  key:'',//验证码唯一KEY值
  captchaUrl:'',
  form:{role:'管理员'},
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' },],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' },],
    verCode: [{ required: true, message: '请输入验证码', trigger: 'blur' },],}
})
//获取验证码
onMounted(() => {
  data.key = Math.random()
  data.captchaUrl = uploadimgUrl+'/captcha?key='+data.key
})
//点击更换验证码
const clickimg = ()=>{
  data.key = Math.random()
  data.captchaUrl = uploadimgUrl+'/captcha?key='+data.key
}
//提交登录表单
const login=()=>{
  formRef.value.validate((valid=>{
    if(valid){
      request.post('adminLogin',qs.stringify({
        username:data.form.username,
        password:data.form.password,
        role:data.form.role,
        verCode:data.form.verCode,
        key:data.key,
      })).then(res=>{
        if(res.code==='200'){
          ElMessage({showClose: true, message: '登录成功', type: 'success'});
          localStorage.setItem("admin",JSON.stringify(res.data));
          router.push('/manager');
        }else {
          ElMessage.error(res.msg)
          data.key = Math.random()
          data.captchaUrl = uploadimgUrl+'/captcha?key='+data.key
          data.form.verCode=''
        }
      })

    }
  }))
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/ms-03.jpg");
  background-color: rgba(0, 0, 0, 0.96);
  background-size: 100% 100%;
}
.login-box {
  width: 350px;
  padding: 50px 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0,.1);
  background-color: rgba(255, 255, 255, 0.4);
  margin-left: 700px;
}
</style>