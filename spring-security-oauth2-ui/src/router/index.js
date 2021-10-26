import Vue from 'vue'
import Router from 'vue-router'
import AuthLogin from '@/views/auth_server/login'
import ClientLogin from '@/views/client/login'
import Home from '@/views/home'
import ClientHome from '@/views/client/home'

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
      path: '/client/index',
      name: 'ClientHome',
      component: ClientHome
    },
    {
      path: '/client/login',
      name: 'ClientLogin',
      component: ClientLogin
    }
  ]
})
