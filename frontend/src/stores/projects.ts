import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  getProjects as apiGetProjects,
  createProject as apiCreateProject,
  updateProject as apiUpdateProject,
  deleteProject as apiDeleteProject,
} from '@/api/projects'
import type { ProjectRequestDTO, ProjectResponseDTO } from '@/api/projects'

export const useProjectsStore = defineStore('projects', () => {
  const projects = ref<ProjectResponseDTO[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const projectCount = computed(() => projects.value.length)

  const recentProjects = computed(() =>
    [...projects.value]
      .sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime())
      .slice(0, 3),
  )

  async function fetchProjects(): Promise<void> {
    loading.value = true
    error.value = null
    try {
      projects.value = await apiGetProjects()
    } catch (e) {
      error.value = 'Projekte konnten nicht geladen werden.'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function createProject(request: ProjectRequestDTO): Promise<ProjectResponseDTO> {
    const created = await apiCreateProject(request)
    projects.value.push(created)
    return created
  }

  async function updateProject(
    id: number,
    request: ProjectRequestDTO,
  ): Promise<ProjectResponseDTO> {
    const updated = await apiUpdateProject(id, request)
    const index = projects.value.findIndex((p) => p.id === id)
    if (index !== -1) {
      projects.value[index] = updated
    }
    return updated
  }

  async function deleteProject(id: number): Promise<void> {
    await apiDeleteProject(id)
    projects.value = projects.value.filter((p) => p.id !== id)
  }

  function reset(): void {
    projects.value = []
    loading.value = false
    error.value = null
  }

  return {
    projects,
    loading,
    error,
    projectCount,
    recentProjects,
    fetchProjects,
    createProject,
    updateProject,
    deleteProject,
    reset,
  }
})