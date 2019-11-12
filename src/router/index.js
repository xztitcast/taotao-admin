/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/libs/api.request.js'
import Main from '@/views/main'
import { getToken, removeComponet } from '@/libs/util'
import { forEach } from '@/libs/tools'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  // { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('login/login'), name: 'login', meta: { title: '登录' } }
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: Main,
  name: 'main',
  redirect: '/home',
  meta: { title: '主入口整体布局' },
  children: [
    { path: '/home', name: 'home', icon: 'md-home', meta: { title: '首页', icon: 'md-home' }, component: _import('single-page/home/home') }
  ],
  beforeEnter (to, from, next) {
    let token = getToken()
    if (!token || !/\S/.test(token)) {
      router.options.isAddDynamicMenuRoutes = false
      sessionStorage.clear()
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),
  isAddDynamicMenuRoutes: false,
  routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
  // 添加动态(菜单)路由
  // 1. 已经添加 or 全局路由, 直接访问
  // 2. 获取菜单列表, 添加并保存本地存储
  if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
    next()
  } else {
    http.request({
      url: '/sys/menu/nav',
      method: 'get'
    }).then(({ data }) => {
      if (data && data.code === 0) {
        fnAddDynamicMenuRoutes(data.menuList)
        router.options.isAddDynamicMenuRoutes = true
        sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
        next({ ...to, replace: true })
      } else {
        sessionStorage.setItem('permissions', '[]')
        next()
      }
    }).catch((e) => {
      console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
      router.push({ name: 'login' })
    })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
  var temp = []
  for (var i = 0; i < globalRoutes.length; i++) {
    if (route.path === globalRoutes[i].path) {
      return 'global'
    } else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
      temp = temp.concat(globalRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}
const fnAddDynamicMenuRoutes = (menuList = []) => {
  let routes = getRouterByMenuList(menuList)
  console.log(routes)
  router.addRoutes([
    ...routes,
    { path: '*', redirect: { name: '404' } }
  ])
  let list = []
  mainRoutes.children.map((element) => {
    list.push(element)
  })
  list = list.concat(routes)
  removeComponet(list)
  sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(list || '[]'))
}

const getRouterByMenuList = (list, componentStr = 'main', routes = []) => {
  forEach(list, item => {
    let obj = {
      path: `/${item.url}`,
      name: item.url.replace('/', '_'),
      icon: item.icon,
      meta: {
        title: item.name,
        icon: item.icon
      },
      component: componentStr === 'main' ? _import(componentStr + '/' + componentStr) : _import(componentStr + '/' + `${item.url}`)
    }
    if (item.list) {
      obj.children = getRouterByMenuList(item.list, item.url)
    }
    routes.push(obj)
  })
  return routes
}

export default router
