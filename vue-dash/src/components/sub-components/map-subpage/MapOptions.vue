<template>
  <div id="map-options" v-if="office != null">
    <h1>Map Options</h1>
    
    <v-checkbox
      class="marker-checkbox"    
      @change="toggledBusStops(busStopSelected)"
      v-model="busStopSelected"
      label="Bus Stops"
      color="var(--branding-blue)"
      :dense="$vuetify.breakpoint.mobile"
    ></v-checkbox>

    <v-checkbox
      class="marker-checkbox"    
      @change="toggledPoi(recreationSelected, markerTypeEnum.RECREATION)"
      v-model="recreationSelected"
      label="Recreation"
      color="var(--branding-red)"
      :dense="$vuetify.breakpoint.mobile"
    ></v-checkbox>

    <v-checkbox
      class="marker-checkbox"    
      @change="toggledPoi(diningSelected, markerTypeEnum.DINING)"
      v-model="diningSelected"
      label="Dining"
      color="var(--branding-yellow)"
      :dense="$vuetify.breakpoint.mobile"
    ></v-checkbox>

    <v-checkbox
      class="marker-checkbox"
      @change="toggledPoi(grocerySelected, markerTypeEnum.GROCERY)"
      v-model="grocerySelected"
      label="Grocery"
      color="var(--branding-green)"
      :dense="$vuetify.breakpoint.mobile"
    ></v-checkbox>        
  </div>
</template>

<script>
import { WEBSITE_URL, EVENTS, MarkerTypeEnum, COLORS } from '../../../utils/constants.js'

const poiLoadingColorDict =  {
  [MarkerTypeEnum.RECREATION]: COLORS.BRANDING_RED,
  [MarkerTypeEnum.DINING]: COLORS.BRANDING_YELLOW,
  [MarkerTypeEnum.GROCERY]: COLORS.BRANDING_GREEN,
}

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
    busStopSelected: false,
    recreationSelected: false,
    diningSelected: false,
    grocerySelected: false,
    markerTypeEnum: MarkerTypeEnum

  }),
  
  methods: {

    onOfficeSelected: async function (office){
      this.office = office;
      
      // If busStopSelected is already selected once a new office is chosen, automatically get the stops for that office
      if (this.busStopSelected) {
        let stopList = await this.getBusStops(office);
        this.$root.$emit(EVENTS.busStopsSelected, stopList);
      }
    },

    toggledBusStops: async function (selected){
      let stopList = [];
      
      if (selected) {
        stopList = await this.getBusStops(this.office);
      }
      this.$root.$emit(EVENTS.busStopsSelected, stopList);
    },

    toggledPoi: async function (selected, markerType){
      let placesData = [];      
      if (selected) {
        placesData = await this.getPoiData(this.office, markerType);
      }

      let finalData = {
        markerType: markerType,
        poiList: placesData
      };
      
      this.$root.$emit(EVENTS.poiSelected, finalData);
    },

    getBusStops: async function (office){

      this.$root.$emit(EVENTS.mapSubpageLoading, {
        isLoading: true,
        color: COLORS.BRANDING_BLUE,
        forEvent: EVENTS.busStopsSelected 
      });

      let response = await fetch(WEBSITE_URL + `/busLocations?office=${office.officeId}`);
      let respData;
      
      if (response.ok) {                
        respData = await response.json();
      } else {
        respData = [];
      }
      
      this.$root.$emit(EVENTS.mapSubpageLoading, {
        isLoading: false,
        forEvent: EVENTS.busStopsSelected
      });

      return respData;
    },

    getPoiData: async function (office, markerType){

      this.$root.$emit(EVENTS.mapSubpageLoading, {
        isLoading: true,
        color: poiLoadingColorDict[markerType],
        forEvent: EVENTS.poiSelected 
      });

      let response = await fetch(WEBSITE_URL + `/poi?office=${office.officeId}&group=${markerType}`);
      let respData;
      
      if (response.ok) {                
        respData = await response.json();
      } else {
        respData = [];
      }
      
      this.$root.$emit(EVENTS.mapSubpageLoading, {
        isLoading: false,
        forEvent: EVENTS.poiSelected 
      });
      return respData;
    }
  }
}
</script>

<style scoped>

  #map-options {
    height: auto;
    width: 300px;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;

    padding: 16px;
    position: absolute;
    bottom: 20px;
    left: 10px;

    background: #FAFAFA;
    box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2), 0px 2px 2px rgba(0, 0, 0, 0.12), 0px 0px 2px rgba(0, 0, 0, 0.14);
    border-radius: 2px;
  }

  h1 {
    color: #666666;
    font-size: 22px;
    margin-bottom: 1rem;
  }  
  
  .marker-checkbox {
    margin: 0;
  }

  @media screen and (max-width: 1025px) {
    h1 {
      font-size: 18px;
      margin-bottom: 0.5rem;
    }
    
    #map-options {
      width: auto;
      padding: 16px 16px 0px 16px;
    }
  }

</style>
