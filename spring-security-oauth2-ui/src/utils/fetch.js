import axios from 'axios'
import {
  Message
} from 'element-ui'

const ajax = axios.create({
  baseURL: 'http://localhost',
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

export default ajax
