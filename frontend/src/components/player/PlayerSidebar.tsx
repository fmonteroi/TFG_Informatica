import CurrentGameStatusCard from '../CurrentGameStatusCard'
import type { CurrentGameDto, PlayerStatsDto, RankedRankDto } from '../../types/api'
import RankCard from './RankCard'
import PlayerStatsCard from './PlayerStatsCard'

type PlayerSidebarProps = {
    currentGame: CurrentGameDto | null
    loadingCurrentGame: boolean
    currentGameError: string | null
    rankedRanks: RankedRankDto[]
    stats: PlayerStatsDto | null
    championMap: Map<number, string> | null
}

function findRank(ranks: RankedRankDto[], queueType: string) {
    return ranks.find((rank) => rank.queueType === queueType) ?? null
}

function PlayerSidebar({
                           currentGame,
                           loadingCurrentGame,
                           currentGameError,
                           rankedRanks,
                           stats,
                           championMap,
                       }: PlayerSidebarProps) {
    const soloRank = findRank(rankedRanks, 'RANKED_SOLO_5x5')
    const flexRank = findRank(rankedRanks, 'RANKED_FLEX_SR')

    return (
        <aside className="space-y-4">
            <CurrentGameStatusCard
                currentGame={currentGame}
                loading={loadingCurrentGame}
                error={currentGameError}
                championMap={championMap}
            />
            <RankCard title="Solo / Duo" rank={soloRank} />
            <RankCard title="Flex" rank={flexRank} />
            <PlayerStatsCard stats={stats} championMap={championMap} />
        </aside>
    )
}

export default PlayerSidebar
