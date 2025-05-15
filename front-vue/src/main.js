import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import '@/assets/css/global.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//svg图标
import SvgIcon from "@/components/SvgIcon.vue";
import '@/assets/icons/iconfont.js'

const app = createApp(App)
app.use(ElementPlus,{
    locale:zhCn,
    size:"default",
})
//配置element-plus的icon图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.component('svg-icon',SvgIcon)
app.use(router)

app.mount('#app')
