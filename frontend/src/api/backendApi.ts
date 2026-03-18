import type {
    ChampionDto,
    CurrentGameDto,
    MatchDetailsDto,
    MessageResponseDto,
    PlayerWithParticipationsDto,
    ProBuildDto,
} from '../types/api'

// Saves backend url
const BASE_URL = 'http://localhost:8080'

// Function to make API requests and handle errors
async function apiRequest<T>(url: string, options?: RequestInit): Promise<T> {
    const response = await fetch(url, options)

    if (!response.ok) {
        throw new Error(`Error HTTP ${response.status}: ${response.statusText}`)
    }

    // Returns the response as JSON, typed as T
    return response.json() as Promise<T>
}

// --------------------------------------------------
// PLAYERS
// -------------------------------------------------

// GET /api/players/search?platform=EUW1&gameName=Faker&tagLine=KR1
export async function searchPlayer(
    platform: string,
    gameName: string,
    tagLine: string
): Promise<PlayerWithParticipationsDto> {
    const params = new URLSearchParams({
        platform,
        gameName,
        tagLine,
    })

    return apiRequest<PlayerWithParticipationsDto>(
        `${BASE_URL}/api/players/search?${params.toString()}`
    )
}

// POST /api/players/refresh?platform=EUW1&puuid=puuid123
export async function refreshPlayer(
    platform: string,
    puuid: string
): Promise<PlayerWithParticipationsDto> {
    const params = new URLSearchParams({
        platform,
        puuid,
    })

    return apiRequest<PlayerWithParticipationsDto>(
        `${BASE_URL}/api/players/refresh?${params.toString()}`,
        {
            method: 'POST',
        }
    )
}

// GET /api/players/current-game?puuid=puuid123
export async function getCurrentGame(
    puuid: string
): Promise<CurrentGameDto> {
    const params = new URLSearchParams({
        puuid,
    })

    return apiRequest<CurrentGameDto>(
        `${BASE_URL}/api/players/current-game?${params.toString()}`
    )
}

// POST /api/professionals/refresh
export async function refreshProfessionals(): Promise<MessageResponseDto> {
    return apiRequest<MessageResponseDto>(
        `${BASE_URL}/api/professionals/refresh`,
        {
            method: 'POST',
        }
    )
}

// GET /api/matches/{matchId}
export async function getMatchById(
    matchId: string
): Promise<MatchDetailsDto> {
    return apiRequest<MatchDetailsDto>(
        `${BASE_URL}/api/matches/${matchId}`
    )
}

// GET /api/champions/{championId}
export async function getChampionById(
    championId: string
): Promise<ChampionDto> {
    return apiRequest<ChampionDto>(
        `${BASE_URL}/api/champions/${championId}`
    )
}

// GET /api/champions
export async function getAllChampions(): Promise<ChampionDto[]> {
    return apiRequest<ChampionDto[]>(
        `${BASE_URL}/api/champions`
    )
}

// GET /api/champions/{championId}/builds?count=10
export async function getChampionBuilds(
    championId: string,
    count: number
): Promise<ProBuildDto[]> {
    const params = new URLSearchParams({
        count: String(count),
    })

    return apiRequest<ProBuildDto[]>(
        `${BASE_URL}/api/champions/${championId}/builds?${params.toString()}`
    )
}