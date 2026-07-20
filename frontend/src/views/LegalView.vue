<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isPrivacy = computed(() => route.path === '/datenschutz')
</script>

<template>
  <div class="max-w-[720px] mx-auto px-6 pt-8 pb-12 text-[var(--text)] font-sans">
    <template v-if="!isPrivacy">
      <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] mt-0 mb-[1.2rem]">Impressum</h1>
      <div class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.6rem] py-6 leading-[1.8]">
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Name / Verantwortlich:</strong> &lt;Platzhalter&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Adresse:</strong> &lt;Platzhalter&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Kontakt (E-Mail):</strong> &lt;Platzhalter&gt;</p>
      </div>
    </template>

    <template v-else>
      <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] mt-0 mb-[1.2rem]">Datenschutz</h1>
      <div class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.6rem] py-6 leading-[1.8]">
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Welche Daten:</strong> Account, Projekte, Scans.</p>
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Zweck &amp; Speicherung:</strong> &lt;Platzhalter&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[var(--muted)]"><strong class="text-[var(--text)]">Kontakt für Auskunft:</strong> &lt;Platzhalter&gt;</p>
      </div>
    </template>
  </div>
</template>
