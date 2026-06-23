import { useMemo } from 'react'
import {
  mdiAlertCircleOutline,
  mdiCheckCircleOutline,
  mdiEmailCheckOutline,
  mdiLoading,
} from '@mdi/js'
import { useMutation } from '@tanstack/react-query'
import type { AxiosError } from 'axios'
import { Link, useSearchParams } from 'react-router-dom'
import { verifyEmail } from '../../features/auth/authApi'
import type { ApiErrorResponse } from '../../types/auth'
import { AuthLayout } from '../../components/auth/AuthLayout'
import { AuthVisualPanel } from '../../components/auth/AuthVisualPanel'
import { AppIcon } from '../../components/ui/AppIcon'

function getVerifyEmailErrorMessage(error: unknown) {
  const axiosError = error as AxiosError<ApiErrorResponse>
  const apiMessage = axiosError.response?.data?.message

  if (!apiMessage) {
    return 'Não foi possível verificar seu e-mail agora. Tente novamente.'
  }

  if (apiMessage === 'Invalid email verification token') {
    return 'O link de verificação é inválido.'
  }

  if (apiMessage === 'Email verification token has expired') {
    return 'O link de verificação expirou. Solicite um novo link.'
  }

  if (apiMessage === 'Email verification token has already been used') {
    return 'Este link de verificação já foi utilizado.'
  }

  if (apiMessage === 'Email verification token has been revoked') {
    return 'Este link de verificação foi substituído por um novo.'
  }

  return apiMessage
}

export function VerifyEmailPage() {
  const [searchParams] = useSearchParams()

  const token = useMemo(() => {
    return searchParams.get('token')?.trim() ?? ''
  }, [searchParams])

  const verifyEmailMutation = useMutation({
    mutationFn: () => verifyEmail({ token }),
  })

  const errorMessage = useMemo(() => {
    if (!verifyEmailMutation.error) {
      return null
    }

    return getVerifyEmailErrorMessage(verifyEmailMutation.error)
  }, [verifyEmailMutation.error])

  const hasToken = token.length > 0
  const isSuccess = verifyEmailMutation.isSuccess
  const isLoading = verifyEmailMutation.isPending

  return (
    <AuthLayout visualPanel={<AuthVisualPanel />}>
      <div className="w-full max-w-[410px]">
        <header>
          <div className="mb-5 flex h-12 w-12 items-center justify-center rounded-2xl bg-[#1C274C]/10 text-[#1C274C]">
            <AppIcon
              path={mdiEmailCheckOutline}
              size={1.15}
            />
          </div>

          <h1 className="text-[clamp(2rem,4vw,2.65rem)] font-semibold leading-[1.08] tracking-[-0.045em] text-[#15182a]">
            Verificação de e-mail
          </h1>

          <p className="mt-3 max-w-[370px] text-sm leading-6 text-slate-500">
            Confirme seu e-mail para ativar sua conta e liberar o acesso ao FluxBank.
          </p>
        </header>

        <div className="mt-8 space-y-4">
          {!hasToken && (
            <div
              role="alert"
              className="flex items-start gap-2 rounded-xl border border-red-200 bg-red-50 px-3 py-2.5 text-sm font-medium text-red-700"
            >
              <span
                aria-hidden="true"
                className="mt-0.5 shrink-0"
              >
                <AppIcon
                  path={mdiAlertCircleOutline}
                  size={0.9}
                />
              </span>

              <span>
                Token de verificação não encontrado no link.
              </span>
            </div>
          )}

          {errorMessage && (
            <div
              role="alert"
              className="flex items-start gap-2 rounded-xl border border-red-200 bg-red-50 px-3 py-2.5 text-sm font-medium text-red-700"
            >
              <span
                aria-hidden="true"
                className="mt-0.5 shrink-0"
              >
                <AppIcon
                  path={mdiAlertCircleOutline}
                  size={0.9}
                />
              </span>

              <span>{errorMessage}</span>
            </div>
          )}

          {isSuccess && (
            <div
              role="status"
              className="flex items-start gap-2 rounded-xl border border-emerald-200 bg-emerald-50 px-3 py-2.5 text-sm font-medium text-emerald-700"
            >
              <span
                aria-hidden="true"
                className="mt-0.5 shrink-0"
              >
                <AppIcon
                  path={mdiCheckCircleOutline}
                  size={0.9}
                />
              </span>

              <span>
                E-mail verificado com sucesso. Agora você já pode fazer login.
              </span>
            </div>
          )}

          {!isSuccess && (
            <button
              type="button"
              disabled={!hasToken || isLoading}
              onClick={() => {
                verifyEmailMutation.mutate()
              }}
              className="flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-[#1C274C] px-4 text-sm font-semibold text-white shadow-[0_12px_24px_rgba(28,39,76,0.18)] outline-none transition-[background-color,transform,box-shadow,opacity] hover:bg-[#26345e] active:scale-[0.99] disabled:cursor-not-allowed disabled:opacity-60"
            >
              {isLoading && (
                <AppIcon
                  path={mdiLoading}
                  size={0.9}
                  spin
                />
              )}

              {isLoading ? 'Verificando...' : 'Verificar e-mail'}
            </button>
          )}

          <div className="space-y-3 text-center">
            <Link
              to="/login"
              className="inline-flex text-sm font-semibold text-[#1C274C] underline-offset-4 transition-colors hover:text-[#26345e] hover:underline"
            >
              Ir para o login
            </Link>

            <p className="text-sm text-slate-500">
              Link expirado?{' '}
              <Link
                to="/resend-verification"
                className="font-semibold text-[#1C274C] underline-offset-4 transition-colors hover:text-[#26345e] hover:underline"
              >
                Reenviar verificação
              </Link>
            </p>
          </div>
        </div>
      </div>
    </AuthLayout>
  )
}