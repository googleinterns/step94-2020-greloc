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
