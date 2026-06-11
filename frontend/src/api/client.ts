import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios'
import { useAuthStore } from '@/stores/auth'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

// Pfade, die niemals einen Token-Refresh auslösen dürfen
const AUTH_PATHS = ['/auth/login', '/auth/register', '/auth/refresh', '/auth/logout']

function isAuthPath(url: string | undefined): boolean {
  if (!url) return false
  return AUTH_PATHS.some((path) => url.includes(path))
}

client.interceptors.request.use((config) => {
  const token = localStorage.getItem('a11yforge_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Bündelung: läuft gerade ein Refresh, warten alle weiteren 401s auf dasselbe Promise
let refreshPromise: Promise<string> | null = null

client.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

    // Nur auf 401 reagieren, und nicht auf Auth-Pfaden, und nur einmal pro Request
    if (
      error.response?.status !== 401 ||
      isAuthPath(originalRequest?.url) ||
      originalRequest?._retry
    ) {
      return Promise.reject(error)
    }

    originalRequest._retry = true
    const authStore = useAuthStore()

    try {
      // Nur ein Refresh gleichzeitig — parallele 401s teilen sich das Promise
      if (!refreshPromise) {
        refreshPromise = authStore.refreshTokens()
      }
      const newAccessToken = await refreshPromise
      refreshPromise = null

      // Original-Request mit neuem Token wiederholen
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
      return client(originalRequest)
    } catch (refreshError) {
      refreshPromise = null
      // Refresh gescheitert -> Session ist endgültig vorbei
      authStore.clearAuth()
      window.location.href = '/login'
      return Promise.reject(refreshError)
    }
  },
)

export default client
