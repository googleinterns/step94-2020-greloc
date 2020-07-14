<template>
    <div class="subpage" id="map-subpage">
        <v-progress-linear 
          absolute 
          indeterminate
          color="var(--branding-blue)"
          height="10px"
          :active="isLoading"
        ></v-progress-linear>
      <ListingsContainer :officeSelectedEvent="officeSelectedEvent" :listings="listings"/>
      <MapContainer :dateRange="selectedDateRange" :selectedOffice="selectedOffice" :listings="listings"/>
    </div>
</template>

<script>
import ListingsContainer from './sub-components/map-subpage/ListingsContainer.vue'
import MapContainer from './sub-components/map-subpage/MapContainer.vue'
import { WEBSITE_URL, EVENTS } from '../utils/constants.js'

const testComp = {
  name: "Cozy Apartment - July 14",
  price: "$2,500",
  type: "Full Apartment",
  desc: "Tidy apartment with beatiful furniture",
  images: [
    "https://stmedia.stimg.co/1010121201_mavenrendering.jpg?fit=crop&crop=faces",
    "https://cloudfront-us-east-1.images.arcpublishing.com/gray/EJ77UNGM7VG3XGH5TVTPIGODEU.jpg",
  ],
  beds: 3,
  bedrooms: 2,
  baths: 2,
  guests: 3,
  contactInfo: {
    name: "Emily Pierre",
    phone: "777-777-777",
    email: "epierre@google.com"
  },
  googlerOwned: true,
  listingStartTimestamp: 1594684800, // July 14th
  listingEndTimestamp: 1595368312,// July 21st
  latitude: 37.395720,
  longitude: -122.028570,
};

export default {
  name: 'Template',

  created() {
    this.$root.$on(EVENTS.mapSubpageLoading, isLoading => {
      this.isLoading = isLoading;
    });

    this.$root.$on(EVENTS.dateRangeSelected, dateRange => {
      this.dateRangeSelectedEvent(dateRange);
    });

    // this.createListing();
  },

  components: {
    ListingsContainer,
    MapContainer
  },

  props: {
    userData: Object
  },

  data: () => ({
    listings: [],
    offices: [],
    selectedOffice: null,
    selectedDateRange: null,
    isLoading: false
  }),

  methods: {

    officeSelectedEvent: function(office) {
      this.selectedOffice = office;
      this.$root.$emit(EVENTS.officeChanged, office);

      this.attemptRetrieveListings(office);
    },

    dateRangeSelectedEvent: function(dateRange) {
      this.selectedDateRange = dateRange;
      this.attemptRetrieveListings(this.selectedOffice);
    },    

    attemptRetrieveListings: async function(office) {

      if (this.getListingsConditionsNotMet()) {
        return;
      }

      this.isLoading = true;
      let startDateTimestamp = new Date(this.selectedDateRange[0]).getTime() / 1000;
      let endDateTimestamp = new Date(this.selectedDateRange[1]).getTime() / 1000;

      console.log(startDateTimestamp);
      console.log(endDateTimestamp);
      let response = await fetch(WEBSITE_URL + `/locations?office=${office.officeId}&start=${startDateTimestamp}&end=${endDateTimestamp}`);
      let respData;
      
      if (response.ok) {                
        respData = await response.json();
      } else {
        respData = [];
      }

      this.listings = respData;
      this.$root.$emit(EVENTS.newListings, respData);
      this.isLoading = false;
    },

    getListingsConditionsNotMet: function() {      
      return this.selectedOffice == null || this.selectedDateRange == null;
    },    

    createListing: async function() {
        
      let response = await fetch(WEBSITE_URL + '/locations', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(testComp)
      });

      if (response.ok) {
        // TODO: tell user listing created succesfully
        console.log("createListing: Success");
      } else {
        // TODO: error handling
        console.log("createListing: error");
      }
    },
  },

}
</script>

<style scoped>
  #map-subpage {
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
    padding-bottom: 0;
    padding-right: 0;
  }
</style>
