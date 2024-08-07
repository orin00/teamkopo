import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faShoppingCart, faTrash, faArrowRight, faMagnifyingGlass, faStar, faStarHalf } from '@fortawesome/free-solid-svg-icons'

library.add(faShoppingCart)
library.add(faTrash)
library.add(faArrowRight)
library.add(faMagnifyingGlass)
library.add(faStar)
library.add(faStarHalf)

createApp(App).use(store).use(router).component('font-awesome-icon', FontAwesomeIcon).mount('#app')
