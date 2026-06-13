<script setup lang="ts"></script>

<template>
  <footer class="flex items-center justify-between py-[1.1rem] px-8 border-t border-[#322840] text-[#a99cb0] text-[0.85rem]">
    <span>© a11yforge</span>
    <nav aria-label="Rechtliches" class="flex gap-[1.2rem]">
      <router-link to="/impressum" class="text-[#a99cb0] no-underline hover:text-[#ff7a52]">Impressum</router-link>
      <router-link to="/datenschutz" class="text-[#a99cb0] no-underline hover:text-[#ff7a52]">Datenschutz</router-link>
    </nav>
  </footer>
</template>
