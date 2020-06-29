<template>
  <div id="map-cont">
    <div id="map-empty-state" v-if="selectedOffice === null || selectedOffice.officeId === 'default'">
      <v-icon class="empty-state-icon">mdi-map-marker</v-icon>
      <span class="empty-state-text">Select an office from the dropdown to view listings near that area</span>
    </div>
    <ReloMap
       :listings="listings"
       :style="[(selectedOffice === null || selectedOffice.officeId === 'default') 
        ? {'display': 'none'} : {'display': 'block'}]"
    />
  </div>
</template>

<script>
import ReloMap from './ReloMap.vue'
export default {
  name: 'MapContainer',
  components: {
    ReloMap
  },
  props: {
    listings: Array,
    selectedOffice: Object
  },
  data: () => ({

  }),
  methods: {
    
  }
}
</script>

<style scoped>
  #map-cont {
    position: relative;
    height: 100%;
    width: 100%;

    display: flex;
    align-items: center;
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
