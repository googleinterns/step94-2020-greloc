<template>
  <div class="listing-form"> 
    <v-btn class="add-button"
      absolute
      dark
      fab
      left
      @click="showForm"
      color="#3cba54">
      <v-icon >mdi-plus</v-icon>
    </v-btn>
    
    <div class="listing-title"> 
      <v-text-field class="listing-title-entry"
      label = "Listing Title"
      v-model="listingTitle"
      filled>
      </v-text-field>
    </div>
    <div class="listing-address">
      <v-text-field class="listing-address-entry"
        solo
        label="Street Address"
        v-model="streetAddress"
        prepend-inner-icon="mdi-map-marker"
        filled>
      </v-text-field>
        <div class="location">
          <v-text-field class="listing-city-entry"
            solo
            label="City"
            v-model="listingCity"
            filled>
          </v-text-field>
          <div class="divider"/>
          <v-select class="listing-state-entry"
            :items ="states"
            v-model="listingState"
            label="State/Province">
          </v-select>
        </div>
    </div>
    <div class="dates">
    <DateRangeSelector/>
    </div>
    <div class="description-entry">
      <ImageInput/>
      <v-textarea
        solo
        name="input-7-4"
        v-model="listingDescription"
        label="Property Description">
        
      </v-textarea>
        <div class="property-type">
          <v-select class="property-types"
            :items="properties"
            v-model="type"
            item-value="value"
            label="Type of Property">
          </v-select>
          <div class="divider"/>
          <v-text-field class="listing-price"
            solo
            label='Price Per Month (Ex: "$2000")'
            v-model="price"
            filled>
          </v-text-field>
        </div>
        <div class="selectors">
          <v-select class="total-beds"
            :items="items"
            v-model="beds"
            label="Total Number of Beds">
          </v-select>
          <div class="divider"/>
          <v-select class="total-bedrooms"
              :items="bedrooms"
              v-model="bedrooms"
              label="Bedrooms">
          </v-select>
          <div class="divider"/>
          <v-select class="total-baths"
            :items="baths"
            v-model="bathrooms"
            label="Baths">
          </v-select>
        </div>
    </div>
    <div class="amenities">
      <v-layout>
        <v-checkbox v-model="wifiCheckbox" :label="`WiFi `"></v-checkbox>
        <v-checkbox v-model="acCheckbox" :label="`Air Conditioning`"></v-checkbox>
        <v-checkbox v-model="washerCheckbox" :label="`Washer/Dryer `"></v-checkbox>
        <v-checkbox v-model="gymCheckbox" :label="`Gym `"></v-checkbox>
        <v-checkbox v-model="poolCheckbox" :label="`Pool `"></v-checkbox>
        <v-checkbox v-model="parkingCheckbox" :label="`Parking `"></v-checkbox>
        <v-checkbox v-model="greenspaceCheckbox" :label="`Greenspace `"></v-checkbox>
      </v-layout>
    </div>
    <div class="contact-entry">
      <v-text-field class="name-entry"
        solo
        label="Owner Full Name"
        v-model="ownerName"
        filled>
      </v-text-field>
      <v-text-field class="email-entry"
        solo
        label="Contact Email"
        v-model="ownerEmail"
        filled>
      </v-text-field>
      <v-text-field class="phone-number-entry"
        solo
        label="Contact Number"
        v-model="ownerNumber"
        filled>
      </v-text-field>
    </div>
    <v-btn 
    class="submit-button" 
    rounded color="primary"
    @click="addListing"
    dark> Submit Listing</v-btn>
  </div>
</template>

<script>
import ImageInput from './InsertImage.vue'
import DateRangeSelector from '../map-subpage/DateRangeSelector.vue'

export default {
  name: 'ListingForm',
  components: {
    ImageInput,
    DateRangeSelector,
  },
  props: {
    msg: String,
    dateRange: Array
  },
  data: () => ({
    
    listingTitle: "",
    streetAddress: "",
    listingCity: "",
    listingState: "",
    //listingImages = [], will use temp images for now
    listingDescription: "",
    ownerName: "",
    ownerEmail: "",
    ownerNumber: "",
    price: "",
    propertyType: "",
    date:"",
    menu: "",
    type: "",
    rules: "",
    isEmptyState: true,
    wifiCheckbox: false,
    acCheckbox: false,
    washerCheckbox: false,
    gymCheckbox: false,
    poolCheckbox: false,
    parkingCheckbox: false,
    greenspaceCheckbox: false,

    items: ['1', '2', '3', '4', '5'],
    bedrooms: ['1', '2', '3', '4', '5'],
    baths: ['1', '1.5', '2', '2.5', '3', '3.5'],
    properties: ["Entire House", "Full Apartment", "Private Room",],
    states: [
      'Alabama', 'Alaska', 'American Samoa', 'Arizona',
      'Arkansas', 'California', 'Colorado', 'Connecticut',
      'Delaware', 'District of Columbia', 'Federated States of Micronesia',
      'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho',
      'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
      'Louisiana', 'Maine', 'Marshall Islands', 'Maryland',
      'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
      'Missouri', 'Montana', 'Nebraska', 'Nevada',
      'New Hampshire', 'New Jersey', 'New Mexico', 'New York',
      'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio',
      'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico',
      'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee',
      'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia',
      'Washington', 'West Virginia', 'Wisconsin', 'Wyoming',
    ],
  }),

  computed: {
    displayForm: function() {
      return false;
    }
  },

  methods: {

    addListing: async function() {
      
      // Set variables equal to inputs from forms
      const listingTitle = this.listingTitle;
      const streetAddress = this.streetAddress;
      const listingCity = this.listingTitle;
      const listingDescription = this.listingDescription;
      const ownerName = this.ownerName;
      const ownerEmail = this.ownerEmail;
      const ownerNumber = this.ownerNumber;
      const listingPrice = this.price;
      const listingState = this.listingState;
      const propertyType = this.type;
      const totalBeds = this.beds; 
      const totalBedrooms = this.bedrooms; 
      const totalBaths = this.bathrooms; 
      const hasWifi = this.wifiCheckbox;
      const hasAC = this.acCheckbox;
      const hasWasherDryer = this.washerCheckbox;
      const hasGym = this.gymCheckbox;
      const hasPool = this.poolCheckbox;
      const hasParking = this.parkingCheckbox;
      const hasGreenspace = this.greenspaceCheckbox;

      // Create Listing Object
      const currentListing = {
        name: listingTitle,
        price: listingPrice,
        type: propertyType,
        desc: listingDescription,

        images: [
          "https://cdngeneral.rentcafe.com/dmslivecafe/3/1104500/METRO%20GATEWAY%20IMG%2003(2).jpg?crop=(0,0,300,191.25000000000028)&cropxunits=300&cropyunits=200&quality=85&scale=both",
          "https://cdngeneral.rentcafe.com/dmslivecafe/3/984399/Hearth-Model-Unit-IMG-0370_webopt_2MB.jpg?crop=(0,8,300,199.25000000000028)&cropxunits=300&cropyunits=200&quality=85&scale=both&"
        ],
        contactInfo: {
          name: ownerName,
          phone: ownerNumber,
          email: ownerEmail
        },
        amenities: {
          hasWifi: hasWifi,
          hasAC: hasAC,
          hasWasherDryer: hasWasherDryer,
          hasGym: hasGym,
          hasPool: hasPool,
          hasParking: hasParking,
          hasGreenspace: hasGreenspace
        },
        streetAddress: streetAddress,
        listingCity: listingCity,
        listingState: listingState,
        totalBeds: totalBeds,
        totalBedrooms: totalBedrooms,
        totalBaths: totalBaths,
        startTimeStamp: this.dateRange[0],
        endTimeStamp: this.dateRange[1]
      }
      
      console.log(this.dateRange);

      let response = await fetch('/locations', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(currentListing)
      })
      
      console.log(totalBeds);
      console.log(totalBedrooms);
      console.log(totalBaths);

      if (response.ok){
        // Success Alert
      } else {
        // Unsuccessful Alert
      }

    },

    showForm: function() {
      this.isEmptyState = false;
    }

  }
}
</script>

<style scoped>
.listing-title-entry {
  display: flex;
  width: 500px;
}

.dates {
  display: flex;
  justify-content: space-evenly;
}

.location {
  display: flex;
  justify-content: space-evenly;
}

.selectors {
  display: flex;
  justify-content: space-evenly;
  padding: 10px;
}

.submit-button {
  display: flex;
  justify-content: flex-end;
}

.divider {
  width:20px;
  height:auto;
  display:inline-block;
}

.property-type {
  display: flex;
  justify-content: space-evenly;
  padding: 10px;
}

.add-button {
  margin-top: 50px;
}
</style>
