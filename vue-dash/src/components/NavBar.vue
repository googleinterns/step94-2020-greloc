<template>
  <div id="dash-nav-container">
    <div id="branding-bar">
      <div id="branding-blue"></div>
      <div id="branding-red"></div>
      <div id="branding-yellow"></div>
      <div id="branding-green"></div>

    </div>
    <v-navigation-drawer
      v-model="drawer"
      permanent
      :mini-variant="true"
      mini-variant-width="80px"
      id="dash-nav-drawer"
    >
      <v-list-item class="px-2" id="dash-nav-header">
        <v-list-item-avatar>
          <v-img src="https://randomuser.me/api/portraits/men/85.jpg"></v-img>
        </v-list-item-avatar>

        <v-list-item-title>John Leider</v-list-item-title>
      </v-list-item>

      <v-list dense dark>
        <v-list-item
          v-for="item in items"
          :key="item.title"
          :to="item.routePath"
          class="dash-nav-button"
          dark
          link
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
export default {
  name: 'Template',
  props: {
    msg: String
  },
  data: () => ({
    drawer: true,
    items: [
      { title: 'Map', icon: 'mdi-map', routePath: 'map'},
      { title: 'MyListings', icon: 'mdi-map-marker', routePath: 'mylistings' },
    ],
  })
}
</script>

<style scoped>
  #dash-nav-container {
    position: relative;
    height: 100vh;
    width: auto;

    box-shadow: 1px 0px 4px rgba(0, 0, 0, 0.25);
  }

  #dash-nav-drawer {
    background-color: #505050;
  }

  #dash-nav-header {
    margin: 1rem 0 2rem 0;
  }

  .dash-nav-button {
    padding: 16px;
  }

  #branding-bar {
    height: 100vh;
    width: 5px;

    display: grid;
    grid-template-rows: 1fr 1fr 1fr 1fr;

    position: absolute;
    left: 0;

    z-index: 10;
  }

  #branding-blue {
    background-color: var(--branding-blue);
  }

  #branding-red {
    background-color: var(--branding-red);
  }

  #branding-yellow {
    background-color: var(--branding-yellow);
  }

  #branding-green {
    background-color: var(--branding-green);
  }
</style>
