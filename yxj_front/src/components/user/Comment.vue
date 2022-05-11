<template>

  <div style="margin:0 auto;">
    <h2>我的评论</h2>
    <div id="dis_wu" style="display: none">
      <h3>无</h3>
    </div>
    <div v-for="(item,i) in comments" :key="i"  style='text-align: left' >
      <el-divider></el-divider><br>
      <div slot="header" class="clearFix"  style='background: none'>
        <span @click="readNote(item.note.id)" style="float: left;margin-left: 100px;font-size: 13px;">
          <span style="float:left;font-size: 13px;color: #999999">你在{{formatDateTime(item.createdTime)}}评论了</span>
          {{item.note.title}}</span>

      </div><br>
      <div class="text item" style="margin-left: 50px;margin-top: 20px">
        {{item.commentBody}}
      </div><br>
      <el-tooltip transition="0s" class="item" content="删除评论" placement="top-start">
        <el-button @click="deleteNoteComment(item.commentId)" style="float: right; padding: 0px 0;margin-right: 200px" type="text"><i class="el-icon-delete"></i></el-button>
      </el-tooltip>
    </div>
    <el-divider></el-divider><br>
  </div>
</template>

<script>
    export default {
        name: "Comment",
      data(){
        return{
          comments:[]
        }
      },
      mounted() {
        this.loadComments(JSON.parse(localStorage.getItem("user")).username)
      },
      methods:{
        // 加载用户评论
        loadComments(username){
          var _this = this
          this.axios.get('/comment/getAllCommentByUsername',{
            params:{
              username:username
            }
          }).then(function (response) {
            if (response.status === 200){
              _this.comments=response.data.object
              if (response.data.object.length===0){
                document.getElementById('dis_wu').style.display=''
              }
            }else {
              console.log(response.status)
            }
          })
            .catch(function (error) {
              console.log(error)
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

        // 删除
        deleteNoteComment(commentId){
          var _this = this
          _this.$confirm('确认删除评论？','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=>{
            _this.axios.get('/comment/deleteComment',{
              params:{
                commentId:commentId
            }}).then(function (response) {
                if(response.data.status === 200){
                  _this.$message({
                    type:'success',
                    message:'删除成功'
                  })
                  _this.loadComments(JSON.parse(localStorage.getItem("user")).username)
                }
                else {
                  _this.$message({
                    type:"error",
                    message:'删除失败:  '+response.data.msg
                  })
                  console.log(response)
                }
              })
              .then(function (error) {
                console.log(error)
              })
          })
        },
        // 获得该笔记下的所有评论
        getAllNoteComment(){
          var _this = this
          _this.axios.get("/comment/getAllCommentByNoteId",{
            params:{
              'noteId': parseInt(this.note.id)
            }
          }).then(function (response) {
            if(response.status === 200){
              _this.comments=response.data.object
            }else {
              console.log(response.status)
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
  .note {
    border: 0px solid #EBEEF5!important;
    width: 95%;
    text-align: left;
    margin: auto;
  }

</style>
