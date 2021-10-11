import Vue from 'vue'
import Router from 'vue-router'
import AuthLogin from '@/views/auth_server/login'
import ClientLogin from '@/views/client/login'
import Home from '@/views/home'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/auth/login',
      name: 'AuthLogin',
      component: AuthLogin
    },
    {
      path: '/client/login',
      name: 'ClientLogin',
      component: ClientLogin
    }
  ]
})
