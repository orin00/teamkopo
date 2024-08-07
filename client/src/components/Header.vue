<template>
  <div class="header">
    <header data-bs-theme="dark">
      <div class="navbar navbar-dark navbar-top">
        <div class="container">
          <div class="row">
            <div class="col-sm-4 offset-md-1">
              <ul class="list-unstyled">
                <li v-if="$store.state.account.id">
                  <router-link to="/orders"
                    >주문 내역</router-link
                  >
                </li>
                <li v-else>
                  <router-link to="/member"
                    >회원가입</router-link
                  >
                </li>
                <li>
                  <router-link
                    v-if="$store.state.account.id"
                    to="/login"
                    @click="logout"
                    >로그아웃</router-link
                  >
                  <router-link v-else to="/login"
                    >로그인</router-link
                  >
                </li>
                <li class="user" v-if="$store.state.account.id">
                  <span>{{ $store.state.account.nickname }} 님</span>
                </li>
                <li v-if="$store.state.account.id">
                  <router-link to="/" @click="deleteAccount"
                    >회원 탈퇴</router-link
                  >
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="navbar navbar-dark shadow-sm navbar-bottom">
        <div class="container">
          <router-link to="/" class="navbar-brand d-flex align-items-center">
            <strong>Commerce</strong>
          </router-link>
          <ul class="list-unstyled">
            <li class="category-1">
              <router-link
                :to="{ name: 'items', params: { category: 'apple' } }"
              >
                <span>apple</span>
              </router-link>
            </li>
            <li class="category-2">
              <router-link
                :to="{ name: 'items', params: { category: 'banana' } }"
              >
                <span>banana</span>
              </router-link>
            </li>
            <li class="category-3">
              <router-link
                :to="{ name: 'items', params: { category: 'grape' } }"
              >
                <span>grape</span>
              </router-link>
            </li>
            <li class="admin" v-if="isAdmin">
              <router-link to="/admin">관리자 페이지</router-link>
            </li>
          </ul>
          <router-link
            to="/cart"
            class="cart"
            v-if="$store.state.account.id"
          >
            <font-awesome-icon :icon="['fas', 'shopping-cart']" />
            &nbsp;장바구니
          </router-link>
        </div>
      </div>
    </header>
  </div>
</template>

<script>
import { onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import axios from 'axios'

export default {
  name: 'Header',
  setup () {
    const router = useRouter()
    const store = useStore()

    const isAdmin = computed(() => store.getters.isAdmin)

    // 로그아웃 처리
    const logout = async () => {
      try {
        await axios.post('/api/account/logout')
        store.dispatch('logout')
        router.push({ path: '/' })
      } catch (error) {
        console.error('로그아웃 에러:', error)
      }
    }

    // 계정 삭제 처리
    const deleteAccount = async () => {
      try {
        await axios.delete(`/api/members/${store.state.account.id}`)
        store.commit('resetAccount')
        router.push({ path: '/' })
      } catch (error) {
        console.error('회원 탈퇴 에러:', error)
      }
    }

    // isAdmin 값 실시간으로 업데이트
    onMounted(() => {
      store.dispatch('checkAdmin')
    })

    return { isAdmin, logout, deleteAccount }
  }
}
</script>

<style scoped>
header {
  position: relative;
  width: 100%;
}
header .navbar {
  background: #f3f7fd;
}
header .navbar-top .row {
  height: 36px;
  width: 100%;
  justify-content: flex-end;
  color: #17191d;
}
header .row > * {
  padding-left: 0;
  padding-right: 0;
}
header .navbar-bottom ul {
  width: 80%;
  margin: 0 auto;
}
header .list-unstyled {
  display: flex;
}
header .navbar-top ul {
  justify-content: right;
}
header .navbar-top ul li {
  padding: 3px 0;
  text-align: right;
  margin-left: 24px;
}
header .navbar-top ul li a {
  font-size: 14px;
}
header .navbar-bottom ul li {
  margin: 0 auto;
}
header ul li a {
  cursor: pointer;
  color: #17191d;
}
header .navbar {
  position: relative;
  width: 100%;
}
header .navbar .container {
  width: 1280px;
}
header .navbar .navbar-brand {
  color: #17191d;
  font-size: 24px;
}
header .navbar .cart {
  color: #17191d;
}
header .user {
  color: #17191d;
}
</style>
