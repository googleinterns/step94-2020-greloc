<template>
  <div id="listings-cont">
    <h1 class="subpage-title">Find Accomodations</h1>
    <v-select
      :items="offices"
      label="Select Office"
      id="select-office"
      style="width: 100%;"
      height="50px"
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

  data: () => ({
    offices: [
      "Sunnyvale",
      "Mountain View"
    ],

    listings: [
      {
        name: "Long Stay Villa",
        type: "Full House",
        roomsInfo: "6 Guests | 4 Bedroom | 6 Beds | 3 Bath",
        thumbnail: "assets/long_stay.png",
        price: "$5,000",
        googlerOwned: true
      },

      {
        name: "Long Stay Villa",
        type: "Full House",
        roomsInfo: "6 Guests | 4 Bedroom | 6 Beds | 3 Bath",
        thumbnail: "assets/long_stay.png",
        price: "$5,000",
        googlerOwned: true
      }      
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
