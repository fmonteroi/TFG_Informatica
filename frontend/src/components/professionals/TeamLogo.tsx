import { useEffect, useState } from 'react'
import { teamLogoUrl } from '../../lib/professionals'

type TeamLogoProps = {
    teamName: string
    className?: string
}

const PLACEHOLDER_URL = '/team-logos/placeholder.svg'

function TeamLogo({ teamName, className = 'h-14 w-14' }: TeamLogoProps) {
    const [source, setSource] = useState(teamLogoUrl(teamName))

    useEffect(() => {
        setSource(teamLogoUrl(teamName))
    }, [teamName])

    return (
        <img
            src={source}
            alt={`Logo de ${teamName}`}
            className={`${className} shrink-0 rounded-lg border border-white/10 bg-slate-950 object-contain p-1`}
            onError={() => setSource(PLACEHOLDER_URL)}
        />
    )
}

export default TeamLogo
