<script setup lang="ts">
import { ref, onMounted, useTemplateRef } from 'vue'
import { storeToRefs } from 'pinia'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'
import { useProjectsStore } from '@/stores/projects'
import ProjectList from '@/components/project/ProjectList.vue'
import ProjectFormDialog from '@/components/project/ProjectFormDialog.vue'
import type { ProjectRequestDTO, ProjectResponseDTO } from '@/api/projects'
import { startScan } from '@/api/scan'
import { useRouter } from 'vue-router'

const router = useRouter()

const projectsStore = useProjectsStore()
const { projects, loading, error } = storeToRefs(projectsStore)

const confirm = useConfirm()
const toast = useToast()

const dialogVisible = ref(false)
const editingProject = ref<ProjectResponseDTO | null>(null)
const dialogRef = useTemplateRef<InstanceType<typeof ProjectFormDialog>>('dialogRef')

// TODO Logik-Phase: Projekte clientseitig nach `search` filtern
const search = ref('')

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

async function handleScan(project: ProjectResponseDTO, provider: string) {
  try {
    const scan = await startScan(project.id, provider)
    router.push('/scans/' + scan.id)
  } catch {
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Scan konnte nicht gestartet werden.',
      life: 4000,
    })
  }
}
</script>

<template>
  <div class="page">
    <div class="page__head">
      <h1 class="page__title">Meine Projekte</h1>
      <button class="btn btn--primary" type="button" @click="openCreateDialog">
        + Neues Projekt
      </button>
    </div>

    <input
      v-model="search"
      type="search"
      class="search"
      placeholder="🔍 Projekt suchen …"
      aria-label="Projekt suchen"
    />

    <ProjectList
      :projects="projects"
      :loading="loading"
      :error="error"
      empty-message="Noch keine Projekte — leg dein erstes an."
      @edit="openEditDialog"
      @delete="confirmDelete"
      @scan="handleScan"
      @create="openCreateDialog"
    />

    <ProjectFormDialog
      ref="dialogRef"
      v-model:visible="dialogVisible"
      :project="editingProject"
      @submit="handleSubmit"
    />
  </div>
</template>

<style scoped>
.page {
  max-width: 1040px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.page__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 1.2rem;
}
.page__title {
  font-size: 1.7rem;
  font-weight: 800;
  color: var(--text);
  margin: 0;
}

.search {
  width: 100%;
  background: var(--field);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 0.65rem 0.9rem;
  color: var(--text);
  margin-bottom: 1.5rem;
}
.search::placeholder {
  color: var(--muted);
}
.search:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
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
