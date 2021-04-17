package pl.mjaskola.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.domain.Match;

/**
 * Spring Data SQL repository for the League entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeagueRepository extends JpaRepository<League, Long>, JpaSpecificationExecutor<League> {
    @Query(
        "select l from League l " +
        "join fetch l.leagueStandings ls " +
        "join fetch l.rounds r " +
        "join fetch r.matches m " +
        "join fetch m.matchResult mr " +
        "where :match member of r.matches"
    )
    Optional<League> findOneByMatch(@Param("match") Match match);
}
