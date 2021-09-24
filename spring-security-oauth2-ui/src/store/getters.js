const getters = {
  username: state => state.auth_server.username,
  cookie: state => state.auth_server.cookie,
  token: state => state.auth_server.token
}
export default getters
