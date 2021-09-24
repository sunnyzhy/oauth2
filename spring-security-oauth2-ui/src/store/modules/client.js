import {
  login,
  logout
} from '@/api/auth_server/login'

import {
  getToken
} from '@/utils/token'

const client = {
  state: {
    username: '',
    cookie: '',
    token: getToken()
  },

  mutations: {
    SET_USERNAME: (state, username) => {
      state.username = username
    },
    SET_COOKIE: (state, cookie) => {
      state.cookie = cookie
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    }
  },

  actions: {
    login ({
      commit
    }, user) {
      commit('SET_USERNAME', '')
      commit('SET_COOKIE', '')
      commit('SET_TOKEN', '')
      return new Promise((resolve, reject) => {
        login(user).then(response => {
          commit('SET_USERNAME', response.username)
          commit('SET_COOKIE', response.cookie)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    refresh ({ commit }) {

    },
    logout ({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        if (state.token === '') {
          return resolve()
        }
        logout(state.token).then(() => {
          commit('SET_USERNAME', '')
          commit('SET_COOKIE', '')
          commit('SET_TOKEN', '')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default client
