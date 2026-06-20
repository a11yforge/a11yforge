<script setup lang="ts">
import type { ProjectResponseDTO } from '@/api/projects'

defineProps<{
  project: ProjectResponseDTO | null
}>()

const emit = defineEmits<{
  scan: []
  edit: []
  delete: []
}>()
</script>

<template>
  <header class="flex justify-between items-start gap-6 flex-wrap bg-[var(--surface)] border border-[var(--border)] rounded-[16px] px-[1.7rem] py-[1.6rem] shadow-[0_18px_44px_rgba(0,0,0,0.35)]">
    <div>
      <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] m-0">{{ project?.name }}</h1>
      <p class="mt-2 mb-0 text-[var(--muted)] text-[0.92rem]">
        URL:
        <a :href="project?.baseUrl" target="_blank" rel="noopener" class="text-[var(--teal)] no-underline hover:underline">
          {{ project?.baseUrl }} ↗
        </a>
        <span class="mx-2 text-[var(--border)]">·</span>
        Max. Seiten: {{ project?.crawlMaxPages }}
      </p>
    </div>
    <div class="flex gap-2 flex-wrap">
      <button class="border border-transparent rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('scan')">Scannen</button>
      <button class="border border-[var(--border)] rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('edit')">✏ Bearbeiten</button>
      <button class="border border-[var(--error-border)] rounded-[10px] py-[0.6rem] px-4 font-semibold text-[0.9rem] cursor-pointer bg-transparent text-[var(--error-text)] hover:bg-[var(--danger-bg)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('delete')">🗑 Löschen</button>
    </div>
  </header>
</template>