<template>
  <div class="home">
    <main>
      <div class="album py-5 bg-body-tertiary">
        <div class="container">
          <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
            <div class="col" v-for="(item, idx) in state.items" :key="idx">
              <Card :item="item" />
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios'
import Card from '@/components/Card.vue'
import { reactive } from 'vue'

export default {
  name: 'Home',
  components: {
    Card
  },
  setup () {
    const state = reactive({
      items: []
    })

    axios.get('/api/items')
      .then(({ data }) => {
        state.items = data
      })
      .catch((error) => {
        console.error('Error fetching items:', error)
        alert('Failed to fetch items. Please try again later.')
      })

    return { state }
  }
}
</script>

<style scoped>

</style>
