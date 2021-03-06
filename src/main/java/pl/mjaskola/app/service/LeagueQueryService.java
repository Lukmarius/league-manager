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
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.repository.LeagueRepository;
import pl.mjaskola.app.service.criteria.LeagueCriteria;
import pl.mjaskola.app.service.dto.LeagueDTO;
import pl.mjaskola.app.service.mapper.LeagueMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link League} entities in the database.
 * The main input is a {@link LeagueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeagueDTO} or a {@link Page} of {@link LeagueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeagueQueryService extends QueryService<League> {

    private final Logger log = LoggerFactory.getLogger(LeagueQueryService.class);

    private final LeagueRepository leagueRepository;

    private final LeagueMapper leagueMapper;

    public LeagueQueryService(LeagueRepository leagueRepository, LeagueMapper leagueMapper) {
        this.leagueRepository = leagueRepository;
        this.leagueMapper = leagueMapper;
    }

    /**
     * Return a {@link List} of {@link LeagueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeagueDTO> findByCriteria(LeagueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<League> specification = createSpecification(criteria);
        return leagueMapper.toDto(leagueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeagueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeagueDTO> findByCriteria(LeagueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<League> specification = createSpecification(criteria);
        return leagueRepository.findAll(specification, page).map(leagueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeagueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<League> specification = createSpecification(criteria);
        return leagueRepository.count(specification);
    }

    /**
     * Function to convert {@link LeagueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<League> createSpecification(LeagueCriteria criteria) {
        Specification<League> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), League_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), League_.name));
            }
            if (criteria.getRoundId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoundId(), root -> root.join(League_.rounds, JoinType.LEFT).get(Round_.id))
                    );
            }
            if (criteria.getLeagueStandingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeagueStandingId(),
                            root -> root.join(League_.leagueStandings, JoinType.LEFT).get(LeagueStanding_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
