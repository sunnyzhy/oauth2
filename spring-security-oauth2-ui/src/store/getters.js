const getters = {
  username: state => state.server.username,
  cookie: state => state.server.cookie,
  token: state => state.server.token,
  usernameClient: state => state.client.username
}
export default getters
