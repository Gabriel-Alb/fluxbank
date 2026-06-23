import { createBrowserRouter, Navigate } from 'react-router-dom'
import { PublicOnlyRoute } from '../components/auth/PublicOnlyRoute'
import { RequireAuth } from '../components/auth/RequireAuth'
import { DashboardPage } from '../pages/dashboard/DashboardPage'
import { LoginPage } from '../pages/auth/LoginPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: (
      <Navigate
        to="/dashboard"
        replace
      />
    ),
  },
  {
    path: '/login',
    element: (
      <PublicOnlyRoute>
        <LoginPage />
      </PublicOnlyRoute>
    ),
  },
  {
    path: '/dashboard',
    element: (
      <RequireAuth>
        <DashboardPage />
      </RequireAuth>
    ),
  },
  {
    path: '*',
    element: (
      <Navigate
        to="/dashboard"
        replace
      />
    ),
  },
])