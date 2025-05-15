<template>
    <div >
      <div class="card"><h2 style="text-align: center">欢迎进入个性化健康饮食系统后台管理中心</h2></div>

        <el-row :gutter="10" style="margin-top: 10px">
          <el-col :span="12"><div class="grid-content ep-bg-purple">
            <el-card shadow="always" style="height: 500px;">
              <div id="bie" style="width: 100%;height: 470px;padding-top: 20px;"></div>
              </el-card>
          </div>
          </el-col>
          <el-col :span="12"><div class="grid-content ep-bg-purple">
              <el-card shadow="always" style="height: 500px;">
                <div id="bar" style="width: 100%;height: 470px;padding-top: 20px;"></div>
              </el-card>
          </div>
          </el-col>
        </el-row>


      </div>


<!--      <div style="margin-top: 50px">
        <el-button type="success"  plain>操作按钮</el-button>
      </div>
      <div style="margin-top: 50px">
        <el-input v-model="data.name" style="width: 200px;"/>
        <RouterLink to="/manager/test">跳转至test路由</RouterLink>
        <el-button @click="router.push('/manager/test?id=1')">通过push跳转</el-button>
      </div>-->

</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import request from "@/utils/request.js";
import * as echarts from 'echarts';
import qs from "qs";
import B4I3EM2f from '@/assets/imgs/B4I3EM2f.png'
import dd from '@/assets/imgs/dd.png'
import update1 from '@/assets/imgs/update1.png'
import wffVGXii from '@/assets/imgs/wffVGXii.png'
onMounted(() => {
  //柱状图
  const chart = echarts.init(document.getElementById("bar"));
  const option = {
    title: {
      text: '菜品评分统计',
      subtext: '统计维度：平均分',
      left: 'center'
    },
    tooltip: {},
    /* legend: { data:['销量'] },*/
    xAxis: {
      //data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
    },
    yAxis: {},
    series: [
      {
        //data: [5,15,20,25,30,35],
        type: 'bar',
        showBackground: true,
        backgroundStyle: {
          color: 'rgba(180, 180, 180, 0.2)'
        },
        itemStyle: {
          color: (params) => {
            // 生成随机颜色
            const colorList = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'];
            const colorIndex = Math.floor(Math.random() * colorList.length);
            return colorList[colorIndex];
          }
        }
      }
    ]
  };
  //向后端发起请求获取数据
  request.post('bar',qs.stringify({
  })).then(res=>{
    option.xAxis.data = res.data.xdata
    option.series[0].data = res.data.ydata
    chart.setOption(option)
  })


//饼状图
  const chartbie = echarts.init(document.getElementById("bie"));
  const option1 = {
    title: {
      text: '菜品分类统计',
      subtext: '数据比例统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '菜品分类统计',
        type: 'pie',
        radius: '50%',
        /*     data: [ { value: 1048, name: 'Search Engine' }, ],*/
        label: {
          show: true, // 显示标签
          //position: 'inside', // 在饼图内部显示标签
          formatter: '{b}: {d}%' // 显示百分比
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  //向后端发起请求获取数据
  request.post('bie',qs.stringify({
  })).then(res=>{
    //option.xAxis.data = res.data.xdata
    option1.series[0].data = res.data
    chartbie.setOption(option1);
  })


});

</script>
