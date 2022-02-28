<template>
  <div>
    <div style="height: 100px;">
      <div>
        <el-button style="float: left" type="text"><i href="javascript:history.go(-1)"  class="el-icon-d-arrow-left">退出编辑</i></el-button>
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
            id: null,
            title:null,
            author:null,
            describe:null,
            noteType:null,
            contentHtml:null,
            contentMd:null,
            createdTime:null,
            lastModifiedTime:null,
            isPublic:"0",
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
          this.axios.get('note/getNote',{
            params:{
              noteId:id
            }

          })
          .then(function (response) {
            if (response.status === 200){
              _this.note=response.data.object
              this.note.isPublic = response.data.object.isPublic.toString()
            }else {
              console.log(response.status)
            }
          })
          .catch(function (error) {
            console.log(error)
          })
        },
        saveNote(){
          if(this.note.id===0){
            this.addNote()
          }else {
            this.updateNote()
          }
        },
        addNote(){
          console.log("添加笔记")
          this.note.isPublic=parseInt(this.note.isPublic)
          this.note.author=localStorage.getItem("user")
          this.axios.post('/note/addNote',this.note)
            .then(function (response) {
              console.log(response)
            })
            .then(function (error) {
              console.log(error)
            })
        },
        updateNote(){
          console.log("修改笔记")
          this.note.isPublic=parseInt(this.note.isPublic)
          this.axios.post('/note/updateNote',this.note)
          .then(function (response) {
            console.log(response)
          })
          .then(function (error) {
            console.log(error)
          })
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
