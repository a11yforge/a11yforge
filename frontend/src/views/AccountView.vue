<script setup lang="ts">
import ChangeUserNameForm from '@/components/account/ChangeUserNameForm.vue'
import ChangeEmailForm from '@/components/account/ChangeEmailForm.vue'
import ChangePasswordForm from '@/components/account/ChangePasswordForm.vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

async function handleLogout() {
  await authStore.logout()
}
</script>

<template>
  <div class="max-w-[980px] mx-auto px-6 pt-6 pb-8 text-[#f3e9e2] font-sans">
    <h1 class="text-[1.5rem] font-extrabold text-[#f3e9e2] mt-0 mb-[1.2rem]">Account / Einstellungen</h1>

    <div class="grid gap-4 grid-cols-1 min-[760px]:grid-cols-3">
      <div class="card bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.3rem] py-[1.2rem]"><ChangeUserNameForm /></div>
      <div class="card bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.3rem] py-[1.2rem]"><ChangeEmailForm /></div>
      <div class="card bg-[#1e1a29] border border-[#322840] rounded-[14px] px-[1.3rem] py-[1.2rem]"><ChangePasswordForm /></div>
    </div>

    <button class="border border-[#5a2a2a] rounded-[10px] py-[0.6rem] px-[1.2rem] font-semibold text-[0.9rem] cursor-pointer bg-transparent text-[#f6a3a3] mt-[1.2rem] hover:bg-[#3a1a1a] focus-visible:[outline:2px_solid_#5bbeb2] focus-visible:[outline-offset:2px]" type="button" @click="handleLogout">Logout</button>
  </div>
</template>

<style scoped>
.card :deep(h2) {
  font-size: 1rem;
  font-weight: 700;
  color: var(--text);
}
.card :deep(.p-inputtext),
.card :deep(.p-password) {
  width: 100%;
}
</style>
