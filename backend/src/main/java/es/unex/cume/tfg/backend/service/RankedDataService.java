package es.unex.cume.tfg.backend.service;

import java.util.List;
import java.util.Optional;

/**
 * Provides the common ranked queues and latest stored patch.
 */
public interface RankedDataService {

    /**
     * Gets the ranked queue identifiers used by the application.
     *
     * @return ranked queue identifiers
     */
    List<Integer> getQueueIds();

    /**
     * Finds the patch of the latest stored ranked match.
     *
     * @return latest stored ranked patch when available
     */
    Optional<String> findLatestPatch();
}
