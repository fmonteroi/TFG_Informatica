import ParticipationCard from '../ParticipationCard.tsx'
import MatchScoreboard from '../MatchScoreboard.tsx'
import { formatDate, formatDuration } from '../../lib/format.ts'
import { formatRole, queueLabel } from '../../lib/lol.ts'
import type { ItemInfo } from '../../lib/dragontail.ts'
import type { MatchDetailsDto, ParticipationDto } from '../../types/api.ts'

type PlayerHistoryProps = {
    participations: ParticipationDto[]
    expandedMatchId: string | null
    matchCache: Record<string, MatchDetailsDto>
    loadingMatchId: string | null
    championMap: Map<number, string> | null
    summonerSpellMap: Map<number, string> | null
    itemInfoMap: Map<number, ItemInfo> | null
    playerPuuid: string
    playerPlatform: string
    onToggleParticipation: (participation: ParticipationDto) => void
}

function PlayerHistory({
                           participations,
                           expandedMatchId,
                           matchCache,
                           loadingMatchId,
                           championMap,
                           summonerSpellMap,
                           itemInfoMap,
                           playerPuuid,
                           playerPlatform,
                           onToggleParticipation,
                       }: PlayerHistoryProps) {
    return (
        <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4 space-y-4">
            <div className="flex items-center justify-between">
                <h2 className="text-2xl font-bold">Historial</h2>
                <span className="text-sm text-slate-400">
                    {participations.length} partidas
                </span>
            </div>

            <div className="space-y-4">
                {participations.map((participation) => {
                    let championIcon = null

                    if (participation.championId != null && championMap != null) {
                        championIcon = championMap.get(participation.championId) ?? null
                    }

                    const spellIds = [
                        participation.build?.summoner1Id ?? null,
                        participation.build?.summoner2Id ?? null,
                    ]

                    const summaryParts = [
                        `${participation.kills}/${participation.deaths}/${participation.assists}`,
                    ]

                    if (participation.teamPosition) {
                        summaryParts.push(formatRole(participation.teamPosition))
                    }

                    summaryParts.push(queueLabel(participation.queueId))
                    const summaryLine = summaryParts.join(' · ')

                    let mainItemIds: Array<number | null> = []

                    if (participation.build) {
                        mainItemIds = [
                            participation.build.item0,
                            participation.build.item1,
                            participation.build.item2,
                            participation.build.item3,
                            participation.build.item4,
                            participation.build.item5,
                        ]
                    }

                    const isExpanded = expandedMatchId === participation.matchId
                    let expandedMatch = null

                    if (participation.matchId) {
                        expandedMatch = matchCache[participation.matchId] ?? null
                    }

                    let tone: 'win' | 'loss' = 'loss'
                    let resultClasses = 'bg-rose-500/20 text-rose-300'
                    let resultText = 'Derrota'

                    if (participation.win) {
                        tone = 'win'
                        resultClasses = 'bg-emerald-500/20 text-emerald-300'
                        resultText = 'Victoria'
                    }

                    let expandedContent = null

                    if (loadingMatchId === participation.matchId && !expandedMatch) {
                        expandedContent = (
                            <p className="text-slate-400">
                                Cargando detalle de la partida...
                            </p>
                        )
                    } else if (expandedMatch) {
                        expandedContent = (
                            <MatchScoreboard
                                match={expandedMatch}
                                playerPuuid={playerPuuid}
                                playerPlatform={playerPlatform}
                                championMap={championMap}
                                spellMap={summonerSpellMap}
                                itemInfoMap={itemInfoMap}
                                queueLabel={queueLabel}
                                formatDuration={formatDuration}
                                formatDate={formatDate}
                            />
                        )
                    }

                    return (
                        <ParticipationCard
                            key={participation.id}
                            tone={tone}
                            topLeft={
                                <span
                                    className={[
                                        'rounded-xl px-3 py-2 text-sm font-bold uppercase tracking-wide',
                                        resultClasses,
                                    ].join(' ')}
                                >
                                    {resultText}
                                </span>
                            }
                            topRight={
                                <span className="text-sm text-slate-300">
                                    {formatDate(participation.gameStartAt)}
                                </span>
                            }
                            championIcon={championIcon}
                            championName={participation.championName}
                            summaryLine={summaryLine}
                            spellIds={spellIds}
                            spellMap={summonerSpellMap}
                            mainItemIds={mainItemIds}
                            trinketItemId={participation.build?.item6 ?? null}
                            specialItemId={participation.build?.roleBoundItem ?? null}
                            showSpecialItem={participation.teamPosition === 'BOTTOM'}
                            itemInfoMap={itemInfoMap}
                            expanded={isExpanded}
                            onToggle={() => onToggleParticipation(participation)}
                        >
                            {expandedContent}
                        </ParticipationCard>
                    )
                })}
            </div>
        </section>
    )
}

export default PlayerHistory
