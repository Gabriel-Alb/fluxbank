import { api } from '../../lib/axios'
import type {
  CsrfTokenResponse,
  EmailVerificationRequest,
  EmailVerificationResendRequest,
  EmailVerificationResendResponse,
  EmailVerificationResponse,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  SessionStatusResponse,
} from '../../types/auth'

async function getCsrfToken() {
  const { data } = await api.get<CsrfTokenResponse>('/api/auth/csrf')

  return data.token
}

async function getCsrfHeaders() {
  const csrfToken = await getCsrfToken()

  return {
    'X-XSRF-TOKEN': csrfToken,
  }
}

export async function registerUser(payload: RegisterRequest) {
  const csrfHeaders = await getCsrfHeaders()

  const { data } = await api.post<RegisterResponse>(
    '/api/users',
    payload,
    {
      headers: csrfHeaders,
    },
  )

  return data
}

export async function login(payload: LoginRequest) {
  const csrfHeaders = await getCsrfHeaders()

  const { data } = await api.post<LoginResponse>(
    '/api/auth/login',
    payload,
    {
      headers: csrfHeaders,
    },
  )

  return data
}

export async function logout() {
  const csrfHeaders = await getCsrfHeaders()

  await api.post(
    '/api/auth/logout',
    null,
    {
      headers: csrfHeaders,
    },
  )
}

export async function getSession() {
  const { data } = await api.get<SessionStatusResponse>(
    '/api/auth/session',
  )

  return data
}

export async function getCurrentUser() {
  const { data } = await api.get<SessionStatusResponse>(
    '/api/auth/me',
  )

  return data
}

export async function verifyEmail(payload: EmailVerificationRequest) {
  const csrfHeaders = await getCsrfHeaders()

  const { data } = await api.post<EmailVerificationResponse>(
    '/api/auth/email-verification/verify',
    payload,
    {
      headers: csrfHeaders,
    },
  )

  return data
}

export async function resendEmailVerification(
  payload: EmailVerificationResendRequest,
) {
  const csrfHeaders = await getCsrfHeaders()

  const { data } = await api.post<EmailVerificationResendResponse>(
    '/api/auth/email-verification/resend',
    payload,
    {
      headers: csrfHeaders,
    },
  )

  return data
}