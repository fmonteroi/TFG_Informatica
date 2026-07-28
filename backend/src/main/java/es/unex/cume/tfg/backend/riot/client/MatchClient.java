package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.MatchDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Client for MATCH-V5 endpoints of Riot API.
 */
@Component
public class MatchClient {

    private final RestClient restClient;
    private final BaseUrlBuilder baseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    /**
     * Creates the Riot match client.
     *
     * @param restClient shared REST client
     * @param baseUrlBuilder Riot URL builder
     * @param riotErrorHandler Riot error handler
     */
    public MatchClient(RestClient restClient, BaseUrlBuilder baseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.baseUrlBuilder = baseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    /**
     * Gets match IDs for a player since an optional start time.
     *
     * @param platform the Riot platform used to infer the routing region.
     * @param puuid the player PUUID.
     * @param count the number of match IDs to fetch.
     * @param start the pagination offset.
     * @param startTime the epoch seconds lower bound, or null.
     * @return the fetched match IDs.
     */
    public List<String> getMatchIdsByPuuidSince(Platform platform, String puuid, int count, int start, Long startTime) {
        // Builds the regional Match-V5 URL with pagination
        String baseUrl = baseUrlBuilder.buildRoutingBaseUrl(platform); // europe.api.riotgames.com

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "match", "v5", "matches", "by-puuid", puuid, "ids")
                .queryParam("start", start)
                .queryParam("count", count);

        if (startTime != null) {
            // Limits results to matches played after the last synchronization
            uriBuilder.queryParam("startTime", startTime);
        }

        URI uri = uriBuilder
                .build()
                .encode()
                .toUri();

        // Sends the request and keeps the list element type
        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(new ParameterizedTypeReference<>() {
                });
    }

    /**
     * Gets the full Riot match data by match ID.
     *
     * @param platform the Riot platform used to infer the routing region.
     * @param matchId the Riot match ID.
     * @return the Riot match DTO.
     */
    public MatchDto getMatchByMatchId(Platform platform, String matchId) {
        // Builds the regional Match-V5 detail URL
        String baseUrl = baseUrlBuilder.buildRoutingBaseUrl(platform);

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "match", "v5", "matches", matchId)
                .build()
                .encode()
                .toUri();

        // Sends the request and maps the match response
        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(MatchDto.class);
    }
}
