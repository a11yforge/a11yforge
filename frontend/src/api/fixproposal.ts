
import client from './client'
export interface FixGenerationStartDTO {
  fixProposalId: number
}

export async function requestFix(violationId: number): Promise<FixGenerationStartDTO> {
  const response = await client.post<FixGenerationStartDTO>("/fix-proposals", { violationId })
  return response.data

}
export interface FixProposalDTO {
  id: number
  violationId: number
  status: string
  generatedHtml: string | null
  llmProvider: string
  llmModel: string | null
  promptVersion: string | null
}

export async function getFixProposal(id: number): Promise<FixProposalDTO> {
  const response = await client.get<FixProposalDTO>('/fix-proposals/' + id)
  return response.data
}
