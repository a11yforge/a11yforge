<script setup lang="ts">
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { requestPasswordReset } from '@/api/password-reset'

const email = ref('')
const loading = ref(false)
const submitted = ref(false)

async function handleRequest() {
  loading.value = true
  try {
    await requestPasswordReset({ email: email.value })
  } finally {
    loading.value = false
    submitted.value = true
  }
}
</script>

<template>
  <div>
    <h2 class="text-xl font-semibold mb-4">Passwort vergessen</h2>

    <div v-if="submitted" class="flex flex-col gap-4">
      <p class="text-sm">
        Falls ein Konto mit dieser E-Mail existiert, wurde ein Link zum Zurücksetzen
        des Passworts versendet. Bitte prüfe dein Postfach.
      </p>
      <router-link to="/login" class="text-blue-400 hover:underline text-sm text-center">
        Zurück zum Login
      </router-link>
    </div>

    <form v-else @submit.prevent="handleRequest()" class="flex flex-col gap-4">
      <InputText v-model="email" placeholder="Email" type="email" />
      <Button type="submit" label="Link senden" :loading="loading" />
      <p class="text-sm text-center">
        <router-link to="/login" class="text-blue-400 hover:underline">Zurück zum Login</router-link>
      </p>
    </form>
  </div>
</template>