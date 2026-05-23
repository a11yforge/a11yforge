<script setup lang="ts">
import { ref, onMounted, useTemplateRef } from 'vue'
import { storeToRefs } from 'pinia'
import Button from 'primevue/button'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'
import { useProjectsStore } from '@/stores/projects'
import ProjectList from '@/components/project/ProjectList.vue'
import ProjectFormDialog from '@/components/project/ProjectFormDialog.vue'
import type { ProjectRequestDTO, ProjectResponseDTO } from '@/api/projects'

const projectsStore = useProjectsStore()
const { projects, loading, error } = storeToRefs(projectsStore)

const confirm = useConfirm()
const toast = useToast()

const dialogVisible = ref(false)
const editingProject = ref<ProjectResponseDTO | null>(null)
const dialogRef = useTemplateRef<InstanceType<typeof ProjectFormDialog>>('dialogRef')

onMounted(async () => {
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
})

function openCreateDialog() {
  editingProject.value = null
  dialogVisible.value = true
}

function openEditDialog(project: ProjectResponseDTO) {
  editingProject.value = project
  dialogVisible.value = true
}

async function handleSubmit(request: ProjectRequestDTO) {
  if (editingProject.value) {
    await handleUpdate(editingProject.value.id, request)
  } else {
    await handleCreate(request)
  }
}

async function handleCreate(request: ProjectRequestDTO) {
  try {
    await projectsStore.createProject(request)
    dialogVisible.value = false
    toast.add({
      severity: 'success',
      summary: 'Projekt angelegt',
      detail: `"${request.name}" wurde erfolgreich erstellt.`,
      life: 3000,
    })
  } catch (e: unknown) {
    handleFormError(e, 'Projekt konnte nicht angelegt werden.')
  }
}

async function handleUpdate(id: number, request: ProjectRequestDTO) {
  try {
    await projectsStore.updateProject(id, request)
    dialogVisible.value = false
    toast.add({
      severity: 'success',
      summary: 'Projekt aktualisiert',
      detail: `"${request.name}" wurde gespeichert.`,
      life: 3000,
    })
  } catch (e: unknown) {
    handleFormError(e, 'Projekt konnte nicht aktualisiert werden.')
  }
}

function handleFormError(e: unknown, fallback: string) {
  let message = fallback
  if (
    e &&
    typeof e === 'object' &&
    'response' in e &&
    e.response &&
    typeof e.response === 'object' &&
    'status' in e.response &&
    e.response.status === 409
  ) {
    message = 'Ein Projekt mit diesem Namen existiert bereits.'
  }
  dialogRef.value?.setExternalError(message)
}

function confirmDelete(project: ProjectResponseDTO) {
  confirm.require({
    message: `Möchtest du das Projekt "${project.name}" wirklich löschen? Diese Aktion kann nicht rückgängig gemacht werden.`,
    header: 'Projekt löschen',
    icon: 'pi pi-exclamation-triangle',
    rejectLabel: 'Abbrechen',
    acceptLabel: 'Löschen',
    acceptClass: 'p-button-danger',
    accept: async () => {
      await handleDelete(project)
    },
  })
}

async function handleDelete(project: ProjectResponseDTO) {
  try {
    await projectsStore.deleteProject(project.id)
    toast.add({
      severity: 'success',
      summary: 'Projekt gelöscht',
      detail: `"${project.name}" wurde entfernt.`,
      life: 3000,
    })
  } catch {
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Projekt konnte nicht gelöscht werden.',
      life: 4000,
    })
  }
}
</script>

<template>
  <div class="max-w-7xl mx-auto p-6">
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-bold">Projekte</h1>
      <Button icon="pi pi-plus" label="Neues Projekt" @click="openCreateDialog" />
    </div>

    <ProjectList
      :projects="projects"
      :loading="loading"
      :error="error"
      empty-message="Du hast noch keine Projekte angelegt. Lege dein erstes Projekt an."
      @edit="openEditDialog"
      @delete="confirmDelete"
    />

    <ProjectFormDialog
      ref="dialogRef"
      v-model:visible="dialogVisible"
      :project="editingProject"
      @submit="handleSubmit"
    />
  </div>
</template>

