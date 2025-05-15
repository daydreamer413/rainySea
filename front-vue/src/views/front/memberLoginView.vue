<template>
    <div >
      <div class="login-container">
        <div class="card" style="min-height: 350px;width: 30%;margin: 100px auto">
          <div style="text-align: center">
            <el-divider><h3>用户登录</h3> </el-divider></div>

          <el-form :model="data.form"  ref="formRef" :rules="data.rules" style="padding: 15px;">
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
            <el-form-item>
              <el-button size="large" type="info" style="width: 20%" @click="login">登 录</el-button>
              <el-button size="large" type="success" style="width: 20%" @click="router.push('/front/memberReg')">注 册</el-button>
            </el-form-item>
          </el-form>
          <div style="text-align: right;">
            忘记密码？ <a href="javascript:void (0)"  @click="Add" style="color: red">点击找回</a>
          </div>
        </div>
      </div>

      <el-dialog title="找回密码" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.zhform" ref="formRef" :rules="data.zhrules" label-width="100px" style="padding-right: 50px;margin-top: 20px;">
          <el-form-item label="账号" prop="username">
            <el-input v-model="data.zhform.username" placeholder="请输入输入账号" autocomplete="off" />
          </el-form-item>
          <el-form-item label="联系方式" prop="tel">
            <el-input v-model="data.zhform.tel" placeholder="请输入联系方式" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="zhsave">提 交</el-button>
      </span>
        </template>
      </el-dialog>
      </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import request from "@/utils/request.js";
import qs from "qs";
import {Lock, User} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

const router = useRouter()
//表单校验
const formRef = ref()
// 后台端口
const uploadimgUrl = import.meta.env.VITE_BASE_URL
const data = reactive({
  key:'',
  form:{},
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' },],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' },],
    verCode: [{ required: true, message: '请输入验证码', trigger: 'blur' },],
  },
  zhrules:{
    username: [{ required: true, message: '请输入账号', trigger: 'blur' },],
    tel: [{ required: true, message: '请输入联系方式', trigger: 'blur' },],
  }
})

// 找回密码
const Add = () => {
  data.zhform = {}
  data.formVisible = true
}
//提交找回信息
const zhsave = () => {
  formRef.value.validate((valid=>{
    if (valid){
      request.post('password', qs.stringify({
        username:data.zhform.username,
        tel:data.zhform.tel,
      })).then(res => {
        if (res.code === '200') {
          ElMessage.success(res.data)
          data.formVisible = false
        } else {
          ElMessage.error(res.msg)
        }
        setTimeout(function(){
          window.location.reload();//刷新当前页面.
        },1000)


      })
    }
  }))

}
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
      request.post('memberLogin',qs.stringify({
        username:data.form.username,
        password:data.form.password,
        verCode:data.form.verCode,
        key:data.key,
      })).then(res=>{
        if(res.code==='200'){
          ElMessage({showClose: true, message: '登录成功', type: 'success'});
          localStorage.setItem("member",JSON.stringify(res.data));
          //router.push('/');
          window.location.href = '/'
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
  height:calc(100vh - 120px) ;
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgb(197.7, 225.9, 255);
  background-image: url("@/assets/imgs/ms-04.jpg");
  background-size: cover;
}
</style>
