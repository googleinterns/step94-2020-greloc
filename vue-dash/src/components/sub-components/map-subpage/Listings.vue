<template>
  <v-list 
    id="listings-component"
  >
    <v-list-item-group v-model="selectedListing">
      <v-list-item
        v-for="(listing, index) in listings"
        :key="index"
        @click="onUserSelectsListing(listing, index)"
        class="pa-0"
      >
        <Listing :listingInfo="listing"/>
        <v-divider></v-divider>
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
import Listing from './Listing.vue'
import { EVENTS } from '../../../utils/constants.js'

export default {
  name: 'Listings',
  created () {    
    this.$root.$on(EVENTS.listingDeselected, () => {
      this.clearSelection();
    });
  },

  components: {
    Listing
  },
  props: {
    listings: Array
  },

  data: () => ({
    selectedListing: null,
  }),

  methods: {
    onUserSelectsListing: function(listing, index) {
      if (this.selectedListing != null && this.selectedListing === index) {
        this.clearSelection();
      } else {
        this.$root.$emit(EVENTS.listingSelected, listing);
      }
    },

    clearSelection: function () {
      this.selectedListing = null;
      this.$root.$emit(EVENTS.listingSelected, null);
    }
  }
}
</script>

<style scoped>
  #listings-component {
    height: 100%;
    width: 100%;

    background-color: transparent;
    overflow-y: scroll;
  }
</style>
