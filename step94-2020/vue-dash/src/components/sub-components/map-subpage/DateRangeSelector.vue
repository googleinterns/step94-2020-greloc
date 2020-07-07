<template>
  <div class="date-range-selector">
    <div class="picker-container" id="start-date-cont">      
      <span class="range-label">Start Date:</span>      
      <v-menu
        ref="menu"
        v-model="menu"
        :close-on-content-click="false"
        :return-value.sync="date"
        transition="scale-transition"
        offset-y
      >
        <template v-slot:activator="{ on, attrs }">
          <input
            type="text"
            v-model="date"
            v-bind="attrs"
            v-on="on"
            class="picker-button"
          />
        </template>

        <v-date-picker v-model="date" no-title scrollable>
          <v-spacer></v-spacer>
          <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
          <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
        </v-date-picker>
      </v-menu>
    </div>
    
    <div id="end-date">
      <span class="range-label">End Date:</span>

      <v-menu
        ref="menu"
        v-model="menu"
        :close-on-content-click="false"
        :return-value.sync="date"
        transition="scale-transition"
        offset-y
      >
        <template v-slot:activator="{ on, attrs }">
          <input
            type="text"
            v-model="date"
            v-bind="attrs"
            v-on="on"
            class="picker-button"
          />
        </template>

        <v-date-picker v-model="date" no-title scrollable>
          <v-spacer></v-spacer>
          <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
          <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
        </v-date-picker>
      </v-menu>      
    </div>
  </div>
</template>

<script>
export default {
  name: 'DateRangeSelector',
  components: {
  },
  props: {
    msg: String
  },
  data: () => ({
    date: new Date().toISOString().substr(0, 10),
    menu: false,
  }),
  methods: {
    
  }
}
</script>

<style scoped>

  .date-range-selector {
    width: 100%;
    height: 80px;

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

</style>
