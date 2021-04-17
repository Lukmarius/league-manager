package pl.mjaskola.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.domain.Match;

/**
 * Spring Data SQL repository for the LeagueStanding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeagueStandingRepository extends JpaRepository<LeagueStanding, Long>, JpaSpecificationExecutor<LeagueStanding> {
    @Query("select ls from LeagueStanding ls " + "left join ls.league l " + "left join l.rounds r " + "where :match member of r.matches")
    List<LeagueStanding> findAllInLeagueByMatch(@Param("match") Match matchEntity);
}
