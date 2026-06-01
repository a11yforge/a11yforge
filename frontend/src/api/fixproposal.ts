
import client from './client'
export interface FixGenerationStartDTO {
  fixProposalId: number
}

export async function requestFix(violationId: number): Promise<FixGenerationStartDTO> {
  const response = await client.post<FixGenerationStartDTO>("/fix-proposals", { violationId })
  return response.data
}
