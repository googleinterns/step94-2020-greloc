<template>
  <div :id="forReloMap ? 'gmaps-auto-complete-container': 'gmaps-auto-complete-container-edit-listing'">
    <div 
      id="relo-search-container"
      :style="[showClearMarkers
      ? {'width': '80%'} : {'width': '95%'}]"
    >
      <input v-model="userInput" type="search" id="relo-search-component">
      <button 
        v-if="userInputLength > 0" 
        v-ripple
        v-on:click="clearText()"
      >✕</button>
    </div>
    
    <button 
      v-if="forReloMap && showClearMarkers"
      id="clear-markers-button" 
      v-on:click="clearMarkers()"
    >Clear Markers</button>
  </div>
</template>

<script>
import gmapsInit from '../../../utils/gmaps.js'
import { EVENTS } from '../../../utils/constants.js'

export default {
  name: 'GmapsAutoComplete',
  async created () {
    await this.initializeGoogleMaps();
    if (this.forReloMap) {
      this.createPlacesApiListenerForReloMap();
    } else {
      this.createPlacesApiListenerForEditListing();
    }      
  },

  beforeDestory () {
    // Remove event listener created in initializeGoogleMaps()
    this.google.maps.event.removeListener(this.autocompleteListener);    
  },

  props: {
    forReloMap: Boolean
  },

  computed: {
    userInputLength: function () {
      return this.userInput.length;
    }
  },

  data: () => ({
    google: null,
    autocomplete: null,
    address: "",
    userInput: "",

    showClearMarkers: false
  }),

  methods: {
    initializeGoogleMaps: async function () {
      this.google = await gmapsInit();

      let input = document.getElementById('relo-search-component');
      this.autocomplete = new this.google.maps.places.Autocomplete(input);
    },

    createPlacesApiListenerForReloMap: function() {
      this.autocompleteListener = this.autocomplete.addListener('place_changed', function() {        
          if (this.autocomplete.get('place') === null) return;

          let place = this.autocomplete.getPlace();

          // User entered name of a Place that was not suggested / Place Details request failed.
          if (!place.geometry) {
            window.alert("No details available for input: '" + place.name + "'");
            return;
          }
          
          let address = '';
          if (place.address_components) {
            address = [
              (place.address_components[0] && place.address_components[0].short_name || ''),
              (place.address_components[1] && place.address_components[1].short_name || ''),
              (place.address_components[2] && place.address_components[2].short_name || '')
            ].join(' ');
          }

          this.$root.$emit(EVENTS.userPlaceSearch, place);
          this.userInput = place.name + ', ' + address;
          this.showClearMarkers = true;
      }.bind(this));
    },

    createPlacesApiListenerForEditListing: function() {
      this.autocompleteListener = this.autocomplete.addListener('place_changed', function() {
          if (this.autocomplete.get('place') === null) return;

          let place = this.autocomplete.getPlace();
          if (!place.geometry) {
            window.alert("No details available for input: '" + place.name + "'");
            return;
          }
          
          this.$root.$emit(EVENTS.userSelectedAddress, place);

      }.bind(this));
    },

    clearText: function () {
      this.userInput = "";
      this.autocomplete.set('place', null);
    },

    clearMarkers: function () {
      this.$root.$emit(EVENTS.clearPlaceSearchMakers);
      this.showClearMarkers = false;
      this.autocomplete.set('place', null);
    }
  }
}
</script>

<style scoped>
  #gmaps-auto-complete-container {
    position: absolute;
    height: 56px;
    width: calc(100% - 20px);
    
    align-items: center;
    justify-content: space-between;

    top: 60px;
    left: 10px;
    z-index: 12;

    background: #FAFAFA;
    box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2), 0px 2px 2px rgba(0, 0, 0, 0.12), 0px 0px 2px rgba(0, 0, 0, 0.14);
  }

  #gmaps-auto-complete-container-edit-listing {
    position: relative;
    height: 56px;
    width: 500px;
    
    align-items: center;
    justify-content: space-between;

    background: #FAFAFA;
    box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2), 0px 2px 2px rgba(0, 0, 0, 0.12), 0px 0px 2px rgba(0, 0, 0, 0.14);
  }


  #relo-search-container {
    height: 100%;    
  }

  #relo-search-component {
    height: 100%;
    width: 95%;
    flex: 1;
    padding: 16px;
  }

  #clear-markers-button {
    height: 100%;
    width: 19%;
    background-color: var(--branding-blue);
    color: #ffffff;
    margin-left: 10px;
    border-radius: 0px 2px 2px 0px;
  }

</style>
