<template>
    <div>
      <div style="width: 95%;margin: 10px auto">
      <div class="card" >
        <el-input v-model="data.key" clearable style="width: 200px;margin-right: 10px" placeholder="请输入名称"/>
        <el-button type="warning" plain @click="load(data.key)">搜索</el-button>
      </div>
        <el-row :gutter="10">
          <el-col :span="18">
            <div class="card" style="margin-top: 5px" >
              <el-table :data="data.tableData"
                        border
                        :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
                        size="large"
                        :row-key="getRowKeys" @selection-change="handleSelectionChange" min-height="250" style="width: 100%">
                <el-table-column type="selection" :reserve-selection="true" width="55" />
                <el-table-column prop="goodsname" label="名称" />
                <el-table-column label="营养成分">
                  <template #default="scope">
                    {{scope.row.goods.cfname}}
                  </template>
                </el-table-column>
                <el-table-column label="营养含量(μg/100g)">
                  <template #default="scope">
                    {{scope.row.goods.yynum}}
                  </template>
                </el-table-column>
                <el-table-column label="热量(Ka/100g)">
                  <template #default="scope">
                    {{scope.row.goods.rlnum}}
                  </template>
                </el-table-column>
                <el-table-column label="数量" width="180">
                  <template #default="scope">
                    <el-input-number @change="handleChange(scope.row,data.valsAll)" size="small" :min="1"  v-model="scope.row.slnum"></el-input-number>
                  </template>
                </el-table-column>
                <el-table-column prop="yyheji" label="营养合计" />
                <el-table-column prop="heji" label="热量合计" />
                <el-table-column label="操作" >
                  <template #default="scope">
                    <el-button type="danger" plain size="small" @click="Delete(scope.row.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div v-if="data.total>0" style="display: flex;justify-content: center;margin: 15px auto">
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
          </el-col>
          <el-col :span="6">
            <div class="card" style="margin-top: 5px">
              <div style="float: left;color: #888888FF;">
                <p>已 选 ：{{data.valsAll.length}} 个</p>
                <p>营养含量 ：<strong style="color: #ff5000;font-size: 20px;">{{data.yybtotal}} Ug</strong></p>
                <p>热量合计 ：<strong style="color: #ff5000;font-size: 20px;">{{data.subtotal}} Ka</strong></p>
              </div>
              <el-button v-if="data.valsAll.length>0" @click="Add" type="primary" style="width: 100%;margin-top: 70px;">提交</el-button>
              <el-button v-else disabled type="primary" style="width: 100%;margin-top: 70px;">请选择食物</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-drawer v-model="data.formVisible" :before-close="handleClose"  size="40%" :with-header="false">
        <h3 style="text-align: center">{{data.title}}</h3>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="120px" style="padding: 20px">
          <el-form-item label="饮食日期" prop="rqdate">
            <el-date-picker @blur="comp()"
                            style="width: 100%"
                            v-model="data.form.rqdate"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="选择日期"/>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
        </template>

      </el-drawer>

    </div>
</template>

<script setup>
import {createApp, onMounted, reactive, ref} from "vue";
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from "element-plus";
import qs from "qs";
import {useRouter} from "vue-router";
const router = useRouter()
//提交是表单校验
const formRef = ref()
//关闭弹框回调函数
const handleClose = () => {
  ElMessageBox.confirm('是否确认需要关闭当前窗口?')
      .then(() => {
        data.formVisible = false
      })
      .catch(() => {
      })
}

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
  valsAll:[],
  subtotal:0,
  yybtotal:0,
  rules:{
    rqdate: [{ required: true, message: '日期不能为空', trigger: 'blur' },],
  }
})

//信息列表
const load =()=>{
  request.post('cartList', qs.stringify({
    memberid:data.member.id,
    key:data.key,
    pageNum: data.pageNum,
    pageSize: data.pageSize,
  })).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
//编辑食物数量
const handleChange = (row,vals) =>{
  request.post('cartEdit', qs.stringify({
    id:row.id,
    slnum:row.slnum,
  })).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
  //调用计算选食物合计方法
  if (data.valsAll.length>0){
    data.valsAll = data.valsAll.map(item=>{
      if (item.id===row.id){
        item.slnum = row.slnum
      }
    })
    handleSelectionChange(vals)
  }
}
//解决跨分页选中后分页取消的情况
const getRowKeys = (row)=>{
  return row.id;
}

//选中需要删除的对象
const handleSelectionChange = (val)=>{
  data.valsAll = val;
  //计算选中食物的总价
  let subtotal = 0;
  let yybtotal = 0;
  for (let i = 0; i < data.valsAll.length; i++) {
    //console.log('数量===',this.valsAll[i].slnum)
    subtotal += data.valsAll[i].slnum*data.valsAll[i].goods.rlnum;
    yybtotal += data.valsAll[i].slnum*data.valsAll[i].goods.yynum;
  }
  data.subtotal = subtotal;
  data.yybtotal = yybtotal;
}


// 信息新增
const Add = () => {
  data.form = {}
  data.formVisible = true
  data.title='饮食记录新增'
}

//提交新增
const save = () => {
  formRef.value.validate((valid=>{
    if(valid){
    let ids = data.valsAll.map(item => item.id).join()
  request.post('jiluAdd', qs.stringify({
    memberid:data.member.id,
    rqdate:data.form.rqdate,
    vals:ids
  })).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
      data.valsAll=[]
      data.subtotal=0
      data.yybtotal=0
    } else {
      ElMessage.error(res.msg)
    }
  })
    }
  }))
}

// 删除
const Delete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', { type: 'warning' }).then(res => {
    request.post('cartDel?id=' + id).then(res => {
      if (res.code === '200') {
        load()
        layer.msg('操作成功',{icon:6})
      } else {
        layer.msg(res.msg,{icon:5})
      }
    })
  }).catch(err => {})
}

const comp =()=>{
  let year = new Date().getFullYear(); //年
  let month = new Date().getMonth() +1;//月份
  let day = new Date().getDate();//日
  let time = year + '-' + month + '-' + day
  let formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`; //设置获取当前日期的格式YYYY-MM-DD
  var dqtime = Date.parse(new Date(formattedDate));
  let stime2 = Date.parse(new Date(data.form.rqdate));
  if(dqtime < stime2){
    ElMessage.error('预约日期不能大于当前日期');
    data.form.rqdate=""
  }
}

load()

/*onMounted(() => {
  load(data.pageNum)
})*/

</script>
<style scoped>
</style>
