<script setup lang="ts">
import ProjectCard from './ProjectCard.vue'
import type { ProjectResponseDTO } from '@/api/projects'

defineProps<{
  projects: ProjectResponseDTO[]
  loading: boolean
  error: string | null
  emptyMessage?: string
  compact?: boolean
}>()

const emit = defineEmits<{
  edit: [project: ProjectResponseDTO]
  delete: [project: ProjectResponseDTO]
  scan: [project: ProjectResponseDTO]
  create: []
}>()
</script>

<template>
  <div>
    <div v-if="loading" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] py-10 px-8 text-center text-[#a99cb0]">Projekte werden geladen…</div>

    <div v-else-if="error" class="bg-[#1e1a29] border border-[#5a2a2a] rounded-[14px] py-10 px-8 text-center text-[#f6a3a3]">{{ error }}</div>

    <div v-else-if="projects.length === 0" class="bg-[#1e1a29] border border-[#322840] rounded-[14px] py-10 px-8 text-center text-[#a99cb0] flex flex-col items-center gap-[0.9rem]">
      <img src="/mole.png" alt="" aria-hidden="true" class="w-[80px] [image-rendering:pixelated] opacity-85" />
      <p>{{ emptyMessage ?? 'Noch keine Projekte — leg dein erstes an.' }}</p>
      <button class="border border-transparent rounded-[10px] py-[0.6rem] px-[1.1rem] font-semibold text-[0.9rem] cursor-pointer bg-[#ff7a52] text-[#2a1410] hover:brightness-[1.07] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button" @click="emit('create')">
        + Neues Projekt
      </button>
    </div>

    <div v-else class="grid gap-4 grid-cols-[repeat(auto-fill,minmax(280px,1fr))]">
      <ProjectCard
        v-for="project in projects"
        :key="project.id"
        :project="project"
        :compact="compact"
        @edit="emit('edit', $event)"
        @delete="emit('delete', $event)"
        @scan="(p) => emit('scan', p)"
      />
    </div>
  </div>
</template>
