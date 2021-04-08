package pl.mjaskola.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.mjaskola.app.domain.MatchResult;

/**
 * Spring Data SQL repository for the MatchResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {}
