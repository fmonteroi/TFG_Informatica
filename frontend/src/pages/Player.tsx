import {useEffect, useState} from 'react'
import {useNavigate, useParams} from 'react-router-dom'
import {getCurrentGame, getMatchById, refreshPlayer, searchPlayer} from '../api/backendApi'
import PlayerSearchBar from '../components/player/PlayerSearchBar.tsx'
import {useDragontailAssets} from '../lib/dragontail'
import type {CurrentGameDto, MatchDetailsDto, ParticipationDto, PlayerDetailsDto} from '../types/api'
import { safeError } from '../lib/errors'
import { sortMatchParticipations } from '../lib/lol'
import { CARD_CLASS } from '../lib/constants'
import PlayerHeader from '../components/player/PlayerHeader.tsx'
import PlayerSidebar from '../components/player/PlayerSidebar.tsx'
import PlayerHistory from '../components/player/PlayerHistory.tsx'




function Player() {
    const navigate = useNavigate()
    const {platform = '', gameName = '', tagLine = ''} = useParams()

    const decodedPlatform = decodeURIComponent(platform)
    const decodedGameName = decodeURIComponent(gameName)
    const decodedTagLine = decodeURIComponent(tagLine)

    function handleSearch(platform: string, gameName: string, tagLine: string) {
        navigate(
            `/jugador/${encodeURIComponent(platform)}/${encodeURIComponent(gameName)}/${encodeURIComponent(tagLine)}`,
        )
    }

    const { dataDragonVersion, championMap, summonerSpellMap, itemInfoMap } = useDragontailAssets()


    const [playerData, setPlayerData] = useState<PlayerDetailsDto | null>(null)
    const [expandedMatchId, setExpandedMatchId] = useState<string | null>(null)
    const [matchCache, setMatchCache] = useState<Record<string, MatchDetailsDto>>({})
    const [loadingPlayer, setLoadingPlayer] = useState(true)
    const [loadingMatchId, setLoadingMatchId] = useState<string | null>(null)
    const [refreshingPlayer, setRefreshingPlayer] = useState(false)
    const [currentGame, setCurrentGame] = useState<CurrentGameDto | null>(null)
    const [loadingCurrentGame, setLoadingCurrentGame] = useState(false)

    const [playerError, setPlayerError] = useState<string | null>(null)
    const [refreshError, setRefreshError] = useState<string | null>(null)
    const [matchError, setMatchError] = useState<string | null>(null)
    const [currentGameError, setCurrentGameError] = useState<string | null>(null)


    useEffect(() => {
        let cancelled = false

        async function loadPlayer() {
            try {
                setLoadingPlayer(true)
                setPlayerError(null)
                setRefreshError(null)
                setMatchError(null)
                setExpandedMatchId(null)
                setMatchCache({})

                const data = await searchPlayer(decodedPlatform, decodedGameName, decodedTagLine)

                if (!cancelled) {
                    setPlayerData(data)
                }
            } catch (error) {
                if (!cancelled) {
                    setPlayerError(safeError(error))
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

    useEffect(() => {
        let cancelled = false

        async function loadCurrentGame() {
            if (!playerData?.player.puuid) {
                setCurrentGame(null)
                return
            }

            try {
                setLoadingCurrentGame(true)
                setCurrentGameError(null)

                const data = await getCurrentGame(playerData.player.puuid)

                if (!cancelled) {
                    setCurrentGame(data)
                }
            } catch (error) {
                if (!cancelled) {
                    setCurrentGameError(safeError(error))
                }
            } finally {
                if (!cancelled) {
                    setLoadingCurrentGame(false)
                }
            }
        }

        void loadCurrentGame()

        return () => {
            cancelled = true
        }
    }, [playerData?.player.puuid])


    async function handleRefreshPlayer() {
        if (!playerData) {
            return
        }

        try {
            setRefreshingPlayer(true)
            setRefreshError(null)

            const refreshedData = await refreshPlayer(decodedPlatform, playerData.player.puuid)

            setPlayerData(refreshedData)
            setExpandedMatchId(null)
            setMatchCache({})
        } catch (error) {
            setRefreshError(safeError(error))
        } finally {
            setRefreshingPlayer(false)
        }
    }

    async function handleToggleParticipation(participation: ParticipationDto) {
        if (!participation.matchId) {
            return
        }

        if (expandedMatchId === participation.matchId) {
            setExpandedMatchId(null)
            return
        }

        setExpandedMatchId(participation.matchId)

        if (matchCache[participation.matchId]) {
            return
        }

        try {
            setLoadingMatchId(participation.matchId)

            const matchDetails = await getMatchById(participation.matchId)

            setMatchCache((previousCache) => ({
                ...previousCache,
                [participation.matchId]: {
                    ...matchDetails,
                    participations: sortMatchParticipations(matchDetails.participations),
                },
            }))
        } catch (error) {
            setMatchError(safeError(error))
        } finally {
            setLoadingMatchId(null)
        }
    }

    return (
        <div className="space-y-6">
            <PlayerSearchBar
                onSearch={handleSearch}
                initialPlatform={decodedPlatform}
                initialGameName={decodedGameName}
                initialTagLine={decodedTagLine}
            />

            {loadingPlayer && (
                <section className={CARD_CLASS}>
                    <p>Cargando jugador...</p>
                </section>
            )}

            {!loadingPlayer && playerError && (
                <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                    <p>{playerError}</p>
                </section>
            )}

            {!loadingPlayer && !playerError && !playerData && (
                <section className={CARD_CLASS}>
                    <p>No se encontró el jugador.</p>
                </section>
            )}

            {refreshError && (
                <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                    <p>Error al refrescar el jugador: {refreshError}</p>
                </section>
            )}

            {matchError && (
                <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                    <p>Error al cargar la partida: {matchError}</p>
                </section>
            )}

            {!loadingPlayer && !playerError && playerData && (
                <>
                    <PlayerHeader
                        player={playerData.player}
                        dataDragonVersion={dataDragonVersion}
                        refreshing={refreshingPlayer}
                        onRefresh={handleRefreshPlayer}
                    />

                    <div className="grid gap-6 xl:grid-cols-[240px_1fr]">
                        <PlayerSidebar
                            currentGame={currentGame}
                            loadingCurrentGame={loadingCurrentGame}
                            currentGameError={currentGameError}
                            rankedRanks={playerData.rankedRanks}
                            stats={playerData.stats}
                            championMap={championMap}
                        />

                        <PlayerHistory
                            participations={playerData.participations}
                            expandedMatchId={expandedMatchId}
                            matchCache={matchCache}
                            loadingMatchId={loadingMatchId}
                            championMap={championMap}
                            summonerSpellMap={summonerSpellMap}
                            itemInfoMap={itemInfoMap}
                            playerPuuid={playerData.player.puuid}
                            playerPlatform={playerData.player.platform}
                            onToggleParticipation={handleToggleParticipation}
                        />
                    </div>
                </>
            )}
        </div>
    )
}

export default Player
