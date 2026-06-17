<script setup lang="ts">
import { computed } from 'vue'
import type { ProjectResponseDTO } from '@/api/projects'

const props = defineProps<{
  project: ProjectResponseDTO
  compact?: boolean
}>()

const emit = defineEmits<{
  edit: [project: ProjectResponseDTO]
  delete: [project: ProjectResponseDTO]
  scan: [project: ProjectResponseDTO]
}>()

const displayUrl = computed(() => {
  try {
    const url = new URL(props.project.baseUrl)
    return url.hostname + (url.pathname !== '/' ? url.pathname : '')
  } catch {
    return props.project.baseUrl
  }
})
</script>

<template>
  <article class="relative flex flex-col gap-[0.4rem] bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.3rem] py-[1.2rem] shadow-[0_14px_34px_rgba(0,0,0,0.3)] transition duration-150 hover:-translate-y-0.5 hover:border-[var(--coral)] hover:shadow-[0_18px_42px_rgba(0,0,0,0.4)]">
    <h3 class="m-0">
      <router-link
        :to="`/projects/${project.id}`"
        :title="project.name"
        class="block text-[1.1rem] font-bold text-[var(--text)] no-underline overflow-hidden text-ellipsis whitespace-nowrap focus-visible:outline-none after:content-[''] after:absolute after:inset-0 after:rounded-[14px] focus-visible:after:[outline:2px_solid_var(--teal)] focus-visible:after:[outline-offset:2px]"
      >
        {{ project.name }}
      </router-link>
    </h3>

    <a
      :href="project.baseUrl"
      target="_blank"
      rel="noopener noreferrer"
      class="relative z-10 text-[var(--teal)] no-underline text-[0.9rem] overflow-hidden text-ellipsis whitespace-nowrap hover:underline"
      :title="project.baseUrl"
    >
      {{ displayUrl }} ↗
    </a>
    <p class="text-[var(--muted)] text-[0.85rem] mt-0 mb-[0.4rem]">Max. {{ project.crawlMaxPages }} Seiten / Scan</p>

    <div class="flex items-center justify-end gap-2 mt-auto">
      <button
        class="relative z-10 bg-transparent border border-[var(--border)] rounded-[9px] w-[2.1rem] h-[2.1rem] text-[0.95rem] cursor-pointer text-[var(--text)] hover:border-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
        type="button"
        aria-label="Bearbeiten"
        title="Bearbeiten"
        @click="emit('edit', project)"
      >
        ✏
      </button>
      <button
        class="relative z-10 bg-transparent border border-[var(--border)] rounded-[9px] w-[2.1rem] h-[2.1rem] text-[0.95rem] cursor-pointer text-[var(--text)] hover:border-[var(--danger-border)] hover:bg-[var(--danger-bg)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
        type="button"
        aria-label="Löschen"
        title="Löschen"
        @click="emit('delete', project)"
      >
        🗑
      </button>
    </div>
  </article>
  <!--<article class="flex flex-col gap-[0.4rem] bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.3rem] py-[1.2rem] shadow-[0_14px_34px_rgba(0,0,0,0.3)]">
    <h3 class="text-[1.1rem] font-bold text-[var(--text)] m-0 overflow-hidden text-ellipsis whitespace-nowrap" :title="project.name">{{ project.name }}</h3>

    <a
      :href="project.baseUrl"
      target="_blank"
      rel="noopener noreferrer"
      class="text-[var(--teal)] no-underline text-[0.9rem] overflow-hidden text-ellipsis whitespace-nowrap hover:underline"
      :title="project.baseUrl"
    >
      {{ displayUrl }} ↗
    </a>
    <p class="text-[var(--muted)] text-[0.85rem] mt-0 mb-[0.4rem]">Max. {{ project.crawlMaxPages }} Seiten / Scan</p>

    <div class="flex items-center gap-2 mt-auto">
      <button
        class="mr-auto border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer inline-flex items-center justify-center bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
        type="button"
        @click="emit('scan', project)"
      >
        Scannen ▶
      </button>
      <router-link :to="`/projects/${project.id}`" class="border border-[var(--border)] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer no-underline inline-flex items-center justify-center bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]">Öffnen</router-link>
      
      <button
        class="bg-transparent border border-[var(--border)] rounded-[9px] w-[2.1rem] h-[2.1rem] text-[0.95rem] cursor-pointer text-[var(--text)] hover:border-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
        type="button"
        aria-label="Bearbeiten"
        title="Bearbeiten"
        @click="emit('edit', project)"
      >
        ✏
      </button>
      <button
        class="bg-transparent border border-[var(--border)] rounded-[9px] w-[2.1rem] h-[2.1rem] text-[0.95rem] cursor-pointer text-[var(--text)] hover:border-[var(--danger-border)] hover:bg-[var(--danger-bg)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
        type="button"
        aria-label="Löschen"
        title="Löschen"
        @click="emit('delete', project)"
      >
        🗑
      </button>
    </div>
  </article>
  -->
</template>