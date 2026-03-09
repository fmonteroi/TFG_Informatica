package es.unex.cume.tfg.backend.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ChampionJsonLoader {

    private final ObjectMapper objectMapper;

    public ChampionJsonLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ChampionSeed> loadChampionSeeds() {
        try (InputStream inputStream = new ClassPathResource("static/champion.json").getInputStream()) {
            ChampionJsonFile championJsonFile = objectMapper.readValue(inputStream, ChampionJsonFile.class);

            if (championJsonFile == null || championJsonFile.data() == null || championJsonFile.data().isEmpty()) {
                throw new IllegalStateException("champion.json does not contain champion data");
            }

            List<ChampionSeed> championSeeds = new ArrayList<>();

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

    public record ChampionSeed(
            Integer championId,
            String championName
    ) {
    }

    private record ChampionJsonFile(Map<String, ChampionJsonData> data) {
    }

    private record ChampionJsonData(String key, String name) {
    }
}
