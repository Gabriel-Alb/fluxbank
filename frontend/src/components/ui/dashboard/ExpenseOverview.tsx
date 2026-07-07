import type { CSSProperties } from 'react'
import { mdiChartBar } from '@mdi/js'
import { AppIcon } from '../AppIcon'
import type { ExpenseOverviewData } from '../../../types/dashboard'

interface ExpenseOverviewProps {
  expenses: ExpenseOverviewData
}

const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

const categoryStyles = [
  {
    dot: 'bg-[#9BE7FF]',
    text: 'text-[#9BE7FF]',
  },
  {
    dot: 'bg-[#6D5DFB]',
    text: 'text-[#C7D2FE]',
  },
  {
    dot: 'bg-[#4F7CFF]',
    text: 'text-[#BFDBFE]',
  },
]

export function ExpenseOverview({ expenses }: ExpenseOverviewProps) {
  const firstCategory = expenses.categories[0]?.percentage ?? 0
  const secondCategory = expenses.categories[1]?.percentage ?? 0
  const firstStop = firstCategory
  const secondStop = firstCategory + secondCategory

  const donutStyle: CSSProperties = {
    background: `conic-gradient(#9BE7FF 0% ${firstStop}%, #6D5DFB ${firstStop}% ${secondStop}%, #4F7CFF ${secondStop}% 100%)`,
  }

  return (
    <section className="grid gap-6 rounded-[32px] border border-white/[0.08] bg-white/[0.045] p-5 shadow-[0_28px_90px_rgba(0,0,0,0.22)] backdrop-blur-2xl md:grid-cols-[220px_minmax(0,1fr)] md:p-6">
      <div className="flex items-center justify-center">
        <div className="relative flex h-48 w-48 items-center justify-center rounded-full p-[14px] shadow-[0_0_50px_rgba(79,124,255,0.18)]">
          <div className="absolute inset-0 rounded-full blur-2xl" style={donutStyle} />

          <div
            className="relative flex h-full w-full items-center justify-center rounded-full"
            style={donutStyle}
            aria-label="Gráfico de despesas mockado"
          >
            <div className="flex h-[118px] w-[118px] items-center justify-center rounded-full border border-white/[0.08] bg-[#050711] shadow-inner">
              <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-white/[0.06]">
                <AppIcon
                  path={mdiChartBar}
                  size={0.95}
                  className="text-[#9BE7FF]"
                  title="Despesas"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="flex min-w-0 flex-col justify-center">
        <p className="text-sm font-medium capitalize text-slate-400">
          Despesas de {expenses.monthLabel}
        </p>

        <h2 className="mt-2 text-3xl font-semibold tracking-[-0.05em] text-white md:text-4xl">
          {currencyFormatter.format(expenses.total)}
        </h2>

        <div className="mt-6 grid gap-3">
          {expenses.categories.map((category, index) => {
            const style = categoryStyles[index] ?? categoryStyles[0]

            return (
              <div
                key={category.id}
                className="flex items-center justify-between gap-4 rounded-2xl border border-white/[0.06] bg-[#050711]/48 px-4 py-3"
              >
                <div className="flex min-w-0 items-center gap-3">
                  <span className={`h-2.5 w-2.5 rounded-full ${style.dot}`} />
                  <div className="min-w-0">
                    <p className="truncate text-sm font-medium text-slate-200">
                      {category.name}
                    </p>
                    <p className="text-xs text-slate-500">
                      {category.percentage}% do resumo mensal
                    </p>
                  </div>
                </div>

                <strong className={`shrink-0 text-sm font-semibold ${style.text}`}>
                  {currencyFormatter.format(category.amount)}
                </strong>
              </div>
            )
          })}
        </div>
      </div>
    </section>
  )
}