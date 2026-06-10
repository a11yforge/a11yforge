<script setup lang="ts">
import {ref, onMounted} from 'vue'
import { useRoute } from 'vue-router'
import { getScanFromProject, type ScanResponseDTO } from '../api/scan'

const route = useRoute()

const loading = ref(true)
const error = ref<string | null>(null)
const projectId = Number(route.params.id)
const scans = ref<ScanResponseDTO[]>([])

onMounted(async() => {
  try {
    scans.value = await getScanFromProject(projectId)
  } catch(e) {
    error.value = "Scan konnte nicht geladen werden!"
  } finally {
    loading.value = false
  }
})

</script>
<template>
  <h1>ScanHistoryView</h1>

  <div>
    <p v-if="loading">Lädt…</p>
    <p v-else-if="error">{{ error }}</p>
    <p v-else-if="scans.length === 0">Noch keine Scans für dieses Projekt.</p>
    <ul v-else>
      <li v-for="scan in scans" :key="scan.id">
        {{ scan.startedAt }} - {{ scan.status }}
        <RouterLink :to="`/scans/${scan.id}`">Details</RouterLink>
      </li>

    </ul>
  </div>
</template>
