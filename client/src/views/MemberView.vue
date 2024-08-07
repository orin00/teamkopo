<template>
  <div class="form-signup w-100 m-auto">
    <form @submit.prevent="member">
      <h3 class="member-title">회원가입</h3>

      <div class="form-floating">
        <input
          type="email"
          class="form-control"
          id="floatingInputEmail"
          placeholder="name@example.com"
          v-model="state.form.email"
        />
        <label for="floatingInputEmail">이메일 주소</label>
      </div>
      <div class="form-floating">
        <input
          type="password"
          class="form-control"
          id="floatingPassword"
          placeholder="비밀번호"
          v-model="state.form.password"
        />
        <label for="floatingPassword">비밀번호</label>
      </div>
      <div class="form-floating">
        <input
          type="password"
          class="form-control"
          id="floatingConfirmPassword"
          placeholder="비밀번호 확인"
          v-model="state.form.confirmPassword"
        />
        <label for="floatingConfirmPassword">비밀번호 확인</label>
      </div>
      <div class="form-floating">
        <input
          type="text"
          class="form-control"
          id="floatingInputNickname"
          placeholder="사용할 닉네임을 입력해주세요"
          v-model="state.form.nickname"
        />
        <label for="floatingInputNickname">닉네임</label>
      </div>

      <button
        class="btn btn-primary w-100 py-2"
        :disabled="submitting"
        @click="member"
      >
        {{ submitting ? "가입 중..." : "회원 가입" }}
      </button>
    </form>
  </div>
</template>

<script>
import { reactive } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

export default {
  setup () {
    const state = reactive({
      form: {
        email: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        role: 'user' // 기본값 설정
      }
    })

    const router = useRouter()
    let submitting = false // 폼 제출 상태를 나타내는 플래그

    const member = async () => {
      if (submitting) {
        return // 이미 제출 중인 경우 처리 중지
      }

      if (
        !state.form.email ||
        !state.form.password ||
        !state.form.confirmPassword ||
        !state.form.nickname
      ) {
        window.alert('모든 필드를 입력해주세요.')
        return
      }

      if (state.form.password !== state.form.confirmPassword) {
        window.alert('비밀번호가 일치하지 않습니다.')
        return
      }

      submitting = true // 제출 상태로 변경

      try {
        // 회원 가입 요청
        await axios.post('api/members/signup', {
          email: state.form.email,
          password: state.form.password,
          nickname: state.form.nickname
        })
        router.push({ path: '/login' })
      } catch (error) {
        console.error('회원 가입 오류:', error)
        alert('오류가 발생했습니다. 잠시 후 다시 시도해주세요.')
      } finally {
        submitting = false // 제출 상태 초기화
      }
    }

    return { state, member, submitting }
  }
}
</script>

<style scoped>
.form-signup {
  max-width: 520px;
  padding: 40px 0;
}
.form-signup form {
  padding: 40px 60px;
  height: 600px;
  display: flex;
  flex-direction: column;
  border: 0.5px solid #ddd;
  background: #f3f7fd;
}
.form-signup .member-title {
  margin: 40px 0;
  text-align: center;
}
.form-signup .form-floating:focus-within {
  z-index: 2;
}
.form-signup input[type="email"],
.form-signup input[type="password"],
.form-signup input[type="text"] {
  border-radius: 0;
}
.form-signup input[type="email"],
.form-signup input[type="password"] {
  margin-bottom: 20px;
}
.form-signup .btn {
  margin-top: 40px;
}
</style>
