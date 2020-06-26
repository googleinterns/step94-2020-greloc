<template>
    <div class="subpage" id="map-subpage">
      <ListingsContainer :getListings="getListings" :listings="listings"/>

      <v-btn
          v-on:click="createListing()"
      >
        create
      </v-btn>

      <v-btn
          v-on:click="deleteListings()"
      >
        delete
      </v-btn>
    </div>
</template>

<script>
import ListingsContainer from './sub-components/map-subpage/ListingsContainer.vue'
import { WEBSITE_URL } from '../utils/constants.js'

const testComp = {
  name: "Cozy Apartment",
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
  startTimestamp: 123089213,
  endTimestamp: 123099213,
  latitude: 37.395720,
  longitude: -122.028570,
};

export default {
  name: 'Template',

  components: {
    ListingsContainer
  },
  props: {
    userData: Object
  },

  data: () => ({
    listings: []
  }),

  methods: {
    getListings: async function(office) {

      let response = await fetch(WEBSITE_URL + `/locations?office=${office.officeId}`);
      let respData;
      
      if (response.ok) {                
        respData = await response.json();
        console.log("getListings: success");
        console.log(respData);
      } else {
        console.log("getListings: error");
        respData = [];
      }

      this.listings = respData;
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

    // PROTOTYPE PURPOSES ONLY
    deleteListings: async function() {
      let response = await fetch(WEBSITE_URL + '/data');

      if (response.ok) {
        console.log("deleteListings: Success");
      } else {
        console.log("deleteListings: Failure");
      }      
    }
  },

}
</script>

<style scoped>
  #map-subpage {
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
  }
</style>
