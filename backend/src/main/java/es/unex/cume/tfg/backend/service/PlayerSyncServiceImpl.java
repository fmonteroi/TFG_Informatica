package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.repository.PlayerRepository;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Special service for synchronizing basic player information from Riot's MatchDto.Participant data.
 *
 * Note: It must be done here because ParticipationService cannot depend on PlayerService to
 * avoid circular dependencies.
 *
 */
@Service
public class PlayerSyncServiceImpl implements PlayerSyncService {

    private final PlayerRepository playerRepository;

    public PlayerSyncServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Synchronizes basic player information from a MatchDto.Participant.
     *
     * @param participant
     * @param platform
     * @return
     */
    @Override
    public Player syncBasicPlayer(MatchDto.Participant participant, Platform platform) {
        // Check if player already exists by PUUID
        Optional<Player> optionalPlayer = playerRepository.findByPuuid(participant.puuid());

        // If exists, returns it
        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get();
        }

        // Otherwise, creates a new Player entity with the basic information and saves it
        Player player = new Player();
        player.setPuuid(participant.puuid());
        player.setPlatform(platform);
        player.setGameName(participant.riotIdGameName());
        player.setTagLine(participant.riotIdTagline());

        try {
            return playerRepository.save(player);
        } catch (DataIntegrityViolationException ex) {
            // Another process inserted the same player while this one was trying to save it.
            Optional<Player> existingPlayer = playerRepository.findByPuuid(participant.puuid());

            if (existingPlayer.isPresent()) {
                return existingPlayer.get();
            }

            throw ex;
        }
    }
}
