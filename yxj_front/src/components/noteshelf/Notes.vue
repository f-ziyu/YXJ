<template>
  <div style="margin:0 auto;">
    <el-card v-for="(item,i) in notes" :key="i"  class="box-card">
      <div slot="header" class="clearFix">
        <span style="float: left;margin-left: 100px">{{item.title}}</span>
        <span style="float: bottom;margin-left: 20px;margin-bottom:2px;font-size: 12px">最近修改时间:{{item.lastModifiedTime}}</span>



        <el-button @click="updateNotePublicStatus(item.id,item.isPublic)" style="float: right;margin-right: 20px; padding: 5px 0;" type="text">
          <el-radio v-model="item.isPublic.toString()" label="1" >公开</el-radio>
        </el-button>
        <el-button @click="updateNoteType(item.id)" style="float: right; padding: 5px 0;margin-right: 30px" type="text">
          <span>{{item.noteType?item.noteType.name:"暂无类别"}}</span>
        </el-button>
        <el-tooltip transition="0s" class="item" content="删除笔记" placement="top-start">
          <el-button @click="deleteNote(item.id)" style="float: right; padding: 5px 0;margin-right: 30px" type="text"><i class="el-icon-delete"></i></el-button>
        </el-tooltip>
        <el-tooltip transition="0s" class="item" content="编辑笔记" placement="top-start">
          <el-button @click="editNote(item.id)" style="float: right; padding: 5px 0" type="text"><i class="el-icon-edit"></i></el-button>
        </el-tooltip>
        <el-tooltip transition="0s" class="item" content="查看笔记" placement="top">
          <el-button @click="readNote(item.id)" style="float: right; padding: 5px 0;" type="text"><i class="el-icon-reading"></i></el-button>
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
        },
      mounted() {
        var _this = this
        var username = JSON.parse(localStorage.getItem("user")).username;
        this.axios.get("/note/getNotes",{
          params:{
            'username':username
          }
        })
        .then(function (response) {
          if(response.status === 200){
            _this.notes = response.data.object
          }
        })
        .catch(function (error) {
          console.log(error)
        })

      },
      methods:{
        // 携带笔记id跳转至编辑页面
        editNote(id){
          this.$router.push({
            path:'note/edit',
            name:'NoteEdit',
            params:{
              noteId:id
            }
          })
        },
        // 携带笔记id跳转至阅读页面
        readNote(id){
          this.$router.push({
            path:'note/reading',
            name:'NoteRead',
            params:{
              noteId:id
            }
          })
        },
        // 更新笔记公开状态
        updateNotePublicStatus(noteId,isPublic){
          console.log("更新状态"+noteId)
          var _this = this
          var msgText = ''
          var resText = ''
          if(isPublic === 1){
            msgText = "确认隐藏该笔记？",
            resText = '隐藏笔记'
          }else {
            msgText = "确认公开该笔记？"
            resText = '公开笔记'
          }
          _this.$confirm(msgText,'提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=> {
            _this.axios.get("/note/updatePublicValue",{
              params:{
                noteId:noteId
              }
            }).then(function (response) {
                if(response.status === 200){
                  _this.$message({
                    type: 'success',
                    message: resText+"完成"
                  })
                  location.reload();
                }else {
                  _this.$message({
                    type: "error",
                    message: resText+'失败:'+response.data.msg
                  })
                  console.log(response)
                }
              })
              .catch(function (error) {
                console.log(error)
              })
          })
        },
        // 删除笔记
        deleteNote(noteId){
          var _this = this
          _this.$confirm('确认删除该笔记？','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=> {
            _this.axios.get("/note/moveNoteInRecycler", {
              params: {
                noteId: noteId
              }
            }).then(function (response) {
              if(response.status === 200) {
                _this.$message({
                  type: 'success',
                  message: '删除成功'
                })
                location.reload();
              }else {
                _this.$message({
                  type: "error",
                  message: '删除失败:'+response.data.msg
                })
                console.log(response)
              }
            }).catch(function (error) {
                console.log(error)
              })
          })
        },
        // 修改笔记的类型
        updateNoteType(noteId){

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
    margin-left: 3%;
    margin-top: 20px;

    width: 90%;
  }
</style>
