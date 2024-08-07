<template>
  <div class="admin">
    <main>
      <div class="album py-5 bg-body-tertiary">
        <div class="container">
          <div class="title">
            <h2>관리자 페이지</h2>
          </div>
          <div class="tablist">
            <ul>
              <li>
                <a
                  :class="{ active: currentComponent === 'AddItems' }"
                  href="#"
                  @click.prevent="currentComponent = 'AddItems'"
                  >상품 추가</a
                >
              </li>
              <li>
                <a
                  :class="{ active: currentComponent === 'RemoveItems' }"
                  href="#"
                  @click.prevent="currentComponent = 'RemoveItems'"
                  >상품 제거</a
                >
              </li>
              <li>
                <a
                  :class="{ active: currentComponent === 'RemoveReviews' }"
                  href="#"
                  @click.prevent="currentComponent = 'RemoveReviews'"
                  >댓글 삭제</a
                >
              </li>
              <li>
                <a
                  :class="{ active: currentComponent === 'UpdateDelivery' }"
                  href="#"
                  @click.prevent="currentComponent = 'UpdateDelivery'"
                  >배송 관리</a
                >
              </li>
            </ul>
          </div>
          <div class="tabpanel">
            <div class="media__contents">
              <component :is="currentComponent"></component>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import AddItems from '@/components/adminComponents/AddItems.vue'
import RemoveItems from '@/components/adminComponents/RemoveItems.vue'
import RemoveReviews from '@/components/adminComponents/RemoveReviews.vue'
import UpdateDelivery from '@/components/adminComponents/UpdateDelivery.vue'

export default {
  name: 'Admin',
  components: {
    AddItems,
    RemoveItems,
    RemoveReviews,
    UpdateDelivery
  },
  data () {
    return {
      currentComponent: null
    }
  },
  methods: {
    selectComponent (component) {
      this.currentComponent = component
    }
  }
}
</script>

<style scoped>
.tablist ul {
  margin: 25px;
  display: flex;
  flex-direction: row;
  list-style: none;
}
.tablist ul li {
  width: 25%;
  text-align: center;
  margin: 12px 0;
}
.tablist ul li a {
  padding: 11px 90px;
  text-decoration: none;
  color: #131221;
  font-size: 22px;
}

.tablist ul li a.active {
  background-color: rgb(199, 199, 199);
  border-radius: 5px;
}
</style>
