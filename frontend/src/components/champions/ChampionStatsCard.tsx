import type { ChampionStatsDto } from '../../types/api'
import StatGrid from '../StatGrid'

type ChampionStatsCardProps = {
    stats: ChampionStatsDto | null
}

function ChampionStatsCard({ stats }: ChampionStatsCardProps) {
    return (
        <aside className="h-fit rounded-lg border border-slate-800 bg-slate-900 p-4">
            <h2 className="text-base font-bold">Estadísticas globales</h2>

            {!stats ? (
                <p className="mt-3 text-sm text-slate-400">Todavía no hay datos calculados.</p>
            ) : (
                <div className="mt-4">
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
                </div>
            )}
        </aside>
    )
}

export default ChampionStatsCard
