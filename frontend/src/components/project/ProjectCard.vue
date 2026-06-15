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
  <article class="flex flex-col gap-[0.4rem] bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.3rem] py-[1.2rem] shadow-[0_14px_34px_rgba(0,0,0,0.3)]">
    <h3 class="text-[1.1rem] font-bold text-[#f3e9e2] m-0 overflow-hidden text-ellipsis whitespace-nowrap" :title="project.name">{{ project.name }}</h3>

    <a
      :href="project.baseUrl"
      target="_blank"
      rel="noopener noreferrer"
      class="text-[#5bbeb2] no-underline text-[0.9rem] overflow-hidden text-ellipsis whitespace-nowrap hover:underline"
      :title="project.baseUrl"
    >
      {{ displayUrl }} ↗
    </a>
    <p class="text-[#a99cb0] text-[0.85rem] mt-0 mb-[0.4rem]">Max. {{ project.crawlMaxPages }} Seiten / Scan</p>

    <div class="flex items-center gap-2 mt-auto">
      <button
        class="mr-auto border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer inline-flex items-center justify-center bg-[#ff7a52] text-[#2a1410] hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]"
        type="button"
        @click="emit('scan', project)"
      >
        Scannen ▶
      </button>
      <router-link :to="`/projects/${project.id}`" class="border border-[#322840] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer no-underline inline-flex items-center justify-center bg-transparent text-[#f3e9e2] hover:border-[#ff7a52] hover:text-[#ff7a52] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]">Öffnen</router-link>
      <button
        class="bg-transparent border border-[#322840] rounded-[9px] w-[2.1rem] h-[2.1rem] text-[0.95rem] cursor-pointer text-[#f3e9e2] hover:border-[#ff7a52] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]"
        type="button"
        aria-label="Bearbeiten"
        title="Bearbeiten"
        @click="emit('edit', project)"
      >
        ✏
      </button>
      <button
        class="bg-transparent border border-[#322840] rounded-[9px] w-[2.1rem] h-[2.1rem] text-[0.95rem] cursor-pointer text-[#f3e9e2] hover:border-[#a14444] hover:bg-[#3a1a1a] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]"
        type="button"
        aria-label="Löschen"
        title="Löschen"
        @click="emit('delete', project)"
      >
        🗑
      </button>
    </div>
  </article>
</template>
