<script setup lang="ts"></script>

<template>
  <footer class="foot">
    <span class="foot__copy">© a11yforge</span>
    <nav aria-label="Rechtliches" class="foot__nav">
      <router-link to="/impressum">Impressum</router-link>
      <router-link to="/datenschutz">Datenschutz</router-link>
    </nav>
  </footer>
</template>

<style scoped>
.foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.1rem 2rem;
  border-top: 1px solid var(--border);
  color: var(--muted);
  font-size: 0.85rem;
}
.foot__nav {
  display: flex;
  gap: 1.2rem;
}
.foot__nav a {
  color: var(--muted);
  text-decoration: none;
}
.foot__nav a:hover {
  color: var(--coral);
}
</style>
