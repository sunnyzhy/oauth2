import axios from 'axios'

const ajax = axios.create({
  baseURL: 'http://localhost/client',
  timeout: 30000
})

export function login (userName, password) {
  const data = {
    userName,
    password
  }
  return ajax({
    url: '/login',
    method: 'post',
    data
  })
}

export function logout (user) {
  return ajax({
    url: '/auth/login',
    method: 'post',
    data: {
      username: user.userName,
      password: user.password
    }
  })
}