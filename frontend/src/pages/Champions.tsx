import { useEffect, useMemo, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAllChampions } from '../api/backendApi'
import { useDragontailAssets } from '../lib/dragontail'
import type { ChampionDto, Tier } from '../types/api'
import { safeError } from '../lib/errors'
import { CARD_CLASS } from '../lib/constants'
import ChampionTierSection from '../components/champions/ChampionTierSection'

const TIER_ORDER: Tier[] = ['S', 'A', 'B', 'C', 'D', 'E']

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

    const championsByTier = useMemo(() => {
        const grouped = new Map<Tier, ChampionDto[]>()

        for (const tier of TIER_ORDER) {
            grouped.set(tier, [])
        }

        for (const champion of filteredChampions) {
            let tier = champion.tier

            // Uses C while the backend has not calculated a tier yet
            if (tier == null) {
                tier = 'C'
            }

            const tierChampions = grouped.get(tier)

            if (tierChampions) {
                tierChampions.push(champion)
            }
        }

        for (const tierChampions of grouped.values()) {
            tierChampions.sort((first, second) =>
                first.championName.localeCompare(second.championName),
            )
        }

        return grouped
    }, [filteredChampions])

    function openChampion(championId: number) {
        navigate(`/campeones/${championId}`)
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()

        if (filteredChampions.length > 0) {
            openChampion(filteredChampions[0].championId)
        }
    }

    const catalogContent = (
        <div className="space-y-5">
            {TIER_ORDER.map((tier) => (
                <ChampionTierSection
                    key={tier}
                    tier={tier}
                    champions={championsByTier.get(tier) || []}
                    championMap={championMap}
                />
            ))}
        </div>
    )

    return (
        <div className="space-y-7">
            <header className="border-b border-slate-800 pb-6">
                <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
                    <h1 className="text-3xl font-black">Campeones</h1>

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

            {loading && <section className={CARD_CLASS}>Cargando campeones...</section>}

            {!loading && !error && (
                <>
                    {catalogContent}
                </>
            )}
        </div>
    )
}

export default Champions
