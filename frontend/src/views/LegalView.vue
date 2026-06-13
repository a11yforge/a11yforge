<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isPrivacy = computed(() => route.path === '/datenschutz')
</script>

<template>
  <div class="page">
    <template v-if="!isPrivacy">
      <h1 class="title">Impressum</h1>
      <div class="card">
        <p><strong>Name / Verantwortlich:</strong> &lt;Platzhalter&gt;</p>
        <p><strong>Adresse:</strong> &lt;Platzhalter&gt;</p>
        <p><strong>Kontakt (E-Mail):</strong> &lt;Platzhalter&gt;</p>
      </div>
    </template>

    <template v-else>
      <h1 class="title">Datenschutz</h1>
      <div class="card">
        <p><strong>Welche Daten:</strong> Account, Projekte, Scans.</p>
        <p><strong>Zweck &amp; Speicherung:</strong> &lt;Platzhalter&gt;</p>
        <p><strong>Kontakt für Auskunft:</strong> &lt;Platzhalter&gt;</p>
      </div>
    </template>
  </div>
</template>

<style scoped>
.page {
  max-width: 720px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.title {
  font-size: 1.7rem;
  font-weight: 800;
  color: var(--text);
  margin: 0 0 1.2rem;
}
.card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.5rem 1.6rem;
  line-height: 1.8;
}
.card p {
  margin: 0 0 0.6rem;
  color: var(--muted);
}
.card strong {
  color: var(--text);
}
</style>
