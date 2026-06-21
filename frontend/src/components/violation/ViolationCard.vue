<script setup lang="ts">
import ImpactBadge from './ImpactBadge.vue'
import FixProposalPanel from '@/components/fixproposal/FixProposalPanel.vue'
import type { ViolationDTO } from '@/api/scan'
import type { FixProposalDTO } from '@/api/fixproposal'

const props = defineProps<{
  violation: ViolationDTO
  fix: FixProposalDTO | undefined
}>()

const emit = defineEmits<{
  'request-fix': [violationId: number]
  review: [violationId: number, fixProposalId: number, decision: 'ACCEPTED' | 'REJECTED']
}>()

function onReview(decision: 'ACCEPTED' | 'REJECTED') {
  if (props.fix) {
    emit('review', props.violation.id, props.fix.id, decision)
  }
}
</script>

<template>
  <article class="bg-[var(--surface)] border border-[var(--border)] rounded-[14px] px-[1.2rem] py-[1.1rem]">
    <div class="flex justify-between gap-4 flex-wrap">
      <div class="flex-1 min-w-0">
        <span class="font-bold text-[var(--text)]">{{ violation.ruleId }}</span>
        <ImpactBadge :impact="violation.impact" class="ml-2" />
        <p class="mt-[0.4rem] mb-0 text-[var(--muted)] text-[0.9rem]">{{ violation.description }}</p>
      </div>
      <div class="flex gap-2 flex-wrap h-fit ml-auto">
        <button class="border border-[var(--border)] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button">✋ Selbst fixen</button>
        <button
          class="border border-transparent rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-[var(--coral)] text-[var(--on-coral)] disabled:opacity-50 disabled:cursor-not-allowed hover:enabled:brightness-[1.07] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]"
          type="button"
          :disabled="fix?.status === 'PENDING'"
          @click="emit('request-fix', violation.id)"
        >
          🤖 Mit KI fixen
        </button>
      </div>
    </div>

    <pre class="mt-[0.8rem] mb-0 bg-[var(--field)] border border-[var(--border)] rounded-lg py-[0.7rem] px-[0.8rem] font-mono text-[0.82rem] text-[var(--muted)] overflow-x-auto whitespace-pre">{{ violation.htmlSnippet }}</pre>

    <FixProposalPanel v-if="fix" :fix="fix" @review="onReview" />
  </article>
</template>