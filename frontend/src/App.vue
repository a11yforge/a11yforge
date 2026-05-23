<script setup lang="ts">
import { RouterView } from 'vue-router'
import Toast from 'primevue/toast'
import ConfirmDialog from 'primevue/confirmdialog'
import AppNavbar from './components/AppNavbar.vue'
</script>

<template>
  <AppNavbar />
  <RouterView />
  <Toast />
  <ConfirmDialog />
</template>
