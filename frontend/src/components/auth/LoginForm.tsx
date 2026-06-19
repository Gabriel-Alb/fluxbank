import { useState } from 'react'
import { zodResolver } from '@hookform/resolvers/zod'
import {
  mdiEmailOutline,
  mdiEyeOffOutline,
  mdiEyeOutline,
  mdiLoading,
  mdiLockOutline,
} from '@mdi/js'
import { useForm, type SubmitHandler } from 'react-hook-form'
import { z } from 'zod'
import { AppIcon } from '../ui/AppIcon'

const loginSchema = z.object({
  email: z
    .string()
    .min(1, 'E-mail obrigatório')
    .email('E-mail inválido'),
  password: z
    .string()
    .min(1, 'Senha obrigatória')
    .min(6, 'Senha deve ter no mínimo 6 caracteres'),
})

type LoginFormData = z.infer<typeof loginSchema>

export function LoginForm() {
  const [showPassword, setShowPassword] = useState(false)

  const {
    register,
    handleSubmit,
    formState: {
      errors,
      isSubmitting,
    },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  })

  const onSubmit: SubmitHandler<LoginFormData> = async () => {
    // A integração com o serviço de autenticação será adicionada
    // quando o fluxo de login estiver disponível no projeto.
  }

  const emailErrorId = errors.email
    ? 'login-email-error'
    : undefined

  const passwordErrorId = errors.password
    ? 'login-password-error'
    : undefined

  return (
    <div className="w-full max-w-[410px]">


      <header>
        <h1 className="text-[clamp(2rem,4vw,2.7rem)] font-semibold leading-[1.08] tracking-[-0.045em] text-[#15182a]">
          Entre na sua conta
        </h1>

        <p className="mt-3 max-w-[360px] text-sm leading-6 text-slate-500">
          Acesse sua conta para acompanhar suas finanças com segurança.
        </p>
      </header>

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="mt-8 space-y-5"
        noValidate
      >
        <div>
          <label
            htmlFor="login-email"
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
              id="login-email"
              type="email"
              autoComplete="email"
              placeholder="seuemail@exemplo.com"
              aria-invalid={Boolean(errors.email)}
              aria-describedby={emailErrorId}
              disabled={isSubmitting}
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
              id="login-email-error"
              role="alert"
              className="mt-1.5 text-xs font-medium text-red-600"
            >
              {errors.email.message}
            </p>
          )}
        </div>

        <div>
          <label
            htmlFor="login-password"
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
              id="login-password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="current-password"
              placeholder="Digite sua senha"
              aria-invalid={Boolean(errors.password)}
              aria-describedby={passwordErrorId}
              disabled={isSubmitting}
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
              disabled={isSubmitting}
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
              id="login-password-error"
              role="alert"
              className="mt-1.5 text-xs font-medium text-red-600"
            >
              {errors.password.message}
            </p>
          )}
        </div>

        <button
          type="submit"
          disabled={isSubmitting}
          className="flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-[#1C274C] px-4 text-sm font-semibold text-white shadow-[0_12px_24px_rgba(28,39,76,0.18)] outline-none transition-[background-color,transform,box-shadow,opacity] hover:bg-[#26345e] hover:shadow-[0_14px_28px_rgba(28,39,76,0.22)] active:scale-[0.99] active:bg-[#151e3c] focus-visible:ring-4 focus-visible:ring-[#1C274C]/20 disabled:cursor-not-allowed disabled:opacity-60 motion-reduce:transition-none"
        >
          {isSubmitting && (
            <AppIcon
              path={mdiLoading}
              size={0.9}
              spin
            />
          )}

          {isSubmitting ? 'Entrando...' : 'Entrar'}
        </button>
      </form>

      <p className="mt-6 text-center text-xs leading-5 text-slate-400">
        Seus dados são protegidos e utilizados somente para acessar sua conta.
      </p>
    </div>
  )
}