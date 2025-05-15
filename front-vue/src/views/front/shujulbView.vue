<template>
    <div>
      <div style="width: 95%;margin: 20px auto">
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入标题"/>
        <el-button type="warning" plain @click="load(data.key)">搜索</el-button>
        <el-button type="success" @click="Add">新增</el-button>
        <el-button type="danger" plain @click="delAll">批量删除</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys" @selection-change="handleSelectionChange" min-height="250" style="width: 100%">
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
          <el-table-column prop="jktype" label="适合身体状况" width="150"/>
          <el-table-column prop="mbtype" label="适合人群" width="100"/>
          <el-table-column prop="llnum" label="浏览量" width="100"/>
          <el-table-column prop="pfnum" label="评分" width="100"/>
          <el-table-column prop="savetime" label="发布日期" width="200" />
          <el-table-column fixed="right" label="操作" width="150">
            <template #default="scope">
              <el-button type="warning" link plain size="small" @click="Edit(scope.row)">编辑</el-button>
              <el-button type="danger" link plain size="small" @click="Delete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div  v-if="data.total>0" style="display: flex;justify-content: center;margin: 20px auto">
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
      </div>
      <el-drawer v-model="data.formVisible" :before-close="handleClose"  size="60%" :with-header="false">
        <h3 style="text-align: center">{{data.title}}</h3>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="120px" style="padding: 20px">
          <el-form-item label="图片" prop="avatar">
            <el-upload
                class="avatar-uploader"
                :action="uploadimgUrl"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                accept=".jpg,.png,.jpeg,.JPG,.PNG">
              <img  v-if="data.form.fileimg" :src="data.form.fileimg" class="avatar">
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="data.form.name" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item label="口味分类" prop="protypeid">
            <el-select v-model="data.form.protypeid" placeholder="请选择分类">
              <el-option v-for="protype in data.protypelist" :key="protype.id" :label="protype.name" :value="protype.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="分类" prop="categoryid">
            <el-select v-model="data.form.categoryid" placeholder="请选择分类">
              <el-option v-for="category in data.categorylist" :key="category.id" :label="category.name" :value="category.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="健康状况" >
            <el-radio-group v-model="data.form.jktype">
              <el-radio value="健康">健康</el-radio>
              <el-radio value="亚健康">亚健康</el-radio>
              <el-radio value="疾病">疾病</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="饮食目标" >
            <el-radio-group v-model="data.form.mbtype">
              <el-radio value="减肥">减肥</el-radio>
              <el-radio value="增肌">增肌</el-radio>
              <el-radio value="维持体重">维持体重</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="简要介绍" prop="jjcontent">
            <el-input type="textarea" :rows="3" v-model="data.form.jjcontent" placeholder="请输入简要介绍"/>
          </el-form-item>
          <el-form-item label="详情" prop="content">
            <div id="editor" style="width: 100%"></div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
        </template>

      </el-drawer>

    </div>
</template>

<script setup>
import {createApp, onMounted, reactive, ref} from "vue";
import router from "@/router/index.js";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import E from 'wangeditor'

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

//关闭弹框回调函数
const handleClose = () => {
  ElMessageBox.confirm('是否确认需要关闭当前窗口?')
      .then(() => {
        data.formVisible = false
      })
      .catch(() => {
      })
}
//提交是表单校验
const formRef = ref()
// 文件上传的接口地址
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}'),
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  tableData:[],
  title:'',
  ids:[],
  categorylist:[],
  protypelist:[],
  rules:{
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' },],
    protypeid: [{ required: true, message: '口味分类不能为空', trigger: 'blur' },],
    categoryid: [{ required: true, message: '分类不能为空', trigger: 'blur' },],
    jjcontent: [{ required: true, message: '简要介绍不能为空', trigger: 'blur' },],
  }
})
//查询分类列表
const getclist=()=>{
request.post('cateListAll').then(res=>{
  data.categorylist = res.data
})
}
//查询口味分类列表
const getPlist=()=>{
request.post('protypeListAll').then(res=>{
  data.protypelist = res.data
})
}

//信息列表
const load =()=>{
  request.post('shujuList', qs.stringify({
    memberid:data.member.id,
    key:data.key,
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
    mbtype:'维持体重',
    jktype:'健康',
  }
  data.formVisible = true
  data.uploadUrl=''
  initwangeditor("")
  //editor=''
  data.title='菜品信息新增'
}

// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  initwangeditor(row.content?data.form.content:'')
  //editor=''
  data.title='菜品信息编辑'
}

// 新增或编辑提交
const save = () => {
  formRef.value.validate((valid=>{
    if(valid){
  data.form.content = editor.txt.html()
  request.post('shujuAdd', qs.stringify({
    id:data.form.id,
    memberid:data.member.id,
    name:data.form.name,
    fileimg:data.form.fileimg,
    protypeid:data.form.protypeid,
    categoryid:data.form.categoryid,
    mbtype:data.form.mbtype,
    jktype:data.form.jktype,
    jjcontent:data.form.jjcontent,
    content:data.form.content,
  })).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
      //editor=''
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
    return false
  }else {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
      request.post('shujuDeleteAll',qs.stringify({
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
  data.form.fileimg = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}
getclist()
getPlist()
load()

/*onMounted(() => {
  load(data.pageNum)
})*/

</script>
<style scoped>
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
