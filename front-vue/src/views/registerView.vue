<template>
<div>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">员 工 用 户 注 册</div>
      <el-form :model="data.form" label-width="80" ref="formRef" :rules="data.rules">
        <el-form-item label="头像" >
          <el-upload
              class="avatar-uploader"
              :action="uploadimgUrl"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              accept=".jpg,.png,.jpeg,.JPG,.PNG">
            <img  v-if="data.form.img" :src="data.form.img" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input  v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input  v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input  v-model="data.form.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" >
          <el-radio-group v-model="data.form.sex">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input type="number" min="1"  v-model="data.form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="联系方式" prop="tel">
          <el-input  v-model="data.form.tel" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="所在分类" prop="categoryid">
          <el-select  style="width: 100%" v-model="data.form.categoryid">
            <el-option value="" label="请选择分类"></el-option>
            <el-option v-for="category in data.categoryList" :key="category.id" :value="category.id" :label="category.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职务" prop="typeid">
          <el-select  style="width: 100%" v-model="data.form.typeid">
            <el-option value="" label="请选择职务"></el-option>
            <el-option v-for="systype in data.systypeList" :key="systype.id" :value="systype.id" :label="systype.name"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button  type="primary" style="width: 100%" @click="login">提 交</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right;">
        已有账号？ <a href="javascript:void (0)" @click="router.push('/')" style="color: red;">返回登录</a>
      </div>
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
const uploadimgUrl = import.meta.env.VITE_BASE_URL+ '/fles/upload'
const data = reactive({
  form:{
    sex:'男',
  },
  categoryList:[],
  systypeList:[],
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' },],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' },],
    name: [{ required: true, message: '请输入姓名', trigger: 'blur' },],
    age: [{ required: true, message: '请输入年龄', trigger: 'blur' },],
    tel: [{ required: true, message: '请输入联系方式', trigger: 'blur' },],
    categoryid: [{ required: true, message: '分类不能为空', trigger: 'blur' },],
    typeid: [{ required: true, message: '职务不能为空', trigger: 'blur' },],
}
})
//查询分类列表
const catelist=()=>{
  request.post('cateListAll', qs.stringify({
  })).then(res => {
    data.categoryList = res.data
  })
}

//查询职务列表
const typelist=()=>{
  request.post('systypeListAll', qs.stringify({
  })).then(res => {
    data.systypeList = res.data
  })
}
//提交登录表单
const login=()=>{
  formRef.value.validate((valid=>{
    if(valid){
      if(!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(data.form.tel)){ElMessage.error("电话号码格式有误");return ;}
      request.post('adminReg',qs.stringify({
        username:data.form.username,
        password:data.form.password,
        name:data.form.name,
        sex:data.form.sex,
        age:data.form.age,
        tel:data.form.tel,
        img:data.form.img,
        categoryid:data.form.categoryid,
        typeid:data.form.typeid,
        role:'用户',
      })).then(res=>{
        if(res.code==='200'){
          ElMessage({showClose: true, message: '注册成功，请登录', type: 'success'});
          router.push('/');
        }else {
          ElMessage.error(res.msg)
        }
      })

    }
  }))
}
// 处理文件上传的钩子
const handleAvatarSuccess = (res) => {
  data.form.img = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}
catelist()
typelist()
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow:hidden;
  display: flex;
  //justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bj-001.jpg");
  background-size: 100% 100%;
}
.login-box {
  margin-left: 200px;
  width: 450px;
  padding: 50px 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0,.1);
  background-color: rgba(255, 255, 255, 1);
}
</style>