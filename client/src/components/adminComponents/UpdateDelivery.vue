<template>
  <div class="admin_tab">
    <div class="tab_title">
      <h1>주문 관리 탭</h1>
    </div>
    <div class="admin_contents">
      <input type="number" v-model="memberId" placeholder="사용자 ID 입력">
      <button @click="searchOrders">주문 조회</button>
      <ul v-if="orders.length > 0">
        <li v-for="order in orders" :key="order.id">
          <p>{{ order.name }} 님의 주문 정보:</p>
          <p>주소: {{ order.address }}</p>
          <p>배송 상태: {{ order.deliveryStatus }}</p>
          <button v-if="order.deliveryStatus == '준비 완료'" @click="updateDeliveryStatus(order.id, '배송중')">배송중</button>
          <button v-if="order.deliveryStatus == '배송중' " @click="updateDeliveryStatus(order.id, '배송 완료')">배송 완료</button>
        </li>
      </ul>
      <p v-else>주문 정보가 없습니다.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UpdateDelivery',
  data () {
    return {
      memberId: '',
      orders: []
    }
  },
  methods: {
    async searchOrders () {
      try {
        const response = await fetch(`/api/orders/${this.memberId}`)
        if (!response.ok) {
          throw new Error('주문 정보를 가져오지 못했습니다.')
        }
        this.orders = await response.json()
      } catch (error) {
        console.error('주문 조회 에러:', error)
        this.orders = []
      }
    },
    async updateDeliveryStatus (orderId, status) {
      try {
        const response = await fetch(`/api/orders/${orderId}/update-delivery-status?status=${status}`, {
          method: 'PUT'
        })
        if (!response.ok) {
          throw new Error('배송 상태를 업데이트하지 못했습니다.')
        }
        // Update local orders list after successful update
        const updatedOrder = this.orders.find(order => order.id === orderId)
        if (updatedOrder) {
          updatedOrder.deliveryStatus = status
        }
      } catch (error) {
        console.error('배송 상태 업데이트 에러:', error)
      }
    }
  }
}
</script>

<style>
/* Add your CSS styles here */
</style>
