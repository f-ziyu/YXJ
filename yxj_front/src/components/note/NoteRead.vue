<template>
  <div>
    <div style="width: 100%; margin:0 auto; text-align:center">
      <h1>{{note.title}}</h1>
      <div>
        <span id="usernameSpan">作者: {{this.note.author.username.toString()}}</span>
        <span>创建时间: {{formatDateTime(note.createdTime)}}</span>
        <div style="text-align: left">
          <span style="margin-left: 30px">简介: </span><br/><br/>
          <span style="margin-left: 30px">&nbsp;&nbsp;&nbsp;&nbsp;{{note.describe}}</span>
        </div>
      </div>
      <el-divider style=""></el-divider>
      <div style="">
        <el-row>
          <el-col :span="bookmarkCol" >
            <bookmark ref="bookmark"></bookmark>
          </el-col>
          <el-col :span="noteCol"  style="margin-right:20px;width: 100%;">
            <el-card class="box-card note" shadow="never" style="margin-top: 35px;margin-bottom: 35px" v-model="note">
              <el-scrollbar style="height: 100%">
                <div v-html="note.contentHtml" class="text note-html markdown-body" style="min-height: 450px"></div>
              </el-scrollbar>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <div>
        <el-button style="display: none" id="editNoteButton" icon="el-icon-edit" @click="editNote()" circle></el-button>
        <el-button :type=collType icon="el-icon-star-off" @click="setCollection()" circle></el-button>
      </div>

      <div style="margin-top: 50px;text-align: left">
        <span style="margin-left: 50px;font-family: 'Microsoft YaHei';font-weight: bolder;font-size: 20px">发表评论</span><br>
        <div style="margin-top: 20px">
          <el-form ref="form" :model="comment" label-width="80x">
            <el-form-item label="">
              <el-input type="textarea" v-model="comment.commentBody" placeholder="你的评论"></el-input>
            </el-form-item>
            <el-form-item style="margin-top: 10px">
              <el-button type="primary" @click="onSubmit" style="margin-left: 40px">立即发表</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <el-divider style="margin-top: 50px"></el-divider><br>

      <div>
        <div v-for="(item,i) in comments" :key="i"  style='text-align: left' >
          <div slot="header" class="clearFix"  style='background: none'>
            <span style="float: left;margin-left: 100px;font-family: 'Microsoft YaHei';font-weight: bold">{{item.user.username}}</span>
            <span style="float: right; margin-right: 100px;font-size: 13px">评论时间:{{formatDateTime(item.createdTime)}}</span>
          </div><br>
          <div class="text item" style="margin-left: 50px;margin-top: 20px">
            {{item.commentBody}}
          </div><br>

          <el-tooltip :style=checkDelete(item.user.username) transition="0s" class="item" content="删除评论" placement="top-start">
            <el-button @click="deleteNoteComment(item.commentId)" style="float: right; padding: 0px 0;margin-right: 200px" type="text"><i class="el-icon-delete"></i></el-button>
          </el-tooltip>
          <el-divider></el-divider><br>
        </div>
      </div>
      <div style="height: 150px">
      </div>
    </div>
  </div>
</template>

<script>
    export default {
      name: "NoteRead",
      data(){
        return{
          note:{
            id: null,
            title:null,
            author: {
              username:'',
            },
            describe:null,
            noteType:null,
            contentHtml:null,
            isPublic:"0",
          },
          collType:'',

          quote:{
          },
          quoteLink:'',
          bookmarkStatus:true,
          quoteStatus:false,
          bookmarkCol:3,
          noteCol:21,
          quoteCol:0,
          comment: {
            commentId:null,
            noteId:null,
            user: {username:''},
            commentBody:'',
            createdTime:'',
          },
          comments:[]
        }
      },
      mounted() {
        if(this.$route.query.noteId){
          this.loadNote(this.$route.query.noteId)
        }

      },
      methods:{
        // 加载笔记
        loadNote(id){
          var _this = this
          this.axios.get('note/getNote',{
            params:{
              noteId:id
            }
          }).then(function (response) {
              if (response.status === 200){
                _this.note=response.data.object
                _this.isCollection(id)
                _this.checkEdit(response.data.object.author.username)
                _this.getAllNoteComment()
                document.getElementById("quoteArea").innerHTML=_this.note.contentHtml
                this.note.isPublic = response.data.object.isPublic+""
              }else {
                console.log(response.status)
              }
            })
            .catch(function (error) {
              console.log(error)
            })
        },
        // 携带笔记id跳转至编辑页面
        editNote(){
          this.$router.push({
            path:'note/edit',
            name:'NoteEdit',
            params:{
              noteId:this.note.id
            }
          })
        },
        // 查看收藏关系
        isCollection(id){
          var _this = this
          _this.axios.get('collection/getIsCollection',{
            params:{
              noteId:id,
              username:JSON.parse(localStorage.getItem("user")).username
            }
          }).then(function (response) {
            if (response.status === 200){
              if(response.data.object === true){
                _this.collType = 'success'
              }else {
                _this.collType = 'warning'
              }
            }else {
              console.log(response.status)
            }
          })
            .catch(function (error) {
              console.log(error)
            })
        },
        // 判断编辑按钮显示状态
        checkEdit(username){
          if(username===JSON.parse(localStorage.getItem("user")).username){
            document.getElementById('editNoteButton').style.display=''
          }
        },
        // 判断编辑按钮显示状态
        checkDelete(username){
          if(username===JSON.parse(localStorage.getItem("user")).username){

          }else {
            return 'display: none'
          }
        },
        // 改变收藏关系
        setCollection(){
          if(this.collType==='success'){
            var msg='确认取消收藏该笔记？'
            var msg_result='取消收藏成功'
          }else {
            var msg='确认收藏该笔记？'
            var msg_result='收藏成功'
          }
          var _this = this
          var id=_this.note.id
          _this.$confirm(msg,'提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=>{
            _this.axios.get('collection/setCollection',{
              params:{
                noteId:id,
                username:JSON.parse(localStorage.getItem("user")).username
              }
            }).then(function (response) {
                if(response.data.status === 200){
                  _this.$message({
                    type:'success',
                    message:msg_result
                  })
                  _this.isCollection(id);
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

        // 提交评论
        onSubmit() {
          this.comment.note=this.note;
          this.comment.user.username=JSON.parse(localStorage.getItem("user")).username
          this.addNoteComment(this.comment)
        },
        // 发表评论
        addNoteComment(comment){
          var _this = this
          _this.$confirm('确认发表评论？','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=>{
            _this.axios.post('comment/addNoteComment',comment)
              .then(function (response) {
                if(response.data.status === 200){
                  _this.$message({
                    type:'success',
                    message:'评论成功'
                  })
                  _this.getAllNoteComment();
                }
                else {
                  _this.$message({
                    type:"error",
                    message:'评论失败:  '+response.data.msg
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
        },
        // 删除评论
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
                _this.getAllNoteComment()
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
