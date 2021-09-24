import axios from 'axios'

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
      return Promise.reject(resp.msg)
    }
  },
  error => {
    return Promise.reject(error)
  })

export function login (user) {
  return ajax({
    url: '/auth/login',
    method: 'post',
    data: {
      username: user.userName,
      password: user.password
    }
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

// export function login (user) {
//   const data = {
//     username: user.userName,
//     password: user.password
//   }
//   return ajax({
//     url: '/login',
//     method: 'post',
//     data,
//     transformRequest: [
//       function (data) {
//         let ret = ''
//         for (let it in data) {
//           ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
//         }
//         ret = ret.substring(0, ret.lastIndexOf('&'))
//         return ret
//       }
//     ],
//     headers: {
//       'Content-Type': 'application/x-www-form-urlencoded'
//     }
//   })
// }
