<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const identifier = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleLogin() {
  errorMsg.value = ''
  loading.value = true
  try {
    await authStore.login({ identifier: identifier.value, password: password.value })
  } catch {
    errorMsg.value = 'Login fehlgeschlagen. Bitte überprüfe deine Eingaben.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <div class="w-full max-w-sm p-8 border border-gray-700 rounded-lg">
      <h1 class="text-2xl font-bold mb-6">A11yForge — Login</h1>

      <form @submit.prevent="handleLogin" class="flex flex-col gap-4">
        <InputText v-model="identifier" placeholder="Email oder Username" />
        <Password v-model="password" placeholder="Passwort" :feedback="false" toggleMask />
        <Button type="submit" label="Login" :loading="loading" />
        <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
        <p class="text-sm text-center">
          Noch kein Konto?
          <router-link to="/register" class="text-blue-400 hover:underline">Registrieren</router-link>
        </p>
      </form>
    </div>
  </div>
</template>
