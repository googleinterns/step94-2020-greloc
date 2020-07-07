<template>
  <div id="info-container">
    <v-btn
      id="exit-fab"
      color="var(--branding-blue)"
      @click="onCloseClick()"
      dark
      fab
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
      <div id="image-container">
        <img 
          class="carousel-image" 
          v-for="(image, index) in selectedListing.propertyMap.images"
          :key="index"
          :src="image"
          :alt="index"
        >
      </div>
      <p id="description-cont">
        {{selectedListing.propertyMap.desc}}
      </p>
    </div>

    <div id="sidebar-container">
      <div id="contact-card">
        
      </div>

    </div>

    <div id="footing-container">
      <v-avatar>
        <img src="assets/google.svg" alt="Googler owned">
      </v-avatar>
      <span id="source-descriptor">Googler Owned</span>
    </div>
  </div>
</template>

<script>
import { EVENTS } from '../../../utils/constants.js'

export default {
  name: 'ListingInfo',

  components: {
  },
  props: {
    selectedListing: Object
  },
  data: () => ({
    distanceToOffice: 3.8,
    colors: [
      'indigo',
      'warning',
      'pink darken-2',
      'red lighten-1',
      'deep-purple accent-4',
    ],
    slides: [
      'First',
      'Second',
      'Third',
      'Fourth',
      'Fifth',
    ]
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

  #contact-card {
    width: 100%;
    height: 250px;

    border: 1px solid #C4C4C4;
    box-sizing: border-box;
    border-radius: 5px;
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
    color: gray;
  }

</style>
