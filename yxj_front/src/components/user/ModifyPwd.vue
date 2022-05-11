<template>
  <div>
    <div class="el-container" style="margin-top:75px;margin-left: 500px;margin-right: 500px">
      <div>
        <el-form ref="form" :model="modifyPwdForm" label-width="80px">
          <el-form-item>
            <h2>修改密码</h2>
          </el-form-item>
          <el-form-item>
            <el-input placeholder="请输入旧密码" v-model="modifyPwdForm.oldPassword" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-input placeholder="请输入新密码" v-model="modifyPwdForm.newPassword" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-input placeholder="请确认新密码" v-model="modifyPwdForm.newPassword_sure" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="modifyPwd()">立即修改</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>

</template>

<script>
    export default {
        name: "modifyPwd",
      data() {
        return {
          modifyPwdForm:{
            oldPassword:'',
            newPassword:'',
            newPassword_sure:''
          }
        }
      },
      methods: {
        modifyPwd() {
          console.log('修改密码')
          var _this = this
          _this.$confirm('确认修改密码？', '提示', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: "info"
          }).then(() => {
            if (this.modifyPwdForm.oldPassword != '') {
              if (this.modifyPwdForm.newPassword === this.modifyPwdForm.newPassword_sure) {
                if (this.modifyPwdForm.newPassword != '') {
                  if (this.modifyPwdForm.newPassword != this.modifyPwdForm.oldPassword) {
                    var user = {
                      username: JSON.parse(localStorage.getItem("user")).username,
                      password: this.modifyPwdForm.oldPassword,
                      salt: this.modifyPwdForm.newPassword
                    }
                    this.axios.post("/user/modifyPassword", user
                    ).then(function (response) {
                      if (response.data.status === 200) {
                        _this.$message({
                          type: 'success',
                          message: '修改成功'
                        })
                        _this.$router.replace('/login')
                      } else {
                        _this.$message({
                          type: "error",
                          message: '修改失败:' + response.data.object
                        })
                        console.log(response)
                      }
                    })
                      .catch(function (error) {
                        console.log(error)
                      })
                  } else {
                    _this.$confirm('新旧密码一致', '提示', {
                      confirmButtonText: '确认',
                      cancelButtonText: '取消',
                      type: "info"
                    }).then(() => {
                    })
                  }
                } else {
                  _this.$confirm('新密码不能为空', '提示', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: "info"
                  }).then(() => {
                  })
                }
              } else {
                _this.$confirm('两次输入密码不匹配', '提示', {
                  confirmButtonText: '确认',
                  cancelButtonText: '取消',
                  type: "info"
                }).then(() => {
                })
              }
            } else {
              _this.$confirm('旧密码不能为空', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: "info"
              }).then(() => {
              })
            }
          })
        }
      }
    }
</script>

<style scoped>

</style>
