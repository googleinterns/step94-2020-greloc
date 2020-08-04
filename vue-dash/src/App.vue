<template>
  <div id="app" v-if="isUserAuth">
    <v-app>
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,500,700,400italic|Material+Icons" rel="stylesheet">
        <div id="dashboard-container">          
          <NavBarMobileBottom 
            v-if="$vuetify.breakpoint.mobile"
          />
          <NavBar v-else/>
          
          <v-main>
            <router-view :key="$route.fullPath"/>
          </v-main>          
        </div>
    </v-app>
  </div>

  <div v-else>
    Authorizing...
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'
import NavBarMobileBottom from './components/NavBarMobileBottom.vue'
import { fetchUserData, createUserCookieWithData } from './utils/authmanager.js'

export default {
  name: 'App',
  
  async created() {
    let userData = await fetchUserData();
    if (userData !== null) {
      this.isUserAuth = true;
      this.$forceUpdate();
      createUserCookieWithData(userData);
    } else {
      location.href = "/";      
    }  
  },

  state: {
    isUserAuth: false
  },
  components: {
    NavBar,
    NavBarMobileBottom
  }
}
</script>

<style>
  @import url(https://fonts.googleapis.com/css?family=Roboto&display=swap);

  :root {
    --background-color-light: #FBFBFB;
    
    --branding-blue: #4285f4;
    --branding-red: #ea4335;
    --branding-yellow: #fbbc05;
    --branding-green: #34a853;

    --nav-bar-color: #505050;

    --text-heading: #515151;
    --text-subtle: #727272;
    --text-main: #5c5c5c;

    --size-sm-max-width: 600px;
    --size-md-max-width: 1025px;
  }

  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    -ms-overflow-style: none;
  }

  * ::-webkit-scrollbar {
    display: none;
  }

  body {
    font-family: 'Roboto', sans-serif;
    line-height: 1.4;
  }

  #app {
    background-color: #292929;
    min-height: 100vh;
    width: 100vw;
    overflow: hidden;
  }

  #unauth-blank {
    height: 100%;
    width: 100%;
    background-color: #ffffff;
  }

  #dashboard-container {
    height: 100vh;
    width: 100vw;

    display: flex;
    flex-direction: row;
    align-items: flex-start;
  }

  .subpage {
    background-color: var(--background-color-light);
    height: 100vh;
    width: 100%;
  }

  .subpage-title {
    margin-bottom: 1rem;
    color: #515151;
  }

  .empty-state-icon {
    margin-bottom: 1rem;
  }

  .empty-state-text {
    color: rgba(0,0,0,.87);
  }

  @media screen and (max-width: 1025px) {
    .subpage {
      padding-bottom: 60px !important;
    }
  }
</style>

