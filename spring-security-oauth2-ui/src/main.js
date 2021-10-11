// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import i18n from './lang'
import ElementUI from 'element-ui'
import JsonViewer from 'vue-json-viewer'
import 'element-ui/lib/theme-chalk/index.css'
import { getToken, getCookie } from './utils/token'

Vue.config.productionTip = false

Vue.use(ElementUI, {
  size: 'medium',
  i18n: (key, value) => i18n.t(key, value)
})

Vue.use(JsonViewer)

const whiteList = ['/auth/login', '/client/login']

router.beforeEach((to, from, next) => {
  if (getToken() || getCookie()) {
    if (to.path === '/auth/login' || to.path === '/client/login') {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/auth/login')
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
