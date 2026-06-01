<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScan, type ScanDetailDTO } from '@/api/scan'
import {requestFix, getFixProposal, type FixProposalDTO} from '@/api/fixproposal'


const scanId = Number(useRoute().params.id)
const scan = ref<ScanDetailDTO | null>(null)
const fixes = ref<Record<number, FixProposalDTO>>({})
onMounted(async () => {
  scan.value = await getScan(scanId)
})

async function handleRequestFix(violationId: number) {
  const { fixProposalId } = await requestFix(violationId)

  for (let i = 0; i < 30; i++) {
    const fix = await getFixProposal(fixProposalId)
    fixes.value[violationId] = fix
    if (fix.status !== 'PENDING') break
    await new Promise((r) => setTimeout(r, 2000))
  }
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
      <div v-if="fixes[v.id]">
        Status: {{ fixes[v.id].status }}
        <pre v-if="fixes[v.id].generatedHtml">{{ fixes[v.id].generatedHtml }}</pre>
    </div>
  </div>
  </div>
</template>
