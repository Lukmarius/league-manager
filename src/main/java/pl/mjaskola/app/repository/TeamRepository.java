package pl.mjaskola.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.Team;
import pl.mjaskola.app.domain.User;

/**
 * Spring Data SQL repository for the Team entity.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query(
        value = "select distinct team from Team team left join fetch team.users",
        countQuery = "select count(distinct team) from Team team"
    )
    Page<Team> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct team from Team team left join fetch team.users")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.users where team.id =:id")
    Optional<Team> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct team from Team team left join fetch team.users users where :user in (users)")
    List<Team> findAllWithEagerRelationshipsByUser(@Param("user") User user);

    @Query("select distinct team from Team team left join fetch team.users users where :userId in (users)")
    List<Team> findAllWithEagerRelationshipsByUserId(@Param("userId") User user);

    List<Team> findDistinctByUsersContains(User user);
}
