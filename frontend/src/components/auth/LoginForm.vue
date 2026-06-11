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
  <div>
    <h2 class="text-xl font-semibold mb-4">Login</h2>

    <form @submit.prevent="handleLogin()" class="flex flex-col gap-4">
      <InputText v-model="identifier" placeholder="Email oder Username" />
      <Password v-model="password" placeholder="Passwort" :feedback="false" toggleMask />
      <Button type="submit" label="Login" :loading="loading" />
      <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>

      <div class="flex flex-col gap-1 text-sm text-center">
        <router-link to="/password-reset" class="text-blue-400 hover:underline">
          Passwort vergessen?
        </router-link>
        <p>
          Noch kein Konto?
          <router-link to="/register" class="text-blue-400 hover:underline">Registrieren</router-link>
        </p>
      </div>
    </form>
  </div>
</template>