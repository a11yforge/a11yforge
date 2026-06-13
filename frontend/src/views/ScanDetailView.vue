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
  <div v-if="!scan" class="page state">Scan wird geladen…</div>

  <div v-else class="page">
    <img src="/mole.png" alt="" aria-hidden="true" class="mascot" />

    <!-- Kopf -->
    <header class="head">
      <div>
        <h1 class="head__title">Scan #{{ scan.id }}</h1>
        <p class="head__sub">
          Status:
          <span class="status">{{ statusLabel[scan.status] ?? scan.status }}</span>
          <span class="dot">·</span>
          {{ new Date(scan.startedAt).toLocaleString('de-AT') }}
        </p>
      </div>
      <button class="btn btn--primary" type="button" @click="handleExport">Export ⬇</button>
    </header>

    <!-- Befunde -->
    <div class="befunde__head">
      <h2 class="h2">Befunde</h2>
      <!-- Platzhalter: Bulk-Fix (Backend-Endpoint kommt später) -->
      <button class="btn btn--ghost" type="button">⚡ Alle mit KI fixen</button>
    </div>

    <div class="list">
      <article v-for="v in scan.violations" :key="v.id" class="vcard">
        <div class="vcard__head">
          <div>
            <span class="rule">{{ v.ruleId }}</span>
            <span class="badge" :class="v.impact === 'CRITICAL' ? 'badge--crit' : 'badge--warn'">
              {{ v.impact }}
            </span>
            <p class="desc">{{ v.description }}</p>
          </div>
          <div class="vcard__actions">
            <button
              class="btn btn--primary"
              type="button"
              :disabled="fixes[v.id]?.status === 'PENDING'"
              @click="handleRequestFix(v.id)"
            >
              🤖 Mit KI fixen
            </button>
            <!-- Platzhalter: manueller Fix (MANUAL-Status kommt später) -->
            <button class="btn btn--ghost" type="button">✋ Selbst fixen</button>
          </div>
        </div>

        <pre class="snippet">{{ v.htmlSnippet }}</pre>

        <!-- Fix-Vorschlag -->
        <div v-if="fixes[v.id]" class="fix">
          <p v-if="fixes[v.id]?.status === 'PENDING'" class="fix__pending">
            ⏳ Wird generiert &amp; verifiziert…
          </p>

          <template v-else>
            <div class="fix__status">
              <span
                class="badge"
                :class="fixes[v.id]?.status === 'VERIFIED' ? 'badge--ok' : 'badge--crit'"
              >
                {{ fixes[v.id]?.status }}
              </span>
              <span class="muted">
                {{ fixes[v.id]?.llmProvider }} / {{ fixes[v.id]?.llmModel }}
              </span>
            </div>

            <div class="diff">
              <div>
                <p class="diff__label">Vorher</p>
                <pre class="snippet">{{ v.htmlSnippet }}</pre>
              </div>
              <div v-if="fixes[v.id]?.generatedHtml">
                <p class="diff__label">Nachher</p>
                <pre class="snippet snippet--ok">{{ fixes[v.id]?.generatedHtml }}</pre>
              </div>
            </div>

            <div v-if="fixes[v.id]?.status === 'VERIFIED'" class="fix__review">
              <button
                class="btn btn--ok"
                type="button"
                @click="handleReview(v.id, fixes[v.id]!.id, 'ACCEPTED')"
              >
                ✅ Annehmen
              </button>
              <button
                class="btn btn--danger"
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

<style scoped>
.page {
  position: relative;
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.state {
  color: var(--muted);
}

.mascot {
  position: absolute;
  top: -8px;
  right: 12px;
  width: 64px;
  image-rendering: pixelated;
  opacity: 0.9;
  pointer-events: none;
}

.head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1.5rem;
  flex-wrap: wrap;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 1.5rem 1.6rem;
  box-shadow: 0 18px 44px rgba(0, 0, 0, 0.35);
}
.head__title {
  font-size: 1.6rem;
  font-weight: 800;
  color: var(--text);
  margin: 0;
}
.head__sub {
  margin: 0.4rem 0 0;
  color: var(--muted);
  font-size: 0.9rem;
}
.status {
  color: var(--peach);
  font-weight: 600;
}
.dot {
  margin: 0 0.5rem;
  color: var(--border);
}

.befunde__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 2rem 0 1rem;
}
.h2 {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--text);
  margin: 0;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 0.9rem;
}
.vcard {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.1rem 1.2rem;
}
.vcard__head {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  flex-wrap: wrap;
}
.vcard__actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
  height: fit-content;
}
.rule {
  font-weight: 700;
  color: var(--text);
}
.desc {
  margin: 0.4rem 0 0;
  color: var(--muted);
  font-size: 0.9rem;
}

.snippet {
  margin: 0.8rem 0 0;
  background: var(--field);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 0.7rem 0.8rem;
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 0.82rem;
  color: var(--muted);
  overflow-x: auto;
  white-space: pre;
}
.snippet--ok {
  color: var(--teal);
}

.fix {
  margin-top: 1rem;
  border-top: 1px solid var(--border);
  padding-top: 1rem;
}
.fix__pending {
  color: var(--muted);
  font-size: 0.9rem;
  margin: 0;
}
.fix__status {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-bottom: 0.6rem;
  font-size: 0.85rem;
}
.diff {
  display: grid;
  gap: 0.8rem;
}
@media (min-width: 720px) {
  .diff {
    grid-template-columns: 1fr 1fr;
  }
}
.diff__label {
  margin: 0 0 0.3rem;
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.4px;
  color: var(--muted);
}
.fix__review {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.8rem;
}
.muted {
  color: var(--muted);
}

.btn {
  border: 1px solid transparent;
  border-radius: 10px;
  padding: 0.55rem 1rem;
  font-weight: 600;
  font-size: 0.88rem;
  cursor: pointer;
}
.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
.btn--primary {
  background: var(--coral);
  color: #2a1410;
}
.btn--primary:hover:not(:disabled) {
  filter: brightness(1.07);
}
.btn--ghost {
  background: transparent;
  color: var(--text);
  border-color: var(--border);
}
.btn--ghost:hover {
  border-color: var(--coral);
  color: var(--coral);
}
.btn--ok {
  background: var(--teal);
  color: #0d201d;
}
.btn--ok:hover {
  filter: brightness(1.07);
}
.btn--danger {
  background: transparent;
  color: #f6a3a3;
  border-color: #5a2a2a;
}
.btn--danger:hover {
  background: #3a1a1a;
}

.badge {
  border-radius: 999px;
  padding: 0.2rem 0.6rem;
  font-size: 0.72rem;
  font-weight: 700;
  margin-left: 0.5rem;
}
.badge--crit {
  background: rgba(255, 122, 82, 0.15);
  color: var(--coral);
}
.badge--warn {
  background: rgba(246, 200, 154, 0.15);
  color: var(--peach);
}
.badge--ok {
  background: rgba(91, 190, 178, 0.15);
  color: var(--teal);
  margin-left: 0;
}
</style>
