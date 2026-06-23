import type { ReactNode } from 'react'
import { Navigate } from 'react-router-dom'
import { useAuthSession } from '../../features/auth/useAuthSession'

interface PublicOnlyRouteProps {
  children: ReactNode
}

export function PublicOnlyRoute({ children }: PublicOnlyRouteProps) {
  const {
    data,
    isLoading,
    isFetching,
  } = useAuthSession()

  if (isLoading || isFetching) {
    return (
      <main className="flex min-h-dvh items-center justify-center bg-white px-6">
        <div className="rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm font-medium text-slate-600 shadow-sm">
          Carregando...
        </div>
      </main>
    )
  }

  if (data?.authenticated) {
    return (
      <Navigate
        to="/dashboard"
        replace
      />
    )
  }

  return children
}