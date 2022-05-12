<template>
    <div style="width: 100%;height: 100%">
      <div style="
      width: 10%;
      position:absolute; left:0;
      height: 100%;
      ">
        <img style="position:absolute;max-width: 100%;left: 0;top:10px;" src="static/images/cloud-left.png">
      </div>
      <div style="
      width: 10%;
      position:absolute; right:0;
      height: 100%;
      ">
        <img style="position:absolute;max-width: 100%;left: 0;top: 10px" src="static/images/cloud-right.png">
      </div>
      <div class="main-page">
        <nav-menu></nav-menu>
        <router-view/>
      </div>
    </div>

</template>

<script>
  import NavMenu from "./common/NavMenu";
    export default {
        name: "Index",
        components:{NavMenu}
    }
</script>

<style>
  * {margin: 0;padding: 0;background:rgba(0,0,0,0.0)}/*去掉白色边框*/

  .main-page {
    width: 80%;
    height: 100%;
    /*background: #ccccac;*/
    /*border: 1px dashed #ccc;*/
    margin: 0 auto;
    padding: 0;
  }



</style>
