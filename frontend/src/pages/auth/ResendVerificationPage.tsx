import { useMemo, useState } from 'react'
import { zodResolver } from '@hookform/resolvers/zod'
import {
  mdiAlertCircleOutline,
  mdiCheckCircleOutline,
  mdiEmailOutline,
  mdiLoading,
  mdiSendOutline,
} from '@mdi/js'
import { useMutation } from '@tanstack/react-query'
import type { AxiosError } from 'axios'
import { Link } from 'react-router-dom'
import { useForm, type SubmitHandler } from 'react-hook-form'
import { z } from 'zod'
import { resendEmailVerification } from '../../features/auth/authApi'
import type { ApiErrorResponse } from '../../types/auth'
import { AuthLayout } from '../../components/auth/AuthLayout'
import { AuthVisualPanel } from '../../components/auth/AuthVisualPanel'
import { AppIcon } from '../../components/ui/AppIcon'

const resendVerificationSchema = z.object({
  email: z
    .string()
    .min(1, 'E-mail obrigatório')
    .email('E-mail inválido'),
})

type ResendVerificationFormData = z.infer<
  typeof resendVerificationSchema
>

function getResendVerificationErrorMessage(error: unknown) {
  const axiosError = error as AxiosError<ApiErrorResponse>
  const apiMessage = axiosError.response?.data?.message

  if (!apiMessage) {
    return 'Não foi possível reenviar a verificação agora. Tente novamente.'
  }

  if (apiMessage === 'Validation failed') {
    return 'Informe um e-mail válido.'
  }

  return apiMessage
}

export function ResendVerificationPage() {
  const [successMessage, setSuccessMessage] = useState<string | null>(null)

  const resendMutation = useMutation({
    mutationFn: resendEmailVerification,
    onSuccess: (response) => {
      setSuccessMessage(response.message)
    },
  })

  const {
    register,
    handleSubmit,
    formState: {
      errors,
      isSubmitting,
    },
  } = useForm<ResendVerificationFormData>({
    resolver: zodResolver(resendVerificationSchema),
    defaultValues: {
      email: '',
    },
  })

  const isLoading = isSubmitting || resendMutation.isPending

  const submitErrorMessage = useMemo(() => {
    if (!resendMutation.error) {
      return null
    }

    return getResendVerificationErrorMessage(resendMutation.error)
  }, [resendMutation.error])

  const onSubmit: SubmitHandler<ResendVerificationFormData> = async (data) => {
    setSuccessMessage(null)

    try {
      await resendMutation.mutateAsync({
        email: data.email,
      })
    } catch {
      // O erro é tratado visualmente pelo React Query.
    }
  }

  return (
    <AuthLayout visualPanel={<AuthVisualPanel />}>
      <div className="w-full max-w-[410px]">
        <header>
          <div className="mb-5 flex h-12 w-12 items-center justify-center rounded-2xl bg-[#1C274C]/10 text-[#1C274C]">
            <AppIcon
              path={mdiSendOutline}
              size={1.1}
            />
          </div>

          <h1 className="text-[clamp(2rem,4vw,2.65rem)] font-semibold leading-[1.08] tracking-[-0.045em] text-[#15182a]">
            Reenviar verificação
          </h1>

          <p className="mt-3 max-w-[370px] text-sm leading-6 text-slate-500">
            Informe seu e-mail para solicitar um novo link de verificação.
          </p>
        </header>

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="mt-8 space-y-5"
          noValidate
        >
          {submitErrorMessage && (
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

              <span>{submitErrorMessage}</span>
            </div>
          )}

          {successMessage && (
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

              <span>{successMessage}</span>
            </div>
          )}

          <div>
            <label
              htmlFor="resend-verification-email"
              className="mb-2 block text-sm font-medium text-slate-700"
            >
              E-mail
            </label>

            <div className="relative">
              <span
                aria-hidden="true"
                className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
              >
                <AppIcon
                  path={mdiEmailOutline}
                  size={0.85}
                />
              </span>

              <input
                id="resend-verification-email"
                type="email"
                autoComplete="email"
                placeholder="seuemail@exemplo.com"
                aria-invalid={Boolean(errors.email)}
                disabled={isLoading}
                {...register('email')}
                className={`
                  h-12 w-full rounded-xl border bg-white pl-10 pr-4
                  text-sm text-slate-900 outline-none
                  transition-[border-color,box-shadow,background-color]
                  placeholder:text-slate-400
                  hover:border-slate-300
                  focus:border-[#1C274C]
                  focus:ring-4 focus:ring-[#1C274C]/10
                  disabled:cursor-not-allowed
                  disabled:bg-slate-100
                  disabled:text-slate-500
                  ${
                    errors.email
                      ? 'border-red-400 focus:border-red-500 focus:ring-red-500/10'
                      : 'border-slate-200'
                  }
                `}
              />
            </div>

            {errors.email && (
              <p
                role="alert"
                className="mt-1.5 text-xs font-medium text-red-600"
              >
                {errors.email.message}
              </p>
            )}
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-[#1C274C] px-4 text-sm font-semibold text-white shadow-[0_12px_24px_rgba(28,39,76,0.18)] outline-none transition-[background-color,transform,box-shadow,opacity] hover:bg-[#26345e] active:scale-[0.99] disabled:cursor-not-allowed disabled:opacity-60"
          >
            {isLoading && (
              <AppIcon
                path={mdiLoading}
                size={0.9}
                spin
              />
            )}

            {isLoading ? 'Enviando...' : 'Reenviar verificação'}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-500">
          Lembrou sua senha?{' '}
          <Link
            to="/login"
            className="font-semibold text-[#1C274C] underline-offset-4 transition-colors hover:text-[#26345e] hover:underline"
          >
            Entrar
          </Link>
        </p>
      </div>
    </AuthLayout>
  )
}