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

/**
 * Sends a request to the backend and preserves structured API errors.
 */
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

/**
 * Searches a player by Riot ID.
 */
export async function searchPlayer(platform: string, gameName: string, tagLine: string) {
    const params = new URLSearchParams({ platform, gameName, tagLine })
    return apiRequest<PlayerWithParticipationsDto>(`/api/players/search?${params.toString()}`)
}

/**
 * Refreshes an existing player's profile and match history.
 */
export async function refreshPlayer(platform: string, puuid: string) {
    const params = new URLSearchParams({ platform, puuid })
    return apiRequest<PlayerWithParticipationsDto>(`/api/players/refresh?${params.toString()}`, { method: 'POST' })
}

/**
 * Fetches the current game status for a player.
 */
export async function getCurrentGame(puuid: string) {
    const params = new URLSearchParams({ puuid })
    return apiRequest<CurrentGameDto>(`/api/players/current-game?${params.toString()}`)
}

/**
 * Refreshes professional players and their recent matches.
 */
export async function refreshProfessionals() {
    return apiRequest<ProfessionalsRefreshResultDto>('/api/professionals/refresh', { method: 'POST' })
}

/**
 * Fetches match details by match ID.
 */
export async function getMatchById(matchId: string) {
    return apiRequest<MatchDetailsDto>(`/api/matches/${matchId}`)
}

/**
 * Fetches a champion by ID.
 */
export async function getChampionById(championId: string) {
    return apiRequest<ChampionDto>(`/api/champions/${championId}`)
}

/**
 * Fetches all champions from the backend catalog.
 */
export async function getAllChampions() {
    return apiRequest<ChampionDto[]>('/api/champions')
}

/**
 * Fetches recent professional builds for a champion.
 */
export async function getChampionBuilds(championId: string, count: number) {
    const params = new URLSearchParams({ count: String(count) })
    return apiRequest<ProBuildDto[]>(`/api/champions/${championId}/builds?${params.toString()}`)
}
