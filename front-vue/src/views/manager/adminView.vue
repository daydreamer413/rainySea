<template>
    <div>
      <div class="card" style="margin-bottom: 10px;">
        <el-breadcrumb :separator-icon="ArrowRight" style="margin: 5px;">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入姓名"/>
        <el-button type="info" plain @click="load(data.key)">搜索</el-button>
        <el-button type="success" @click="Add">新增</el-button>
        <el-button type="danger" plain @click="delAll">批量删除</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys" @selection-change="handleSelectionChange"
                  min-height="250" style="width: 100%">
          <el-table-column type="selection" :reserve-selection="true" width="55" />
          <el-table-column label="头像">
            <template #default="scope">
              <el-image
                  style="width: 50px; height: 50px"
                  :src="scope.row.img"
                  :preview-src-list="[scope.row.img]"
                  :preview-teleported="true">
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="账号" />
          <el-table-column prop="password" label="密码" />
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="sex" label="性别" />
          <el-table-column prop="age" label="年龄" />
          <el-table-column prop="tel" label="联系方式" />
          <el-table-column label="操作" >
            <template #default="scope">
              <el-button type="warning" link plain size="small" @click="Edit(scope.row)">编辑</el-button>
              <el-button type="danger" link plain size="small" @click="Delete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="data.total>0" style="display: flex;justify-content: center;margin: 15px;">
          <el-pagination
              @current-change="load()"
              background
              size="small"
              layout="prev, pager, next"
              v-model:page-size="data.pageSize"
              v-model:current-page="data.pageNum"
              :total="data.total"
              prev-text="上一页"
              next-text="上一页"
          />
        </div>
      </div>
      <el-dialog :title="data.title" width="60%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" label-width="100px" ref="formRef" :rules="data.rules" style="padding-right: 50px;margin-top: 20px;">
          <el-form-item label="头像" prop="avatar">
            <el-upload
                class="avatar-uploader"
                :action="uploadimgUrl"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                accept=".jpg,.png,.jpeg,.JPG,.PNG">
              <img  v-if="data.imageUrl" :src="data.imageUrl" class="avatar">
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="账号" prop="username">
            <el-input v-model="data.form.username" :disabled="data.form.id!=null" placeholder="请输入账号" autocomplete="off" />
          </el-form-item>
          <el-form-item v-show="data.title==='管理员信息新增'" label="密码" prop="password">
            <el-input v-model="data.form.password" show-password placeholder="请输入密码" autocomplete="off" />
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="data.form.name"  placeholder="请输入姓名" autocomplete="off" />
          </el-form-item>
          <el-form-item label="性别" >
            <el-radio-group v-model="data.form.sex">
              <el-radio value="男">男</el-radio>
              <el-radio value="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input type="number" min="1" v-model="data.form.age" placeholder="请输入年龄" autocomplete="off" />
          </el-form-item>
          <el-form-item label="联系方式" prop="tel">
            <el-input v-model="data.form.tel" placeholder="请输入联系方式" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
        </template>
      </el-dialog>

    </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import E from 'wangeditor'
import {ArrowRight} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
//创建路由
const router = useRouter()
// 文件上传的接口地址
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
//提交时表单校验
const formRef = ref()
const data = reactive({
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  tableData:[],
  title:'',
  imageUrl:'',
  fileList:[],
  ids:[],
  rules:{
    username: [{ required: true, message: '账号不能为空', trigger: 'blur' },],
    password: [{ required: true, message: '密码不能为空', trigger: 'blur' },],
    name: [{ required: true, message: '姓名不能为空', trigger: 'blur' },],
    age: [{ required: true, message: '年龄不能为空', trigger: 'blur' },],
    tel: [{ required: true, message: '联系方式不能为空', trigger: 'blur' },]
  }
})

//信息列表
const load =()=>{
  request.post('adminList', qs.stringify({
    key:data.key,
    role:'管理员',
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}


// 信息新增
const Add = () => {
  data.form = {
    sex:'男'
  }
  data.formVisible = true
  data.imageUrl=''
  data.title='管理员信息新增'
}

// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.imageUrl=data.form.img
  data.title='管理员信息编辑'
}

// 新增或编辑提交
const save = () => {
  formRef.value.validate((valid=>{
    if(valid){
      if(!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(data.form.tel)){ElMessage.error("电话号码格式有误");return ;}
  request.post('adminAdd', qs.stringify({
    id:data.form.id,
    username:data.form.username,
    password:data.form.password,
    img:data.imageUrl,
    name:data.form.name,
    sex:data.form.sex,
    age:data.form.age,
    tel:data.form.tel,
    categoryid:10,
    typeid:1,
    role:'管理员',
  })).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
    }
  }))
}

// 删除
const Delete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
    request.post('adminDel?id=' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

//解决跨分页选中后分页取消的情况
const getRowKeys = (row)=>{
  return row.id;
}

//选中需要删除的对象
const handleSelectionChange = (rows)=>{
  data.ids = rows.map(row=>row.id).join()
}
//批量删除
const delAll=()=>{
  if(data.ids.length === 0){
    ElMessage.warning({message:'请选择需要删除的项',plain:true,grouping: true})
    return
  }else {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
      request.post('adminDeleteAll',qs.stringify({
        vals:data.ids
      })).then(res => {
        if (res.code === '200') {
          load()
          ElMessage.success('操作成功')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }).catch(err => {})
  }

}

// 处理文件上传的钩子
const handleAvatarSuccess = (res) => {
  data.imageUrl = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}
load()

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
