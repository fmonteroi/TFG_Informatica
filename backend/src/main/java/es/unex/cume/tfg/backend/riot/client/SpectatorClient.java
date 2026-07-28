package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.dto.CurrentGameInfoDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

/**
 * Client for SPECTATOR-V5 endpoints of Riot API.
 */
@Component
public class SpectatorClient {

    private final RestClient restClient;
    private final BaseUrlBuilder baseUrlBuilder;
    private final RiotErrorHandler riotErrorHandler;

    /**
     * Creates the Riot spectator client.
     *
     * @param restClient shared REST client
     * @param baseUrlBuilder Riot URL builder
     * @param riotErrorHandler Riot error handler
     */
    public SpectatorClient(RestClient restClient, BaseUrlBuilder baseUrlBuilder, RiotErrorHandler riotErrorHandler) {
        this.restClient = restClient;
        this.baseUrlBuilder = baseUrlBuilder;
        this.riotErrorHandler = riotErrorHandler;
    }

    /**
     * Gets the current active game for a player.
     *
     * @param platform the Riot platform.
     * @param puuid the player PUUID.
     * @return the current game if Riot reports one.
     */
    public Optional<CurrentGameInfoDto> fetchCurrentGameByPuuid(Platform platform, String puuid) {
        // Builds the platform Spectator-V5 URL
        String baseUrl = baseUrlBuilder.buildPlatformBaseUrl(platform); // euw1.api.riotgames.com

        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment("lol", "spectator", "v5", "active-games", "by-summoner", puuid)
                .build()
                .encode()
                .toUri();

        try {
            // Sends the request and maps the active game response
            CurrentGameInfoDto currentGame = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, riotErrorHandler::handleError)
                    .body(CurrentGameInfoDto.class);

            return Optional.ofNullable(currentGame);
        } catch (RiotApiException ex) {
            // Riot returns 404 when the player is not in a game
            if (ex.getStatus().value() == 404) {
                return Optional.empty();
            }
            throw ex;
        }
    }
}
