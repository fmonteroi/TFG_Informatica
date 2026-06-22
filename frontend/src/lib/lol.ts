import type { ParticipationDto } from '../types/api'

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
 * Returns the display order used for League of Legends positions.
 */
export function positionOrder(position: string) {
    const order: Record<string, number> = {
        TOP: 1,
        JUNGLE: 2,
        MIDDLE: 3,
        BOTTOM: 4,
        UTILITY: 5,
    }

    return order[position] ?? 99
}

/**
 * Sorts match participants by team, position and player name.
 */
export function sortMatchParticipations(participations: ParticipationDto[]) {
    return [...participations].sort((a, b) => {
        return (
            a.teamId - b.teamId ||
            positionOrder(a.teamPosition) - positionOrder(b.teamPosition) ||
            (a.gameName ?? '').localeCompare(b.gameName ?? '')
        )
    })
}
