import { Link } from 'react-router-dom'
import type { ProfessionalDto } from '../../types/api'
import { professionalRole } from '../../lib/professionals'
import TeamLogo from './TeamLogo'

type TeamCardProps = {
    teamName: string
    professionals: ProfessionalDto[]
}

function TeamCard({ teamName, professionals }: TeamCardProps) {
    return (
        <article className="flex aspect-square min-h-0 flex-col overflow-hidden rounded-lg border border-slate-800 bg-slate-900">
            <header className="flex items-center gap-3 border-b border-cyan-300/20 bg-cyan-400/10 p-3">
                <TeamLogo teamName={teamName} className="h-12 w-12" />
                <div className="min-w-0">
                    <h2 className="truncate text-xl font-bold">{teamName}</h2>
                    <p className="text-xs uppercase text-cyan-200/70">
                        {professionals[0]?.league ?? 'Competición'}
                    </p>
                </div>
            </header>

            <ul className="flex min-h-0 flex-1 flex-col justify-center p-2">
                {professionals.map((professional) => (
                    <li key={professional.puuid}>
                        <Link
                            to={`/profesionales/${encodeURIComponent(professional.puuid)}`}
                            className="flex items-center justify-between gap-3 rounded-md px-3 py-1.5 transition hover:bg-slate-800 focus-visible:outline-2 focus-visible:outline-cyan-300"
                        >
                            <span className="truncate font-semibold">{professional.proName}</span>
                            <span className="text-xs text-slate-400">
                                {professionalRole(professional.proName)}
                            </span>
                        </Link>
                    </li>
                ))}
            </ul>
        </article>
    )
}

export default TeamCard
