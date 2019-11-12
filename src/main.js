import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import iView from 'view-design'
import config from '@/config'
import httpRequest from '@/libs/api.request'
import { hasPerms } from '@/libs/util'
import './index.less'
import '@/assets/icons/iconfont.css'

Vue.config.productionTip = false
// 实际打包时应该不引入mock
/* eslint-disable */
// if (process.env.NODE_ENV !== 'production') require('@/mock')

Vue.prototype.$config = config
Vue.prototype.$http = httpRequest
Vue.prototype.hasPerms = hasPerms

Vue.use(iView)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
