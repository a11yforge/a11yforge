<script setup lang="ts"></script>

<template>
  <footer class="flex items-center justify-between py-[1.1rem] px-8 border-t border-[var(--border)] text-[var(--muted)] text-[0.85rem]">
    <span>© a11yforge</span>
    <nav aria-label="Rechtliches" class="flex gap-[1.2rem]">
      <router-link to="/impressum" class="text-[var(--muted)] no-underline hover:text-[var(--coral)]">Impressum</router-link>
      <router-link to="/datenschutz" class="text-[var(--muted)] no-underline hover:text-[var(--coral)]">Datenschutz</router-link>
    </nav>
  </footer>
</template>
