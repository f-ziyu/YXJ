<template>
  <div>
    <div style="height: 100px;">
      <div>
        <el-button style="float: left" type="text"><i  class="el-icon-d-arrow-left">退出编辑</i></el-button>
        <input class="edit-title" v-model="note.title" placeholder="请输入笔记题目"></input>

        <div style="float: right;margin-right: 40px">
          <el-radio v-model="note.isPublic" label="1" >公开</el-radio>
          <el-radio v-model="note.isPublic" label="0" >私密</el-radio>
        </div>
      </div>

      <el-input
        type="textarea"
        :rows="2"
        placeholder="请输入笔记相关表述"
        v-model="note.describe">
      </el-input>

    </div>
    <div style="float: top">
      <mavon-editor
        style="min-height: 900px"
        v-model="note.contentMd"
        @save = "saveNote"
      ></mavon-editor>
    </div>
  </div>
</template>

<script>
    export default {
      name: "noteEdit",
      data(){
        return{
          note:{
            id: 0,
            title:'',
            describe:'',
            noteType:'',
            contentHtml:'',
            contentMd:'',
            createdTime:'',
            lastModifiedTime:'',
            isPublic:"1",
          }
        }
      },
      mounted() {
        if(this.$route.params.noteId){
          this.loadNote(this.$route.params.noteId)
        }
      },
      methods:{
        loadNote(id){
          var _this = this
          this.axios.get('note/getNoteById/'+id.toString())
          .then(function (response) {
            if (response.status === 200){
              _this.note=response.data.object
              _this.note.isPublic = _this.note.isPublic.toString()
            }else {
              console.log(response.status)
            }
          })
          .catch(function (error) {
            console.log(error.toString())
          })
        },
        saveNote(){
          if(this.note.id===0){
            console.log("新增笔记")
          }else {
            console.log("添加笔记：笔记id为"+this.note.id)
          }
        }
      }
    }
</script>

<style scoped>
  .edit-title{
    width: 200px;
    border: none;
    font-size: 20px;
    font-family: "Microsoft YaHei";
    text-align: center;
    outline:none;
  }

</style>
