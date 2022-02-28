<template>
  <el-tabs :tab-position="tabPosition" style="height: 600px;width: 250px" v-model="currentNTid" @tab-click="handleClick" class="noteTypeBar">

    <el-tab-pane label="全部笔记"  name="allNotesOfUser"></el-tab-pane>
    <el-tab-pane v-for="(item,i) in noteTypes"  :label="item.name" :key="i" :name="item.id.toString()">
    </el-tab-pane>
    <!-- 管理笔记分类，需要修改数据库以后再做
    <el-tab-pane label="添加分类"  name=""></el-tab-pane>
    -->

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
          this.axios.get("/noteType/getNoteTypes")
            .then(function (response) {
              if(response.status === 200){
                _this.noteTypes = response.data.object
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
