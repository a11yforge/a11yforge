<script setup lang="ts">
import AppNavbar from '@/components/layout/AppNavbar.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
</script>

<template>
  <div class="app">
    <AppNavbar />
    <main class="app__main">
      <slot />
    </main>
    <AppFooter />
  </div>
</template>

<style scoped>
.app {
  --bg: #16121d;
  --bg-2: #142323;
  --surface: #1e1a29;
  --field: #14111c;
  --text: #f3e9e2;
  --muted: #a99cb0;
  --coral: #ff7a52;
  --peach: #f6c89a;
  --teal: #5bbeb2;
  --border: #322840;

  min-height: 100vh;
  display: flex;
  flex-direction: column;
  color: var(--text);
  background:
    radial-gradient(900px 500px at 72% 0%, rgba(255, 122, 82, 0.1), transparent 60%),
    linear-gradient(160deg, var(--bg) 0%, var(--bg) 55%, var(--bg-2) 100%);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.app__main {
  flex: 1;
}
</style>
