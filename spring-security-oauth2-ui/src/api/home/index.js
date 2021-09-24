import axios from 'axios'
import {
  Message
} from 'element-ui'

const ajax = axios.create({
  baseURL: 'http://localhost:8090',
  timeout: 30000
})

ajax.interceptors.response.use(
  response => {
    const resp = response.data
    if (resp.code === 0) {
      return resp.data
    } else {
      Message({
        message: resp.msg,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(resp.msg)
    }
  },
  error => {
    return Promise.reject(error)
  })

export function getCode (vo) {
  return ajax({
    url: '/auth/code',
    method: 'post',
    data: {
      clientId: vo.clientId,
      cookie: vo.cookie
    }
  })
}

export function getToken (vo) {
  return ajax({
    url: '/auth/token',
    method: 'post',
    data: {
      clientId: vo.clientId,
      clientSecret: vo.clientSecret,
      code: vo.code
    }
  })
}
