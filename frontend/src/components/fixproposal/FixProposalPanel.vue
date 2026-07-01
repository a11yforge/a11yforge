<script setup lang="ts">
import FixStatusBadge from './FixStatusBadge.vue'
import type { FixProposalDTO } from '@/api/fixproposal'

defineProps<{
  fix: FixProposalDTO
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
    </template>
  </div>
</template>
