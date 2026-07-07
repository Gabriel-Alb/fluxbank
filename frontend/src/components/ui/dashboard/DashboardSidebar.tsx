import {
  mdiChartLine,
  mdiCogOutline,
  mdiCreditCardOutline,
  mdiFileDocumentOutline,
  mdiHelpCircleOutline,
  mdiHomeVariantOutline,
  mdiSwapHorizontal,
  mdiWalletOutline,
} from '@mdi/js'
import { AppIcon } from '../AppIcon'

const navigationItems = [
  {
    label: 'Dashboard',
    icon: mdiHomeVariantOutline,
    active: true,
  },
  {
    label: 'Cartões',
    icon: mdiCreditCardOutline,
    active: false,
  },
  {
    label: 'Transações',
    icon: mdiSwapHorizontal,
    active: false,
  },
  {
    label: 'Investimentos',
    icon: mdiChartLine,
    active: false,
  },
  {
    label: 'Documentos',
    icon: mdiFileDocumentOutline,
    active: false,
  },
  {
    label: 'Configurações',
    icon: mdiCogOutline,
    active: false,
  },
  {
    label: 'Ajuda',
    icon: mdiHelpCircleOutline,
    active: false,
  },
]

export function DashboardSidebar() {
  return (
    <aside className="flex h-full min-h-0 flex-col rounded-[28px] border border-white/[0.08] bg-white/[0.035] p-5 shadow-[0_24px_90px_rgba(0,0,0,0.35)] backdrop-blur-2xl lg:sticky lg:top-6 lg:h-[calc(100vh-48px)]">
      <div className="flex items-center gap-3">
        <div className="flex h-11 w-11 items-center justify-center rounded-2xl border border-white/[0.1] bg-[#1C274C] shadow-[0_16px_40px_rgba(79,124,255,0.22)]">
          <AppIcon
            path={mdiWalletOutline}
            size={1}
            className="text-[#9BE7FF]"
            title="FluxBank"
          />
        </div>

        <div>
          <strong className="block text-lg font-semibold tracking-[-0.03em] text-white">
            FluxBank
          </strong>
          <span className="text-xs font-medium text-slate-400">
            Digital Banking
          </span>
        </div>
      </div>

      <nav
        className="mt-9 flex flex-1 flex-col gap-2"
        aria-label="Navegação principal do dashboard"
      >
        {navigationItems.map((item) => (
          <button
            key={item.label}
            type="button"
            className={[
              'group flex w-full items-center gap-3 rounded-2xl px-4 py-3 text-left text-sm font-medium outline-none transition duration-200',
              'focus-visible:ring-2 focus-visible:ring-[#9BE7FF]/70 focus-visible:ring-offset-2 focus-visible:ring-offset-[#050711]',
              item.active
                ? 'bg-white text-[#050711] shadow-[0_16px_35px_rgba(155,231,255,0.14)]'
                : 'text-slate-400 hover:bg-white/[0.06] hover:text-white',
            ].join(' ')}
            aria-current={item.active ? 'page' : undefined}
          >
            <AppIcon
              path={item.icon}
              size={0.86}
              className={item.active ? 'text-[#1C274C]' : 'text-current'}
              title={item.label}
            />
            <span>{item.label}</span>
          </button>
        ))}
      </nav>

      <section className="mt-6 overflow-hidden rounded-[24px] border border-white/[0.1] bg-[linear-gradient(135deg,rgba(28,39,76,0.95),rgba(109,93,251,0.72),rgba(79,124,255,0.42))] p-4 shadow-[0_20px_60px_rgba(28,39,76,0.28)]">
        <div className="mb-7 flex h-10 w-10 items-center justify-center rounded-2xl bg-white/15">
          <AppIcon
            path={mdiWalletOutline}
            size={0.78}
            className="text-white"
            title="Conta Premium"
          />
        </div>

        <h2 className="text-sm font-semibold text-white">Conta Premium</h2>
        <p className="mt-1 max-w-[170px] text-xs leading-5 text-white/72">
          Mais limites, benefícios e recursos para sua rotina financeira.
        </p>

        <button
          type="button"
          className="mt-4 rounded-full bg-white px-4 py-2 text-xs font-semibold text-[#1C274C] outline-none transition hover:bg-[#EAF6FF] focus-visible:ring-2 focus-visible:ring-white/80 focus-visible:ring-offset-2 focus-visible:ring-offset-[#1C274C]"
        >
          Conhecer plano
        </button>
      </section>
    </aside>
  )
}