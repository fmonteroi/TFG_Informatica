import type { ChampionDto } from '../../types/api'
import ChampionTierBadge from './ChampionTierBadge'

type ChampionHeaderProps = {
    champion: Pick<ChampionDto, 'championId' | 'championName' | 'tier'>
    championIcon: string | null
    onBack: () => void
}

function ChampionHeader({ champion, championIcon, onBack }: ChampionHeaderProps) {
    let iconContent

    if (championIcon) {
        iconContent = (
            <img
                src={championIcon}
                alt={`Icono de ${champion.championName}`}
                className="h-20 w-20 rounded-lg border border-slate-700"
            />
        )
    } else {
        iconContent = (
            <div
                role="img"
                aria-label="Icono de campeón no disponible"
                className="h-20 w-20 rounded-lg border border-slate-700 bg-slate-800"
            />
        )
    }

    return (
        <section className="flex flex-col gap-4 border-b border-slate-800 pb-6 lg:flex-row lg:items-center lg:justify-between">
            <div className="flex items-center gap-4">
                {iconContent}

                <div>
                    <div className="flex items-center gap-3">
                        <h1 className="text-3xl font-black">{champion.championName}</h1>
                        <ChampionTierBadge tier={champion.tier} size="small" />
                    </div>
                </div>
            </div>

            <button
                type="button"
                onClick={onBack}
                className="rounded-lg border border-cyan-300/40 bg-cyan-300/10 px-4 py-3 text-center font-semibold text-cyan-100 transition hover:bg-cyan-300/20"
            >
                Volver a campeones
            </button>
        </section>
    )
}

export default ChampionHeader
