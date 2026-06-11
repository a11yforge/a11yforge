import client from './client'

export interface PasswordResetRequestDTO {
  email: string
}

export interface PasswordResetConfirmDTO {
  token: string
  newPassword: string
}

export async function requestPasswordReset(request: PasswordResetRequestDTO): Promise<void> {
  await client.post('/password-reset/request', request)
}

export async function confirmPasswordReset(request: PasswordResetConfirmDTO): Promise<void> {
  await client.post('/password-reset/confirm', request)
}