<script setup lang="ts">
import { ref } from 'vue'

const loading = ref(false)
const hasScans = ref(true)

const project = {
  name: 'Beispiel-Projekt',
  baseUrl: 'https://example.com',
  crawlMaxPages: 10,
}

const scans = [
  { id: 12, status: 'COMPLETED', startedAt: '2026-06-06T12:00:00Z', violationCount: 12 },
  { id: 9, status: 'RUNNING', startedAt: '2026-06-05T10:00:00Z', violationCount: 20 },
  { id: 7, status: 'FAILED', startedAt: '2026-06-05T09:00:00Z', violationCount: 0 },
]

const statusMeta: Record<string, { icon: string; label: string; cls: string }> = {
  COMPLETED: { icon: '✅', label: 'Fertig', cls: 'completed' },
  RUNNING: { icon: '⏳', label: 'Läuft', cls: 'running' },
  FAILED: { icon: '❌', label: 'Fehler', cls: 'failed' },
}
function meta(status: string) {
  return statusMeta[status] ?? { icon: '•', label: status, cls: 'running' }
}
function fmt(iso: string) {
  return new Date(iso).toLocaleDateString('de-AT')
}
</script>

<template>
  <div class="max-w-[880px] mx-auto px-6 pt-8 pb-12 text-[#f3e9e2] font-sans">
    <router-link to="/projects" class="inline-block mb-[1.4rem] text-[#a99cb0] no-underline text-[0.9rem] hover:text-[#ff7a52]">← Zurück zu Projekte</router-link>

    <header class="flex justify-between items-start gap-6 flex-wrap bg-[#1e1a29] border border-[#322840] rounded-[16px] px-[1.7rem] py-[1.6rem] shadow-[0_18px_44px_rgba(0,0,0,0.35)]">
      <div>
        <h1 class="text-[1.7rem] font-extrabold text-[#f3e9e2] m-0">{{ project.name }}</h1>
        <p class="mt-2 mb-0 text-[#a99cb0] text-[0.92rem]">
          URL:
          <a :href="project.baseUrl" target="_blank" rel="noopener" class="text-[#5bbeb2] no-underline hover:underline">
            {{ project.baseUrl }} ↗
          </a>
          <span class="mx-2 text-[#322840]">·</span>
          Max. Seiten: {{ project.crawlMaxPages }}
        </p>
      </div>
      <div class="flex gap-2 flex-wrap">
        <button class="border border-transparent rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-[#ff7a52] text-[#2a1410] hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button">Scannen ▶</button>
        <button class="border border-[#322840] rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-transparent text-[#f3e9e2] hover:border-[#ff7a52] hover:text-[#ff7a52] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button">✏ Bearbeiten</button>
        <button class="border border-[#5a2a2a] rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-transparent text-[#f6a3a3] hover:bg-[#3a1a1a] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button">🗑 Löschen</button>
      </div>
    </header>

    <section class="mt-8">
      <div class="flex items-baseline justify-between mb-4">
        <h2 class="text-[1.15rem] font-bold text-[#f3e9e2] m-0">Letzte Scans</h2>
        <router-link
          v-if="hasScans && !loading"
          :to="`/projects/1/scans`"
          class="text-[#ff7a52] no-underline text-[0.9rem] font-semibold hover:underline"
        >
          Alle Scans ansehen →
        </router-link>
      </div>

      <div v-if="loading" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] p-8 text-center text-[#a99cb0]">Scans werden geladen…</div>

      <div v-else-if="!hasScans" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] p-8 text-center text-[#a99cb0] flex flex-col items-center gap-[0.9rem]">
        <img src="/mole.png" alt="" aria-hidden="true" class="w-[72px] [image-rendering:pixelated] opacity-85" />
        <p>Noch kein Scan — starte den ersten.</p>
        <button class="border border-transparent rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-[#ff7a52] text-[#2a1410] hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button">Scannen ▶</button>
      </div>

      <ul v-else class="list-none m-0 p-0 flex flex-col gap-[0.6rem]">
        <li v-for="scan in scans" :key="scan.id" class="flex items-center gap-4 bg-[#1e1a29] border border-[#322840] rounded-[12px] py-[0.85rem] px-[1.1rem]">
          <span class="font-bold min-w-[5.5rem]">Scan #{{ scan.id }}</span>
          <span
            class="rounded-full py-[0.2rem] px-[0.65rem] text-[0.74rem] font-bold whitespace-nowrap"
            :class="{
              'bg-[rgba(91,190,178,0.15)] text-[#5bbeb2]': meta(scan.status).cls === 'completed',
              'bg-[rgba(246,200,154,0.15)] text-[#f6c89a]': meta(scan.status).cls === 'running',
              'bg-[rgba(255,122,82,0.15)] text-[#ff7a52]': meta(scan.status).cls === 'failed',
            }"
          >
            {{ meta(scan.status).icon }} {{ meta(scan.status).label }}
          </span>
          <span class="text-[#a99cb0] text-[0.9rem]">{{ fmt(scan.startedAt) }}</span>
          <span class="text-[#a99cb0] text-[0.9rem] ml-auto">{{ scan.violationCount }} Befunde</span>
          <router-link :to="`/scans/${scan.id}`" class="bg-transparent border border-[#322840] rounded-[9px] py-[0.4rem] px-[0.9rem] text-[#f3e9e2] no-underline text-[0.88rem] font-semibold hover:border-[#ff7a52] hover:text-[#ff7a52]">Öffnen</router-link>
        </li>
      </ul>
    </section>
  </div>
</template>
