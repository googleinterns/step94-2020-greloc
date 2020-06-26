<template>
  <div id="listings-cont">
    <h1 class="subpage-title">Find Accomodations</h1>
    <v-select
      :items="offices"
      label="Select Office"
      id="select-office"
      style="width: 100%;"
      height="50px"
      v-model="selectedOffice"
      item-text="name"
      item-value="value"
      v-on:input="getListings(selectedOffice)"
      return-object
      solo
    ></v-select>

    <DateRangeSelector/>

    <Listings :listings="listings"/>
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

  props: {
    getListings: Function,
    listings: Array
  },

  data: () => ({
    selectedOffice: { name: "", officeId: "default" },
    offices: [
      { name: "Sunnyvale", officeId: "svl" },
      { name: "Mountain View", officeId: "mtv" },
    ]
  }),

  methods: {

  }
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
