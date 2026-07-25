package es.unex.cume.tfg.backend.riot.region;

import es.unex.cume.tfg.backend.model.Platform;
import org.springframework.stereotype.Component;

/**
 * Maps Riot platform values to platform and routing host codes.
 */
@Component
public class RegionMapper {

    /**
     * Maps a platform to the routing region required by regional Riot endpoints.
     *
     * @param platform the Riot platform.
     * @return the routing region.
     */
    public Region toRoutingRegion(Platform platform){
        switch (platform){
            case BR1, LA1, LA2, NA1 -> {
                return Region.AMERICAS;
            }
            case EUN1, EUW1, ME1, RU, TR1 -> {
                return Region.EUROPE;
            }
            case JP1, KR -> {
                return Region.ASIA;
            }
            case OC1, PH2, SG2, TH2, TW2, VN2 -> {
                return Region.SEA;
            }
        }
        throw new IllegalArgumentException("Unknown platform region: " + platform);
    }

    /**
     * Converts a platform enum to the lowercase Riot host code.
     *
     * @param platform the Riot platform.
     * @return the lowercase platform code.
     */
    public String toPlatformCode(Platform platform){
        return platform.name().toLowerCase(); // Enum EUW1 -> "euw1"
    }

    /**
     * Converts a routing region enum to the lowercase Riot host code.
     *
     * @param region the Riot routing region.
     * @return the lowercase routing code.
     */
    public String toRoutingCode(Region region){
        return region.name().toLowerCase(); // Enum EUROPE -> "europe"
    }
}
