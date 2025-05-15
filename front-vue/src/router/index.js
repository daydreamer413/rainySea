import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    //前台一级路由
    {path: '/', name: 'front',redirect:'/front/index', component: () => import('../views/FrontView.vue'),
      children:[//前台二级路由
        {path: '/front/memberLogin', name: 'memberLogin',meta: {name: '用户登录' }, component: () => import('../views/front/memberLoginView.vue')},
        {path: '/front/memberReg', name: 'memberReg',meta: {name: '用户注册' }, component: () => import('../views/front/memberRegView.vue')},
        {path: '/front/memberpreson', name: 'memberpreson',meta: {qTis:true, name: '个人信息编辑' }, component: () => import('../views/front/memberpresonView.vue')},
        {path: '/front/memberpass', name: 'memberpass',meta: {qTis:true, name: '个人密码编辑' }, component: () => import('../views/front/memberpassView.vue')},
        {path: '/front/index', name: 'index',meta: {name: '首页' }, component: () => import('../views/front/indexView.vue')},
        {path: '/front/newslist', name: 'newslist',meta: {name: '健康资讯列表' }, component: () => import('../views/front/newslistView.vue')},
        {path: '/front/newsDateil', name: 'newsDateil',meta: {name: '健康资讯详情' }, component: () => import('../views/front/newsDateilView.vue')},
        {path: '/front/favlist', name: 'favlist',meta: {qTis:true,name: '收藏记录' }, component: () => import('../views/front/favlistView.vue')},
        {path: '/front/shujulist', name: 'shujulist',meta: { name: '菜品信息' }, component: () => import('../views/front/shujulistView.vue')},
        {path: '/front/shujuDateil', name: 'shujuDateil',meta: {name: '菜品信息详情' }, component: () => import('../views/front/shujuDateilView.vue')},
        {path: '/front/shujulb', name: 'shujulb',meta: {qTis:true,name: '菜品信息管理' }, component: () => import('../views/front/shujulbView.vue')},
        {path: '/front/messagelist', name: 'messagelist',meta: {qTis:true,name: '在线留言' }, component: () => import('../views/front/messagelistView.vue')},
        {path: '/front/cartlist', name: 'cartlist',meta: {qTis:true,name: '饮食清单' }, component: () => import('../views/front/cartlistView.vue')},
        {path: '/front/jilulist', name: 'jilulist',meta: {qTis:true,name: '每日饮食记录信息' }, component: () => import('../views/front/jilulistView.vue')},
        {path: '/front/goodslist', name: 'goodslist',meta: {name: '食物信息列表' }, component: () => import('../views/front/goodslistView.vue')},
      ]},

      //后台一级路由
    {path: '/login', name: 'login',meta: { name: '登录' }, component: () => import('../views/loginView.vue')},
    {path: '/register', name: 'register',meta: { name: '注册' }, component: () => import('../views/registerView.vue')},
    {path: '/manager', name: 'manager',redirect:'/manager/home', component: () => import('../views/ManagerView.vue'),
    children:[//后台二级路由
      {path: '/manager/home', name: 'home',meta: {isAuth:true, name: '后台主页' }, component: () => import('../views/manager/HomeView.vue')},
      {path: '/manager/imgadv', name: 'imgadv',meta: {isAuth:true, name: '轮播图信息管理' }, component: () => import('../views/manager/imgadvView.vue')},
      {path: '/manager/news', name: 'news',meta: {isAuth:true, name: '健康资讯信息管理' }, component: () => import('../views/manager/newsView.vue')},
      {path: '/manager/admin', name: 'admin',meta: {isAuth:true, name: '管理员信息' }, component: () => import('../views/manager/adminView.vue')},
      {path: '/manager/member', name: 'member',meta: {isAuth:true, name: '用户信息管理' }, component: () => import('../views/manager/memberView.vue')},
      {path: '/manager/person', name: 'person',meta: {isAuth:true, name: '个人信息' }, component: () => import('../views/manager/personView.vue')},
      {path: '/manager/passuedit', name: 'passuedit',meta: {isAuth:true, name: '密码编辑' }, component: () => import('../views/manager/passueditView.vue')},
      {path: '/manager/category', name: 'category',meta: {isAuth:true, name: '分类信息管理' }, component: () => import('../views/manager/categoryView.vue')},
      {path: '/manager/evaluate', name: 'evaluate',meta: {isAuth:true, name: '评价信息管理' }, component: () => import('../views/manager/evaluateView.vue')},
      {path: '/manager/protype', name: 'protype',meta: {isAuth:true, name: '口味分类信息管理' }, component: () => import('../views/manager/protypeView.vue')},
      {path: '/manager/shuju', name: 'shuju',meta: {isAuth:true, name: '菜品信息管理' }, component: () => import('../views/manager/shujuView.vue')},
      {path: '/manager/message', name: 'message',meta: {isAuth:true, name: '留言信息管理' }, component: () => import('../views/manager/messageView.vue')},
      {path: '/manager/rizhi', name: 'rizhi',meta: {isAuth:true, name: '日志信息管理' }, component: () => import('../views/manager/rizhiView.vue')},
      {path: '/manager/goods', name: 'goods',meta: {isAuth:true, name: '食物信息管理' }, component: () => import('../views/manager/goodsView.vue')},
      {path: '/manager/jilu', name: 'jilu',meta: {isAuth:true, name: '饮食记录信息管理' }, component: () => import('../views/manager/jiluView.vue')},
    ]},


  ],
})

//前置路由守卫
router.beforeEach((to, from, next) => {
  //document.title = to.meta.name;
const admin = JSON.parse(localStorage.getItem('admin') || '{}')
  //console.log(JSON.stringify(admin))
  //后台管理路由鉴权
  if (to.meta.isAuth){
    if (JSON.stringify(admin)!=='{}'){
      next();
    }else{
      next('/login');
    }
  }else {
    next();
  }
});
//前置路由守卫
router.beforeEach((to, from, next) => {
  //document.title = to.meta.name;
  const member = JSON.parse(localStorage.getItem('member') || '{}')
  //console.log(JSON.stringify(member.status))
  //前台台管理路由鉴权
  if (to.meta.qTis){
    if (JSON.stringify(member)!=='{}'){
      next();
    }else{

      next('/front/memberLogin');
    }
  }else {
    next();
  }
});

//后置路由守卫
router.afterEach((to,from)=>{
  document.title = to.meta.name || '个性化健康饮食系统';
})

export default router
