<script setup lang="ts">
import { ref } from 'vue'
import ChangeUserNameForm from '@/components/account/ChangeUserNameForm.vue'
import ChangeEmailForm from '@/components/account/ChangeEmailForm.vue'
import ChangePasswordForm from '@/components/account/ChangePasswordForm.vue'

type Tab = 'username' | 'email' | 'password'

const activeTab = ref<Tab>('username')

const tabs: { key: Tab; label: string }[] = [
  { key: 'username', label: 'Username' },
  { key: 'email', label: 'E-Mail' },
  { key: 'password', label: 'Passwort' },
]
</script>

<template>
  <div class="flex justify-center px-4 py-10">
    <div
      class="w-full max-w-[460px] bg-[#1e1a29] border border-[#322840] rounded-[16px] px-[1.9rem] pt-8 pb-[1.8rem] shadow-[0_24px_60px_rgba(0,0,0,0.45)]"
    >
      <h1 class="text-xl font-bold mb-6 text-[var(--text)]">Account</h1>

      <div class="tab-bar" role="tablist" aria-label="Account-Einstellungen">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          type="button"
          role="tab"
          :aria-selected="activeTab === tab.key"
          :class="['tab', { 'tab--active': activeTab === tab.key }]"
          @click="activeTab = tab.key"
        >
          {{ tab.label }}
        </button>
      </div>

      <div class="mt-6">
        <ChangeUserNameForm v-if="activeTab === 'username'" />
        <ChangeEmailForm v-else-if="activeTab === 'email'" />
        <ChangePasswordForm v-else />
      </div>
    </div>
  </div>
</template>

<style scoped>
.tab-bar {
  display: flex;
  gap: 0.4rem;
  border-bottom: 1px solid var(--border);
}
.tab {
  flex: 1;
  background: transparent;
  border: 0;
  border-bottom: 2px solid transparent;
  color: var(--muted);
  font-weight: 600;
  font-size: 0.9rem;
  padding: 0.6rem 0.4rem;
  cursor: pointer;
  transition: color 0.15s ease, border-color 0.15s ease;
}
.tab:hover {
  color: var(--text);
}
.tab--active {
  color: var(--coral);
  border-bottom-color: var(--coral);
}
.tab:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}

:deep(h2) {
  font-size: 1.1rem;
  font-weight: 700;
  margin-bottom: 1rem;
  color: var(--text);
}
:deep(.p-inputtext),
:deep(.p-password),
:deep(.p-password-input) {
  width: 100%;
}
:deep(.p-inputtext),
:deep(.p-password-input) {
  background: var(--field);
  border: 1px solid var(--border);
  color: var(--text);
  border-radius: 10px;
  padding: 0.7rem 0.9rem;
}
:deep(.p-inputtext::placeholder),
:deep(.p-password-input::placeholder) {
  color: var(--muted);
}
:deep(.p-inputtext:enabled:focus),
:deep(.p-password-input:enabled:focus) {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
  border-color: var(--teal);
  box-shadow: none;
}
:deep(.p-button) {
  background: var(--coral);
  border: 1px solid var(--coral);
  color: #2a1410;
  font-weight: 600;
  border-radius: 10px;
  padding: 0.7rem 1.1rem;
  justify-content: center;
}
:deep(.p-button:enabled:hover) {
  background: var(--coral);
  border-color: var(--coral);
  filter: brightness(1.07);
}
:deep(.p-button:focus-visible) {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
</style>
