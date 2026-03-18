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

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final RiotFetchService riotFetchService;
    private final MatchService matchService;

    public PlayerServiceImpl(PlayerRepository playerRepository, RiotFetchService riotFetchService, MatchService matchService) {
        this.playerRepository = playerRepository;
        this.riotFetchService = riotFetchService;
        this.matchService = matchService;
    }

    /**
     * Finds a player by their PUUID.
     *
     * @param puuid
     * @return
     */
    @Override
    public Optional<Player> findByPuuid(String puuid) {
        return playerRepository.findByPuuid(puuid);
    }

    /**
     * Finds if a player already exists in the database by their PUUID.
     *
     * @param puuid
     * @return
     */
    @Override
    public boolean existsByPuuid(String puuid) {
        return playerRepository.existsByPuuid(puuid);
    }

    /**
     * Looks for a player by their Riot game name and tag line. If it exists, returns it.
     * If not, creates a new one.
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @return
     */
    @Transactional
    @Override
    public Player searchPlayer(Platform platform, String gameName, String tagLine) {
        // Get PUUID from Riot's API
        String puuid = riotFetchService.fetchPuuid(platform, gameName, tagLine);

        // If already exists, return the existing player
        Optional<Player> existingPlayer = playerRepository.findByPuuid(puuid);
        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();

            // If this player was created as "basic" (from match participants), completes initialization
            if (player.getLastSyncAt() == null) {
                return initExistingPlayer(player, platform);
            }

            return player;
        }

        // If not, creates it
        return savePlayer(puuid, platform, gameName, tagLine);
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
        // Finds the player by PUUID
        Optional<Player> optionalPlayer = playerRepository.findByPuuid(puuid);

        // If player doesn't exist, throw an exception
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(puuid);
        }

        // If player exists, gets it
        Player player = optionalPlayer.get();

        // Gets last sync to know from when to load new matches
        Instant lastSyncAt = player.getLastSyncAt();

        // Updates summoner data (icon, level)
        SummonerDto summonerDto = riotFetchService.fetchSummoner(platform, puuid);
        player.setProfileIconId(summonerDto.profileIconId());
        player.setSummonerLevel(summonerDto.summonerLevel());

        // If was never synced, load matches since last year, otherwise since lastSync.
        if (lastSyncAt == null) {
            Instant oneYearAgo = Instant.now().minus(365, ChronoUnit.DAYS);
            matchService.loadMatchesSince(platform, puuid, 20, oneYearAgo);
        } else {
            matchService.loadMatchesSince(platform, puuid, 20, lastSyncAt);
        }

        // Update timestamp after loading matches
        player.setLastSyncAt(Instant.now());

        // Saves and return the updated player
        return playerRepository.save(player);
    }

    /**
     * Synchronizes a player's data for a professional player.
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @param puuid
     * @return
     */
    @Override
    public Player syncPlayerForProfessional(Platform platform, String gameName, String tagLine, String puuid) {
        // Fetches summoner data
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

        // Note: It doesn't update lastSyncAt because it doesn't load matches here.
        return playerRepository.save(newPlayer);
    }

    /**
     * Creates a new player if it doesn't exist.
     *
     * @param platform
     * @param gameName
     * @param tagLine
     * @return
     */
    private Player savePlayer(String puuid, Platform platform, String gameName, String tagLine) {
        // Creates new player with basic data
        Player newPlayer = new Player();

        newPlayer.setPuuid(puuid);
        newPlayer.setPlatform(platform);
        newPlayer.setGameName(gameName);
        newPlayer.setTagLine(tagLine);

        // Fetches summoner data (icon + level)
        SummonerDto summonerDto = riotFetchService.fetchSummoner(platform, puuid);
        newPlayer.setProfileIconId(summonerDto.profileIconId());
        newPlayer.setSummonerLevel(summonerDto.summonerLevel());

        // Saves player so there's an ID to link matches to
        Player savedPlayer = playerRepository.save(newPlayer);

        // Loads and saves all matches from last 365 days (max 20)
        Instant oneYearAgo = Instant.now().minus(365, ChronoUnit.DAYS);
        matchService.loadMatchesSince(platform, puuid, 20, oneYearAgo);

        // Set lastSync timestamp only after successful match loading
        savedPlayer.setLastSyncAt(Instant.now());

        return playerRepository.save(savedPlayer);
    }

    /**
     * Completes initialization for players that already exist but were created with basic data only.
     *
     * @param player
     * @param platform
     * @return
     */
    private Player initExistingPlayer(Player player, Platform platform) {
        // Fetches summoner data (icon + level)
        SummonerDto summonerDto = riotFetchService.fetchSummoner(platform, player.getPuuid());
        player.setProfileIconId(summonerDto.profileIconId());
        player.setSummonerLevel(summonerDto.summonerLevel());

        // Loads and saves all matches from last 365 days (max 20)
        Instant oneYearAgo = Instant.now().minus(365, ChronoUnit.DAYS);
        matchService.loadMatchesSince(platform, player.getPuuid(), 20, oneYearAgo);

        // Set lastSync timestamp only after successful match loading
        player.setLastSyncAt(Instant.now());

        return playerRepository.save(player);
    }

}