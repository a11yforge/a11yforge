<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScan, getScanExport,type ScanDetailDTO } from '@/api/scan'
import {requestFix, getFixProposal, type FixProposalDTO} from '@/api/fixproposal'
import {createReview} from "@/api/review"


const scanId = Number(useRoute().params.id)
const scan = ref<ScanDetailDTO | null>(null)
const fixes = ref<Record<number, FixProposalDTO>>({})
onMounted(async () => {
  scan.value = await getScan(scanId)
})
const reviewed = ref<Record<number, "ACCEPTED" | "REJECTED">>({})

async function handleRequestFix(violationId: number) {
  const { fixProposalId } = await requestFix(violationId)

  for (let i = 0; i < 30; i++) {
    const fix = await getFixProposal(fixProposalId)
    fixes.value[violationId] = fix
    if (fix.status !== 'PENDING') break
    await new Promise((r) => setTimeout(r, 2000))
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



async function handleReview(violationId: number, fixProposalId: number, decision: "ACCEPTED" | "REJECTED") {
  await createReview(fixProposalId, decision)
  reviewed.value[violationId] = decision
}
</script>

<template>


  <div v-if="!scan" class="p-6 text-gray-400">
    <p>Loading…</p>
  </div>

  <div v-else class="max-w-4xl mx-auto p-6">
    <header class="mb-6 flex items-start justify-between">
      <div>
        <h1 class="text-2xl font-bold">Scan-Ergebnis</h1>
        <p class="text-sm text-gray-400">
          Status: {{ scan.status }} · {{ new Date(scan.startedAt).toLocaleString('de-AT') }}
        </p>
      </div>
      <button
        class="rounded bg-emerald-600 px-3 py-1 text-sm text-white hover:bg-emerald-500"
        @click="handleExport"
      >
        Export JSON
      </button>
    </header>

    <div class="flex flex-col gap-4">
      <div
        v-for="v in scan?.violations"
        :key="v.id"
        class="rounded-lg border border-gray-700 p-4"
      >

        <div class="flex items-start justify-between gap-3">
          <div>
            <span class="font-semibold">{{ v.ruleId }}</span>
            <span
              class="ml-2 rounded px-2 py-0.5 text-xs font-medium"
              :class="v.impact === 'CRITICAL' ? 'bg-red-900 text-red-200' : 'bg-amber-900 text-amber-200'"
            >
              {{ v.impact }}
            </span>
            <p class="mt-1 text-sm text-gray-400">{{ v.description }}</p>
          </div>
          <button
            class="shrink-0 rounded bg-emerald-600 px-3 py-1 text-sm text-white hover:bg-emerald-500 disabled:opacity-50"
            :disabled="fixes[v.id]?.status === 'PENDING'"
            @click="handleRequestFix(v.id)"
          >
            Fix generieren
          </button>
        </div>

        <div v-if="fixes[v.id]" class="mt-3 border-t border-gray-700 pt-3">
          <p v-if="fixes[v.id]?.status === 'PENDING'" class="text-sm text-gray-400">
            ⏳ Wird generiert & verifiziert…
          </p>

          <template v-else>
            <div class="mb-2 flex items-center gap-2 text-sm">
              <span
                class="rounded px-2 py-0.5 font-medium"
                :class="fixes[v.id]?.status === 'VERIFIED' ? 'bg-emerald-900 text-emerald-200' : 'bg-red-900 text-red-200'"
              >
                {{ fixes[v.id]?.status }}
              </span>
              <span class="text-gray-500">
                {{ fixes[v.id]?.llmProvider }} / {{ fixes[v.id]?.llmModel }}
              </span>
            </div>

            <div class="grid gap-3 md:grid-cols-2">
              <div>
                <p class="mb-1 text-xs uppercase tracking-wide text-gray-500">Vorher</p>
                <pre class="overflow-x-auto rounded bg-gray-900 p-2 text-xs text-gray-300">{{ v.htmlSnippet }}</pre>
              </div>
              <div v-if="fixes[v.id]?.generatedHtml">
                <p class="mb-1 text-xs uppercase tracking-wide text-gray-500">Nachher</p>
                <pre class="overflow-x-auto rounded bg-gray-900 p-2 text-xs text-emerald-300">{{ fixes[v.id]?.generatedHtml }}</pre>
              </div>
            </div>
            <div v-if="fixes[v.id]?.status === 'VERIFIED'" class="mt-2 flex gap-2">
              <button
                class="rounded bg-emerald-700 px-3 py-1 text-sm text-white hover:bg-emerald-600"
                @click="handleReview(v.id, fixes[v.id]!.id, 'ACCEPTED')"
              >
                Akzeptieren
              </button>
              <button
                class="rounded bg-red-800 px-3 py-1 text-sm text-white hover:bg-red-700"
                @click="handleReview(v.id, fixes[v.id]!.id, 'REJECTED')"
              >
                Ablehnen
              </button>
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
