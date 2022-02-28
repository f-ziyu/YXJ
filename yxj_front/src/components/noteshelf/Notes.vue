<template>
  <div style="margin:0 auto;">
    <el-card v-for="(item,i) in notes" :key="i"  class="box-card">
      <div slot="header" class="clearFix">
        <span style="float: left;margin-left: 100px">{{item.title}}</span>

        <div style="float: right;margin-right: 40px">
          <el-radio v-model="item.isPublic.toString()" label="1" @click="updateNotePublicStatus(item.id)" >公开</el-radio>
        </div>
        <span style="float: bottom;margin-left: 20px;margin-bottom:2px;font-size: 12px">最近修改时间:{{item.lastModifiedTime}}</span>
        <el-tooltip transition="0s" class="item" content="删除笔记" placement="top-start">
          <el-button style="float: right; padding: 5px 0;margin-right: 70px" type="text"><i class="el-icon-delete"></i></el-button>
        </el-tooltip>
        <el-tooltip transition="0s" class="item" content="编辑笔记" placement="top-start">
          <el-button @click="editNote(item.id)" style="float: right; padding: 5px 0;margin-right: 10px" type="text"><i class="el-icon-edit"></i></el-button>
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
        editNote(id){
          this.$router.push({
            path:'note/edit',
            name:'NoteEdit',
            params:{
              noteId:id
            }
          })
        },
        updateNotePublicStatus(noteId){
          this.axios.get("/note/updatePublicStatus",{
            params:{
              noteId:noteId
            }
          })
            .then(function (response) {
              if(response.status === 200){
                location.reload();
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
