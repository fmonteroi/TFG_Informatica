import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getMatchById, refreshPlayer, searchPlayer } from '../api/backendApi'
import { getItemImageUrl, getProfileIconUrl, useDragontailAssets } from '../lib/dragontail'
import type { BuildDto, MatchDetailsDto, ParticipationDto, PlayerWithParticipationsDto } from '../types/api'

const cardClass = 'rounded-2xl border border-slate-800 bg-slate-900 p-4'

function safeError(error: unknown) {
    if (error instanceof Error) {
        return error.message
    }

    return 'Ha ocurrido un error desconocido'
}

function formatDate(value: string) {
    return new Intl.DateTimeFormat('es-ES', {
        dateStyle: 'medium',
        timeStyle: 'short',
    }).format(new Date(value))
}

function formatDuration(seconds: number) {
    const minutes = Math.floor(seconds / 60)
    const remainingSeconds = seconds % 60

    return `${minutes}:${String(remainingSeconds).padStart(2, '0')}`
}

function queueLabel(queueId: number) {
    if (queueId === 420) return 'Ranked Solo/Duo'
    if (queueId === 440) return 'Ranked Flex'
    if (queueId === 450) return 'ARAM'
    if (queueId === 400) return 'Normal Draft'
    if (queueId === 430) return 'Normal Blind'

    return 'Modo especial'
}

function positionOrder(position: string) {
    const order: Record<string, number> = {
        TOP: 1,
        JUNGLE: 2,
        MIDDLE: 3,
        BOTTOM: 4,
        UTILITY: 5,
    }

    return order[position] ?? 99
}

function sortMatchParticipations(participations: ParticipationDto[]) {
    return [...participations].sort((a, b) => {
        return (
            a.teamId - b.teamId ||
            positionOrder(a.teamPosition) - positionOrder(b.teamPosition) ||
            (a.gameName ?? '').localeCompare(b.gameName ?? '')
        )
    })
}

function BuildStrip({
                        build,
                        spellMap,
                    }: {
    build: BuildDto | null
    spellMap: Map<number, string> | null
}) {
    if (!build) {
        return <p className="text-sm text-slate-500">Sin build registrada</p>
    }

    const spellIds = [build.summoner1Id, build.summoner2Id]

    // Builds items
    const mainItemIds = [
        build.item0,
        build.item1,
        build.item2,
        build.item3,
        build.item4,
        build.item5,
    ]

    // Trinket
    const trinketIcon = getItemImageUrl(build.item6)

    // roleBoundItem
    const roleBoundItemIcon = getItemImageUrl(build.roleBoundItem)

    return (
        <div className="space-y-3">
            <div className="flex gap-2">
                {spellIds.map((spellId, index) => {
                    const spellIcon = spellId && spellMap ? spellMap.get(spellId) : null

                    if (!spellIcon) {
                        return (
                            <div
                                key={`spell-empty-${index}`}
                                className="h-8 w-8 rounded-lg border border-slate-700 bg-slate-800"
                            />
                        )
                    }

                    return (
                        <img
                            key={`spell-${index}`}
                            src={spellIcon}
                            alt={`Summoner spell ${spellId}`}
                            className="h-8 w-8 rounded-lg"
                        />
                    )
                })}
            </div>

            <div className="flex flex-wrap gap-2">
                {mainItemIds.map((itemId, index) => {
                    const itemIcon = getItemImageUrl(itemId)

                    if (!itemIcon) {
                        return (
                            <div
                                key={`item-empty-${index}`}
                                className="h-10 w-10 rounded-lg border border-slate-700 bg-slate-800"
                            />
                        )
                    }

                    return (
                        <img
                            key={`item-${index}`}
                            src={itemIcon}
                            alt={`Item ${itemId}`}
                            className="h-10 w-10 rounded-lg"
                        />
                    )
                })}
            </div>

            <div className="flex flex-wrap gap-4">
                {trinketIcon && (
                    <div className="space-y-1">
                        <p className="text-xs uppercase tracking-wide text-slate-500">
                            Trinket
                        </p>
                        <img
                            src={trinketIcon}
                            alt={`Trinket ${build.item6}`}
                            className="h-10 w-10 rounded-lg"
                        />
                    </div>
                )}

                {roleBoundItemIcon && (
                    <div className="space-y-1">
                        <p className="text-xs uppercase tracking-wide text-slate-500">
                            Slot especial
                        </p>
                        <img
                            src={roleBoundItemIcon}
                            alt={`Role bound item ${build.roleBoundItem}`}
                            className="h-10 w-10 rounded-lg"
                        />
                    </div>
                )}
            </div>
        </div>
    )
}


function Player() {
    const { platform = '', gameName = '', tagLine = '' } = useParams()

    const decodedPlatform = decodeURIComponent(platform)
    const decodedGameName = decodeURIComponent(gameName)
    const decodedTagLine = decodeURIComponent(tagLine)

    const { championMap, summonerSpellMap } = useDragontailAssets()

    const [playerData, setPlayerData] = useState<PlayerWithParticipationsDto | null>(null)
    const [selectedMatchId, setSelectedMatchId] = useState<string | null>(null)
    const [matchCache, setMatchCache] = useState<Record<string, MatchDetailsDto>>({})
    const [loadingPlayer, setLoadingPlayer] = useState(true)
    const [loadingMatchId, setLoadingMatchId] = useState<string | null>(null)
    const [refreshingPlayer, setRefreshingPlayer] = useState(false)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadPlayer() {
            try {
                setLoadingPlayer(true)
                setError(null)
                setSelectedMatchId(null)
                setMatchCache({})

                const data = await searchPlayer(decodedPlatform, decodedGameName, decodedTagLine)

                if (!cancelled) {
                    setPlayerData(data)
                }
            } catch (error) {
                if (!cancelled) {
                    setError(safeError(error))
                }
            } finally {
                if (!cancelled) {
                    setLoadingPlayer(false)
                }
            }
        }

        void loadPlayer()

        return () => {
            cancelled = true
        }
    }, [decodedPlatform, decodedGameName, decodedTagLine])

    async function handleRefreshPlayer() {
        if (!playerData) {
            return
        }

        try {
            setRefreshingPlayer(true)
            setError(null)

            const refreshedData = await refreshPlayer(decodedPlatform, playerData.player.puuid)

            setPlayerData(refreshedData)
            setSelectedMatchId(null)
            setMatchCache({})
        } catch (error) {
            setError(safeError(error))
        } finally {
            setRefreshingPlayer(false)
        }
    }

    async function handleSelectParticipation(participation: ParticipationDto) {
        if (!participation.matchId) {
            return
        }

        setSelectedMatchId(participation.matchId)

        if (matchCache[participation.matchId]) {
            return
        }

        try {
            setLoadingMatchId(participation.matchId)

            const matchDetails = await getMatchById(participation.matchId)

            setMatchCache((previousCache) => ({
                ...previousCache,
                [participation.matchId!]: {
                    ...matchDetails,
                    participations: sortMatchParticipations(matchDetails.participations),
                },
            }))
        } catch (error) {
            setError(safeError(error))
        } finally {
            setLoadingMatchId(null)
        }
    }

    if (loadingPlayer) {
        return <div className={cardClass}>Cargando jugador...</div>
    }

    if (error) {
        return <div className={cardClass}>Error: {error}</div>
    }

    if (!playerData) {
        return <div className={cardClass}>No se encontró el jugador.</div>
    }

    const selectedMatch = selectedMatchId ? matchCache[selectedMatchId] : null

    return (
        <div className="space-y-6">
            <section className={`${cardClass} flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between`}>
                <div className="flex items-center gap-4">
                    <img
                        src={getProfileIconUrl(playerData.player.profileIconId)}
                        alt="Profile icon"
                        className="h-20 w-20 rounded-2xl"
                    />

                    <div>
                        <h1 className="text-3xl font-black">
                            {playerData.player.gameName}#{playerData.player.tagLine}
                        </h1>

                        <p className="text-slate-400">
                            {playerData.player.platform} · Nivel {playerData.player.summonerLevel}
                        </p>

                        <p className="text-sm text-slate-500">
                            Última sincronización:{' '}
                            {playerData.player.lastSyncAt
                                ? formatDate(playerData.player.lastSyncAt)
                                : 'Sin sincronizar'}
                        </p>
                    </div>
                </div>

                <button
                    onClick={handleRefreshPlayer}
                    className="rounded-xl bg-cyan-500 px-4 py-3 font-bold text-slate-950 transition hover:bg-cyan-400"
                >
                    {refreshingPlayer ? 'Refrescando...' : 'Refrescar historial'}
                </button>
            </section>

            <div className="grid gap-6 xl:grid-cols-[420px_1fr]">
                <section className={`${cardClass} space-y-4`}>
                    <div className="flex items-center justify-between">
                        <h2 className="text-2xl font-bold">Participaciones recientes</h2>
                        <span className="text-sm text-slate-400">
              {playerData.participations.length} partidas
            </span>
                    </div>

                    {playerData.participations.map((participation) => {
                        const championIcon =
                            participation.championId && championMap
                                ? championMap.get(participation.championId) ?? null
                                : null

                        const isSelected = selectedMatchId === participation.matchId

                        return (
                            <button
                                key={participation.id}
                                onClick={() => handleSelectParticipation(participation)}
                                className={[
                                    'w-full rounded-2xl border p-4 text-left transition',
                                    isSelected
                                        ? 'border-cyan-400 bg-slate-800'
                                        : 'border-slate-800 bg-slate-950 hover:border-slate-600',
                                ].join(' ')}
                            >
                                <div className="flex items-start gap-4">
                                    {championIcon ? (
                                        <img
                                            src={championIcon}
                                            alt={participation.championName ?? 'Champion'}
                                            className="h-16 w-16 rounded-xl"
                                        />
                                    ) : (
                                        <div className="h-16 w-16 rounded-xl border border-slate-700 bg-slate-800" />
                                    )}

                                    <div className="min-w-0 flex-1">
                                        <div className="flex items-center justify-between gap-3">
                                            <p className={participation.win ? 'font-bold text-emerald-400' : 'font-bold text-rose-400'}>
                                                {participation.win ? 'Victoria' : 'Derrota'}
                                            </p>

                                            <p className="text-sm text-slate-400">
                                                {formatDate(participation.gameStartAt)}
                                            </p>
                                        </div>

                                        <p className="mt-1 text-lg font-semibold">
                                            {participation.championName}
                                        </p>

                                        <p className="text-sm text-slate-400">
                                            {participation.kills}/{participation.deaths}/{participation.assists} ·{' '}
                                            {participation.teamPosition || 'Sin rol'} ·{' '}
                                            {queueLabel(participation.queueId)}
                                        </p>

                                        <div className="mt-3">
                                            <BuildStrip build={participation.build} spellMap={summonerSpellMap} />
                                        </div>
                                    </div>
                                </div>
                            </button>
                        )
                    })}
                </section>

                <section className={cardClass}>
                    {!selectedMatchId && (
                        <p className="text-slate-400">
                            Selecciona una participación para cargar el detalle de la partida.
                        </p>
                    )}

                    {selectedMatchId && loadingMatchId === selectedMatchId && (
                        <p className="text-slate-400">Cargando detalle de la partida...</p>
                    )}

                    {selectedMatch && (
                        <div className="space-y-6">
                            <div>
                                <h2 className="text-2xl font-bold">Detalle de la partida</h2>
                                <p className="text-slate-400">
                                    {queueLabel(selectedMatch.queueId)} ·{' '}
                                    {formatDuration(selectedMatch.gameDuration)} ·{' '}
                                    {formatDate(selectedMatch.gameStartAt)} ·{' '}
                                    Parche {selectedMatch.gameVersion}
                                </p>
                            </div>

                            {[100, 200].map((teamId) => {
                                const teamParticipations = selectedMatch.participations.filter(
                                    (participation) => participation.teamId === teamId,
                                )

                                return (
                                    <div key={teamId} className="space-y-3">
                                        <h3 className="text-lg font-bold">
                                            {teamId === 100 ? 'Equipo azul' : 'Equipo rojo'}
                                        </h3>

                                        {teamParticipations.map((participation) => {
                                            const championIcon =
                                                participation.championId && championMap
                                                    ? championMap.get(participation.championId) ?? null
                                                    : null

                                            const isFocusedPlayer =
                                                participation.puuid === playerData.player.puuid

                                            return (
                                                <div
                                                    key={participation.id}
                                                    className={[
                                                        'grid gap-4 rounded-2xl border p-4 lg:grid-cols-[220px_1fr_240px]',
                                                        isFocusedPlayer
                                                            ? 'border-cyan-400 bg-slate-800'
                                                            : 'border-slate-800 bg-slate-950',
                                                    ].join(' ')}
                                                >
                                                    <div className="flex items-center gap-3">
                                                        {championIcon ? (
                                                            <img
                                                                src={championIcon}
                                                                alt={participation.championName ?? 'Champion'}
                                                                className="h-14 w-14 rounded-xl"
                                                            />
                                                        ) : (
                                                            <div className="h-14 w-14 rounded-xl border border-slate-700 bg-slate-800" />
                                                        )}

                                                        <div>
                                                            <p className="font-bold">
                                                                {participation.gameName}#{participation.tagLine}
                                                            </p>

                                                            <p className="text-sm text-slate-400">
                                                                {participation.championName} ·{' '}
                                                                {participation.teamPosition || 'Sin rol'}
                                                            </p>
                                                        </div>
                                                    </div>

                                                    <div className="flex items-center">
                                                        <p className="text-lg font-semibold">
                                                            {participation.kills}/{participation.deaths}/{participation.assists}
                                                        </p>
                                                    </div>

                                                    <BuildStrip
                                                        build={participation.build}
                                                        spellMap={summonerSpellMap}
                                                    />
                                                </div>
                                            )
                                        })}
                                    </div>
                                )
                            })}
                        </div>
                    )}
                </section>
            </div>
        </div>
    )
}

export default Player
