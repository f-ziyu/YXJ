import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Register from "../components/Register";
import Login from "../components/Login";
import Index from "../components/Index";
import Home from "../components/home/Home";
import NoteShelf from "../components/noteshelf/NoteShelf";
import Community from "../components/community/Community";

Vue.use(Router)

export default new Router({
  routes: [
    // {
    //   path: '/',
    //   name: 'HelloWorld',
    //   component: HelloWorld
    // },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/',
      name: 'Index',
      component: Index,
      redirect: '/home',
      children:[
        {
          path:'/home',
          name:'Home',
          component:Home
        },
        {
          path:'/notes',
          name:'NoteShelf',
          component:NoteShelf
        },
        {
          path:'/community',
          name:'Community',
          component:Community
        }
      ]


    }
  ]
})

