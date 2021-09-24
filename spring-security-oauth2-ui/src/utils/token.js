import Cookies from 'js-cookie'

const TOKEN_KEY = 'access-token'
const COOKIE_KEY = 'access-cookie'

export function getToken () {
  return Cookies.get(TOKEN_KEY)
}

export function setToken (token) {
  return Cookies.set(TOKEN_KEY, token)
}

export function removeToken () {
  return Cookies.remove(TOKEN_KEY)
}

export function getCookie () {
  return Cookies.get(COOKIE_KEY)
}

export function setCookie (cookie) {
  return Cookies.set(COOKIE_KEY, cookie)
}

export function removeCookie () {
  return Cookies.remove(COOKIE_KEY)
}
