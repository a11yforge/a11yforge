<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const { isAuthenticated, userName } = storeToRefs(authStore)

async function handleLogout() {
  await authStore.logout()
}
</script>

<template>
  <nav class="flex items-center justify-between py-[0.9rem] px-8 border-b border-[#322840]" aria-label="Hauptnavigation">
    <router-link to="/" class="flex items-center gap-[0.6rem] no-underline text-[#f3e9e2]" aria-label="Zur Startseite">
      <img class="w-[34px] h-[34px] rounded-[9px] object-cover object-[center_18%] border border-[#322840] [image-rendering:pixelated]" src="/mole.png" alt="" aria-hidden="true" />
      <span class="font-extrabold text-[1.15rem] tracking-[0.2px]">a11y<span class="text-[#ff7a52]">forge</span></span>
    </router-link>

    <div class="flex items-center gap-[1.3rem]">
      <template v-if="isAuthenticated">
        <router-link to="/projects" class="text-[#a99cb0] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[#f3e9e2]" active-class="!text-[#ff7a52]">Projekte</router-link>
        <router-link to="/account" class="text-[#a99cb0] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[#f3e9e2]" active-class="!text-[#ff7a52]">Account</router-link>
        <router-link v-if="userName" to="/account" class="text-[#a99cb0] text-[0.85rem]" active-class="!text-[#ff7a52]">
          {{ userName }}
        </router-link>
        <button class="inline-flex items-center gap-[0.4rem] bg-transparent border border-[#322840] text-[#f3e9e2] rounded-[9px] py-[0.45rem] px-[0.85rem] text-[0.9rem] font-semibold cursor-pointer transition-[border-color,color] duration-[150ms] ease hover:border-[#ff7a52] hover:text-[#ff7a52] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button" @click="handleLogout">
          <i class="pi pi-sign-out" aria-hidden="true"></i>
          Logout
        </button>
      </template>

      <template v-else>
        <router-link to="/login" class="text-[#a99cb0] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[#f3e9e2]" active-class="!text-[#ff7a52]">Login</router-link>
        <router-link to="/register" class="text-[#a99cb0] no-underline text-[0.95rem] transition-colors duration-[150ms] ease hover:text-[#f3e9e2]" active-class="!text-[#ff7a52]">Registrieren</router-link>
      </template>
    </div>
  </nav>
</template>
