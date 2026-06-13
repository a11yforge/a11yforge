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
  scan: [project: ProjectResponseDTO, selectedProvider: string]
  create: []
}>()
</script>

<template>
  <div>
    <div v-if="loading" class="state">Projekte werden geladen…</div>

    <div v-else-if="error" class="state state--error">{{ error }}</div>

    <div v-else-if="projects.length === 0" class="state state--empty">
      <img src="/mole.png" alt="" aria-hidden="true" class="state__mole" />
      <p>{{ emptyMessage ?? 'Noch keine Projekte — leg dein erstes an.' }}</p>
      <button class="btn btn--primary" type="button" @click="emit('create')">
        + Neues Projekt
      </button>
    </div>

    <div v-else class="grid">
      <ProjectCard
        v-for="project in projects"
        :key="project.id"
        :project="project"
        :compact="compact"
        @edit="emit('edit', $event)"
        @delete="emit('delete', $event)"
        @scan="(p, provider) => emit('scan', p, provider)"
      />
    </div>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
}

.state {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 2.5rem 2rem;
  text-align: center;
  color: var(--muted);
}
.state--error {
  color: #f6a3a3;
  border-color: #5a2a2a;
}
.state--empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.9rem;
}
.state__mole {
  width: 80px;
  image-rendering: pixelated;
  opacity: 0.85;
}

.btn {
  border: 1px solid transparent;
  border-radius: 10px;
  padding: 0.6rem 1.1rem;
  font-weight: 600;
  font-size: 0.9rem;
  cursor: pointer;
}
.btn--primary {
  background: var(--coral);
  color: #2a1410;
}
.btn--primary:hover {
  filter: brightness(1.07);
}
.btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
</style>
