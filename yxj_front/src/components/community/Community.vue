<template>
  <div style="margin:0 auto;">
    <div style="min-height: 500px" v-loading="loading">
      <el-card v-for="(item,i) in notes" :key="i"  class="box-card" @click="readNote(item.id)">
        <div slot="header" class="clearFix" >
          <span style="float: left;margin-left: 100px">{{item.title}}</span>

          <el-button style="float: right; padding: 5px 0;margin-right: 50px" type="text">
            <el-dropdown>
              <span class="el-dropdown-link">
                {{item.noteType?item.noteType.name:"暂无类别"}}
              </span>
            </el-dropdown>
          </el-button>

          <el-tooltip transition="0s" class="item" content="查看笔记" placement="top">
            <el-button @click="readNote(item.id)" style="float: right; padding: 5px 0;margin-right: 100px" type="text"><i class="el-icon-reading"></i></el-button>
          </el-tooltip>

          <span style="float: right; margin-right: 100px;font-size: 13px">发布时间:{{formatDateTime(item.createdTime)}}</span>
          <span style="float: right; margin-right: 100px;font-size: 15px">作者:{{item.author.username}}</span>
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

    <div class="block" style="float: bottom;margin-top: 50px;margin-bottom: 50px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>



  </div>
</template>

<script>
    export default {
        name: "Community",
      data(){
        return{
          notes:[],
          noteTypes:[],
          dialogVisible: false,
          collType:'',
          currentPage:1,
          total:0,
          pageSize:5,
          loading:false,
        }
      },
      mounted() {
        this.getPublicNotesTotalSize();
      },
      methods:{
        handleSizeChange(val) {
          this.pageSize=val;
          this.getPublicNotes()
        },
        handleCurrentChange(val) {
          this.currentPage=val
          this.getPublicNotes()
        },
        // 分页获得所有公开笔记
        getPublicNotes(){
          var _this = this
          _this.loading=true
          this.axios.get("/note/pageable/getPublicNotes",{
            params:{
              'currentPage':this.currentPage,
              'pageSize':this.pageSize
            }
          }).then(function (response) {
            if(response.data.status === 200){
              _this.notes = response.data.object
              _this.loading=false
            }
          })
            .catch(function(error) {
              console.log(error)
            })
        },
        // 获得所有公开笔记总数
        getPublicNotesTotalSize(){
          var _this = this
          this.axios.get("/note/pageable/getPublicNotesTotalSize",{
          }).then(function (response) {
            if(response.data.status === 200){
              _this.total = response.data.object
              _this.getPublicNotes();
            }
          })
            .catch(function(error) {
              console.log(error)
            })
        },
        // 携带笔记id跳转至阅读页面
        readNote(id){
          this.$router.push({
            path:'note/reading',
            name:'NoteRead',
            query:{
              noteId:id
            }
          })
        },

        // 时间戳转换日期格式方法
        formatDateTime(value) {
          if (value == null) {
            return ''
          } else {
            const date = new Date(value)
            const y = date.getFullYear() // 年
            let MM = date.getMonth() + 1 // 月
            MM = MM < 10 ? ('0' + MM) : MM
            let d = date.getDate() // 日
            d = d < 10 ? ('0' + d) : d
            let h = date.getHours() // 时
            h = h < 10 ? ('0' + h) : h
            let m = date.getMinutes()// 分
            m = m < 10 ? ('0' + m) : m
            let s = date.getSeconds()// 秒
            s = s < 10 ? ('0' + s) : s
            return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s
          }
        },
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
    margin-left: 2%;
    margin-right: 2%;
    margin-top: 20px;
    /*width: 1%;*/
  }

  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
