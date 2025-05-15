<template>
  <div>
    <div style="width: 90%;margin: 10px auto;">
      <div >
        <el-row :gutter="10">
          <el-col :span="18">
            <el-card class="box-card">

              <div style="margin: 15px 10px;">
                <el-breadcrumb :separator-icon="ArrowRight">
                  <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                  <el-breadcrumb-item :to="{ path: 'shujulist' }">菜品信息</el-breadcrumb-item>
                  <el-breadcrumb-item>{{route.currentRoute.value.meta.name}}</el-breadcrumb-item>
                </el-breadcrumb>
              </div>
              <el-divider style="margin: 5px auto"/>

              <div class="grid-content bg-purple" style="min-height: 350px;">
                <el-row :gutter="20">
                  <el-col :span="11">
                    <div class="divimgclass" style="border: 1px solid #10d8d8;border-radius: 10px;padding: 3px;">
                    <el-image class="imgclass" :src="data.shuju.fileimg" style="width: 100%;height: 400px;border-radius: 10px;" />
                  </div> </el-col>
                  <el-col :span="13">
                    <div class="grid-content ep-bg-purple" >
                      <el-descriptions style="margin-top: 20px"
                          :title="data.shuju.name"
                          :column="2"
                          size="large">
                        <el-descriptions-item label="口味分类:"> <el-tag size="small">{{data.shuju.protypename }}</el-tag></el-descriptions-item>
                        <el-descriptions-item label="分 类:"> <el-tag size="small">{{data.shuju.categoryname }}</el-tag></el-descriptions-item>
                        <el-descriptions-item label="适合人群:"> <el-tag size="small">{{data.shuju.mbtype }}</el-tag></el-descriptions-item>
                        <el-descriptions-item label="适合身体状况:"> <el-tag size="small">{{data.shuju.jktype }}</el-tag></el-descriptions-item>
                        <el-descriptions-item label="浏览量:"><el-tag type="primary" size="small">{{data.shuju.llnum}}</el-tag></el-descriptions-item>
                        <el-descriptions-item label="评  分:"><el-tag type="success" size="small" :effect="'dark'">{{data.shuju.pfnum}} 分</el-tag></el-descriptions-item>
                        <el-descriptions-item label="发布用户:">{{data.shuju.membername}}</el-descriptions-item>
                        <el-descriptions-item label="发布日期:">{{data.shuju.savetime}}</el-descriptions-item>
                        <el-descriptions-item label="简要介绍:">{{data.shuju.jjcontent}}</el-descriptions-item>
                      </el-descriptions>

                      <div style="margin: 5px;text-align: right">
                        <template v-if="data.member==='{}'">
                          <el-button type="primary" plain @click="Adddianzan(data.shuju.id)">
                            <svg-icon icon-name="icon-dianzan" />  &nbsp;点赞 （{{data.shuju.dznum}}）</el-button>
                          <el-button type="primary" plain @click="Addfav(data.shuju.id)">
                            <el-icon><Pointer /></el-icon>  &nbsp;收藏 （{{data.shuju.fanum}}）</el-button>
                        </template>
                        <template v-else>
                          <el-button v-if="data.dzlist.length===0" type="primary" plain @click="Adddianzan(data.shuju.id)">
                            <svg-icon icon-name="icon-dianzan" />  &nbsp;点赞 （{{data.shuju.dznum}}）</el-button>
                        <el-button v-else type="danger" plain @click="Adddianzan(data.shuju.id)">
                          <svg-icon icon-name="icon-diancai" />  &nbsp;取消 （{{data.shuju.dznum}}）</el-button>
                          <el-button type="primary" plain @click="Addfav(data.shuju.id)">
                            <el-icon><Pointer /></el-icon>  &nbsp;收藏 （{{data.shuju.fanum}}）</el-button>
                        </template>
                      </div>

                  </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
            <div style="margin-top: 20px;" class="card">
              <el-tabs v-model="data.activeName" type="card">
                <el-tab-pane label="详情介绍" name="first">
                  <div v-html="data.shuju.content" style="line-height: 1.8"></div>
                </el-tab-pane>
                <el-tab-pane label="评论信息" name="second" >

                  <div style="min-height: 150px;margin-top: 20px;">
<!--                    <div slot="header" class="clearfix" style="text-align: center;">
                      <strong>评价信息</strong>
                    </div>-->
                    <div v-for="item in data.evaluatelist"  style="padding: 10px;width: 100%">
                      <div style="display: flex;margin-top: 15px;min-height: 100px;">
                        <div>
                          <el-image :src="item.member.img" style="width: 70px;height: 70px;border-radius: 100%;" />
                        </div>
                        <div style="padding-left: 15px;line-height: 2;width: 100%">
                          <div >
                            <strong >{{item.member.username}}</strong>
                            <span style="padding-left: 20px;color: #888888FF"><el-icon><Calendar /></el-icon> {{item.savetime}}</span>
                            <el-link v-if="JSON.stringify(data.member)!=='{}' && data.member.id===item.memberid" style="margin-left: 20px;color: red" @click="del(item.id)" :underline="false">
                              <el-icon><Delete /></el-icon> 删除</el-link>
                            <el-link v-if="JSON.stringify(data.member)!=='{}'" style="margin-left: 20px;" @click="toggleInput(item.id)" icon="el-icon-edit" :underline="false">
                              <el-icon><EditPen /></el-icon> 回复</el-link>
                          </div>
                          <div>
                            <el-rate style=":root { --el-rate-void-color: red; --el-rate-fill-color: #10d8d8;--el-rate-text-color:#10d8d8; }"
                                v-model="item.pfnum"
                                disabled
                                :max="10"
                                show-score
                                text-color="#10d8d8"
                                score-template="{value} 分"
                            />
                          </div>
                          <span style="padding-right: 20px"  class="line2"> {{item.content}}</span>
                          <div @click="Showcontent(item.content)" v-if="item.content.length>110" style="color: #8c939d;cursor: pointer">
                            查看更多</div>


                          <div style="border-top: 1px dashed rgba(140,147,157,0.84);margin-top: 20px;"></div>
                          <!--  二级回复-->
                          <div v-for="comment in item.hflist"   style="padding-top: 30px">
                            <div style="display: flex;min-height: 80px;">
                              <div>
                                <img :src="comment.member.img" style="width: 70px;height: 70px;border-radius: 100%;">
                              </div>
                              <div style="padding-left: 15px;line-height: 2;width: 100%">
                                <div >
                                  <strong >{{comment.member.username}}</strong>
                                  <span style="padding-left: 20px;color: #888888FF"> <el-icon><Calendar /></el-icon> {{item.savetime}}</span>
                                  <el-link v-if="JSON.stringify(data.member)!=='{}' && data.member.id===comment.memberid" style="margin-left: 20px;color: red" @click="del(comment.id)" icon="el-icon-delete" :underline="false">
                                    删除</el-link>
                                </div>
                                <span style="padding-top: 20px;"> {{comment.content}}</span><br>
                                <div style="border-top: 1px dashed rgba(140,147,157,0.84);margin-top: 20px;"></div>
                              </div>
                            </div>
                          </div>
                          <div v-if="item.id===data.currentId" style="margin-top: 10px">
                            <el-input  placeholder="输入回复内容" v-model="data.hfcontent" style="width: 90%" />
                            <el-button type="warning" @click="commAdd(item.id)">提交</el-button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="block" v-if="data.total>0" style="margin-top: 40px;text-align: center;margin-bottom: 20px;display: flex;justify-content: center">
                    <el-pagination
                        @current-change="getevaluatelist()"
                        :current-page="data.pageNum"
                        :page-size="data.pageSize"
                        background
                        layout="prev, pager, next"
                        :total="data.total"
                        prev-text="上一页"
                        next-text="下一页"
                        size="small">
                    </el-pagination>
                  </div>

                  <div >
                    <el-form-item label="评分">
                      <el-rate v-model="data.pfnum"
                               size="large"
                               show-text
                               :max="10"
                               :texts="['1分', '2分', '3分', '4分', '5分', '6分', '7分', '8分', '9分', '10分']"
                               style=":root { --el-rate-void-color: red; --el-rate-fill-color: #10d8d8;--el-rate-text-color:#10d8d8; }"></el-rate>
                    </el-form-item>
                    <el-input type="textarea" v-model="data.content" placeholder="输入评价内容"  style="width: 100%;"></el-input>
                    <el-button type="primary" @click="commAdd(0)" style="margin: 10px">提交</el-button>
                  </div>


                </el-tab-pane>

              </el-tabs>
            </div>


          </el-col>

          <el-col :span="6">
            <div class="grid-content bg-purple" style="min-height: 350px;">
              <el-card class="box-card">
                <div slot="header" class="clearfix" style="text-align: center;margin: 20px auto;">
                  <h3>菜品评分排行</h3>
                </div>
                <el-divider border-style="dashed" style="margin: 10px 0" />
                <div v-for="(item,i) in data.shujulist" :key="item.id" v-show="i<20">
                      <div style="display: flex;margin-top: 15px;gap: 5px">
                        <div style="margin: 15px 3px;color: #8c939d">
                          {{i+1}}.
                        </div>
                        <div>
                          <el-image :src="item.fileimg" @click="xqdateil(item.id)" style="width: 50px;height: 50px;border-radius: 10px;" />
                        </div>
                        <div style="padding-left: 5px;line-height: 2">
                          <div class="line1" style="cursor: pointer">
                            <b @click="xqdateil(item.id)" >{{item.name}}</b>
                          </div>
                          <p style="margin: 1px auto;color: #10d8d8">{{item.protypename}} / {{ item.categoryname }} / <b>{{ item.pfnum }} 分</b></p>
                        </div>
                    </div>
                  <el-divider border-style="dashed" style="margin: 10px 0" />
                </div>
              </el-card>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <el-dialog title="评价信息" width="60%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <div v-html="data.form" style="line-height: 1.8"></div>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">关 闭</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
  
</template>

<script setup>
import {createApp, onMounted, onUnmounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import {useRouter} from "vue-router";
import {listenMsg} from "@/utils/crossTagMsg.js";
import {ArrowRight, Calendar, Delete, Edit, EditPen, Pointer, ShoppingCart, Star, View} from "@element-plus/icons-vue";
import SvgIcon from "@/components/SvgIcon.vue";
//创建路由对象
const route = useRouter()
const data = reactive({
  member:JSON.parse(localStorage.getItem('member') || '{}'),
  id:route.currentRoute.value.query.id,
  key:'',
  pageNum:1,
  pageSize:10,
  total:0,
  shuju:{},
  shujulist:[],
  evaluatelist:[],
  dianzanlist:[],
  dzlist:[],
  content:'',
  hfcontent:'',
  currentId:0,
  activeName:'first',
  pfnum:8,
  form: {},
  formVisible:false
})

const cancelListen = listenMsg(msginfo=>{
  if (msginfo.type==='evaluate'){
    getevaluatelist()
  }
})
onUnmounted(cancelListen)
//查看更多
const Showcontent  = (content)=>{
  data.form = content
  data.formVisible = true
}

//信息详情
const dateil =()=>{
  request.post('shujuDateil', qs.stringify({
    id:data.id,
  })).then(res => {
    data.shuju = res.data.shuju
    data.shujulist = res.data.list
    data.dianzanlist = res.data.dzlist
  })
}

//查询是否点赞
const getdzlist =()=>{
  request.post('DzList', qs.stringify({
    memberid:data.member.id,
    shujuid:data.id,
  })).then(res => {
    data.dzlist = res.data
  })
}

//点赞新增
const Adddianzan = (sjid) =>{
  if (JSON.stringify(data.member)==='{}'){ElMessage({ message: '登录后点赞', grouping: true, type: 'error',});return false;}
  request.post('dianzanAdd', qs.stringify({
    memberid:data.member.id,
    shujuid:sjid,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success("点赞+1")
    }else{
      ElMessage.error(res.msg)
    }
    dateil()
    getdzlist()
  })
}
//收藏新增
const Addfav = (sjid) =>{
  if (JSON.stringify(data.member)==='{}'){ElMessage({ message: '登录后收藏', grouping: true, type: 'error',});return false;}
  request.post('favAdd', qs.stringify({
    memberid:data.member.id,
    shujuid:sjid,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success("收藏成功")
    }else{
      ElMessage.error(res.msg)
    }
    dateil()
  })
}

//评价信息列表
const getevaluatelist =()=>{
  request.post('evaluateList', qs.stringify({
    shujuid:data.id,
    evaluateid:0,
    pageNum:data.pageNum,
    pageSize:data.pageSize,
  })).then(res => {
    data.evaluatelist = res.data.list
    data.total = res.data.total
  })
}

//评价交流信息新增
const commAdd =(evaluateid)=>{
  if (JSON.stringify(data.member)==='{}'){ElMessage({ message: '登录后评价', grouping: true, type: 'error',});return false;}
  if(data.hfcontent!==''){
    data.content = data.hfcontent
    data.pfnum = 0
  }
  if(data.content===''){ElMessage.error("请输入评价内容");return;}
  request.post('evaluateAdd', qs.stringify({
    memberid:data.member.id,
    shujuid:data.id,
    evaluateid:evaluateid,
    content:data.content,
    pfnum:data.pfnum,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success("操作成功")
      getevaluatelist()
      dateil()
      data.content=''
      data.hfcontent=''
      data.pfnum = 8
    }else{
      ElMessage.error(res.msg)
      data.content=''
    }
  })
}
//显示评价回复输入框
const toggleInput =(id)=>{
  data.currentId = id;
  data.content=''
}

//删除评价信息
const del = (id)=>{
  request.post('evaluateDel', qs.stringify({
    id:id,
  })).then(res => {
    if(res.code==='200'){
      ElMessage.success("操作成功")
      getevaluatelist()
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const xqdateil=(id)=>{
  location.href = '/front/shujuDateil?id='+id
}

  dateil()
  getevaluatelist()
  getdzlist()

</script>
<style scoped>


</style>
