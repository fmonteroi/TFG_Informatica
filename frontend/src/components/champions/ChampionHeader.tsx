import type { ChampionDto } from '../../types/api.ts'

type ChampionHeaderProps = {
    champion: ChampionDto
    championIcon: string | null
    onBack: () => void
}

function ChampionHeader({ champion, championIcon, onBack }: ChampionHeaderProps) {
    return (
        <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4 flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="flex items-center gap-4">
                {championIcon ? (
                    <img
                        src={championIcon}
                        alt={champion.championName}
                        className="h-20 w-20 rounded-2xl"
                    />
                ) : (
                    <div className="h-20 w-20 rounded-2xl border border-slate-700 bg-slate-800" />
                )}

                <div>
                    <h1 className="text-3xl font-black">{champion.championName}</h1>
                    <p className="text-slate-400">
                        Builds recientes de profesionales para este campeón.
                    </p>
                </div>
            </div>

            <button
                onClick={onBack}
                className="rounded-xl bg-slate-800 px-4 py-3 font-bold text-slate-100 transition hover:bg-slate-700"
            >
                Volver a campeones
            </button>
        </section>
    )
}

export default ChampionHeader
