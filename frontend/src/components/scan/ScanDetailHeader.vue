<script setup lang="ts">
import type { ScanDetailDTO } from '@/api/scan'

defineProps<{
  scan: ScanDetailDTO
}>()

const emit = defineEmits<{
  export: []
}>()

const statusLabel: Record<string, string> = {
  COMPLETED: 'Fertig',
  RUNNING: 'Läuft',
  FAILED: 'Fehler',
}
</script>

<template>
  <header class="flex justify-between items-start gap-6 flex-wrap bg-[var(--surface)] border border-[var(--border)] rounded-[16px] px-[1.6rem] py-6 shadow-[0_18px_44px_rgba(0,0,0,0.35)]">
    <div>
      <h1 class="text-[1.6rem] font-extrabold text-[var(--text)] m-0">Scan #{{ scan.projectScanNumber }}</h1>
      <p class="mt-[0.4rem] mb-0 text-[var(--muted)] text-[0.9rem]">
        Status:
        <span class="text-[var(--peach)] font-semibold">{{ statusLabel[scan.status] ?? scan.status }}</span>
        <span class="mx-2 text-[var(--border)]">·</span>
        {{ new Date(scan.startedAt).toLocaleString('de-AT') }}
      </p>
    </div>
    <button class="border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('export')">Export ⬇</button>
  </header>
</template>
