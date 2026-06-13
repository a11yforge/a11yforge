<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { useToast } from 'primevue/usetoast'
import { changeUserName } from '@/api/users'
import { useAuthStore } from '@/stores/auth'
import { extractFieldError } from '@/components/account/accountErrors'

const authStore = useAuthStore()
const toast = useToast()

const userName = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleSubmit() {
  errorMsg.value = ''
  loading.value = true
  try {
    const user = await changeUserName({ userName: userName.value })
    authStore.setUserInfo(user.email, user.userName)
    userName.value = ''
    toast.add({
      severity: 'success',
      summary: 'Username geändert',
      detail: `Dein Username ist jetzt "${user.userName}".`,
      life: 3000,
    })
  } catch (e: unknown) {
    errorMsg.value = extractFieldError(e, {
      conflict: 'Dieser Username ist bereits vergeben.',
      fallback: 'Username konnte nicht geändert werden.',
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="flex flex-col gap-3">
    <h2 class="text-lg font-semibold">Username ändern</h2>
    <form @submit.prevent="handleSubmit()" class="flex flex-col gap-3">
      <InputText v-model="userName" placeholder="Neuer Username" />
      <Button type="submit" label="Speichern" :loading="loading" />
      <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
    </form>
  </section>
</template>