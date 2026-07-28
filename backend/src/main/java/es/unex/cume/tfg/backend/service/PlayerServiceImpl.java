package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.exception.PlayerNotFoundException;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.repository.PlayerRepository;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Default implementation of PlayerService.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final RiotFetchService riotFetchService;
    private final MatchService matchService;
    private final RankedRankService rankedRankService;
    private final PlayerStatsService playerStatsService;

    // Lock map to prevent concurrent refreshes for the same player
    private final ConcurrentHashMap<String, ReentrantLock> refreshLocks = new ConcurrentHashMap<>();


    /**
     * Creates the player service.
     *
     * @param playerRepository player repository
     * @param riotFetchService Riot data service
     * @param matchService match service
     * @param rankedRankService ranked result service
     * @param playerStatsService player statistics service
     */
    public PlayerServiceImpl(PlayerRepository playerRepository, RiotFetchService riotFetchService, MatchService matchService, RankedRankService rankedRankService,
                             PlayerStatsService playerStatsService) {
        this.playerRepository = playerRepository;
        this.riotFetchService = riotFetchService;
        this.matchService = matchService;
        this.rankedRankService = rankedRankService;
        this.playerStatsService = playerStatsService;
    }

    /**
     * Finds a player by their PUUID.
     *
     * @param puuid player PUUID
     * @return the player if it exists.
     */
    @Override
    public Optional<Player> findByPuuid(String puuid) {
        return playerRepository.findByPuuid(puuid);
    }

    /**
     * Finds if a player already exists in the database by their PUUID.
     *
     * @param puuid player PUUID
     * @return true if the player exists.
     */
    @Override
    public boolean existsByPuuid(String puuid) {
        return playerRepository.existsByPuuid(puuid);
    }

    /**
     * Looks for a player by their Riot game name and tag line. If it exists, returns it.
     * If not, creates a new one.
     *
     * @param platform Riot platform
     * @param gameName Riot game name
     * @param tagLine Riot tag line
     * @return the existing or newly created player.
     */
    @Transactional
    @Override
    public Player searchPlayer(Platform platform, String gameName, String tagLine) {
        Optional<Player> localPlayer = playerRepository.findByPlatformAndGameNameIgnoreCaseAndTagLineIgnoreCase(platform, gameName, tagLine);

        String puuid;

        if (localPlayer.isPresent()) {
            puuid = localPlayer.get().getPuuid();
        } else {
            puuid = riotFetchService.fetchPuuid(platform, gameName, tagLine);
        }

        // Prevents two requests from creating or completing the same player
        ReentrantLock lock = getPlayerLock(puuid);
        lock.lock();

        try {

            // If already exists, return the existing player
            Optional<Player> existingPlayer = playerRepository.findByPuuid(puuid);
            if (existingPlayer.isPresent()) {
                Player player = existingPlayer.get();

                // If this player was created as "basic" (from match participants), completes initialization
                if (player.getLastSyncAt() == null || player.getStats() == null) {
                    return syncPlayerData(player, platform);
                }

                return player;
            }

            // If not, creates it
            return savePlayer(puuid, platform, gameName, tagLine);
        } finally {
            lock.unlock();

            if (!lock.isLocked() && !lock.hasQueuedThreads()) {
                refreshLocks.remove(puuid, lock);
            }
        }
    }

    /**
     * Refreshes a player's data and their recent matches from Riot API.
     *
     * @param platform the platform/region
     * @param puuid    the player's PUUID
     * @return the updated player
     */
    @Override
    @Transactional
    public Player refreshPlayer(Platform platform, String puuid) {
        ReentrantLock lock = getPlayerLock(puuid);
        lock.lock();

        try {
            // Finds the player by PUUID
            Optional<Player> optionalPlayer = playerRepository.findByPuuid(puuid);

            // If player doesn't exist, throw an exception
            if (optionalPlayer.isEmpty()) {
                throw new PlayerNotFoundException(puuid);
            }

            // If player exists, gets it
            Player player = optionalPlayer.get();

            return syncPlayerData(player, platform);

        } finally {
            lock.unlock();

            if (!lock.isLocked() && !lock.hasQueuedThreads()) {
                refreshLocks.remove(puuid, lock);
            }
        }
    }

    /**
     * Synchronizes a player's data for a professional player.
     *
     * @param platform Riot platform
     * @param gameName Riot game name
     * @param tagLine Riot tag line
     * @param puuid player PUUID
     * @return the synchronized player.
     */
    @Override
    public Player syncPlayerForProfessional(Platform platform, String gameName, String tagLine, String puuid) {
        // Gets the latest icon and summoner level
        SummonerDto summonerDto = riotFetchService.fetchSummoner(platform, puuid);

        // Checks if player already exists
        Optional<Player> optionalPlayer = playerRepository.findByPuuid(puuid);

        // If player exists, updates it with the latest data
        if (optionalPlayer.isPresent()) {
            Player existingPlayer = optionalPlayer.get();
            existingPlayer.setGameName(gameName);
            existingPlayer.setTagLine(tagLine);
            existingPlayer.setPlatform(platform);
            existingPlayer.setProfileIconId(summonerDto.profileIconId());
            existingPlayer.setSummonerLevel(summonerDto.summonerLevel());

            return playerRepository.save(existingPlayer);
        }

        // Otherwise, creates a new player with the latest data
        Player newPlayer = new Player();
        newPlayer.setPuuid(puuid);
        newPlayer.setGameName(gameName);
        newPlayer.setTagLine(tagLine);
        newPlayer.setPlatform(platform);
        newPlayer.setProfileIconId(summonerDto.profileIconId());
        newPlayer.setSummonerLevel(summonerDto.summonerLevel());

        // Note: It doesn't update lastSyncAt because it doesn't load matches here
        return playerRepository.save(newPlayer);
    }

    /**
     * Creates a new player if it doesn't exist.
     *
     * @param puuid player PUUID
     * @param platform Riot platform
     * @param gameName Riot game name
     * @param tagLine Riot tag line
     * @return the created player.
     */
    private Player savePlayer(String puuid, Platform platform, String gameName, String tagLine) {
        // Creates new player with basic data
        Player newPlayer = new Player();

        newPlayer.setPuuid(puuid);
        newPlayer.setPlatform(platform);
        newPlayer.setGameName(gameName);
        newPlayer.setTagLine(tagLine);

        // Saves player so there's an ID to link matches to
        Player savedPlayer = playerRepository.save(newPlayer);

        return syncPlayerData(savedPlayer, platform);
    }

    /**
     * Updates profile, matches, ranks and statistics as one synchronization.
     *
     * @param player player to update
     * @param platform Riot platform
     * @return synchronized player
     */
    private Player syncPlayerData(Player player, Platform platform){
        Instant originalSyncAt = player.getLastSyncAt();
        Instant newSyncAt = Instant.now();

        // Gets the latest icon and summoner level
        SummonerDto summonerDto = riotFetchService.fetchSummoner(platform, player.getPuuid());
        player.setProfileIconId(summonerDto.profileIconId());
        player.setSummonerLevel(summonerDto.summonerLevel());

        // Loads new matches
        Instant matchesSince;

        if (originalSyncAt == null){
            matchesSince = newSyncAt.minus(365, ChronoUnit.DAYS);
        } else {
            matchesSince = originalSyncAt;
        }

        matchService.loadMatchesSince(platform, player.getPuuid(), 20, matchesSince);

        // Updates ranked information
        rankedRankService.refreshRanks(platform, player);

        // Calculates statistics using the updated participations
        playerStatsService.calculatePlayerStats(player);

        player.setLastSyncAt(newSyncAt);
        return playerRepository.save(player);
    }

    /**
     * Gets the lock for a player to prevent concurrent refreshes. If the lock doesn't exist, creates it.
     *
     * @param puuid player PUUID
     * @return the lock for the player.
     */
    private ReentrantLock getPlayerLock(String puuid) {
        return refreshLocks.computeIfAbsent(puuid, key -> new ReentrantLock());
    }

}
