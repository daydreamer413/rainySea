<template>
    <div>
      <div style="width: 70%;margin: 20px auto">
      <div class="card" >
        <el-input v-model="data.key" @input="load(data.key)" clearable style="width: 200px;margin-right: 10px" placeholder="请输入菜品名称"/>
        <el-button type="warning" plain @click="load(data.key)">搜索</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  min-height="250" style="width: 100%">
          <el-table-column label="图片">
            <template #default="scope">
              <el-image @click="route.push('shujuDateil?id='+scope.row.shujuid)"
                  style="width: 80px; height: 80px"
                  :src="scope.row.shuju.fileimg"
                  :preview-teleported="true">
              </el-image>
            </template>
          </el-table-column>
          <el-table-column prop="shujuname" label="名称"/>
          <el-table-column prop="savetime" label="收藏日期" />
          <el-table-column label="操作" >
            <template #default="scope">
              <el-button link type="danger" plain size="small" @click="Delete(scope.row.id)">删除</el-button>
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
              next-text="下一页"
          />
        </div>
      </div>
      </div>
    </div>
</template>

<script setup>
import {createApp, onMounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import {useRouter} from "vue-router";

//创建路由对象
const route = useRouter()
const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}'),
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData:[],
})

//信息列表
const load =()=>{
  request.post('favList', qs.stringify({
    memberid:data.member.id,
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
    request.post('favDel?id=' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}
load()

</script>
<style scoped>
</style>
