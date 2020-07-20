<template>
    <div class="subpage" id="map-subpage">
        <v-progress-linear 
          absolute 
          indeterminate
          :color="loadingBarColor"
          height="10px"
          :active="isLoading"
        ></v-progress-linear>
      <ListingsContainer :dateRange=selectedDateRange :officeSelectedEvent="officeSelectedEvent" :listings="listings"/>
      <MapContainer :dateRange="selectedDateRange" :selectedOffice="selectedOffice" :listings="listings"/>
    </div>
</template>

<script>
import ListingsContainer from './sub-components/map-subpage/ListingsContainer.vue'
import MapContainer from './sub-components/map-subpage/MapContainer.vue'
import { WEBSITE_URL, EVENTS, COLORS } from '../utils/constants.js'

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
  listingStartDate: 1594684800000, // July 14th
  listingEndDate: 1595368312000,// July 21st
  latitude: 37.395720,
  longitude: -122.028570,
};

export default {
  name: 'Template',

  created() {
    this.$root.$on(EVENTS.mapSubpageLoading, loadingEventData => {
      this.loadingEvent(loadingEventData);
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
    
    
    isLoading: false,
    loadingBarColor: "#4285f4"
  }),

  methods: {

    loadingEvent: function(loadingEventData){
      if (loadingEventData.isLoading) {
        this.loadingBarColor = loadingEventData.color ? loadingEventData.color : COLORS.BRANDING_BLUE;
        this.isLoading = true;
      } else {
        this.isLoading = false;
      }
    },

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

      this.$root.$emit(EVENTS.mapSubpageLoading, {
        isLoading: true,
        color: COLORS.BRANDING_BLUE,
        forEvent: EVENTS.newListings 
      });

      let startDateTimestamp = new Date(this.selectedDateRange[0]).getTime();
      let endDateTimestamp = new Date(this.selectedDateRange[1]).getTime();

      let response = await fetch(WEBSITE_URL + `/locations?office=${office.officeId}&startMillis=${startDateTimestamp}&endMillis=${endDateTimestamp}`);
      let respData;
      
      if (response.ok) {                
        respData = await response.json();
      } else {
        respData = [];
      }

      this.listings = respData;
      this.$root.$emit(EVENTS.newListings, respData);
      this.$root.$emit(EVENTS.mapSubpageLoading, {
        isLoading: false,
        forEvent: EVENTS.newListings 
      });
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
