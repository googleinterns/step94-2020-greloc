<template>
  <div 
    id="relo-map"
    :style="[(selectedListing != null) 
    ? {'height': '25%'} : {'height': '100%'}]"
  >
  </div>
</template>

<script>

import gmapsInit from '../../../utils/gmaps.js'
import { EVENTS } from '../../../utils/constants.js'

export default {
  name: 'ReloMap',
  async created () {
    await this.initializeGoogleMaps();
    this.map.setCenter({
      lat: 0,
      lng: 0
    });

    this.$root.$on(EVENTS.officeChanged, data => {
      this.selectedOffice = data;
      this.showSelectedOffice(data);
    });

    this.$root.$on(EVENTS.newListings, data => {
      this.showListingsOnMap(data);  
    });

    this.$root.$on(EVENTS.listingSelected, data => {
      this.onListingSelected(data);
    });

    this.$root.$on(EVENTS.busStopsSelected, data => {
      console.log(data);
      this.onBusStopsSelected(data);
    });
  },

  components: {
  },

  props: {
    listings: Array,
  },

  data: () => ({
    listingMarkers: [],
    stopMarkers: [],
    google: null,
    map: null,
    selectedOffice: null,
    selectedListing: null,
    directionsService: null,
    directionsRenderer: null

  }),

  methods: {
    
    /**
     * Creates all of the necessary objects from the Google Maps Static API to create 
     * and manipulate an embedded map and stores them in this component's state for later use
     */    
    initializeGoogleMaps: async function () {      
      try {            
        // geocoder = new google.maps.Geocoder();
        this.google = await gmapsInit();
        this.map = new this.google.maps.Map(document.getElementById('relo-map'), {
          zoom: 8
        });

        this.directionsService = new this.google.maps.DirectionsService();
        this.directionsRenderer = new this.google.maps.DirectionsRenderer({suppressMarkers: true});
        this.directionsRenderer.setMap(this.map);
      }
      
      catch (error) {
        console.error(error);
      }
    },

    /**
     * Displays a maker (pin) with a special office icon within the embedded Google Map
     * to represent a Google office
     */ 
    showSelectedOffice: function(office) {
      
      // Clear markers of previously selected office, if there are any
      this.clearListingMarkers();

      let location = { 
          lat: office.coordinates.latitude, 
          lng: office.coordinates.longitude 
      };

      let marker = new this.google.maps.Marker({
        position: location,
        title: office.name,
        icon: {
          url: "assets/office_marker_30px.png",
          size: new this.google.maps.Size(70, 70),
          origin: new this.google.maps.Point(0, 0),
          anchor: new this.google.maps.Point(17, 34),
        }
      });
      
      marker.setMap(this.map);
      this.map.setCenter(location);
      this.map.setZoom(14);
    },

    /**
     * Displays markers for every listing in listings
     * @param {Array} listings: List of listing objects which must contain `propertyMap.latitude` and `propertyMap.longitude` attributes
     */ 
    showListingsOnMap: function(listings) {      
      for (let listing of listings){
        this.addListingMarker(listing);
      }
    },

    showBusStopsOnMap: function(stopList) {
      for (let stop of stopList){
        this.addBusStopMarker(stop);
      }
    },    

    /**
     * When passed a listing object, this method will decrease the height to 25% and display a route from the `listing` to the 
     * selected office. When passed null, it will return this component to its normal state. 
     * @param {Object or null} listing: The listing that was selected or null
     */
    onListingSelected: function(listing) {
      this.selectedListing = listing;
      if (listing != null) {
        this.showRouteOnMap(listing, this.selectedOffice);
      } else {
        this.removeRoutesFromMap();
      }      
    },

    onBusStopsSelected: function(stopList) {
      this.clearBusStopMarkers();
      if (stopList.length > 0) {
        this.showBusStopsOnMap(stopList);
      }
    },    

    /**
     * Shows a route between listing and office, set's the map's center
     * to the midpoint between the two locations, and sets an appropriate zoom level
     * @param {Object} listing: The listing for which a route will be made to the selected office
     */
    showRouteOnMap: function(listing, office) {
      let listingCoordinates = {
        lat: listing.propertyMap.latitude,
        lng: listing.propertyMap.longitude,
      };

      let officeCoordinates = {
        lat: office.coordinates.latitude,
        lng: office.coordinates.longitude
      };

      let midpoint = this.calculateMidpoint(listingCoordinates, officeCoordinates);
      this.map.setCenter(midpoint);

      this.calcRouteAndDisplay(listingCoordinates, officeCoordinates);
    },
    
    /**
     * Returns an object with the midpoint between two geographical coordinates. 
     * For SHORT distances, we can treat earth as a flat and simply find the 
     * averages of the latitudes and longitudes to find the midpoint.
     * @param {Object} start: with attributes `lat` and `lng` that represent the location of the start point
     * @param {Object} end:   with attributes `lat` and `lng` that represent the location of the end point
     */
    calculateMidpoint: function(start, end) {
      return {
        lat: (start.lat + end.lat) / 2,
        lng: (start.lng + end.lng) / 2
      }
    },

    /**
     * Calculates the route between `start` and `end` using Google Maps API and creates a path on the embedded map that 
     * represents the route
     * @param {Object} start: with attributes `lat` and `lng` that represent the location of the start point
     * @param {Object} end:   with attributes `lat` and `lng` that represent the location of the end point
     */
    calcRouteAndDisplay: function(start, end) {
      let request = {
        origin: start,
        destination: end,
        travelMode: 'DRIVING'
      };
      
      this.directionsService.route(request, function(result, status) {
        if (status == 'OK') {
          console.log(this.selectedListing);
          this.directionsRenderer.setDirections(result);
        }
      }.bind(this));
    },

    removeRoutesFromMap: function() {
      this.directionsRenderer.setDirections({routes: []});
    },

    /**
     * Displays a marker (pin) with a special icon within the embedded Google Map
     * on the location of the listing that is passed in
     * @param {Object} listing: the listing for which a marker will be displayed
     */
    addListingMarker: function (listing) {
      let location = {
        lat: Number(listing.propertyMap.latitude),
        lng: Number(listing.propertyMap.longitude)
      };

      let marker = new this.google.maps.Marker({
        position: location,
        title: listing.propertyMap.name,
        icon: {
          url: "assets/googler_listing_marker_25px.png",
          size: new this.google.maps.Size(40, 40),
          origin: new this.google.maps.Point(0, 0),
          anchor: new this.google.maps.Point(17, 34),
        }
      });
      
      this.listingMarkers.push(marker);
      marker.setMap(this.map);
    },

    /**
     * Removes all listing markers from the embedded map. Does NOT remove office markers
     */    
    clearListingMarkers: function() {
      for (let marker of this.listingMarkers) {
        marker.setMap(null);
      }
      this.listingMarkers = [];
    },

    clearBusStopMarkers: function (){
      for (let marker of this.stopMarkers) {
        marker.setMap(null);
      }
      this.stopMarkers = [];
    },

    addBusStopMarker: function(stop)  {      
      let location = {
        lat: Number(stop.propertyMap.latitude),
        lng: Number(stop.propertyMap.longitude)
      };

      let marker = new this.google.maps.Marker({
        position: location,
        title: stop.propertyMap.name,
        icon: {
          url: "assets/bus_pin.png",
          size: new this.google.maps.Size(40, 40),
          origin: new this.google.maps.Point(0, 0),
          anchor: new this.google.maps.Point(17, 34),
        }
      });

      this.stopMarkers.push(marker);
      marker.setMap(this.map);
    }
  }
}
</script>

<style scoped>
  #relo-map {
    position: relative;
    width: 100%;
  }
</style>
