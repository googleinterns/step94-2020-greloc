<template>
  <div class="date-range-selector">
    
    <div class="picker-container" id="start-date-cont">      
      <span class="range-label">Start Date:</span>
      <span class="picker-button" @click="dateSelectorOpen=true">{{formattedStartDate}}</span>
    </div>
    
    <div class="picker-container" id="end-date">
      <span class="range-label">End Date:</span>
      <span class="picker-button" @click="dateSelectorOpen=true">{{formattedEndDate}}</span>
    </div>

    <transition name="fade">
      <v-date-picker
        v-if="dateSelectorOpen"
        id="drs-date-selector"
        v-model="dateRange" 
        scrollable
        multiple
        landscape
        elevation="5"
        range
        show-current
        :min="minDate"
      >
        <v-spacer></v-spacer>
        <v-btn text color="primary" @click="clearDates()">Clear</v-btn>
        <v-btn text color="primary" @click="datesReady()">Ok</v-btn>
      </v-date-picker>
    </transition>    
  </div>
</template>

<script>
import { EVENTS } from '../../../utils/constants.js'
export default {
  name: 'DateRangeSelector',
  components: {
  },
  data: () => ({
    dateSelectorOpen: false,
    dateRange: [],
  }),

  computed: {
    minDate: function() {
      const today = new Date();
      return today.toISOString().substring(0,10);
    },
    
    formattedStartDate: function () {
      if (this.dateRange.length < 1) return " ";
      let localDate = getLocalDate(this.dateRange[0]);
      return getFormattedDate(localDate);
    },

    formattedEndDate: function () {
      if (this.dateRange.length < 2) return " ";
      let localDate = getLocalDate(this.dateRange[1]);
      return getFormattedDate(localDate);
    }    
  },
  
  watch: {
    // Ensures dates are not reversed. i.e. (7/20 - 7/15)
    dateRange: function() {
      if (this.dateRange.length !== 2) return;
      let dateOne = new Date(this.dateRange[0]);
      let dateTwo = new Date(this.dateRange[1]);

      if (dateTwo < dateOne) this.dateRange.reverse();
    }
  },

  methods: {
    datesReady: function () {
      this.dateSelectorOpen = false;
      this.$root.$emit(EVENTS.dateRangeSelected, this.dateRange);
    },

    clearDates: function(){
      this.dateRange = [];
    }
  }
}

// MARK: HELPER FUNCTIONS
function getLocalDate(dateString) {
  let utcDate = new Date(dateString);
  return new Date(utcDate.getTime() + utcDate.getTimezoneOffset() * 60000);
}

function getFormattedDate(date) {
  let month = date.getMonth() + 1;
  let day = date.getDate();
  let year = date.getFullYear();
  return month + "/" + day + "/" + year;
}
</script>

<style scoped>

  .date-range-selector {
    width: 100%;
    height: 80px;
    position: relative;

    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;

    margin-top: -20px;
    margin-bottom: 1rem;
  }

  .picker-container {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  #start-date-cont {
    margin-right: 4rem;
  }

  .range-label {
    font-weight: bold;
    margin-right: .5rem;
    color: var(--text-subtle);
  }

  .picker-button {
    width: 100px;
    padding: 10px;

    border-radius: 2px;
    background-color: #ededed;
  }

  #drs-date-selector {
    position: absolute;
    top: 50px;
    left: 50%;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);

    z-index: 5;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to {
    opacity: 0;
  }

</style>
