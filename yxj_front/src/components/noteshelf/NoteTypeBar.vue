<template>
  <el-tabs :tab-position="tabPosition" style="height: 300px;" v-model="currentNTid" @tab-click="handleClick" class="noteTypeBar">
    <div style="height: 50px"></div>
    <el-tab-pane label="全部笔记"  name="allNotesOfUser"></el-tab-pane>
    <el-tab-pane v-for="(item,i) in noteTypes"  :label="item.name" :key="i" :name="item.id.toString()">
    </el-tab-pane>

  </el-tabs>
</template>

<script>
    export default {
        name: "NoteTypeBar",
        data(){
          return{
            currentNTid:"allNotesOfUser",
            tabPosition: 'left',
            noteTypes:[]
          };
        },
        mounted() {
          var _this = this;
          this.axios.get("/noteTypes")
            .then(function (response) {
              if(response.status === 200){
                _this.noteTypes = response.data
              }
            });
        },
        methods:{
          handleClick(tab, event){
            this.$emit("noteTypeSelect")
          }
        }
    }
</script>

<style scoped>
  .noteTypeBar{
    height: 1000px;
  }

</style>
