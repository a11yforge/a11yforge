<script setup lang="ts">
import ProjectCard from './ProjectCard.vue'
import ProgressSpinner from 'primevue/progressspinner'
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
}>()
</script>

<template>
  <div class="w-full">
    <div v-if="loading" class="flex justify-center py-12">
      <ProgressSpinner />
    </div>

    <div v-else-if="error" class="text-red-500 text-center py-8">
      {{ error }}
    </div>

    <div v-else-if="projects.length === 0" class="text-gray-400 text-center py-8">
      {{ emptyMessage ?? 'Noch keine Projekte vorhanden.' }}
    </div>

    <div v-else class="grid gap-4 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
      <ProjectCard
        v-for="project in projects"
        :key="project.id"
        :project="project"
        :compact="compact"
        @edit="emit('edit', $event)"
        @delete="emit('delete', $event)"
        @scan="emit('scan', $event)"
      />
    </div>
  </div>
</template>
