import type { ChampionDto, Tier } from '../../types/api'
import ChampionCatalogCard from './ChampionCatalogCard'
import ChampionTierBadge from './ChampionTierBadge'

type ChampionTierSectionProps = {
    tier: Tier
    champions: ChampionDto[]
    championMap: Map<number, string> | null | undefined
}

function ChampionTierSection({
    tier,
    champions,
    championMap,
}: ChampionTierSectionProps) {
    let championsContent

    if (champions.length > 0) {
        championsContent = (
            <div className="grid grid-cols-2 gap-3 p-4 sm:grid-cols-4 lg:grid-cols-6 xl:grid-cols-8">
                {champions.map((champion) => (
                    <ChampionCatalogCard
                        key={champion.championId}
                        champion={champion}
                        icon={championMap?.get(champion.championId) ?? null}
                    />
                ))}
            </div>
        )
    } else {
        championsContent = (
            <p className="px-4 py-5 text-sm text-slate-500">
                No hay coincidencias en este tier.
            </p>
        )
    }

    return (
        <section
            aria-labelledby={`tier-${tier}`}
            className="overflow-hidden rounded-lg border border-slate-800 bg-slate-900"
        >
            <header className="flex min-h-16 items-center gap-3 border-b border-slate-800 px-4 py-3">
                <ChampionTierBadge
                    id={`tier-${tier}`}
                    tier={tier}
                    size="large"
                />
            </header>

            {championsContent}
        </section>
    )
}

export default ChampionTierSection
