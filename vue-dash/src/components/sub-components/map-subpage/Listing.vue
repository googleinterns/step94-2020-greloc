<template>
  <div class="indv-listing">
    <img class="listing-thumbnail" :src="listingInfo.propertyMap.images[0]" alt="listing-image">
    <div class="info-box">
      <div class="top-row">
        <h1>{{listingInfo.propertyMap.name}}</h1>
        <span>{{listingInfo.propertyMap.type}}</span>
        <span>{{`${listingInfo.propertyMap.guests} Guests | ${listingInfo.propertyMap.bedrooms} Bedroom | ${listingInfo.propertyMap.beds} Beds | ${listingInfo.propertyMap.baths} Bath`}}</span>
      </div>

      <div class="bottom-row">
        <v-avatar size="30">
          <img v-if="listingInfo.propertyMap.googlerOwned" src="assets/google.svg" alt="Google">
          <div v-else class="unknown-source-symbol"></div>
        </v-avatar>
        <span><b>{{listingInfo.propertyMap.price}}</b>/month</span>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: 'Listing',
  props: {
    listingInfo: Object
  },

  data: () => ({
    isSelected: false
  }),
}
</script>

<style scoped>
  .indv-listing {
    height: 160px;
    width: 100%;

    display: flex;
    align-items: center;
    justify-content: flex-start;

    padding: 10px 0;
  }
  
  .listing-thumbnail {
    height: 140px;
    width: 140px;
    margin-right: 0.5rem;
  }  

  .info-box {
    height: 100%;
    width: calc(100% - 140px);

    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .top-row {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
  }

  .top-row h1{
    font-size: 24px;
    font-weight: normal;
  }

  .bottom-row {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
  }

  .unknown-source-symbol {
    height: 100%; 
    width:100%; 
    background-color: transparent;
  }
</style>
