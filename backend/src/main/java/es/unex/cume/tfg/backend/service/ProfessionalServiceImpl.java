package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ProfessionalsRefreshResultDto;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Professional;
import es.unex.cume.tfg.backend.repository.ProfessionalRepository;
import es.unex.cume.tfg.backend.riot.client.RiotApiException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final PlayerService playerService;
    private final RiotFetchService riotFetchService;
    private final MatchService matchService;

    public ProfessionalServiceImpl(
            ProfessionalRepository professionalRepository,
            PlayerService playerService,
            RiotFetchService riotFetchService,
            MatchService matchService
    ) {
        this.professionalRepository = professionalRepository;
        this.playerService = playerService;
        this.riotFetchService = riotFetchService;
        this.matchService = matchService;
    }

    /**
     * Initializes professionals in the db.
     */
    @Override
    public void initProfessionals() {
        // If there are already professionals, it returns
        if (professionalRepository.count() > 0) {
            return;
        }

        // Otherwise, it initializes the professionals from the seeds
        List<ProfessionalSeed> seeds = getInitialProfessionals();

        // For each seed, it fetches the puuid, syncs the basic player and creates the professional
        for (ProfessionalSeed seed : seeds) {
            try {
                // Fetches the PUUID from Riot's API
                String puuid = riotFetchService.fetchPuuid(seed.platform(), seed.gameName(), seed.tagLine());

                // Syncs the basic player info
                Player player = playerService.syncPlayerForProfessional(seed.platform(), seed.gameName(), seed.tagLine(), puuid);

                // Creates the professional
                Professional professional = new Professional();
                professional.setPlayer(player);
                professional.setProName(seed.proName());
                professional.setTeamName(seed.teamName());
                professional.setLeague(seed.league());

                player.setProfessional(professional);

                professionalRepository.save(professional);
            } catch (RiotApiException ex) {
                System.err.printf(
                        "Error fetching PUUID for %s using Riot ID %s#%s: %s%n",
                        seed.proName(),
                        seed.gameName(),
                        seed.tagLine(),
                        ex.getMessage()
                );
            }
        }
    }

    @Override
    public ProfessionalsRefreshResultDto refreshProfessionals() {
        List<Professional> professionals = professionalRepository.findAllWithPlayer();

        int processed = 0;
        int successful = 0;
        boolean stoppedByRateLimit = false;
        for (Professional professional : professionals) {
            try {
                refreshProfessional(professional);
                successful++;
                processed++;
            } catch (RiotApiException ex) {
                processed++;
                if (ex.getStatus().value() == 429) {
                    stoppedByRateLimit = true;
                    break;
                }
            } catch (Exception ex) {
                processed++;
            }
        }

        String message;
        if (stoppedByRateLimit) {
            message = "Rate Limit alcanzado.";
        } else {
            message = "Refresco completado.";
        }

        message = message + " Se procesaron " + successful + "/" + professionals.size() + " profesionales.";

        return new ProfessionalsRefreshResultDto(professionals.size(), processed, successful, stoppedByRateLimit, message);
    }


    /**
     * Refreshes professional recent matches.
     *
     * @param professional the professional to refresh
     */
    private void refreshProfessional(Professional professional) {
        // Basic variables for the match fetching
        Player player = professional.getPlayer();
        Platform platform = player.getPlatform();
        String puuid = professional.getPuuid();
        Instant lastSyncAt = player.getLastSyncAt();
        Instant now = Instant.now();

        // If it's the first time refreshing, loads matches from the last 30 days (max 20)
        if (lastSyncAt == null) {
            Instant oneMonthAgo = now.minus(30, ChronoUnit.DAYS);
            matchService.loadMatchesSince(platform, puuid, 20, oneMonthAgo);
        } else { // Otherwise, loads matches since last build update (max 20)
            matchService.loadMatchesSince(platform, puuid, 20, lastSyncAt);
        }

        // Updates last sync time
        player.setLastSyncAt(Instant.now());
        professionalRepository.save(professional);
    }

    /**
     * Gets initial professionals to be added to the db.
     *
     * @return the initial professional seeds
     */
    private List<ProfessionalSeed> getInitialProfessionals() {
        return List.of(
                // KOI
                new ProfessionalSeed(Platform.EUW1, "Elyoya", "komanche uchiha", "elite", "KOI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Alvaro", "alvarooo", "000", "KOI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Supa", "tukaan", "tukan", "KOI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Myrwn", "snoopy", "kite", "KOI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "jojopyun", "VJBYYTF0AO", "EUW", "KOI", "LEC"),

                // G2
                new ProfessionalSeed(Platform.EUW1, "Caps", "G2 Caps", "1323", "G2", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Labrov", "G2 Labrov", "8085", "G2", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Hans sama", "G2 Hans Sama", "12838", "G2", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "BrokenBlade", "G2 BrokenBlade", "1918", "G2", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "SkewMond", "G2 SkewMond", "3327", "G2", "LEC")
        );
    }

    /**
     * Record that represents the data for a professional player.
     *
     * @param platform
     * @param proName
     * @param gameName
     * @param tagLine
     * @param teamName
     * @param league
     */
    private record ProfessionalSeed(
            Platform platform,
            String proName,
            String gameName,
            String tagLine,
            String teamName,
            String league
    ) {
    }

}