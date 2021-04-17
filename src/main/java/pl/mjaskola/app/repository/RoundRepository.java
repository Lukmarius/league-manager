package pl.mjaskola.app.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.domain.Round;

/**
 * Spring Data SQL repository for the Round entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoundRepository extends JpaRepository<Round, Long>, JpaSpecificationExecutor<Round> {
    Set<Round> findAllByLeague(League league);
}
