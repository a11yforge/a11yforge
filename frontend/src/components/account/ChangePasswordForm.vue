<script setup lang="ts">
import { ref } from 'vue'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { useToast } from 'primevue/usetoast'
import { changePassword } from '@/api/users'
import { useAuthStore } from '@/stores/auth'
import { extractFieldError } from '@/components/account/accountErrors'

const authStore = useAuthStore()
const toast = useToast()

const currentPassword = ref('')
const newPassword = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleSubmit() {
  errorMsg.value = ''

  if (newPassword.value.length < 8) {
    errorMsg.value = 'Das neue Passwort muss mindestens 8 Zeichen lang sein.'
    return
  }

  loading.value = true
  try {
    await changePassword({
      currentPassword: currentPassword.value,
      newPassword: newPassword.value,
    })
    toast.add({
      severity: 'success',
      summary: 'Passwort geändert',
      detail: 'Bitte melde dich mit deinem neuen Passwort an.',
      life: 3000,
    })
    await authStore.logout()
  } catch (e: unknown) {
    errorMsg.value = extractFieldError(e, {
      mismatch: 'Das aktuelle Passwort ist nicht korrekt.',
      fallback: 'Passwort konnte nicht geändert werden.',
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="flex flex-col gap-3">
    <h2 class="text-lg font-semibold">Passwort ändern</h2>
    <form @submit.prevent="handleSubmit()" class="flex flex-col gap-3">
      <Password
        v-model="currentPassword"
        placeholder="Aktuelles Passwort"
        :feedback="false"
        toggleMask
      />
      <Password
        v-model="newPassword"
        placeholder="Neues Passwort"
        :feedback="false"
        toggleMask
      />
      <Button type="submit" label="Speichern" :loading="loading" />
      <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
    </form>
  </section>
</template>