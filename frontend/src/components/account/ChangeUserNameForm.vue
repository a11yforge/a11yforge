<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { useToast } from 'primevue/usetoast'
import { changeUserName } from '@/api/users'
import { useAuthStore } from '@/stores/auth'
import { extractFieldError } from '@/components/account/accountErrors'

const authStore = useAuthStore()
const toast = useToast()

const userName = ref('')
const currentPassword = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleSubmit() {
  errorMsg.value = ''
  loading.value = true
  try {
    const user = await changeUserName({
      userName: userName.value,
      currentPassword: currentPassword.value,
    })
    authStore.setUserInfo(user.email, user.userName)
    userName.value = ''
    currentPassword.value = ''
    toast.add({
      severity: 'success',
      summary: 'Username geändert',
      detail: `Dein Username ist jetzt "${user.userName}".`,
      life: 3000,
    })
  } catch (e: unknown) {
    errorMsg.value = extractFieldError(e, {
      conflict: 'Dieser Username ist bereits vergeben.',
      mismatch: 'Das aktuelle Passwort ist nicht korrekt.',
      fallback: 'Username konnte nicht geändert werden.',
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div>
    <h2>Username ändern</h2>
    <form @submit.prevent="handleSubmit()" class="flex flex-col gap-4">
      <InputText v-model="userName" placeholder="Neuer Username" />
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