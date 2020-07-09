 
// const WEBSITE_URL = "http://capparelli-step-2020.appspot.com"
const WEBSITE_URL = "https://8080-b83fc153-d2cf-481d-a321-9342cdf80f21.us-east1.cloudshell.dev";

const EVENTS = {
  officeChanged: "officeChanged",
  newListings: "newListings",
  listingSelected: "listingSelected",
  listingDeselected: "listingDeselected",
  busStopsSelected: "busStopsSelected",
  mapSubpageLoading: "mapSubpageLoading"
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


export { EVENTS, WEBSITE_URL, OFFICES };