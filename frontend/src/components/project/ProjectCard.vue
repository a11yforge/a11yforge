<script setup lang="ts">
import { ref, computed } from 'vue'
import Select from 'primevue/select'
import type { ProjectResponseDTO } from '@/api/projects'

const providerOptions = [
  { label: 'Ohne KI', value: 'NONE' },
  { label: 'Anthropic', value: 'ANTHROPIC' },
  { label: 'Ollama (lokal)', value: 'OLLAMA' },
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
  <article class="card">
    <h3 class="card__name" :title="project.name">{{ project.name }}</h3>

    <a
      :href="project.baseUrl"
      target="_blank"
      rel="noopener noreferrer"
      class="card__url"
      :title="project.baseUrl"
    >
      {{ displayUrl }} ↗
    </a>
    <p class="card__meta">Max. {{ project.crawlMaxPages }} Seiten / Scan</p>

    <div class="card__scan">
      <Select
        v-model="selectedProvider"
        :options="providerOptions"
        option-label="label"
        option-value="value"
        aria-label="LLM-Provider auswählen"
        class="card__select"
      />
      <button
        class="btn btn--primary"
        type="button"
        @click="emit('scan', project, selectedProvider)"
      >
        Scannen ▶
      </button>
    </div>

    <div class="card__actions">
      <router-link :to="`/projects/${project.id}`" class="btn btn--ghost">Öffnen</router-link>
      <button
        class="icon-btn"
        type="button"
        aria-label="Bearbeiten"
        title="Bearbeiten"
        @click="emit('edit', project)"
      >
        ✏
      </button>
      <button
        class="icon-btn icon-btn--danger"
        type="button"
        aria-label="Löschen"
        title="Löschen"
        @click="emit('delete', project)"
      >
        🗑
      </button>
    </div>
  </article>
</template>

<style scoped>
.card {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.2rem 1.3rem;
  box-shadow: 0 14px 34px rgba(0, 0, 0, 0.3);
}
.card__name {
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--text);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card__url {
  color: var(--teal);
  text-decoration: none;
  font-size: 0.9rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card__url:hover {
  text-decoration: underline;
}
.card__meta {
  color: var(--muted);
  font-size: 0.85rem;
  margin: 0 0 0.4rem;
}

.card__scan {
  display: flex;
  gap: 0.5rem;
  margin-top: auto;
}
.card__select {
  flex: 1;
  min-width: 0;
}

.card__actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.6rem;
}

.btn {
  border: 1px solid transparent;
  border-radius: 10px;
  padding: 0.55rem 1rem;
  font-weight: 600;
  font-size: 0.88rem;
  cursor: pointer;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
.btn--primary {
  background: var(--coral);
  color: #2a1410;
}
.btn--primary:hover {
  filter: brightness(1.07);
}
.btn--ghost {
  background: transparent;
  color: var(--text);
  border-color: var(--border);
  margin-right: auto;
}
.btn--ghost:hover {
  border-color: var(--coral);
  color: var(--coral);
}

.icon-btn {
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 9px;
  width: 2.1rem;
  height: 2.1rem;
  font-size: 0.95rem;
  cursor: pointer;
  color: var(--text);
}
.icon-btn:hover {
  border-color: var(--coral);
}
.icon-btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
.icon-btn--danger:hover {
  border-color: #a14444;
  background: #3a1a1a;
}

/* PrimeVue Select an die Palette angleichen */
:deep(.p-select) {
  background: var(--field);
  border: 1px solid var(--border);
  border-radius: 10px;
  width: 100%;
}
:deep(.p-select-label) {
  color: var(--text);
  padding: 0.55rem 0.7rem;
}
:deep(.p-select:not(.p-disabled).p-focus) {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
  border-color: var(--teal);
  box-shadow: none;
}
</style>
