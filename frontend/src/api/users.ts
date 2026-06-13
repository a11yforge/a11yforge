import client from './client'

export interface ChangeUserNameRequestDTO {
  userName: string
}

export interface ChangeEmailRequestDTO {
  email: string
}

export interface ChangePasswordRequestDTO {
  currentPassword: string
  newPassword: string
}

export interface UserResponseDTO {
  id: number
  email: string
  userName: string
  createdAt: string
}

export async function changeUserName(
  request: ChangeUserNameRequestDTO,
): Promise<UserResponseDTO> {
  const response = await client.put<UserResponseDTO>('/users/me/username', request)
  return response.data
}

export async function changeEmail(request: ChangeEmailRequestDTO): Promise<UserResponseDTO> {
  const response = await client.put<UserResponseDTO>('/users/me/email', request)
  return response.data
}

export async function changePassword(request: ChangePasswordRequestDTO): Promise<void> {
  await client.put('/users/me/password', request)
}