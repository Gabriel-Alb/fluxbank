import { mdiBellOutline, mdiMagnify, mdiShieldCheckOutline } from '@mdi/js'
import { AppIcon } from '../AppIcon'

export function DashboardHeader() {
  return (
    <header className="flex flex-col gap-4 rounded-[28px] border border-white/[0.08] bg-white/[0.045] p-4 shadow-[0_18px_60px_rgba(0,0,0,0.22)] backdrop-blur-2xl md:flex-row md:items-center md:justify-between">
      <label className="group flex min-h-12 flex-1 items-center gap-3 rounded-2xl border border-white/[0.08] bg-[#050711]/70 px-4 transition focus-within:border-[#9BE7FF]/45 focus-within:bg-[#070B16]">
        <AppIcon
          path={mdiMagnify}
          size={0.9}
          className="text-slate-500 transition group-focus-within:text-[#9BE7FF]"
          title="Buscar"
        />
        <input
          type="search"
          className="h-12 w-full bg-transparent text-sm text-slate-100 outline-none placeholder:text-slate-500"
          placeholder="Buscar transações, cartões ou contatos..."
          aria-label="Buscar transações, cartões ou contatos"
        />
      </label>

      <div className="flex items-center justify-between gap-3 md:justify-end">
        <div className="hidden items-center gap-2 rounded-2xl border border-white/[0.08] bg-white/[0.04] px-3 py-2 text-xs font-medium text-slate-300 sm:flex">
          <span className="flex h-7 w-7 items-center justify-center rounded-xl bg-[#1C274C]">
            <AppIcon
              path={mdiShieldCheckOutline}
              size={0.72}
              className="text-[#9BE7FF]"
              title="Conta protegida"
            />
          </span>
          Conta protegida
        </div>

        <button
          type="button"
          className="relative flex h-12 w-12 items-center justify-center rounded-2xl border border-white/[0.08] bg-white/[0.05] text-slate-300 outline-none transition hover:bg-white/[0.08] hover:text-white focus-visible:ring-2 focus-visible:ring-[#9BE7FF]/70 focus-visible:ring-offset-2 focus-visible:ring-offset-[#050711]"
          aria-label="Abrir notificações"
        >
          <AppIcon path={mdiBellOutline} size={0.88} title="Notificações" />
          <span className="absolute right-3 top-3 h-2 w-2 rounded-full bg-[#9BE7FF] shadow-[0_0_16px_rgba(155,231,255,0.9)]" />
        </button>

        <button
          type="button"
          className="flex items-center gap-3 rounded-2xl border border-white/[0.08] bg-white/[0.05] px-2.5 py-2 outline-none transition hover:bg-white/[0.08] focus-visible:ring-2 focus-visible:ring-[#9BE7FF]/70 focus-visible:ring-offset-2 focus-visible:ring-offset-[#050711]"
          aria-label="Abrir menu do usuário"
        >
          <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-[linear-gradient(135deg,#4F7CFF,#9BE7FF)] text-sm font-bold text-[#050711]">
            GA
          </span>
          <span className="hidden pr-2 text-left sm:block">
            <span className="block text-sm font-semibold text-white">
              Gabriel
            </span>
            <span className="block text-xs text-slate-400">Conta digital</span>
          </span>
        </button>
      </div>
    </header>
  )
}