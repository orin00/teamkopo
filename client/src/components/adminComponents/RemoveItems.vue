<template>
  <div class="admin_tab">
    <div class="tab_title">
      <h1>상품 제거 탭</h1>
    </div>
    <div class="admin_contents">
      <ul>
        <li v-for="item in items" :key="item.id">
          <img :src="item.imgPath" alt="Item Image" />
          <span class="name">{{ item.name }}</span>
          <small class="price text-muted">
            {{ getNumberFormatted(item.price) }}
          </small>
          <span class="discount badge bg-danger">
            {{ item.discountPer }}%
          </span>
          <small class="price real text-danger">
            {{ getNumberFormatted(item.price - (item.price * item.discountPer) / 100) }}
          </small>
          <button @click="deleteItem(item.id)">삭제</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'RemoveItems',
  data () {
    return {
      items: []
    }
  },
  mounted () {
    this.getItems()
  },
  methods: {
    async getItems () {
      try {
        const response = await axios.get('/api/items')
        this.items = response.data
      } catch (error) {
        console.error('상품 목록을 불러오는 중 오류가 발생했습니다:', error)
      }
    },
    async deleteItem (itemId) {
      try {
        await axios.delete(`/api/items/${itemId}`)
        this.items = this.items.filter(item => item.id !== itemId)
        console.log(`ID가 ${itemId}인 상품이 삭제되었습니다.`)
      } catch (error) {
        console.error(`ID가 ${itemId}인 상품 삭제 중 오류가 발생했습니다:`, error)
      }
    },
    getNumberFormatted (val) {
      return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  }
}
</script>

<style>
ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 10px;
}

button {
  padding: 5px 10px;
  font-size: 14px;
  background-color: #dc3545;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #c82333;
}
</style>
