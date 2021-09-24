import Vue from 'vue'
import Vuex from 'vuex'
import authServer from './modules/auth_server'
import client from './modules/client'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    authServer,
    client
  },
  getters
})

export default store
