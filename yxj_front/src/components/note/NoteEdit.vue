<template>
  <div>
    <div style="height: 100px;">
      <div>
        <el-button style="float: left" type="text"><i @click="goBack()" class="el-icon-d-arrow-left">退出编辑</i></el-button>
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
            contentMd:null,
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
        // 加载笔记
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
              _this.note.isPublic = _this.note.isPublic.toString()
            }else {
              console.log(response.status)
            }
          })
          .catch(function (error) {
            console.log(error)
          })
        },
        saveNote(value, render){
          if(this.note.id===null){
            this.addNote(value, render)
          }else {
            this.updateNote(value, render)
          }
        },
        // 添加笔记
        addNote(value, render){
          var _this = this
          _this.$confirm('确认添加笔记？','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=>{
            this.note.isPublic=parseInt(this.note.isPublic)
            this.note.contentMd=value
            this.note.contentHtml=render
            _this.axios.post('/note/addNote',this.note)
              .then(function (response) {
                if(response.data.status === 200){
                  _this.$message({
                    type:'success',
                    message:'添加成功'
                  })
                  _this.$router.push({path:'/notes'})
                }
                else {
                  _this.$message({
                    type:"error",
                    message:'添加失败:  '+response.data.msg
                  })
                  console.log(response)
                }
              })
              .then(function (error) {
                console.log(error)
              })
          })
        },
        // 更新笔记
        updateNote(value, render){
          var _this = this
          _this.$confirm('确认保存修改？','提示',{
            confirmButtonText:'确认',
            cancelButtonText:'取消',
            type:"info"
          }).then(()=>{
            this.note.isPublic=parseInt(this.note.isPublic)
            this.note.contentMd=value
            this.note.contentHtml=render
            _this.axios.post('/note/updateNote',this.note)
              .then(function (response) {
                if(response.data.status === 200){
                  _this.$message({
                    type:'success',
                    message:'修改成功'
                  })
                  _this.$router.push({path:'/notes'})
                }
                else {
                  _this.$message({
                    type:"error",
                    message:'修改失败: '+response.data.msg
                  })
                  console.log(response)
                }
              })
              .then(function (error) {
                console.log(error)
              })
          })
        },

        // 退出编辑
        goBack(){
          var _this = this
          _this.$confirm('退出后不会保存当前修改,是否退出？','提示',{
            confirmButtonText:'退出',
            cancelButtonText:'取消',
            type:"warning"
          }).then(()=>{
            history.back(-1)
          })
        },


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
