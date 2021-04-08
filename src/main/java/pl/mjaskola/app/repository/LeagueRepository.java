package pl.mjaskola.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.League;

/**
 * Spring Data SQL repository for the League entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeagueRepository extends JpaRepository<League, Long>, JpaSpecificationExecutor<League> {}
