<template>
  <div id="app">
    <v-app>
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,500,700,400italic|Material+Icons" rel="stylesheet">
        <div id="dashboard-container">
          <NavBar/>
          
          <v-main>
            <router-view :key="$route.fullPath"/>
          </v-main>          
        </div>
    </v-app>
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'

export default {
  name: 'App',
  components: {
    NavBar
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

    --text-subtle: #727272;
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
    padding: 16px;
    background-color: var(--background-color-light);
    height: 100vh;
    width: 100%;
  }

  .subpage-title {
    margin-bottom: 1rem;
    color: #515151;
  }
</style>

