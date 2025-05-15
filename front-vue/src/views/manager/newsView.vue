<template>
    <div>
      <div class="card" style="margin-bottom: 10px;">
        <el-breadcrumb :separator-icon="ArrowRight" style="margin: 5px;">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入标题"/>
        <el-button type="info"  @click="load(data.key)">搜索</el-button>
        <el-button type="success" @click="Add">新增</el-button>
        <el-button type="danger"  @click="delAll">批量删除</el-button>
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
          <el-table-column prop="title" show-overflow-tooltip label="标题" />
          <el-table-column prop="savetime" label="发布日期" width="200"/>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <!-- <svg-icon icon-name="icon-dianzan" style="color: #606266"/>-->
              <el-button link type="warning" plain size="small" @click="Edit(scope.row)">编辑</el-button>
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
              size="small"
          />
        </div>
      </div>
      <el-dialog :title="data.title" width="60%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="100px" style="padding-right: 50px">
          <el-form-item label="图片" prop="avatar">
            <el-upload class="upload-demo"
                       :action="uploadimgUrl"
                       :limit="1"
                       list-type="picture"
                       :file-list="data.fileList"
                       accept=".jpg,.png,.jpeg,.JPG,.PNG"
                       :on-success="handleImgSuccess">
              <el-button type="primary">上传图片</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input v-model="data.form.title" placeholder="请输入标题" autocomplete="off" />
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <div id="editor"></div>
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
import {createApp, onMounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import E from 'wangeditor'
import {sendMsg} from "@/utils/crossTagMsg.js";
import SvgIcon from "@/components/SvgIcon.vue";
import {ArrowRight} from "@element-plus/icons-vue";
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
// 文件上传的接口地址
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
const router = useRouter()
const formRef = ref()
const data = reactive({
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 6,
  total: 0,
  formVisible: false,
  form: {},
  tableData:[],
  fileList:[],
  title:'',
  ids:[],
  rules:{
    title: [{ required: true, message: '标题不能为空', trigger: 'blur' },],
    content: [{ required: true, message: '内容不能为空', trigger: 'blur' },]
  }

})

//信息列表
const load =()=>{
  request.post('newsList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
    sendMsg('新闻更新',1)
  })
}


// 信息新增
const Add = () => {
  data.form = {}
  data.formVisible = true
  data.fileList = []
  editor=''
  initwangeditor("")
  data.title='健康资讯信息新增'
}

// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.fileList = [{name: data.form.fileimg.split('fles/').pop(), url: data.form.fileimg}];
  editor=''
  initwangeditor(row.content?data.form.content:'')
  data.title='健康资讯信息编辑'
}

// 新增或编辑提交
const save = () => {
  data.form.content = editor.txt.html()
  formRef.value.validate((valid=>{
    if(valid){
  request.post('newsAdd', qs.stringify({
    id:data.form.id,
    title:data.form.title,
    fileimg:data.form.fileimg,
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
    request.post('newsDel?id=' + id).then(res => {
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
  }
  request.post('newsDeleteAll',qs.stringify({
    vals:data.ids
  })).then(res => {
    if (res.code === '200') {
      load()

      ElMessage.success('操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
// 处理文件上传的钩子
const handleImgSuccess = (res) => {
  data.form.fileimg = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}
load()



</script>
<style scoped>
</style>
