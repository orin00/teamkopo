<template>
  <div class="category-view">
    <main>
      <div class="album py-5 bg-body-tertiary">
        <div class="container">
          <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <div class="col" v-for="(item, idx) in state.items" :key="idx">
              <router-link
                :to="{
                  name: 'item-detail',
                  params: { id: item.id },
                }"
              >
                <Card :item="item" />
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import axios from 'axios'
import Card from '@/components/Card.vue'
import { reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const state = reactive({
  items: null
})

// 카테고리별 상품 조회 API 호출
const fetchItems = () => {
  axios
    .get(`/api/items/category/${route.params.category}`)
    .then(({ data }) => {
      state.items = data
    })
    .catch((error) => {
      console.error('Error fetching items by category:', error)
      alert('Failed to fetch items by category. Please try again later.')
    })
}

onMounted(fetchItems)

watch(() => route.params.category, (newCategory, oldCategory) => {
  fetchItems()
})
</script>

  <style scoped>
/* scoped 스타일링 */
</style>
