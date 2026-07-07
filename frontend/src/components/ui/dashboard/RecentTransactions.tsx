import type { RecentTransaction } from '../../../types/dashboard'

interface RecentTransactionsProps {
  transactions: RecentTransaction[]
}

const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

export function RecentTransactions({ transactions }: RecentTransactionsProps) {
  return (
    <section className="rounded-[32px] border border-white/[0.08] bg-white/[0.045] p-5 shadow-[0_24px_80px_rgba(0,0,0,0.2)] backdrop-blur-2xl">
      <div className="mb-5 flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-semibold text-white">Transações recentes</p>
          <p className="mt-1 text-xs text-slate-500">Movimentações mockadas</p>
        </div>

        <button
          type="button"
          className="rounded-full border border-white/[0.08] px-3 py-1.5 text-xs font-semibold text-slate-300 outline-none transition hover:bg-white/[0.07] hover:text-white focus-visible:ring-2 focus-visible:ring-[#9BE7FF]/70 focus-visible:ring-offset-2 focus-visible:ring-offset-[#050711]"
        >
          Ver todas
        </button>
      </div>

      <div className="grid gap-3">
        {transactions.map((transaction) => {
          const isIncome = transaction.type === 'income'

          return (
            <article
              key={transaction.id}
              className="flex items-center gap-3 rounded-2xl border border-white/[0.06] bg-[#050711]/46 p-3 transition hover:bg-white/[0.055]"
            >
              <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl border border-white/[0.08] bg-white/[0.06] text-xs font-bold text-slate-200">
                {transaction.iconLabel}
              </div>

              <div className="min-w-0 flex-1">
                <h3 className="truncate text-sm font-semibold text-white">
                  {transaction.name}
                </h3>
                <p className="mt-0.5 truncate text-xs text-slate-500">
                  {transaction.category} · {transaction.time}
                </p>
              </div>

              <strong
                className={[
                  'shrink-0 text-sm font-semibold',
                  isIncome ? 'text-emerald-300' : 'text-slate-200',
                ].join(' ')}
              >
                {isIncome ? '+' : ''}
                {currencyFormatter.format(transaction.amount)}
              </strong>
            </article>
          )
        })}
      </div>
    </section>
  )
}