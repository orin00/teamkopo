<template>
  <div class="orders">
    <div class="container">
      <table class="table table-bordered">
        <thead>
          <tr>
            <th>주문 일자</th>
            <th>주문자명</th>
            <th>주소</th>
            <th>상세주소</th>
            <th>결제 수단</th>
            <th>구입 항목</th>
            <th>배송 상태</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(o, idx1) in state.orders" :key="idx1">
            <td>{{ formatDate(o.createdAt) }}</td>
            <td>{{ o.name }}</td>
            <td>{{ o.address }}</td>
            <td>{{ o.addressDetail }}</td>
            <td>{{ o.payment }}</td>
            <td>
              <div v-for="(i, idx2) in o.items" :key="idx2">
                <span>
                  <img :src="i.imgPath" alt="Item Image" class="tableImg" />
                </span>
                {{ i.name }} - {{ i.price - (i.price * i.discountPer) / 100 }}원
                - {{ i.quantity }} 개 -
                {{
                  (i.price - (i.price * i.discountPer) / 100) * i.quantity
                }}
                원
              </div>
            </td>

            <td>{{ o.deliveryStatus }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue'
import axios from 'axios'
import lib from '@/scripts/lib'
import moment from 'moment' // Moment.js 추가
import 'moment/locale/ko' // 한국어 locale 추가

// 한국어 locale 설정
moment.locale('ko')

export default {
  setup () {
    const state = reactive({
      orders: []
    })

    axios.get('/api/orders').then(({ data }) => {
      state.orders = []

      for (const d of data) {
        if (d.items) {
          d.items = JSON.parse(d.items)
        }
        state.orders.push(d)
      }
    })

    // Moment.js를 이용한 날짜 포맷팅
    const formatDate = (datetime) => {
      return moment(datetime).format('LLL') // 'LLL' 형식은 ex: '2024년 6월 17일 오후 1시 30분'
    }

    return { state, lib, formatDate }
  }
}
</script>

<style scoped>
.table {
  margin-top: 30px;
}
.tableImg {
  width: 50px;
  height: auto;
}
</style>
