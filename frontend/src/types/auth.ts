export type UserStatus =
  | 'PENDING_VERIFICATION'
  | 'ACTIVE'
  | 'SUSPENDED'
  | 'DEACTIVATED'

export interface AuthenticatedUser {
  id: string
  fullName: string
  email: string
  status: UserStatus
  emailVerified: boolean
}

export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  user: AuthenticatedUser
}

export interface SessionStatusResponse {
  authenticated: boolean
  user: AuthenticatedUser | null
}

export interface RegisterRequest {
  fullName: string
  email: string
  cpf: string
  password: string
  passwordConfirmation: string
}

export interface RegisterResponse {
  id: string
  fullName: string
  email: string
  cpfMasked: string
  status: UserStatus
  emailVerified: boolean
  createdAt: string
}

export interface CsrfTokenResponse {
  headerName: string
  parameterName: string
  token: string
}

export interface EmailVerificationRequest {
  token: string
}

export interface EmailVerificationResponse {
  verified: boolean
  message: string
}

export interface EmailVerificationResendRequest {
  email: string
}

export interface EmailVerificationResendResponse {
  message: string
}

export interface ApiFieldError {
  field: string
  message: string
}

export interface ApiErrorResponse {
  timestamp: string
  status: number
  error: string
  message: string
  path: string
  fieldErrors: ApiFieldError[]
}