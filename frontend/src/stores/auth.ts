import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, register as apiRegister } from '@/api/auth'
import type { LoginRequestDTO, RegisterRequestDTO } from '@/api/auth'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('a11yforge_token'))
  const email = ref<string | null>(null)
  const userName = ref<string | null>(null)

  const isAuthenticated = computed(() => token.value !== null)

  async function login(request: LoginRequestDTO): Promise<void> {
    const response = await apiLogin(request)
    token.value = response.token
    email.value = response.email
    userName.value = response.userName
    localStorage.setItem('a11yforge_token', response.token)
    await router.push('/projects')
  }

  async function register(request: RegisterRequestDTO): Promise<void> {
    await apiRegister(request)
    await router.push('/login')
  }

  function logout(): void {
    token.value = null
    email.value = null
    userName.value = null
    localStorage.removeItem('a11yforge_token')
    router.push('/login')
  }

  return { token, email, userName, isAuthenticated, login, register, logout }
})