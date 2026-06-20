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
  <li class="flex items-center gap-4 bg-[var(--surface)] border border-[var(--border)] rounded-[12px] py-[0.85rem] px-[1.1rem]">
    <span class="font-bold min-w-[5.5rem]">Scan #{{ scan.id }}</span>
    <ScanStatusBadge :status="scan.status" />
    <span class="text-[var(--muted)] text-[0.9rem]">{{ startedAtFormatted }}</span>
    <span class="text-[var(--muted)] text-[0.9rem] ml-auto">{{ scan.violationCount }} Befunde</span>
    <router-link
      :to="`/scans/${scan.id}`"
      class="bg-transparent border border-[var(--border)] rounded-[9px] py-[0.4rem] px-[0.9rem] text-[var(--text)] no-underline text-[0.88rem] font-semibold hover:border-[var(--coral)] hover:text-[var(--coral)]"
    >
      Öffnen
    </router-link>
  </li>
</template>