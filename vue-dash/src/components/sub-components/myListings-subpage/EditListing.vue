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

    <v-alert id="form-alert"
    v-model="alert"
    transition="scroll-y-transition"
    :type="alertType"
    elevation="10"
    dismissible
    absolute> 
      {{alertMessage}}
    </v-alert>
    
    <div class="listing-title"> 
      <v-text-field class="listing-title-entry"
      label = "Listing Title"
      v-model="listingTitle"
      :rules="titleRules"
      filled>
      </v-text-field>
    </div>
    <div class="listing-address">
      <GmapsAutoComplete class="listing-address-entry" :forReloMap="false"/>
    </div>
    <div class="dates">
      <DateRangeSelector/>
    </div>
    <div class="description-entry">
      <!--<ImageInput/> -->
      <v-file-input
      v-model="files"
      accept="image/png, image/jpeg, image/bmp"
      placeholder="Upload Property Images"
      prepend-icon="mdi-camera"
      label="Images"
      multiple
      chips
    ></v-file-input>
      <v-textarea
        solo
        name="input-7-4"
        v-model="listingDescription"
        :rules="descriptionRules"
        :counter="200"
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
            :rules="priceRules"
            v-model="price"
            filled>
          </v-text-field>
        </div>
        <div class="selectors">
          <v-select class="total-beds"
            :items="totalBeds"
            v-model="beds"
            label="Total Number of Beds">
          </v-select>
          <div class="divider"/>
          <v-select class="total-bedrooms"
              :items="totalBedrooms"
              v-model="bedrooms"
              label="Bedrooms">
          </v-select>
          <div class="divider"/>
          <v-select class="total-baths"
            :items="totalBaths"
            v-model="baths"
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
        :rules="ownerNameRules"
        filled>
      </v-text-field>
      <v-text-field class="email-entry"
        solo
        label="Contact Email"
        v-model="ownerEmail"
        :rules="ownerEmailRules"
        filled>
      </v-text-field>
      <v-text-field class="phone-number-entry"
        solo
        label="Contact Number"
        v-model="ownerNumber"
        :rules="ownerNumberRules"
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
import DateRangeSelector from '../map-subpage/DateRangeSelector.vue'
import GmapsAutoComplete from '../map-subpage/GmapsAutoComplete'
import {EVENTS, WEBSITE_URL} from '../../../utils/constants.js'

export default {
  name: 'ListingForm',
  created(){
    this.$root.$on(EVENTS.dateRangeSelected, dateRange => {
      this.dateRange = dateRange;
    });
    this.$root.$on(EVENTS.userSelectedAddress, place => {
      console.log("From Listener!");
      console.log(place);
      this.isPlaceSelected = true;
      this.onUserSelectedAddress(place);
    });
  },
  components: {
    DateRangeSelector,
    GmapsAutoComplete,
  },
  props: {
    msg: String,
  },
  data: () => ({

    alert: false,
    alertMessage: "",
    alertType: "info",
    
    files: [],
    listingTitle: "",
    streetAddress: "",
    isPlaceSelected: false,
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
    userSelectedPlace: "",
    beds: "",
    bedrooms: "",
    baths: "",
    dateRange: [], 
    totalBeds: ['1', '2', '3', '4', '5'],
    totalBedrooms: ['1', '2', '3', '4', '5'],
    totalBaths: ['1', '1.5', '2', '2.5', '3', '3.5'],
    properties: ["Entire House", "Full Apartment", "Private Room",],
    titleRules: [
      v => !!v || 'A Listing Title is Required',
    ],
    descriptionRules: [
      v => !!v || 'A Description of the Listing is Required',
    ],
    priceRules: [
      v => !!v || 'Listing Price per Month is Required',
    ],
    ownerNameRules: [
      v => !!v || 'Listing Owner Name is Required',
    ],
    ownerEmailRules: [
      v => !!v || 'Listing Contact email is Required',
      v => /.+@.+/.test(v) || 'A valid email is Required',
    ],
    ownerNumberRules: [
      v => !!v || 'Owner Phone Number is Required',
    ],

  }),

  computed: {
    displayForm: function() {
      return false;
    }
  },

  methods: {

    addListing: async function() {

      if (!this.isInputValid()) {
        const alertMessage = "One or more entry fields is missing or invalid";
        this.sendAlert(alertMessage, "warning");
        return;
      }
      
      // Set variables equal to inputs from forms
      const listingTitle = this.listingTitle;
      const listingDescription = this.listingDescription;
      const ownerName = this.ownerName;
      const ownerEmail = this.ownerEmail;
      const ownerNumber = this.ownerNumber;
      const listingPrice = this.price;
      const propertyType = this.type;
      const totalBeds = this.beds; 
      const totalBedrooms = this.bedrooms; 
      const totalBaths = this.baths; 
      const hasWifi = this.wifiCheckbox;
      const hasAC = this.acCheckbox;
      const hasWasherDryer = this.washerCheckbox;
      const hasGym = this.gymCheckbox;
      const hasPool = this.poolCheckbox;
      const hasParking = this.parkingCheckbox;
      const hasGreenspace = this.greenspaceCheckbox;
      const files = this.files;
      const imageLinks = [];
      const longitude = this.userSelectedPlace.geometry.location.lng();
      const latitude = this.userSelectedPlace.geometry.location.lat();

      // Save image URL's to image links array
      for (var i = 0; i < files.length; i++) {
        this.generateImageURL(imageLinks, files[i]);
      }

      let address = '';
      if (this.userSelectedPlace.address_components) {
        address = [
          (this.userSelectedPlace.address_components[0] && this.userSelectedPlace.address_components[0].short_name || ''),
          (this.userSelectedPlace.address_components[1] && this.userSelectedPlace.address_components[1].short_name || ''),
          (this.userSelectedPlace.address_components[2] && this.userSelectedPlace.address_components[2].short_name || '')
        ].join(' ');
      }


      let startDateMillis = Date.parse(this.dateRange[0]);
      let endDateMillis= Date.parse(this.dateRange[1]);

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
          wifi: hasWifi,
          ac: hasAC,
          washer: hasWasherDryer,
          gym: hasGym,
          pool: hasPool,
          parking: hasParking,
          greenspace: hasGreenspace
        },
        streetAddress: address,
        beds: totalBeds,
        bedrooms: totalBedrooms,
        baths: totalBaths,
        listingStartDate: startDateMillis,
        listingEndDate: endDateMillis,
        longitude: longitude,
        latitude: latitude,
      }

      let response = await fetch(WEBSITE_URL + '/locations', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(currentListing)
      })

      if (response.ok){
        this.sendAlert("Listing was Successfully Added!", "success");
      } else {
        this.sendAlert("Uh-oh. Something went wrong. Try Again Later", "error");
      }

    },

    showForm: function() {
      this.isEmptyState = false;
    },

    onUserSelectedAddress: function(place) {
      this.userSelectedPlace = place;
    },

    generateImageURL: function(imageArr, img) {
      const reader = new FileReader();
      reader.readAsDataURL(img);

      reader.onload =  function(e){
        const link = e.target.result;
        imageArr.push(String(link));
      };

    },
    
    // Verify input fields of the form are filled
    isInputValid: function() {

      // Check entry fields for given input
      if (this.listingTitle.trim().length == 0) {
        return false;
      } else if (!this.isPlaceSelected) {
        return false;
      } else if (this.dateRange.length != 2) {
        return false;
      } else if (this.files.length == 0) {
        return false;
      } else if (this.listingDescription.trim().length == 0) {
        return false;
      } else if (this.price.trim().length == 0) {
        return false;
      } else if (this.ownerName.trim().length == 0) {
        return false;
      } else if (this.ownerNumber.trim().length == 0) {
        return false;
      } else if (!this.isEmailValid(this.ownerEmail)) {
        return false;
      }

      return true;
    },

    sendAlert: function(alertMessage, alertType) {
      this.alert = true;
      this.alertMessage = alertMessage;
      this.alertType = alertType;
    },

    isEmailValid: function(email) {
     return /.+@.+/.test(email);
    }

  }
}
</script>

<style scoped>
.listing-title-entry {
  display: flex;
  width: 500px;
}

.listing-address-entry {
  display: flex;
  width: 500px !important;
  position: relative !important;
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

#form-alert {
  position: absolute;
  top: 20px;
  z-index: 5;
  left: 50%;
}

</style>
