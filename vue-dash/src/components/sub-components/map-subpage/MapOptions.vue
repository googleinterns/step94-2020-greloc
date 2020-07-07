<template>
  <div id="map-options" v-if="office != null">
    <v-checkbox
      @change="toggledBusStops(busStopSelected)"
      v-model="busStopSelected"
      label="Bus Stops"
      color="var(--branding-blue)"
    ></v-checkbox>    
  </div>
</template>

<script>
import { WEBSITE_URL, EVENTS } from '../../../utils/constants.js'

export default {
  name: 'MapOptions',

  created () {
    this.$root.$on(EVENTS.officeChanged, (office) => {
      this.onOfficeSelected(office);
    });
  },

  components: {
  },
  props: {
    msg: String
  },
  
  data: () => ({
    office: null,
    busStopSelected: false
  }),
  
  methods: {

    onOfficeSelected: async function (office){
      this.office = office;
      
      // If busStopSelected is already selected once a new office is chosen, automatically get the stops for that office
      if (this.busStopSelected) {
        let stopList = await this.getBusStops(office);
        this.$root.$emit(EVENTS.busStopsSelected, stopList)
      }
    },

    toggledBusStops: async function (selected){
      let stopList = [];
      
      if (selected) {
        stopList = await this.getBusStops(this.office);
      }
      
      console.log(selected);
      console.log(stopList);
      this.$root.$emit(EVENTS.busStopsSelected, stopList);
    },

    getBusStops: async function (office){
      let response = await fetch(WEBSITE_URL + `/busLocations?office=${office.officeId}`);
      let respData;
      
      if (response.ok) {                
        respData = await response.json();
      } else {
        respData = [];
      }

      return respData;
    }
  }
}
</script>

<style scoped>
  #map-options {
    height: 200px;
    width: 200px;

    padding: 16px;
    position: absolute;
    bottom: 20px;
    left: 20px;

    background: #FAFAFA;
    box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2), 0px 2px 2px rgba(0, 0, 0, 0.12), 0px 0px 2px rgba(0, 0, 0, 0.14);
    border-radius: 2px;
  }


</style>
