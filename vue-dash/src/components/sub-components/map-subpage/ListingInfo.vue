<template>
  <div id="info-container">
    <v-btn
      id="exit-fab"
      color="var(--branding-blue)"
      @click="onCloseClick()"
      dark
      fab
      v-if="!$vuetify.breakpoint.mobile"
    >
      <v-icon>mdi-chevron-down</v-icon>
    </v-btn>
    <div id="header-container">
      <div class="header-row">
        <h1>{{selectedListing.propertyMap.name}}</h1>
        <span id="header-type">{{selectedListing.propertyMap.type}}</span>
      </div>

      <div class="header-row">
        <span>{{`${selectedListing.propertyMap.guests} Guests | ${selectedListing.propertyMap.bedrooms} Bedroom | ${selectedListing.propertyMap.beds} Beds | ${selectedListing.propertyMap.baths} Bath`}}</span>
        <span>{{`${distanceToOffice} mi (from office)`}}</span>
      </div>      
    </div>
  
    <div id="content-container">
      <div id="image-container" v-if="!$vuetify.breakpoint.mobile">
        <img 
          class="carousel-image" 
          v-for="(image, index) in selectedListing.propertyMap.images"
          :key="index"
          :src="image"
          :alt="index"
        >
      </div>
      <v-carousel 
        v-else
        id="mobile-image-carousel"        
        hide-delimiter-background
        :show-arrows="false"
        height="250px"
      >
        <v-carousel-item
          v-for="(image, index) in selectedListing.propertyMap.images"
          :key="index"
          :src="image"
        ></v-carousel-item>
      </v-carousel>      
      <p id="description-cont">
        {{selectedListing.propertyMap.desc}}
      </p>
    </div>

    <div id="sidebar-container">
      <div id="contact-card">
        <h3 class="sidebar-titles">Owner</h3>
      </div>
      <div id="amenities-card" v-if="visibleAmenities.length > 0">
        <h3 class="sidebar-titles">Amenities</h3>
        <ul id="amenity-list">
          <li 
          v-for="(amenity, index) in visibleAmenities"
          :key="index"
          >
            <v-icon class="amenity-icon">{{amenityMap[amenity]["icon"]}}</v-icon> 
            <span class="amenity-title">{{amenityMap[amenity]["name"]}}</span>
          </li>
        </ul>
      </div>      
    </div>

    <div id="footing-container" v-if="selectedListing.propertyMap.googlerOwned">
      <v-avatar>
        <img src="assets/google.svg" alt="Googler owned">
      </v-avatar>
      <span id="source-descriptor">Googler Owned</span>
    </div>
  </div>
</template>

<script>
import { EVENTS } from '../../../utils/constants.js'

const amenityMap = {
  "wifi": {
    "icon": "mdi-wifi",
    "name": "Wi-Fi"
  },

  "washer": {
    "icon": "mdi-washing-machine",    
    "name": "Washer/Dryer"
  },

  "gym": {
    "icon": "mdi-dumbbell",
    "name": "Gym"
  },

  "pool": { 
    "icon":"mdi-pool",
    "name": "Pool"
  },

  "greenspace": {
    "icon": "mdi-nature",
    "name": "Greenspace"
  },

  "parking": {
    "icon": "mdi-parking",
    "name": "Parking"
  },
  
  "ac": {
    "icon": "mdi-snowflake",
    "name": "Air Conditioning"
  },

}

export default {
  name: 'ListingInfo',

  components: {
  },
  props: {
    selectedListing: Object
  },

  computed: {
    visibleAmenities: function() {
      let selectedListingObject = this.selectedListing.propertyMap;
      if (!selectedListingObject.amenities) return [];
      
      let amenities = [];
      for (let [key, value] of Object.entries(selectedListingObject.amenities.value.propertyMap)) {
        if (value) {
          amenities.push(key);
        }
      }
      return amenities;
    }
  },

  data: () => ({
    distanceToOffice: 3.8,
    amenityMap: amenityMap
  }),
  methods: {
    onCloseClick: function () {
      this.$root.$emit(EVENTS.listingDeselected);
    }
  }
}
</script>

<style scoped>
  #info-container {
    position: relative;
    height: 75%;
    width: 100%;
    padding: 16px 32px;

    display: grid;
    grid-template-areas: 
    "header header"
    "content sidebar"
    "footer footer";
    grid-template-columns: min-content 1fr;
    grid-template-rows: min-content auto min-content;
    
    background-color: #ffffff;
    box-shadow: 0px -1px 4px rgba(0, 0, 0, 0.25);
  }

  #exit-fab {
    position: absolute;
    top: -28px;
    left: 50%;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);
  }

  #header-container {
    width: 100%;
    grid-area: header;

    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
  }

  #header-container h1{
    font-size: 40px;
    color: var(--text-heading);
  }

  .header-row {
    width: 100%;

    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .header-row span {
    font-size: 22px;
    color: var(--text-subtle);
  }

  #header-type {
    font-size: 35px;
  }
  
  #content-container {
    grid-area: content;
  }

  #image-container {
    height: auto;
    width: 100%;
    margin: 2rem 0;
    display: grid;
    row-gap: 1rem;
    column-gap: 1rem;
    grid-template-columns: repeat(4, min-content);
  }

  #mobile-image-carousel {
    margin: 1rem 0;
    border-radius: 10px;
    width: calc(100vw - 32px);
  }  

  .carousel-image {
    height: 250px;
    width: 250px;
    margin-right: 1rem;
    border-radius: 5px;
    object-fit: cover;
  }

  #description-cont {
    width: 100%;
    font-size: 24px;
    color: gray;
  }

  #sidebar-container {
    grid-area: sidebar;
    height: 100%;
    width: 200px;
    align-self: end;
    justify-self: end;
  }

  .sidebar-titles {
    color: var(--text-subtle);
  }

  #contact-card {
    width: 100%;
    height: 250px;

    border: 1px solid #C4C4C4;
    box-sizing: border-box;
    border-radius: 5px;
    margin-bottom: 1rem;
    padding: 16px;
  }

  #amenities-card {
    width: 100%;
    height: auto;

    border: 1px solid #C4C4C4;
    box-sizing: border-box;
    border-radius: 5px;    

    padding: 16px 16px 0px 16px;
  }

  #amenities-card li {
    margin: 1.5rem 0;
  }

  #amenity-list {
    list-style: none;
  }

  .amenity-title {
    margin-left: 1rem;
    text-transform:capitalize;
  }

  #footing-container {
    grid-area: footer;
    width: 100%;
    display: flex;
    align-items: flex-end;
    justify-content: flex-start;
  }

  #source-descriptor {
    margin-left: 1rem;
    font-size: 18px;
    color: var(--text-main);
  }

  @media screen and (max-width: 1025px) {
    #info-container {
      grid-template-areas: 
        "header"
        "content"
        "sidebar"
        "footer ";
      overflow-y: scroll;
      overflow-x: hidden;
      grid-template-columns: none;
      padding: 16px;

    }

    #header-container {
      flex-direction: column;
    }

    #header-container h1 {
      font-size: 34px;
    }

    #header-type {
      font-size: 24px;
    }

    .header-row {
      flex-direction: column;
      align-items: flex-start;
    }

    .header-row span {
      font-size: 20px
    }    

    #sidebar-container {
      display: flex;
      flex-direction: column-reverse;
      justify-self: start;
      width: 100%;
    }

    #amenities-card {
      margin-bottom: 1rem;
      border: none;
      padding: 0;
    }

    #amenities-card li{
      margin: 0;
    }

    #amenity-list {
      display: grid;
      grid-template-columns: 1fr 1fr;
      grid-template-rows: min-content min-content min-content;
      gap: 0px;
      list-style: none;
      padding: 0;
      row-gap: 2rem;
      margin-top: 1rem;
    }

  }
</style>
