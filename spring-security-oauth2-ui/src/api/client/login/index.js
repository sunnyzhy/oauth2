import fetch from '@/utils/fetch'

export function login (user) {
  const data = {
    username: user.userName,
    password: user.password
  }
  return fetch({
    url: '/client/login',
    method: 'post',
    data
  })
}

export function logout (user) {
  return fetch({
    url: '/auth/login',
    method: 'post',
    data: {
      username: user.userName,
      password: user.password
    }
  })
}
