package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.ProBuildDto;
import es.unex.cume.tfg.backend.model.Build;

import java.util.List;
import java.util.Optional;

public interface BuildService {

    Optional<Build> findByParticipationId(Long participationId);

    Build saveBuild(Build build);

    List<ProBuildDto> findRecentProBuildsByChampionId(Integer championId, int limit);
}

