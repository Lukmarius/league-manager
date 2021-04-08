package pl.mjaskola.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.mjaskola.app.service.dto.LeagueStandingDTO;

/**
 * Service Interface for managing {@link pl.mjaskola.app.domain.LeagueStanding}.
 */
public interface LeagueStandingService {
    /**
     * Save a leagueStanding.
     *
     * @param leagueStandingDTO the entity to save.
     * @return the persisted entity.
     */
    LeagueStandingDTO save(LeagueStandingDTO leagueStandingDTO);

    /**
     * Partially updates a leagueStanding.
     *
     * @param leagueStandingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeagueStandingDTO> partialUpdate(LeagueStandingDTO leagueStandingDTO);

    /**
     * Get all the leagueStandings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeagueStandingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leagueStanding.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeagueStandingDTO> findOne(Long id);

    /**
     * Delete the "id" leagueStanding.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
