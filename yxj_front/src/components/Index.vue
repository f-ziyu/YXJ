<template>
    <div class="main-page">
      <nav-menu></nav-menu>
      <router-view/>
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
  .main-page {
    width: 80%;
    height: 100%;
    /*background: #ccccac;*/
    /*border: 1px dashed #ccc;*/
    margin: 0 auto;
    padding: 0;
  }



</style>
