import { useMemo, useState } from 'react'
import { zodResolver } from '@hookform/resolvers/zod'
import {
  mdiAccountOutline,
  mdiAlertCircleOutline,
  mdiCardAccountDetailsOutline,
  mdiCheckCircleOutline,
  mdiEmailOutline,
  mdiEyeOffOutline,
  mdiEyeOutline,
  mdiLoading,
  mdiLockOutline,
} from '@mdi/js'
import { useMutation } from '@tanstack/react-query'
import type { AxiosError } from 'axios'
import { Link } from 'react-router-dom'
import { useForm, type SubmitHandler } from 'react-hook-form'
import { z } from 'zod'
import { registerUser } from '../../features/auth/authApi'
import type { ApiErrorResponse } from '../../types/auth'
import { AppIcon } from '../ui/AppIcon'

const registerSchema = z
  .object({
    fullName: z
      .string()
      .min(1, 'Nome obrigatório')
      .min(3, 'Nome deve ter no mínimo 3 caracteres')
      .max(120, 'Nome deve ter no máximo 120 caracteres'),
    email: z
      .string()
      .min(1, 'E-mail obrigatório')
      .email('E-mail inválido'),
    cpf: z
      .string()
      .min(1, 'CPF obrigatório')
      .regex(
        /^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/,
        'CPF inválido',
      ),
    password: z
      .string()
      .min(1, 'Senha obrigatória')
      .min(8, 'Senha deve ter no mínimo 8 caracteres')
      .max(64, 'Senha deve ter no máximo 64 caracteres')
      .regex(/[A-Z]/, 'Senha deve ter pelo menos uma letra maiúscula')
      .regex(/[0-9]/, 'Senha deve ter pelo menos um número')
      .regex(
        /[^A-Za-z0-9]/,
        'Senha deve ter pelo menos um caractere especial',
      )
      .regex(/^\S+$/, 'Senha não pode conter espaços'),
    passwordConfirmation: z
      .string()
      .min(1, 'Confirmação de senha obrigatória'),
  })
  .refine(
    (data) => data.password === data.passwordConfirmation,
    {
      path: ['passwordConfirmation'],
      message: 'As senhas não conferem',
    },
  )

type RegisterFormData = z.infer<typeof registerSchema>

function getRegisterErrorMessage(error: unknown) {
  const axiosError = error as AxiosError<ApiErrorResponse>
  const apiMessage = axiosError.response?.data?.message

  if (!apiMessage) {
    return 'Não foi possível criar sua conta agora. Verifique sua conexão e tente novamente.'
  }

  if (apiMessage === 'Email is already registered') {
    return 'Este e-mail já está cadastrado.'
  }

  if (apiMessage === 'CPF is already registered') {
    return 'Este CPF já está cadastrado.'
  }

  if (apiMessage === 'Password confirmation does not match') {
    return 'As senhas não conferem.'
  }

  if (apiMessage === 'Validation failed') {
    return 'Verifique os dados informados e tente novamente.'
  }

  return apiMessage
}

export function RegisterForm() {
  const [showPassword, setShowPassword] = useState(false)
  const [showPasswordConfirmation, setShowPasswordConfirmation] =
    useState(false)
  const [successMessage, setSuccessMessage] = useState<string | null>(null)

  const registerMutation = useMutation({
    mutationFn: registerUser,
    onSuccess: () => {
      setSuccessMessage(
        'Conta criada com sucesso. Agora verifique seu e-mail antes de fazer login.',
      )
    },
  })

  const {
    register,
    handleSubmit,
    reset,
    formState: {
      errors,
      isSubmitting,
    },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      fullName: '',
      email: '',
      cpf: '',
      password: '',
      passwordConfirmation: '',
    },
  })

  const isLoading = isSubmitting || registerMutation.isPending

  const submitErrorMessage = useMemo(() => {
    if (!registerMutation.error) {
      return null
    }

    return getRegisterErrorMessage(registerMutation.error)
  }, [registerMutation.error])

  const onSubmit: SubmitHandler<RegisterFormData> = async (data) => {
    setSuccessMessage(null)

    try {
      await registerMutation.mutateAsync({
        fullName: data.fullName,
        email: data.email,
        cpf: data.cpf,
        password: data.password,
        passwordConfirmation: data.passwordConfirmation,
      })

      reset()
    } catch {
      // O erro é tratado visualmente pelo React Query.
    }
  }

  return (
    <div className="w-full max-w-[430px]">
      <header>
        <h1 className="text-[clamp(2rem,4vw,2.65rem)] font-semibold leading-[1.08] tracking-[-0.045em] text-[#15182a]">
          Crie sua conta
        </h1>

        <p className="mt-3 max-w-[370px] text-sm leading-6 text-slate-500">
          Cadastre-se para começar a usar sua conta digital com segurança.
        </p>
      </header>

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="mt-7 space-y-4"
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
            htmlFor="register-full-name"
            className="mb-2 block text-sm font-medium text-slate-700"
          >
            Nome completo
          </label>

          <div className="relative">
            <span
              aria-hidden="true"
              className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
            >
              <AppIcon
                path={mdiAccountOutline}
                size={0.85}
              />
            </span>

            <input
              id="register-full-name"
              type="text"
              autoComplete="name"
              placeholder="Seu nome completo"
              aria-invalid={Boolean(errors.fullName)}
              disabled={isLoading}
              {...register('fullName')}
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
                  errors.fullName
                    ? 'border-red-400 focus:border-red-500 focus:ring-red-500/10'
                    : 'border-slate-200'
                }
              `}
            />
          </div>

          {errors.fullName && (
            <p
              role="alert"
              className="mt-1.5 text-xs font-medium text-red-600"
            >
              {errors.fullName.message}
            </p>
          )}
        </div>

        <div>
          <label
            htmlFor="register-email"
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
              id="register-email"
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

        <div>
          <label
            htmlFor="register-cpf"
            className="mb-2 block text-sm font-medium text-slate-700"
          >
            CPF
          </label>

          <div className="relative">
            <span
              aria-hidden="true"
              className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
            >
              <AppIcon
                path={mdiCardAccountDetailsOutline}
                size={0.85}
              />
            </span>

            <input
              id="register-cpf"
              type="text"
              inputMode="numeric"
              autoComplete="off"
              placeholder="000.000.000-00"
              aria-invalid={Boolean(errors.cpf)}
              disabled={isLoading}
              {...register('cpf')}
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
                  errors.cpf
                    ? 'border-red-400 focus:border-red-500 focus:ring-red-500/10'
                    : 'border-slate-200'
                }
              `}
            />
          </div>

          {errors.cpf && (
            <p
              role="alert"
              className="mt-1.5 text-xs font-medium text-red-600"
            >
              {errors.cpf.message}
            </p>
          )}
        </div>

        <div>
          <label
            htmlFor="register-password"
            className="mb-2 block text-sm font-medium text-slate-700"
          >
            Senha
          </label>

          <div className="relative">
            <span
              aria-hidden="true"
              className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
            >
              <AppIcon
                path={mdiLockOutline}
                size={0.85}
              />
            </span>

            <input
              id="register-password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="new-password"
              placeholder="Crie uma senha"
              aria-invalid={Boolean(errors.password)}
              disabled={isLoading}
              {...register('password')}
              className={`
                h-12 w-full rounded-xl border bg-white pl-10 pr-12
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
                  errors.password
                    ? 'border-red-400 focus:border-red-500 focus:ring-red-500/10'
                    : 'border-slate-200'
                }
              `}
            />

            <button
              type="button"
              onClick={() => {
                setShowPassword((currentValue) => !currentValue)
              }}
              aria-label={
                showPassword
                  ? 'Ocultar senha'
                  : 'Mostrar senha'
              }
              disabled={isLoading}
              className="absolute right-2.5 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg text-slate-400 outline-none transition-colors hover:bg-slate-100 hover:text-slate-700 focus-visible:ring-2 focus-visible:ring-[#1C274C]/30 disabled:cursor-not-allowed disabled:opacity-50"
            >
              <AppIcon
                path={
                  showPassword
                    ? mdiEyeOffOutline
                    : mdiEyeOutline
                }
                size={0.9}
              />
            </button>
          </div>

          {errors.password && (
            <p
              role="alert"
              className="mt-1.5 text-xs font-medium text-red-600"
            >
              {errors.password.message}
            </p>
          )}
        </div>

        <div>
          <label
            htmlFor="register-password-confirmation"
            className="mb-2 block text-sm font-medium text-slate-700"
          >
            Confirmar senha
          </label>

          <div className="relative">
            <span
              aria-hidden="true"
              className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
            >
              <AppIcon
                path={mdiLockOutline}
                size={0.85}
              />
            </span>

            <input
              id="register-password-confirmation"
              type={showPasswordConfirmation ? 'text' : 'password'}
              autoComplete="new-password"
              placeholder="Confirme sua senha"
              aria-invalid={Boolean(errors.passwordConfirmation)}
              disabled={isLoading}
              {...register('passwordConfirmation')}
              className={`
                h-12 w-full rounded-xl border bg-white pl-10 pr-12
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
                  errors.passwordConfirmation
                    ? 'border-red-400 focus:border-red-500 focus:ring-red-500/10'
                    : 'border-slate-200'
                }
              `}
            />

            <button
              type="button"
              onClick={() => {
                setShowPasswordConfirmation(
                  (currentValue) => !currentValue,
                )
              }}
              aria-label={
                showPasswordConfirmation
                  ? 'Ocultar confirmação de senha'
                  : 'Mostrar confirmação de senha'
              }
              disabled={isLoading}
              className="absolute right-2.5 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg text-slate-400 outline-none transition-colors hover:bg-slate-100 hover:text-slate-700 focus-visible:ring-2 focus-visible:ring-[#1C274C]/30 disabled:cursor-not-allowed disabled:opacity-50"
            >
              <AppIcon
                path={
                  showPasswordConfirmation
                    ? mdiEyeOffOutline
                    : mdiEyeOutline
                }
                size={0.9}
              />
            </button>
          </div>

          {errors.passwordConfirmation && (
            <p
              role="alert"
              className="mt-1.5 text-xs font-medium text-red-600"
            >
              {errors.passwordConfirmation.message}
            </p>
          )}
        </div>

        <button
          type="submit"
          disabled={isLoading}
          className="flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-[#1C274C] px-4 text-sm font-semibold text-white shadow-[0_12px_24px_rgba(28,39,76,0.18)] outline-none transition-[background-color,transform,box-shadow,opacity] hover:bg-[#26345e] hover:shadow-[0_14px_28px_rgba(28,39,76,0.22)] active:scale-[0.99] active:bg-[#151e3c] focus-visible:ring-4 focus-visible:ring-[#1C274C]/20 disabled:cursor-not-allowed disabled:opacity-60 motion-reduce:transition-none"
        >
          {isLoading && (
            <AppIcon
              path={mdiLoading}
              size={0.9}
              spin
            />
          )}

          {isLoading ? 'Criando conta...' : 'Criar conta'}
        </button>
      </form>

      <p className="mt-6 text-center text-sm text-slate-500">
        Já tem uma conta?{' '}
        <Link
          to="/login"
          className="font-semibold text-[#1C274C] underline-offset-4 transition-colors hover:text-[#26345e] hover:underline"
        >
          Entrar
        </Link>
      </p>
    </div>
  )
}