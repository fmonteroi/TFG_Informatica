import { Link } from 'react-router-dom'
import type { ChampionDto } from '../../types/api'

type ChampionCatalogCardProps = {
    champion: ChampionDto
    icon: string | null
}

function ChampionCatalogCard({ champion, icon }: ChampionCatalogCardProps) {
    let iconContent

    if (icon) {
        iconContent = (
            <img
                src={icon}
                alt={`Icono de ${champion.championName}`}
                className="mx-auto h-16 w-16 rounded-md"
            />
        )
    } else {
        iconContent = (
            <div
                role="img"
                aria-label={`Icono de ${champion.championName} no disponible`}
                className="mx-auto h-16 w-16 rounded-md bg-slate-800"
            />
        )
    }

    return (
        <Link
            to={`/campeones/${champion.championId}`}
            className="group rounded-lg border border-slate-800 bg-slate-950/70 p-3 text-center transition hover:-translate-y-0.5 hover:border-cyan-300/50 focus-visible:outline-2 focus-visible:outline-cyan-300"
        >
            {iconContent}

            <p className="mt-2 truncate text-sm font-semibold text-slate-100 group-hover:text-cyan-200">
                {champion.championName}
            </p>
        </Link>
    )
}

export default ChampionCatalogCard
