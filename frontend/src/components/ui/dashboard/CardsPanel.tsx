import type { DashboardCardData } from '../../../types/dashboard'

interface CardsPanelProps {
  card: DashboardCardData
}

export function CardsPanel({ card }: CardsPanelProps) {
  return (
    <section className="rounded-[32px] border border-white/[0.08] bg-white/[0.045] p-5 shadow-[0_24px_80px_rgba(0,0,0,0.24)] backdrop-blur-2xl">
      <div className="mb-5 flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-semibold text-white">Meus cartões</p>
          <p className="mt-1 text-xs text-slate-500">Cartão principal</p>
        </div>

        <button
          type="button"
          className="rounded-full border border-white/[0.08] px-3 py-1.5 text-xs font-semibold text-slate-300 outline-none transition hover:bg-white/[0.07] hover:text-white focus-visible:ring-2 focus-visible:ring-[#9BE7FF]/70 focus-visible:ring-offset-2 focus-visible:ring-offset-[#050711]"
        >
          Gerenciar
        </button>
      </div>

      <div className="relative overflow-hidden rounded-[28px] border border-white/[0.12] bg-[linear-gradient(135deg,#1C274C_0%,#4F7CFF_46%,#6D5DFB_72%,#9BE7FF_120%)] p-5 shadow-[0_24px_70px_rgba(79,124,255,0.24)]">
        <div className="absolute -right-16 -top-20 h-48 w-48 rounded-full bg-white/20 blur-3xl" />
        <div className="absolute -bottom-24 left-4 h-48 w-48 rounded-full bg-[#050711]/35 blur-3xl" />

        <div className="relative z-10 flex items-start justify-between">
          <div className="h-11 w-14 rounded-xl border border-white/30 bg-[linear-gradient(135deg,rgba(255,255,255,0.72),rgba(255,255,255,0.18))] p-2">
            <div className="h-full w-full rounded-lg border border-[#1C274C]/25" />
          </div>

          <span className="rounded-full bg-white/15 px-3 py-1 text-xs font-semibold text-white">
            FluxPay
          </span>
        </div>

        <div className="relative z-10 mt-12">
          <p className="text-sm font-medium text-white/70">{card.name}</p>
          <p className="mt-3 text-2xl font-semibold tracking-[0.18em] text-white">
            {card.maskedNumber}
          </p>
        </div>

        <div className="relative z-10 mt-8 flex items-end justify-between gap-4">
          <div>
            <p className="text-[10px] uppercase tracking-[0.18em] text-white/50">
              Titular
            </p>
            <p className="mt-1 text-sm font-semibold text-white">{card.holder}</p>
          </div>

          <div className="text-right">
            <p className="text-[10px] uppercase tracking-[0.18em] text-white/50">
              Validade
            </p>
            <p className="mt-1 text-sm font-semibold text-white">
              {card.expiresAt}
            </p>
          </div>
        </div>
      </div>

      <div className="mt-5 rounded-2xl border border-white/[0.06] bg-[#050711]/52 p-4">
        <div className="mb-3 flex items-center justify-between text-xs">
          <span className="font-medium text-slate-400">Limite utilizado</span>
          <strong className="text-slate-200">{card.limitUsedPercentage}%</strong>
        </div>

        <div className="h-2 overflow-hidden rounded-full bg-white/[0.06]">
          <div
            className="h-full rounded-full bg-[linear-gradient(90deg,#4F7CFF,#9BE7FF)]"
            style={{ width: `${card.limitUsedPercentage}%` }}
          />
        </div>
      </div>
    </section>
  )
}