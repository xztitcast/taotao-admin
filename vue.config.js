const path = require('path')

const resolve = dir => {
  return path.join(__dirname, dir)
}

// 项目部署基础

const BASE_URL = process.env.NODE_ENV === 'production'
  ? '/'
  : '/'

module.exports = {
  // Project deployment base
  // basePath: BASE_URL,
  // 如果你不需要使用eslint，把lintOnSave设为false即可
  publicPath: BASE_URL,
  lintOnSave: false,
  chainWebpack: config => {
    config.resolve.alias
      .set('@', resolve('src')) // key,value自行定义，比如.set('@@', resolve('src/components'))
      .set('_c', resolve('src/components'))
  },
  // 设为false打包时不生成.map文件
  productionSourceMap: false,
  // 这里写你调用接口的基础路径，来解决跨域，如果设置了代理，那你本地开发环境的axios的baseUrl要写为 '' ，即空字符串
  // devServer: {
  //   proxy: 'localhost:3000'
  // }
  css: {
    loaderOptions: { // 向 CSS 相关的 loader 传递选项
      less: {
        javascriptEnabled: true
      }
    }
  },
  devServer: {
    host: 'localhost',
    port: 32000,
    proxy: {
      '/sys': {
        target: 'http://localhost:8080',
        pathRewrite: { '^/admin': '/admin' }
      }
    }
  }
}
