<script setup lang="ts">
import { computed } from 'vue'
import Chart from 'primevue/chart'
import type { ScanResponseDTO } from '@/api/scan'

const props = defineProps<{
  scans: ScanResponseDTO[]
}>()

function token(name: string): string {
  return getComputedStyle(document.documentElement).getPropertyValue(name).trim()
}

const mutedColor = token('--muted')
const borderColor = token('--border')

const chartData = computed(() => {
  const chrono = [...props.scans].sort(
    (a, b) => new Date(a.startedAt).getTime() - new Date(b.startedAt).getTime(),
  )
  return {
    labels: chrono.map((s) => new Date(s.startedAt).toLocaleDateString('de-AT')),
    datasets: [{ label: 'Befunde', data: chrono.map((s) => s.violationCount) }],
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { labels: { color: mutedColor } } },
  scales: {
    x: { ticks: { color: mutedColor }, grid: { color: borderColor } },
    y: { beginAtZero: true, ticks: { color: mutedColor }, grid: { color: borderColor } },
  },
}
</script>

<template>
  <div class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] p-6 h-[300px]">
    <Chart type="line" :data="chartData" :options="chartOptions" class="h-full" />
  </div>
</template>