import { AuthLayout } from '../../components/auth/AuthLayout'
import { AuthVisualPanel } from '../../components/auth/AuthVisualPanel'
import { RegisterForm } from '../../components/auth/RegisterForm'

export function RegisterPage() {
  return (
    <AuthLayout visualPanel={<AuthVisualPanel />}>
      <RegisterForm />
    </AuthLayout>
  )
}