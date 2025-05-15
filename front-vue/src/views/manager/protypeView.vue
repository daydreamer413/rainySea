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
        <el-button type="warning" plain @click="load(data.key)">搜索</el-button>
        <el-button type="success" @click="Add">新增</el-button>
        <el-button type="danger" plain @click="delAll">批量删除</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys"
                  @selection-change="handleSelectionChange"
                  min-height="250" style="width: 100%">
          <el-table-column type="selection" :reserve-selection="true" width="55" />
          <el-table-column prop="name" label="分类名称" />
          <el-table-column label="操作" >
            <template #default="scope">
              <el-button type="warning" link plain size="small" @click="Edit(scope.row)">编辑</el-button>
              <el-button type="danger" link plain  size="small" @click="Delete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="card" v-if="data.total>0" style="display: flex;justify-content: center">
          <el-pagination
              @current-change="load()"
              background
              size="small"
              layout="prev, pager, next"
              v-model:page-size="data.pageSize"
              v-model:current-page="data.pageNum"
              :total="data.total"
              prev-text="上一页"
              next-text="下一页"/>
        </div>
      </div>
      <el-dialog :title="data.title" width="60%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="100px" style="padding-right: 50px;margin-top: 20px;">
          <el-form-item label="分类名称" prop="name">
            <el-input v-model="data.form.name" placeholder="请输入分类名称" autocomplete="off" />
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
import {ArrowRight} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import {sendMsg} from "@/utils/crossTagMsg.js";
//提交时表单校验
const formRef = ref()
const router = useRouter()
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
  ids:[],
  rules:{
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' },],
  }
})

//信息列表
const load =()=>{
  request.post('protypeList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
    sendMsg('protype更新',1)
  })
}


// 信息新增
const Add = () => {
  data.form = {}
  data.formVisible = true
  data.title='口味分类信息新增'
}

// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.title='口味分类信息编辑'
}

// 新增或编辑提交
const save = () => {
  formRef.value.validate((valid=>{
    if (valid){
      request.post('protypeAdd', qs.stringify({
        id:data.form.id,
        name:data.form.name,
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
    request.post('protypeDel?id=' + id).then(res => {
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
  }else{
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {

      request.post('protypeDeleteAll',qs.stringify({
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
load()
</script>
<style scoped>

</style>
