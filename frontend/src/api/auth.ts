import client from './client'

export interface LoginRequestDTO {
  identifier: string
  password: string
}

export interface RegisterRequestDTO {
  email: string
  password: string
  userName: string
}

export interface TokenResponseDTO {
  accessToken: string
  refreshToken: string
  email: string
  userName: string
}

export async function login(request: LoginRequestDTO): Promise<TokenResponseDTO> {
  const response = await client.post<TokenResponseDTO>('/auth/login', request)
  return response.data
}

export async function register(request: RegisterRequestDTO): Promise<void> {
  await client.post('/auth/register', request)
}

export async function refresh(refreshToken: string): Promise<TokenResponseDTO> {
  const response = await client.post<TokenResponseDTO>('/auth/refresh', { refreshToken })
  return response.data
}

export async function logout(refreshToken: string): Promise<void> {
  await client.post('/auth/logout', { refreshToken })
}
