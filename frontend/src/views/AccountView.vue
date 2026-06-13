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
  <div class="page">
    <h1 class="title">Account / Einstellungen</h1>

    <div class="grid">
      <div class="card"><ChangeUserNameForm /></div>
      <div class="card"><ChangeEmailForm /></div>
      <div class="card"><ChangePasswordForm /></div>
    </div>

    <button class="btn btn--danger logout" type="button" @click="handleLogout">Logout</button>
  </div>
</template>

<style scoped>
.page {
  max-width: 980px;
  margin: 0 auto;
  padding: 1.5rem 1.5rem 2rem;
  color: var(--text);
  font-family: ui-sans-serif, system-ui, sans-serif;
}
.title {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--text);
  margin: 0 0 1.2rem;
}

.grid {
  display: grid;
  gap: 1rem;
  grid-template-columns: 1fr;
}
@media (min-width: 760px) {
  .grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

.card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 1.2rem 1.3rem;
}

.btn {
  border: 1px solid transparent;
  border-radius: 10px;
  padding: 0.6rem 1.2rem;
  font-weight: 600;
  font-size: 0.9rem;
  cursor: pointer;
}
.btn:focus-visible {
  outline: 2px solid var(--teal);
  outline-offset: 2px;
}
.btn--danger {
  background: transparent;
  color: #f6a3a3;
  border-color: #5a2a2a;
}
.btn--danger:hover {
  background: #3a1a1a;
}
.logout {
  margin-top: 1.2rem;
}

/* Philipps Form-Komponenten an den Sunset-Look angleichen */
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
