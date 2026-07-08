<script setup lang="ts">
import { computed } from 'vue'
import ViolationCard from './ViolationCard.vue'
import type { ViolationDTO } from '@/api/scan'
import type { FixProposalDTO } from '@/api/fixproposal'

const props = defineProps<{
  violations: ViolationDTO[]
  fixes: Record<number, FixProposalDTO>
  reviewed: Record<number, 'ACCEPTED' | 'REJECTED'>
}>()

const emit = defineEmits<{
  'fix-all': []
  'request-fix': [violationId: number]
  review: [violationId: number, fixProposalId: number, decision: 'ACCEPTED' | 'REJECTED']
}>()

const groups = computed(() => {
  const byRule = new Map<string, ViolationDTO[]>()
  for (const v of props.violations) {
    const list = byRule.get(v.ruleId) ?? []
    list.push(v)
    byRule.set(v.ruleId, list)
  }
  return [...byRule.entries()]
    .map(([ruleId, items]) => ({ ruleId, items }))
    .sort((a, b) => b.items.length - a.items.length)
})
</script>

<template>
  
  <div>
    <div class="flex items-center justify-between my-8 mb-4">
      <h2 class="text-[1.2rem] font-bold text-[var(--text)] m-0">Befunde</h2>
      <button class="border border-[var(--border)] rounded-[10px] py-[0.55rem] px-4 font-semibold text-[0.88rem] cursor-pointer bg-transparent text-[var(--text)] hover:border-[var(--coral)] hover:text-[var(--coral)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:2px]" type="button" @click="emit('fix-all')">⚡ Alle mit KI fixen</button>
    </div>
    <div class="flex flex-col gap-3">
      <details
        v-for="g in groups"
        :key="g.ruleId"
        class="border border-[var(--border)] rounded-[12px] overflow-hidden [&[open]>summary]:border-b [&[open]>summary]:border-[var(--border)]"
      >
        <summary class="flex items-center gap-3 py-3 px-4 cursor-pointer select-none font-semibold text-[var(--text)] hover:bg-[var(--surface)] focus-visible:[outline:2px_solid_var(--teal)] focus-visible:[outline-offset:-2px]">
          <span class="font-mono text-[0.92rem]">{{ g.ruleId }}</span>
          <span class="ml-auto text-[0.78rem] font-semibold text-[var(--muted)] border border-[var(--border)] rounded-full py-[0.1rem] px-[0.55rem]">{{ g.items.length }}</span>
        </summary>

        <div class="flex flex-col gap-[0.9rem] p-4">
          <ViolationCard
            v-for="v in g.items"
            :key="v.id"
            :violation="v"
            :fix="fixes[v.id]"
            :decision="reviewed[v.id]"
            @request-fix="emit('request-fix', $event)"
            @review="(violationId, fixProposalId, decision) => emit('review', violationId, fixProposalId, decision)"
          />
        </div>
      </details>
    </div>
  </div>
</template>
