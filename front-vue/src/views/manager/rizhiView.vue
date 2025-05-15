<template>
    <div>
      <div class="card" style="margin-bottom: 10px;">
        <el-breadcrumb :separator-icon="ArrowRight" style="margin: 5px;">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入用户名"/>
        <el-button type="info" plain @click="load(data.key)">搜索</el-button>
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
          <el-table-column prop="username" label="账号" />
          <el-table-column prop="role" label="角色" />
          <el-table-column prop="typename" label="类型" />
          <el-table-column prop="savetime" label="日期" />
          <el-table-column label="操作" >
            <template #default="scope">
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
const router = useRouter()
const data = reactive({
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  tableData:[],
  ids:[],
})

//信息列表
const load =()=>{
  request.post('rizhiList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

// 删除
const Delete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
    request.post('rizhiDel?id=' + id).then(res => {
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
      request.post('rizhiDeleteAll',qs.stringify({
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

/*onMounted(() => {
  load(data.pageNum)
})*/

</script>
