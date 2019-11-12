<template>
  <div class="mod-menu">
    <Form :inline="true" :model="dataForm">
      <FormItem>
        <Button v-if="true" type="primary" @click="addOrUpdateHandle()">新增</Button>
      </FormItem>
    </Form>
    <Table border :row-key="true" :loading="isLoading" :columns="dataColumn" :data="dataList" :span-method="handleSpan">
      <template slot-scope="{ row }" slot="icon">
        <Icon :type="row.icon || ''"></Icon>
      </template>
      <template slot-scope="{ row }" slot="catalog">
        <Tag v-if="row.type === 0" size="default">目录</Tag>
        <Tag v-else-if="row.type === 1" size="default" type="success">菜单</Tag>
        <Tag v-else-if="row.type === 2" size="default" type="info">按钮</Tag>
      </template>
      <template slot-scope="{ row }" slot="handle">
        <Button v-if="true" type="warning" size="small" @click="addOrUpdateHandle(row.menuId)">修改</Button>
        <Button v-if="true" type="error" size="small" @click="deleteHandle(row.menuId)">删除</Button>
      </template>
    </Table>
  </div>
</template>
<script>
import { treeDataTranslate } from '@/libs/util'
export default {
  data () {
    return {
      dataForm: {},
      dataList: [],
      isLoading: false,
      dataColumn: [
        {
          type: 'expand'
        },
        {
          title: '名称',
          align: 'center',
          key: 'name'
        },
        {
          title: '上级菜单',
          align: 'center',
          key: 'parentName'
        },
        {
          title: '图标',
          align: 'center',
          slot: 'icon'
        },
        {
          title: '类型',
          align: 'center',
          slot: 'catalog'
        },
        {
          title: '排序号',
          align: 'center',
          key: 'orderNum'
        },
        {
          title: '菜单URL',
          align: 'center',
          key: 'url'
        },
        {
          title: '授权标识',
          align: 'center',
          key: 'perms'
        },
        {
          title: '操作',
          align: 'center',
          slot: 'handle'
        }
      ]
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    addOrUpdateHandle (id) {

    },
    deleteHandle (id) {

    },
    getDataList () {
      this.isLoading = true
      this.$http.request({
        url: '/sys/menu/list',
        method: 'get',
        params: {}
      }).then(({ data }) => {
        this.dataList = treeDataTranslate(data, 'menuId')
        console.log(this.dataList)
        this.isLoading = false
      })
    },
    handleSpan ({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 0 && columnIndex === 0) {
        return [1, 2]
      }
    }
  }
}
</script>
