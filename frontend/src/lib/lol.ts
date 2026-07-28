import type { ParticipationDto, Role } from '../types/api'

const ROLE_LABELS: Record<Role, string> = {
    TOP: 'Top',
    JUNGLE: 'Jungla',
    MIDDLE: 'Mid',
    BOTTOM: 'Bot',
    SUPPORT: 'Support',
}

export const ROLE_ORDER: Role[] = [
    'TOP',
    'JUNGLE',
    'MIDDLE',
    'BOTTOM',
    'SUPPORT',
]

const RANK_LABELS: Record<string, string> = {
    IRON: 'Hierro',
    BRONZE: 'Bronce',
    SILVER: 'Plata',
    GOLD: 'Oro',
    PLATINUM: 'Platino',
    EMERALD: 'Esmeralda',
    DIAMOND: 'Diamante',
    MASTER: 'Master',
    GRANDMASTER: 'Grandmaster',
    CHALLENGER: 'Challenger',
}

/**
 * Converts a ranked tier into its Spanish label.
 */
export function formatRank(tier: string) {
    const label = RANK_LABELS[tier]

    if (label) {
        return label
    } else {
        return tier
    }
}

/**
 * Gets the local emblem URL for a ranked tier.
 */
export function rankEmblemUrl(tier: string) {
    return `/ranked-emblems/${tier.toLowerCase()}.png`
}

/**
 * Converts Riot queue IDs into readable queue labels.
 */
export function queueLabel(queueId: number) {
    if (queueId === 420) return 'Ranked Solo/Duo'
    if (queueId === 440) return 'Ranked Flex'
    if (queueId === 450) return 'ARAM'
    if (queueId === 400) return 'Normal Draft'
    if (queueId === 430) return 'Normal Blind'

    return 'Modo especial'
}

/**
 * Converts a role into a readable label.
 */
export function formatRole(role: Role | null) {
    if (!role) {
        return ''
    }

    return ROLE_LABELS[role]
}

/**
 * Returns the display order used for League of Legends roles.
 */
export function roleOrder(role: Role | null) {
    const positionValue = role ? ROLE_ORDER.indexOf(role) : -1

    if (positionValue === -1) {
        return 99
    } else {
        return positionValue
    }
}

/**
 * Sorts match participants by team, position and player name.
 */
export function sortMatchParticipations(participations: ParticipationDto[]) {
    return [...participations].sort((a, b) => {
        const teamComparison = a.teamId - b.teamId

        if (teamComparison !== 0) {
            return teamComparison
        }

        const positionComparison =
            roleOrder(a.teamPosition) - roleOrder(b.teamPosition)

        if (positionComparison !== 0) {
            return positionComparison
        }

        let firstName = ''
        let secondName = ''

        if (a.gameName) {
            firstName = a.gameName
        }

        if (b.gameName) {
            secondName = b.gameName
        }

        return firstName.localeCompare(secondName)
    })
}
