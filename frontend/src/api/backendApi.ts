import type {
    ChampionDetailsDto,
    ChampionDto,
    CurrentGameDto,
    MatchDetailsDto,
    PlayerDetailsDto,
    ProfessionalDetailsDto,
    ProfessionalDto,
} from '../types/api'

type BackendErrorBody = {
    timestamp?: string
    status?: number
    code?: string
    error?: string
    message?: string
}

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

/**
 * Error thrown when the backend returns a non-successful response.
 */
export class ApiError extends Error {
    status: number
    code: string
    error: string | null
    timestamp: string | null

    constructor(body: BackendErrorBody, fallbackStatus: number) {
        super(body.message ?? body.error ?? `Error HTTP ${fallbackStatus}`)
        this.name = 'ApiError'
        this.status = body.status ?? fallbackStatus
        this.code = body.code ?? 'HTTP_ERROR'
        this.error = body.error ?? null
        this.timestamp = body.timestamp ?? null
    }
}

async function apiRequest<T>(path: string, options?: RequestInit): Promise<T> {
    const response = await fetch(`${BASE_URL}${path}`, options)

    if (!response.ok) {
        let errorBody: BackendErrorBody

        try {
            errorBody = (await response.json()) as BackendErrorBody
        } catch {
            errorBody = {
                status: response.status,
                code: 'HTTP_ERROR',
                error: response.statusText,
                message: `Error HTTP ${response.status}`,
            }
        }

        throw new ApiError(errorBody, response.status)
    }

    return response.json() as Promise<T>
}

export async function searchPlayer(platform: string, gameName: string, tagLine: string) {
    const params = new URLSearchParams({ platform, gameName, tagLine })
    return apiRequest<PlayerDetailsDto>(`/api/players/search?${params.toString()}`)
}

export async function refreshPlayer(platform: string, puuid: string) {
    const params = new URLSearchParams({ platform, puuid })
    return apiRequest<PlayerDetailsDto>(`/api/players/refresh?${params.toString()}`, {
        method: 'POST',
    })
}

export async function getCurrentGame(puuid: string) {
    const params = new URLSearchParams({ puuid })
    return apiRequest<CurrentGameDto>(`/api/players/current-game?${params.toString()}`)
}

export async function getMatchById(matchId: string) {
    return apiRequest<MatchDetailsDto>(`/api/matches/${encodeURIComponent(matchId)}`)
}

export async function getAllChampions() {
    return apiRequest<ChampionDto[]>('/api/champions')
}

export async function getChampionById(championId: string, buildCount = 10) {
    const params = new URLSearchParams({ buildCount: String(buildCount) })
    return apiRequest<ChampionDetailsDto>(
        `/api/champions/${encodeURIComponent(championId)}?${params.toString()}`,
    )
}

export async function getAllProfessionals() {
    return apiRequest<ProfessionalDto[]>('/api/professionals')
}

export async function getProfessionalByPuuid(puuid: string, buildCount = 20) {
    const params = new URLSearchParams({ buildCount: String(buildCount) })
    return apiRequest<ProfessionalDetailsDto>(
        `/api/professionals/${encodeURIComponent(puuid)}?${params.toString()}`,
    )
}
