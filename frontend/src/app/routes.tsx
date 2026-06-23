import { createBrowserRouter, Navigate } from 'react-router-dom'
import { PublicOnlyRoute } from '../components/auth/PublicOnlyRoute'
import { RequireAuth } from '../components/auth/RequireAuth'
import { DashboardPage } from '../pages/dashboard/DashboardPage'
import { LoginPage } from '../pages/auth/LoginPage'
import { RegisterPage } from '../pages/auth/RegisterPage'

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
    path: '/register',
    element: (
      <PublicOnlyRoute>
        <RegisterPage />
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