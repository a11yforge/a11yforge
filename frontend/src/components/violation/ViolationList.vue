<script setup lang="ts">
import ViolationCard from './ViolationCard.vue'
import type { ViolationDTO } from '@/api/scan'
import type { FixProposalDTO } from '@/api/fixproposal'

defineProps<{
  violations: ViolationDTO[]
  fixes: Record<number, FixProposalDTO>
}>()

const emit = defineEmits<{
  'fix-all': []
  'request-fix': [violationId: number]
  review: [violationId: number, fixProposalId: number, decision: 'ACCEPTED' | 'REJECTED']
}>()
</script>

<template>
  <div>
    <div class="flex items-center justify-between my-8 mb-4">
      <h2 class="text-[1.2rem] font-bold text-[var(--text)] m-0">Befunde</h2>
      <button class="border border-[var(--border)] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('fix-all')">⚡ Alle mit KI fixen</button>
    </div>

    <div class="flex flex-col gap-[0.9rem]">
      <ViolationCard
        v-for="v in violations"
        :key="v.id"
        :violation="v"
        :fix="fixes[v.id]"
        @request-fix="emit('request-fix', $event)"
        @review="(violationId, fixProposalId, decision) => emit('review', violationId, fixProposalId, decision)"
      />
    </div>
  </div>
</template>