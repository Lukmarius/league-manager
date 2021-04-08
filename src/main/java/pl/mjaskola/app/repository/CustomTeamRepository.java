package pl.mjaskola.app.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.Team;
import pl.mjaskola.app.domain.User;

/**
 * Spring Data SQL repository for the Team entity.
 */
@Repository
public interface CustomTeamRepository extends TeamRepository {
    List<Team> findDistinctByUsersContains(User user);
}
