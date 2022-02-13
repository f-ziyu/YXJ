import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const store = new Vuex.Store({
  state:{
    currentUser : ''
  },
  mutations:{
    login(state, user){
      state.currentUser=user
      console.log(user)
    }
  }
})

export default store
