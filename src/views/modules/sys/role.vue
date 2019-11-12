<template>
  <div class="mod-role">
    <Form :inline="true" :model="dataForm" @keyup.enter.native="getDataList">
      <FormItem>
        <Input v-model="dataForm.roleName" placeholder="角色名称" clearable></Input>
      </FormItem>
      <FormItem>
        <Button @click="getDataList">查询</Button>
        <Button v-if="true" type="primary" @click="addOrUpdateHandle()">新增</Button>
        <Button v-if="true" type="primary" @click="deleteHandle()" :disabled="dataListSelection.length <= 0">批量删除</Button>
      </FormItem>
    </Form>
    <Table border :loading="isLoading" :columns="dataColumn" :data="dataList" @on-selection-change="selectChangeHandle">
      <template slot-scope="{ row }" slot="handle">
        <Button v-if="true" type="text" size="small" @click="addOrUpdateHandle(row.roleId)">修改</Button>
        <Button v-if="true" type="text" size="samll" @click="deleteHandle(row.roleId)"></Button>
      </template>
    </Table>
    <Page :total="total"
          :current="pageNum"
          :page-size="pageSize"
          :page-size-opts="[10, 20, 50, 100, 200]"
          @on-change="changeHandle"
          @on-page-size-change="sizeChangeHandle"
          show-total show-sizer></Page>
  </div>
</template>
<script>
export default {
  data () {
    return {
      dataForm: {
        roleName: ''
      },
      dataList: [],
      pageNum: 1,
      pageSize: 20,
      total: 0,
      isLoading: false,
      dataListSelection: [],
      dataColumn: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: 'ID',
          align: 'center',
          key: 'roleId'
        },
        {
          title: '角色名称',
          align: 'center',
          key: 'roleName'
        },
        {
          title: '备注',
          align: 'center',
          key: 'remark'
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
    selectChangeHandle (value) {
      this.dataListSelection = value
    },
    addOrUpdateHandle (id) {

    },
    deleteHandle (id) {

    },
    getDataList () {
      this.isLoading = true
      this.$http.request({
        url: '/sys/role/list',
        method: 'get',
        params: {
          'pageNum': this.pageNum,
          'pageSize': this.pageSize,
          'roleName': this.dataForm.roleName
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.result.rows
          this.total = data.result.total
        } else {
          this.dataList = []
          this.total = 0
        }
        this.isLoading = false
      })
    }
  }
}
</script>
