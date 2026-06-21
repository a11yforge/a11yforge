<script setup lang="ts">
defineProps<{
  status: string
  sort: 'newest' | 'oldest'
}>()

const emit = defineEmits<{
  'update:status': [value: string]
  'update:sort': [value: 'newest' | 'oldest']
}>()
</script>

<template>
  <div class="flex gap-6 flex-wrap mb-[1.2rem]">
    <label class="text-[var(--muted)] text-[0.9rem] flex items-center gap-2">Filter:
      <select
        :value="status"
        @change="emit('update:status', ($event.target as HTMLSelectElement).value)"
        class="bg-[var(--field)] border border-[var(--border)] rounded-lg text-[var(--text)] py-[0.4rem] px-[0.6rem]"
      >
        <option value="ALL">Alle</option>
        <option value="COMPLETED">Fertig</option>
        <option value="RUNNING">Läuft</option>
        <option value="DEGRADED">Teilweise</option>
        <option value="FAILED">Fehler</option>
      </select>
    </label>
    <label class="text-[var(--muted)] text-[0.9rem] flex items-center gap-2">Sortierung:
      <select
        :value="sort"
        @change="emit('update:sort', ($event.target as HTMLSelectElement).value as 'newest' | 'oldest')"
        class="bg-[var(--field)] border border-[var(--border)] rounded-lg text-[var(--text)] py-[0.4rem] px-[0.6rem]"
      >
        <option value="newest">Neueste zuerst</option>
        <option value="oldest">Älteste zuerst</option>
      </select>
    </label>
  </div>
</template>