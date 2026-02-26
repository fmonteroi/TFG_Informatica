package es.unex.cume.tfg.backend.riot.client;

import es.unex.cume.tfg.backend.model.Platform;
import es.unex.cume.tfg.backend.riot.region.RegionMapper;
import org.springframework.stereotype.Component;

@Component
public class RiotBaseUrlBuilder {

    private final RegionMapper regionMapper;

    public RiotBaseUrlBuilder(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    public String buildPlatformBaseUrl(Platform platform){
        String platformCode = regionMapper.toPlatformCode(platform);
        return "https://" + platformCode + ".api.riotgames.com";
    }

    public String buildRoutingBaseUrl(Platform platform){
        String routingCode = regionMapper.toRoutingCode(regionMapper.toRoutingRegion(platform));
        return "https://" + routingCode + ".api.riotgames.com";
    }
}
