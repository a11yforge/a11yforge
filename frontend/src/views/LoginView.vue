<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { login } from '@/api/auth'

const email = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)
const router = useRouter()

async function handleLogin() {
  errorMsg.value = ''
  loading.value = true
  try {
    const result = await login({ email: email.value, password: password.value })
    localStorage.setItem('a11yforge_token', result.token)
    router.push('/projects')
  } catch {
    errorMsg.value = 'Login fehlgeschlagen'
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
        <InputText v-model="email" placeholder="Email" type="email" />
        <Password v-model="password" placeholder="Passwort" :feedback="false" toggleMask />
        <Button type="submit" label="Login" :loading="loading" />
        <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
      </form>
    </div>
  </div>
</template>
