import client from './client'

interface LoginRequest {
  email: string
  password: string
}

interface LoginResponse {
  token: string
}

export async function login(loginInfo: LoginRequest): Promise<LoginResponse> {
  const response = await client.post<LoginResponse>('/auth/login', loginInfo)
  return response.data
}
