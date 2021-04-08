package pl.mjaskola.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.LeagueStanding;

/**
 * Spring Data SQL repository for the LeagueStanding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeagueStandingRepository extends JpaRepository<LeagueStanding, Long>, JpaSpecificationExecutor<LeagueStanding> {}
