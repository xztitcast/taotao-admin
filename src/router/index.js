/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/libs/api.request.js'
import Main from '@/components/main'
import { getToken } from '@/libs/util'

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
    { path: '/home', component: _import('single-page/home/home'), name: 'home', meta: { title: '首页', icon: 'md-home' } }
  ],
  beforeEnter(to, from, next) {
    let token = getToken()
    if (!token || !/\S/.test(token)) {
      // clearLoginInfo()
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),
  isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
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
        sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
        sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
        next({ ...to, replace: true })
      } else {
        sessionStorage.setItem('menuList', '[]')
        sessionStorage.setItem('permissions', '[]')
        next()
      }
    }).catch((e) => {
      console.log(e)
      console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
      router.push({ name: 'login' })
    })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType(route, globalRoutes = []) {
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

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes(menuList = [], routes = []) {
  // var temp = []
  // for (var i = 0; i < menuList.length; i++) {
  //   if (menuList[i].list && menuList[i].list.length >= 1) {
  //     temp = temp.concat(menuList[i].list)
  //   } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
  //     menuList[i].url = menuList[i].url.replace(/^\//, '')
  //     var route = {
  //       path: menuList[i].url.replace('/', '-'),
  //       component: null,
  //       name: menuList[i].url.replace('/', '-'),
  //       meta: {
  //         title: menuList[i].name,
  //         icon: menuList[i].icon
  //       }
  //     }
  //     route['component'] = _import(`modules/${menuList[i].url}`) || null
  //     routes.push(route)
  //   }
  // }
  let parent = []
  for (var i = 0; i < menuList.length; i++) {
    parent.push({ name: menuList[i].name, path: '', meta: { icon: menuList[i].icon, title: menuList[i].name }, children: [] })
    var temp = menuList[i].list
    for (var j = 0; j < temp.length; j++) {
      temp[j].url = temp[j].url.replace(/^\//, '')
      var route = {
        path: temp[j].url.replace('/', '-'),
        component: null,
        name: temp[j].url.replace('/', '-'),
        meta: {
          title: temp[j].name,
          icon: temp[j].icon
        }
      }
      route['component'] = _import(`modules/${temp[i].url}`) || null
      parent[i].children.push(route)
      // routes.push(route)
    }
  }
  mainRoutes.name = 'main-dynamic'
  mainRoutes.children.push(...parent)
  router.addRoutes([
    mainRoutes,
    { path: '*', redirect: { name: '404' } }
  ])
  sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
  console.log('\n')
  console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
  console.log(mainRoutes.children)
  console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
}

export default router
