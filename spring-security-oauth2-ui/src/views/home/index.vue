<template>
  <div class='login-container'>
    <el-form
      label-position='left'
      label-width='0px'
      class='login-form'>
      <el-form-item prop='clientId' style='margin-bottom: 22px'>
        <el-input
          size='small'
          name='clientId'
          v-model='formDataCode.clientId'
          autoComplete='on'
          placeholder='clientId'>
          <i slot='prefix' class='el-icon-user'></i>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type='primary' size='small' class='login-button' :loading='loading' @click='getCode'>获取 Code</el-button>
      </el-form-item>

      <el-form-item prop='clientId' style='margin-bottom: 22px'>
        <el-input
          size='small'
          name='clientId'
          v-model='formDataToken.clientId'
          autoComplete='on'
          placeholder='clientId'>
          <i slot='prefix' class='el-icon-user'></i>
        </el-input>
      </el-form-item>
      <el-form-item prop='clientSecret' style='margin-bottom: 22px'>
        <el-input
          size='small'
          name='clientSecret'
          type='clientSecret'
          v-model='formDataToken.clientSecret'
          autoComplete='off'
          placeholder='clientSecret'>
          <i slot='prefix' class='el-icon-lock'></i>
        </el-input>
      </el-form-item>
      <el-form-item prop='code' style='margin-bottom: 22px'>
        <el-input
          size='small'
          name='clientSecret'
          type='clientSecret'
          v-model='formDataToken.code'
          autoComplete='off'
          placeholder='code'>
          <i slot='prefix' class='el-icon-chat-round'></i>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type='primary' size='small' class='login-button' :loading='loading' @click='getToken'>获取 Token</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :close-on-click-modal='false' title='token'
      :visible.sync='dialogFormVisible'
      :modal='true'
      width='65%'
      append-to-body>
      <json-viewer
        :value='token'
        :expand-depth='5'
        copyable
        boxed
        sort>
      </json-viewer>
    </el-dialog>
  </div>
</template>

<script>
import { getCode, getToken } from '@/api/home'
export default {
  name: 'login',
  data () {
    return {
      formDataCode: {
        clientId: 'messaging-client'
      },
      formDataToken: {
        clientId: 'messaging-client',
        clientSecret: 'secret',
        code: null
      },
      token: null,
      loading: false,
      dialogFormVisible: false
    }
  },
  methods: {
    getCode () {
      let data = {
        clientId: this.formDataCode.clientId,
        cookie: this.$store.state.authServer.cookie
      }
      getCode(data)
        .then(response => {
          if (response.code === '-2') {
            this.$store.dispatch('removeToken')
            this.$router.push({
              path: '/auth/login'
            })
            this.$message.error('请重新登录')
          }
          this.formDataToken.code = response.code
        })
        .catch(error => {
          console.log(error)
        })
    },
    getToken () {
      getToken(this.formDataToken)
        .then(response => {
          this.$store.commit('SET_TOKEN', response.token.access_token)
          this.token = response.token
          this.dialogFormVisible = true
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
