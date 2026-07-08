<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScan, getScanExport, type ScanDetailDTO } from '@/api/scan'
import { requestFix, getFixProposal, type FixProposalDTO } from '@/api/fixproposal'
import { createReview } from '@/api/review'
import ScanDetailHeader from '@/components/scan/ScanDetailHeader.vue'
import ViolationList from '@/components/violation/ViolationList.vue'

const scanId = Number(useRoute().params.id)
const scan = ref<ScanDetailDTO | null>(null)
const fixes = ref<Record<number, FixProposalDTO>>({})
const reviewed = ref<Record<number, 'ACCEPTED' | 'REJECTED'>>({})

let pollTimer: ReturnType<typeof setInterval> | null = null

function stopPolling() {
  if (pollTimer !== null) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

async function loadScan() {
  scan.value = await getScan(scanId)
  if (scan.value.status !== 'RUNNING') {
    stopPolling()
  }
}

onMounted(async () => {
  await loadScan()
  if (scan.value?.status === 'RUNNING') {
    pollTimer = setInterval(loadScan, 2000)
  }
})

onUnmounted(stopPolling)

async function handleRequestFix(violationId: number) {
  const { fixProposalId } = await requestFix(violationId)

  for (let i = 0; i < 30; i++) {
    const fix = await getFixProposal(fixProposalId)
    fixes.value[violationId] = fix
    if (fix.status !== 'PENDING') break
    await new Promise((r) => setTimeout(r, 2000))
  }
}

async function handleFixAll() {
  for (const v of scan.value?.violations ?? []) {
    await handleRequestFix(v.id)
  }
}

async function handleExport() {
  const data = await getScanExport(scanId)
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `scan-${scanId}-fixes.json`
  a.click()
  URL.revokeObjectURL(url)
}

async function handleReview(
  violationId: number,
  fixProposalId: number,
  decision: 'ACCEPTED' | 'REJECTED',
) {
  await createReview(fixProposalId, decision)
  reviewed.value[violationId] = decision
}
</script>

<template>
  <div v-if="!scan" class="relative max-w-[900px] mx-auto px-6 pt-8 pb-12 text-[var(--muted)] font-sans">Scan wird geladen…</div>

  <div v-else class="relative max-w-[900px] mx-auto px-6 pt-8 pb-12 text-[var(--text)] font-sans">
    <img src="/mole.png" alt="" aria-hidden="true" class="absolute top-[-8px] right-3 w-[64px] [image-rendering:pixelated] opacity-90 pointer-events-none" />

    <ScanDetailHeader :scan="scan" @export="handleExport" />

    <div v-if="scan.status === 'RUNNING'" class="flex items-center gap-3 mt-8 text-[var(--muted)]">
      <span class="w-5 h-5 rounded-full border-2 border-[var(--border)] border-t-[var(--peach)] animate-spin"></span>
      Scan läuft – Ergebnisse werden geladen…
    </div>

    <ViolationList
      v-else
      :violations="scan.violations"
      :fixes="fixes"
      :reviewed="reviewed"
      @fix-all="handleFixAll"
      @request-fix="handleRequestFix"
      @review="handleReview"
    />
  </div>
</template>
