<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  status: string
}>()

type StatusMeta = {
  icon: string
  label: string
  tone: 'completed' | 'running' | 'degraded' | 'failed'
}

const STATUS_META: Record<string, StatusMeta> = {
  COMPLETED: { icon: '✅', label: 'Fertig', tone: 'completed' },
  RUNNING: { icon: '⏳', label: 'Läuft', tone: 'running' },
  DEGRADED: { icon: '⚠️', label: 'Teilweise', tone: 'degraded' },
  FAILED: { icon: '❌', label: 'Fehler', tone: 'failed' },
}

const meta = computed<StatusMeta>(
  () => STATUS_META[props.status] ?? { icon: '•', label: props.status, tone: 'running' },
)
</script>

<template>
  <span
    class="rounded-full py-[0.2rem] px-[0.65rem] text-[0.74rem] font-bold whitespace-nowrap"
    :class="{
      'bg-[color-mix(in_srgb,var(--teal)_15%,transparent)] text-[var(--teal)]': meta.tone === 'completed',
      'bg-[color-mix(in_srgb,var(--peach)_15%,transparent)] text-[var(--peach)]':
        meta.tone === 'running' || meta.tone === 'degraded',
      'bg-[color-mix(in_srgb,var(--coral)_15%,transparent)] text-[var(--coral)]': meta.tone === 'failed',
    }"
  >
    {{ meta.icon }} {{ meta.label }}
  </span>
</template>