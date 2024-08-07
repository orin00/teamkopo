import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state () {
    return {
      account: {
        id: 0,
        nickname: '',
        role: ''
      },
      isAdmin: false
    }
  },
  getters: {
    isAdmin: state => state.isAdmin
  },
  mutations: {
    setAccount (state, payload) {
      state.account.id = payload.id
      state.account.nickname = payload.nickname
      state.account.role = payload.role
    },
    setAdminStatus (state, isAdmin) {
      state.isAdmin = isAdmin
    },
    resetAccount (state) {
      state.account.id = 0
      state.account.nickname = ''
      state.account.role = ''
      state.isAdmin = false // 관리자 상태도 초기화
    }
  },
  actions: {
    fetchAccount ({ commit }) {
      axios.get('/api/account/check')
        .then(response => {
          commit('setAccount', response.data)
        })
        .catch(error => {
          console.error('Error fetching account:', error)
        })
    },
    checkAdmin ({ commit }) {
      axios.get('/api/account/admin/check')
        .then(response => {
          commit('setAdminStatus', response.data)
        })
        .catch(error => {
          console.error('Error checking admin status:', error)
        })
    },
    logout ({ commit }) {
      commit('resetAccount')
    }
  }
})
