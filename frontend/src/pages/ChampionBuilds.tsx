import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getChampionById } from '../api/backendApi'
import ParticipationCard from '../components/ParticipationCard'
import ChampionHeader from '../components/champions/ChampionHeader'
import ChampionStatsCard from '../components/champions/ChampionStatsCard'
import RecommendedBuildCard from '../components/champions/RecommendedBuildCard'
import { useDragontailAssets } from '../lib/dragontail'
import type { ChampionDetailsDto } from '../types/api'
import { safeError } from '../lib/errors'
import { formatDate } from '../lib/format'
import { queueLabel } from '../lib/lol'
import { CARD_CLASS } from '../lib/constants'

function ChampionBuilds() {
    const navigate = useNavigate()
    const { championId = '' } = useParams()
    const { championMap, summonerSpellMap, itemInfoMap } = useDragontailAssets()

    const [champion, setChampion] = useState<ChampionDetailsDto | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadChampion() {
            try {
                setLoading(true)
                setError(null)
                const data = await getChampionById(championId, 10)

                if (!cancelled) {
                    setChampion(data)
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

        void loadChampion()

        return () => {
            cancelled = true
        }
    }, [championId])

    if (loading) {
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
        <div className="space-y-7">
            <ChampionHeader
                champion={champion}
                championIcon={championIcon}
                onBack={() => navigate('/campeones')}
            />

            <div className="grid gap-6 xl:grid-cols-[280px_minmax(0,1fr)]">
                <ChampionStatsCard stats={champion.stats} />

                <div className="min-w-0">
                    <RecommendedBuildCard
                        build={champion.recommendedBuild}
                        spellMap={summonerSpellMap}
                        itemInfoMap={itemInfoMap}
                    />

                    <section className="mt-10 border-t border-slate-700 pt-8">
                        <div className="mb-5 flex items-end justify-between gap-4">
                            <div>
                                <p className="text-xs font-semibold uppercase text-slate-500">
                                    Profesionales
                                </p>
                                <h2 className="mt-1 text-2xl font-bold">Builds recientes</h2>
                            </div>
                            <span className="text-sm text-slate-400">
                                {champion.recentProBuilds.length} builds
                            </span>
                        </div>

                        {champion.recentProBuilds.length === 0 ? (
                            <div className={CARD_CLASS}>
                                <p className="text-slate-400">
                                    No hay builds profesionales disponibles para este campeón.
                                </p>
                            </div>
                        ) : (
                            <div className="space-y-4">
                                {champion.recentProBuilds.map((build) => {
                                    const buildChampionIcon =
                                        championMap?.get(build.championId) ?? null

                                    return (
                                        <ParticipationCard
                                            key={`${build.matchId}-${build.proName}`}
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
                                            summaryLine={`${build.teamPosition || 'Sin rol'} · ${queueLabel(build.queueId)} · ${build.league}`}
                                            spellIds={[
                                                build.build.summoner1Id,
                                                build.build.summoner2Id,
                                            ]}
                                            spellMap={summonerSpellMap}
                                            mainItemIds={[
                                                build.build.item0,
                                                build.build.item1,
                                                build.build.item2,
                                                build.build.item3,
                                                build.build.item4,
                                                build.build.item5,
                                            ]}
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
        </div>
    )
}

export default ChampionBuilds
