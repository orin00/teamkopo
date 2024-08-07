<template>
  <div class="item-detail py-5" v-if="state.item">
    <div class="container">
      <div class="card-category">
        홈 > {{ state.item.category }} > {{ state.item.name }}
      </div>
      <div class="box-outer">
        <div class="box-left">
          <img :src="state.item.imgPath" alt="Item Image" />
        </div>

        <div class="box-right">
          <span class="name">{{ state.item.name }}</span>
          <div class="price-outer">
            <div class="price-inner">
              <span class="price-before" style="text-decoration: line-through">
                {{ lib.getNumberFormatted(state.item.price) }}원
              </span>
              <span class="discount">{{ state.item.discountPer }}%</span>
            </div>
            <div class="price-inner">
              <span class="price-after">
                {{ lib.getNumberFormatted(discountedPrice) }}원
              </span>
            </div>
          </div>
          <div class="avg-rating">
            <p>
              <font-awesome-icon :icon="['fas', 'star']" class="rating-star" />
              {{ averageRating.toFixed(2) }}점
            </p>
          </div>

          <div class="select-quantity">
            <button class="quantity-btn" @click="decreaseQuantity">-</button>
            <input type="number" v-model="quantity" min="1" />
            <button class="quantity-btn" @click="increaseQuantity">+</button>
          </div>
          <div class="total-price">
            <strong>총 상품 금액</strong>
            <div>
              <em>총 수량 {{ quantity }} 개</em>
              <strong>{{ lib.getNumberFormatted(getTotalPrice) }}원</strong>
            </div>
          </div>
          <div class="item-detail-submit">
            <button
              type="button"
              class="btn btn-primary"
              @click="addToCart(state.item.id)"
            >
              <font-awesome-icon :icon="['fas', 'shopping-cart']" />
              장바구니에 추가
            </button>
          </div>
        </div>
      </div>

      <div class="review">
        <div class="review-top">
          <p class="review-count">
            <span>상품 후기&nbsp;</span
            ><span class="review-count-length"
              >(&nbsp;{{ state.reviews.length }}&nbsp;)</span
            >
          </p>
        </div>

        <div class="review-bottom" v-if="$store.state.account.id">
          <p class="review-username">{{ store.state.account.nickname }} 님</p>
          <input
            v-model="state.reviewText"
            type="text"
            placeholder="상품의 후기를 입력해주세요"
            v-if="$store.state.account.id"
          />

          <div class="review-score">
            <div class="dropdown">
              <div class="dropdown-toggle" @click="toggleDropdown">
                <div class="dropdown-toggle-outer">
                  <font-awesome-icon
                    :icon="['fas', 'star']"
                    v-for="n in state.reviewRating"
                    :key="n"
                    class="rating-star"
                  />
                  <span style="display: none">{{ state.reviewRating }}</span>
                </div>
              </div>
              <div class="dropdown-menu" v-if="dropdownOpen">
                <div
                  class="dropdown-item"
                  v-for="rating in [5, 4, 3, 2, 1]"
                  :key="rating"
                  @click="selectRating(rating)"
                >
                  <font-awesome-icon
                    :icon="['fas', 'star']"
                    v-for="n in rating"
                    :key="n"
                    class="rating-star"
                  />
                  <span style="display: none">{{ rating }}</span>
                </div>
              </div>
            </div>

            <button
              type="button"
              class="btn btn-primary review-submit"
              @click="submitReview"
            >
              등록
            </button>
          </div>
        </div>

        <span v-else class="not-login">
          <p>로그인 후에 리뷰를 작성하실 수 있습니다.</p>
        </span>
      </div>

      <div class="all-reviews">
        <ul>
          <li v-for="review in reversedReviews" :key="review.id">
            <div class="all-reviews-top">
              <div class="all-reviews-top-list1">
                <p class="nickname">{{ review.nickname }}</p>
                <p>
                  <font-awesome-icon
                    :icon="['fas', 'star']"
                    v-for="n in review.rating"
                    :key="n"
                    class="rating-star"
                  />
                </p>
              </div>

              <span class="all-reviews-top-list2">{{ formatDate(review.createdAt) }}</span>
            </div>

            <p class="all-reviews-bottom">{{ review.text }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, computed, ref } from 'vue'
import axios from 'axios'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import lib from '@/scripts/lib'
import moment from 'moment' // Moment.js 추가
import 'moment/locale/ko' // 한국어 locale 추가

const route = useRoute()
const store = useStore()

const state = reactive({
  item: null,
  reviewText: '',
  reviewRating: 5,
  reviews: [] // 리뷰 데이터를 위한 상태 추가
})

// 주문 수량 상태 추가
const quantity = ref(1)

// 드롭다운 열림/닫힘 상태
const dropdownOpen = ref(false)

// 한국어 locale 설정
moment.locale('ko')

// 상품 정보 및 리뷰 불러오기
onMounted(async () => {
  try {
    const [itemResponse, reviewsResponse] = await Promise.all([
      axios.get(`/api/items/${route.params.id}`),
      axios.get(`/api/items/${route.params.id}/reviews`)
    ])
    state.item = itemResponse.data
    state.reviews = reviewsResponse.data // 리뷰 데이터를 상태에 저장
  } catch (error) {
    console.error('Failed to fetch item details or reviews:', error)
    alert(
      '상품 정보 또는 리뷰를 불러오는 데 실패했습니다. 나중에 다시 시도해주세요.'
    )
  }
})

const averageRating = computed(() => {
  if (state.reviews.length === 0) {
    return 0
  }
  const sum = state.reviews.reduce((acc, review) => acc + review.rating, 0)
  return sum / state.reviews.length
})

// 리뷰를 최신 순으로 정렬
const reversedReviews = computed(() => {
  return state.reviews.slice().reverse()
})

// 장바구니에 추가하기
const addToCart = async (itemId) => {
  try {
    await axios.post(`/api/cart/items/${itemId}`, null, {
      params: { quantity: quantity.value }
    })
    alert('장바구니에 추가되었습니다.')
  } catch (error) {
    console.error('Failed to add to cart:', error)
    alert('장바구니에 추가하는 데 실패했습니다. 다시 시도해주세요.')
  }
}

// 리뷰 제출하기
const submitReview = async () => {
  try {
    const reviewData = {
      text: state.reviewText,
      rating: state.reviewRating,
      nickname: store.state.account.nickname
    }
    await axios.post(`/api/items/${route.params.id}/reviews`, reviewData)
    alert('리뷰가 제출되었습니다.')
    const reviewsResponse = await axios.get(
      `/api/items/${route.params.id}/reviews`
    )
    state.reviews = reviewsResponse.data
  } catch (error) {
    console.error('Failed to submit review:', error)
    alert('리뷰 제출에 실패했습니다. 다시 시도해주세요.')
  }
}

// 수량 증가 및 감소 함수
const increaseQuantity = () => {
  quantity.value += 1
}

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value -= 1
  }
}

// 총 상품 금액 계산
const discountedPrice = computed(() => {
  if (state.item) {
    return state.item.price - (state.item.price * state.item.discountPer) / 100
  }
  return 0
})

const getTotalPrice = computed(() => {
  return quantity.value * discountedPrice.value
})

// 드롭다운 열림/닫힘 토글
const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

// 별점 선택
const selectRating = (rating) => {
  state.reviewRating = rating
  dropdownOpen.value = false
}

// Moment.js를 이용한 날짜 포맷팅
const formatDate = (datetime) => {
  return moment(datetime).format('LLL') // 'LLL' 형식은 ex: '2024년 6월 17일 오후 1시 30분'
}
</script>

<style scoped>
/* scoped 스타일링 */
.item-detail .container {
  --bs-gutter-x: 1.5rem;
  --bs-gutter-y: 0;
  width: 100%;
  padding-right: calc(var(--bs-gutter-x) * 0.5);
  padding-left: calc(var(--bs-gutter-x) * 0.5);
  margin-right: auto;
  margin-left: auto;
}

.item-detail .container .card-category {
  padding-left: 10px;
  font-size: 16px;
}

.item-detail .container .box-outer {
  margin-top: 12px;
  display: grid;
  grid-template-columns: repeat(2, 50%);
  border: 0.5px solid #ddd;
}
.item-detail .container .avg-rating {
  margin-top: 6px;
}
.item-detail .container .avg-rating svg {
  font-size: 16px;
}
.item-detail .container .avg-rating p {
  font-size: 20px;
  color: #17191d;
  letter-spacing: -1px;
  vertical-align: top;
}
.item-detail .container .box-outer .price-outer {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 6px;
}
.item-detail .container .box-outer .price-inner:nth-child(1) {
  display: flex;
  align-items: flex-start;
}
.item-detail .container .box-outer .price-inner:nth-child(1) .price-before {
  margin-top: 6px;
  font-size: 16px;
  font-weight: bold;
  color: #a6a6a6;
}
.item-detail .container .box-outer .price-inner:nth-child(1) .discount {
  font-size: 20px;
  margin-left: 8px;
  border: 0.5px solid #d40022;
  padding: 2px 8px;
  color: #d40022;
}
.item-detail .container .box-outer .price-inner:nth-last-child(1) {
  margin-top: -5px;
  font-size: 28px;
  font-weight: bold;
  color: #17191d;
}
.item-detail .container ul {
  padding: 0;
}
.item-detail .container img {
  width: 100%;
}
.item-detail .container .name {
  display: inline;
  font-size: 30px;
  height: 90px;
  color: #17191d;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical; /* 말줄임 적용 */
  margin-top: 28px;
}
.item-detail .container .box-right {
  padding: 0 38px;
}
.item-detail .container .select-quantity {
  height: 40px;
  display: inline-flex;
}
.item-detail .container .select-quantity > input {
  width: 60px;
  border: 0.5px solid #f1f2f4;
  text-align: center;
  font-weight: bold;
}
.item-detail .container .select-quantity > .quantity-btn {
  width: 40px;
  height: 100%;
  background-color: #f1f2f4;
  color: #17191d;
  border: none;
  font-size: 22px;
  padding: 0 12px 4px;
  font-weight: bold;
}
.item-detail .rating-star {
  color: rgb(228, 153, 42);
}
.item-detail .container .total-price {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 20px 0 20px;
  padding: 18px 16px;
  font-size: 18px;
  width: 100%;
  border-top: 0.5px solid #ddd;
  border-bottom: 0.5px solid #ddd;
}
.item-detail .container .total-price strong:nth-child(1) {
  font-size: 14px;
  color: #17191d;
  line-height: 16px;
}
.item-detail .container .total-price div {
  display: flex;
  align-items: center;
}
.item-detail .container .total-price div em {
  position: relative;
  display: inline-block;
  padding-right: 20px;
  font-size: 16px;
}
.item-detail .container .total-price div em:after {
  position: absolute;
  top: 50%;
  right: 10px;
  width: 1px;
  height: 12px;
  margin-top: -6px;
  background-color: #ddd;
  content: "";
}
.item-detail .container .total-price div strong:nth-last-child(1) {
  display: inline-block;
  margin-top: -1px;
  font-size: 22px;
  line-height: 24px;
  font-weight: bold;
  color: #d40022;
}
.item-detail .container .item-detail-submit {
  display: flex;
  justify-content: center;
}
.item-detail .container .review {
  width: 100%;
  margin-top: 30px;
  padding: 20px 12px;
  border-top: 0.5px solid #ddd;
  border-bottom: 0.5px solid #ddd;
}
.item-detail .container .review .review-top {
  display: inline-flex;
  width: 100%;
  height: 42px;
}
.item-detail .container .review .review-top .review-count {
  color: #333;
  font-weight: bold;
  font-size: 20px;
  display: flex;
  align-items: center;
  margin-left: 8px;
}
.item-detail .container .review .review-top .review-count .review-count-length {
  color: #07f;
  font-size: 16px;
}

.item-detail .container .review .review-bottom {
  position: relative;
  padding: 15px 28px;
  width: 100%;
  border: 0.5px solid #ddd;
}
.item-detail .container .review input {
  width: 100%;
  padding: 13px 15px 14px;
  border: 0.5px solid #ddd;
  line-height: 1.25rem;
  letter-spacing: -0.5px;
  color: #333;
  font-size: 16px;
  height: 4rem;
}
.item-detail .container .dropdown {
  position: relative;
  width: 130px;
  display: inline-block;
}
.item-detail .container .review-score {
  margin-top: 24px;
  display: flex;
  align-items: center;
  justify-content: right;
}
.item-detail .container .dropdown-toggle {
  cursor: pointer;
  height: 38px;
  display: flex;
  align-items: center;
  border: 0.5px solid #ccc;
}
.item-detail .container .dropdown-toggle .dropdown-toggle-outer {
  padding: 0 10px;
}
.item-detail .container .dropdown-toggle::after {
  position: absolute;
  right: 10px;
  color: #333;
}
.item-detail .container .dropdown-menu {
  display: flex;
  flex-direction: column;
  position: absolute;
  background-color: white;
  border: 1px solid #ccc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  --bs-dropdown-min-width: 130px;
}
.item-detail .container .dropdown-item {
  padding: 5px 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
}
.item-detail .container .dropdown-item:hover {
  background-color: #f1f1f1;
}
.item-detail .container .review-submit {
  margin-left: 14px;
}
.item-detail .container .not-login {
  position: relative;
  display: flex;
  padding: 15px 28px;
}
.item-detail .container .all-reviews {
  padding: 20px 12px;
}
.item-detail .container .all-reviews li {
  margin-top: 16px;
}
.item-detail .container .all-reviews-top {
  display: flex;
  flex-direction: column;
}
.item-detail .container .all-reviews-top .all-reviews-top-list1 {
  display: inline-flex;
  flex-direction: row;
  align-items: baseline;
}
.item-detail .container .all-reviews-top .all-reviews-top-list1 p{
  margin-bottom: 5px;
}
.item-detail .container .all-reviews-top .all-reviews-top-list1 .nickname {
  margin-right: 12px;
  font-size: 18px;
  font-weight: 600;
}
.item-detail .container .all-reviews-top .all-reviews-top-list2 {
  font-size: 14px;
  color: #8a8a8a;
}
.item-detail .container .all-reviews-bottom {
  margin-top: 12px;
  margin-left: 10px;
  font-size: 16px;
}
</style>
