<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScanFromProject, type ScanResponseDTO } from '@/api/scan'
import ScanHistoryToolbar from '@/components/scan/ScanHistoryToolbar.vue'
import ScanHistoryTable from '@/components/scan/ScanHistoryTable.vue'
import ScanTrendChart from '@/components/scan/ScanTrendChart.vue'

const route = useRoute()
const loading = ref(true)
const error = ref<string | null>(null)
const projectId = Number(route.params.id)
const scans = ref<ScanResponseDTO[]>([])
const statusFilter = ref('ALL')
const sortOrder = ref<'newest' | 'oldest'>('newest')

onMounted(async () => {
  try {
    scans.value = await getScanFromProject(projectId)
  } catch {
    error.value = 'Scan konnte nicht geladen werden!'
  } finally {
    loading.value = false
  }
})

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
  <div class="max-w-[920px] mx-auto px-6 pt-8 pb-12 text-[var(--text)] font-sans">
    <router-link :to="`/projects/${projectId}`" class="inline-block mb-[1.2rem] text-[var(--muted)] no-underline text-[0.9rem] hover:text-[var(--coral)]">← Zurück zum Projekt</router-link>

    <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] mt-0 mb-[1.2rem]">Scan-Historie</h1>

    <ScanHistoryToolbar v-model:status="statusFilter" v-model:sort="sortOrder" />

    <ScanHistoryTable :scans="displayedScans" :loading="loading" :error="error" />

    <ScanTrendChart :scans="scans" class="mt-6" />
  </div>
</template>
