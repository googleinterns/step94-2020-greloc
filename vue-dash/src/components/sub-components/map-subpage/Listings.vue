<template>
  <div id="listings-component">
    <Listing/>
  </div>
</template>

<script>
import Listing from './Listing.vue'

export default {
  name: 'Listings',
  components: {
    Listing
  },
  props: {
    listings: Array
  }
}
</script>

<style scoped>
  #listings-component {
    height: 100%;
    width: 100%;
    background-color: cornflowerblue;
  }
</style>
