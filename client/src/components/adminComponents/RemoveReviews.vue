<template>
  <div class="admin_tab">
    <div class="tab_title">
      <h1>댓글 삭제 탭</h1>
    </div>
    <div class="admin_contents">
      <!-- 상품 아이디 입력 폼 -->
      <form @submit.prevent="fetchReviews">
        <label>상품 아이디:</label>
        <input type="text" v-model="itemId">
        <button type="submit">조회</button>
      </form>

      <!-- 댓글 목록 표시 -->
      <ul v-if="reviews.length > 0">
        <li v-for="review in reviews" :key="review.id">
          <div>
            <p><strong>닉네임:</strong> {{ review.nickname }}</p>
            <p><strong>내용:</strong> {{ review.text }}</p>
            <p><strong>평점:</strong> {{ review.rating }}</p>
            <p><strong>작성일시:</strong> {{ review.createdAt }}</p>
            <button @click="deleteReview(review.id)">삭제</button>
          </div>
          <hr>
        </li>
      </ul>
      <p v-else>댓글이 없습니다.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'RemoveReviews',
  data () {
    return {
      itemId: '',
      reviews: []
    }
  },
  methods: {
    // 상품 아이디를 기반으로 댓글 목록 가져오기
    fetchReviews () {
      axios.get(`/api/items/${this.itemId}/reviews`)
        .then(response => {
          this.reviews = response.data
        })
        .catch(error => {
          console.error('댓글 목록을 가져오는 중 에러:', error)
        })
    },
    // 댓글 삭제
    deleteReview (reviewId) {
      axios.delete(`/api/items/reviews/${reviewId}`)
        .then(response => {
          // 삭제 후 댓글 목록 다시 가져오기
          this.fetchReviews()
          console.log('댓글 삭제 완료')
        })
        .catch(error => {
          console.error('댓글 삭제 중 에러:', error)
        })
    }
  }
}
</script>

<style>
/* 필요한 스타일링을 추가할 수 있음 */
</style>
