import type { RankedRankDto } from '../../types/api'
import { formatRank, rankEmblemUrl } from '../../lib/lol'

type RankCardProps = {
    title: string
    rank: RankedRankDto | null
}

function rankWinRate(rank: RankedRankDto) {
    const games = rank.wins + rank.losses

    if (games === 0) {
        return 0
    } else {
        return (rank.wins / games) * 100
    }
}

function rankLabel(rank: RankedRankDto) {
    const tierLabel = formatRank(rank.tier)

    if (
        rank.tier === 'MASTER' ||
        rank.tier === 'GRANDMASTER' ||
        rank.tier === 'CHALLENGER'
    ) {
        return tierLabel
    }

    return `${tierLabel} ${rank.rank}`
}

function RankCard({ title, rank }: RankCardProps) {
    let rankContent

    if (rank) {
        rankContent = (
            <>
                <div className="mt-3 flex items-center gap-3">
                    <img
                        src={rankEmblemUrl(rank.tier)}
                        alt={`Emblema de ${formatRank(rank.tier)}`}
                        className="h-16 w-16 shrink-0 object-contain"
                    />

                    <div className="min-w-0 flex-1">
                        <p className="text-lg font-bold text-slate-100">
                            {rankLabel(rank)}
                        </p>

                        <div className="mt-1 flex items-center justify-between gap-2">
                            <p className="text-sm text-cyan-300">
                                {rank.leaguePoints} LP
                            </p>

                            <span className="rounded-md bg-slate-800 px-2 py-1 text-xs text-slate-300">
                                {rankWinRate(rank).toFixed(1)}% WR
                            </span>
                        </div>
                    </div>
                </div>

                <div className="mt-4 flex gap-4 border-t border-slate-800 pt-3 text-sm">
                    <span className="text-emerald-300">{rank.wins} victorias</span>
                    <span className="text-rose-300">{rank.losses} derrotas</span>
                </div>
            </>
        )
    } else {
        rankContent = (
            <p className="mt-3 text-sm text-slate-400">
                Sin clasificación disponible.
            </p>
        )
    }

    return (
        <section className="rounded-lg border border-slate-800 bg-slate-900 p-4">
            <p className="text-xs font-semibold uppercase text-slate-500">{title}</p>

            {rankContent}
        </section>
    )
}

export default RankCard
