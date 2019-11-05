<template>
  <div>
    <Form ref="condition" :model="form" inline @keyup.enter.native="getDataList">
      <FormItem prop="username">
        <Input type="text" v-model="form.username" placeholder="用户名">
          <Icon type="ios-person-outline" slot="prepend"></Icon>
        </Input>
      </FormItem>
      <FormItem>
        <Button @click="getDataList">查询</Button>
        <Button v-if="this.$hasPerms('sys:user:save')">新增</Button>
        <Button v-if="this.$hasPerms('sys:user:delete')">批量删除</Button>
      </FormItem>
    </Form>
    <Table border :loading="isLoading" ref="selection" :columns="columns" :data="dataList">
      <template slot-scope="{ row }" slot="status">
        <Tag v-if="row.status === 1" size="medium" color="error">禁用</Tag>
        <Tag v-else size="default" color="success">正常</Tag>
      </template>
      <template slot-scope="{ row }" slot="handle">
        <Button type="warning" size="small" style="margin-right: 5px" @click="updateHandle(row.userId)">修改</Button>
        <Button type="error" size="small" @click="removeHandle(row.userId)">删除</Button>
      </template>
    </Table>
    <div>
      <Page :total="total"
            :current="pageNum"
            :page-size="pageSize"
            @on-change="changeHandle"
            @on-page-size-change="sizeChangeHandle"
            show-total show-sizer></Page>
    </div>
    <user-add-or-update v-if="addOrUpdateVisible" ref="userAddOrUpdate" @refreshDataList="getDataList"></user-add-or-update>
  </div>
</template>
<script>
import UserAddOrUpdate from './user-add-or-update'
export default {
  components: {
    UserAddOrUpdate
  },
  data () {
    return {
      form: {
        username: ''
      },
      isLoading: true,
      dataList: [],
      pageNum: 1,
      pageSize: 20,
      total: 0,
      addOrUpdateVisible: false,
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: 'ID',
          align: 'center',
          key: 'userId'
        },
        {
          title: '用户名',
          align: 'center',
          key: 'username'
        },
        {
          title: '邮箱',
          align: 'center',
          key: 'email'
        },
        {
          title: '手机号',
          align: 'center',
          key: 'mobile'
        },
        {
          title: '状态',
          align: 'center',
          slot: 'status'
        },
        {
          title: '创建时间',
          align: 'center',
          key: 'createTime'
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
    changeHandle (page) {
      this.pageNum = page
      this.getDataList()
    },
    sizeChangeHandle (page) {
      this.pageSize = page
      this.pageNum = 1
      this.getDataList()
    },
    updateHandle (id) {
      console.log(id)
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.userAddOrUpdate.init(id)
      })
    },
    removeHandle () {

    },
    getDataList () {
      this.$http.request({
        url: '/sys/user/list',
        method: 'get',
        params: {
          'pageNum': this.pageNum,
          'pageSize': this.pageSize,
          'username': this.form.username
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.result.rows
          this.total = Number(data.result.total)
          this.isLoading = false
        } else {
          this.dataList = []
          this.total = 0
        }
      })
    }
  }
}
</script>
