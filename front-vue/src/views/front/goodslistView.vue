<template>
    <div>
      <div style="width: 95%;margin: 20px auto">
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入名称"/>
        <el-button type="warning" plain @click="load(data.key)">搜索</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                   min-height="250" style="width: 100%">
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="cfname" label="营养成分" />
          <el-table-column prop="yynum" label="营养含量(μg/100g)" />
          <el-table-column prop="rlnum" label="热量(Ka/100g)" />
          <el-table-column prop="content" show-overflow-tooltip label="备注信息" />
          <el-table-column  label="操作" width="150">
            <template #default="scope">
              <el-button type="warning" link plain size="small" @click="Addcart(scope.row.id)">加入清单</el-button>
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

    </div>
</template>

<script setup>
import {createApp, onMounted, reactive, ref} from "vue";
import router from "@/router/index.js";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import E from 'wangeditor'

//提交是表单校验
const formRef = ref()
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
  slnum:1
})

//清单新增
const Addcart = (goodsid) =>{
  if (JSON.stringify(data.member)==='{}'){ElMessage.error('登录后添加');return false;}
  request.post('cartAdd', qs.stringify({
    memberid:data.member.id,
    goodsid:goodsid,
    slnum:data.slnum,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success('操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

//信息列表
const load =()=>{
  request.post('goodsList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

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
