package es.unex.cume.tfg.backend.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads champion seed data from the bundled champion JSON file.
 */
@Component
public class ChampionJsonLoader {

    private final ObjectMapper objectMapper;

    public ChampionJsonLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Reads champion seeds from the classpath JSON resource.
     *
     * @return the champion seeds.
     */
    public List<ChampionSeed> loadChampionSeeds() {
        // Opens the champion catalog resource
        try (InputStream inputStream = new ClassPathResource("data/champion.json").getInputStream()) {
            // Maps the JSON content into its internal structure
            ChampionJsonFile championJsonFile = objectMapper.readValue(inputStream, ChampionJsonFile.class);

            if (championJsonFile == null || championJsonFile.data() == null || championJsonFile.data().isEmpty()) {
                throw new IllegalStateException("champion.json does not contain champion data");
            }

            List<ChampionSeed> championSeeds = new ArrayList<>();

            // Converts each JSON champion into an initialization entry
            for (ChampionJsonData championJsonData : championJsonFile.data().values()) {
                Integer championId = Integer.valueOf(championJsonData.key());
                String championName = championJsonData.name();
                championSeeds.add(new ChampionSeed(championId, championName));
            }

            return championSeeds;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load champions from champion.json", ex);
        }
    }

    /**
     * Seed data used to synchronize the local champion catalog.
     */
    public record ChampionSeed(
            Integer championId,
            String championName
    ) {
    }

    /**
     * Root structure of the champion JSON file.
     */
    private record ChampionJsonFile(Map<String, ChampionJsonData> data) {
    }

    /**
     * Champion entry structure in the champion JSON file.
     */
    private record ChampionJsonData(String key, String name) {
    }
}
