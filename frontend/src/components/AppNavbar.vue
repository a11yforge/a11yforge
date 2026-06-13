<script setup lang="ts">
import { storeToRefs } from 'pinia'
import Button from 'primevue/button'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const { isAuthenticated, userName } = storeToRefs(authStore)

async function handleLogout() {
  await authStore.logout()
}
</script>

<template>
  <nav
    class="flex items-center justify-between px-6 py-3 border-b border-gray-700"
    aria-label="Hauptnavigation"
  >
    <div class="flex items-center gap-2">
      <router-link to="/" class="text-lg font-semibold hover:text-blue-400">
        A11yForge
      </router-link>
    </div>

    <div class="flex items-center gap-4">
      <template v-if="isAuthenticated">
        <router-link
          to="/home"
          class="hover:text-blue-400"
          active-class="text-blue-400"
        >
          Home
        </router-link>
        <router-link
          to="/projects"
          class="hover:text-blue-400"
          active-class="text-blue-400"
        >
          Projekte
        </router-link>
        <router-link
          v-if="userName"
          to="/account"
          class="text-gray-400 text-sm hover:text-blue-400"
          active-class="text-blue-400"
        >
          {{ userName }}
        </router-link>
        <Button
          icon="pi pi-sign-out"
          label="Logout"
          severity="secondary"
          size="small"
          @click="handleLogout"
        />
      </template>

      <template v-else>
        <router-link
          to="/login"
          class="hover:text-blue-400"
          active-class="text-blue-400"
        >
          Login
        </router-link>
        <router-link
          to="/register"
          class="hover:text-blue-400"
          active-class="text-blue-400"
        >
          Registrieren
        </router-link>
      </template>
    </div>
  </nav>
</template>

