package pl.mjaskola.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.mjaskola.app.service.dto.LeagueDTO;
import pl.mjaskola.app.service.dto.LeagueWithListsDTO;

/**
 * Service Interface for managing {@link pl.mjaskola.app.domain.League}.
 */
public interface LeagueService {
    /**
     * Save a league.
     *
     * @param leagueDTO the entity to save.
     * @return the persisted entity.
     */
    LeagueDTO save(LeagueDTO leagueDTO);

    /**
     * Partially updates a league.
     *
     * @param leagueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeagueDTO> partialUpdate(LeagueDTO leagueDTO);

    /**
     * Get all the leagues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeagueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" league.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeagueDTO> findOne(Long id);

    /**
     * Delete the "id" league.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<LeagueWithListsDTO> findOneWithLists(Long id);
}
