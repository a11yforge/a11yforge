<script setup lang="ts">
// ⚠️ DESIGN-PHASE: alles hier ist PLATZHALTER und wird in der Logik-Phase
// durch echte Daten (getProjectById / getScanFromProject) ersetzt.
// Layout folgt dem Wireframe docs/frontend-design/Project-Detail.canvas.
import { ref } from 'vue'

// Platzhalter-Flags zum Durchschalten der Zustände:
const loading = ref(false)
const hasScans = ref(true)

const project = {
  name: 'Beispiel-Projekt',
  baseUrl: 'https://example.com',
  crawlMaxPages: 10,
}

// Form ~ ScanResponseDTO  (⚠️ violationCount ist NICHT im DTO -> Platzhalter)
const scans = [
  { id: 12, status: 'COMPLETED', startedAt: '2026-06-06T12:00:00Z', violationCount: 12 },
  { id: 9, status: 'RUNNING', startedAt: '2026-06-05T10:00:00Z', violationCount: 20 },
  { id: 7, status: 'FAILED', startedAt: '2026-06-05T09:00:00Z', violationCount: 0 },
]

const statusMeta: Record<string, { icon: string; label: string; cls: string }> = {
  COMPLETED: { icon: '✅', label: 'Fertig', cls: 'completed' },
  RUNNING: { icon: '⏳', label: 'Läuft', cls: 'running' },
  FAILED: { icon: '❌', label: 'Fehler', cls: 'failed' },
}
function meta(status: string) {
  return statusMeta[status] ?? { icon: '•', label: status, cls: 'running' }
}
function fmt(iso: string) {
  return new Date(iso).toLocaleDateString('de-AT')
}
</script>

<template>
  <div class="page">
    <router-link to="/projects" class="back">← Zurück zu Projekte</router-link>

    <!-- Projekt-Kopf -->
    <header class="head">
      <div class="head__info">
        <h1 class="head__title">{{ project.name }}</h1>
        <p class="head__sub">
          URL:
          <a :href="project.baseUrl" target="_blank" rel="noopener" class="head__url">
            {{ project.baseUrl }} ↗
          </a>
          <span class="dot">·</span>
          Max. Seiten: {{ project.crawlMaxPages }}
        </p>
      </div>
      <div class="head__actions">
        <!-- öffnet später den Scan-Dialog mit LLM-Provider-Auswahl -->
        <button class="btn btn--primary" type="button">Scannen ▶</button>
        <button class="btn btn--ghost" type="button">✏ Bearbeiten</button>
        <button class="btn btn--danger" type="button">🗑 Löschen</button>
      </div>
    </header>

    <!-- Letzte Scans -->
    <section class="scans">
      <div class="scans__head">
        <h2 class="scans__title">Letzte Scans</h2>
        <router-link
          v-if="hasScans && !loading"
          :to="`/projects/1/scans`"
          class="scans__all"
        >
          Alle Scans ansehen →
        </router-link>
      </div>

      <!-- Loading -->
      <div v-if="loading" class="state">Scans werden geladen…</div>

      <!-- Empty -->
      <div v-else-if="!hasScans" class="state state--empty">
        <img src="/mole.png" alt="" aria-hidden="true" class="state__mole" />
        <p>Noch kein Scan — starte den ersten.</p>
        <button class="btn btn--primary" type="button">Scannen ▶</button>
      </div>

      <!-- List -->
      <ul v-else class="list">
        <li v-for="scan in scans" :key="scan.id" class="row">
          <span class="row__id">Scan #{{ scan.id }}</span>
          <span class="badge" :class="`badge--${meta(scan.status).cls}`">
            {{ meta(scan.status).icon }} {{ meta(scan.status).label }}
          </span>
          <span class="row__date">{{ fmt(scan.startedAt) }}</span>
          <span class="row__count">{{ scan.violationCount }} Befunde</span>
          <router-link :to="`/scans/${scan.id}`" class="row__open">Öffnen</router-link>
        </li>
      </ul>
    </section>
  </div>
</template>

<style scoped>
.page {
  --surface: #1e1a29;
  --field: #14111c;
  --text: #f3e9e2;
  --muted: #a99cb0;
  --coral: #ff7a52;
  --peach: #f6c89a;
  --teal: #5bbeb2;
  --border: #322840;

  max-width: 880px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
}

.back {
  display: inline-block;
  margin-bottom: 1.4rem;
  color: var(--muted);
  text-decoration: none;
  font-size: 0.9rem;
}
.back:hover {
  color: var(--coral);
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
  padding: 1.6rem 1.7rem;
  box-shadow: 0 18px 44px rgba(0, 0, 0, 0.35);
}
.head__title {
  font-size: 1.7rem;
  font-weight: 800;
  color: var(--text);
  margin: 0;
}
.head__sub {
  margin: 0.5rem 0 0;
  color: var(--muted);
  font-size: 0.92rem;
}
.head__url {
  color: var(--teal);
  text-decoration: none;
}
.head__url:hover {
  text-decoration: underline;
}
.dot {
  margin: 0 0.5rem;
  color: var(--border);
}
.head__actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.btn {
  border: 1px solid transparent;
  border-radius: 10px;
  padding: 0.6rem 1rem;
  font-weight: 600;
  font-size: 0.9rem;
  cursor: pointer;
}
.btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
.btn--primary {
  background: var(--coral);
  color: #2a1410;
}
.btn--primary:hover {
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
.btn--danger {
  background: transparent;
  color: #f6a3a3;
  border-color: #5a2a2a;
}
.btn--danger:hover {
  background: #3a1a1a;
}

.scans {
  margin-top: 2rem;
}
.scans__head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 1rem;
}
.scans__title {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--text);
  margin: 0;
}
.scans__all {
  color: var(--coral);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 600;
}
.scans__all:hover {
  text-decoration: underline;
}

.state {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 2rem;
  text-align: center;
  color: var(--muted);
}
.state--empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.9rem;
}
.state__mole {
  width: 72px;
  image-rendering: pixelated;
  opacity: 0.85;
}

.list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}
.row {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 0.85rem 1.1rem;
}
.row__id {
  font-weight: 700;
  min-width: 5.5rem;
}
.row__date {
  color: var(--muted);
  font-size: 0.9rem;
}
.row__count {
  color: var(--muted);
  font-size: 0.9rem;
  margin-left: auto;
}
.row__open {
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 9px;
  padding: 0.4rem 0.9rem;
  color: var(--text);
  text-decoration: none;
  font-size: 0.88rem;
  font-weight: 600;
}
.row__open:hover {
  border-color: var(--coral);
  color: var(--coral);
}

.badge {
  border-radius: 999px;
  padding: 0.2rem 0.65rem;
  font-size: 0.74rem;
  font-weight: 700;
  white-space: nowrap;
}
.badge--completed {
  background: rgba(91, 190, 178, 0.15);
  color: var(--teal);
}
.badge--running {
  background: rgba(246, 200, 154, 0.15);
  color: var(--peach);
}
.badge--failed {
  background: rgba(255, 122, 82, 0.15);
  color: var(--coral);
}
</style>
