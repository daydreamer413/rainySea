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
        <el-button type="success" @click="Add">新增</el-button>
        <el-button type="danger"  @click="delAll">批量删除</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys" @selection-change="handleSelectionChange" min-height="250" style="width: 100%">
          <el-table-column  type="selection" :reserve-selection="true" width="55" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="cfname" label="营养成分" />
          <el-table-column prop="yynum" label="营养含量(μg/100g)" />
          <el-table-column prop="rlnum" label="热量(Ka/100g)" />
          <el-table-column prop="content" show-overflow-tooltip label="备注信息" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
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
      <el-dialog :title="data.title" width="70%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="200px" style="padding-right: 50px">
          <el-form-item label="食物名称" prop="name">
            <el-input v-model="data.form.name" placeholder="请输入食物名称" autocomplete="off" />
          </el-form-item>       
          <el-form-item label="营养成分" prop="cfname">
            <el-input v-model="data.form.cfname" placeholder="请输入营养成分" autocomplete="off" />
          </el-form-item>
          <el-form-item label="营养含量(Ug/100g)" prop="yynum">
            <el-input v-model="data.form.yynum" placeholder="请输入营养含量(Ug/100g)" autocomplete="off" />
          </el-form-item>
          <el-form-item label="热量(Ka/100g)" prop="rlnum">
            <el-input type="number" :min1 v-model="data.form.rlnum" placeholder="请输入热量(Ka/100g)" autocomplete="off" />
          </el-form-item>
          <el-form-item label="备注信息" prop="content">
            <el-input type="textarea" :rows="4" v-model="data.form.content" placeholder="请输入备注信息" autocomplete="off" />
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
  pageSize: 7,
  total: 0,
  formVisible: false,
  form: {},
  tableData:[],
  fileList:[],
  title:'',
  ids:[],
  rules:{
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' },],
    cfname: [{ required: true, message: '成分不能为空', trigger: 'blur' },],
    rlnum: [{ required: true, message: '热量不能为空', trigger: 'blur' },],
    yynum: [{ required: true, message: '营养含量不能为空', trigger: 'blur' },],
    content: [{ required: true, message: '备注信息不能为空', trigger: 'blur' },]
  }

})

//信息列表
const load =()=>{
  request.post('goodsList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
    sendMsg('goods更新',1)
  })
}


// 信息新增
const Add = () => {
  data.form = {}
  data.formVisible = true
  initwangeditor("")
  data.title='食物信息新增'
}

// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.title='食物信息编辑'
}

// 新增或编辑提交
const save = () => {
  formRef.value.validate((valid=>{
    if(valid){
  request.post('goodsAdd', qs.stringify({
    id:data.form.id,
    name:data.form.name,
    cfname:data.form.cfname,
    rlnum:data.form.rlnum,
    yynum:data.form.yynum,
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
    request.post('goodsDel?id=' + id).then(res => {
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
  request.post('goodsDeleteAll',qs.stringify({
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
