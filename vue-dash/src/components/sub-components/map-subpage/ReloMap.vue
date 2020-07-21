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
import { EVENTS, MarkerTypeEnum } from '../../../utils/constants.js'

const defaultOptionsForMarkers = {
  DEFAULT: {
    size: 20,
    url: '',    
  },

  [MarkerTypeEnum.LISTING]: {
    size: 40, 
    url: "assets/googler_listing_marker_25px.png"
  },

  [MarkerTypeEnum.BUS_STOP]: {
    size: 40, 
    url: "assets/bus_pin.png"
  },

  [MarkerTypeEnum.RECREATION]: {
    size: 40, 
    url: "assets/recreation_pin.png"
  },

  [MarkerTypeEnum.DINING]: {
    size: 40, 
    url: "assets/dining_pin.png" 
  },
  
  [MarkerTypeEnum.GROCERY]: {
    size: 40, 
    url: "assets/grocery_pin.png"
  },
  
  [MarkerTypeEnum.SEARCH]: {
    size: 40
  }
}


export default {
  name: 'ReloMap',
  async created () {
    await this.initializeGoogleMaps();
    this.initializeEventBusListeners();
  },

  components: {
  },

  props: {
    listings: Array,
  },

  data: () => ({
    listingMarkers: [],
    stopMarkers: [],
    recreationMarkers: [],
    diningMarkers: [],
    groceryMarkers: [],
    userSearchMarkers: [],

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
          zoom: 8,
          mapId: 'f43e88d5033dbf3f'
        });

        this.directionsService = new this.google.maps.DirectionsService();
        this.directionsRenderer = new this.google.maps.DirectionsRenderer({suppressMarkers: true});
        this.directionsRenderer.setMap(this.map);
        
        this.map.setCenter({
          lat: 0,
          lng: 0
        });        
      }
      
      catch (error) {
        console.error(error);
      }
    },

    initializeEventBusListeners: function() {
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
        this.onBusStopsSelected(data);
      });

      this.$root.$on(EVENTS.poiSelected, data => {
        this.onPoiSelected(data.markerType, data.poiList);
      });

      this.$root.$on(EVENTS.userPlaceSearch, data => {
        this.onUserPlaceSearch(data);
      });

      this.$root.$on(EVENTS.clearPlaceSearchMakers, () => {
        this.onClearSearchMarkers();
      });
    },

    /**
     * Displays a maker (pin) with a special office icon within the embedded Google Map
     * to represent a Google office
     */ 
    showSelectedOffice: function(office) {
      
      // Clear markers of previously selected office, if there are any
      this.clearSelectMarkers(MarkerTypeEnum.LISTING);

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
        // this.addListingMarker(listing);
        this.addMarkerForEntity(listing, MarkerTypeEnum.LISTING, defaultOptionsForMarkers[MarkerTypeEnum.LISTING]);
      }
    },

    showBusStopsOnMap: function(stopList) {
      for (let stop of stopList){
        this.addMarkerForEntity(stop, MarkerTypeEnum.BUS_STOP, defaultOptionsForMarkers[MarkerTypeEnum.BUS_STOP]);
      }
    },

    showPoiOnMap: function(markerType, poiList) {
      for (let poi of poiList){
        this.addMarkerForPoi(poi, markerType, defaultOptionsForMarkers[markerType]);
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
      this.clearSelectMarkers(MarkerTypeEnum.BUS_STOP);
      
      if (stopList.length > 0) {
        this.showBusStopsOnMap(stopList);
      }
    },

    onPoiSelected: function(markerType, poiList) {
      this.clearSelectMarkers(markerType);
      
      if (poiList.length > 0) {
        this.showPoiOnMap(markerType, poiList);
      }
    },

    onUserPlaceSearch: function(place) {
      
      this.map.setCenter(place.geometry.location);

      let address = '';
      if (place.address_components) {
        address = [
          (place.address_components[0] && place.address_components[0].short_name || ''),
          (place.address_components[1] && place.address_components[1].short_name || ''),
          (place.address_components[2] && place.address_components[2].short_name || '')
        ].join(' ');
      }

      let marker = new this.google.maps.Marker({
        position: place.geometry.location,
        title: address,
      });

      marker.setPosition(place.geometry.location);
      this.userSearchMarkers.push(marker);
      marker.setMap(this.map);      
    },

    onClearSearchMarkers: function () {
      this.clearSelectMarkers(MarkerTypeEnum.SEARCH);
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
     * Adds a marker on the map for the specified entity given a markerType and options
     * 
     * @param markerType: The type of marker that should be cleared from the map
     */        
    addMarkerForEntity: function (entity, markerType, options = defaultOptionsForMarkers.DEFAULT) {
      let location = {
        lat: Number(entity.propertyMap.latitude),
        lng: Number(entity.propertyMap.longitude)
      };

      let marker = new this.google.maps.Marker({
        position: location,
        title: entity.propertyMap.name,
        icon: {
          url: options.url,
          size: new this.google.maps.Size(options.size, options.size),
          origin: new this.google.maps.Point(0, 0),
          anchor: new this.google.maps.Point(17, 34),
        }
      });

      let markerList = this.getMarkerList(markerType);      
      markerList.push(marker);
      marker.setMap(this.map);
    },

    /**
     * Adds a marker on the map for the specified Point of Interest (Poi) given a markerType and options
     * 
     * @param markerType: The type of marker that should be cleared from the map
     */        
    addMarkerForPoi: function (poi, markerType, options = defaultOptionsForMarkers.DEFAULT) {

      let marker = new this.google.maps.Marker({
        position: poi.geometry.location,
        title: poi.name,
        icon: {
          url: options.url,
          size: new this.google.maps.Size(options.size, options.size),
          origin: new this.google.maps.Point(0, 0),
          anchor: new this.google.maps.Point(17, 34),
        }
      });

      let markerList = this.getMarkerList(markerType);      
      markerList.push(marker);
      marker.setMap(this.map);
    },
    
    /**
     * Removes ALL markers of `markerType` from the map
     * 
     * @param markerType: The type of marker that should be cleared from the map
     */
    clearSelectMarkers: function(markerType) {
      let markerList = this.getMarkerList(markerType);      
      for (let marker of markerList) {
        marker.setMap(null);
      }
      markerList = [];
    },

    /**
     * Returns the stored list of markers for the given `markerType`
     * 
     * @param markerType: The type of marker that should be cleared from the map
     */
    getMarkerList: function(markerType) {
      switch(markerType) {
        case MarkerTypeEnum.LISTING:
          return this.listingMarkers;

        case MarkerTypeEnum.BUS_STOP:
          return this.stopMarkers;

        case MarkerTypeEnum.RECREATION:
          return this.recreationMarkers;
        
        case MarkerTypeEnum.DINING:
          return this.diningMarkers;

        case MarkerTypeEnum.GROCERY:
          return this.groceryMarkers;

        case MarkerTypeEnum.SEARCH:
          return this.userSearchMarkers;

        default:
          throw "MarkerTypeEnum does not exist"
      }      
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
