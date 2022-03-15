import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Register from "../components/Register";
import Login from "../components/Login";
import Index from "../components/Index";
import Home from "../components/home/Home";
import NoteShelf from "../components/noteshelf/NoteShelf";
import Community from "../components/community/Community";
import User from "../components/user/User";
import NoteEdit from "../components/note/NoteEdit";
import NoteRead from "../components/note/NoteRead";


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
      path:'/note/edit',
      name:'NoteEdit',
      component:NoteEdit
    },
    {
      path:'/note/reading',
      name:'NoteRead',
      component:NoteRead
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
        },
        {
          path:'/user',
          name:'User',
          component:User
        },

      ]
    }
  ]
})

