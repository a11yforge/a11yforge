<script setup lang="ts">
defineProps<{
  status: string
}>()
</script>

<template>
  <span
    class="rounded-full py-[0.2rem] px-[0.6rem] text-[0.72rem] font-bold"
    :class="
      status === 'VERIFIED'
        ? 'bg-[color-mix(in_srgb,var(--teal)_15%,transparent)] text-[var(--teal)]'
        : 'bg-[color-mix(in_srgb,var(--coral)_15%,transparent)] text-[var(--coral)]'
    "
  >
    {{ status }}
  </span>
</template>