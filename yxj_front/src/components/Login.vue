<template>
  <div class="el-container">
    <div class="form-boby">
      <h2>登录</h2>
      <el-form ref="form" :model="loginForm" label-width="80px">
        <el-form-item>
          <el-input placeholder="请输入用户名" v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input placeholder="请输入密码" v-model="loginForm.password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        loginForm:{
          username:'ziyu01',
          password:'123456',
        }
      }
    },
    methods: {
      onSubmit(){
        var _this = this
        this.axios.post("/login",{
          username : this.loginForm.username,
          password : this.loginForm.password
        })
        .then(function (response) {
          console.log(response.data.status)
          if(response.data.status === 200){
            _this.$store.commit('login',response.data.object)
            _this.$router.push({path:'/'})
          }
          else {
            alert("账号或密码错误")
          }

        })
        .catch(function (error) {
          console.log(error)
        })
      }
    }
  }
</script>

<style scoped>

</style>
