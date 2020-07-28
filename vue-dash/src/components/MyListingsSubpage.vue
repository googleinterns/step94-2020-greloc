<template>
  <div class="subpage" id="my-listings-subpage">  
  <!--<MyListingsContainer class="property-list" :listings="listings"/> -->
    <ListingForm class="form"/>

      <div id="form-empty-state" v-if=false>
        <v-icon class="form-empty-state-icon">mdi-map</v-icon>
        <span class="form-empty-state-text">Click a current listing to Edit or the '+' to create a new listing</span>
      </div>

  </div>
</template>

<script>
import ListingForm from './sub-components/myListings-subpage/EditListing.vue'
import {WEBSITE_URL} from '../utils/constants.js'
//import MyListingsContainer from './sub-components/myListings-subpage/MyListingsContainer.vue' 

export default {

  data: () => ({
    listings: [],
  }),

  name: 'MyListingsSubpage',
  mounted () {
    this.getUserListings();
  },

  props: {
    msg: String
  },

  methods: {
    getUserListings: async function () {
      let response = await fetch(WEBSITE_URL + '/userListings');

      let respData;
      if (response.ok) {                
        respData = await response.json();
      } else {
        respData = [];
      }
      this.listings = respData;
    }
  },

  components: {
    ListingForm,
    //MyListingsContainer,
  }
}
</script>

<style scoped>
.property-list {
  min-width: 550px;
  height: 100%;
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  border-right: 1px solid #dbdbdb;
  padding: 16px;
  flex-direction: column;
}

.form {
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
  flex-direction: column;
  padding: 30px;
}

#my-listings-subpage {
  display: flex; 
  align-items: flex-start;
  justify-content: flex-start;
}

#form-empty-state {
  width: 300px;
    
  display: flex;    
  flex-direction: column;
  align-items: center;
  justify-content: center;
    
  text-align: center;
}
</style>
