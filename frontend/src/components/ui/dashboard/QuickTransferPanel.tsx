import { mdiPlus } from '@mdi/js'
import { AppIcon } from '../AppIcon'
import type { QuickContact } from '../../../types/dashboard'

interface QuickTransferPanelProps {
  contacts: QuickContact[]
}

export function QuickTransferPanel({ contacts }: QuickTransferPanelProps) {
  return (
    <section className="rounded-[32px] border border-white/[0.08] bg-white/[0.045] p-5 shadow-[0_24px_80px_rgba(0,0,0,0.2)] backdrop-blur-2xl">
      <div className="mb-5 flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-semibold text-white">Enviar dinheiro</p>
          <p className="mt-1 text-xs text-slate-500">Contatos frequentes</p>
        </div>
      </div>

      <div className="flex gap-4 overflow-x-auto pb-1">
        <button
          type="button"
          className="group flex min-w-16 flex-col items-center gap-2 outline-none"
          aria-label="Adicionar novo contato"
        >
          <span className="flex h-14 w-14 items-center justify-center rounded-2xl border border-dashed border-[#9BE7FF]/40 bg-[#9BE7FF]/8 text-[#9BE7FF] transition group-hover:bg-[#9BE7FF]/14 group-focus-visible:ring-2 group-focus-visible:ring-[#9BE7FF]/70 group-focus-visible:ring-offset-2 group-focus-visible:ring-offset-[#050711]">
            <AppIcon path={mdiPlus} size={0.9} title="Adicionar" />
          </span>
          <span className="text-xs font-medium text-slate-400">Adicionar</span>
        </button>

        {contacts.map((contact) => (
          <button
            key={contact.id}
            type="button"
            className="group flex min-w-16 flex-col items-center gap-2 outline-none"
            aria-label={`Enviar dinheiro para ${contact.name}`}
          >
            <span className="flex h-14 w-14 items-center justify-center rounded-2xl border border-white/[0.08] bg-white/[0.06] text-sm font-bold text-white transition group-hover:border-[#9BE7FF]/35 group-hover:bg-white/[0.09] group-focus-visible:ring-2 group-focus-visible:ring-[#9BE7FF]/70 group-focus-visible:ring-offset-2 group-focus-visible:ring-offset-[#050711]">
              {contact.initials}
            </span>
            <span className="text-xs font-medium text-slate-400">
              {contact.name}
            </span>
          </button>
        ))}
      </div>
    </section>
  )
}