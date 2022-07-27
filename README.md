# taotao-admin
提供一个前后端分离的后台管理系统、后端采用java语言、前端采用vue2.0语言编写,开箱即用直接编写自己的业务代码即可
# 后端框架
  SpringBoot+mybatis-plus+redis+shiro+mysql+jdk11
# 前端框架
  vue2.0+elementUI
  前端项目名称: admin-element
# 代码结构
  controller:
  	Sys直接开头为后台管理系统提供(框架本身结构)
  	app目录下为基础应用提供
  	editor目录需要上传下载时使用
  	当需要编写业务时直接新建目录
  提示: 控制器必须以Sys开头, requestMapping必须以/sys开头

  Service:
   	业务接口实现
  提示: 业务接口实现必须extends ServiceImpl implements IService才能调用数据库
  
  entity:
    excel: excel表实体映射
    form: 前后台端参数映射
    数据ORM实体映射
  
  Mapper:
  	数据库Mapper接口
# shiro板块
```
  使用shiro进行后台管理权限认证、本质就是使用token进行赋权
```
# Sys系统管理板块
```
  用户、角色、菜单、日志、定时任务
  菜单: 数据库采用Tree结构进行存储、使用邻表法管理Tree数据
  菜单表: 目录、菜单、按钮三种类型,其中按钮代表访问权限
  用户与角色、角色与菜单采用中间表进行关联
```
# App板块
```
  应用基础功能, banner轮播图、全局配置字典、OSS云存储、版本管理
  当项目为分布式时可以将APP版本独立出去新建一个独立工程供其他服务调用
```
# editor
```
  上传、下载功能以及富文本编辑
```