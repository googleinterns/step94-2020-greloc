<template>
  <div class="insert-images-input">
    <v-file-input
      :rules="rules"
      accept="image/png, image/jpeg, image/bmp"
      placeholder="Upload Property Images"
      prepend-icon="mdi-camera"
      label="Images"
      multiple
      chips
    ></v-file-input>
  </div>
</template>

<script>
export default {
  name: 'ImageInput',
  components: {
  },
  props: {
    msg: String
  },
  data: () => ({
    rules: [],
  }),
  methods: {
    
  }
}
</script>

<style scoped>

</style>
