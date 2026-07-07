import { BalanceOverview } from '../../components/ui/dashboard/BalanceOverview'
import { CardsPanel } from '../../components/ui/dashboard/CardsPanel'
import { DashboardHeader } from '../../components/ui/dashboard/DashboardHeader'
import { DashboardSidebar } from '../../components/ui/dashboard/DashboardSidebar'
import { ExpenseOverview } from '../../components/ui/dashboard/ExpenseOverview'
import { QuickTransferPanel } from '../../components/ui/dashboard/QuickTransferPanel'
import { RecentTransactions } from '../../components/ui/dashboard/RecentTransactions'
import { dashboardMock } from '../../mocks/dashboardMock'

export function HomePage() {
  return (
    <main className="min-h-screen overflow-x-hidden bg-[#050711] text-slate-100">
      <div className="pointer-events-none fixed inset-0">
        <div className="absolute left-[-12%] top-[-18%] h-[520px] w-[520px] rounded-full bg-[#1C274C]/48 blur-[120px]" />
        <div className="absolute right-[-10%] top-[8%] h-[460px] w-[460px] rounded-full bg-[#6D5DFB]/24 blur-[120px]" />
        <div className="absolute bottom-[-18%] left-[28%] h-[520px] w-[520px] rounded-full bg-[#4F7CFF]/18 blur-[130px]" />
      </div>

      <div className="relative z-10 grid min-h-screen gap-4 p-4 lg:grid-cols-[280px_minmax(0,1fr)] lg:gap-5 lg:p-6">
        <DashboardSidebar />

        <section className="flex min-w-0 flex-col gap-4 lg:gap-5">
          <DashboardHeader />

          <div className="grid min-w-0 gap-4 xl:grid-cols-[minmax(0,1fr)_380px] xl:gap-5">
            <div className="flex min-w-0 flex-col gap-4 lg:gap-5">
              <BalanceOverview
                balance={dashboardMock.balance}
                monthlyEvolution={dashboardMock.monthlyEvolution}
              />

              <ExpenseOverview expenses={dashboardMock.expenses} />
            </div>

            <aside className="flex min-w-0 flex-col gap-4 lg:gap-5">
              <CardsPanel card={dashboardMock.card} />

              <QuickTransferPanel contacts={dashboardMock.quickContacts} />

              <RecentTransactions transactions={dashboardMock.recentTransactions} />
            </aside>
          </div>
        </section>
      </div>
    </main>
  )
}