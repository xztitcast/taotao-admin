<template>
  <Modal v-model="visible" :title="!dataForm.userId ? '新增' : '修改'" :mask-closable="false">
    <Form ref="dataForm" :model="dataForm" :rules="dataRules" @keyup.enter.native="handleSubmit">
      <FormItem prop="username" label="用户名">
        <Input v-model="dataForm.username" placeholder="登陆账号"></Input>
      </FormItem>
      <FormItem prop="password" label="密码">
        <Input v-model="dataForm.password" placeholder="密码"></Input>
      </FormItem>
      <FormItem prop="email" label="邮箱">
        <Input v-model="dataForm.email" placeholder="邮箱"></Input>
      </FormItem>
      <FormItem prop="mobile" label="手机号">
        <Input v-model="dataForm.mobile" placeholder="手机号"></Input>
      </FormItem>
      <FormItem prop="roleIdList" label="角色">
        <CheckboxGroup v-model="dataForm.roleIdList">
          <Checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.roleName }}</Checkbox>
        </CheckboxGroup>
      </FormItem>
      <FormItem prop="status" label="状态">
        <RadioGroup v-model="dataForm.status">
          <Radio :label=0>正常</Radio>
          <Radio :label=1>禁用</Radio>
        </RadioGroup>
      </FormItem>
    </Form>
    <div slot="footer">
      <Button @click="visible = false">取消</Button>
      <Button type="primary" @click="handleSubmit">保存</Button>
    </div>
  </Modal>
</template>
<script>
export default {
  data () {
    return {
      visible: false,
      roleList: [],
      dataForm: {
        userId: 0,
        username: '',
        password: '',
        email: '',
        mobile: '',
        roleIdList: [],
        status: 0
      },
      dataRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.userId = id || 0
      this.$http.request({
        url: '/sys/role/select',
        method: 'get'
      }).then(({ data }) => {
        this.roleList = data && data.code === 0 ? data.result : []
      }).then(() => {
        this.$nextTick(() => {
          this.visible = true
          this.$refs.dataForm.resetFields()
        })
      }).then(() => {
        if (this.dataForm.userId) {
          this.$http.request({
            url: `/sys/user/info/${this.dataForm.userId}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm = data.result
            }
          })
        }
      })
    },
    handleSubmit () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$http.request({
            url: `/sys/user/${!this.dataForm.userId ? 'save' : 'update'}`,
            method: 'post',
            data: this.dataForm,
            dataType: 'json'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.visible = false
              this.$Message.success({
                content: '操作成功',
                duration: 2,
                closable: true,
                onClose: () => {
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$Message.error(data.message)
            }
          })
        }
      })
    }
  }
}
</script>
