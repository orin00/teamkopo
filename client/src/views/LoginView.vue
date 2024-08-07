<template>
  <div class="form-signin w-100 m-auto">
    <form @submit.prevent="submit">
      <h3 class="login-title">로그인</h3>

      <div class="form-floating">
        <input
          type="email"
          class="form-control"
          id="floatingInput"
          placeholder="name@example.com"
          v-model="state.form.email"
        />
        <label for="floatingInput">이메일</label>
      </div>
      <div class="form-floating">
        <input
          type="password"
          class="form-control"
          id="floatingPassword"
          placeholder="Password"
          v-model="state.form.password"
        />
        <label for="floatingPassword">비밀번호</label>
      </div>
      <button class="btn btn-primary w-100 py-2" @click="submit">로그인</button>
    </form>
  </div>
</template>

<script>
import { reactive } from 'vue'
import axios from 'axios'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  setup () {
    const state = reactive({
      form: {
        email: '',
        password: ''
      }
    })

    const router = useRouter()
    const store = useStore()

    const submit = () => {
      if (!state.form.email || !state.form.password) {
        alert('이메일 또는 비밀번호가 입력되지 않았습니다.')
        return
      }

      axios.post('/api/account/login', state.form)
        .then((res) => {
          store.commit('setAccount', res.data)
          sessionStorage.setItem('account', JSON.stringify(res.data))
          // 로그인 후 권한 확인
          store.dispatch('checkAdmin').then(() => {
            const isAdmin = store.getters.isAdmin
            if (isAdmin) {
              router.push({ path: '/' }) // 여기서 메인 페이지로 이동
            } else {
              // 권한 없는 경우 다른 페이지로 이동
              router.push({ path: '/' })
            }
          })
        })
        .catch((error) => {
          if (error.response) {
            if (error.response.status === 404) {
              alert('로그인 엔드포인트를 찾을 수 없습니다. 지원팀에 문의해주세요.')
            } else if (error.response.status === 401) {
              alert('인증 실패. 자격 증명을 확인해주세요.')
            } else {
              alert('오류가 발생했습니다: ' + error.message)
            }
          } else if (error.request) {
            alert('요청이 실패했습니다. 네트워크 연결을 확인해주세요.')
          } else {
            alert('오류가 발생했습니다: ' + error.message)
          }
        })
    }

    return { state, submit }
  }
}
</script>

<style scoped>

.form-signin {
  max-width: 520px;
  padding: 40px 0;
}
.form-signin form {
  padding: 40px 60px;
  height: 600px;
  display: flex;
  flex-direction: column;
  border: 0.5px solid #ddd;
  background: #f3f7fd;
}
.form-signin .login-title {
  margin: 40px 0;
  text-align: center;
}
.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="email"] {
  margin-bottom: 20px;
  border-radius: 0;
}

.form-signin input[type="password"] {
  border-radius: 0;
}
.form-signin .btn {
  margin-top: 40px;
}
</style>
