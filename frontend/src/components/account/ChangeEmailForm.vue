<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { useToast } from 'primevue/usetoast'
import { changeEmail } from '@/api/users'
import { useAuthStore } from '@/stores/auth'
import { extractFieldError } from '@/components/account/accountErrors'

const authStore = useAuthStore()
const toast = useToast()

const email = ref('')
const currentPassword = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleSubmit() {
  errorMsg.value = ''
  loading.value = true
  try {
    const user = await changeEmail({
      email: email.value,
      currentPassword: currentPassword.value,
    })
    authStore.setUserInfo(user.email, user.userName)
    email.value = ''
    currentPassword.value = ''
    toast.add({
      severity: 'success',
      summary: 'E-Mail geändert',
      detail: `Deine E-Mail ist jetzt "${user.email}".`,
      life: 3000,
    })
  } catch (e: unknown) {
    errorMsg.value = extractFieldError(e, {
      conflict: 'Diese E-Mail ist bereits vergeben.',
      mismatch: 'Das aktuelle Passwort ist nicht korrekt.',
      fallback: 'E-Mail konnte nicht geändert werden.',
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div>
    <h2>E-Mail ändern</h2>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-4">
      <InputText v-model="email" type="email" placeholder="Neue E-Mail" />
      <Password
        v-model="currentPassword"
        placeholder="Aktuelles Passwort"
        :feedback="false"
        toggleMask
      />
      <Button type="submit" label="Speichern" :loading="loading" />
      <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
    </form>
  </div>
</template>