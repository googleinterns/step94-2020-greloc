import App from './App.vue';
import Vue from 'vue';
import vuetify from '@/plugins/vuetify';
import VueRouter from 'vue-router';
import MapSubpage from './components/MapSubpage.vue';
import MyListingsSubpage from './components/MyListingsSubpage.vue';
import {authCheck} from './utils/authmanager.js';


Vue.config.productionTip = false;
Vue.use(VueRouter);

const routes = [
  { path: '/', redirect: '/map' },
  { name: "Map", path:"/map", component: MapSubpage },
  { name: "MyListings", path:"/mylistings", component: MyListingsSubpage },

];

const router = new VueRouter({
  routes
});

// Checks if user is authorized before routing to a new page. Redirects to landing page if not.
router.beforeEach((to, from, next) => {
  authCheck()
    .then(() => {
      next()
    })
    .catch(() => {
      window.location = "/"
      next(false)
    })
});

new Vue({
  vuetify,
  router,
  render: h => h(App),
}).$mount('#app');

