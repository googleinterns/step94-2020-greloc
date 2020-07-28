<template>
  <div id="mylistings-cont">
    <h1 class="subpage-title">My Current Listings</h1>
    <Listings :listings="listings"/>
  </div>
</template>

<script>
import Listings from '../map-subpage/Listings.vue'

export default {
  name: 'MyListingsContainer',
  components: {
    Listings
  },

  data: () => ({
  }),

  methods: {

  },

  props : {
    listings: Array,
  },
}
</script>

<style scoped>
  #listings-cont {
    width: 550px;
    height: 100%;

    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    
    border-right: 1px solid #dbdbdb;
    padding: 16px;
  }

  #select-office {
    width: 100%;
  }

</style>
