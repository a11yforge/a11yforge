<script setup lang="ts">
import { computed, ref } from 'vue'
import Card from 'primevue/card'
import Button from 'primevue/button'
import Select from 'primevue/select'
import type { ProjectResponseDTO } from '@/api/projects'

const providerOptions = [
  { label: 'Ohne KI', value: "NONE" },
  { label: 'Anthropic', value: "ANTHROPIC"},
  { label: "Ollama (lokal)", value: "OLLAMA"},
]

const selectedProvider = ref<string>('NONE')

const props = defineProps<{
  project: ProjectResponseDTO
  compact?: boolean
}>()

const emit = defineEmits<{
  edit: [project: ProjectResponseDTO]
  delete: [project: ProjectResponseDTO]
  scan: [project: ProjectResponseDTO, selectedProvider: string]
}>()

const updatedAtFormatted = computed(() => {
  const date = new Date(props.project.updatedAt)
  return date.toLocaleDateString('de-AT', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
})

const displayUrl = computed(() => {
  try {
    const url = new URL(props.project.baseUrl)
    return url.hostname + (url.pathname !== '/' ? url.pathname : '')
  } catch {
    return props.project.baseUrl
  }
})
</script>

<template>
  <Card class="w-full">
    <template #title>
      <div class="flex items-start justify-between gap-2">
        <h3 class="text-lg font-semibold truncate" :title="project.name">
          {{ project.name }}
        </h3>
      </div>
    </template>

    <template #content>
      <div class="flex flex-col gap-2 text-sm">
        <div>
          <span class="text-gray-400">URL:</span>
          <a
            :href="project.baseUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="ml-2 text-blue-400 hover:underline truncate inline-block max-w-full align-bottom"
            :title="project.baseUrl"
          >
            {{ displayUrl }}
          </a>
        </div>

        <div v-if="!compact">
          <span class="text-gray-400">Max. Seiten pro Scan:</span>
          <span class="ml-2">{{ project.crawlMaxPages }}</span>
        </div>

        <div>
          <span class="text-gray-400">Zuletzt aktualisiert:</span>
          <span class="ml-2">{{ updatedAtFormatted }}</span>
        </div>
      </div>
    </template>

    <template #footer>
      <div class="flex items-center justify-end gap-2">
        <Button
          icon="pi pi-pencil"
          label="Bearbeiten"
          severity="secondary"
          size="small"
          @click="emit('edit', project)"
        />
        <router-link :to="`/projects/${project.id}/scans`" custom v-slot="{ navigate }">
          <Button label="Historie" icon="pi pi-history" severity="secondary" size="small" @click="navigate" />
        </router-link>
        <Button
          icon="pi pi-trash"
          label="Löschen"
          severity="danger"
          size="small"
          @click="emit('delete', project)"
        />
        <Select
          v-model="selectedProvider"
          :options="providerOptions"
          option-label="label"
          option-value="value"
          size="small"
          class="w-32"
          aria-label="LLM-Provider auswählen"
        />
        <Button
          icon="pi pi-play"
          label="Scan"
          severity="success"
          size="small"
          @click="emit('scan', project, selectedProvider)"
        />
      </div>
    </template>
  </Card>
</template>
