<script setup lang="ts">
import ScanStatusBadge from './ScanStatusBadge.vue'
import type { ScanResponseDTO } from '@/api/scan'

defineProps<{
  scans: ScanResponseDTO[]
  loading: boolean
  error: string | null
  emptyMessage?: string
}>()

function fmt(iso: string | null) {
  return iso ? new Date(iso).toLocaleString('de-AT') : '–'
}
</script>

<template>
  <div>
    <div v-if="loading" class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] p-8 text-center text-[var(--muted)]">Lädt…</div>
    <div v-else-if="error" class="bg-[var(--surface)] border border-[var(--error-border)] rounded-[14px] p-8 text-center text-[var(--error-text)]">{{ error }}</div>
    <div v-else-if="scans.length === 0" class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] p-8 text-center text-[var(--muted)]">{{ emptyMessage ?? 'Noch keine Scans für dieses Projekt.' }}</div>

    <div v-else class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] overflow-x-auto">
      <table class="w-full border-collapse text-[0.9rem]">
        <thead>
          <tr>
            <th class="text-left text-[var(--muted)] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[var(--border)]">#</th>
            <th class="text-left text-[var(--muted)] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[var(--border)]">Status</th>
            <th class="text-left text-[var(--muted)] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[var(--border)]">Gestartet</th>
            <th class="text-left text-[var(--muted)] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[var(--border)]">Abgeschlossen</th>
            <th class="text-left text-[var(--muted)] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[var(--border)]">Befunde</th>
            <th class="text-left text-[var(--muted)] font-semibold text-[0.78rem] uppercase tracking-[0.4px] py-[0.8rem] px-4 border-b border-[var(--border)]"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="scan in scans" :key="scan.id" class="group">
            <td class="py-[0.8rem] px-4 border-b border-[var(--border)] text-[var(--text)] group-last:border-b-0 font-mono font-bold">#{{ scan.projectScanNumber }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[var(--border)] text-[var(--text)] group-last:border-b-0">
              <ScanStatusBadge :status="scan.status" />
            </td>
            <td class="py-[0.8rem] px-4 border-b border-[var(--border)] text-[var(--muted)] group-last:border-b-0">{{ fmt(scan.startedAt) }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[var(--border)] text-[var(--muted)] group-last:border-b-0">{{ fmt(scan.completedAt) }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[var(--border)] text-[var(--muted)] group-last:border-b-0">{{ scan.violationCount }}</td>
            <td class="py-[0.8rem] px-4 border-b border-[var(--border)] text-[var(--text)] group-last:border-b-0"><router-link :to="`/scans/${scan.id}`" class="text-[var(--coral)] no-underline font-semibold hover:underline">Öffnen</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
