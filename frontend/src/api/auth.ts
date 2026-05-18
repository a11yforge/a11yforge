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

export interface LoginResponseDTO {
  token: string
  email: string
  userName: string
}

export async function login(request: LoginRequestDTO): Promise<LoginResponseDTO> {
  const response = await client.post<LoginResponseDTO>('/auth/login', request)
  return response.data
}

export async function register(request: RegisterRequestDTO): Promise<void> {
  await client.post('/auth/register', request)
}
