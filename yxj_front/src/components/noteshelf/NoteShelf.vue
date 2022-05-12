<template>
  <div style="height: 100%">
    <div class="noteTypeCss">
      <NoteTypeBar @noteTypeSelect="getUserNotesSize()" ref="noteTypeBar"></NoteTypeBar>
    </div>
    <div class="noteCss" v-loading="loading" style="background-repeat: no-repeat; text-align: center;
      /*  z-index:1; */
      position: fixed;
      background-image: url('static/images/mo_02.png');background-size: 90%;min-height: 700px">
      <div style="margin-bottom: 50px;min-height: 650px">
      <Notes ref="notes"></Notes>
      </div>

      <div class="block" style="font-weight: bold;font-family: 幼圆;padding-bottom: 50px">
        <el-pagination style=""
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
  </div>
</template>

<script>
  import Notes from "./Notes";
  import NoteTypeBar from "./NoteTypeBar";
    export default {
        name: "NoteShelf",
      components: {Notes, NoteTypeBar},
      data(){
          return{
            loading:false,
            currentPage:1,
            total:0,
            pageSize:5,
          };
      },
      mounted() {
        this.getNotesByUsernameSize(JSON.parse(localStorage.getItem("user")).username);
      },
      methods:{
        // 获得用户笔记
        getNotesByUsername(username){
          var _this = this
          this.axios.get("/note/pageable/getNotes",{
            params:{
              'username':username,
              'currentPage':this.currentPage,
              'pageSize':this.pageSize
            }
          }).then(function (response) {
            if(response.data.status === 200){
              _this.$refs.notes.notes = response.data.object
            }
          })
            .catch(function(error) {
              console.log(error)
            })
        },
        // 获得用户笔记总数
        getNotesByUsernameSize(username){
          var _this = this
          this.axios.get("/note/getNotesSize",{
            params:{
              'username':username
            }
          }).then(function (response) {
            if(response.data.status === 200){
              _this.total = response.data.object
              if (response.data.object===0){
                document.getElementById('wu').style.display=''
              }else {
                _this.getNotesByUsername(username)
                document.getElementById('wu').style.display='none'
              }
            }
          })
            .catch(function(error) {
              console.log(error)
            })

        },
        handleSizeChange(val) {
          var _this = this
          this.pageSize=val;
          _this.loading=true
          this.getUserNotes()
          _this.loading=false
        },
        handleCurrentChange(val) {
          var _this = this
          this.currentPage=val
          _this.loading=true
          this.getUserNotes()
          _this.loading=false
        },
          getUserNotes(){
            var _this = this
            var username = JSON.parse(localStorage.getItem("user")).username;
            var noteTypeId = this.$refs.noteTypeBar.currentNTid

            if(noteTypeId === "allNotesOfUser"){
              noteTypeId = null
            }
            _this.loading=true
            this.axios.get("/note/pageable/getNotes",{
              params:{
                'username': username,
                'noteTypeId': noteTypeId,
                'currentPage':this.currentPage,
                'pageSize':this.pageSize
              }
            }).then(function (response) {
                if(response.status === 200){
                  _this.$refs.notes.notes = response.data.object
                  _this.loading=false
                }
              })
          },
        getUserNotesSize(){
          var _this = this
          var username = JSON.parse(localStorage.getItem("user")).username;
          var noteTypeId = this.$refs.noteTypeBar.currentNTid
          if(noteTypeId === "allNotesOfUser"){
            noteTypeId = null
          }
          _this.loading=true
          this.axios.get("/note/getNotesSize",{
            params:{
              'username': username,
              'noteTypeId': noteTypeId
            }
          }).then(function (response) {
            if(response.status === 200){
              _this.total = response.data.object
              _this.loading=false
              _this.getUserNotes()
              if (response.data.object.length===0){
                document.getElementById('wu').style.display=''
              }else {
                document.getElementById('wu').style.display='none'
              }
            }
          })
        },
      },
    }
</script>

<style scoped>
  .noteTypeCss{
    margin:0 auto;
    width: 10%;
    float: left;

  }
  .noteCss{
    margin:0 auto;
    float: right;
    width: 90%;
  }


</style>
