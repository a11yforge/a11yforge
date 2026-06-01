<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScan, type ScanDetailDTO } from '@/api/scan'
import {requestFix} from '@/api/fixproposal'

const scanId = Number(useRoute().params.id)
const scan = ref<ScanDetailDTO | null>(null)
onMounted(async () => {
  scan.value = await getScan(scanId)
})

async function handleRequestFix(violationId: number){
  const result = await requestFix(violationId)
  console.log("test: ", result.fixProposalId)
}
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
      <button @click="handleRequestFix(v.id)">Fix generieren</button>
    </div>
  </div>
</template>
