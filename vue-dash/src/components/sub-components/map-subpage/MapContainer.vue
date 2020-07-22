<template>
  <div id="map-cont"
    :style="[showMapRequirementsNotMet
    ? {'align-items': 'center'} : {'align-items': 'flex-end'}]"
  >
    <div id="map-empty-state" v-if="showMapRequirementsNotMet">
      <v-icon class="empty-state-icon">mdi-map</v-icon>
      <span class="empty-state-text">A map of all available listings will appear here once an office is selected</span>
    </div>
    
    <ReloMap
       :listings="listings"
       :style="[showMapRequirementsNotMet
        ? {'display': 'none'} : {'display': 'block'}]"
    />
        
    <MapOptions 
      :style="[showMapToolsRequirementsNotMet
      ? {'display': 'none'} : {'display': 'block'}]"
    />

    <GmapsAutoComplete
      :forReloMap="true"
      :style="[showMapToolsRequirementsNotMet
      ? {'display': 'none'} : {'display': 'flex'}]"    
    />

    <ListingInfo
      v-if="selectedListing != null"
      :selectedListing="selectedListing"
    />
  </div>
</template>

<script>
import ReloMap from './ReloMap.vue'
import MapOptions from './MapOptions.vue'
import GmapsAutoComplete from './GmapsAutoComplete.vue'
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
    MapOptions,
    GmapsAutoComplete
  },
  props: {
    listings: Array,
    selectedOffice: Object,
    dateRange: Array,
    selectedListing: Object,
  },
  data: () => ({
    google: null
  }),

  computed: {
    showMapRequirementsNotMet: function() {
      return this.selectedOffice === null || this.selectedOffice.officeId === 'default' || this.dateRange === null;
    },

    showMapToolsRequirementsNotMet: function() {
      return this.selectedOffice === null || this.selectedOffice.officeId === 'default' || this.dateRange === null || this.selectedListing != null;
    }    
  },

  methods: {
    listingSelectedEvent: function(listing) {
      console.log("weee");
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
