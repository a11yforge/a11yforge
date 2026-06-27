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
  projectScanNumber: number
}

export interface ScanResponseDTO {
  id: number
  projectId: number
  status: string
  startedAt: string
  completedAt: string | null
  violationCount: number
  projectScanNumber: number
}

export async function getScan(id: number): Promise<ScanDetailDTO> {
  const scan = await client.get<ScanDetailDTO>(`/scan/` + id)
  return scan.data
}

export async function getScanFromProject(projectId: number): Promise<ScanResponseDTO[]> {
  const scan = await client.get<ScanResponseDTO[]>('/scan', { params: { projectId }})
  return scan.data
}

export async function startScan(projectId: number): Promise<ScanResponseDTO> {
  const scan = await client.post<ScanResponseDTO>('/scan', { projectId })
  return scan.data
}

export async function getScanExport(id: number):Promise<unknown[]> {
  const response = await client.get<unknown[]>(`/scan/${id}/export`)
  return response.data
}


