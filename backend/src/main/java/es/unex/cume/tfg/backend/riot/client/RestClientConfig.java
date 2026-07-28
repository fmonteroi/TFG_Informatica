package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.riot.config.ApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Configures the shared RestClient used to call Riot APIs.
 */
@Configuration
public class RestClientConfig {
    /**
     * Creates a RestClient with Riot authentication headers and timeouts.
     *
     * @param props Riot API configuration properties.
     * @return the configured RestClient.
     */
    @Bean
    public RestClient riotRestClient(ApiProperties props) {
        if (props.key() == null || props.key().isBlank()) {
            throw new IllegalStateException("RIOT_API_KEY is not set.");
        }

        // Creates the HTTP client with a connection timeout
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(defaultIfNull(props.connectTimeout(), Duration.ofSeconds(3)))
                .build();

        // Adds a read timeout to the Spring request factory
        JdkClientHttpRequestFactory rf = new JdkClientHttpRequestFactory(httpClient);
        rf.setReadTimeout(defaultIfNull(props.readTimeout(), Duration.ofSeconds(5)));

        // Adds Riot authentication and JSON response headers
        return RestClient.builder()
                .requestFactory(rf)
                .defaultHeader("X-Riot-Token", props.key())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Uses a configured duration or its default value.
     *
     * @param value configured duration
     * @param def default duration
     * @return configured duration or default when null
     */
    private static Duration defaultIfNull(Duration value, Duration def) {
        if (value != null){
            return value;
        }else{
            return def;
        }
    }
}
