<script setup lang="ts">
defineProps<{
  impact: string
}>()
</script>

<template>
  <span
    class="rounded-full py-[0.2rem] px-[0.6rem] text-[0.72rem] font-bold"
    :class="
      impact === 'CRITICAL'
        ? 'bg-[color-mix(in_srgb,var(--coral)_15%,transparent)] text-[var(--coral)]'
        : 'bg-[color-mix(in_srgb,var(--peach)_15%,transparent)] text-[var(--peach)]'
    "
  >
    {{ impact }}
  </span>
</template>