<template>
  <div class="listing-form"> 
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
      <div class="start-date">
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="date"
          transition="scale-transition"
          offset-y
          min-width="290px">
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date"
                label="Available Start Date"
                prepend-icon="event"
                readonly
                v-bind="attrs"
                v-on="on">
              </v-text-field>
            </template>
            <v-date-picker v-model="date" no-title scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
              <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
            </v-date-picker>
        </v-menu>
      </div>
      <div class="divider"/>
      <div class="end-date">
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="date"
          transition="scale-transition"
          offset-y
          min-width="290px">
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date"
                label="Available End Date"
                prepend-icon="event"
                readonly
                v-bind="attrs"
                v-on="on">
              </v-text-field>
            </template>
            <v-date-picker v-model="date" no-title scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
              <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
            </v-date-picker>
        </v-menu>
      </div>
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
            label="Total Number of Beds">
          </v-select>
          <div class="divider"/>
          <v-select class="total-bedrooms"
              :items="bedrooms"
              label="Bedrooms">
          </v-select>
          <div class="divider"/>
          <v-select class="total-baths"
            :items="baths"
            label="Baths">
          </v-select>
        </div>
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

export default {
  name: 'ListingForm',
  components: {
    ImageInput
  },
  props: {
    msg: String
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
  methods: {

    addListing: async function() {

      const listingTitle = this.listingTitle;
      const streetAddress = this.streetAddress;
      const listingCity = this.listingTitle;
      const listingDescription = this.listingDescription;
      const ownerName = this.ownerName;
      const ownerEmail = this.ownerEmail;
      const ownerNumber = this.ownerNumber;
      const listingPrice = this.price;
      const listingState = this.listingState;
      const propertyType = this.propertyType;

      
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
        streetAddress: streetAddress,
        listingCity: listingCity,
        listingState: listingState,
        //startTimeStamp: <START_TIME HERE>,
        //endTimeStamp: <END_TIME HERE>
      }
      
      let response = await fetch('/locations', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(currentListing)
      })

      if (response.ok){
        // Success Alert
      } else {
        // Unsuccessful Alert
      }

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
</style>
