import { createBrowserRouter, Navigate } from 'react-router-dom'
import { PublicOnlyRoute } from '../components/auth/PublicOnlyRoute'
import { RequireAuth } from '../components/auth/RequireAuth'
import { LoginPage } from '../pages/auth/LoginPage'
import { RegisterPage } from '../pages/auth/RegisterPage'
import { ResendVerificationPage } from '../pages/auth/ResendVerificationPage'
import { VerifyEmailPage } from '../pages/auth/VerifyEmailPage'
import { DashboardPage } from '../pages/dashboard/DashboardPage'
import { HomePage } from '../pages/home/HomePage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: (
      <Navigate
        to="/home"
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
    path: '/resend-verification',
    element: (
      <PublicOnlyRoute>
        <ResendVerificationPage />
      </PublicOnlyRoute>
    ),
  },
  {
    path: '/verify-email',
    element: <VerifyEmailPage />,
  },
  {
    path: '/home',
    element: (
      <RequireAuth>
        <HomePage />
      </RequireAuth>
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
        to="/home"
        replace
      />
    ),
  },
])