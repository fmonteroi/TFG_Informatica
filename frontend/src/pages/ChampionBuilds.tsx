import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getChampionBuilds, getChampionById } from '../api/backendApi'
import ParticipationCard from '../components/ParticipationCard'
import { useDragontailAssets } from '../lib/dragontail'
import type { ChampionDto, ProBuildDto } from '../types/api'
import { safeError } from '../lib/errors'
import { formatDate } from '../lib/format'
import { queueLabel } from '../lib/lol'
import { CARD_CLASS } from '../lib/constants'
import ChampionHeader from '../components/champions/ChampionHeader.tsx'

function ChampionBuilds() {
    const navigate = useNavigate()
    const { championId = '' } = useParams()
    const { championMap, summonerSpellMap, itemInfoMap } = useDragontailAssets()

    const [champion, setChampion] = useState<ChampionDto | null>(null)
    const [builds, setBuilds] = useState<ProBuildDto[]>([])
    const [loadingChampion, setLoadingChampion] = useState(true)
    const [loadingBuilds, setLoadingBuilds] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadChampionHeader() {
            try {
                setLoadingChampion(true)
                setError(null)

                const data = await getChampionById(championId)

                if (!cancelled) {
                    setChampion(data)
                }
            } catch (error) {
                if (!cancelled) {
                    setError(safeError(error))
                }
            } finally {
                if (!cancelled) {
                    setLoadingChampion(false)
                }
            }
        }

        void loadChampionHeader()

        return () => {
            cancelled = true
        }
    }, [championId])

    useEffect(() => {
        let cancelled = false

        async function loadBuilds() {
            try {
                setLoadingBuilds(true)
                setError(null)

                const data = await getChampionBuilds(championId, 10)

                if (!cancelled) {
                    setBuilds(data)
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

    if (loadingChampion) {
        return <section className={CARD_CLASS}>Cargando campeón...</section>
    }

    if (error) {
        return <section className={CARD_CLASS}>Error: {error}</section>
    }

    if (!champion) {
        return <section className={CARD_CLASS}>No se encontró el campeón.</section>
    }

    const championIcon = championMap?.get(champion.championId) ?? null

    return (
        <div className="space-y-6">
            <ChampionHeader
                champion={champion}
                championIcon={championIcon}
                onBack={() => navigate('/campeones')}
            />

            <div className="grid gap-6 xl:grid-cols-[240px_1fr]">
                <aside className={`${CARD_CLASS} h-fit space-y-4`}>
                    <div className="flex items-center justify-between">
                        <h2 className="w-full text-center text-xl font-bold">Estadísticas</h2>
                    </div>

                    <div className="rounded-2xl border border-dashed border-slate-700 bg-slate-950/60 p-4">
                        <p className="text-sm text-slate-400">
                            Futuras versiones
                        </p>
                    </div>
                </aside>

                <section className={`${CARD_CLASS} space-y-4`}>
                    <div className="flex items-center justify-between">
                        <h2 className="text-2xl font-bold">Builds recientes</h2>
                        <span className="text-sm text-slate-400">{builds.length} builds</span>
                    </div>

                    {loadingBuilds ? (
                        <p className="text-slate-400">Cargando builds...</p>
                    ) : builds.length === 0 ? (
                        <p className="text-slate-400">
                            No hay builds disponibles para este campeón.
                        </p>
                    ) : (
                        <div className="space-y-4">
                            {builds.map((build) => {
                                const buildChampionIcon =
                                    championMap?.get(build.championId) ?? null

                                const spellIds = [
                                    build.build.summoner1Id,
                                    build.build.summoner2Id,
                                ]

                                const mainItemIds = [
                                    build.build.item0,
                                    build.build.item1,
                                    build.build.item2,
                                    build.build.item3,
                                    build.build.item4,
                                    build.build.item5,
                                ]

                                return (
                                    <ParticipationCard
                                        key={build.buildId}
                                        tone="neutral"
                                        topLeft={
                                            <div>
                                                <p className="font-bold text-slate-100">
                                                    {build.proName}
                                                </p>
                                                <p className="text-sm text-slate-400">
                                                    {build.teamName}
                                                </p>
                                            </div>
                                        }
                                        topRight={
                                            <span className="text-sm text-slate-300">
                                    {formatDate(build.gameStartAt)}
                                </span>
                                        }
                                        championIcon={buildChampionIcon}
                                        championName={build.championName}
                                        summaryLine={`${build.teamPosition} · ${queueLabel(build.queueId)} · ${build.league}`}
                                        spellIds={spellIds}
                                        spellMap={summonerSpellMap}
                                        mainItemIds={mainItemIds}
                                        trinketItemId={build.build.item6}
                                        specialItemId={build.build.roleBoundItem}
                                        showSpecialItem={build.teamPosition === 'BOTTOM'}
                                        itemInfoMap={itemInfoMap}
                                    />
                                )
                            })}
                        </div>
                    )}
                </section>
            </div>

        </div>
    )
}

export default ChampionBuilds
