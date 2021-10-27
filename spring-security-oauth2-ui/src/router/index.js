import Vue from 'vue'
import Router from 'vue-router'
import AuthLogin from '@/views/auth/login'
import ClientLogin from '@/views/client/login'
import AuthHome from '@/views/auth/home'
import ClientHome from '@/views/client/home'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'AuthHome',
      component: AuthHome
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
    },
    {
      path: '/client/index',
      name: 'ClientHome',
      component: ClientHome
    }
  ]
})
