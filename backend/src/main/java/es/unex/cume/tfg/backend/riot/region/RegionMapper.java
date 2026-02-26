package es.unex.cume.tfg.backend.riot.region;

import es.unex.cume.tfg.backend.model.Platform;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper {

    public Region toRoutingRegion(Platform platform){
        switch (platform){
            case BR1, LA1, LA2, NA1, OC1 -> {
                return Region.AMERICAS;
            }
            case EUN1, EUW1, RU, TR1 -> {
                return Region.EUROPE;
            }
            case JP1, KR, SG2, TW2, VN2, ME1 -> {
                return Region.ASIA;
            }
        }
        throw new IllegalArgumentException("Unknown platform region: " + platform);
    }

    public String toPlatformCode(Platform platform){
        return platform.name().toLowerCase(); // Enum EUW1 -> "euw1"
    }

    public String toRoutingCode(Region region){
        return region.name().toLowerCase(); // Enum EUROPE -> "europe"
    }
}
