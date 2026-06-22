package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.SummonerDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Client for SUMMONER-V4 endpoints of Riot API.
 */
@Component
public class SummonerClient {

    private final RestClient restClient;
    private final BaseUrlBuilder baseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    public SummonerClient(RestClient restClient, BaseUrlBuilder baseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.baseUrlBuilder = baseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    /**
     * Fetches summoner profile data by PUUID.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the Riot summoner DTO.
     */
    public SummonerDto fetchByPuuid(Platform platform, String puuid) {
        String baseUrl = baseUrlBuilder.buildPlatformBaseUrl(platform); // euw1.api.riotgames.com

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "summoner", "v4", "summoners", "by-puuid", puuid)
                .build()
                .encode()
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(SummonerDto.class);
    }
}
