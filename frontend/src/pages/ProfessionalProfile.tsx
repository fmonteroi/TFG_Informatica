import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getProfessionalByPuuid } from '../api/backendApi'
import ParticipationCard from '../components/ParticipationCard'
import TeamLogo from '../components/professionals/TeamLogo'
import { getProfileIconUrl, useDragontailAssets } from '../lib/dragontail'
import { professionalRole } from '../lib/professionals'
import type { ProfessionalDetailsDto } from '../types/api'
import { safeError } from '../lib/errors'
import { formatDate } from '../lib/format'
import { queueLabel } from '../lib/lol'
import { CARD_CLASS } from '../lib/constants'

function ProfessionalProfile() {
    const { puuid = '' } = useParams()
    const { dataDragonVersion, championMap, summonerSpellMap, itemInfoMap } =
        useDragontailAssets()
    const [professional, setProfessional] = useState<ProfessionalDetailsDto | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadProfessional() {
            try {
                setLoading(true)
                setError(null)
                const data = await getProfessionalByPuuid(puuid, 20)

                if (!cancelled) {
                    setProfessional(data)
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

        void loadProfessional()

        return () => {
            cancelled = true
        }
    }, [puuid])

    if (loading) {
        return <section className={CARD_CLASS}>Cargando profesional...</section>
    }

    if (error) {
        return <section className={CARD_CLASS}>Error: {error}</section>
    }

    if (!professional) {
        return <section className={CARD_CLASS}>No se encontró el profesional.</section>
    }

    const normalProfileUrl = `/jugador/${encodeURIComponent(professional.platform)}/${encodeURIComponent(professional.gameName)}/${encodeURIComponent(professional.tagLine)}`

    return (
        <div className="space-y-8">
            <header className="border-b border-slate-800 pb-6">
                <div className="flex flex-col gap-5 md:flex-row md:items-center md:justify-between">
                    <div className="flex items-center gap-4">
                        {dataDragonVersion ? (
                            <img
                                src={getProfileIconUrl(
                                    professional.profileIconId,
                                    dataDragonVersion,
                                )}
                                alt={`Icono de perfil de ${professional.proName}`}
                                className="h-20 w-20 rounded-lg border border-slate-700"
                            />
                        ) : (
                            <div
                                role="img"
                                aria-label="Icono de perfil cargando"
                                className="h-20 w-20 rounded-lg border border-slate-700 bg-slate-800"
                            />
                        )}
                        <div>
                            <p className="text-xs font-semibold uppercase text-cyan-300">
                                Perfil profesional
                            </p>
                            <h1 className="mt-1 text-3xl font-black">{professional.proName}</h1>
                            <p className="mt-1 text-slate-400">
                                {professional.gameName}#{professional.tagLine}
                            </p>
                        </div>
                    </div>

                    <Link
                        to={normalProfileUrl}
                        className="rounded-lg border border-cyan-300/40 bg-cyan-300/10 px-4 py-3 text-center font-semibold text-cyan-100 transition hover:bg-cyan-300/20"
                    >
                        Ver perfil normal
                    </Link>
                </div>

                <dl className="mt-6 grid gap-3 sm:grid-cols-3">
                    <div className="flex items-center gap-3 rounded-lg border border-slate-800 bg-slate-900 p-3">
                        <TeamLogo teamName={professional.teamName} className="h-12 w-12" />
                        <div>
                            <dt className="text-xs uppercase text-slate-500">Equipo</dt>
                            <dd className="font-semibold">{professional.teamName}</dd>
                        </div>
                    </div>
                    <div className="rounded-lg border border-slate-800 bg-slate-900 p-3">
                        <dt className="text-xs uppercase text-slate-500">Rol</dt>
                        <dd className="mt-1 font-semibold">
                            {professionalRole(professional.proName)}
                        </dd>
                    </div>
                    <div className="rounded-lg border border-slate-800 bg-slate-900 p-3">
                        <dt className="text-xs uppercase text-slate-500">Liga</dt>
                        <dd className="mt-1 font-semibold">{professional.league}</dd>
                    </div>
                </dl>
            </header>

            <section>
                <div className="mb-5 flex items-end justify-between gap-4">
                    <div>
                        <p className="text-xs font-semibold uppercase text-slate-500">
                            Actividad reciente
                        </p>
                        <h2 className="mt-1 text-2xl font-bold">Últimas probuilds</h2>
                    </div>
                    <span className="text-sm text-slate-400">
                        {professional.recentBuilds.length} de 20
                    </span>
                </div>

                {professional.recentBuilds.length === 0 ? (
                    <div className={CARD_CLASS}>
                        <p className="text-slate-400">No hay builds recientes disponibles.</p>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {professional.recentBuilds.map((build) => (
                            <ParticipationCard
                                key={`${build.matchId}-${build.championId}`}
                                tone="neutral"
                                topLeft={
                                    <span className="rounded-md bg-slate-800 px-3 py-2 text-sm font-semibold">
                                        {queueLabel(build.queueId)}
                                    </span>
                                }
                                topRight={
                                    <span className="text-sm text-slate-300">
                                        {formatDate(build.gameStartAt)}
                                    </span>
                                }
                                championIcon={championMap?.get(build.championId) ?? null}
                                championName={build.championName}
                                summaryLine={`${build.teamPosition || 'Sin rol'} · Parche ${build.gameVersion}`}
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
                        ))}
                    </div>
                )}
            </section>
        </div>
    )
}

export default ProfessionalProfile
