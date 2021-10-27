import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'
import client from './modules/client'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    auth,
    client
  },
  getters
})

export default store
