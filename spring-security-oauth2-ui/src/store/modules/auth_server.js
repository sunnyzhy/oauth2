import {
  login,
  logout
} from '@/api/auth_server/login'

import {
  getToken,
  removeToken,
  getCookie,
  setCookie,
  removeCookie
} from '@/utils/token'

const authServer = {
  state: {
    username: '',
    cookie: getCookie(),
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
          setCookie(response.cookie)
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
          removeToken()
          removeCookie()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    removeToken ({commit}) {
      commit('SET_USERNAME', '')
      commit('SET_COOKIE', '')
      commit('SET_TOKEN', '')
      removeToken()
      removeCookie()
    }
  }
}

export default authServer