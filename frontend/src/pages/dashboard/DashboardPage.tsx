import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { logout } from '../../features/auth/authApi'
import { authKeys } from '../../features/auth/authKeys'
import { useAuthSession } from '../../features/auth/useAuthSession'

export function DashboardPage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const {
    data,
    isLoading,
  } = useAuthSession()

  const logoutMutation = useMutation({
    mutationFn: logout,
    onSuccess: () => {
      queryClient.setQueryData(authKeys.session(), {
        authenticated: false,
        user: null,
      })

      navigate('/login', {
        replace: true,
      })
    },
  })

  if (isLoading) {
    return (
      <main className="flex min-h-dvh items-center justify-center bg-slate-50 px-6">
        <div className="rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm font-medium text-slate-600 shadow-sm">
          Carregando dashboard...
        </div>
      </main>
    )
  }

  const user = data?.user

  return (
    <main className="min-h-dvh bg-slate-50 px-6 py-8 text-slate-950">
      <section className="mx-auto flex w-full max-w-5xl flex-col gap-6">
        <header className="flex flex-col gap-4 rounded-3xl border border-slate-200 bg-white p-6 shadow-sm sm:flex-row sm:items-center sm:justify-between">
          <div>
            <p className="text-sm font-medium text-slate-500">
              FluxBank
            </p>

            <h1 className="mt-1 text-2xl font-semibold tracking-[-0.03em] text-slate-950">
              Olá, {user?.fullName ?? 'usuário'}.
            </h1>

            <p className="mt-2 text-sm leading-6 text-slate-500">
              Sua sessão está ativa.
            </p>
          </div>

          <button
            type="button"
            disabled={logoutMutation.isPending}
            onClick={() => {
              logoutMutation.mutate()
            }}
            className="h-11 rounded-xl bg-[#1C274C] px-5 text-sm font-semibold text-white shadow-sm transition-colors hover:bg-[#26345e] disabled:cursor-not-allowed disabled:opacity-60"
          >
            {logoutMutation.isPending ? 'Saindo...' : 'Sair'}
          </button>
        </header>

        <section className="grid gap-4 md:grid-cols-3">
          <article className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
            <p className="text-xs font-semibold uppercase tracking-[0.12em] text-slate-400">
              E-mail
            </p>

            <p className="mt-2 break-all text-sm font-medium text-slate-800">
              {user?.email ?? '-'}
            </p>
          </article>

          <article className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
            <p className="text-xs font-semibold uppercase tracking-[0.12em] text-slate-400">
              Status
            </p>

            <p className="mt-2 text-sm font-medium text-slate-800">
              {user?.status ?? '-'}
            </p>
          </article>

          <article className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
            <p className="text-xs font-semibold uppercase tracking-[0.12em] text-slate-400">
              E-mail verificado
            </p>

            <p className="mt-2 text-sm font-medium text-slate-800">
              {user?.emailVerified ? 'Sim' : 'Não'}
            </p>
          </article>
        </section>
      </section>
    </main>
  )
}