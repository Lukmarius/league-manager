package pl.mjaskola.app.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.domain.*; // for static metamodels
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.repository.LeagueStandingRepository;
import pl.mjaskola.app.service.criteria.LeagueStandingCriteria;
import pl.mjaskola.app.service.dto.LeagueStandingDTO;
import pl.mjaskola.app.service.mapper.LeagueStandingMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LeagueStanding} entities in the database.
 * The main input is a {@link LeagueStandingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeagueStandingDTO} or a {@link Page} of {@link LeagueStandingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeagueStandingQueryService extends QueryService<LeagueStanding> {

    private final Logger log = LoggerFactory.getLogger(LeagueStandingQueryService.class);

    private final LeagueStandingRepository leagueStandingRepository;

    private final LeagueStandingMapper leagueStandingMapper;

    public LeagueStandingQueryService(LeagueStandingRepository leagueStandingRepository, LeagueStandingMapper leagueStandingMapper) {
        this.leagueStandingRepository = leagueStandingRepository;
        this.leagueStandingMapper = leagueStandingMapper;
    }

    /**
     * Return a {@link List} of {@link LeagueStandingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeagueStandingDTO> findByCriteria(LeagueStandingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeagueStanding> specification = createSpecification(criteria);
        return leagueStandingMapper.toDto(leagueStandingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeagueStandingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeagueStandingDTO> findByCriteria(LeagueStandingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeagueStanding> specification = createSpecification(criteria);
        return leagueStandingRepository.findAll(specification, page).map(leagueStandingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeagueStandingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeagueStanding> specification = createSpecification(criteria);
        return leagueStandingRepository.count(specification);
    }

    /**
     * Function to convert {@link LeagueStandingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeagueStanding> createSpecification(LeagueStandingCriteria criteria) {
        Specification<LeagueStanding> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeagueStanding_.id));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition(), LeagueStanding_.position));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), LeagueStanding_.points));
            }
            if (criteria.getScoredGoals() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScoredGoals(), LeagueStanding_.scoredGoals));
            }
            if (criteria.getLostGoals() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLostGoals(), LeagueStanding_.lostGoals));
            }
            if (criteria.getWins() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWins(), LeagueStanding_.wins));
            }
            if (criteria.getDraws() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDraws(), LeagueStanding_.draws));
            }
            if (criteria.getLosses() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLosses(), LeagueStanding_.losses));
            }
            if (criteria.getTeamId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeamId(), root -> root.join(LeagueStanding_.team, JoinType.LEFT).get(Team_.id))
                    );
            }
            if (criteria.getLeagueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLeagueId(), root -> root.join(LeagueStanding_.league, JoinType.LEFT).get(League_.id))
                    );
            }
        }
        return specification;
    }
}
