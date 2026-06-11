<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import Password from 'primevue/password'
import Button from 'primevue/button'
import { confirmPasswordReset } from '@/api/password-reset'

const route = useRoute()
const token = computed(() => {
  const raw = route.query.token
  return typeof raw === 'string' ? raw : ''
})

const newPassword = ref('')
const errorMsg = ref('')
const loading = ref(false)
const success = ref(false)

async function handleConfirm() {
  errorMsg.value = ''

  if (newPassword.value.length < 8) {
    errorMsg.value = 'Das Passwort muss mindestens 8 Zeichen lang sein.'
    return
  }

  loading.value = true
  try {
    await confirmPasswordReset({ token: token.value, newPassword: newPassword.value })
    success.value = true
  } catch {
    errorMsg.value =
      'Das Zurücksetzen ist fehlgeschlagen. Der Link ist möglicherweise abgelaufen oder ungültig.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div>
    <h2 class="text-xl font-semibold mb-4">Neues Passwort</h2>

    <div v-if="success" class="flex flex-col gap-4">
      <p class="text-sm">Dein Passwort wurde erfolgreich geändert.</p>
      <router-link to="/login" class="text-blue-400 hover:underline text-sm text-center">
        Zum Login
      </router-link>
    </div>

    <div v-else-if="!token" class="flex flex-col gap-4">
      <p class="text-red-500 text-sm">
        Kein gültiger Token gefunden. Bitte fordere den Link erneut an.
      </p>
      <router-link to="/password-reset" class="text-blue-400 hover:underline text-sm text-center">
        Passwort vergessen
      </router-link>
    </div>

    <form v-else @submit.prevent="handleConfirm()" class="flex flex-col gap-4">
      <Password v-model="newPassword" placeholder="Neues Passwort" :feedback="false" toggleMask />
      <Button type="submit" label="Passwort speichern" :loading="loading" />
      <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>
    </form>
  </div>
</template>