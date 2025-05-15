<template>
    <div >
      <div style="margin: 0 auto;width: 99.7%">
        <el-row :gutter="5">
          <el-col :span="24">
            <div class="grid-content ep-bg-purple" >
              <el-carousel
                  height="500px"
                  type="card"
                  motion-blur>
                <el-carousel-item  v-for="imgadv in data.imgadvlist"  :key="imgadv.id" >
                  <el-image @click="router.push(imgadv.url)" :src="imgadv.fileimg" style="width: 100%;height: 100%;"/>
                </el-carousel-item>
              </el-carousel>
            </div>
          </el-col>

        </el-row>
      </div>

      <div style="width: 80%;margin: 30px auto;">
        <el-row :gutter="10">
          <el-col :span="17"><div class="grid-content ep-bg-purple" >
            <div class="card1" style="min-height: 580px;padding: 0;">
              <div style="display: flex;height: 50px;align-items: center">
                <div style="flex: 1;font-size: 17px;;padding-left: 15px;font-weight: bold;color: #1a1a1a">最新菜品信息</div>
                <div style="flex: 1"></div>
                <div style="flex: 1;font-size: 15px;padding-right: 15px;text-align: right;cursor: pointer;color: #10d8d8" @click="router.push('/front/shujulist')">更多菜品 <el-icon><Position /></el-icon></div>
              </div>
              <el-divider style="margin: 0"/>
              <div style="margin: 15px 15px;">
                <el-row :gutter="5">
                  <el-col :span="6" v-for="(shuju,i) in data.shujulist" v-show="i<12" :key="shuju.id">
                    <div class="grid-content ep-bg-purple" style="margin-bottom: 15px;">
                      <el-card shadow="never" style="border: 1px solid #10d8d8;border-radius: 10px;" class="divimgclass">
                        <el-image class="imgclass" :src="shuju.fileimg" @click="router.push('/front/shujuDateil?id='+shuju.id)" style="width: 100%;height: 160px;border-radius: 10px;" />
                        <div style="text-align: left;padding: 5px 5px;" >
<!--                          | <el-tag type="warning">{{ shuju.categoryname }}</el-tag>-->
                          <b class="line1"> {{shuju.name}}</b>
                          <p style="margin: 5px auto;color: #606266">{{shuju.protypename}} / {{shuju.categoryname}} / 浏览: {{ shuju.llnum }}</p>
                          <div style="float: right;margin-bottom: 5px;"><el-tag type="primary" :effect="'dark'"> <el-icon><StarFilled /></el-icon> {{shuju.pfnum}}</el-tag></div>

                        </div>

                      </el-card>
                    </div>
                  </el-col>
                </el-row>

              </div>


            </div>
          </div></el-col>

          <el-col :span="7"><div class="grid-content ep-bg-purple" >
            <div class="card1" style="min-height: 887px;padding: 0;">
              <div style="display: flex;height: 50px;align-items: center">
                <div style="flex: 1;font-size: 17px;padding-left: 15px;font-weight: bold;color: #000">最新资讯</div>
                <div style="flex: 1"></div>
                <div style="flex: 1;font-size: 15px;padding-right: 15px;text-align: right;cursor: pointer;color: #10d8d8" @click="router.push('/front/newslist')">更多 <el-icon><Position /></el-icon></div>
              </div>

              <el-divider style="margin: 0"/>
              <div style="margin: 15px 15px;">
                <div v-for="(item,i) in data.newslist" :key="item.id" v-show="i<3" >
                  <div style="display: flex;margin-top: 10px;">
                    <div>
                      <el-image :src="item.fileimg" @click="router.push('newsDateil?id='+item.id)" style="width: 50px;height: 50px;border-radius: 10px;" />
                    </div>
                    <div style="padding-left: 15px;line-height: 2">
                      <div class="line1" style="cursor: pointer">
                          <b @click="router.push('newsDateil?id='+item.id)" >{{item.title}}</b>
                      </div>
                      <span style="padding-top: 20px;color: #888888FF"> <el-icon><Calendar /></el-icon> {{item.savetime}}</span>
                    </div>
                  </div>

                </div>


              </div>

              <el-divider style="margin: 0"/>

              <div style="display: flex;height: 50px;align-items: center">
                <div style="flex: 1;font-size: 14px;padding-left: 10px;font-weight: bold;color: #000">口味/目标/健康状况推荐</div>
                <div style="flex: 1"></div>
                <div style="flex: 1;font-size: 15px;padding-right: 15px;text-align: right;cursor: pointer;color: #10d8d8" @click="router.push('/front/shujulist')">更多 <el-icon><Position /></el-icon></div>
              </div>
              <el-divider style="margin: 0"/>
              <template v-if="JSON.stringify(data.member)!=='{}'">
              <div style="margin: 15px 15px;">
                <div v-for="(item,i) in data.phlist" :key="item.id" v-show="i<8" >
                  <div style="display: flex;margin-top: 10px;">
<!--                    <div style="margin: 15px 3px;color: #8c939d">
                      {{i+1}}.
                    </div>-->
                    <div>
                      <el-image :src="item.fileimg" @click="router.push('shujuDateil?id='+item.id)" style="width: 50px;height: 50px;border-radius: 10px;" />
                    </div>
                    <div style="padding-left: 15px;line-height: 2">
                      <div class="line1" style="cursor: pointer">
                        <b @click="router.push('shujuDateil?id='+item.id)" >{{item.name}}</b>
                      </div>
                      <p style="margin: 1px auto;color: #10d8d8">{{item.protypename}} / {{item.categoryname}} / 浏览：{{ item.llnum }} / <b>{{ item.pfnum }} 分</b></p>
                    </div>
                  </div>

                </div>


              </div>

              </template>

            </div>
          </div></el-col>
        </el-row>
      </div>


    </div>
</template>

<script setup>
import {onUnmounted, reactive, ref} from "vue";
//import router from "@/router/index.js";
import request from "@/utils/request.js";
import qs from "qs";
import {useRouter} from "vue-router";
import {listenMsg} from "@/utils/crossTagMsg.js";
import {ArrowRight, Calendar, Position, StarFilled} from "@element-plus/icons-vue";
const router = useRouter()
const data = reactive({
  member: JSON.parse(localStorage.getItem('member') || '{}'),
  key:'',
  key1:'',
  imgadvlist:[],
  newslist:[],
  catelist:[],
  shujulist:[],
  phlist:[],
  gongluelist:[],
})
const cancelListen = listenMsg(msginfo=>{
  if (msginfo.type==='轮播图更新' || msginfo.type==='新闻更新' || msginfo.type==='shuju更新'  || msginfo.type==='分类更新' || msginfo.type==='gonglue更新'){
    getindex()
  }
})
onUnmounted(cancelListen)

//首页信息查询
const getindex =()=>{
  request.post('index', qs.stringify({
    key:data.key,
    memberid:data.member.id,
  })).then(res => {
    data.imgadvlist = res.data.imglist
    data.newslist = res.data.nlist
    data.shujulist = res.data.sjlist
    data.phlist = res.data.phlist
    data.gongluelist = res.data.gllist
  })
}
getindex()
</script>

<style scoped>

</style>
