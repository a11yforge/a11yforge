<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getScanFromProject, type ScanResponseDTO } from '../api/scan'

const route = useRoute()

const loading = ref(true)
const error = ref<string | null>(null)
const projectId = Number(route.params.id)
const scans = ref<ScanResponseDTO[]>([])

onMounted(async () => {
  try {
    scans.value = await getScanFromProject(projectId)
  } catch {
    error.value = 'Scan konnte nicht geladen werden!'
  } finally {
    loading.value = false
  }
})

const statusMeta: Record<string, { icon: string; label: string; cls: string }> = {
  COMPLETED: { icon: '✅', label: 'Fertig', cls: 'completed' },
  RUNNING: { icon: '⏳', label: 'Läuft', cls: 'running' },
  FAILED: { icon: '❌', label: 'Fehler', cls: 'failed' },
}
function meta(status: string) {
  return statusMeta[status] ?? { icon: '•', label: status, cls: 'running' }
}
function fmt(iso: string | null) {
  return iso ? new Date(iso).toLocaleString('de-AT') : '–'
}
</script>

<template>
  <div class="page">
    <router-link :to="`/projects/${projectId}`" class="back">← Zurück zum Projekt</router-link>

    <h1 class="title">Scan-Historie</h1>

    <!-- Platzhalter: Filter/Sortierung (Logik-Phase) -->
    <div class="toolbar">
      <label class="ctrl">Filter:
        <select class="select"><option>Status ▾</option></select>
      </label>
      <label class="ctrl">Sortierung:
        <select class="select"><option>Neueste zuerst ▾</option></select>
      </label>
    </div>

    <div v-if="loading" class="state">Lädt…</div>
    <div v-else-if="error" class="state state--error">{{ error }}</div>
    <div v-else-if="scans.length === 0" class="state">Noch keine Scans für dieses Projekt.</div>

    <div v-else class="tablewrap">
      <table class="table">
        <thead>
          <tr>
            <th>#</th>
            <th>Status</th>
            <th>Gestartet</th>
            <th>Abgeschlossen</th>
            <th>Befunde</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="scan in scans" :key="scan.id">
            <td class="mono">#{{ scan.id }}</td>
            <td>
              <span class="badge" :class="`badge--${meta(scan.status).cls}`">
                {{ meta(scan.status).icon }} {{ meta(scan.status).label }}
              </span>
            </td>
            <td class="muted">{{ fmt(scan.startedAt) }}</td>
            <td class="muted">{{ fmt(scan.completedAt) }}</td>
            <!-- violationCount ist NICHT im ScanResponseDTO -> Platzhalter -->
            <td class="muted">–</td>
            <td><router-link :to="`/scans/${scan.id}`" class="open">Öffnen</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Platzhalter: Trend-Chart (Logik-Phase / F-09) -->
    <div class="chart">📈 Trend-Chart: Befunde über Zeit <span class="muted">(kommt später)</span></div>

    <!-- Platzhalter: Pagination -->
    <div class="pager">
      <button class="pager__btn" type="button">‹</button>
      <span class="muted">Seite 1 / 1</span>
      <button class="pager__btn" type="button">›</button>
    </div>
  </div>
</template>

<style scoped>
.page {
  max-width: 920px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.back {
  display: inline-block;
  margin-bottom: 1.2rem;
  color: var(--muted);
  text-decoration: none;
  font-size: 0.9rem;
}
.back:hover {
  color: var(--coral);
}
.title {
  font-size: 1.7rem;
  font-weight: 800;
  color: var(--text);
  margin: 0 0 1.2rem;
}

.toolbar {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
  margin-bottom: 1.2rem;
}
.ctrl {
  color: var(--muted);
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.select {
  background: var(--field);
  border: 1px solid var(--border);
  border-radius: 8px;
  color: var(--text);
  padding: 0.4rem 0.6rem;
}

.state {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 2rem;
  text-align: center;
  color: var(--muted);
}
.state--error {
  color: #f6a3a3;
  border-color: #5a2a2a;
}

.tablewrap {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  overflow-x: auto;
}
.table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}
.table th {
  text-align: left;
  color: var(--muted);
  font-weight: 600;
  font-size: 0.78rem;
  text-transform: uppercase;
  letter-spacing: 0.4px;
  padding: 0.8rem 1rem;
  border-bottom: 1px solid var(--border);
}
.table td {
  padding: 0.8rem 1rem;
  border-bottom: 1px solid var(--border);
  color: var(--text);
}
.table tr:last-child td {
  border-bottom: 0;
}
.mono {
  font-family: ui-monospace, monospace;
  font-weight: 700;
}
.muted {
  color: var(--muted);
}
.open {
  color: var(--coral);
  text-decoration: none;
  font-weight: 600;
}
.open:hover {
  text-decoration: underline;
}

.badge {
  border-radius: 999px;
  padding: 0.2rem 0.6rem;
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

.chart {
  margin-top: 1.5rem;
  background: var(--surface);
  border: 1px dashed var(--border);
  border-radius: 14px;
  padding: 2rem;
  text-align: center;
  color: var(--text);
}

.pager {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-top: 1.5rem;
}
.pager__btn {
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 8px;
  color: var(--text);
  width: 2.2rem;
  height: 2.2rem;
  cursor: pointer;
}
.pager__btn:hover {
  border-color: var(--coral);
  color: var(--coral);
}
</style>
