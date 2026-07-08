<script setup lang="ts">
import FixStatusBadge from './FixStatusBadge.vue'
import type { FixProposalDTO } from '@/api/fixproposal'

defineProps<{
  fix: FixProposalDTO
  decision: 'ACCEPTED' | 'REJECTED' | undefined
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

      <div v-if="fix.status === 'VERIFIED'" class="flex items-center gap-2 mt-[0.9rem] flex-wrap">
        <button
          class="border border-transparent rounded-[10px] py-[0.5rem] px-[0.9rem] font-semibold text-[0.85rem] cursor-pointer bg-[var(--teal)] text-[var(--on-teal)] hover:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          type="button"
          @click="emit('review', 'ACCEPTED')"
        >
          ✅ Akzeptieren
        </button>
        <button
          class="border border-[var(--border)] rounded-[10px] py-[0.5rem] px-[0.9rem] font-semibold text-[0.85rem] cursor-pointer bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          type="button"
          @click="emit('review', 'REJECTED')"
        >
          ❌ Verwerfen
        </button>

        <span
          v-if="decision === 'ACCEPTED'"
          class="ml-1 rounded-full py-[0.2rem] px-[0.6rem] text-[0.72rem] font-bold bg-[color-mix(in_srgb,var(--teal)_15%,transparent)] text-[var(--teal)]"
        >
          ✔ Akzeptiert
        </span>
        <span
          v-else-if="decision === 'REJECTED'"
          class="ml-1 rounded-full py-[0.2rem] px-[0.6rem] text-[0.72rem] font-bold bg-[color-mix(in_srgb,var(--coral)_15%,transparent)] text-[var(--coral)]"
        >
          ✕ Verworfen
        </span>
      </div>
    </template>
  </div>
</template>
