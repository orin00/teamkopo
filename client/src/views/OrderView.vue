<template>
  <div class="order">
    <div class="container">
      <main>
        <div class="py-5 text-center">
          <h2>주문하기</h2>
          <p class="lead">
            Below is an example form built entirely with Bootstrap’s form
            controls. Each required form group has a validation state that can
            be triggered by attempting to submit the form without completing it.
          </p>
        </div>

        <div class="row g-5">
          <div class="col-md-5 col-lg-4 order-md-last">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
              <span class="text-primary">구입 목록</span>
            </h4>
            <ul class="list-group mb-3">
              <li class="list-group-item d-flex justify-content-between lh-sm" v-for="(item, idx) in state.items" :key="idx">
                <div>
                  <h6 class="my-0">{{ item.name }}</h6>
                </div>
                <span class="text-body-secondary">
                  {{ lib.getNumberFormatted(item.price - (item.price * item.discountPer / 100)) }} 원
                </span>

                <span class="quantity">{{ item.quantity }} 개</span>
              </li>
            </ul>
            <h3 class="text-center total-price">
              {{ lib.getNumberFormatted(computedPrice) }} 원
            </h3>
          </div>
          <div class="col-md-7 col-lg-8">
            <h4 class="mb-3">주문자 정보</h4>
            <div class="needs-validation" novalidate>
              <div class="row g-3">
                <div class="col-12">
                  <label for="username" class="form-label">이름</label>
                  <input
                    type="text"
                    class="form-control"
                    id="username"
                    v-model="state.form.name"
                  />
                </div>

                <div class="col-12">
                  <label for="address" class="form-label">주소</label>
                  <input
                    type="text"
                    class="form-control"
                    id="address"
                    v-model="state.form.address"
                  />
                </div>

                <div class="col-12">
                  <label for="addressDetail" class="form-label">상세 주소</label>
                  <input
                    type="text"
                    class="form-control"
                    id="addressDetail"
                    v-model="state.form.addressDetail"
                  />
                </div>
              </div>

              <hr class="my-4" />

              <h4 class="mb-3">결제 수단</h4>

              <div class="my-3">
                <div class="form-check">
                  <input
                    id="card"
                    name="paymentMethod"
                    type="radio"
                    class="form-check-input"
                    value="card"
                    v-model="state.form.payment"
                  />
                  <label class="form-check-label" for="card">신용카드</label>
                </div>
                <div class="form-check">
                  <input
                    id="bank"
                    name="paymentMethod"
                    type="radio"
                    class="form-check-input"
                    value="bank"
                    v-model="state.form.payment"
                  />
                  <label class="form-check-label" for="bank">무통장입금</label>
                </div>
              </div>

              <div class="row gy-3">
                <div class="col-md">
                  <label for="cc-number" class="form-label">카드번호</label>
                  <input
                    type="text"
                    class="form-control"
                    id="cc-number"
                    v-model="state.form.cardNumber"
                    />
                </div>
              </div>

              <hr class="my-4" />

              <button class="w-100 btn btn-primary btn-lg" @click="submit">결제하기</button>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import { computed, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import lib from '@/scripts/lib'

export default {
  setup () {
    const state = reactive({
      items: [],
      form: {
        name: '',
        address: '',
        addressDetail: '',
        payment: '',
        cardNumber: '',
        items: '',
        deliveryStatus: '준비 완료'
      }
    })

    const router = useRouter()

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

    const submit = async () => {
      const args = JSON.parse(JSON.stringify(state.form))
      args.items = JSON.stringify(state.items)

      try {
        await axios.post('/api/orders', args)
        console.log('success')
        router.push({ path: '/orders' })
      } catch (error) {
        console.error(error)
      }
    }

    const computedPrice = computed(() => {
      let result = 0
      for (const item of state.items) {
        result += item.price - (item.price * item.discountPer / 100)
      }
      return result
    })

    onMounted(load)

    return { state, lib, computedPrice, submit }
  }
}
</script>

<style scoped>
.quantity {
  margin-left: 10px;
  font-size: 0.9em;
  color: #555;
}
</style>
