import type { ChampionDto } from '../../types/api'

type ChampionHeaderProps = {
    champion: ChampionDto
    championIcon: string | null
    onBack: () => void
}

function ChampionHeader({ champion, championIcon, onBack }: ChampionHeaderProps) {
    return (
        <section className="flex flex-col gap-4 border-b border-slate-800 pb-6 lg:flex-row lg:items-center lg:justify-between">
            <div className="flex items-center gap-4">
                {championIcon ? (
                    <img
                        src={championIcon}
                        alt={`Icono de ${champion.championName}`}
                        className="h-20 w-20 rounded-lg border border-slate-700"
                    />
                ) : (
                    <div
                        role="img"
                        aria-label="Icono de campeón no disponible"
                        className="h-20 w-20 rounded-lg border border-slate-700 bg-slate-800"
                    />
                )}

                <div>
                    <h1 className="text-3xl font-black">{champion.championName}</h1>
                </div>
            </div>

            <button
                type="button"
                onClick={onBack}
                className="rounded-lg border border-slate-700 px-4 py-3 font-semibold text-slate-100 transition hover:bg-slate-800"
            >
                Volver a campeones
            </button>
        </section>
    )
}

export default ChampionHeader
