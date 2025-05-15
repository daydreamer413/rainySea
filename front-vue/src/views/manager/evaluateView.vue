<template>
    <div>
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入名称"/>
        <el-button type="info" plain @click="load(data.key)">搜索</el-button>
        <el-button type="danger" plain @click="delAll">批量删除</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys" @selection-change="handleSelectionChange" min-height="250" style="width: 100%">
          <el-table-column fixed type="selection" :reserve-selection="true" width="55" />
          <el-table-column prop="evaluateid" label="父级ID" width="100"/>
          <el-table-column label="用户" width="100">
            <template #default="scope">{{scope.row.member.name}}</template>
          </el-table-column>
          <el-table-column label="评价菜品" width="300">
            <template #default="scope">{{scope.row.shuju.name}}</template>
          </el-table-column>
          <el-table-column label="评分" width="100">
            <template #default="scope">
           {{scope.row.pfnum}} 分
            </template>
          </el-table-column>
          <el-table-column prop="content" show-overflow-tooltip label="评价内容" width="400"/>
          <el-table-column prop="savetime" label="评价日期" width="200"/>
          <el-table-column fixed="right" label="操作" width="100">
            <template #default="scope">
              <el-button type="danger" link plain size="small" @click="Delete(scope.row.id)">删除</el-button>
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
              next-text="上一页"
          />
        </div>
      </div>

    </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from '@/utils/request.js'
import qs from "qs";
import {ElMessage, ElMessageBox} from "element-plus";
//提交时表单校验
const formRef = ref()

const data = reactive({
  key:'',
  key1:'',
  admin: JSON.parse(localStorage.getItem('admin') || '{}'),
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  tableData:[],
  title:'',
  ids:[],
  rules:{
    hfcontent: [{ required: true, message: '回复内容不能为空', trigger: 'blur' },],
  }
})

//信息列表
const load =()=>{
  request.post('evaluateList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

// 信息回复
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.title='评价回复'
}

// 评价回复提交
const save = () => {
  formRef.value.validate((valid=>{
    if (valid){
      request.post('evaluateEdit', qs.stringify({
        id:data.form.id,
        hfcontent:data.form.hfcontent,
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
    request.post('evaluateDel?id=' + id).then(res => {
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
  }else{
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
      request.post('evaluateDeleteAll',qs.stringify({
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
