<template>
  <div style="margin:0 auto;">
    <el-card v-for="(item,i) in notes" :key="i"  class="box-card">
      <div slot="header" class="clearFix">
        <span style="float: left;margin-left: 100px">{{item.title}}</span>
        <span style="float: bottom;margin-left: 200px;margin-bottom:2px;font-size: 12px">最近修改时间:{{item.lastModifiedTime}}</span>
        <el-tooltip transition="0s" class="item" content="删除笔记" placement="top-start">
          <el-button style="float: right; padding: 5px 0;margin-right: 70px" type="text"><i class="el-icon-delete"></i></el-button>
        </el-tooltip>
        <el-tooltip transition="0s" class="item" content="编辑笔记" placement="top-start">
          <el-button style="float: right; padding: 5px 0;margin-right: 10px" type="text"><i class="el-icon-edit"></i></el-button>
        </el-tooltip>
        <el-tooltip transition="0s" class="item" content="查看笔记" placement="top">
          <el-button style="float: right; padding: 5px 0;" type="text"><i class="el-icon-reading"></i></el-button>
        </el-tooltip>
      </div>
      <div class="text item" style="
        display: -webkit-box;
        overflow: hidden;
        white-space: normal !important;
        text-overflow: ellipsis;
        word-wrap: break-word;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;">
        {{item.describe}}
      </div>
    </el-card>
  </div>
</template>

<script>
    export default {
        name: "Notes",
        data(){
          return{
            notes:[]
          }
        }
    }
</script>

<style scoped>
  .text {
    font-size: 14px;
    float: left;
  }

  .item {
    margin-bottom: 18px;
  }
  .clearFix{
    height: 20px;
  }

  .clearFix:before,
  .clearFix:after {
    display: table;
    content: "";
  }
  .clearFix:after {
    clear: both
  }

  .box-card {
    margin-left: 3%;
    margin-top: 20px;

    width: 90%;
  }
</style>
