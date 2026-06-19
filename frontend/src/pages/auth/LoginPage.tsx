import { AuthLayout } from '../../components/auth/AuthLayout'
import { AuthVisualPanel } from '../../components/auth/AuthVisualPanel'
import { LoginForm } from '../../components/auth/LoginForm'

export function LoginPage() {
  return (
    <AuthLayout visualPanel={<AuthVisualPanel />}>
      <LoginForm />
    </AuthLayout>
  )
}