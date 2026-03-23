import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getAllChampions, getChampionBuilds, refreshProfessionals } from '../api/backendApi'
import { getItemImageUrl, useDragontailAssets } from '../lib/dragontail'
import type { BuildDto, ChampionDto, ProBuildDto } from '../types/api'

const cardClass = 'rounded-2xl border border-slate-800 bg-slate-900 p-4'
const PROFESSIONALS_REFRESH_KEY = 'professionals-last-refresh'
const PROFESSIONALS_REFRESH_INTERVAL_MS = 30 * 60 * 1000

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

function queueLabel(queueId: number) {
    if (queueId === 420) return 'Ranked Solo/Duo'
    if (queueId === 440) return 'Ranked Flex'
    if (queueId === 450) return 'ARAM'
    if (queueId === 400) return 'Normal Draft'
    if (queueId === 430) return 'Normal Blind'

    return 'Modo especial'
}

async function refreshProfessionalsIfNeeded(force: boolean) {
    const lastRefreshRaw = localStorage.getItem(PROFESSIONALS_REFRESH_KEY)
    const lastRefresh = lastRefreshRaw ? Number(lastRefreshRaw) : 0
    const now = Date.now()
    const isExpired = now - lastRefresh > PROFESSIONALS_REFRESH_INTERVAL_MS

    if (!force && !isExpired) {
        return null
    }

    const result = await refreshProfessionals()
    localStorage.setItem(PROFESSIONALS_REFRESH_KEY, String(now))

    return result
}

function BuildStrip({
                        build,
                        spellMap,
                    }: {
    build: BuildDto
    spellMap: Map<number, string> | null
}) {
    const spellIds = [build.summoner1Id, build.summoner2Id]

    const mainItemIds = [
        build.item0,
        build.item1,
        build.item2,
        build.item3,
        build.item4,
        build.item5,
    ]

    const trinketIcon = getItemImageUrl(build.item6)
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

function Champions() {
    const navigate = useNavigate()
    const { championId } = useParams()

    const { championMap, summonerSpellMap } = useDragontailAssets()

    const [champions, setChampions] = useState<ChampionDto[]>([])
    const [builds, setBuilds] = useState<ProBuildDto[]>([])
    const [loadingChampions, setLoadingChampions] = useState(true)
    const [loadingBuilds, setLoadingBuilds] = useState(false)
    const [refreshingProfessionals, setRefreshingProfessionals] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [refreshMessage, setRefreshMessage] = useState<string | null>(null)
    const [refreshWarning, setRefreshWarning] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadChampions() {
            try {
                setLoadingChampions(true)
                setError(null)

                const championList = await getAllChampions()

                if (!cancelled) {
                    setChampions(championList)
                }
            } catch (error) {
                if (!cancelled) {
                    setError(safeError(error))
                }
            } finally {
                if (!cancelled) {
                    setLoadingChampions(false)
                }
            }
        }

        void loadChampions()

        return () => {
            cancelled = true
        }
    }, [])

    useEffect(() => {
        let cancelled = false

        async function loadBuilds() {
            if (!championId) {
                setBuilds([])
                return
            }

            try {
                setLoadingBuilds(true)
                setError(null)

                const championBuilds = await getChampionBuilds(championId, 10)

                if (!cancelled) {
                    setBuilds(championBuilds)
                }
            } catch (error) {
                if (!cancelled) {
                    setError(safeError(error))
                }
            } finally {
                if (!cancelled) {
                    setLoadingBuilds(false)
                }
            }
        }

        void loadBuilds()

        return () => {
            cancelled = true
        }
    }, [championId])

    useEffect(() => {
        let cancelled = false

        async function refreshInBackground() {
            try {
                setRefreshingProfessionals(true)

                const result = await refreshProfessionalsIfNeeded(false)

                if (!cancelled && result && championId) {
                    const championBuilds = await getChampionBuilds(championId, 10)
                    setBuilds(championBuilds)
                }
            } catch {
                // ignoramos errores del refresco en segundo plano
            } finally {
                if (!cancelled) {
                    setRefreshingProfessionals(false)
                }
            }
        }

        void refreshInBackground()

        return () => {
            cancelled = true
        }
    }, [championId])

    async function handleSelectChampion(selectedChampionId: number) {
        navigate(`/campeones/${selectedChampionId}`)
    }

    async function handleManualRefresh() {
        try {
            setRefreshingProfessionals(true)
            setError(null)
            setRefreshMessage(null)
            setRefreshWarning(null)

            const result = await refreshProfessionalsIfNeeded(true)

            if (result) {
                if (result.stoppedByRateLimit) {
                    setRefreshWarning(
                        `Se actualizaron ${result.successfulProfessionals} de ${result.totalProfessionals} profesionales. Se alcanzó el límite de Riot.`
                    )
                } else {
                    setRefreshMessage(
                        `Se actualizaron ${result.successfulProfessionals} profesionales correctamente.`
                    )
                }
            }

            if (championId) {
                const championBuilds = await getChampionBuilds(championId, 10)
                setBuilds(championBuilds)
            }
        } catch (error) {
            setError(safeError(error))
        } finally {
            setRefreshingProfessionals(false)
        }
    }

    return (
        <div className="space-y-6">
            <section className={`${cardClass} flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between`}>
                <div>
                    <h1 className="text-3xl font-black">Campeones</h1>
                    <p className="text-slate-400">
                        Explora campeones y consulta builds recientes de profesionales.
                    </p>
                </div>

                <button
                    onClick={handleManualRefresh}
                    className="rounded-xl bg-cyan-500 px-4 py-3 font-bold text-slate-950 transition hover:bg-cyan-400"
                >
                    {refreshingProfessionals ? 'Refrescando pros...' : 'Refrescar profesionales'}
                </button>
            </section>

            {error && (
                <section className={cardClass}>
                    <p>Error: {error}</p>
                </section>
            )}

            {refreshMessage && (
                <section className={cardClass}>
                    <p className="text-emerald-400">{refreshMessage}</p>
                </section>
            )}

            {refreshWarning && (
                <section className={cardClass}>
                    <p className="text-amber-400">{refreshWarning}</p>
                </section>
            )}

            {refreshingProfessionals && (
                <section className={cardClass}>
                    <p className="text-slate-400">
                        Actualizando partidas recientes de profesionales...
                    </p>
                </section>
            )}

            <section className={cardClass}>
                <div className="mb-4 flex items-center justify-between">
                    <h2 className="text-2xl font-bold">Todos los campeones</h2>
                    <span className="text-sm text-slate-400">{champions.length} campeones</span>
                </div>

                {loadingChampions ? (
                    <p className="text-slate-400">Cargando campeones...</p>
                ) : (
                    <div className="grid grid-cols-2 gap-3 sm:grid-cols-4 lg:grid-cols-6 xl:grid-cols-8">
                        {champions.map((champion) => {
                            const championIcon = championMap?.get(champion.championId) ?? null
                            const isSelected = championId === String(champion.championId)

                            return (
                                <button
                                    key={champion.championId}
                                    onClick={() => handleSelectChampion(champion.championId)}
                                    className={[
                                        'rounded-2xl border p-3 text-center transition',
                                        isSelected
                                            ? 'border-cyan-400 bg-slate-800'
                                            : 'border-slate-800 bg-slate-950 hover:border-slate-600',
                                    ].join(' ')}
                                >
                                    {championIcon ? (
                                        <img
                                            src={championIcon}
                                            alt={champion.championName}
                                            className="mx-auto h-16 w-16 rounded-xl"
                                        />
                                    ) : (
                                        <div className="mx-auto h-16 w-16 rounded-xl border border-slate-700 bg-slate-800" />
                                    )}

                                    <p className="mt-2 text-sm font-medium">
                                        {champion.championName}
                                    </p>
                                </button>
                            )
                        })}
                    </div>
                )}
            </section>

            {championId && (
                <section className={cardClass}>
                    <h2 className="mb-4 text-2xl font-bold">Builds recientes de profesionales</h2>

                    {loadingBuilds ? (
                        <p className="text-slate-400">Cargando builds...</p>
                    ) : builds.length === 0 ? (
                        <p className="text-slate-400">
                            No hay builds disponibles para este campeón.
                        </p>
                    ) : (
                        <div className="space-y-4">
                            {builds.map((build) => (
                                <article
                                    key={build.buildId}
                                    className="rounded-2xl border border-slate-800 bg-slate-950 p-4"
                                >
                                    <div className="mb-3 flex flex-col gap-2 lg:flex-row lg:items-center lg:justify-between">
                                        <div>
                                            <p className="text-lg font-bold">
                                                {build.proName} · {build.teamName} · {build.league}
                                            </p>

                                            <p className="text-slate-400">
                                                {build.gameName}#{build.tagLine} · {build.teamPosition} · {queueLabel(build.queueId)}
                                            </p>
                                        </div>

                                        <div className="text-sm text-slate-500">
                                            {formatDate(build.gameStartAt)} · {build.matchId}
                                        </div>
                                    </div>

                                    <BuildStrip build={build.build} spellMap={summonerSpellMap} />
                                </article>
                            ))}
                        </div>
                    )}
                </section>
            )}
        </div>
    )
}

export default Champions
