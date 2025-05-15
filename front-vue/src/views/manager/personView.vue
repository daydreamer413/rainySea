<template>
    <div>
      <div class="card" style="width: 50%">
        <h3 style="text-align: center">编辑个人信息</h3>
        <el-form :model="data.admin" label-width="100px" ref="formRef" :rules="data.rules" style="padding-right: 50px;margin-top: 20px;">
          <el-form-item label="头像" prop="avatar">
            <el-upload
                class="avatar-uploader"
                :action="uploadimgUrl"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                accept=".jpg,.png,.jpeg,.JPG,.PNG">
              <img v-if="data.admin.img" :src="data.admin.img" class="avatar">
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="账号" prop="username">
            <el-input v-model="data.admin.username" disabled placeholder="请输入账号" autocomplete="off" />
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="data.admin.name"  placeholder="请输入姓名" autocomplete="off" />
          </el-form-item>
          <el-form-item label="性别" >
            <el-radio-group v-model="data.admin.sex">
              <el-radio value="男">男</el-radio>
              <el-radio value="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input type="number" min="1" v-model="data.admin.age" placeholder="请输入年龄" autocomplete="off" />
          </el-form-item>
          <el-form-item label="联系方式" prop="tel">
            <el-input v-model="data.admin.tel" placeholder="请输入联系方式" autocomplete="off" />
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

// 文件上传的接口地址
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
//提交时表单校验
const formRef = ref()
const data = reactive({
  admin: JSON.parse(localStorage.getItem('admin') || '{}'),
  imageUrl:'',
  rules:{
    name: [{ required: true, message: '姓名不能为空', trigger: 'blur' },],
    age: [{ required: true, message: '年龄不能为空', trigger: 'blur' },],
    tel: [{ required: true, message: '联系方式不能为空', trigger: 'blur' },]
  }
})


const emit = defineEmits(["updateUser"])

// 修改个人信息
const save = () => {
  formRef.value.validate((valid=>{
    if(valid){
      if(!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(data.admin.tel)){ElMessage.error("电话号码格式有误");return ;}
  request.post('adminAdd', qs.stringify({
    id:data.admin.id,
    img:data.admin.img,
    name:data.admin.name,
    sex:data.admin.sex,
    age:data.admin.age,
    tel:data.admin.tel,
  })).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      localStorage.setItem('admin', JSON.stringify(data.admin))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
    }
  }))
}

// 处理文件上传的钩子
const handleAvatarSuccess = (res) => {
  data.admin.img = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}

/*onMounted(() => {
  load(data.pageNum)
})*/

</script>
<style scoped>
.avatar-uploader .avatar {
  width: 100px !important;
  height: 100px !important;;
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
  width: 100px;
  height: 100px;
  text-align: center;
}
</style>
