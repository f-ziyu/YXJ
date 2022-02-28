<template>
  <div>
    <div class="noteTypeCss">
      <NoteTypeBar @noteTypeSelect="getUserNotes" ref="noteTypeBar"></NoteTypeBar>
    </div>
    <div class="noteCss">
      <Notes ref="notes"></Notes>
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

          };
      },
      mounted() {

      },
      methods:{
          getUserNotes(){
            //需要修改存储cookie的方式
            var _this = this
            var username = JSON.parse(localStorage.getItem("user")).username;
            var noteTypeId = this.$refs.noteTypeBar.currentNTid
            if(noteTypeId === "allNotesOfUser"){
              noteTypeId = null
            }
            this.axios.get("/note/getNotes",{
              params:{
                'username': username,
                'noteTypeId': noteTypeId,
              }
            })
              .then(function (response) {
                if(response.status === 200){
                  _this.$refs.notes.notes = response.data.object
                }
              })
          }
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
