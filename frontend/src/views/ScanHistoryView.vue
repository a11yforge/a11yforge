<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScanFromProject, type ScanResponseDTO } from '../api/scan'
import Chart from 'primevue/chart'

const route = useRoute()
const loading = ref(true)
const error = ref<string | null>(null)
const projectId = Number(route.params.id)
const scans = ref<ScanResponseDTO[]>([])
const statusFilter = ref('ALL')
const sortOrder = ref<'newest' | 'oldest'>('newest')

const chartData = computed(() => {
  const chrono = [...scans.value].sort(
    (a, b) => new Date(a.startedAt).getTime() - new Date(b.startedAt).getTime()
  )
  return {
    labels: chrono.map((s) => new Date(s.startedAt).toLocaleDateString('de-AT')),
    datasets: [{ label: 'Befunde', data: chrono.map((s) => s.violationCount) }],
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { labels: { color: '#a99cb0' } } },
  scales: {
    x: { ticks: { color: '#a99cb0' }, grid: { color: '#322840' } },
    y: { beginAtZero: true, ticks: { color: '#a99cb0' }, grid: { color: '#322840' } },
  },
}

onMounted(async () => {
  try {
    scans.value = await getScanFromProject(projectId)
  } catch {
    error.value = 'Scan konnte nicht geladen werden!'
  } finally {
    loading.value = false
  }
})

const statusMeta: Record<string, { icon: string; label: string; cls: string }> = {
  COMPLETED: { icon: '✅', label: 'Fertig', cls: 'completed' },
  RUNNING: { icon: '⏳', label: 'Läuft', cls: 'running' },
  FAILED: { icon: '❌', label: 'Fehler', cls: 'failed' },
}
function meta(status: string) {
  return statusMeta[status] ?? { icon: '•', label: status, cls: 'running' }
}
function fmt(iso: string | null) {
  return iso ? new Date(iso).toLocaleString('de-AT') : '–'
}

const displayedScans = computed(() => {
  const filtered =
    statusFilter.value === 'ALL'
      ? scans.value
      : scans.value.filter((s) => s.status === statusFilter.value)

  return [...filtered].sort((a, b) => {
    const diff = new Date(b.startedAt).getTime() - new Date(a.startedAt).getTime()
    return sortOrder.value === 'newest' ? diff : -diff
  })
})
</script>

<template>
  <div class="max-w-[920px] mx-auto px-6 pt-8 pb-12 text-[#f3e9e2] font-sans">
    <router-link :to="`/projects/${projectId}`" class="inline-block mb-[1.2rem] text-[#a99cb0] no-underline text-[0.9rem] hover:text-[#ff7a52]">← Zurück zum Projekt</router-link>

    <h1 class="text-[1.7rem] font-extrabold text-[#f3e9e2] mt-0 mb-[1.2rem]">Scan-Historie</h1>

    <div class="flex gap-6 flex-wrap mb-[1.2rem]">
      <label class="text-[#a99cb0] text-[0.9rem] flex items-center gap-2">Filter:
        <select v-model="statusFilter" class="bg-[#14111c] border border-[#322840] rounded-lg text-[#f3e9e2] py-[0.4rem] px-[0.6rem]">
          <option value="ALL">Alle</option>
          <option value="COMPLETED">Fertig</option>
          <option value="RUNNING">Läuft</option>
          <option value="FAILED">Fehler</option>
        </select>
      </label>
      <label class="text-[#a99cb0] text-[0.9rem] flex items-center gap-2">Sortierung:
        <select v-model="sortOrder" class="bg-[#14111c] border border-[#322840] rounded-lg text-[#f3e9e2] py-[0.4rem] px-[0.6rem]">
          <option value="newest">Neueste zuerst</option>
          <option value="oldest">Älteste zuerst</option>
        </select>
      </label>
    </div>

    <div v-if="loading" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] p-8 text-center text-[#a99cb0]">Lädt…</div>
    <div v-else-if="error" class="bg-[#1e1a29] border border-[#5a2a2a] rounded-[14px] p-8 text-center text-[#f6a3a3]">{{ error }}</div>
    <div v-else-if="scans.length === 0" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] p-8 text-center text-[#a99cb0]">Noch keine Scans für dieses Projekt.</div>

    <div v-else class="bg-[#1e1a29] border border-[#322840] rounded-[14px] overflow-x-auto">
      <table class="w-full border-collapse text-[0.9rem]">
        <thead>
          <tr>
            <th class="text-left text-[#a99cb0] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[#322840]">#</th>
            <th class="text-left text-[#a99cb0] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[#322840]">Status</th>
            <th class="text-left text-[#a99cb0] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[#322840]">Gestartet</th>
            <th class="text-left text-[#a99cb0] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[#322840]">Abgeschlossen</th>
            <th class="text-left text-[#a99cb0] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[#322840]">Befunde</th>
            <th class="text-left text-[#a99cb0] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[#322840]"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="scan in displayedScans" :key="scan.id" class="group">
            <td class="py-[0.8rem] px-4 border-b border-[#322840] text-[#f3e9e2] group-last:border-b-0 font-mono font-bold">#{{ scan.id }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[#322840] text-[#f3e9e2] group-last:border-b-0">
              <span
                class="rounded-full py-[0.2rem] px-[0.6rem] text-[0.74rem] font-bold whitespace-nowrap"
                :class="{
                  'bg-[rgba(91,190,178,0.15)] text-[#5bbeb2]': meta(scan.status).cls === 'completed',
                  'bg-[rgba(246,200,154,0.15)] text-[#f6c89a]': meta(scan.status).cls === 'running',
                  'bg-[rgba(255,122,82,0.15)] text-[#ff7a52]': meta(scan.status).cls === 'failed',
                }"
              >
                {{ meta(scan.status).icon }} {{ meta(scan.status).label }}
              </span>
            </td>
            <td class="py-[0.8rem] px-4 border-b border-[#322840] text-[#a99cb0] group-last:border-b-0">{{ fmt(scan.startedAt) }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[#322840] text-[#a99cb0] group-last:border-b-0">{{ fmt(scan.completedAt) }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[#322840] text-[#a99cb0] group-last:border-b-0">{{ scan.violationCount }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[#322840] text-[#f3e9e2] group-last:border-b-0"><router-link :to="`/scans/${scan.id}`" class="text-[#ff7a52] no-underline font-semibold hover:underline">Öffnen</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="mt-6 bg-[#1e1a29] border border-[#322840] rounded-[14px] p-6 h-[300px]">
      <Chart type="line" :data="chartData" :options="chartOptions" class="h-full" />
    </div>
  </div>
</template>
