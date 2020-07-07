<template>
  <div class="subpage" id="my-listings-subpage">
    <MyListingsContainer class="property-list"/>
      <ListingForm class="form"/>
  </div>
</template>

<script>
import ListingForm from './sub-components/myListings-subpage/EditListing.vue'
import MyListingsContainer from './sub-components/myListings-subpage/MyListingsContainer.vue'

export default {
  name: 'MyListingsSubpage',
  props: {
    msg: String
  },
  components: {
    ListingForm,
    MyListingsContainer
  }
}
</script>

<style scoped>
.property-list {
  min-width: 550px;
  height: 100%;
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  border-right: 1px solid #dbdbdb;
  padding: 16px;
}

.form {
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
  flex-direction: column;
  padding: 30px;
}

#my-listings-subpage {
  display: flex; 
  align-items: flex-start;
  justify-content: flex-start;
}
</style>
