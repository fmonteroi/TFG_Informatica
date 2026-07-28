package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.config.ProfessionalEntry;
import es.unex.cume.tfg.backend.config.ProfessionalJsonLoader;
import es.unex.cume.tfg.backend.exception.ProfessionalNotFoundException;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Professional;
import es.unex.cume.tfg.backend.repository.ProfessionalRepository;
import es.unex.cume.tfg.backend.riot.client.RiotApiException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Default implementation of ProfessionalService.
 */
@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final PlayerService playerService;
    private final RiotFetchService riotFetchService;
    private final ProfessionalJsonLoader professionalJsonLoader;


    /**
     * Creates the professional player service.
     *
     * @param professionalRepository professional repository
     * @param playerService player service
     * @param riotFetchService Riot data service
     * @param professionalJsonLoader professional roster loader
     */
    public ProfessionalServiceImpl(ProfessionalRepository professionalRepository, PlayerService playerService, RiotFetchService riotFetchService, ProfessionalJsonLoader professionalJsonLoader) {
        this.professionalRepository = professionalRepository;
        this.playerService = playerService;
        this.riotFetchService = riotFetchService;
        this.professionalJsonLoader = professionalJsonLoader;
    }

    /**
     * Synchronizes stored professionals with the configured roster.
     */
    @Override
    public void synchronizeProfessionals() {
        // Loads the configured roster and stored professionals
        List<ProfessionalEntry> entries = professionalJsonLoader.load();
        List<Professional> existingProfessionals = professionalRepository.findAllWithPlayerOrderByLastSyncAt();

        // Tracks stored professionals that are not present in the new roster
        Map<String, Professional> existingByCode = new HashMap<>();

        for (Professional professional : existingProfessionals) {
            existingByCode.put(professional.getCode(), professional);
        }

        boolean synchronizationCompleted = true;

        for (ProfessionalEntry entry : entries) {
            Professional professional = existingByCode.remove(entry.code());

            if (professional != null) {
                // Updates roster data without calling Riot
                updateProfessional(professional, entry);
                professionalRepository.save(professional);
                continue;
            }

            try {
                // Calls Riot only when the professional is not stored yet
                createProfessional(entry);
            } catch (RiotApiException exception) {
                synchronizationCompleted = false;

                System.err.println("Error creating professional " + entry.proName() + ": " + exception.getMessage());

                if (exception.getStatus().value() == 429) {
                    break;
                }
            }
        }

        // Avoid deleting professionals after an incomplete synchronization
        if (synchronizationCompleted) {
            removeMissingProfessionals(existingByCode.values());
        }
    }

    /**
     * Refreshes every professional until all finish or Riot limits requests.
     */
    @Override
    public void refreshProfessionals() {

        List<Professional> professionals = professionalRepository.findAllWithPlayerOrderByLastSyncAt();

        int refreshed = 0;

        for (Professional professional : professionals) {
            try {
                refreshProfessional(professional);
                refreshed++;
            } catch (RiotApiException exception) {
                if (exception.getStatus().value() == 429) {
                    System.err.println("Rate limit reached while refreshing professionals");
                    break;
                }

                System.err.println("Riot API error refreshing professional " + professional.getProName() + ": " + exception.getMessage());

            } catch (Exception exception) {
                System.err.println("Unexpected error refreshing professional " + professional.getProName() + ": " + exception.getMessage());
            }
        }

        System.out.println("Professional refresh completed: " + refreshed + "/" + professionals.size());
    }

    /**
     * Finds every professional ordered by name.
     *
     * @return professional players
     */
    @Override
    public List<Professional> findAllProfessionals() {
        return professionalRepository.findAllByOrderByProNameAsc();
    }

    /**
     * Finds one professional with their player account loaded.
     *
     * @param puuid professional player PUUID
     * @return matching professional player
     */
    @Override
    public Professional findProfessional(String puuid) {
        Optional<Professional> optionalProfessional = professionalRepository.findByPuuidWithPlayer(puuid);

        if (optionalProfessional.isEmpty()) {
            throw new ProfessionalNotFoundException(puuid);
        }

        return optionalProfessional.get();
    }


    /**
     * Refreshes professional recent matches.
     *
     * @param professional the professional to refresh
     */
    private void refreshProfessional(Professional professional) {
        Player player = professional.getPlayer();

        playerService.refreshPlayer(player.getPlatform(), player.getPuuid());
    }

    /**
     * Updates roster fields that may change between splits.
     *
     * @param professional stored professional
     * @param entry current roster entry
     */
    private void updateProfessional(Professional professional, ProfessionalEntry entry) {
        professional.setProName(entry.proName());
        professional.setTeamName(entry.teamName());
        professional.setLeague(entry.league());
        professional.setRole(entry.role());
    }

    /**
     * Creates a professional and links the matching player account.
     *
     * @param entry roster entry to create
     */
    private void createProfessional(ProfessionalEntry entry) {
        // Gets the stable PUUID for a new roster entry
        String puuid = riotFetchService.fetchPuuid(entry.platform(), entry.gameName(), entry.tagLine());

        // Creates or updates the basic player account
        Player player = playerService.syncPlayerForProfessional(entry.platform(), entry.gameName(), entry.tagLine(), puuid);

        Professional professional = new Professional();
        professional.setCode(entry.code());
        professional.setPlayer(player);

        updateProfessional(professional, entry);

        player.setProfessional(professional);
        professionalRepository.save(professional);
    }

    /**
     * Removes professional links that are no longer in the roster.
     *
     * @param professionals professionals missing from the configured roster
     */
    private void removeMissingProfessionals(Collection<Professional> professionals) {
        for (Professional professional : professionals) {
            Player player = professional.getPlayer();
            player.setProfessional(null);
            professionalRepository.delete(professional);
        }
    }

}
