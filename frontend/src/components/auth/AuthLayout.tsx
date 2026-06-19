import type { ReactNode } from 'react'

interface AuthLayoutProps {
  visualPanel: ReactNode
  children: ReactNode
}

export function AuthLayout({
  visualPanel,
  children,
}: AuthLayoutProps) {
  return (
    <main className="min-h-dvh w-full overflow-x-hidden bg-white">
      <section className="grid min-h-dvh w-full md:grid-cols-[minmax(300px,46%)_minmax(0,54%)]">
        <div className="hidden min-h-dvh p-2 md:block lg:p-3">
          {visualPanel}
        </div>

        <div className="flex min-h-dvh min-w-0 items-center justify-center px-6 py-10 sm:px-10 md:px-12 lg:px-16 xl:px-24">
          {children}
        </div>
      </section>
    </main>
  )
}