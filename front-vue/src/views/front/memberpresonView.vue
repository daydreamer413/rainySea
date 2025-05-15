<template>
    <div >
      <div class="login-container">
        <div class="card" style="min-height: 400px;width: 50%;margin: 100px auto;">
          <div style="text-align: center;"><h3>个人信息编辑</h3> </div>

          <el-form style="max-width: 500px;margin: 40px auto" label-width="100px" ref="formRef"  :model="data.member" :rules="data.rules" class="demo-ruleForm">
            <el-form-item label="头像" >
              <el-upload
                  class="avatar-uploader"
                  :action="uploadimgUrl"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  accept=".jpg,.png,.jpeg,.JPG,.PNG">
                <img  v-if="data.member.img" :src="data.member.img" class="avatar">
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="账号" prop="username">
              <el-input  v-model="data.member.username" disabled placeholder="请输入账号" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input  v-model="data.member.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="性别" >
              <el-radio-group v-model="data.member.sex">
                <el-radio value="男">男</el-radio>
                <el-radio value="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input type="number" min="1"  v-model="data.member.age" placeholder="请输入年龄" />
            </el-form-item>
            <el-form-item label="联系方式" prop="tel">
              <el-input  v-model="data.member.tel" placeholder="请输入联系方式" />
            </el-form-item>
            <el-form-item label="口味偏好" prop="protypeid">
              <el-select v-model="data.member.protypeid" placeholder="请选择口味偏好">
                <el-option v-for="protype in data.protypelist" :key="protype.id" :label="protype.name" :value="protype.id" />
              </el-select>
            </el-form-item>

            <el-form-item label="健康状况" >
              <el-radio-group v-model="data.member.jktype">
                <el-radio value="健康">健康</el-radio>
                <el-radio value="亚健康">亚健康</el-radio>
                <el-radio value="疾病">疾病</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="饮食目标" >
              <el-radio-group v-model="data.member.mbtype">
                <el-radio value="减肥">减肥</el-radio>
                <el-radio value="增肌">增肌</el-radio>
                <el-radio value="维持体重">维持体重</el-radio>
              </el-radio-group>
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
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}'),
  rules: {
    name: [{required: true, message: '请输入姓名', trigger: 'blur'},],
    age: [{required: true, message: '请输入年龄', trigger: 'blur'},],
    tel: [{required: true, message: '请输入联系方式', trigger: 'blur'},],
    protypeid: [{ required: true, message: '口味偏好不能为空', trigger: 'blur' },],
  },
  protypelist:[],
})
//查询口味分类列表
const getPlist=()=>{
  request.post('protypeListAll').then(res=>{
    data.protypelist = res.data
  })
}
const emit = defineEmits(["updatemember"])
//提交表单
const save=()=>{
  formRef.value.validate((valid=>{
    if(valid){
      if(!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(data.member.tel)){ElMessage.error("电话号码格式有误");return ;}
      request.post('memberAdd',qs.stringify({
        id:data.member.id,
        img:data.member.img,
        name:data.member.name,
        sex:data.member.sex,
        age:data.member.age,
        tel:data.member.tel,
        protypeid:data.member.protypeid,
        mbtype:data.member.mbtype,
        jktype:data.member.jktype,
      })).then(res=>{
        if(res.code==='200'){
          ElMessage.success('操作成功')
          localStorage.setItem('member', JSON.stringify(data.member))
          emit('updatemember')
        }else {
          ElMessage.error(res.msg)
        }
      })

    }
  }))
}
// 处理文件上传的钩子
const handleAvatarSuccess = (res) => {
  data.member.img = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}
getPlist()
</script>

<style scoped>
.login-container {
  height:calc(100vh - 10px) ;
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  /*background-color: #a0cfff;*/
 /* background-image: url("@/assets/imgs/bg.png");*/
  background-size: cover;
}
.avatar-uploader .avatar {
  width: 80px !important;
  height: 80px !important;;
  display: block !important;;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
}
</style>
