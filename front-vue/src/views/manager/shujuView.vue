<template>
    <div>
      <div class="card" style="margin-bottom: 10px;">
        <el-breadcrumb :separator-icon="ArrowRight" style="margin: 5px;">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入名称"/>
        <el-button type="info"  @click="load(data.key)">搜索</el-button>
        <el-button type="danger"  @click="delAll">批量删除</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys" @selection-change="handleSelectionChange"
                  min-height="250" style="width: 100%">
          <el-table-column fixed type="selection" :reserve-selection="true" width="55" />
          <el-table-column label="图片" width="100">
            <template #default="scope">
              <el-image
                  style="width: 50px; height: 50px"
                  :src="scope.row.fileimg"
                  :preview-src-list="[scope.row.fileimg]"
                  :preview-teleported="true">
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="membername" label="发布用户" show-overflow-tooltip width="200"/>
          <el-table-column prop="name" label="名称" show-overflow-tooltip width="200"/>
          <el-table-column prop="protypename" label="口味分类" width="100"/>
          <el-table-column prop="categoryname" label="分类" width="100"/>
          <el-table-column prop="jjcontent" show-overflow-tooltip label="简要介绍" width="300"/>
          <el-table-column prop="llnum" label="浏览量" width="100"/>
          <el-table-column prop="pfnum" label="评分" width="100"/>
          <el-table-column prop="savetime" label="发布日期" width="200" />
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button link type="warning" plain size="small" @click="Edit(scope.row)">详情</el-button>
              <el-button link type="danger" plain size="small" @click="Delete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="card" v-if="data.total>0" style="display: flex;justify-content: center">
          <el-pagination
              @current-change="load()"
              background
              layout="prev, pager, next"
              v-model:page-size="data.pageSize"
              v-model:current-page="data.pageNum"
              :total="data.total"
              prev-text="上一页"
              next-text="下一页"
              size="small"/>
        </div>
      </div>

      <el-dialog :title="data.title" width="65%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="100px" style="padding-right: 50px;margin-top: 20px">
          <el-form-item label="图片" prop="avatar">
            <el-upload
                class="avatar-uploader"
                :action="uploadimgUrl"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                accept=".jpg,.png,.jpeg,.JPG,.PNG">
              <el-image  v-if="data.imageUrl" :src="data.imageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="data.form.name" placeholder="请输入名称" autocomplete="off" />
          </el-form-item>
          <el-form-item label="分类" prop="categoryid">
            <el-select  style="width: 100%" v-model="data.form.categoryid">
              <el-option value="" label="请选择分类"></el-option>
              <el-option v-for="category in data.categoryList" :key="category.id" :value="category.id" :label="category.name"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="简要介绍" prop="jjcontent">
            <el-input type="textarea" :rows="3" v-model="data.form.jjcontent" placeholder="请输入简要介绍" autocomplete="off" />
          </el-form-item>
          <el-form-item label="详情介绍" prop="content">
            <div id="editor"></div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">关 闭</el-button>
<!--        <el-button type="primary" @click="save">保 存</el-button>-->
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
import {sendMsg} from "@/utils/crossTagMsg.js";
import {ArrowRight, Plus} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
//富文本编辑器
let editor;
function initwangeditor(content) { setTimeout(()=> {
  if(!editor){
    //初始化富文编辑器
    editor = new E("#editor")
    editor.config.placeholder="请输入内容"
    //上传图片
    editor.config.uploadFileName='file'
    editor.config.uploadImgServer=  import.meta.env.VITE_BASE_URL + '/fles/wang/upload?type=img'
    //上传视频
    editor.config.uploadVideoName = 'file'
    editor.config.uploadVideoServer= import.meta.env.VITE_BASE_URL + '/fles/wang/upload?type=video'
    editor.create()
  }
  editor.txt.html(content)
},0)

}
//创建路由
const router = useRouter()

// 文件上传的接口地址
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
const formRef = ref()
const data = reactive({
  key:'',
  pageNum: 1,
  pageSize: 6,
  total: 0,
  tableData:[],
  contVisible:false,
  formVisible:false,
  fileList:[],
  imageUrl:'',
  categoryList:[],
  form:{},
  ids:[],
  admin: JSON.parse(localStorage.getItem('admin') || '{}'),
  rules:{
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' },],
    categoryid: [{ required: true, message: '分类不能为空', trigger: 'blur' },],
    jjcontent: [{ required: true, message: '简要介绍不能为空', trigger: 'blur' },],
    content: [{ required: true, message: '详情介绍不能为空', trigger: 'blur' },]
  }
})


//信息列表
const load =()=>{
  request.post('shujuList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
    sendMsg('shuju更新',1)
  })
}
// 信息新增
const Add = () => {
  data.form = {
    }
  data.formVisible = true
  data.imageUrl=''
  editor=''
  initwangeditor("")
  data.title='菜品信息新增'
}
// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.imageUrl=data.form.fileimg
  editor=''
  initwangeditor(row.content?data.form.content:'')
  data.title='菜品信息查看'
}

// 新增或编辑提交
const save = () => {
  data.form.content = editor.txt.html()
  formRef.value.validate((valid=>{
    if(valid){
      request.post('shujuAdd', qs.stringify({
        id:data.form.id,
        fileimg:data.imageUrl,
        name:data.form.name,
        categoryid:data.form.categoryid,
        jjcontent:data.form.jjcontent,
        content:data.form.content,
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
    request.post('shujuDel?id=' + id).then(res => {
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
    return false;
  }else {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
      request.post('shujuDeleteAll',qs.stringify({
        vals:data.ids
      })).then(res => {
        if (res.code === '200') {
          load()
          loadDD()
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
//查询分类列表
const catelist=()=>{
  request.post('cateListAll', qs.stringify({
  })).then(res => {
    data.categoryList = res.data
  })
}
catelist()
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
