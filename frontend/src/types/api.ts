// TypeScript representations of the DTOs returned by the Spring Boot API.

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

export interface ChampionDto {
    championId: number
    championName: string
}

export interface ChampionStatsDto {
    championId: number
    gamesPlayed: number
    wins: number
    losses: number
    winRate: number
    averageKills: number
    averageDeaths: number
    averageAssists: number
    kda: number
}

export interface RecommendedBuildDto extends BuildDto {
    championId: number
}

export interface PlayerDto {
    puuid: string
    gameName: string
    tagLine: string
    platform: string
    profileIconId: number
    summonerLevel: number
    lastSyncAt: string | null
}

export interface PlayerStatsDto {
    gamesPlayed: number
    wins: number
    losses: number
    winRate: number
    averageKills: number
    averageDeaths: number
    averageAssists: number
    kda: number
    bestChampion: ChampionDto | null
}

export interface RankedRankDto {
    queueType: string
    tier: string
    rank: string
    leaguePoints: number
    wins: number
    losses: number
}

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

export interface PlayerDetailsDto {
    player: PlayerDto
    stats: PlayerStatsDto | null
    rankedRanks: RankedRankDto[]
    participations: ParticipationDto[]
}

export interface CurrentGameDto {
    inGame: boolean
    hidden: boolean
    queueId: number | null
    queueName: string | null
    gameLengthSeconds: number | null
    championId: number | null
    championName: string | null
}

export interface MatchDetailsDto {
    matchId: string
    queueId: number
    gameStartAt: string
    gameDuration: number
    gameVersion: string
    participations: ParticipationDto[]
}

export interface ChampionProBuildDto {
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

export interface ChampionDetailsDto {
    championId: number
    championName: string
    stats: ChampionStatsDto | null
    recommendedBuild: RecommendedBuildDto | null
    recentProBuilds: ChampionProBuildDto[]
}

export interface ProfessionalDto {
    puuid: string
    proName: string
    teamName: string
    league: string
}

export interface ProBuildDto {
    matchId: string
    gameStartAt: string
    gameVersion: string
    queueId: number
    championId: number
    championName: string
    teamPosition: string
    build: BuildDto
}

export interface ProfessionalDetailsDto {
    puuid: string
    proName: string
    teamName: string
    league: string
    gameName: string
    tagLine: string
    platform: string
    profileIconId: number
    recentBuilds: ProBuildDto[]
}
