function statusOf(e: unknown): number | undefined {
  if (
    e &&
    typeof e === 'object' &&
    'response' in e &&
    e.response &&
    typeof e.response === 'object' &&
    'status' in e.response &&
    typeof e.response.status === 'number'
  ) {
    return e.response.status
  }
  return undefined
}

export function extractFieldError(
  e: unknown,
  messages: { conflict?: string; mismatch?: string; fallback: string },
): string {
  const status = statusOf(e)
  if (status === 409 && messages.conflict) {
    return messages.conflict
  }
  if (status === 400 && messages.mismatch) {
    return messages.mismatch
  }
  return messages.fallback
}