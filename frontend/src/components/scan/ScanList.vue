<script setup lang="ts">
import ScanListItem from './ScanListItem.vue'
import type { ScanResponseDTO } from '@/api/scan'

defineProps<{
  scans: ScanResponseDTO[]
  loading: boolean
  error: string | null
  emptyMessage?: string
}>()

const emit = defineEmits<{
  scan: []
}>()
</script>

<template>
  <div>
    <div v-if="loading" class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] p-8 text-center text-[var(--muted)]">Scans werden geladen…</div>

    <div v-else-if="error" class="bg-[var(--surface)] border border-[var(--error-border)] rounded-[14px] p-8 text-center text-[var(--error-text)]">{{ error }}</div>

    <div v-else-if="scans.length === 0" class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] p-8 text-center text-[var(--muted)] flex flex-col items-center gap-[0.9rem]">
      <img src="/mole.png" alt="" aria-hidden="true" class="w-[72px] [image-rendering:pixelated] opacity-85" />
      <p>{{ emptyMessage ?? 'Noch kein Scan — starte den ersten.' }}</p>
      <button class="border border-transparent rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('scan')">
        Scannen
      </button>
    </div>

    <ul v-else class="list-none m-0 p-0 flex flex-col gap-[0.6rem]">
      <ScanListItem v-for="scan in scans" :key="scan.id" :scan="scan" />
    </ul>
  </div>
</template>