<template>
  <div class='login-container'>
    <el-form
      label-position='left'
      label-width='0px'
      class='login-form'>
      <el-form-item prop='userName' style='margin-bottom: 22px'>
        <el-input
          size='small'
          name='username'
          v-model='loginForm.userName'
          autoComplete='on'
          placeholder='用户名'>
          <i slot='prefix' class='el-icon-user'></i>
        </el-input>
      </el-form-item>
      <el-form-item prop='password' style='margin-bottom: 22px'>
        <el-input
          size='small'
          name='password'
          type='password'
          v-model='loginForm.password'
          autoComplete='off'
          placeholder='密码'>
          <i slot='prefix' class='el-icon-lock'></i>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button type='primary' size='small' class='login-button'
          :loading='loading' @click='doLogin'>登录(AuthServer)</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'login',
  data () {
    return {
      loginForm: {
        userName: 'admin',
        password: 'admin'
      },
      loading: false
    }
  },
  methods: {
    doLogin () {
      this.$store.dispatch('login', this.loginForm)
        .then(() => {
          if (this.$store.state.authServer.cookie.length === 0) {
            this.$message.error('用户名或密码错误')
          } else {
            this.$router.push({
              path: '/'
            })
            this.$message({
              message: 'success',
              type: 'success'
            })
          }
        })
        .catch(error => {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>
.login-container {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: rgba(5, 15, 31);
  display: hidden
}

.login-form {
  width: 300px;
  margin: 160px auto; /* 上下间距160px，左右自动居中*/
  background-color: rgb(32, 32, 27, 0.8); /* 透明背景色 */
  padding: 30px;
  border-radius: 10px /* 圆角 */
}

.login-button {
  margin-top: 20px;
  width: 100%;
  border-radius: 2px
}
</style>
