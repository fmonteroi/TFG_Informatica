import ParticipationCard from '../ParticipationCard.tsx'
import MatchScoreboard from '../MatchScoreboard.tsx'
import { formatDate, formatDuration } from '../../lib/format.ts'
import { queueLabel } from '../../lib/lol.ts'
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
                    const championIcon =
                        participation.championId && championMap
                            ? championMap.get(participation.championId) ?? null
                            : null

                    const spellIds = [
                        participation.build?.summoner1Id ?? null,
                        participation.build?.summoner2Id ?? null,
                    ]

                    const mainItemIds = participation.build
                        ? [
                            participation.build.item0,
                            participation.build.item1,
                            participation.build.item2,
                            participation.build.item3,
                            participation.build.item4,
                            participation.build.item5,
                        ]
                        : []

                    const isExpanded = expandedMatchId === participation.matchId
                    const expandedMatch = participation.matchId
                        ? matchCache[participation.matchId] ?? null
                        : null

                    return (
                        <ParticipationCard
                            key={participation.id}
                            tone={participation.win ? 'win' : 'loss'}
                            topLeft={
                                <span
                                    className={[
                                        'rounded-xl px-3 py-2 text-sm font-bold uppercase tracking-wide',
                                        participation.win
                                            ? 'bg-emerald-500/20 text-emerald-300'
                                            : 'bg-rose-500/20 text-rose-300',
                                    ].join(' ')}
                                >
                                    {participation.win ? 'Victoria' : 'Derrota'}
                                </span>
                            }
                            topRight={
                                <span className="text-sm text-slate-300">
                                    {formatDate(participation.gameStartAt)}
                                </span>
                            }
                            championIcon={championIcon}
                            championName={participation.championName}
                            summaryLine={`${participation.kills}/${participation.deaths}/${participation.assists} · ${
                                participation.teamPosition || 'Sin rol'
                            } · ${queueLabel(participation.queueId)}`}
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
                            {loadingMatchId === participation.matchId && !expandedMatch ? (
                                <p className="text-slate-400">Cargando detalle de la partida...</p>
                            ) : expandedMatch ? (
                                <MatchScoreboard
                                    match={expandedMatch}
                                    playerPuuid={playerPuuid}
                                    championMap={championMap}
                                    spellMap={summonerSpellMap}
                                    itemInfoMap={itemInfoMap}
                                    queueLabel={queueLabel}
                                    formatDuration={formatDuration}
                                    formatDate={formatDate}
                                />
                            ) : null}
                        </ParticipationCard>
                    )
                })}
            </div>
        </section>
    )
}

export default PlayerHistory
