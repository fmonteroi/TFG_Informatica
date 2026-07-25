import type {ItemInfo} from '../lib/dragontail'
import ItemTooltip from './ItemTooltip'
import type {MatchDetailsDto} from '../types/api'
import { Link } from 'react-router-dom'

type MatchScoreboardProps = {
    match: MatchDetailsDto
    playerPuuid: string
    playerPlatform: string
    championMap: Map<number, string> | null
    spellMap: Map<number, string> | null
    queueLabel: (queueId: number) => string
    formatDuration: (seconds: number) => string
    formatDate: (value: string) => string
    itemInfoMap: Map<number, ItemInfo> | null
}

function scoreBoardRowClass(isFocusedPlayer: boolean) {
    return isFocusedPlayer
        ? 'border-cyan-400/50 bg-cyan-950/30'
        : 'border-slate-800 bg-slate-950/80'
}

function teamSectionClass(teamId: number) {
    return teamId === 100
        ? 'border-cyan-500/20 bg-cyan-950/10'
        : 'border-rose-500/20 bg-rose-950/10'
}

function MatchScoreboard({
                             match,
                             playerPuuid,
                             playerPlatform,
                             championMap,
                             spellMap,
                             queueLabel,
                             formatDuration,
                             formatDate,
                             itemInfoMap,
                         }: MatchScoreboardProps) {
    return (
        <div className="space-y-4">
            <div className="flex flex-wrap items-center gap-3 text-sm text-slate-400">
                <span>{queueLabel(match.queueId)}</span>
                <span>{formatDuration(match.gameDuration)}</span>
                <span>{formatDate(match.gameStartAt)}</span>
                <span>Parche {match.gameVersion}</span>
            </div>

            {[100, 200].map((teamId) => {
                const teamParticipations = match.participations.filter(
                    (participation) => participation.teamId === teamId,
                )

                return (
                    <section
                        key={teamId}
                        className={[
                            'space-y-3 rounded-2xl border p-4',
                            teamSectionClass(teamId),
                        ].join(' ')}
                    >
                        <div className="flex items-center justify-between">
                            <h3 className="text-sm font-bold uppercase tracking-wide text-slate-100">
                                {teamId === 100 ? 'Equipo azul' : 'Equipo rojo'}
                            </h3>

                            <span className="text-xs text-slate-300">
                                {teamParticipations.length} jugadores
                            </span>
                        </div>

                        <div className="space-y-2">
                            {teamParticipations.map((participation) => {
                                const championIcon =
                                    participation.championId && championMap
                                        ? championMap.get(participation.championId) ?? null
                                        : null

                                const isFocusedPlayer = participation.puuid === playerPuuid

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

                                return (
                                    <div
                                        key={participation.id}
                                        className={[
                                            'grid gap-3 rounded-2xl border p-3',
                                            'xl:grid-cols-[minmax(0,230px)_90px_minmax(0,1fr)]',
                                            scoreBoardRowClass(isFocusedPlayer),
                                        ].join(' ')}
                                    >
                                        <div className="flex min-w-0 items-center gap-3">
                                            {championIcon ? (
                                                <img
                                                    src={championIcon}
                                                    alt={participation.championName ?? 'Champion'}
                                                    className="h-12 w-12 rounded-xl"
                                                />
                                            ) : (
                                                <div
                                                    className="h-12 w-12 rounded-xl border border-slate-700 bg-slate-800"/>
                                            )}

                                            <div className="min-w-0">
                                                {participation.gameName && participation.tagLine ? (
                                                    <Link
                                                        to={`/jugador/${encodeURIComponent(playerPlatform)}/${encodeURIComponent(participation.gameName)}/${encodeURIComponent(participation.tagLine)}`}
                                                        className="block truncate font-bold text-slate-100 transition hover:text-cyan-300 hover:underline focus-visible:outline-2 focus-visible:outline-cyan-300"
                                                    >
                                                        {participation.gameName}#{participation.tagLine}
                                                    </Link>
                                                ) : (
                                                    <p className="truncate font-bold text-slate-400">
                                                        Jugador desconocido
                                                    </p>
                                                )}

                                                <p className="truncate text-sm text-slate-300">
                                                    {participation.championName} · {participation.teamPosition || 'Sin rol'}
                                                </p>
                                            </div>
                                        </div>

                                        <div className="flex items-center">
                                            <p className="text-lg font-semibold text-slate-100">
                                                {participation.kills}/{participation.deaths}/{participation.assists}
                                            </p>
                                        </div>

                                        <div className="flex flex-wrap items-center gap-1.5 xl:flex-nowrap">
                                            {spellIds.map((spellId, index) => {
                                                const spellIcon = spellId && spellMap ? spellMap.get(spellId) : null

                                                if (!spellIcon) {
                                                    return (
                                                        <div
                                                            key={`score-spell-empty-${participation.id}-${index}`}
                                                            className="h-9 w-9 rounded-lg border border-slate-700 bg-slate-800"
                                                        />
                                                    )
                                                }

                                                return (
                                                    <img
                                                        key={`score-spell-${participation.id}-${index}`}
                                                        src={spellIcon}
                                                        alt={`Summoner spell ${spellId}`}
                                                        className="h-9 w-9 rounded-lg"
                                                    />
                                                )
                                            })}

                                            <div className="h-9 w-4 shrink-0"/>

                                            {mainItemIds.map((itemId, index) => (
                                                <ItemTooltip
                                                    key={`score-item-${participation.id}-${index}`}
                                                    itemId={itemId}
                                                    itemInfoMap={itemInfoMap}
                                                    sizeClassName="h-9 w-9"
                                                    roundedClassName="rounded-lg"
                                                />
                                            ))}


                                            {participation.teamPosition === 'BOTTOM' && participation.build?.roleBoundItem && (
                                                <>
                                                    <div className="h-9 w-4 shrink-0"/>
                                                    <ItemTooltip
                                                        itemId={participation.build.roleBoundItem}
                                                        itemInfoMap={itemInfoMap}
                                                        sizeClassName="h-9 w-9"
                                                        roundedClassName="rounded-lg"
                                                    />
                                                </>
                                            )}


                                            <div className="h-9 w-4 shrink-0"/>

                                            <div className="h-9 w-4 shrink-0"/>
                                            <ItemTooltip
                                                itemId={participation.build?.item6 ?? null}
                                                itemInfoMap={itemInfoMap}
                                                sizeClassName="h-9 w-9"
                                                roundedClassName="rounded-lg"
                                            />

                                        </div>
                                    </div>
                                )
                            })}
                        </div>
                    </section>
                )
            })}
        </div>
    )
}

export default MatchScoreboard
