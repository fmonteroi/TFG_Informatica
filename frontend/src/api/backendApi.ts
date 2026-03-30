import type {
    ChampionDto,
    CurrentGameDto,
    MatchDetailsDto,
    ProfessionalsRefreshResultDto,
    PlayerWithParticipationsDto,
    ProBuildDto,
} from '../types/api'

type BackendErrorBody = {
    timestamp?: string
    status?: number
    error?: string
    message?: string
}

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

async function apiRequest<T>(path: string, options?: RequestInit): Promise<T> {
    const response = await fetch(`${BASE_URL}${path}`, options)

    if (!response.ok) {
        let message = `Error HTTP ${response.status}`

        try {
            const body = (await response.json()) as BackendErrorBody
            message = body.message ?? body.error ?? message
        } catch {
            // si no viene JSON, dejamos el mensaje genérico
        }

        throw new Error(message)
    }

    return response.json() as Promise<T>
}

export async function searchPlayer(platform: string, gameName: string, tagLine: string) {
    const params = new URLSearchParams({ platform, gameName, tagLine })
    return apiRequest<PlayerWithParticipationsDto>(`/api/players/search?${params.toString()}`)
}

export async function refreshPlayer(platform: string, puuid: string) {
    const params = new URLSearchParams({ platform, puuid })
    return apiRequest<PlayerWithParticipationsDto>(`/api/players/refresh?${params.toString()}`, {method: 'POST',})
}

export async function getCurrentGame(puuid: string) {
    const params = new URLSearchParams({ puuid })
    return apiRequest<CurrentGameDto>(`/api/players/current-game?${params.toString()}`)
}

export async function refreshProfessionals() {
    return apiRequest<ProfessionalsRefreshResultDto>('/api/professionals/refresh', {method: 'POST',})
}

export async function getMatchById(matchId: string) {
    return apiRequest<MatchDetailsDto>(`/api/matches/${matchId}`)
}

export async function getChampionById(championId: string) {
    return apiRequest<ChampionDto>(`/api/champions/${championId}`)
}

export async function getAllChampions() {
    return apiRequest<ChampionDto[]>('/api/champions')
}

export async function getChampionBuilds(championId: string, count: number) {
    const params = new URLSearchParams({ count: String(count) })
    return apiRequest<ProBuildDto[]>(`/api/champions/${championId}/builds?${params.toString()}`)
}
