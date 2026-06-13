<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isPrivacy = computed(() => route.path === '/datenschutz')
</script>

<template>
  <div class="max-w-[720px] mx-auto px-6 pt-8 pb-12 text-[#f3e9e2] font-sans">
    <template v-if="!isPrivacy">
      <h1 class="text-[1.7rem] font-extrabold text-[#f3e9e2] mt-0 mb-[1.2rem]">Impressum</h1>
      <div class="bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.6rem] py-6 leading-[1.8]">
        <p class="mt-0 mb-[0.6rem] text-[#a99cb0]"><strong class="text-[#f3e9e2]">Name / Verantwortlich:</strong> &lt;Platzhalter&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[#a99cb0]"><strong class="text-[#f3e9e2]">Adresse:</strong> &lt;Platzhalter&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[#a99cb0]"><strong class="text-[#f3e9e2]">Kontakt (E-Mail):</strong> &lt;Platzhalter&gt;</p>
      </div>
    </template>

    <template v-else>
      <h1 class="text-[1.7rem] font-extrabold text-[#f3e9e2] mt-0 mb-[1.2rem]">Datenschutz</h1>
      <div class="bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.6rem] py-6 leading-[1.8]">
        <p class="mt-0 mb-[0.6rem] text-[#a99cb0]"><strong class="text-[#f3e9e2]">Welche Daten:</strong> Account, Projekte, Scans.</p>
        <p class="mt-0 mb-[0.6rem] text-[#a99cb0]"><strong class="text-[#f3e9e2]">Zweck &amp; Speicherung:</strong> &lt;Platzhalter&gt;</p>
        <p class="mt-0 mb-[0.6rem] text-[#a99cb0]"><strong class="text-[#f3e9e2]">Kontakt für Auskunft:</strong> &lt;Platzhalter&gt;</p>
      </div>
    </template>
  </div>
</template>
