import { createRouter, createWebHistory } from 'vue-router'
import store from '../store/index' // Vuex 스토어 가져오기
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/member',
    name: 'member',
    component: () => import(/* webpackChunkName: "member" */ '../views/MemberView.vue')
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import(/* webpackChunkName: "admin" */ '../views/AdminView.vue'),
    beforeEnter: (to, from, next) => {
      if (store.getters.isAdmin) {
        next()
      } else {
        alert('관리자 권한이 없습니다. 홈페이지로 이동합니다.')
        next('/') // 관리자가 아니면 홈페이지로 리디렉션
      }
    }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
  },
  {
    path: '/items/:category',
    name: 'items',
    component: () => import(/* webpackChunkName: "category" */ '../views/CategoryView.vue'),
    props: true // route의 params를 컴포넌트의 props로 전달
  },
  {
    path: '/item/:id',
    name: 'item-detail',
    component: () => import(/* webpackChunkName: "item-detail" */ '../views/ItemDetailView.vue')
  },
  {
    path: '/cart',
    name: 'cart',
    component: () => import(/* webpackChunkName: "cart" */ '../views/CartView.vue')
  },
  {
    path: '/order',
    name: 'order',
    component: () => import(/* webpackChunkName: "order" */ '../views/OrderView.vue')
  },
  {
    path: '/orders',
    name: 'orders',
    component: () => import(/* webpackChunkName: "orders" */ '../views/OrdersView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
