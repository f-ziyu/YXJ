<template>
  <div style="margin-top:175px;margin-left: 35%;background-repeat: no-repeat;min-height: 600px;
      /*  z-index:1; */
      position: fixed;
      right: 20%;
      background-image: url('static/images/hua_01.png');background-size: 45%;">
    <div style=" margin:0 auto;align:center;" >
      <div class="el-container">
        <div class="form-boby">
          <el-form ref="form" :model="loginForm" label-width="80px">
            <el-form-item>
              <h2>登录</h2>
            </el-form-item>
            <el-form-item>
              <el-input placeholder="请输入用户名" v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input placeholder="请输入密码" v-model="loginForm.password" show-password></el-input>
            </el-form-item>
            <br>
<!--            <el-form-item>-->
<!--              <div id="verify_image" @click="getVerify">-->
<!--                <img id="imgVerify" src="api/login/loginValidateCode" alt="更换验证码" height="36" width="170" >-->
<!--              </div>-->
<!--              <el-input id="imgVerifyCode_input" placeholder="请输入图形验证码" v-model="imgVerifyCode"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item>-->
<!--              <el-button type="primary"  plain @click="checkVerify">验证</el-button>-->
<!--              <el-button :type=imgVerify_status icon="el-icon-check" circle disabled></el-button>-->
<!--            </el-form-item>-->




            <el-form-item>
              <el-button id="login_btu" type="primary" @click="onSubmit">登录</el-button>
              <el-button type="primary" @click="register">注册</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>


</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        loginForm:{
          username:'',
          password:'',
        },
        imgVerifyCode:'',
        imgVerify_status:'',
        Verify_status:true

      }
    },
    methods: {
      onSubmit(){
        var _this = this
        if(this.Verify_status === false){
          _this.$message({
            type: "warning",
            message: '请先通过图形验证'
          })
          return 0
        }
        if(this.loginForm.username!='' && this.loginForm.password!=''){
          this.axios.post("/user/login",{
            username : this.loginForm.username,
            password : this.loginForm.password
          })
            .then(function (response) {
              console.log(response.data.status)
              if(response.data.status === 200){
                _this.$store.commit('login',response.data.object)
                _this.$router.push({path:'/'})
              }else {
                _this.$message({
                  type: "error",
                  message: '登录失败:'+response.data.object
                })
                _this.Verify_status=false
                _this.imgVerify_status='error'
                document.getElementById('imgVerifyCode_input').value=''
                _this.getVerify()
                console.log(response)
              }
            })
            .catch(function (error) {
              console.log(error)
            })
          } else {
          _this.$confirm('用户和密码不能为空','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=> {})
        }

      },
      register(){
        this.$router.push({
          path:'register',
          name:'Register'

        })
      },
      getVerify() {
        this.axios.get("/login/loginValidateCode")
          .then(function (response) {
          console.log(response.data.status)
            if (response.status===200){
              document.getElementById('imgVerify').src='api/login/loginValidateCode?'+Math.random()
            }
        })
          .catch(function (error) {
            console.log(error)
          })
       },

    checkVerify() {
        let value = this.imgVerifyCode
        var _this = this
        if (value.length != 4) {
          _this.$message({
            type:'error',
            message:'验证码长度不对，重新输入'
          })
          return 0
        }
        this.axios.get("/login/checkVerify", {
          params: {
            validateCode:value
          }
        }).then(function (response) {
            if(response.data.status === 200){
              _this.$message({
                type:'success',
                message:'验证通过'
              })
              _this.Verify_status=true
              _this.imgVerify_status='success'
            }else {
              _this.$message({
                type: "error",
                message: response.data.msg
              })
              _this.Verify_status=false
              _this.imgVerify_status='error'
              document.getElementById('imgVerifyCode_input').value=''
              _this.getVerify()
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
