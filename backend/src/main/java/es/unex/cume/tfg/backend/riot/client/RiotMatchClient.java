package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.RiotMatchDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/*
 * Client for MATCH-V5 endpoints of Riot API.
 */
@Component
public class RiotMatchClient {

    private final RestClient restClient;
    private final RiotBaseUrlBuilder riotBaseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    public RiotMatchClient(RestClient restClient, RiotBaseUrlBuilder riotBaseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.riotBaseUrlBuilder = riotBaseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    public List<String> getMatchIdsByPuuid(Platform platform, String puuid, int count) {
        String baseUrl = riotBaseUrlBuilder.buildRoutingBaseUrl(platform);

        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "match", "v5", "matches", "by-puuid", puuid, "ids")
                .queryParam("start", 0)
                .queryParam("count", count)
                .encode()
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(new ParameterizedTypeReference<>() {});
    }

    public RiotMatchDto getMatchByMatchId(Platform platform, String matchId) {
        String baseUrl = riotBaseUrlBuilder.buildRoutingBaseUrl(platform);

        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "match", "v5", "matches", matchId)
                .encode()
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(RiotMatchDto.class);
    }
}

