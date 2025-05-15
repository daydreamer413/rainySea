<template>
  <div>
    <div  style="width: 75%;min-height: 350px;margin: 10px auto;" class="card">
      <div style="margin: 20px 10px; " >
        <el-input clearable v-model="data.key" @input="load()" placeholder="请输入标题" style="width: 260px;margin-right: 10px;"></el-input>
        <el-button type="info" size="default" @click="load()">搜索</el-button>
      </div>

<!--      <div style="margin:40px 10px">
        <el-divider />
        <div v-for="(item,i) in data.newslist" :key="item.id">
        <span><a href="javascript:void (0)" @click="router.push('/front/newsDateil?id='+item.id)"> <b > {{item.title}}</b></a></span>
          <div style="float: right">
            <span> <el-icon><Calendar /></el-icon>  {{item.savetime}}</span>
          </div>
          <el-divider />
        </div>
      </div>-->
      <div style="margin:40px 10px">
        <el-divider style="margin: 15px 0"/>
        <el-row :gutter="10" >
          <el-col :span="12" v-for="(item,i) in data.newslist" :key="item.id" style="margin-top: 10px">
            <div style="display:flex;gap: 10px">
              <div style="flex: 0.2;width: 85px;height: 85px;background: #f0f0f0;text-align: center;color: #666">
                <p style="margin-bottom: 10px;margin-top:20px;font-family: 宋体,serif;font-size: 19px">{{item.savetime.substring(8,10)}}</p>
                <p style="margin-bottom: 10px;font-family: 宋体,serif;font-size: 16px">{{item.savetime.substring(0,7)}}</p>
              </div>
              <div style="align-items: center;display: flex;flex: 1">
                <div>
                  <a href="javascript:void (0)" @click="router.push('/front/newsDateil?id='+item.id)"> <b>{{item.title}}</b></a>
                  <p class="line2" v-html="item.content" style="font-size: 13px;margin-bottom: 5px;margin-top: 0;color: #666"></p>
                </div>

              </div>

            </div>
            <el-divider style="margin: 15px 0"/>
          </el-col>
        </el-row>

      </div>

      <div class="block" v-if="data.total>0" style="margin: 20px auto;display: flex;justify-content: center">
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
    </div>


  </div>
</template>

<script setup>
import {createApp, onMounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import qs from "qs";
import {useRouter} from "vue-router";
const router = useRouter()
const data = reactive({
  key:'',
  key1:'',
  pageNum: 1,
  pageSize: 9,
  total: 0,
  newslist:[],
})
//信息列表
const load =()=>{
  request.post('newsList', qs.stringify({
    key:data.key,
    key1:data.key1,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.newslist = res.data.list
    data.total = res.data.total
  })
}

onMounted(() => {
  load()
})

</script>
<style scoped>
</style>
