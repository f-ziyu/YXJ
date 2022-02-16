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
            var username = "ziyu01"
            var noteTypeId = this.$refs.noteTypeBar.currentNTid
            var getNotesUrl = "/noteTypes/"+username+"/"+noteTypeId+"/notes"
            if(noteTypeId === "allNotesOfUser"){
              getNotesUrl="/noteTypes/"+username+"/notes"
            }
            this.axios.get(getNotesUrl)
              .then(function (response) {
                if(response.status === 200){
                  _this.$refs.notes.notes = response.data
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
