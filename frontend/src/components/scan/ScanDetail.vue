<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScan, type ScanDetailDTO } from '@/api/scan'

const scanId = Number(useRoute().params.id)
const scan = ref<ScanDetailDTO | null>(null)
onMounted(async () => {
  scan.value = await getScan(scanId)
})
</script>

<template>
  <div v-if="!scan">
    <p>Loading</p>
  </div>
  <div v-else>
    {{ scan.status }}
    {{ scan.startedAt }}

    <div v-for="v in scan?.violations" :key="v.id">
      {{ v.ruleId }} – {{ v.impact }} – {{ v.description }}
    </div>
  </div>
</template>