package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.AccountDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/*
 * Client for ACCOUNT-V1 endpoints of Riot API.
 */
@Component
public class AccountClient {

    private final RestClient restClient;
    private final BaseUrlBuilder baseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    public AccountClient(RestClient restClient, BaseUrlBuilder baseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.baseUrlBuilder = baseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    public AccountDto fetchByRiotId(Platform platform, String gameName, String tagLine) {
        String baseUrl = baseUrlBuilder.buildRoutingBaseUrl(platform); // europe.api.riotgames.com

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("riot", "account", "v1", "accounts", "by-riot-id", gameName, tagLine)
                .build()
                .encode()
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(AccountDto.class);
    }
}