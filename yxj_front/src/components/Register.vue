<template>
  <div  style="margin-top:175px;margin-left: 35%;background-repeat: no-repeat;min-height: 600px;
      /*  z-index:1; */
      position: fixed;
      right: 20%;
      background-image: url('static/images/hua_01.png');background-size: 45%;">
    <div class="el-container">
      <div>
        <el-form ref="form" :model="registerForm" label-width="80px">
          <el-form-item>
            <h2>注册</h2>
          </el-form-item>
          <el-form-item>
            <el-input placeholder="请输入用户名" v-model="registerForm.username"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input placeholder="请输入密码" v-model="registerForm.password" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-input placeholder="请确认密码" v-model="registerForm.password_sure" show-password></el-input>
          </el-form-item>

<!--          <el-form-item>-->
<!--            <div id="verify_image" @click="getVerify">-->
<!--              <img id="imgVerify" src="api/login/loginValidateCode" alt="更换验证码" height="36" width="170" >-->
<!--            </div>-->
<!--            <el-input id="imgVerifyCode_input" placeholder="请输入图形验证码" v-model="imgVerifyCode"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary"  plain @click="checkVerify">验证</el-button>-->
<!--            <el-button :type=imgVerify_status icon="el-icon-check" circle disabled></el-button>-->
<!--          </el-form-item>-->

          <el-form-item>
            <el-button type="primary" @click="onSubmit">立即创建</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>


</template>

<script>
  export default {
    name: "Register",
    data() {
      return {
        registerForm:{
          username:'',
          password:'',
          password_sure:''
        },
        imgVerifyCode:'',
        imgVerify_status:'',
        Verify_status:true
      }
    },
    methods: {
      onSubmit(){
        var _this=this
        if(this.Verify_status === false){
          _this.$message({
            type: "warning",
            message: '请先通过图形验证'
          })
          return 0
        }
        if(this.registerForm.username!='' && this.registerForm.password!=''){
          if(this.registerForm.password_sure === this.registerForm.password){
            this.axios.post("/user/register",{
              username : this.registerForm.username,
              password : this.registerForm.password
            })
              .then(function (response) {
                if(response.data.status === 200){
                  _this.$message({
                    type: 'success',
                    message: '注册成功'
                  })
                  _this.$router.push({path:'/login'})
                }else {
                  _this.$message({
                    type: "error",
                    message: '失败:'+response.data.msg
                  })
                  _this.Verify_status=false
                  _this.imgVerify_status='error'
                  document.getElementById('imgVerifyCode_input').value=''
                  _this.getVerify()
                }
              })
              .catch(function(error) {
                console.log(error)
              })
          } else {
            _this.$confirm('两次输入密码不匹配','提示',{
              confirmButtonText:'确认',
              cancelButtonText:'取消',
              type:"info"
            }).then(()=> {})
          }
        } else {
            _this.$confirm('用户名和密码不能为空','提示',{
              confirmButtonText:'确认',
              cancelButtonText:'取消',
              type:"info"
            }).then(()=> {})
          }
        },

      getVerify() {
        this.axios.post("/login/loginValidateCode")
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
