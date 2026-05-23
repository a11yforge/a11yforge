import client from './client'

export interface ProjectRequestDTO {
  name: string
  baseUrl: string
  crawlMaxPages: number
}

export interface ProjectResponseDTO {
  id: number
  name: string
  baseUrl: string
  crawlMaxPages: number
  createdAt: string
  updatedAt: string
}

export async function getProjects(): Promise<ProjectResponseDTO[]> {
  const response = await client.get<ProjectResponseDTO[]>('/projects')
  return response.data
}

export async function getProjectById(id: number): Promise<ProjectResponseDTO> {
  const response = await client.get<ProjectResponseDTO>(`/projects/${id}`)
  return response.data
}

export async function createProject(request: ProjectRequestDTO): Promise<ProjectResponseDTO> {
  const response = await client.post<ProjectResponseDTO>('/projects', request)
  return response.data
}

export async function updateProject(
  id: number,
  request: ProjectRequestDTO,
): Promise<ProjectResponseDTO> {
  const response = await client.put<ProjectResponseDTO>(`/projects/${id}`, request)
  return response.data
}

export async function deleteProject(id: number): Promise<void> {
  await client.delete(`/projects/${id}`)
}