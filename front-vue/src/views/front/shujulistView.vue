<template>
  <div>
    <div  style="width: 90%;margin: 10px auto;text-align: center;" >
      <div >
        <el-row :gutter="10">
          <el-col :span="18">
            <div class="card1" style="margin-bottom: 10px">
              <div style="height: 40px;margin: 10px auto">
                <el-input clearable v-model="data.key" @input="load(data.key,'','')" placeholder="请输入名称" style="width: 240px;margin-right: 10px;"></el-input>
                <el-button type="info" size="default" @click="load(data.key,data.key1,data.key2)">搜索</el-button>
              </div>
              <div>
                <el-button :size="'small'" type="primary" :plain="data.key1!==undefined" size="default" @click="load('','','')" style="margin-bottom: 10px;">全部</el-button>
                <el-button :size="'small'" type="primary" :plain="data.key1!==category.id" size="default" @click="load('',category.id,'')" v-for="(category,i) in data.categorylist" style="margin-bottom: 10px;">
                  {{ category.name }}
                </el-button>
              </div>
              <div>
                <el-button :size="'small'" type="success" :plain="data.key2!==undefined" size="default" @click="load('','','')" style="margin-bottom: 10px;">全部</el-button>
                <el-button :size="'small'" type="success" :plain="data.key2!==protype.id" size="default" @click="load('','',protype.id)" v-for="(protype,i) in data.protypelist" style="margin-bottom: 10px;">
                  {{ protype.name }}
                </el-button>
              </div>

            </div>


            <el-row :gutter="10">
              <el-col :span="6" v-for="(shuju,i) in data.shujulist" :key="shuju.id">
                <div class="grid-content ep-bg-purple" style="margin-bottom: 15px;">
                  <el-card shadow="never" style="border: 1px solid #10d8d8;border-radius: 10px;" class="divimgclass">
                    <el-image class="imgclass" :src="shuju.fileimg" @click="router.push('/front/shujuDateil?id='+shuju.id)" style="width: 100%;height: 170px;border-radius: 10px;" />
                    <div style="text-align: left;padding: 5px 5px;" >
                      <b class="line1"> {{shuju.name}}</b>
                      <p style="margin: 5px auto;color: #606266">{{shuju.protypename}} / {{ shuju.categoryname }} /浏览：{{shuju.llnum}} </p>
                      <div style="float: right;margin-bottom: 5px;"><el-tag type="primary" :effect="'dark'"> <el-icon><StarFilled /></el-icon> {{shuju.pfnum}}</el-tag></div>

                    </div>

                  </el-card>
                </div>
              </el-col>
            </el-row>
            <div class="block" v-if="data.total>0" style="margin: 10px auto;display: flex;justify-content: center">
              <el-pagination
                  @current-change="load()"
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

          </el-col>

          <el-col :span="6">
            <el-card shadow="never" class="front-left" >
              <div v-if="JSON.stringify(data.member)!=='{}'">
                <h3>为您推荐</h3>
                <el-divider style="margin: 0"/>
                <div style="margin: 15px 15px;">
                  <div v-for="(item,i) in data.tjlist" :key="item.id" v-show="i<14" >
                    <div style="display: flex;margin-top: 10px;">
                      <div>
                        <el-image :src="item.fileimg" @click="router.push('shujuDateil?id='+item.id)" style="width: 50px;height: 50px;border-radius: 10px;" />
                      </div>
                      <div style="padding-left: 10px;line-height: 2;text-align: left">
                        <div class="line1" style="cursor: pointer">
                          <b @click="router.push('shujuDateil?id='+item.id)" >{{item.name}}</b>
                        </div>
                        <p style="margin: 1px auto;color: #10d8d8">{{item.protypename}} / {{ item.categoryname }} / 浏览：{{item.llnum}} / <b>{{ item.pfnum }} 分</b></p>
                      </div>
                    </div>

                  </div>


                </div>
              </div>
              <div v-if="JSON.stringify(data.member)==='{}'">
                <h3>菜品评分排行</h3>
                <el-divider style="margin: 0"/>
                <div style="margin: 15px 15px;">
                  <div v-for="(item,i) in data.phlist" :key="item.id" v-show="i<14" >
                    <div style="display: flex;margin-top: 10px;">
                      <div style="margin: 15px 3px;color: #8c939d">
                        {{i+1}}.
                      </div>
                      <div>
                        <el-image :src="item.fileimg" @click="router.push('shujuDateil?id='+item.id)" style="width: 50px;height: 50px;border-radius: 10px;" />
                      </div>
                      <div style="padding-left: 10px;line-height: 2;text-align: left">
                        <div class="line1" style="cursor: pointer">
                          <b @click="router.push('shujuDateil?id='+item.id)" >{{item.name}}</b>
                        </div>
                        <p style="margin: 1px auto;color: #10d8d8">{{item.protypename}} / {{ item.categoryname }} / 浏览：{{item.llnum}} / <b>{{ item.pfnum }} 分</b></p>
                      </div>
                    </div>

                  </div>


                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

    </div>

  </div>
</template>

<script setup>
import {createApp, onMounted, onUnmounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import qs from "qs";
import {useRouter} from "vue-router";
import {listenMsg} from "@/utils/crossTagMsg.js";
import {StarFilled} from "@element-plus/icons-vue";
const router = useRouter()
const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}'),
  key:'',
  key1:'',
  key2:'',
  pageNum: 1,
  pageSize: 12,
  total: 0,
  categorylist:[],
  protypelist:[],
  shujulist:[],
  orlist:[],
  phlist:[],
  tjlist:[],
})

//查询菜品排行
const getorlist = () =>{
  request.post('index',qs.stringify({
  })).then(res=>{
    data.phlist = res.data.pflist
  })
}
//查询菜品排行
const getTJlist = () =>{
  request.post('shujuxtTJ',qs.stringify({
    memberid:data.member.id
  })).then(res=>{
    data.tjlist = res.data.ctlist
  })
}

//查询分类列表
const getclist=()=>{
request.post('cateListAll').then(res=>{
  data.categorylist = res.data
})
}
//查询口味分类列表
const getplist=()=>{
  request.post('protypeListAll').then(res=>{
    data.protypelist = res.data
  })
}

//信息列表
const load =(key,key1,key2)=>{
  data.key1 = key1 || undefined
  data.key2 = key2 || undefined
  request.post('shujuList', qs.stringify({
    key:data.key,
    key1:data.key1,
    key2:data.key2,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.shujulist = res.data.list
    data.total = res.data.total
  })
}
getTJlist()
onMounted(() => {
  getorlist()
  getclist()
  getplist()
  load()
})
const cancelListen = listenMsg(msginfo=>{
  if (msginfo.type==='shuju更新' || msginfo.type==='category更新' || msginfo.type==='protype更新'){
    load()
    getclist()
    getplist()
  }
})
onUnmounted(cancelListen)

</script>
<style scoped>

</style>
