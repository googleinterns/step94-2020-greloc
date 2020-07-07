<template>
  <div class="listing-form"> 
    <div class="listing-title"> 
      <v-text-field class="listing-title-entry"
      label = "Listing Title"
      filled>
      </v-text-field>
    </div>
    <div class="listing-address">
      <v-text-field class="listing-address-entry"
        solo
        label="Street Address"
        prepend-inner-icon="mdi-map-marker"
        filled>
      </v-text-field>
        <div class="location">
          <v-text-field class="listing-city-entry"
            solo
            label="City"
            filled>
          </v-text-field>
          <v-text-field class="listing-state-entry"
            solo
            label="State/Province"
            filled>
          </v-text-field>
        </div>
    </div>
    <!--Insert Image Components Here/Drag and Drop-->
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
      <v-textarea
        solo
        name="input-7-4"
        label="Property Description">
      </v-textarea>
        <div class="selectors">
          <v-select class="total-beds"
            :items="items"
            label="Total Number of Beds">
          </v-select>
          <v-select class="total-bedrooms"
              :items="bedrooms"
              label="Bedrooms">
          </v-select>
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
        filled>
      </v-text-field>
      <v-text-field class="email-entry"
        solo
        label="Contact Email"
        filled>
      </v-text-field>
      <v-text-field class="phone-number-entry"
        solo
        label="Contact Number"
        filled>
      </v-text-field>
    </div>
    <v-btn class="submit-button" rounded color="primary" dark>Submit Listing</v-btn>
  </div>
</template>

<script>
export default {
  name: 'ListingForm',
  components: {
  },
  props: {
    msg: String
  },
  data: () => ({
    items: ['1', '2', '3', '4', '5'],
    bedrooms: ['1', '2', '3', '4', '5'],
    baths: ['1', '2', '3'],
  }),
  methods: {
    
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
</style>
