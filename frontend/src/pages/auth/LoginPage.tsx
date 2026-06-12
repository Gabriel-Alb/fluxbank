import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { mdiEmail, mdiLock, mdiEye, mdiEyeOff, mdiLoading } from '@mdi/js'
import { useState } from 'react'
import { AppIcon } from '../../components/ui/AppIcon'

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

export function LoginPage() {
  const [showPassword, setShowPassword] = useState(false)

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  })

  async function onSubmit(data: LoginFormData) {
    // TODO: chamar serviço de autenticação
    console.log('Login:', data)
    await new Promise((resolve) => setTimeout(resolve, 1000))
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-[radial-gradient(circle_at_top,#172554_0%,transparent_34%),linear-gradient(135deg,#020617_0%,#09090b_50%,#020617_100%)]">
      <div className="w-full max-w-md">
        <div className="rounded-2xl border border-white/10 bg-white/[0.04] p-8 shadow-2xl shadow-black/40 backdrop-blur-xl">
          <div className="mb-8 text-center">


            <h1 className="text-2xl font-semibold tracking-tight text-white">
              FluxBank
            </h1>

            <p className="mt-2 text-sm text-zinc-400">
              Entre com seus dados para acessar sua conta
            </p>
          </div>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5" noValidate>
            <div>
              <label className="mb-2 block text-sm font-medium text-zinc-300">
                E-mail
              </label>

              <div className="relative">
                <span className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500">
                  <AppIcon path={mdiEmail} size={0.85} />
                </span>

                <input
                  type="email"
                  autoComplete="email"
                  placeholder="seu@email.com"
                  {...register('email')}
                  className={`
                    w-full rounded-xl border bg-zinc-950/70 py-3 pl-10 pr-4 text-sm text-white
                    placeholder:text-zinc-600 outline-none transition
                    focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10
                    ${errors.email
                      ? 'border-red-500/80 focus:border-red-500 focus:ring-red-500/10'
                      : 'border-white/10 hover:border-white/20'
                    }
                  `}
                />
              </div>

              {errors.email && (
                <p className="mt-1.5 text-xs text-red-400">
                  {errors.email.message}
                </p>
              )}
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-zinc-300">
                Senha
              </label>

              <div className="relative">
                <span className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-zinc-500">
                  <AppIcon path={mdiLock} size={0.85} />
                </span>

                <input
                  type={showPassword ? 'text' : 'password'}
                  autoComplete="current-password"
                  placeholder="••••••••"
                  {...register('password')}
                  className={`
                    w-full rounded-xl border bg-zinc-950/70 py-3 pl-10 pr-11 text-sm text-white
                    placeholder:text-zinc-600 outline-none transition
                    focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10
                    ${errors.password
                      ? 'border-red-500/80 focus:border-red-500 focus:ring-red-500/10'
                      : 'border-white/10 hover:border-white/20'
                    }
                  `}
                />

                <button
                  type="button"
                  onClick={() => setShowPassword((prev) => !prev)}
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-zinc-500 transition-colors hover:text-zinc-300"
                  aria-label={showPassword ? 'Ocultar senha' : 'Mostrar senha'}
                >
                  <AppIcon path={showPassword ? mdiEyeOff : mdiEye} size={0.9} />
                </button>
              </div>

              {errors.password && (
                <p className="mt-1.5 text-xs text-red-400">
                  {errors.password.message}
                </p>
              )}
            </div>

            <div className="flex justify-end">
              <a
                href="#"
                className="text-xs font-medium text-blue-400 transition-colors hover:text-blue-300"
              >
                Esqueci minha senha
              </a>
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="
                flex w-full items-center justify-center gap-2 rounded-xl
                bg-blue-600 px-4 py-3 text-sm font-medium text-white
                shadow-lg shadow-blue-950/30 transition
                hover:bg-blue-500 active:bg-blue-700
                disabled:cursor-not-allowed disabled:opacity-60
              "
            >
              {isSubmitting && (
                <AppIcon path={mdiLoading} size={0.9} spin />
              )}

              {isSubmitting ? 'Entrando...' : 'Entrar'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}