<template>
  <div>
    <h3>最近修改</h3>
    <div style="margin:0 auto;">
      <el-card v-for="(item,i) in notes" :key="i"  class="box-card">
        <div slot="header" class="clearFix">
          <span style="float: left;margin-left: 100px">{{item.title}}</span>

          <el-button @click="updateNotePublicStatus($event,item.id,item.isPublic)" style="float: right;margin-right: 20px; padding: 5px 0;" type="text">
            <el-radio v-model="item.isPublic.toString()" label="1" >公开</el-radio>
          </el-button>
          <el-button style="float: right; padding: 5px 0;margin-right: 30px" type="text">
            <el-dropdown>
            <span class="el-dropdown-link">
              {{item.noteType?item.noteType.name:"暂无类别"}}
            </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="(itemType,i) in noteTypes"  :label="itemType.name" :key="i" :name="itemType.id.toString()">
                  <el-button @click="updateNoteType(item.id,itemType.id)" type="text">
                    {{itemType.name}}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <el-button @click="updateNoteType(item.id,-1)" type="text">
                    暂无类别
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-button>

          <el-tooltip transition="0s" class="item" content="删除笔记" placement="top-start">
            <el-button @click="deleteNote(item.id)" style="float: right; padding: 5px 0;margin-right: 30px" type="text"><i class="el-icon-delete"></i></el-button>
          </el-tooltip>

          <el-tooltip transition="0s" class="item" content="编辑笔记" placement="top-start">
            <el-button @click="editNote(item.id)" style="float: right; padding: 5px 0" type="text"><i class="el-icon-edit"></i></el-button>
          </el-tooltip>

          <el-tooltip transition="0s" class="item" content="查看笔记" placement="top">
            <el-button @click="readNote(item.id)" style="float: right; padding: 5px 0;" type="text"><i class="el-icon-reading"></i></el-button>
          </el-tooltip>

          <span style="float: right; margin-right: 30px;font-size: 12px">最近修改时间:{{formatDateTime(item.lastModifiedTime)}}</span>
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


    <h3>最近收藏</h3>
  </div>
</template>

<script>
  export default {
    name: "Home",
    data(){
      return{
        notes:[],
        noteTypes:[],
        dialogVisible: false
      }
    },
    mounted() {
      this.getNotesByUsername(JSON.parse(localStorage.getItem("user")).username);
      var _this = this
      this.axios.get("/noteType/getNoteTypes")
        .then(function (response) {
          if(response.data.status === 200){
            _this.noteTypes = response.data.object
          }
        }).catch(function(error) {
        console.log(error)
      });
    },
    methods:{
      // 获得用户笔记
      getNotesByUsername(username){
        var _this = this
        this.axios.get("/note/getNotes",{
          params:{
            'username':username
          }
        }).then(function (response) {
          if(response.data.status === 200){
            _this.notes = response.data.object
          }
        })
          .catch(function(error) {
            console.log(error)
          })

      },
      // 携带笔记id跳转至编辑页面
      editNote(id){
        this.$router.push({
          path:'note/edit',
          name:'NoteEdit',
          params:{
            noteId:id
          }
        })
      },
      // 携带笔记id跳转至阅读页面
      readNote(id){
        this.$router.push({
          path:'note/reading',
          name:'NoteRead',
          query:{
            noteId:id
          }
        })
      },
      // 更新笔记公开状态
      updateNotePublicStatus(e,noteId,isPublic){
        if(e.target.tagName === 'INPUT') return  // 取消重复调用函数
        var _this = this
        var msgText = ''
        if(isPublic === 1){
          msgText = "确认隐藏该笔记？"
        }else {
          msgText = "确认公开该笔记？"
        }
        _this.$confirm(msgText,'提示',{
          confirmButtonText:'确认',
          cancelButtonText:'取消',
          type:"info"
        }).then(()=> {
          _this.axios.get("/note/updatePublicValue",{
            params:{
              noteId:noteId
            }
          }).then(function (response) {
            if(response.data.status === 200){
              _this.$message({
                type: 'success',
                message: '修改成功'
              })
              _this.getNotesByUsername(JSON.parse(localStorage.getItem("user")).username);
            }else {
              _this.$message({
                type: "error",
                message: '失败:'+response.data.msg
              })
              console.log(response)
            }
          })
            .catch(function(error) {
              console.log(error)
            })
        })
      },
      // 删除笔记
      deleteNote(noteId){
        var _this = this
        _this.$confirm('确认删除该笔记？','提示',{
          confirmButtonText:'确认',
          cancelButtonText:'取消',
          type:"info"
        }).then(()=> {
          _this.axios.get("/note/moveNoteInRecycler", {
            params: {
              noteId: noteId
            }
          }).then(function (response) {
            if(response.data.status === 200) {
              _this.$message({
                type: 'success',
                message: '删除成功'
              })
              _this.getNotesByUsername(JSON.parse(localStorage.getItem("user")).username);
            }else {
              _this.$message({
                type: "error",
                message: '删除失败:'+response.data.msg
              })
              console.log(response)
            }
          }).catch(function (error) {
            console.log(error)
          })
        })
      },
      // 修改笔记的类型
      updateNoteType(noteId, typeId){
        var _this = this
        _this.$confirm('确认修改该笔记的笔记类别','提示',{
          confirmButtonText:'确认',
          cancelButtonText:'取消',
          type:"info"
        }).then(()=> {
          _this.axios.get("/note/modifyNoteType",{
            params:{
              noteId:noteId,
              typeId:typeId
            }
          }).then(function (response) {
            if(response.data.status === 200){
              _this.$message({
                type: 'success',
                message: '修改成功'
              })
              _this.getNotesByUsername(JSON.parse(localStorage.getItem("user")).username);
            }else {
              _this.$message({
                type: "error",
                message: '修改失败:'+response.data.msg
              })
              console.log(response)
            }
          })
            .catch(function(error) {
              console.log(error)
            })
        })
      },
      // 时间戳转换日期格式方法
      formatDateTime(value) {
        if (value == null) {
          return ''
        } else {
          const date = new Date(value)
          const y = date.getFullYear() // 年
          let MM = date.getMonth() + 1 // 月
          MM = MM < 10 ? ('0' + MM) : MM
          let d = date.getDate() // 日
          d = d < 10 ? ('0' + d) : d
          let h = date.getHours() // 时
          h = h < 10 ? ('0' + h) : h
          let m = date.getMinutes()// 分
          m = m < 10 ? ('0' + m) : m
          let s = date.getSeconds()// 秒
          s = s < 10 ? ('0' + s) : s
          return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s
        }
      },
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

  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
