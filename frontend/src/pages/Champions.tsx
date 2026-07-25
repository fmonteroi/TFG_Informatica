import { useEffect, useMemo, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAllChampions } from '../api/backendApi'
import { useDragontailAssets } from '../lib/dragontail'
import type { ChampionDto } from '../types/api'
import { safeError } from '../lib/errors'
import { CARD_CLASS } from '../lib/constants'

function Champions() {
    const navigate = useNavigate()
    const { championMap } = useDragontailAssets()
    const [champions, setChampions] = useState<ChampionDto[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [searchTerm, setSearchTerm] = useState('')

    useEffect(() => {
        let cancelled = false

        async function loadChampions() {
            try {
                setLoading(true)
                setError(null)
                const data = await getAllChampions()

                if (!cancelled) {
                    setChampions(data)
                }
            } catch (requestError) {
                if (!cancelled) {
                    setError(safeError(requestError))
                }
            } finally {
                if (!cancelled) {
                    setLoading(false)
                }
            }
        }

        void loadChampions()

        return () => {
            cancelled = true
        }
    }, [])

    const filteredChampions = useMemo(() => {
        const normalizedSearch = searchTerm.trim().toLowerCase()

        if (!normalizedSearch) {
            return champions
        }

        return champions.filter((champion) =>
            champion.championName.toLowerCase().includes(normalizedSearch),
        )
    }, [champions, searchTerm])

    function openChampion(championId: number) {
        navigate(`/campeones/${championId}`)
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()

        if (filteredChampions.length > 0) {
            openChampion(filteredChampions[0].championId)
        }
    }

    return (
        <div className="space-y-7">
            <header className="border-b border-slate-800 pb-6">
                <p className="text-xs font-semibold uppercase text-cyan-300">Catálogo</p>
                <div className="mt-2 flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
                    <div>
                        <h1 className="text-3xl font-black">Campeones</h1>
                        <p className="mt-2 text-slate-400">
                            Consulta estadísticas, recomendaciones y builds profesionales.
                        </p>
                    </div>

                    <form onSubmit={handleSubmit} className="w-full max-w-md">
                        <label htmlFor="champion-search" className="sr-only">
                            Buscar campeón
                        </label>
                        <input
                            id="champion-search"
                            type="search"
                            className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 outline-none transition placeholder:text-slate-500 focus:border-cyan-300"
                            placeholder="Buscar campeón..."
                            value={searchTerm}
                            onChange={(event) => setSearchTerm(event.target.value)}
                        />
                    </form>
                </div>
            </header>

            {error && <section className={CARD_CLASS}>Error: {error}</section>}

            {loading ? (
                <section className={CARD_CLASS}>Cargando campeones...</section>
            ) : (
                <section>
                    <div className="mb-4 flex items-center justify-between">
                        <h2 className="text-lg font-bold">Todos los campeones</h2>
                        <span className="text-sm text-slate-400">
                            {filteredChampions.length} resultados
                        </span>
                    </div>

                    {filteredChampions.length === 0 ? (
                        <div className={CARD_CLASS}>No hay coincidencias.</div>
                    ) : (
                        <div className="grid grid-cols-2 gap-3 sm:grid-cols-4 lg:grid-cols-6 xl:grid-cols-8">
                            {filteredChampions.map((champion) => {
                                const icon = championMap?.get(champion.championId) ?? null

                                return (
                                    <button
                                        key={champion.championId}
                                        type="button"
                                        onClick={() => openChampion(champion.championId)}
                                        className="rounded-lg border border-slate-800 bg-slate-900 p-3 text-center transition hover:-translate-y-0.5 hover:border-cyan-300/50"
                                    >
                                        {icon ? (
                                            <img
                                                src={icon}
                                                alt={`Icono de ${champion.championName}`}
                                                className="mx-auto h-16 w-16 rounded-lg"
                                            />
                                        ) : (
                                            <div
                                                role="img"
                                                aria-label="Icono de campeón no disponible"
                                                className="mx-auto h-16 w-16 rounded-lg bg-slate-800"
                                            />
                                        )}
                                        <p className="mt-2 truncate text-sm font-medium">
                                            {champion.championName}
                                        </p>
                                    </button>
                                )
                            })}
                        </div>
                    )}
                </section>
            )}
        </div>
    )
}

export default Champions
