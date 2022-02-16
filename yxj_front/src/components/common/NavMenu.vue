<template>
  <div>
    <el-menu
      :default-active="'/home'"
      router
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect">
      <el-menu-item v-for="(item,i) in navList" :index="item.url":key="i">
        {{ item.name }}
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
    export default {
        name: "NavMenu",
        data() {
          return {
            navList:[
              {name:'首页',url:'/'},
              {name:'写笔记',url:'/note/edit'},
              {name:'笔记库',url:'/notes'},
              {name:'社区',url:'/community'},
              {name:'个人中心',url:'/user'}
            ],
          };
        },
        methods: {
          handleSelect(key, keyPath) {
            //console.log(key, keyPath);
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

</style>
