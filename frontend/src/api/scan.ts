import client from './client'

export interface ViolationDTO {
  id: number
  pageId: number
  ruleId: string
  source: string
  impact: string
  htmlSnippet: string
  targetSelector: string
  description: string
}

export interface ScanDetailDTO {
  id: number
  projectId: number
  status: string
  startedAt: string
  completedAt: string | null
  violations: ViolationDTO[]
}

export async function getScan(id: number): Promise<ScanDetailDTO> {
  const scan = await client.get<ScanDetailDTO>(`/scan/` + id)
  return scan.data
}
