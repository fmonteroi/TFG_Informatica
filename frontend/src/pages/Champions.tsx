import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAllChampions, refreshProfessionals } from '../api/backendApi'
import { useDragontailAssets } from '../lib/dragontail'
import type { ChampionDto } from '../types/api'
import { safeError } from '../lib/errors'
import { CARD_CLASS } from '../lib/constants'

const PROFESSIONALS_REFRESH_KEY = 'professionals-last-refresh'
const PROFESSIONALS_REFRESH_INTERVAL_MS = 30 * 60 * 1000

async function refreshProfessionalsIfNeeded(force: boolean) {
    const lastRefreshRaw = localStorage.getItem(PROFESSIONALS_REFRESH_KEY)
    const lastRefresh = lastRefreshRaw ? Number(lastRefreshRaw) : 0
    const now = Date.now()
    const isExpired = now - lastRefresh > PROFESSIONALS_REFRESH_INTERVAL_MS

    if (!force && !isExpired) {
        return null
    }

    const result = await refreshProfessionals()
    if (result.message !== 'Ya hay un refresco de profesionales en curso.') {
        localStorage.setItem(PROFESSIONALS_REFRESH_KEY, String(now))
    }

    return result
}

function Champions() {
    const navigate = useNavigate()
    const { championMap } = useDragontailAssets()

    const [champions, setChampions] = useState<ChampionDto[]>([])
    const [loadingChampions, setLoadingChampions] = useState(true)
    const [refreshingProfessionalsManually, setRefreshingProfessionalsManually] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [refreshMessage, setRefreshMessage] = useState<string | null>(null)
    const [refreshWarning, setRefreshWarning] = useState<string | null>(null)
    const [searchTerm, setSearchTerm] = useState('')

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

        async function refreshInBackground() {
            try {
                await refreshProfessionalsIfNeeded(false)
            } catch {
                // ignores any error
            }
        }

        if (!cancelled) {
            void refreshInBackground()
        }

        return () => {
            cancelled = true
        }
    }, [])

    const normalizedSearch = searchTerm.trim().toLowerCase()

    const searchSuggestions = normalizedSearch
        ? champions
            .filter((champion) =>
                champion.championName.toLowerCase().includes(normalizedSearch),
            )
            .slice(0, 6)
        : []

    function handleSelectChampion(championId: number) {
        navigate(`/campeones/${championId}`)
    }

    function handleSearchSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()

        if (searchSuggestions.length === 0) {
            return
        }

        handleSelectChampion(searchSuggestions[0].championId)
    }

    async function handleManualRefresh() {
        if (refreshingProfessionalsManually) {
            return
        }
        try {
            setRefreshingProfessionalsManually(true)
            setError(null)
            setRefreshMessage(null)
            setRefreshWarning(null)

            const result = await refreshProfessionalsIfNeeded(true)

            if (result) {
                if (result.stoppedByRateLimit || result.message === 'Ya hay un refresco de profesionales en curso.') {
                    setRefreshWarning(result.message)
                } else {
                    setRefreshMessage(result.message)
                }
            }

        } catch (error) {
            setError(safeError(error))
        } finally {
            setRefreshingProfessionalsManually(false)
        }
    }

    return (
        <div className="space-y-6">
            {error && (
                <section className={CARD_CLASS}>
                    <p>Error: {error}</p>
                </section>
            )}

            <section className={`${CARD_CLASS} space-y-4`}>
                <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
                    <div>
                        <h2 className="text-2xl font-bold">Buscar campeón</h2>
                    </div>

                    <button
                        onClick={handleManualRefresh}
                        disabled={refreshingProfessionalsManually}
                        className={[
                            'rounded-xl px-4 py-3 font-bold text-slate-950 transition',
                            refreshingProfessionalsManually
                                ? 'cursor-not-allowed bg-cyan-300'
                                : 'bg-cyan-500 hover:bg-cyan-400',
                        ].join(' ')}
                    >
                        {refreshingProfessionalsManually ? 'Refrescando pros...' : 'Refrescar profesionales'}
                    </button>
                </div>

                <div className="relative">
                    <form onSubmit={handleSearchSubmit}>
                        <input
                            className="w-full rounded-2xl border border-slate-700 bg-slate-950 px-4 py-4 text-lg outline-none transition focus:border-cyan-400"
                            placeholder="Escribe un campeón..."
                            value={searchTerm}
                            onChange={(event) => setSearchTerm(event.target.value)}
                        />
                    </form>

                    {searchSuggestions.length > 0 && (
                        <div className="absolute left-0 right-0 top-full z-10 mt-2 overflow-hidden rounded-2xl border border-slate-800 bg-slate-950 shadow-2xl">
                            {searchSuggestions.map((champion) => {
                                const championIcon = championMap?.get(champion.championId) ?? null

                                return (
                                    <button
                                        key={champion.championId}
                                        type="button"
                                        onClick={() => handleSelectChampion(champion.championId)}
                                        className="flex w-full items-center gap-3 border-b border-slate-800 px-4 py-3 text-left transition last:border-b-0 hover:bg-slate-900"
                                    >
                                        {championIcon ? (
                                            <img
                                                src={championIcon}
                                                alt={champion.championName}
                                                className="h-10 w-10 rounded-xl"
                                            />
                                        ) : (
                                            <div className="h-10 w-10 rounded-xl border border-slate-700 bg-slate-800" />
                                        )}

                                        <span className="font-medium text-slate-100">
                                            {champion.championName}
                                        </span>
                                    </button>
                                )
                            })}
                        </div>
                    )}
                </div>

                {refreshingProfessionalsManually && (
                    <p className="text-sm font-medium text-slate-400">
                        Actualizando partidas recientes de profesionales...
                    </p>
                )}

                {refreshMessage && (
                    <p className="text-sm font-medium text-emerald-400">
                        {refreshMessage}
                    </p>
                )}

                {refreshWarning && (
                    <p className="text-sm font-medium text-amber-400">
                        {refreshWarning}
                    </p>
                )}
            </section>

            <section className={CARD_CLASS}>
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

                            return (
                                <button
                                    key={champion.championId}
                                    onClick={() => handleSelectChampion(champion.championId)}
                                    className="rounded-2xl border border-slate-800 bg-slate-950 p-3 text-center transition hover:border-slate-600"
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
        </div>
    )
}

export default Champions
