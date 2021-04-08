package pl.mjaskola.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.mjaskola.app.repository.LeagueStandingRepository;
import pl.mjaskola.app.service.LeagueStandingQueryService;
import pl.mjaskola.app.service.LeagueStandingService;
import pl.mjaskola.app.service.criteria.LeagueStandingCriteria;
import pl.mjaskola.app.service.dto.LeagueStandingDTO;
import pl.mjaskola.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.mjaskola.app.domain.LeagueStanding}.
 */
@RestController
@RequestMapping("/api")
public class LeagueStandingResource {

    private final Logger log = LoggerFactory.getLogger(LeagueStandingResource.class);

    private static final String ENTITY_NAME = "leagueStanding";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeagueStandingService leagueStandingService;

    private final LeagueStandingRepository leagueStandingRepository;

    private final LeagueStandingQueryService leagueStandingQueryService;

    public LeagueStandingResource(
        LeagueStandingService leagueStandingService,
        LeagueStandingRepository leagueStandingRepository,
        LeagueStandingQueryService leagueStandingQueryService
    ) {
        this.leagueStandingService = leagueStandingService;
        this.leagueStandingRepository = leagueStandingRepository;
        this.leagueStandingQueryService = leagueStandingQueryService;
    }

    /**
     * {@code POST  /league-standings} : Create a new leagueStanding.
     *
     * @param leagueStandingDTO the leagueStandingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leagueStandingDTO, or with status {@code 400 (Bad Request)} if the leagueStanding has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/league-standings")
    public ResponseEntity<LeagueStandingDTO> createLeagueStanding(@Valid @RequestBody LeagueStandingDTO leagueStandingDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeagueStanding : {}", leagueStandingDTO);
        if (leagueStandingDTO.getId() != null) {
            throw new BadRequestAlertException("A new leagueStanding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeagueStandingDTO result = leagueStandingService.save(leagueStandingDTO);
        return ResponseEntity
            .created(new URI("/api/league-standings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /league-standings/:id} : Updates an existing leagueStanding.
     *
     * @param id the id of the leagueStandingDTO to save.
     * @param leagueStandingDTO the leagueStandingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leagueStandingDTO,
     * or with status {@code 400 (Bad Request)} if the leagueStandingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leagueStandingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/league-standings/{id}")
    public ResponseEntity<LeagueStandingDTO> updateLeagueStanding(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeagueStandingDTO leagueStandingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeagueStanding : {}, {}", id, leagueStandingDTO);
        if (leagueStandingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leagueStandingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leagueStandingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeagueStandingDTO result = leagueStandingService.save(leagueStandingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leagueStandingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /league-standings/:id} : Partial updates given fields of an existing leagueStanding, field will ignore if it is null
     *
     * @param id the id of the leagueStandingDTO to save.
     * @param leagueStandingDTO the leagueStandingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leagueStandingDTO,
     * or with status {@code 400 (Bad Request)} if the leagueStandingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leagueStandingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leagueStandingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/league-standings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LeagueStandingDTO> partialUpdateLeagueStanding(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeagueStandingDTO leagueStandingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeagueStanding partially : {}, {}", id, leagueStandingDTO);
        if (leagueStandingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leagueStandingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leagueStandingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeagueStandingDTO> result = leagueStandingService.partialUpdate(leagueStandingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leagueStandingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /league-standings} : get all the leagueStandings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leagueStandings in body.
     */
    @GetMapping("/league-standings")
    public ResponseEntity<List<LeagueStandingDTO>> getAllLeagueStandings(LeagueStandingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeagueStandings by criteria: {}", criteria);
        Page<LeagueStandingDTO> page = leagueStandingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /league-standings/count} : count all the leagueStandings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/league-standings/count")
    public ResponseEntity<Long> countLeagueStandings(LeagueStandingCriteria criteria) {
        log.debug("REST request to count LeagueStandings by criteria: {}", criteria);
        return ResponseEntity.ok().body(leagueStandingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /league-standings/:id} : get the "id" leagueStanding.
     *
     * @param id the id of the leagueStandingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leagueStandingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/league-standings/{id}")
    public ResponseEntity<LeagueStandingDTO> getLeagueStanding(@PathVariable Long id) {
        log.debug("REST request to get LeagueStanding : {}", id);
        Optional<LeagueStandingDTO> leagueStandingDTO = leagueStandingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leagueStandingDTO);
    }

    /**
     * {@code DELETE  /league-standings/:id} : delete the "id" leagueStanding.
     *
     * @param id the id of the leagueStandingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/league-standings/{id}")
    public ResponseEntity<Void> deleteLeagueStanding(@PathVariable Long id) {
        log.debug("REST request to delete LeagueStanding : {}", id);
        leagueStandingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
