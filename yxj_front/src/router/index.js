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
import Collection from "../components/user/Collection";
import Recycle from "../components/user/Recycle";
import ModifyPwd from "../components/user/ModifyPwd";
import Search from "../components/noteshelf/Search";
import Comment from "../components/user/Comment";

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
      component:NoteEdit,
      meta: {
        requireAuth:true
      }
    },

    {
      path: '/',
      name: 'Index',
      component: Index,
      redirect: '/notes',
      children:[
        {
          path:'/home',
          name:'Home',
          component:Home,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/note/reading',
          name:'NoteRead',
          component:NoteRead
        },
        {
          path:'/notes',
          name:'NoteShelf',
          component:NoteShelf,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/community',
          name:'Community',
          component:Community
        },
        {
          path:'/user',
          name:'User',
          component:User,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/user/collection',
          name:'Collection',
          component:Collection,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/user/recycle',
          name:'Recycle',
          component:Recycle,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/user/modifyPwd',
          name:'ModifyPwd',
          component:ModifyPwd,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/note/search',
          name:'Search',
          component:Search,
          meta: {
            requireAuth:true
          }
        },
        {
          path:'/user/comment',
          name:'Comment',
          component:Comment,
          meta: {
            requireAuth:true
          }
        },
      ]
    }
  ]
})

