<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import Toast from 'primevue/toast'
import ConfirmDialog from 'primevue/confirmdialog'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import PublicLayout from '@/layouts/PublicLayout.vue'
import AuthLayout from '@/layouts/AuthLayout.vue'

const route = useRoute()

const layouts = {
  default: DefaultLayout,
  public: PublicLayout,
  auth: AuthLayout,
} as const

// route.meta.layout decides the wrapper; no meta -> bare (e.g. 404)
const layout = computed(() => {
  const key = route.meta.layout as keyof typeof layouts | undefined
  return key ? layouts[key] : null
})
</script>

<template>
  <component :is="layout" v-if="layout">
    <RouterView />
  </component>
  <RouterView v-else />

  <Toast />
  <ConfirmDialog />
</template>
