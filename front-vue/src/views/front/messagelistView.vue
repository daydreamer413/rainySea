<template>
  <div>
    <div  style="width: 60%;min-height: 150px;margin: 30px auto;">
      <div style="margin-top: 30px;margin-bottom: 30px;">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="text-align: center;">
            <h2>留言建议</h2>
          </div>
          <div v-for="item in data.messagelist"  >
            <div style="display: flex;margin-top: 15px;min-height: 100px;">
              <div>
                <img :src="item.member.img" style="width: 70px;height: 70px;border-radius: 100%;">
              </div>
              <div style="padding-left: 15px;line-height: 2;width: 100%">
                <div >
                  <strong >{{item.username}}</strong>
                  <span style="padding-left: 20px;color: #888888FF"> <el-icon><Calendar /></el-icon> {{item.savetime}}</span>
                  <el-link style="margin-left: 20px;color: red" @click="del(item.id)" :underline="false"> <el-icon><Delete /></el-icon> 删除</el-link>

                </div>
                <span style="padding-top: 20px;"> {{item.content}}</span><br>

                <span style="padding-top: 20px;" v-if="item.hfcontent!==''"><b>回复内容：</b>【{{item.hftime}}】 {{item.hfcontent}}</span>

                <div style="border-top: 1px dashed rgba(140,147,157,0.84);margin-top: 20px;"></div>
              </div>
            </div>
          </div>

          <div class="block" v-if="data.total>0" style="margin-top: 40px;display: flex;justify-content: center">
            <el-pagination
                @current-change="getmessagelist()"
                :current-page="data.pageNum"
                :page-size="data.pageSize"
                background
                layout="prev, pager, next"
                :total="data.total">
            </el-pagination>
          </div>

          <div style="margin-top: 150px">
            <el-input type="textarea" v-model="data.content" style="width: 100%;height: 70px;" placeholder="输入留言内容"></el-input>
            <el-button type="warning" @click="submit()">提交</el-button>
          </div>

        </el-card>

      </div>



    </div>
  </div>
  
</template>

<script setup>
import {createApp, onMounted, onUnmounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import {useRouter} from "vue-router";
import {listenMsg} from "@/utils/crossTagMsg.js";
//创建路由对象
const router = useRouter()
const data = reactive({
  member:JSON.parse(localStorage.getItem('member') || '{}'),
  key:'',
  pageNum:1,
  pageSize:10,
  total:0,
  messagelist:[],
  content:'',
  sessionmember:{}
})
onMounted(() => {
 data.sessionmember = JSON.stringify(data.member)
  if(data.sessionmember==='{}'){
    ElMessage.warning("登录后留言")
    router.push('/front/memberLogin')
  }
})

const cancelListen = listenMsg(msginfo=>{
  if (msginfo.type==='留言'){
    getmessagelist()
  }
})
onUnmounted(cancelListen)

//意见箱信息列表
const getmessagelist =()=>{
  request.post('messageList', qs.stringify({
    memberid:data.member.id,
    pageNum:data.pageNum,
    pageSize:data.pageSize,
  })).then(res => {
    data.messagelist = res.data.list
    data.total = res.data.total
  })
}
//意见箱信息新增
const submit =()=>{
  if(data.sessionmember==='{}'){ElMessage.error("登录后留言");return }
  if(data.content===''){ElMessage.error("请输入内容");return;}
  request.post('messageAdd', qs.stringify({
    memberid:data.member.id,
    content:data.content,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success("操作成功")
      getmessagelist()
      data.content=''
    }else{
      ElMessage.error(res.msg)
    }
  })
}
//删除留言信息
const del = (id)=>{
  request.post('messageDel', qs.stringify({
    id:id,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success("操作成功")
      getmessagelist()
    }else{
      ElMessage.error(res.msg)
    }
  })
}
  getmessagelist()
</script>
<style scoped>
</style>
