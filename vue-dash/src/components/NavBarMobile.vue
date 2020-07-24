<template>
  <div id="nav-container"> 
    <v-app-bar id="toolbar" app fixed dark color="var(--nav-bar-color)" elevation="2" height="auto">        
      
      <div id="toolbar-content-container">
        <v-toolbar-title>gRelo</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-app-bar-nav-icon v-on:click="drawer = !drawer" ></v-app-bar-nav-icon>
      </div>
    </v-app-bar>

    <v-navigation-drawer
        v-model="drawer"
        temporary
        fixed
        right
    >
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title>{{currentPage}}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-list-item v-if="isUserLoggedIn">
        <v-list-item-content>
          <v-list-item-title>{{userEmail}}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list>
        <v-list-item 
          v-for="item in items"
          :key="item.title"
          :to="item.routePath"
          v-on:click="currentPage = item.title"
          link
        >
          <v-list-item-icon>
            <v-icon>{{item.icon}}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{item.title}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item 
            v-if="isUserLoggedIn"
            v-on:click="logout()"
            link
        >
          <v-list-item-icon>
            <v-icon>mdi-account</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Logout</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
export default {
  name: 'NavBarMobile',
  methods: {
    logout: function () {
      // TODO: implement later
    }
  },

  data: () => ({
    drawer: false,
    currentPage: "Map",
    isUserLoggedIn: false,
    items: [
      { title: 'Map', icon: 'mdi-map', routePath: 'map'},
      { title: 'MyListings', icon: 'mdi-map-marker', routePath: 'mylistings' },
    ],    
  }),
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  #nav-container {
    width: 100%;
    height: auto;
    overflow: hidden;
  }
  #toolbar {
    width: 100%;
  }
  .content-li {
    padding: 0;
  }
  .content-li-title {
    font-weight: bold;
     font-size: 18px;
  }
  .content-li-subtitle {
    font-size: 14px;
  }
  #toolbar-content-container {
    width: 100%; 
    max-width: 900px; 
    display: flex; 
    align-items: center; 
    justify-content: space-between; 
    margin: auto;
  }
  #toolbar-list {
    height: 100%;
    width: 100%;
    background-color: #343333;
    padding: 0;
  }
</style>