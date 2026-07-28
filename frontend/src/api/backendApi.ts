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

function getBaseUrl() {
    const configuredUrl = import.meta.env.VITE_API_URL

    if (configuredUrl) {
        return configuredUrl
    } else {
        return 'http://localhost:8080'
    }
}

function getErrorMessage(body: BackendErrorBody, fallbackStatus: number) {
    if (body.message) {
        return body.message
    } else if (body.error) {
        return body.error
    } else {
        return `Error HTTP ${fallbackStatus}`
    }
}

const BASE_URL = getBaseUrl()

/**
 * Error thrown when the backend returns a non-successful response.
 */
export class ApiError extends Error {
    status: number
    code: string
    error: string | null
    timestamp: string | null

    constructor(body: BackendErrorBody, fallbackStatus: number) {
        super(getErrorMessage(body, fallbackStatus))
        this.name = 'ApiError'

        this.status = fallbackStatus
        if (body.status !== undefined) {
            this.status = body.status
        }

        this.code = 'HTTP_ERROR'
        if (body.code) {
            this.code = body.code
        }

        this.error = null
        if (body.error) {
            this.error = body.error
        }

        this.timestamp = null
        if (body.timestamp) {
            this.timestamp = body.timestamp
        }
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
