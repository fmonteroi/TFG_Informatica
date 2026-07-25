import { useEffect, useMemo, useState } from 'react'
import { getAllProfessionals } from '../api/backendApi'
import TeamCard from '../components/professionals/TeamCard'
import type { ProfessionalDto } from '../types/api'
import { safeError } from '../lib/errors'
import { CARD_CLASS } from '../lib/constants'
import { professionalRoleOrder } from '../lib/professionals'

function Professionals() {
    const [professionals, setProfessionals] = useState<ProfessionalDto[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadProfessionals() {
            try {
                setLoading(true)
                setError(null)
                const data = await getAllProfessionals()

                if (!cancelled) {
                    setProfessionals(data)
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

        void loadProfessionals()

        return () => {
            cancelled = true
        }
    }, [])

    const teams = useMemo(() => {
        const grouped = new Map<string, ProfessionalDto[]>()

        professionals.forEach((professional) => {
            const teamMembers = grouped.get(professional.teamName) ?? []
            teamMembers.push(professional)
            grouped.set(professional.teamName, teamMembers)
        })

        grouped.forEach((members) => {
            members.sort(
                (first, second) =>
                    professionalRoleOrder(first.proName) -
                    professionalRoleOrder(second.proName),
            )
        })

        return Array.from(grouped.entries()).sort(([firstTeam], [secondTeam]) =>
            firstTeam.localeCompare(secondTeam),
        )
    }, [professionals])

    return (
        <div className="space-y-7">
            <header className="border-b border-slate-800 pb-6">
                <p className="text-xs font-semibold uppercase text-cyan-300">Escena competitiva</p>
                <h1 className="mt-2 text-3xl font-black">Profesionales</h1>
                <p className="mt-2 max-w-2xl text-slate-400">
                    Explora los equipos y consulta la actividad ranked reciente de cada jugador.
                </p>
            </header>

            {loading && <section className={CARD_CLASS}>Cargando profesionales...</section>}
            {error && <section className={CARD_CLASS}>Error: {error}</section>}

            {!loading && !error && teams.length === 0 && (
                <section className={CARD_CLASS}>No hay profesionales disponibles.</section>
            )}

            {!loading && !error && teams.length > 0 && (
                <section
                    aria-label="Equipos profesionales"
                    className="grid gap-5 sm:grid-cols-2 xl:grid-cols-3"
                >
                    {teams.map(([teamName, members]) => (
                        <TeamCard
                            key={teamName}
                            teamName={teamName}
                            professionals={members}
                        />
                    ))}
                </section>
            )}
        </div>
    )
}

export default Professionals
