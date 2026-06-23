import type { ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useAuthSession } from '../../features/auth/useAuthSession'

interface RequireAuthProps {
  children: ReactNode
}

export function RequireAuth({ children }: RequireAuthProps) {
  const location = useLocation()

  const {
    data,
    isLoading,
    isFetching,
  } = useAuthSession()

  if (isLoading || isFetching) {
    return (
      <main className="flex min-h-dvh items-center justify-center bg-slate-50 px-6">
        <div className="rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm font-medium text-slate-600 shadow-sm">
          Verificando sessão...
        </div>
      </main>
    )
  }

  if (!data?.authenticated) {
    return (
      <Navigate
        to="/login"
        replace
        state={{ from: location }}
      />
    )
  }

  return children
}