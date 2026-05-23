<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Button from 'primevue/button'
import type { ProjectRequestDTO, ProjectResponseDTO } from '@/api/projects'

const props = defineProps<{
  visible: boolean
  project: ProjectResponseDTO | null
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  submit: [request: ProjectRequestDTO]
}>()

const isEditMode = computed(() => props.project !== null)
const dialogTitle = computed(() =>
  isEditMode.value ? 'Projekt bearbeiten' : 'Neues Projekt anlegen',
)
const submitLabel = computed(() => (isEditMode.value ? 'Speichern' : 'Anlegen'))

const name = ref('')
const baseUrl = ref('')
const crawlMaxPages = ref<number | null>(50)
const loading = ref(false)
const errorMsg = ref('')

const nameError = ref('')
const baseUrlError = ref('')
const crawlMaxPagesError = ref('')

watch(
  () => props.visible,
  (isVisible) => {
    if (isVisible) {
      if (props.project) {
        name.value = props.project.name
        baseUrl.value = props.project.baseUrl
        crawlMaxPages.value = props.project.crawlMaxPages
      } else {
        name.value = ''
        baseUrl.value = ''
        crawlMaxPages.value = 50
      }
      errorMsg.value = ''
      nameError.value = ''
      baseUrlError.value = ''
      crawlMaxPagesError.value = ''
    }
  },
)

function validate(): boolean {
  nameError.value = ''
  baseUrlError.value = ''
  crawlMaxPagesError.value = ''

  let valid = true

  const trimmedName = name.value.trim()
  if (!trimmedName) {
    nameError.value = 'Name ist erforderlich.'
    valid = false
  } else if (trimmedName.length > 150) {
    nameError.value = 'Name darf maximal 150 Zeichen lang sein.'
    valid = false
  }

  const trimmedUrl = baseUrl.value.trim()
  if (!trimmedUrl) {
    baseUrlError.value = 'Base-URL ist erforderlich.'
    valid = false
  } else if (!/^https?:\/\//i.test(trimmedUrl)) {
    baseUrlError.value = 'URL muss mit http:// oder https:// beginnen.'
    valid = false
  } else if (trimmedUrl.length > 500) {
    baseUrlError.value = 'URL darf maximal 500 Zeichen lang sein.'
    valid = false
  }

  if (crawlMaxPages.value === null) {
    crawlMaxPagesError.value = 'Maximale Seitenzahl ist erforderlich.'
    valid = false
  } else if (crawlMaxPages.value < 1) {
    crawlMaxPagesError.value = 'Mindestens 1 Seite.'
    valid = false
  } else if (crawlMaxPages.value > 50) {
    crawlMaxPagesError.value = 'Maximal 50 Seiten erlaubt.'
    valid = false
  }

  return valid
}

async function handleSubmit() {
  if (!validate()) return

  loading.value = true
  errorMsg.value = ''
  try {
    const request: ProjectRequestDTO = {
      name: name.value.trim(),
      baseUrl: baseUrl.value.trim(),
      crawlMaxPages: crawlMaxPages.value as number,
    }
    emit('submit', request)
  } finally {
    loading.value = false
  }
}

function handleCancel() {
  emit('update:visible', false)
}

function setExternalError(message: string) {
  errorMsg.value = message
}

defineExpose({ setExternalError })
</script>

<template>
  <Dialog
    :visible="visible"
    @update:visible="emit('update:visible', $event)"
    :header="dialogTitle"
    :modal="true"
    :closable="!loading"
    :style="{ width: '32rem' }"
  >
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4">
      <div class="flex flex-col gap-1">
        <label for="project-name" class="text-sm">Name</label>
        <InputText
          id="project-name"
          v-model="name"
          placeholder="z. B. Firmenwebsite"
          :invalid="!!nameError"
          :disabled="loading"
        />
        <p v-if="nameError" class="text-red-500 text-xs">{{ nameError }}</p>
      </div>

      <div class="flex flex-col gap-1">
        <label for="project-baseurl" class="text-sm">Base-URL</label>
        <InputText
          id="project-baseurl"
          v-model="baseUrl"
          placeholder="https://example.com"
          :invalid="!!baseUrlError"
          :disabled="loading"
        />
        <p v-if="baseUrlError" class="text-red-500 text-xs">{{ baseUrlError }}</p>
      </div>

      <div class="flex flex-col gap-1">
        <label for="project-crawlmaxpages" class="text-sm">Maximale Seitenzahl pro Scan</label>
        <InputNumber
          id="project-crawlmaxpages"
          v-model="crawlMaxPages"
          :min="1"
          :max="50"
          :invalid="!!crawlMaxPagesError"
          :disabled="loading"
        />
        <p v-if="crawlMaxPagesError" class="text-red-500 text-xs">{{ crawlMaxPagesError }}</p>
      </div>

      <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>

      <div class="flex justify-end gap-2 mt-2">
        <Button
          type="button"
          label="Abbrechen"
          severity="secondary"
          @click="handleCancel"
          :disabled="loading"
        />
        <Button type="submit" :label="submitLabel" :loading="loading" />
      </div>
    </form>
  </Dialog>
</template>