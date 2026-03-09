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

/*
 * Client for MATCH-V5 endpoints of Riot API.
 */
@Component
public class MatchClient {

    private final RestClient restClient;
    private final BaseUrlBuilder baseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    public MatchClient(RestClient restClient, BaseUrlBuilder baseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.baseUrlBuilder = baseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    public List<String> getMatchIdsByPuuidSince(Platform platform, String puuid, int count, int start, Long startTime) {
        String baseUrl = baseUrlBuilder.buildRoutingBaseUrl(platform); // europe.api.riotgames.com

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "match", "v5", "matches", "by-puuid", puuid, "ids")
                .queryParam("start", start)
                .queryParam("count", count);

        if (startTime != null) {
            uriBuilder.queryParam("startTime", startTime);
        }

        URI uri = uriBuilder
                .build()
                .encode()
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public MatchDto getMatchByMatchId(Platform platform, String matchId) {
        String baseUrl = baseUrlBuilder.buildRoutingBaseUrl(platform);

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "match", "v5", "matches", matchId)
                .build()
                .encode()
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(MatchDto.class);
    }
}