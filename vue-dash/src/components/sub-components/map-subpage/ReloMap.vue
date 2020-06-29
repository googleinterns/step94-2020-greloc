<template>
  <div id="relo-map">

  </div>
</template>

<script>
import gmapsInit from '../../../utils/gmaps.js'

export default {
  name: 'ReloMap',
  async created () {
    this.$root.$on('officeChanged', data => {
      console.log("RECIEVE  officeChanged!!");
      this.showSelectedOffice(data);
    });

    await this.initializeGoogleMaps();
    this.map.setCenter({
      lat: 0,
      lng: 0
    });

    this.$root.$on('newListings', data => {
      console.log("RECIEVE newListings!!");
      this.showListingsOnMap(data);  
    });
  },

  mounted () {
    
  },
  props: {
    listings: Array,
  },
  data: () => ({
    google: null,
    map: null,

  }),
  methods: {
    initializeGoogleMaps: async function () {
      
      try {            
        // geocoder = new google.maps.Geocoder();
        this.google = await gmapsInit();
        this.map = new this.google.maps.Map(document.getElementById('relo-map'), {
          zoom: 8
        });
      }
      
      catch (error) {
        console.error(error);
      }

      console.log("INIT GMAPS DONE");
    },

    showSelectedOffice: function(office) {
      let location = { 
          lat: office.coordinates.latitude, 
          lng: office.coordinates.longitude 
      };

      let marker = new this.google.maps.Marker({
        position: location,
        title: office.name,
        icon: "assets/office_marker_30px.png"
      });
      
      marker.setMap(this.map);
      this.map.setCenter(location);
      this.map.setZoom(14);

    },

    showListingsOnMap: function(listings) {      
      for (let listing of listings){
        this.addListingMarker(listing);
      }
    },

    addListingMarker: function (listing) {
      let location = {
        lat: Number(listing.propertyMap.latitude),
        lng: Number(listing.propertyMap.longitude)
      };

      let marker = new this.google.maps.Marker({
        position: location,
        title: listing.propertyMap.name,
        icon: "assets/googler_listing_marker_25px.png"
      });

      marker.setMap(this.map);
    }
  }
}
</script>

<style scoped>
  #relo-map {
    height: 100%;
    width: 100%;
  }
</style>
