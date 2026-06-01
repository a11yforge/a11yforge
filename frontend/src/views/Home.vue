<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import Card from 'primevue/card'
import Button from 'primevue/button'
import WcagCarousel from '@/components/home/WcagCarousel.vue'
import { useToast } from 'primevue/usetoast'
import { useAuthStore } from '@/stores/auth'
import { useProjectsStore } from '@/stores/projects'
import ProjectList from '@/components/project/ProjectList.vue'
import type { ProjectResponseDTO } from '@/api/projects'

const router = useRouter()
const toast = useToast()

const authStore = useAuthStore()
const { userName } = storeToRefs(authStore)

const projectsStore = useProjectsStore()
const { recentProjects, projectCount, loading, error } = storeToRefs(projectsStore)

onMounted(async () => {
  if (projectsStore.projects.length === 0) {
    try {
      await projectsStore.fetchProjects()
    } catch {
      toast.add({
        severity: 'error',
        summary: 'Fehler',
        detail: 'Projekte konnten nicht geladen werden.',
        life: 4000,
      })
    }
  }
})

function goToProjects() {
  router.push('/projects')
}

function handleEdit(project: ProjectResponseDTO) {
  router.push('/projects')
}

function handleDelete(project: ProjectResponseDTO) {
  router.push('/projects')
}
</script>

<template>
  <div class="max-w-7xl mx-auto p-6">
    <div class="mb-8">
      <h1 class="text-3xl font-bold">
        Willkommen<span v-if="userName">, {{ userName }}</span>
      </h1>
      <p class="text-gray-400 mt-1">Übersicht über deine A11y-Projekte</p>
    </div>

    <div class="grid gap-4 grid-cols-1 md:grid-cols-3 mb-8">
      <Card>
        <template #title>
          <span class="text-sm text-gray-400 font-normal">Projekte gesamt</span>
        </template>
        <template #content>
          <div class="text-3xl font-bold">{{ projectCount }}</div>
        </template>
      </Card>

      <Card>
        <template #title>
          <span class="text-sm text-gray-400 font-normal">Scans gesamt</span>
        </template>
        <template #content>
          <div class="text-3xl font-bold text-gray-500">—</div>
          <p class="text-xs text-gray-500 mt-1">Kommt in Phase 2</p>
        </template>
      </Card>

      <Card>
        <template #title>
          <span class="text-sm text-gray-400 font-normal">Offene Reviews</span>
        </template>
        <template #content>
          <div class="text-3xl font-bold text-gray-500">—</div>
          <p class="text-xs text-gray-500 mt-1">Kommt in Phase 2</p>
        </template>
      </Card>
    </div>

    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-semibold">Zuletzt aktualisiert</h2>
      <Button
        label="Alle Projekte"
        icon="pi pi-arrow-right"
        icon-pos="right"
        severity="secondary"
        size="small"
        @click="goToProjects"
      />
    </div>

    <ProjectList
      :projects="recentProjects"
      :loading="loading"
      :error="error"
      :compact="true"
      empty-message='Noch keine Projekte. Lege dein erstes Projekt unter "Projekte" an.'
      @edit="handleEdit"
      @delete="handleDelete"
    />
  </div>
<div>
  <WcagCarousel/>
</div>

</template>
