<template>
  <div class="cart">
    <div class="container">
      <ul v-if="state.items.length > 0">
        <li v-for="(item, idx) in state.items" :key="idx">
          <img :src="item.imgPath" />
          <span class="name">{{ item.name }}</span>
          <span class="price">{{ lib.getNumberFormatted(item.price - (item.price * item.discountPer / 100)) }} 원</span>
          <span class="quantity">{{ item.quantity }} 개</span>
          <font-awesome-icon :icon="['fas', 'trash']" class="trash_icon" @click="remove(item.id)"/>
        </li>
      </ul>
      <ul v-else>
        <li>주문할 상품 내역이 없습니다.</li>
      </ul>
      <router-link to="/order" class="btn btn-primary" v-if="state.items.length > 0">구입하기</router-link>
    </div>
  </div>
</template>

<script>
import { reactive, onMounted } from 'vue'
import axios from 'axios'
import lib from '@/scripts/lib'

export default {
  setup () {
    const state = reactive({
      items: []
    })

    const load = async () => {
      try {
        const { data } = await axios.get('/api/cart/items')
        state.items = data
        await loadQuantities()
      } catch (error) {
        console.error(error)
      }
    }

    const loadQuantities = async () => {
      const promises = state.items.map(async (item) => {
        try {
          const { data: quantity } = await axios.get(`/api/cart/items/${item.id}/quantity`)
          item.quantity = quantity
        } catch (error) {
          console.error(error)
        }
      })
      await Promise.all(promises)
    }

    const remove = async (itemId) => {
      try {
        await axios.delete(`/api/cart/items/${itemId}`)
        load()
      } catch (error) {
        console.error(error)
      }
    }

    onMounted(load)

    return { state, lib, remove }
  }
}
</script>

<style scoped>
.cart ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
.cart ul li {
  border: 1px solid #eee;
  margin: 25px 0;
}
.cart ul li img {
  width: 150px;
  height: 150px;
}
.cart ul li .name,
.cart ul li .price {
  margin-left: 25px;
}

.cart ul li .quantity {
  margin-left: 25px;
}

.cart ul li .trash_icon {
  float: right;
  font-size: 22px;
  margin-top: 65px;
  margin-right: 50px;
}

.cart .btn {
  width: 300px;
  display: block;
  margin: 0 auto;
  padding: 30px 50px;
  font-size: 20px;
}
</style>
