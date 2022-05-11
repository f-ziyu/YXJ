<template>
  <div>
    <el-menu
      :default-active="'/home'"
      router
      class="el-menu-demo"
      mode="horizontal"
      menu-trigger="click"
      @select="handleSelect">
      <el-menu-item v-for="(item,i) in navList" :index="item.url":key="i">
        {{ item.name }}
      </el-menu-item>
      <el-submenu index="2" style="float: right;padding-right: 20px">
        <template slot="title">{{userFlag.name}}</template>
        <el-menu-item style="left: 25px;font-size: 16px;" v-for="(item,i) in userFlag.menuList" :index="item.url" :key="i">{{item.name}}</el-menu-item>
        <el-menu-item id="logout" style="left: 25px;font-size: 16px;color: #dd6161" @click="logout">退出登录</el-menu-item>
      </el-submenu>
      <div style="margin-top:8px;float:left;padding-left: 150px">
        <el-button icon="el-icon-search" style="border: none" @click="searchNote()"></el-button>
      </div>
    </el-menu>
  </div>
</template>

<script>
    export default {
        name: "NavMenu",
        data() {
          return {
            isLogin:'none',
            userFlag:{
              name:'',
              menuList:[]
            },
            navList:[
              // {name:'首页',url:'/'},
              {name:'写笔记',url:'/note/edit'},
              {name:'笔记库',url:'/notes'},
              {name:'社区',url:'/community'},
            ],
          };
        },
        mounted() {
          if(window.localStorage.getItem("user")!=null){
            this.userFlag.name=JSON.parse(window.localStorage.getItem("user")).username;
            this.userFlag.menuList=[
              {url:'/user/collection',name:'我的收藏'},
              {url:'/user/comment',name:'我的评论'},
              {url:'/user/recycle',name:'回收站'},
              {url:'/user/modifyPwd',name:'修改密码'},
            ]
            document.getElementById('logout').style.display=''
          }
          else {
            this.userFlag.name = '未登录';
            this.userFlag.menuList=[
              {url:'/login',name:'登录'},
              {url:'/register',name:'注册'},
            ]
            document.getElementById('logout').style.display='none'

          }

        },
      methods: {
          handleSelect(key, keyPath) {
            //console.log(key, keyPath);
          },
        // 携带笔记id跳转至编辑页面
        searchNote(){
          this.$router.push({
            path:'note/search',
            name:'Search',
          })
        },
          logout(){
            var _this = this
            _this.$confirm('确认退出登录？','提示',{
              confirmButtonText:'确认',
              cancelButtonText:'取消',
              type:"info"
            }).then(()=> {
              var _this = this
              this.axios.get('/logout')
                .then(function (response) {
                  if (response.data.status === 200) {
                    _this.$store.commit('logout');
                    _this.$router.replace('/login')
                  }
                })
                .catch(function (error) {
                  console.log(error)
                })
            })
          }
        }
    }

</script>

<style>
  .nav-bar{
    position: fixed;
    width: 100%;
    left: 20px;
    top: 10px;
  }
  .el-menu-item{
    position: center;
    font-size: 18px;
    padding-left: 30px;
    padding-right: 30px;
    font-weight: bolder;
    left: 120px;
  }
  #search input {

    width: 270px;

    height: 35px;

    border: 0;                          //去掉input边框

  font-size: 14px;

    outline: none;

    background-color: rgba(32, 74, 102, 0);

    color: white;

    font-size: 16px;

    margin: 0 10px;
  }


  #search{

    width: 320px;

    height: 35px;

    border: 1px solid white;

    border-radius: 30px;                    //把边框设计成半圆

  display:flex;

    margin-top:30px;
  }


  #search a{

    margin-top:5px;

    margin-right:20px;

  }


  li{

    margin-top:10px;

    font-size: large;

    list-style: none;
  }


  li a{

    text-decoration: none;

    color:white;

  }



    li a:hover{

      font-size: x-large;

      color:blue;

      font-weight: bolder;
    }
</style>
