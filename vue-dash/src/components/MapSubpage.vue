<template>
  <div class="subpage" id="map-subpage">
    <v-progress-linear 
      absolute 
      indeterminate
      :color="loadingBarColor"
      height="10px"
      :active="isLoading"
    ></v-progress-linear>

    <ListingsContainer 
      :style="[(mapVisible && $vuetify.breakpoint.mobile)
      ? {'display': 'none'} : {'display': 'flex'}]"
      :dateRange=selectedDateRange 
      :officeSelectedEvent="officeSelectedEvent" 
      :listings="listings"
    />
    
    <MapContainer 
      :style="[showMapContainer
      ? {'display': 'flex'} : {'display': 'none'}]"
      :dateRange="selectedDateRange" 
      :selectedOffice="selectedOffice" 
      :listings="listings"
    />

    <button 
      id="control-fab"
      v-if="$vuetify.breakpoint.mobile"
      v-on:click="controlFabClicked()"
    >
      <v-icon color="#ffffff" style="margin-right: 0.5rem;">{{mapVisible ? "mdi-view-list" : "mdi-map"}}</v-icon>
      <span>{{mapVisible ? "List" : "Map"}}</span>
    </button>
  </div>
</template>

<script>
import ListingsContainer from './sub-components/map-subpage/ListingsContainer.vue'
import MapContainer from './sub-components/map-subpage/MapContainer.vue'
import { WEBSITE_URL, EVENTS, COLORS } from '../utils/constants.js'

const testComp = {
  name: "Cozy Apartment - (July 21st - August 30th)",
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
  listingStartDate: 1595373644000, // July 21st
  listingEndDate: 1598829644000,// August 30th
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

    this.$root.$on(EVENTS.listingSelected, () => {
      this.listingSelectedEvent();
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

  computed: {
    showMapContainer: function (){
      return !this.$vuetify.breakpoint.mobile || this.mapVisible;
    }
  },

  data: () => ({
    listings: [],
    offices: [],
    selectedOffice: null,
    selectedDateRange: null,
    
    isLoading: false,
    loadingBarColor: "#4285f4",

    mapVisible: false
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
    
    listingSelectedEvent: function() {
      if (this.$vuetify.breakpoint.mobile) {
        this.mapVisible = true;
      }
    },

    controlFabClicked: function () {
      if (this.mapVisible) {
        this.$root.$emit(EVENTS.listingDeselected);
      }
      this.mapVisible = !this.mapVisible;
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
    position: relative;
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
    padding-bottom: 0;
    padding-right: 0;
  }

  #control-fab {
    display: flex;
    align-items: center;
    justify-content: space-between;

    position: absolute;
    bottom: 70px;
    width: fit-content;
    padding: 10px 20px;

    color: #ffffff;
    background-color: var(--branding-blue);
    border-radius: 30px;
    box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 3px 1px -2px rgba(0,0,0,0.12), 0 1px 5px 0 rgba(0,0,0,0.20);

  }

  @media screen and (max-width: 1025px) {
    #map-subpage {
      flex-direction: column;
      align-items: center;
    }
  }
</style>
