import type { RankedRankDto } from '../../types/api'

type RankCardProps = {
    title: string
    rank: RankedRankDto | null
}

function rankWinRate(rank: RankedRankDto) {
    const games = rank.wins + rank.losses
    return games === 0 ? 0 : (rank.wins / games) * 100
}

function RankCard({ title, rank }: RankCardProps) {
    return (
        <section className="rounded-lg border border-slate-800 bg-slate-900 p-4">
            <p className="text-xs font-semibold uppercase text-slate-500">{title}</p>

            {rank ? (
                <>
                    <div className="mt-3 flex items-end justify-between gap-3">
                        <div>
                            <p className="text-xl font-bold capitalize text-slate-100">
                                {rank.tier.toLowerCase()} {rank.rank}
                            </p>
                            <p className="mt-1 text-sm text-cyan-300">
                                {rank.leaguePoints} LP
                            </p>
                        </div>

                        <span className="rounded-md bg-slate-800 px-2 py-1 text-xs text-slate-300">
                            {rankWinRate(rank).toFixed(1)}% WR
                        </span>
                    </div>

                    <div className="mt-4 flex gap-4 border-t border-slate-800 pt-3 text-sm">
                        <span className="text-emerald-300">{rank.wins} victorias</span>
                        <span className="text-rose-300">{rank.losses} derrotas</span>
                    </div>
                </>
            ) : (
                <p className="mt-3 text-sm text-slate-400">Sin clasificación disponible.</p>
            )}
        </section>
    )
}

export default RankCard
