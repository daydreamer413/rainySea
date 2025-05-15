<template>
    <div >
      <div class="login-container">
        <div class="card" style="min-height: 400px;width: 50%;margin: 100px auto;">
          <div style="text-align: center;"><h3>个人密码编辑</h3> </div>

          <el-form style="max-width: 500px;margin: 40px auto" label-width="100px" ref="formRef"  :model="data" :rules="data.rules" class="demo-ruleForm">
            <el-form-item label="原密码" prop="onlpassword">
              <el-input v-model="data.onlpassword"  placeholder="请输入原密码" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newpassword">
              <el-input v-model="data.newpassword"  placeholder="请输入新密码" show-password/>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPasword">
              <el-input  v-model="data.confirmPasword" placeholder="请输入确认密码" show-password/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="save" >提交</el-button>
            </el-form-item>
          </el-form>

        </div>
      </div>
      </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import qs from "qs";
import {ElMessage} from "element-plus";

//表单校验
const formRef = ref()
//文件上传的接口地址
const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}'),
  onlpassword:'',
  newpassword:'',
  confirmPasword:'',
  rules:{
    onlpassword: [{ required: true, message: '原密码不能为空', trigger: 'blur' },],
    newpassword: [{ required: true, message: '新密码不能为空', trigger: 'blur' },],
    confirmPasword: [{ required: true, message: '确认密码不能为空', trigger: 'blur' },]
  }
})
//提交表单
const save=()=>{
  formRef.value.validate((valid=>{
    if(valid){
      if(data.newpassword!==data.confirmPasword){
        data.newpassword=''
        data.confirmPasword=''
        ElMessage.error("两次密码输入不一致")
        return
      }
      request.post('memberPass',qs.stringify({
        id:data.member.id,
        password:data.member.password,
        onlpassword:data.onlpassword,
        newpassword:data.newpassword,
        confirmPasword:data.confirmPasword,
      })).then(res=>{
        if(res.code=='200'){
          ElMessage.success('操作成功,请重新登录')
          localStorage.removeItem('member')
          //router.push('/front/memberLogin')
          setTimeout(function(){
            window.location.href='/front/memberLogin'
          },1000)
        }else {
          ElMessage.error(res.msg)
        }
      })

    }
  }))
}
</script>

<style scoped>
.login-container {
  height:calc(100vh - 100px) ;
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  /*background-color: #a0cfff;*/
 /* background-image: url("@/assets/imgs/bg.png");*/
  background-size: cover;
}
</style>
