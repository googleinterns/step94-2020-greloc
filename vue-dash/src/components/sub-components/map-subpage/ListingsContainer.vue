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
      v-on:input="officeSelectedEvent(selectedOffice)"
      return-object
      solo
    ></v-select>

    <DateRangeSelector/>

    <Listings 
      v-if="(selectedOffice.officeId != 'default') && (listings.length > 0)"
      :listings="listings"
    />

    <div 
      v-else-if="(selectedOffice.officeId != 'default') && (listings.length === 0)"
      id="listings-empty-state" 
    >
      <v-icon class="empty-state-icon">mdi-emoticon-frown-outline</v-icon>
      <span class="empty-state-text">Sorry, there are no available listings for these parameters</span>
    </div>

    <div id="listings-empty-state" v-else>
      <v-icon class="empty-state-icon">mdi-map-marker</v-icon>
      <span class="empty-state-text">Select an office from the dropdown to view listings near that area</span>
    </div>    

  </div>
</template>

<script>
import Listings from './Listings.vue'
import DateRangeSelector from './DateRangeSelector.vue'
import { OFFICES } from '../../../utils/constants.js'

export default {
  name: 'ListingsContainer',
  components: {
    Listings,
    DateRangeSelector
  },

  props: {
    officeSelectedEvent: Function,
    listings: Array
  },

  data: () => ({
    selectedOffice: { name: "", officeId: "default" },
    offices: OFFICES
  }),

  methods: {

  }
}
</script>

<style scoped>

  #listings-cont {
    min-width: 550px;
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

  #listings-empty-state {
    width: 100%;
    height: 100%;
    padding: 4rem 20%;
    
    display: flex;    
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    
    text-align: center;    
  }

</style>
