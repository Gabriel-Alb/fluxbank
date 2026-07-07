import { mdiChevronDown, mdiTrendingUp } from '@mdi/js'
import { AppIcon } from '../AppIcon'
import type {
  DashboardBalance,
  MonthlyEvolutionItem,
} from '../../../types/dashboard'

interface BalanceOverviewProps {
  balance: DashboardBalance
  monthlyEvolution: MonthlyEvolutionItem[]
}

const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

export function BalanceOverview({ balance, monthlyEvolution }: BalanceOverviewProps) {
  return (
    <section className="overflow-hidden rounded-[32px] border border-white/[0.08] bg-white/[0.045] p-5 shadow-[0_28px_90px_rgba(0,0,0,0.28)] backdrop-blur-2xl md:p-6">
      <div className="flex flex-col gap-5 sm:flex-row sm:items-start sm:justify-between">
        <div>
          <p className="text-sm font-medium text-slate-400">Saldo total</p>
          <h1 className="mt-2 text-4xl font-semibold tracking-[-0.06em] text-white md:text-5xl">
            {currencyFormatter.format(balance.total)}
          </h1>

          <div className="mt-4 flex flex-wrap items-center gap-2 text-sm text-slate-400">
            <span className="flex h-8 items-center gap-2 rounded-full border border-emerald-400/15 bg-emerald-400/10 px-3 text-emerald-200">
              <AppIcon
                path={mdiTrendingUp}
                size={0.72}
                title="Ganho mensal"
              />
              +{currencyFormatter.format(balance.monthlyGain)}
            </span>
            <span>Ganhos de outubro em investimentos</span>
          </div>
        </div>

        <button
          type="button"
          className="inline-flex h-11 items-center justify-center gap-2 rounded-2xl border border-white/[0.08] bg-[#050711]/70 px-4 text-sm font-semibold text-slate-200 outline-none transition hover:bg-white/[0.08] focus-visible:ring-2 focus-visible:ring-[#9BE7FF]/70 focus-visible:ring-offset-2 focus-visible:ring-offset-[#050711]"
          aria-label="Selecionar período do gráfico"
        >
          {balance.selectedPeriod}
          <AppIcon path={mdiChevronDown} size={0.72} title="Abrir período" />
        </button>
      </div>

      <div
        className="mt-8 flex h-56 items-end gap-2 overflow-hidden rounded-[26px] border border-white/[0.06] bg-[#050711]/60 px-3 pb-4 pt-6 sm:gap-3 sm:px-5"
        aria-label="Gráfico mensal mockado de janeiro a dezembro"
      >
        {monthlyEvolution.map((item) => (
          <div
            key={item.month}
            className="flex h-full min-w-0 flex-1 flex-col items-center justify-end gap-3"
          >
            <div className="flex h-full w-full items-end justify-center">
              <div
                className={[
                  'w-full max-w-8 rounded-t-full transition duration-300 hover:opacity-100',
                  item.active
                    ? 'bg-[linear-gradient(180deg,#9BE7FF,#4F7CFF,#6D5DFB)] opacity-100 shadow-[0_0_32px_rgba(79,124,255,0.34)]'
                    : 'bg-[linear-gradient(180deg,rgba(155,231,255,0.42),rgba(79,124,255,0.18))] opacity-70 hover:bg-[linear-gradient(180deg,rgba(155,231,255,0.66),rgba(79,124,255,0.32))]',
                ].join(' ')}
                style={{ height: `${item.value}%` }}
                title={`${item.month}: ${item.value}%`}
              />
            </div>

            <span
              className={[
                'text-[11px] font-medium',
                item.active ? 'text-[#9BE7FF]' : 'text-slate-500',
              ].join(' ')}
            >
              {item.month}
            </span>
          </div>
        ))}
      </div>
    </section>
  )
}