package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.region.RegionMapper;
import org.springframework.stereotype.Component;

/**
 * Builds Riot API base URLs from platform and routing information.
 */
@Component
public class BaseUrlBuilder {

    private final RegionMapper regionMapper;

    /**
     * Creates the Riot base URL builder.
     *
     * @param regionMapper mapper used to get Riot host codes
     */
    public BaseUrlBuilder(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    /**
     * Builds a platform-scoped Riot API base URL.
     *
     * @param platform the Riot platform.
     * @return the platform base URL.
     */
    public String buildPlatformBaseUrl(Platform platform){
        String platformCode = regionMapper.toPlatformCode(platform);
        return "https://" + platformCode + ".api.riotgames.com"; // euw1.api.riotgames.com
    }

    /**
     * Builds a routing-region Riot API base URL.
     *
     * @param platform the Riot platform used to infer the routing region.
     * @return the routing base URL.
     */
    public String buildRoutingBaseUrl(Platform platform){
        String routingCode = regionMapper.toRoutingCode(regionMapper.toRoutingRegion(platform));
        return "https://" + routingCode + ".api.riotgames.com"; // europe.api.riotgames.com
    }
}
