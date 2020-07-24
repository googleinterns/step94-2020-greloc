<template>
  <div id="app">
    <v-app>
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,500,700,400italic|Material+Icons" rel="stylesheet">
        <div id="dashboard-container">          
          <NavBarMobile v-if="$vuetify.breakpoint.mobile"/>
          <NavBar v-else/>
          
          <v-main>
            <router-view :key="$route.fullPath"/>
          </v-main>          
        </div>
    </v-app>
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'
import NavBarMobile from './components/NavBarMobile.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    NavBarMobile
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

    --size-sm-max-width: 600px;
    --size-md-max-width: 960px;
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

  @media screen and (max-width: var(--size-md-max-width)) {
    div.example {
      flex-direction: column;
    }
  }
</style>

