import type { PlayerStatsDto } from '../../types/api'
import StatGrid from '../StatGrid'

type PlayerStatsCardProps = {
    stats: PlayerStatsDto | null
    championMap: Map<number, string> | null
}

function PlayerStatsCard({ stats, championMap }: PlayerStatsCardProps) {
    const bestChampionIcon = stats?.bestChampion
        ? championMap?.get(stats.bestChampion.championId) ?? null
        : null

    return (
        <section className="rounded-lg border border-slate-800 bg-slate-900 p-4">
            <h2 className="text-base font-bold">Estadísticas</h2>

            {!stats ? (
                <p className="mt-3 text-sm text-slate-400">Todavía no hay datos calculados.</p>
            ) : (
                <div className="mt-4 space-y-4">
                    <StatGrid
                        items={[
                            { label: 'Partidas', value: stats.gamesPlayed },
                            { label: 'Win rate', value: `${stats.winRate.toFixed(1)}%`, tone: 'accent' },
                            { label: 'Victorias', value: stats.wins, tone: 'positive' },
                            { label: 'Derrotas', value: stats.losses, tone: 'negative' },
                            { label: 'KDA', value: stats.kda.toFixed(2) },
                            { label: 'Kills', value: stats.averageKills.toFixed(1) },
                            { label: 'Deaths', value: stats.averageDeaths.toFixed(1) },
                            { label: 'Assists', value: stats.averageAssists.toFixed(1) },
                        ]}
                    />

                    {stats.bestChampion && (
                        <div className="flex items-center gap-3 border-t border-slate-800 pt-4">
                            {bestChampionIcon ? (
                                <img
                                    src={bestChampionIcon}
                                    alt={`Icono de ${stats.bestChampion.championName}`}
                                    className="h-11 w-11 rounded-lg"
                                />
                            ) : (
                                <div
                                    role="img"
                                    aria-label="Icono de campeón no disponible"
                                    className="h-11 w-11 rounded-lg bg-slate-800"
                                />
                            )}
                            <div>
                                <p className="text-xs uppercase text-slate-500">Mejor campeón</p>
                                <p className="font-semibold">{stats.bestChampion.championName}</p>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </section>
    )
}

export default PlayerStatsCard
