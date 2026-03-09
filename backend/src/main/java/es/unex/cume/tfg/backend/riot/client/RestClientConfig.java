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

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient riotRestClient(ApiProperties props) {
        if (props.key() == null || props.key().isBlank()) {
            throw new IllegalStateException("RIOT_API_KEY is not set.");
        }

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(defaultIfNull(props.connectTimeout(), Duration.ofSeconds(3)))
                .build();

        JdkClientHttpRequestFactory rf = new JdkClientHttpRequestFactory(httpClient);
        rf.setReadTimeout(defaultIfNull(props.readTimeout(), Duration.ofSeconds(5)));

        return RestClient.builder()
                .requestFactory(rf)
                .defaultHeader("X-Riot-Token", props.key())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private static Duration defaultIfNull(Duration value, Duration def) {
        if (value != null){
            return value;
        }else{
            return def;
        }
    }
}
