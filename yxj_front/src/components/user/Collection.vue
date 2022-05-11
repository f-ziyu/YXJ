<template>
  <div style="margin:0 auto;">
    <h2>我的收藏</h2>
    <div id="dis_wu" style="display: none">
      <h3>无</h3>
    </div>

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
        <el-tooltip transition="0s" class="item" content="移除收藏" placement="top-start">
          <el-button @click="removeNoteCollection(item.id)" style="float: right; padding: 5px 0;margin-right: 100px" type="text"><i class="el-icon-delete"></i></el-button>
        </el-tooltip>
        <el-tooltip transition="0s" class="item" content="查看笔记" placement="top">
          <el-button @click="readNote(item.id)" style="float: right; padding: 5px 0;margin-right: 50px" type="text"><i class="el-icon-reading"></i></el-button>
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
</template>

<script>
    export default {
        name: "Collection",
      data(){
        return{
          notes:[],
          noteTypes:[],
          dialogVisible: false
        }
      },
      mounted() {
        this.getCollectionNotesByUsername(JSON.parse(localStorage.getItem("user")).username);
      },
      methods:{
        // 获得收藏笔记
        getCollectionNotesByUsername(){
          var _this = this
          this.axios.get("/collection/getUserCollectionNotes",{
            params:{
              username:JSON.parse(localStorage.getItem("user")).username
            }
          }).then(function (response) {
            if(response.data.status === 200){
              _this.notes = response.data.object
              if (response.data.object.length===0){
                document.getElementById('dis_wu').style.display=''
              }
            }
          })
            .catch(function(error) {
              console.log(error)
            })

        },
        // 移除收藏
        removeNoteCollection(noteId){
          var _this = this
          _this.$confirm('确认取消收藏该笔记？','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=>{
            _this.axios.get('collection/setCollection',{
              params:{
                noteId:noteId,
                username:JSON.parse(localStorage.getItem("user")).username
              }
            }).then(function (response) {
              if(response.data.status === 200){
                _this.$message({
                  type:'success',
                  message:'取消收藏成功'
                })
                _this.getCollectionNotesByUsername(JSON.parse(localStorage.getItem("user")).username);
              }
              else {
                _this.$message({
                  type:"error",
                  message:'修改失败:  '+response.data.msg
                })
                console.log(response)
              }
            })
              .then(function (error) {
                console.log(error)
              })
          })
        },
        // 携带笔记id跳转至阅读页面
        readNote(id){
          var new_win = this.$router.resolve({
            path:'note/reading',
            name:'NoteRead',
            query:{
              noteId:id
            }
          })
          window.open(new_win.href,'_blank')
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
