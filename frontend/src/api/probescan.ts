import client from './client'

export interface ProbeViolation {
  ruleId: string
  impact: string
  description: string
  targetSelector: string | null
}

export interface ProbeScanResponse {
  violations: ProbeViolation[]
  totalViolations: number
}

export async function probeScan(url: string): Promise<ProbeScanResponse> {
  const res = await client.post<ProbeScanResponse>('/probescan', { url })
  return res.data
}
