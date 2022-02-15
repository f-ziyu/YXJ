<template>
  <el-tabs :tab-position="tabPosition" style="height: 300px;" v-model="currentNTid" @tab-click="handleClick">
    <el-tab-pane v-for="(item,i) in noteTypes"  :label="item.name" :key="i" :name="item.id.toString()">

    </el-tab-pane>
  </el-tabs>
</template>

<script>
    export default {
        name: "NoteTypeBar",
        data(){
          return{
            currentNTid:"107",
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
              console.log(response)
            })
        },
        methods:{
          handleClick(tab, event){
            this.$emit("noteTypeSelect")
          }
        }
    }
</script>

<style scoped>

</style>
