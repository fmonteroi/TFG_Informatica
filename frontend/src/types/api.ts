// This file contains the TypeScript types that represent
// the DTOs returned by Spring Boot backend.
//
// IMPORTANT:
// In Java there are types like Instant, Integer, Long, boolean...
// In JSON, when they arrive at the frontend, they are usually converted like this:
// - Instant -> string
// - enum -> string
// - Integer / Long -> number
// - boolean -> boolean
// - String -> string


// --------------------------------------------------
// Result of refreshing professionals DTO
// ProfessionalsRefreshResultDto in backend
// --------------------------------------------------
export interface ProfessionalsRefreshResultDto {
    totalProfessionals: number
    processedProfessionals: number
    successfulProfessionals: number
    failedProfessionals: number
    stoppedByRateLimit: boolean
    stoppedAtProName: string | null
    message: string
}

// --------------------------------------------------
// Build DTO
// BuildDto in backend
// --------------------------------------------------
export interface BuildDto {
    item0: number | null
    item1: number | null
    item2: number | null
    item3: number | null
    item4: number | null
    item5: number | null
    item6: number | null
    roleBoundItem: number | null
    summoner1Id: number | null
    summoner2Id: number | null
}

// --------------------------------------------------
// Champion DTO
// ChampionDto in backend
// --------------------------------------------------
export interface ChampionDto {
    championId: number
    championName: string
}

// --------------------------------------------------
// Player DTO
// PlayerDto in backend
// --------------------------------------------------
export interface PlayerDto {
    puuid: string
    gameName: string
    tagLine: string
    platform: string
    profileIconId: number
    summonerLevel: number
    lastSyncAt: string
}

// --------------------------------------------------
// Participation DTO
// ParticipationDto in backend
// --------------------------------------------------
export interface ParticipationDto {
    id: number
    matchId: string
    queueId: number
    puuid: string | null
    gameName: string | null
    tagLine: string | null
    championId: number | null
    championName: string | null
    teamId: number
    win: boolean
    kills: number
    deaths: number
    assists: number
    gameStartAt: string
    teamPosition: string
    build: BuildDto | null
}

// --------------------------------------------------
// Player and participations DTO
// PlayerWithParticipationsDto in backend
// --------------------------------------------------
export interface PlayerWithParticipationsDto {
    player: PlayerDto
    participations: ParticipationDto[]
}

// --------------------------------------------------
// Current game DTO
// CurrentGameDto in backend
// --------------------------------------------------
export interface CurrentGameDto {
    inGame: boolean
    queueId: number | null
    queueName: string | null
    gameLengthSeconds: number | null
    championId: number | null
    championName: string | null
}

// --------------------------------------------------
// Match details DTO
// MatchDetailsDto in backend
// --------------------------------------------------
export interface MatchDetailsDto {
    matchId: string
    queueId: number
    gameStartAt: string
    gameDuration: number
    gameVersion: string
    participations: ParticipationDto[]
}

// --------------------------------------------------
// Pro build DTO
// ProBuildDto in backend
// --------------------------------------------------
export interface ProBuildDto {
    buildId: number
    matchId: string
    gameStartAt: string
    gameVersion: string
    queueId: number
    championId: number
    championName: string
    proName: string
    gameName: string
    tagLine: string
    teamName: string
    league: string
    teamPosition: string
    build: BuildDto
}