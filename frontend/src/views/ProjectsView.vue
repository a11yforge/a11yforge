<script setup lang="ts">
import { ref, computed, onMounted, useTemplateRef } from 'vue'
import { storeToRefs } from 'pinia'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'
import { useProjectsStore } from '@/stores/projects'
import ProjectList from '@/components/project/ProjectList.vue'
import ProjectFormDialog from '@/components/project/ProjectFormDialog.vue'
import type { ProjectRequestDTO, ProjectResponseDTO } from '@/api/projects'
//import { startScan } from '@/api/scan'
//import { useRouter } from 'vue-router'

//const router = useRouter()

const projectsStore = useProjectsStore()
const { projects, loading, error } = storeToRefs(projectsStore)

const confirm = useConfirm()
const toast = useToast()

const dialogVisible = ref(false)
const editingProject = ref<ProjectResponseDTO | null>(null)
const dialogRef = useTemplateRef<InstanceType<typeof ProjectFormDialog>>('dialogRef')

const search = ref('')
const filteredProjects = computed(() => {
  return projects.value.filter((p) => {
    const projectName = p.name.toLowerCase()
    const searchInput = search.value.toLowerCase()
    return projectName.includes(searchInput)
  })
})

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
/*
async function handleScan(project: ProjectResponseDTO) {
  try {
    const scan = await startScan(project.id)
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
*/
</script>

<template>
  <div class="max-w-[1040px] mx-auto px-6 pt-8 pb-12 text-[var(--text)] font-sans">
    <div class="flex items-center justify-between gap-4 mb-[1.2rem]">
      <h1 class="text-[1.7rem] font-extrabold text-[var(--text)] m-0">Meine Projekte</h1>
      <button class="border border-transparent rounded-[10px] py-[0.6rem] px-[1.1rem] font-semibold text-[0.9rem] cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="openCreateDialog">
        + Neues Projekt
      </button>
    </div>

    <input
      v-model="search"
      type="search"
      class="w-full bg-[var(--field)] border border-[var(--border)] rounded-[10px] py-[0.65rem] px-[0.9rem] text-[var(--text)] mb-6 placeholder:text-[var(--muted)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
      placeholder="🔍 Projekt suchen …"
      aria-label="Projekt suchen"
    />

    <ProjectList
      :projects="filteredProjects"
      :loading="loading"
      :error="error"
      empty-message="Noch keine Projekte — leg dein erstes an."
      @edit="openEditDialog"
      @delete="confirmDelete"
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
