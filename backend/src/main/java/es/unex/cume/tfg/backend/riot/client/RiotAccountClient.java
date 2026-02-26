package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.RiotAccountDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

/*
 * Client for ACCOUNT-V1 endpoints of Riot API.
 */
@Component
public class RiotAccountClient {

    private final RestClient restClient;
    private final RiotBaseUrlBuilder riotBaseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    public RiotAccountClient(RestClient restClient, RiotBaseUrlBuilder riotBaseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.riotBaseUrlBuilder = riotBaseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    public RiotAccountDto fetchByRiotId(Platform platform, String gameName, String tagLine) {
        String baseUrl = riotBaseUrlBuilder.buildRoutingBaseUrl(platform);

        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("riot", "account", "v1", "accounts", "by-riot-id", gameName, tagLine)
                .encode()
                .toUriString();

        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                .body(RiotAccountDto.class);
    }
}
