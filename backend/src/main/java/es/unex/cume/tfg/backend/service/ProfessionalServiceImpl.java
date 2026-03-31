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
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final PlayerService playerService;
    private final RiotFetchService riotFetchService;
    private final MatchService matchService;

    // To prevent multiple professional refresh jobs from running at the same time.
    private final ReentrantLock professionalsRefreshLock = new ReentrantLock();


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
        List<ProfessionalSeed> seeds = getInitialProfessionals();

        // For each seed, it fetches the puuid, syncs the basic player and creates the professional
        for (ProfessionalSeed seed : seeds) {
            try {
                // Fetches the PUUID from Riot's API
                String puuid = riotFetchService.fetchPuuid(seed.platform(), seed.gameName(), seed.tagLine());

                // Syncs the basic player info
                Player player = playerService.syncPlayerForProfessional(seed.platform(), seed.gameName(), seed.tagLine(), puuid);

                // If this player is already linked to a professional, skip it
                if (player.getProfessional() != null) {
                    continue;
                }

                // Otherwise, creates the professional
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
        if (!professionalsRefreshLock.tryLock()) {
            return new ProfessionalsRefreshResultDto(
                    0,
                    0,
                    0,
                    false,
                    "Ya hay un refresco de profesionales en curso."
            );
        }

        try {
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

            message = message + " Se actualizaron " + successful + "/" + professionals.size() + " profesionales.";

            return new ProfessionalsRefreshResultDto(professionals.size(), processed, successful, stoppedByRateLimit, message);
        } finally {
            professionalsRefreshLock.unlock();
        }
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
                new ProfessionalSeed(Platform.EUW1, "SkewMond", "G2 SkewMond", "3327", "G2", "LEC"),

                // Fnatic
                new ProfessionalSeed(Platform.EUW1, "Razork", "Razørk Activoo", "razzz", "Fnatic", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Vladi", "J1HUIV", "000", "Fnatic", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Upset", "afkdoks", "3101", "Fnatic", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Empyros", "pt4", "000", "Fnatic", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Lospa", "i want to win", "이기고싶다", "Fnatic", "LEC"),

                // GIANTX
                new ProfessionalSeed(Platform.EUW1, "Lot", "chenzelot", "LOT", "GIANTX", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "ISMA", "ismααα", "EUW", "GIANTX", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Jackies", "Michael Jackson", "MJWIN", "GIANTX", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Noah", "GX Monkey", "XDD", "GIANTX", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Jun", "Guilhoto", "Messi", "GIANTX", "LEC"),

                // KC
                new ProfessionalSeed(Platform.EUW1, "Canna", "Katze", "myao", "KC", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Yike", "KC Yiken", "1111", "KC", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "kyeahoo", "Left Hand", "korea", "KC", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Caliste", "KC NEXT ADKING", "EUW", "KC", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Busio", "basil", "fan", "KC", "LEC"),

                // NAVI
                new ProfessionalSeed(Platform.EUW1, "Maynter", "Maynter", "EUW", "NAVI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Rhilech", "Rhilech", "15105", "NAVI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Poby", "T1 cloud", "2007", "NAVI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "SamD", "T1 Smash", "2006", "NAVI", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Parus", "Thumbs Down", "4847", "NAVI", "LEC"),

                // Vitality
                new ProfessionalSeed(Platform.EUW1, "Naak Nako", "El Matador", "VIT", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Lyncas", "JG top boy", "lync1", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Humanoid", "Marek Brazda1", "DOG", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Carzzy", "hovinko z kose", "marek", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Fleshy", "VIT Fleshy", "EU1", "Vitality", "LEC")
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