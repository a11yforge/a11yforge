<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScan, getScanExport, type ScanDetailDTO } from '@/api/scan'
import { requestFix, getFixProposal, type FixProposalDTO } from '@/api/fixproposal'
import { createReview } from '@/api/review'

const scanId = Number(useRoute().params.id)
const scan = ref<ScanDetailDTO | null>(null)
const fixes = ref<Record<number, FixProposalDTO>>({})
onMounted(async () => {
  scan.value = await getScan(scanId)
})
const reviewed = ref<Record<number, 'ACCEPTED' | 'REJECTED'>>({})

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

const statusLabel: Record<string, string> = {
  COMPLETED: 'Fertig',
  RUNNING: 'Läuft',
  FAILED: 'Fehler',
}
</script>

<template>
  <div v-if="!scan" class="relative max-w-[900px] mx-auto px-6 pt-8 pb-12 text-[#a99cb0] font-sans">Scan wird geladen…</div>

  <div v-else class="relative max-w-[900px] mx-auto px-6 pt-8 pb-12 text-[#f3e9e2] font-sans">
    <img src="/mole.png" alt="" aria-hidden="true" class="absolute top-[-8px] right-3 w-[64px] [image-rendering:pixelated] opacity-90 pointer-events-none" />

    <header class="flex justify-between items-start gap-6 flex-wrap bg-[#1e1a29] border border-[#322840] rounded-[16px] px-[1.6rem] py-6 shadow-[0_18px_44px_rgba(0,0,0,0.35)]">
      <div>
        <h1 class="text-[1.6rem] font-extrabold text-[#f3e9e2] m-0">Scan #{{ scan.id }}</h1>
        <p class="mt-[0.4rem] mb-0 text-[#a99cb0] text-[0.9rem]">
          Status:
          <span class="text-[#f6c89a] font-semibold">{{ statusLabel[scan.status] ?? scan.status }}</span>
          <span class="mx-2 text-[#322840]">·</span>
          {{ new Date(scan.startedAt).toLocaleString('de-AT') }}
        </p>
      </div>
      <button class="border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-[#ff7a52] text-[#2a1410] hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button" @click="handleExport">Export ⬇</button>
    </header>

    <div class="flex items-center justify-between my-8 mb-4">
      <h2 class="text-[1.2rem] font-bold text-[#f3e9e2] m-0">Befunde</h2>
      <button class="border border-[#322840] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[#f3e9e2] hover:border-[#ff7a52] hover:text-[#ff7a52] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button" @click="handleFixAll">⚡ Alle mit KI fixen</button>
    </div>

    <div class="flex flex-col gap-[0.9rem]">
      <article v-for="v in scan.violations" :key="v.id" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.2rem] py-[1.1rem]">
        <div class="flex justify-between gap-4 flex-wrap">
          <div>
            <span class="font-bold text-[#f3e9e2]">{{ v.ruleId }}</span>
            <span
              class="rounded-full py-[0.2rem] px-[0.6rem] text-[0.72rem] font-bold ml-2"
              :class="v.impact === 'CRITICAL' ? 'bg-[rgba(255,122,82,0.15)] text-[#ff7a52]' : 'bg-[rgba(246,200,154,0.15)] text-[#f6c89a]'"
            >
              {{ v.impact }}
            </span>
            <p class="mt-[0.4rem] mb-0 text-[#a99cb0] text-[0.9rem]">{{ v.description }}</p>
          </div>
          <div class="flex gap-2 flex-wrap h-fit">
            <button
              class="border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-[#ff7a52] text-[#2a1410] disabled:opacity-50 disabled:cursor-not-allowed hover:enabled:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]"
              type="button"
              :disabled="fixes[v.id]?.status === 'PENDING'"
              @click="handleRequestFix(v.id)"
            >
              🤖 Mit KI fixen
            </button>
            <button class="border border-[#322840] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[#f3e9e2] hover:border-[#ff7a52] hover:text-[#ff7a52] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button">✋ Selbst fixen</button>
          </div>
        </div>

        <pre class="mt-[0.8rem] mb-0 bg-[#14111c] border border-[#322840] rounded-lg py-[0.7rem] px-[0.8rem] font-mono text-[0.82rem] text-[#a99cb0] overflow-x-auto whitespace-pre">{{ v.htmlSnippet }}</pre>

        <div v-if="fixes[v.id]" class="mt-4 border-t border-[#322840] pt-4">
          <p v-if="fixes[v.id]?.status === 'PENDING'" class="text-[#a99cb0] text-[0.9rem] m-0">
            ⏳ Wird generiert &amp; verifiziert…
          </p>

          <template v-else>
            <div class="flex items-center gap-[0.6rem] mb-[0.6rem] text-[0.85rem]">
              <span
                class="rounded-full py-[0.2rem] px-[0.6rem] text-[0.72rem] font-bold"
                :class="fixes[v.id]?.status === 'VERIFIED' ? 'bg-[rgba(91,190,178,0.15)] text-[#5bbeb2]' : 'bg-[rgba(255,122,82,0.15)] text-[#ff7a52]'"
              >
                {{ fixes[v.id]?.status }}
              </span>
              <span class="text-[#a99cb0]">
                {{ fixes[v.id]?.llmProvider }} / {{ fixes[v.id]?.llmModel }}
              </span>
            </div>

            <div class="grid gap-[0.8rem] grid-cols-1 min-[720px]:grid-cols-2">
              <div>
                <p class="mt-0 mb-[0.3rem] text-[0.72rem] uppercase tracking-[0.4px] text-[#a99cb0]">Vorher</p>
                <pre class="mt-[0.8rem] mb-0 bg-[#14111c] border border-[#322840] rounded-lg py-[0.7rem] px-[0.8rem] font-mono text-[0.82rem] text-[#a99cb0] overflow-x-auto whitespace-pre">{{ v.htmlSnippet }}</pre>
              </div>
              <div v-if="fixes[v.id]?.generatedHtml">
                <p class="mt-0 mb-[0.3rem] text-[0.72rem] uppercase tracking-[0.4px] text-[#a99cb0]">Nachher</p>
                <pre class="mt-[0.8rem] mb-0 bg-[#14111c] border border-[#322840] rounded-lg py-[0.7rem] px-[0.8rem] font-mono text-[0.82rem] text-[#5bbeb2] overflow-x-auto whitespace-pre">{{ fixes[v.id]?.generatedHtml }}</pre>
              </div>
            </div>

            <div v-if="fixes[v.id]?.status === 'VERIFIED'" class="flex gap-2 mt-[0.8rem]">
              <button
                class="border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-[#5bbeb2] text-[#0d201d] hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]"
                type="button"
                @click="handleReview(v.id, fixes[v.id]!.id, 'ACCEPTED')"
              >
                ✅ Annehmen
              </button>
              <button
                class="border border-[#5a2a2a] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[#f6a3a3] hover:bg-[#3a1a1a] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]"
                type="button"
                @click="handleReview(v.id, fixes[v.id]!.id, 'REJECTED')"
              >
                ❌ Ablehnen
              </button>
            </div>
          </template>
        </div>
      </article>
    </div>
  </div>
</template>
