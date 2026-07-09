<script setup lang="ts">
import AppNavbar from '@/components/layout/AppNavbar.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col text-[var(--text)] font-sans bg-[radial-gradient(900px_500px_at_72%_0%,rgba(255,122,82,0.1),transparent_60%),linear-gradient(160deg,#16121d_0%,#16121d_55%,#142323_100%)]">
    <AppNavbar />
    <main class="flex-1">
      <slot />
    </main>
    <AppFooter />
  </div>
</template>
