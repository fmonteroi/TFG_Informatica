package es.unex.cume.tfg.backend.loader;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Loads the configured professional roster from a JSON resource.
 */
@Component
public class ProfessionalJsonLoader {

    private final ObjectMapper objectMapper;
    private final Resource professionalsResource;

    /**
     * Creates a professional roster loader.
     *
     * @param objectMapper mapper used to deserialize JSON content
     * @param professionalsResource configured professional roster resource
     */
    public ProfessionalJsonLoader(ObjectMapper objectMapper, @Value("${app.professionals.file:classpath:data/professionals.json}") Resource professionalsResource) {
        this.objectMapper = objectMapper;
        this.professionalsResource = professionalsResource;
    }

    /**
     * Loads and deserializes all configured professional entries.
     *
     * @return configured professional roster entries
     * @throws IllegalStateException when the roster cannot be read or parsed
     */
    public List<ProfessionalEntry> load() {
        // Opens the configured roster resource
        try (InputStream inputStream = professionalsResource.getInputStream()) {
            // Maps the JSON content into professional entries
            return objectMapper.readValue(inputStream, new TypeReference<List<ProfessionalEntry>>() {});
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load professional roster", exception);
        }
    }
}
