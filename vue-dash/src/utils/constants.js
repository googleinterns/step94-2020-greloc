 
// const WEBSITE_URL = "https://greloc.uc.r.appspot.com"
const WEBSITE_URL = "https://8080-b83fc153-d2cf-481d-a321-9342cdf80f21.us-east1.cloudshell.dev";

const EVENTS = {
  officeChanged: "officeChanged",
  newListings: "newListings",
  listingSelected: "listingSelected",
  listingDeselected: "listingDeselected",
  busStopsSelected: "busStopsSelected",
  poiSelected: "poiSelected",
  mapSubpageLoading: "mapSubpageLoading",
  dateRangeSelected: "dateRangeSelected",
  
  userPlaceSearch: "userPlaceSearch",
  clearPlaceSearchMakers: "clearPlaceSearchMakers",
}

const COLORS = {
  BRANDING_BLUE: "#4285f4",
  BRANDING_RED: "#ea4335",
  BRANDING_YELLOW: "#fbbc05",
  BRANDING_GREEN: "#34a853",
}

const OFFICES = [
  { 
    name: "Sunnyvale", 
    officeId: "svl", 
    coordinates: {
      latitude:  37.4030,
      longitude: -122.0326
    }
  },
  { 
    name: "Mountain View", 
    officeId: "mtv",
    coordinates: {
      latitude:  37.4220,
      longitude: -122.0841
    }
  },
]

const MarkerTypeEnum = Object.freeze({
  LISTING: 0,
  BUS_STOP: 1,
  RECREATION: 2,
  DINING: 3,
  GROCERY: 4,
  SEARCH: 100,
});


export { EVENTS, WEBSITE_URL, OFFICES, COLORS, MarkerTypeEnum};