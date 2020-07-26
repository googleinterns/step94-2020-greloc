<template>
    <v-bottom-navigation
    :value="activeBtn"
    absolute
    id="bottom-nav-mobile"
    dark
    grow
    shift
    height="60px"
  >
    <v-btn
      v-for="item in items"
      :key="item.title"
      :to="item.routePath"
    >
      <span>{{item.title}}</span>
      <v-icon>{{item.icon}}</v-icon>
    </v-btn>

    <div id="branding-bar-bottom">
      <div id="branding-blue"></div>
      <div id="branding-red"></div>
      <div id="branding-yellow"></div>
      <div id="branding-green"></div>
    </div>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: 'NavBarMobileBottom',
  components: {
  },
  props: {
    msg: String
  },
  data: () => ({
    activeBtn: 0,
    items: [
      { title: 'Map', icon: 'mdi-map', routePath: 'map'},
      { title: 'MyListings', icon: 'mdi-map-marker', routePath: 'mylistings' },
    ],    
  }),

  methods: {
    
  }
}
</script>

<style scoped>
  #bottom-nav-mobile {
    background-color: var(--nav-bar-color);
  }

  #branding-bar-bottom {
    height: 1px;
    width: 100vw;

    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;


    position: absolute;
    bottom: 0;
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
