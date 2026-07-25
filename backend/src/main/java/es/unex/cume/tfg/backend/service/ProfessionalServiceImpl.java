package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.exception.ProfessionalNotFoundException;
import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.model.Player;
import es.unex.cume.tfg.backend.model.Professional;
import es.unex.cume.tfg.backend.repository.ProfessionalRepository;
import es.unex.cume.tfg.backend.riot.client.RiotApiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of ProfessionalService.
 */
@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final PlayerService playerService;
    private final RiotFetchService riotFetchService;


    public ProfessionalServiceImpl(
            ProfessionalRepository professionalRepository,
            PlayerService playerService,
            RiotFetchService riotFetchService
    ) {
        this.professionalRepository = professionalRepository;
        this.playerService = playerService;
        this.riotFetchService = riotFetchService;
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

                Professional professional = player.getProfessional();

                if (professional == null) {
                    professional = new Professional();
                    professional.setPlayer(player);
                    player.setProfessional(professional);
                }

                // Updates mutable professional data on every initialization
                professional.setProName(seed.proName());
                professional.setTeamName(seed.teamName());
                professional.setLeague(seed.league());

                professionalRepository.save(professional);
            } catch (RiotApiException exception) {
                if (exception.getStatus().value() == 429) {
                    System.err.println("Rate limit reached while initializing professionals");
                    break;
                }

                System.err.println("Error initializing professional " + seed.proName() + ": " + exception.getMessage());
            }
        }
    }

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

    @Override
    public List<Professional> findAllProfessionals() {
        return professionalRepository.findAllByOrderByProNameAsc();
    }

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
                new ProfessionalSeed(Platform.EUW1, "Myrwn", "denji", "kite", "KOI", "LEC"),
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
                new ProfessionalSeed(Platform.EUW1, "Upset", "FNC Upset", "0308", "Fnatic", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Empyros", "pt4", "000", "Fnatic", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Lospa", "i want to win", "이기고싶다", "Fnatic", "LEC"),

                // GIANTX
                new ProfessionalSeed(Platform.EUW1, "Lot", "chenzelot", "LOT", "GIANTX", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "ISMA", "ismααα", "EUW", "GIANTX", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Jackies", "detdert mid acc", "RANK1", "GIANTX", "LEC"),
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
                new ProfessionalSeed(Platform.EUW1, "Naak Nako", "VIT NaakNako", "TOP", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Lyncas", "JG top boy", "lync1", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Humanoid", "Marek Brazda1", "DOG", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Carzzy", "hovinko z kose", "marek", "Vitality", "LEC"),
                new ProfessionalSeed(Platform.EUW1, "Fleshy", "Passed Pawn", "EUW2", "Vitality", "LEC")
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
