<template>
    <div class="subpage" id="map-subpage">
      <ListingsContainer/>
    </div>
</template>

<script>
import ListingsContainer from './sub-components/map-subpage/ListingsContainer.vue'

export default {
  name: 'Template',
  components: {
    ListingsContainer
  },
  props: {
    userData: Object
  }
}
</script>

<style scoped>
  #map-subpage {
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
  }
</style>
