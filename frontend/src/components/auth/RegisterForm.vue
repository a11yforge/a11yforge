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
  <div>
    <h2 class="text-xl font-semibold mb-4">Registrieren</h2>

    <form @submit.prevent="handleRegister()" class="flex flex-col gap-4">
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
</template>