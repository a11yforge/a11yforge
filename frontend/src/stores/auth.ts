import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, register as apiRegister, logout as apiLogout } from '@/api/auth'
import type { LoginRequestDTO, RegisterRequestDTO } from '@/api/auth'
import { useProjectsStore } from '@/stores/projects'
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
    await router.push('/home')
  }

  async function register(request: RegisterRequestDTO): Promise<void> {
    await apiRegister(request)
    await router.push('/login')
  }

  async function logout(): Promise<void> {
    try {
      await apiLogout()
    } catch {
      // Backend-Call ist best effort — Logout muss immer durchgehen,
      // auch wenn das Backend nicht erreichbar ist oder das Token bereits
      // abgelaufen ist (in welchem Fall der 401-Interceptor sowieso greift).
    }

    token.value = null
    email.value = null
    userName.value = null
    localStorage.removeItem('a11yforge_token')

    const projectsStore = useProjectsStore()
    projectsStore.reset()

    router.push('/login')
  }

  return { token, email, userName, isAuthenticated, login, register, logout }
})