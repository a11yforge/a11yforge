<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const email = ref('')
const userName = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

async function handleRegister() {
  errorMsg.value = ''
  loading.value = true
  try {
    await authStore.register({ email: email.value, password: password.value, userName: userName.value })
  } catch {
    errorMsg.value = 'Registrierung fehlgeschlagen. Bitte überprüfe deine Eingaben.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen">
    <div class="w-full max-w-sm p-8 border border-gray-700 rounded-lg">
      <h1 class="text-2xl font-bold mb-6">A11yForge — Registrieren</h1>

      <form @submit.prevent="handleRegister" class="flex flex-col gap-4">
        <InputText v-model="email" placeholder="Email" type="email" />
        <InputText v-model="userName" placeholder="Username" />
        <Password v-model="password" placeholder="Passwort" :feedback="false" toggleMask />
        <Button type="submit" label="Registrieren" :loading="loading" />
        <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
        <p class="text-sm text-center">
          Bereits ein Konto?
          <router-link to="/login" class="text-blue-400 hover:underline">Login</router-link>
        </p>
      </form>
    </div>
  </div>
</template>