<template>
    <div>
      <div class="card" style="width: 50%">
        <h3 style="text-align: center">编辑个人密码</h3>
        <el-form :model="data" label-width="100px" ref="formRef" :rules="data.rules" style="padding-right: 50px;margin-top: 20px;">
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
            <el-button type="primary" @click="save">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import router from "@/router/index.js";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";

//提交时表单校验
const formRef = ref()
const data = reactive({
  admin: JSON.parse(localStorage.getItem('admin') || '{}'),
  onlpassword:'',
  newpassword:'',
  confirmPasword:'',
  rules:{
    onlpassword: [{ required: true, message: '原密码不能为空', trigger: 'blur' },],
    newpassword: [{ required: true, message: '新密码不能为空', trigger: 'blur' },],
    confirmPasword: [{ required: true, message: '确认密码不能为空', trigger: 'blur' },]
  }
})

// 修改个人密码
const save = () => {
  formRef.value.validate((valid=>{
    if(valid){
  if(data.newpassword!==data.confirmPasword){
    data.newpassword=''
    data.confirmPasword=''
    ElMessage.error("两次密码输入不一致")
    return
  }

  request.post('adminpassEdit', qs.stringify({
    id:data.admin.id,
    password:data.admin.password,
    onlpassword:data.onlpassword,
    newpassword:data.newpassword,
    confirmPasword:data.confirmPasword,
  })).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功,请重新登录')
      localStorage.removeItem('admin')
      router.push('/login')
    } else {
      data.onlpassword=''
      ElMessage.error(res.msg)
    }
  })
    }
  }))
}

</script>
