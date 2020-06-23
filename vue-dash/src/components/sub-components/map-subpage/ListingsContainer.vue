<template>
  <div id="listings-cont">
    <h1 class="subpage-title">Find Accomodations</h1>
    <v-select
      :items="offices"
      label="Select Office"
      id="select-office"
      style="width: 100%; margin-bottom: 0;"
      height="50px"
      solo
    ></v-select>

    <DateRangeSelector/>

    <Listings :listings="[]"/>
  </div>
</template>

<script>
import Listings from './Listings.vue'
import DateRangeSelector from './DateRangeSelector.vue'

export default {
  name: 'ListingsContainer',
  components: {
    Listings,
    DateRangeSelector
  },

  data: () => ({
    offices: [
      "Sunnyvale",
      "Mountain View"
    ]
  }),

  methods: {

  }
}
</script>

<style scoped>

  #listings-cont {
    width: 500px;
    height: 100%;

    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;

    background-color: #dbdbdb;
    padding: 16px;
  }

  #select-office {
    width: 100%;
  }

</style>
