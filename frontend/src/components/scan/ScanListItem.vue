<script setup lang="ts">
import { computed } from 'vue'
import ScanStatusBadge from './ScanStatusBadge.vue'
import type { ScanResponseDTO } from '@/api/scan'

const props = defineProps<{
  scan: ScanResponseDTO
}>()

const startedAtFormatted = computed(() =>
  new Date(props.scan.startedAt).toLocaleDateString('de-AT'),
)
</script>

<template>
  <li class="flex flex-col gap-2 sm:flex-row sm:items-center sm:gap-4 bg-[var(--surface)] border border-[var(--border)] rounded-[12px] py-[0.85rem] px-[1.1rem]">
    <div class="flex items-center gap-3 sm:contents">
      <span class="font-bold whitespace-nowrap min-w-[5.5rem]">Scan #{{ scan.projectScanNumber }}</span>
      <ScanStatusBadge :status="scan.status" />
    </div>

    <div class="flex items-center gap-3 sm:contents">
      <span class="text-[var(--muted)] text-[0.9rem] whitespace-nowrap">{{ startedAtFormatted }}</span>
      <span class="text-[var(--muted)] text-[0.9rem] whitespace-nowrap sm:ml-auto">{{ scan.violationCount }} Befunde</span>
      <router-link
        :to="`/scans/${scan.id}`"
        class="ml-auto sm:ml-0 shrink-0 bg-transparent border border-[var(--border)] rounded-[9px] py-[0.4rem] px-[0.9rem] text-[var(--text)] no-underline text-[0.88rem] font-semibold hover:border-[var(--coral)] hover:text-[var(--coral)]"
      >
        Öffnen
      </router-link>
    </div>
  </li>
</template>
