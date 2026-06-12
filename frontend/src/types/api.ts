export interface ApiResponse<T> {
  data: T
  message: string
  success: boolean
}

export interface PaginatedResponse<T> {
  data: T[]
  message: string
  success: boolean
  pagination: {
    page: number
    size: number
    totalElements: number
    totalPages: number
  }
}


export interface ApiError {
  message: string
  status: number
  errors?: Record<string, string> // erros de validação campo a campo
}