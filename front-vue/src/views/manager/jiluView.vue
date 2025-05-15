<template>
    <div>
      <div class="card" style="margin-bottom: 10px;">
        <el-breadcrumb :separator-icon="ArrowRight" style="margin: 5px;">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="card" >
        <el-date-picker  style="width: 230px;margin-right: 10px"
                         v-model="data.key"
                         type="date"
                         value-format="YYYY-MM-DD"
                         placeholder="选择日期"/>
        <el-button type="info"  @click="load(data.key)">搜索</el-button>
        <el-button type="danger"  @click="delAll">批量删除</el-button>
        <el-button type="success" plain @click="exportBxs(data.key)">导出Excel</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  :row-key="getRowKeys" @selection-change="handleSelectionChange"
                  min-height="250" style="width: 100%">
          <el-table-column fixed type="selection" :reserve-selection="true" width="55" />
          <el-table-column prop="membername" label="用户信息" show-overflow-tooltip width="200"/>
          <el-table-column prop="goodsname" label="食物信息" show-overflow-tooltip width="300">
            <template #default="scope">
              <div v-html="scope.row.goodsname" />
            </template>
          </el-table-column>
          <el-table-column prop="rqdate" label="饮食日期" width="150"/>
          <el-table-column prop="yybtotal" label="营养量(Ug)" width="200"/>
          <el-table-column prop="subtotal" label="热量(Ka)" width="200"/>
          <el-table-column prop="content" show-overflow-tooltip label="改善建议" width="200" />
          <el-table-column prop="savetime" label="创建日期" width="200" />
          
          <el-table-column fixed="right"  label="操作" >
            <template #default="scope">
              <el-button link type="danger" plain  size="small" @click="Delete(scope.row.id)">删除</el-button>
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
const router = useRouter()
const uploadimgUrldc = import.meta.env.VITE_BASE_URL
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
  ids:[],
})

//信息列表
const load =()=>{
  request.post('jiluList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
    sendMsg('jilu更新',1)
  })
}


// 删除
const Delete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
    request.post('jiluDel?id=' + id).then(res => {
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

      request.post('jiluDeleteAll',qs.stringify({
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
//信息导出
const exportBxs=()=>{
  location.replace(uploadimgUrldc+'/jiluDC?key='+data.key)
}
load()
</script>
<style scoped>

</style>
