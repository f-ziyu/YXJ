<template>
  <div>
    <el-row style="margin-top: 10px">
      <el-col :span="bookmarkCol" >
        <bookmark ref="bookmark"></bookmark>
      </el-col>
      <el-col :span="noteCol" style="border-left: 1px solid #585858;">
        <el-card class="box-card note" shadow="never" v-model="note">
          <div slot="header" class="clearFix">
            <span>{{note.name}}</span>
            <el-button style="float: right; padding: 3px 0" type="text">
              <i class="el-icon-edit" @click=""></i>
            </el-button>
            <el-button style="float: right; padding: 3px 0;margin-right: 5px" type="text">
              <i class="el-icon-tickets" @click=""></i>
            </el-button>
          </div>
          <el-scrollbar style="height: 100%">
            <div v-html="note.contentHtml" class="text note-html markdown-body" @click=""></div>
          </el-scrollbar>
        </el-card>
      </el-col>

      <el-col :span="quoteCol" style="border-left: 1px solid #585858;">
        <el-card class="box-card note" shadow="never" v-model="quote">
          <div slot="header" class="clearfix">
            <span>{{quote.name}}</span>
            <el-button style="float: right; padding: 3px 0" type="text">
              <i class="el-icon-edit" @click=""></i>
            </el-button>
            <el-button style="float: right; padding: 3px 0" type="text">
              <i class="el-icon-close" @click=""></i>
            </el-button>
          </div>
          <el-scrollbar style="height: 100%" id="quoteArea">
            <div v-html="quote.contentHtml" class="text note-html markdown-body"></div>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
      name: "NoteRead",
      data(){
        return{
          note:{
            id: null,
            title:null,
            author:null,
            describe:null,
            noteType:null,
            contentHtml:null,
            isPublic:"0",
          },
          quote:{
          },
          quoteLink:'',
          bookmarkStatus:true,
          quoteStatus:false,
          bookmarkCol:3,
          noteCol:21,
          quoteCol:0,
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
          }).then(function (response) {
              if (response.status === 200){
                _this.note=response.data.object
                document.getElementById("quoteArea").innerHTML=_this.note.contentHtml
                this.note.isPublic = response.data.object.isPublic+""

              }else {
                console.log(response.status)
              }
            })
            .catch(function (error) {
              console.log(error)
            })
        },
      }
    }
</script>

<style scoped>
  .note {
    border: 0px solid #EBEEF5!important;
    width: 95%;
    text-align: left;
    margin: auto;
  }
  .note-html{
    height: 450px;
  }
  .back{
    background-color: #585858!important;
    border: 0px;
    position: absolute;
    margin-top: 10px;
    margin-bottom: 5px;
    left: 10px;
    padding: 5px 5px;
    z-index: 99;
  }
  .op-btn{
    cursor: pointer;
  }
</style>
