<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import ForgotPasswordForm from './ForgotPasswordForm.vue'
import ResetPasswordForm from './ResetPasswordForm.vue'

const route = useRoute()
const hasToken = computed(() => typeof route.query.token === 'string' && route.query.token !== '')
</script>

<template>
  <ResetPasswordForm v-if="hasToken" />
  <ForgotPasswordForm v-else />
</template>