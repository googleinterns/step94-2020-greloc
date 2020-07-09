<template>
  <div id="map-cont"
    :style="[(selectedOffice === null || selectedOffice.officeId === 'default') 
    ? {'align-items': 'center'} : {'align-items': 'flex-end'}]"
  >
    <div id="map-empty-state" v-if="(selectedOffice === null || selectedOffice.officeId === 'default')">
      <v-icon class="empty-state-icon">mdi-map</v-icon>
      <span class="empty-state-text">A map of all available listings will appear here once an office is selected</span>
    </div>
    
    <ReloMap
       :listings="listings"
       :style="[(selectedOffice === null || selectedOffice.officeId === 'default') 
        ? {'display': 'none'} : {'display': 'block'}]"
    />
        
    <MapOptions/>

    <ListingInfo 
      v-if="selectedListing != null"
      :selectedListing="selectedListing"
    />
  </div>
</template>

<script>
import ReloMap from './ReloMap.vue'
import MapOptions from './MapOptions.vue'
import ListingInfo from './ListingInfo.vue'
import { EVENTS } from '../../../utils/constants.js'
export default {
  name: 'MapContainer',

  created() {    
    this.$root.$on(EVENTS.listingSelected, data => {
      this.listingSelectedEvent(data);
    });    
  },

  components: {
    ReloMap,
    ListingInfo,
    MapOptions
  },
  props: {
    listings: Array,
    selectedOffice: Object,
    selectedListing: Object
  },
  data: () => ({
    
  }),
  methods: {
    listingSelectedEvent: function(listing) {
      this.selectedListing = listing;
    }
  }
}
</script>

<style scoped>
  #map-cont {
    position: relative;
    height: 100%;
    width: 100%;

    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  #map-empty-state {
    width: 300px;
    
    display: flex;    
    flex-direction: column;
    align-items: center;
    justify-content: center;
    
    text-align: center;
  }
</style>
