<template>
    <div>
      <div class="card" style="margin-bottom: 10px;">
        <el-breadcrumb :separator-icon="ArrowRight" style="margin: 5px;">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{router.currentRoute.value.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="card" >
        <el-button type="success" @click="Add">新增</el-button>
      </div>
      <div class="card" style="margin-top: 5px" >
        <el-table :data="data.tableData"
                  border
                  :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                  size="large"
                  min-height="250" style="width: 100%">
          <el-table-column label="图片">
            <template #default="scope">
              <el-image :src="scope.row.fileimg" style="width: 200px; height: 110px;"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="URL" prop="url"></el-table-column>
          <el-table-column prop="address" label="操作" >
            <template #default="scope">
              <el-button link type="warning" size="small" plain @click="Edit(scope.row)">编辑</el-button>
              <el-button link type="danger"  size="small" plain @click="Delete(scope.row.id)">删除</el-button>
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
        <el-form :model="data.form" label-width="100px" style="padding-right: 50px;margin-top: 20px;">
          <el-form-item label="图片" prop="avatar">
            <el-upload class="upload-demo"
                :action="uploadimgUrl"
                :limit="1"
                list-type="picture"
                :file-list="data.fileList"
                accept=".jpg,.png,.jpeg,.JPG,.PNG"
                :on-success="handleImgSuccess"
                >
              <el-button type="primary">上传图片</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="URL" prop="URL">
            <el-input v-model="data.form.url" placeholder="请输入跳转链接" autocomplete="off" />
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
import {createApp, nextTick, reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import E from 'wangeditor'
import {sendMsg} from "@/utils/crossTagMsg.js";
import {ArrowRight} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
//创建路由
const router = useRouter()
// 文件上传的接口地址
const uploadimgUrl = import.meta.env.VITE_BASE_URL + '/fles/upload'
const data = reactive({
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 3,
  total: 0,
  formVisible: false,
  form: {},
  tableData:[],
  title:'',
  uploadUrl:'',
  fileList:[],
  ids:[]
})

//信息列表
const load =()=>{
  request.post('imgadvList', qs.stringify({
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
    sendMsg('轮播图更新',1)
  })
}


// 信息新增
const Add = () => {
  data.form = {}
  data.formVisible = true
  data.uploadUrl=''
  data.fileList = []
  data.title='轮播图信息新增'
}

// 信息编辑
const Edit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.uploadUrl=data.form.fileimg
  data.fileList = [{name: data.form.fileimg.split('fles/').pop(), url: data.form.fileimg}];
  data.title='轮播图信息编辑'
}

// 新增或编辑提交
const save = () => {
  request.post('imgadvAdd', qs.stringify({
    id:data.form.id,
    fileimg:data.form.fileimg,
    url:data.form.url,
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

// 删除
const Delete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
    request.post('imgadvDel?id=' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 处理文件上传的钩子
const handleImgSuccess = (res) => {
  data.form.fileimg = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}
load()

/*onMounted(() => {
  load(data.pageNum)
})*/

</script>
<style scoped>

</style>
