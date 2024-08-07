<template>
  <div class="admin_tab">
    <div class="tab_title">
      <h1>상품 추가 탭</h1>
    </div>
    <div class="admin_contents">
      <form @submit.prevent="addItem">
        <div>
          <label for="name">상품 이름:</label>
          <input type="text" id="name" v-model="item.name" required />
        </div>
        <div>
          <label for="imgPath">이미지 이름:</label>
          <input type="text" id="imgPath" v-model="displayImgPath" @input="updateImagePath" />
        </div>
        <div>
          <label for="price">가격:</label>
          <input type="number" id="price" v-model="item.price" required />
        </div>
        <div>
          <label for="discountPer">할인율:</label>
          <input type="number" id="discountPer" v-model="item.discountPer" />
        </div>
        <div>
          <label>카테고리:</label>
          <span>{{ item.category }}</span>
          <div class="category-buttons">
            <button type="button" @click="setCategory('apple')">Apple</button>
            <button type="button" @click="setCategory('banana')">Banana</button>
            <button type="button" @click="setCategory('grape')">Grape</button>
          </div>
        </div>
        <button type="submit">상품 추가</button>
      </form>
      <div v-if="message">
        <p>{{ message }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AddItems',
  data () {
    return {
      item: {
        name: '',
        imgPath: '/img/',
        price: null,
        discountPer: null,
        category: ''
      },
      message: '',
      displayImgPath: ''
    }
  },
  watch: {
    // Watch the actual imgPath to update displayImgPath
    'item.imgPath': function (newVal) {
      this.displayImgPath = newVal.replace('/img/', '')
    }
  },
  methods: {
    async addItem () {
      try {
        console.log('Sending item:', this.item)
        await axios.post('/api/items', this.item)
        this.message = '상품이 성공적으로 추가되었습니다.'
        this.resetForm()
      } catch (error) {
        this.message = '상품 추가에 실패했습니다.'
        console.error(error)
      }
    },
    resetForm () {
      this.item = {
        name: '',
        imgPath: '/img/',
        price: null,
        discountPer: null,
        category: ''
      }
      this.displayImgPath = ''
    },
    updateImagePath (event) {
      // Update the item.imgPath based on the user input
      const filename = event.target.value
      this.item.imgPath = `/img/${filename}`
    },
    setCategory (category) {
      this.item.category = category
    }
  },
  mounted () {
    // Initialize displayImgPath when component mounts
    this.displayImgPath = this.item.imgPath.replace('/img/', '')
  }
}
</script>

<style scoped>
.admin_tab {
  padding: 20px;
}

.tab_title {
  margin-bottom: 20px;
}

.admin_contents {
  display: flex;
  flex-direction: column;
}

form {
  display: flex;
  flex-direction: column;
}

form > div {
  margin-bottom: 10px;
}

label {
  margin-bottom: 5px;
}

input {
  padding: 5px;
  font-size: 14px;
}

button {
  padding: 10px;
  font-size: 16px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.category-buttons button {
  margin-right: 10px;
}

.category-buttons span {
  margin-left: 10px;
  font-weight: bold;
  color: #007bff;
}
</style>
