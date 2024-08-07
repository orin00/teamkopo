<template>
  <div class="card shadow-sm" style="border-radius: 0">
    <router-link :to="{ name: 'item-detail', params: { id: item.id } }">
      <span class="img" :style="{ backgroundImage: `url(${item.imgPath})` }" />
    </router-link>
    <div class="card-body">
      <p class="card-top">
        <span class="card-category">{{ item.category }}</span>
      </p>
      <p class="card-middle">
        <router-link
          :to="{ name: 'item-detail', params: { id: item.id } }"
          class="card-name"
        >
          {{ item.name }}
        </router-link>
      </p>
      <p class="card-bottom">
        <span class="discount"> {{ item.discountPer }}% </span>
        <span>
          <small class="price">
            {{ lib.getNumberFormatted(item.price) }}원
          </small>
          <small class="real">
            {{
              lib.getNumberFormatted(
                item.price - (item.price * item.discountPer) / 100
              )
            }}원
          </small>
        </span>
      </p>
    </div>
  </div>
</template>

<script>
import lib from '@/scripts/lib'

export default {
  name: 'Card',
  props: {
    item: Object
  },
  setup () {
    return { lib }
  }
}
</script>

<style scoped>
.card .img {
  display: inline-block;
  width: 100%;
  height: 250px;
  background-size: cover;
  background-position: center;
}
.card .card-body {
  padding: 16px 28px 22px;
}
.card .card-body .price {
  text-decoration: line-through;
}
.card .card-body p {
  margin-bottom: 0;
}
.card .card-body .card-top .card-category {
  display: block;
  line-height: 18px;
  font-size: 14px;
  color: #868686;
}
.card .card-body .card-middle {
  margin-top: 8px;
}
.card .card-body .card-middle .card-name {
  font-size: 22px;
  color: #222;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical; /* 말줄임 적용 */
}
.card .card-body .card-bottom {
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card .card-body .card-bottom .discount {
  font-size: 20px;
  border: 0.5px solid #d40022;
  padding: 2px 8px;
  color: #d40022;
}
.card .card-body .card-bottom span .price {
  font-size: 14px;
  color: #a6a6a6;
}
.card .card-body .card-bottom span .real {
  margin-top: -5px;
  margin-left: 8px;
  font-size: 22px;
  font-weight: bold;
  color: #17191d;
}
.card.shadow-sm {
  overflow: hidden;
}
</style>
