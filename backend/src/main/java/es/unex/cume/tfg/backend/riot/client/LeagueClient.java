package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.LeagueEntryDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class LeagueClient {

    private final RestClient restClient;
    private final BaseUrlBuilder baseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    public LeagueClient(RestClient restClient, BaseUrlBuilder baseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.baseUrlBuilder = baseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    public List<LeagueEntryDto> getEntriesByPuuid(Platform platform, String puuid) {
        String baseUrl = baseUrlBuilder.buildPlatformBaseUrl(platform);

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "league", "v4", "entries", "by-puuid", puuid)
                .build()
                .encode()
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(new ParameterizedTypeReference<>() {});
    }
}