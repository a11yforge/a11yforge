import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  login as apiLogin,
  register as apiRegister,
  logout as apiLogout,
  refresh as apiRefresh,
} from '@/api/auth'
import type { LoginRequestDTO, RegisterRequestDTO } from '@/api/auth'
import { useProjectsStore } from '@/stores/projects'
import router from '@/router'

const ACCESS_TOKEN_KEY = 'a11yforge_token'
const REFRESH_TOKEN_KEY = 'a11yforge_refresh_token'

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref<string | null>(localStorage.getItem(ACCESS_TOKEN_KEY))
  const refreshToken = ref<string | null>(localStorage.getItem(REFRESH_TOKEN_KEY))
  const email = ref<string | null>(null)
  const userName = ref<string | null>(null)

  const isAuthenticated = computed(() => accessToken.value !== null)

  function setTokens(newAccessToken: string, newRefreshToken: string): void {
    accessToken.value = newAccessToken
    refreshToken.value = newRefreshToken
    localStorage.setItem(ACCESS_TOKEN_KEY, newAccessToken)
    localStorage.setItem(REFRESH_TOKEN_KEY, newRefreshToken)
  }

  function setUserInfo(newEmail: string, newUserName: string): void {
    email.value = newEmail
    userName.value = newUserName
  }

  function clearAuth(): void {
    accessToken.value = null
    refreshToken.value = null
    email.value = null
    userName.value = null
    localStorage.removeItem(ACCESS_TOKEN_KEY)
    localStorage.removeItem(REFRESH_TOKEN_KEY)
  }

  async function login(request: LoginRequestDTO): Promise<void> {
    const response = await apiLogin(request)
    setTokens(response.accessToken, response.refreshToken)
    email.value = response.email
    userName.value = response.userName
    await router.push('/projects')
  }

  async function register(request: RegisterRequestDTO): Promise<void> {
    await apiRegister(request)
    await router.push('/login')
  }

  async function refreshTokens(): Promise<string> {
    if (!refreshToken.value) {
      throw new Error('No refresh token available')
    }
    const response = await apiRefresh(refreshToken.value)
    setTokens(response.accessToken, response.refreshToken)
    email.value = response.email
    userName.value = response.userName
    return response.accessToken
  }

  async function logout(): Promise<void> {
    try {
      if (refreshToken.value) {
        await apiLogout(refreshToken.value)
      }
    } catch {
      // Backend-Call ist best effort — Logout muss immer lokal durchgehen,
      // auch wenn das Backend nicht erreichbar ist oder der Token bereits
      // revoked/abgelaufen ist.
    }

    clearAuth()

    const projectsStore = useProjectsStore()
    projectsStore.reset()

    router.push('/login')
  }

  return {
    accessToken,
    refreshToken,
    email,
    userName,
    isAuthenticated,
    login,
    register,
    refreshTokens,
    logout,
    setTokens,
    clearAuth,
    setUserInfo,
  }
})