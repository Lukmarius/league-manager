package pl.mjaskola.app.service;

import java.util.List;
import java.util.Optional;
import pl.mjaskola.app.service.dto.MatchResultDTO;

/**
 * Service Interface for managing {@link pl.mjaskola.app.domain.MatchResult}.
 */
public interface MatchResultService {
    /**
     * Save a matchResult.
     *
     * @param matchResultDTO the entity to save.
     * @return the persisted entity.
     */
    MatchResultDTO save(MatchResultDTO matchResultDTO);

    /**
     * Partially updates a matchResult.
     *
     * @param matchResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MatchResultDTO> partialUpdate(MatchResultDTO matchResultDTO);

    /**
     * Get all the matchResults.
     *
     * @return the list of entities.
     */
    List<MatchResultDTO> findAll();

    /**
     * Get the "id" matchResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MatchResultDTO> findOne(Long id);

    /**
     * Delete the "id" matchResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
