import App from './App.vue';
import Vue from 'vue';
import vuetify from '@/plugins/vuetify';
import VueRouter from 'vue-router';
import MapSubpage from './components/MapSubpage.vue';
import MyListingsSubpage from './components/MyListingsSubpage.vue';


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

new Vue({
  vuetify,
  router,
  render: h => h(App),
}).$mount('#app');

