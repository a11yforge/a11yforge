<script setup lang="ts">
import ChangeUserNameForm from '@/components/account/ChangeUserNameForm.vue'
import ChangeEmailForm from '@/components/account/ChangeEmailForm.vue'
import ChangePasswordForm from '@/components/account/ChangePasswordForm.vue'
</script>

<template>
  <div class="max-w-3xl mx-auto p-6">
    <h1 class="text-2xl font-bold mb-6">Account / Einstellungen</h1>

    <div class="grid gap-6 md:grid-cols-3">
      <ChangeUserNameForm />
      <ChangeEmailForm />
      <ChangePasswordForm />
    </div>
  </div>
</template>
