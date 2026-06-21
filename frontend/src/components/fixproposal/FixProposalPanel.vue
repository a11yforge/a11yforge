<script setup lang="ts">
import FixStatusBadge from './FixStatusBadge.vue'
import type { FixProposalDTO } from '@/api/fixproposal'

defineProps<{
  fix: FixProposalDTO
}>()

const emit = defineEmits<{
  review: [decision: 'ACCEPTED' | 'REJECTED']
}>()
</script>

<template>
  <div class="mt-4 border-t border-[var(--border)] pt-4">
    <p v-if="fix.status === 'PENDING'" class="text-[var(--muted)] text-[0.9rem] m-0">
      ⏳ Wird generiert & verifiziert…
    </p>

    <template v-else>
      <div class="flex items-center gap-[0.6rem] mb-[0.6rem] text-[0.85rem]">
        <FixStatusBadge :status="fix.status" />
        <span class="text-[var(--muted)]">{{ fix.llmProvider }} / {{ fix.llmModel }}</span>
      </div>

      <div v-if="fix.generatedHtml">
        <p class="mt-0 mb-[0.3rem] text-[0.72rem] uppercase tracking-[0.4px] text-[var(--muted)]">Nachher</p>
        <pre class="mt-[0.8rem] mb-0 bg-[var(--field)] border border-[var(--border)] rounded-lg py-[0.7rem] px-[0.8rem] font-mono text-[0.82rem] text-[var(--teal)] overflow-x-auto whitespace-pre">{{ fix.generatedHtml }}</pre>
      </div>

      <div v-if="fix.status === 'VERIFIED'" class="flex gap-2 mt-[0.8rem]">
        <button
          class="border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-[var(--teal)] text-[var(--on-teal)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          type="button"
          @click="emit('review', 'ACCEPTED')"
        >
          ✅ Annehmen
        </button>
        <button
          class="border border-[var(--error-border)] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[var(--error-text)] hover:bg-[var(--danger-bg)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          type="button"
          @click="emit('review', 'REJECTED')"
        >
          ❌ Ablehnen
        </button>
      </div>
    </template>
  </div>
</template>