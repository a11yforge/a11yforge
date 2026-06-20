<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useConfirm } from 'primevue/useconfirm'
import { getScanFromProject, startScan, type ScanResponseDTO } from '@/api/scan'
import {
  getProjectById,
  deleteProject,
  updateProject,
  type ProjectRequestDTO,
  type ProjectResponseDTO,
} from '@/api/projects'
import ProjectDetailHeader from '@/components/project/ProjectDetailHeader.vue'
import ProjectFormDialog from '@/components/project/ProjectFormDialog.vue'
import ScanList from '@/components/scan/ScanList.vue'

const router = useRouter()
const confirm = useConfirm()
const route = useRoute()

const loading = ref(true)
const error = ref<string | null>(null)
const scans = ref<ScanResponseDTO[]>([])
const projectId = Number(route.params.id)
const project = ref<ProjectResponseDTO | null>(null)
const editVisible = ref(false)

onMounted(async () => {
  try {
    const [proj, scanList] = await Promise.all([
      getProjectById(projectId),
      getScanFromProject(projectId),
    ])
    project.value = proj
    scans.value = scanList
  } catch {
    error.value = 'Scan konnte nicht geladen werden!'
  } finally {
    loading.value = false
  }
})

function handleDelete() {
  confirm.require({
    message: `Möchtest du "${project.value?.name}" wirklich löschen?`,
    header: 'Projekt löschen',
    icon: 'pi pi-exclamation-triangle',
    acceptClass: 'p-button-danger',
    accept: async () => {
      await deleteProject(projectId)
      router.push('/projects')
    },
  })
}

function openEdit() {
  editVisible.value = true
}

async function handleUpdate(request: ProjectRequestDTO) {
  project.value = await updateProject(projectId, request)
  editVisible.value = false
}

async function handleScan() {
  const scan = await startScan(projectId)
  router.push('/scans/' + scan.id)
}
</script>

<template>
  <div class="max-w-[880px] mx-auto px-6 pt-8 pb-12 text-[var(--text)] font-sans">
    <router-link to="/projects" class="inline-block mb-[1.4rem] text-[var(--muted)] no-underline text-[0.9rem] hover:text-[var(--coral)]">← Zurück zu Projekte</router-link>

    <ProjectDetailHeader
      :project="project"
      @scan="handleScan"
      @edit="openEdit"
      @delete="handleDelete"
    />

    <section class="mt-8">
      <div class="flex items-baseline justify-between mb-4">
        <h2 class="text-[1.15rem] font-bold text-[var(--text)] m-0">Letzte Scans</h2>
        <router-link
          v-if="scans.length > 0 && !loading"
          :to="`/projects/${projectId}/scans`"
          class="text-[var(--coral)] no-underline text-[0.9rem] font-semibold hover:underline"
        >
          Alle Scans ansehen →
        </router-link>
      </div>

      <ScanList
        :scans="scans"
        :loading="loading"
        :error="error"
        @scan="handleScan"
      />
    </section>
  </div>

  <ProjectFormDialog
    v-model:visible="editVisible"
    :project="project"
    @submit="handleUpdate"
  />
</template>