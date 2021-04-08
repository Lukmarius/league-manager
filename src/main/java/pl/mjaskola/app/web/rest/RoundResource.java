package pl.mjaskola.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import pl.mjaskola.app.repository.RoundRepository;
import pl.mjaskola.app.service.RoundQueryService;
import pl.mjaskola.app.service.RoundService;
import pl.mjaskola.app.service.criteria.RoundCriteria;
import pl.mjaskola.app.service.dto.RoundDTO;
import pl.mjaskola.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.mjaskola.app.domain.Round}.
 */
@RestController
@RequestMapping("/api")
public class RoundResource {

    private final Logger log = LoggerFactory.getLogger(RoundResource.class);

    private static final String ENTITY_NAME = "round";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoundService roundService;

    private final RoundRepository roundRepository;

    private final RoundQueryService roundQueryService;

    public RoundResource(RoundService roundService, RoundRepository roundRepository, RoundQueryService roundQueryService) {
        this.roundService = roundService;
        this.roundRepository = roundRepository;
        this.roundQueryService = roundQueryService;
    }

    /**
     * {@code POST  /rounds} : Create a new round.
     *
     * @param roundDTO the roundDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roundDTO, or with status {@code 400 (Bad Request)} if the round has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rounds")
    public ResponseEntity<RoundDTO> createRound(@RequestBody RoundDTO roundDTO) throws URISyntaxException {
        log.debug("REST request to save Round : {}", roundDTO);
        if (roundDTO.getId() != null) {
            throw new BadRequestAlertException("A new round cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoundDTO result = roundService.save(roundDTO);
        return ResponseEntity
            .created(new URI("/api/rounds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rounds/:id} : Updates an existing round.
     *
     * @param id the id of the roundDTO to save.
     * @param roundDTO the roundDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roundDTO,
     * or with status {@code 400 (Bad Request)} if the roundDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roundDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rounds/{id}")
    public ResponseEntity<RoundDTO> updateRound(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoundDTO roundDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Round : {}, {}", id, roundDTO);
        if (roundDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roundDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roundRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoundDTO result = roundService.save(roundDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roundDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rounds/:id} : Partial updates given fields of an existing round, field will ignore if it is null
     *
     * @param id the id of the roundDTO to save.
     * @param roundDTO the roundDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roundDTO,
     * or with status {@code 400 (Bad Request)} if the roundDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roundDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roundDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rounds/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RoundDTO> partialUpdateRound(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoundDTO roundDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Round partially : {}, {}", id, roundDTO);
        if (roundDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roundDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roundRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoundDTO> result = roundService.partialUpdate(roundDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roundDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rounds} : get all the rounds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rounds in body.
     */
    @GetMapping("/rounds")
    public ResponseEntity<List<RoundDTO>> getAllRounds(RoundCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rounds by criteria: {}", criteria);
        Page<RoundDTO> page = roundQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rounds/count} : count all the rounds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rounds/count")
    public ResponseEntity<Long> countRounds(RoundCriteria criteria) {
        log.debug("REST request to count Rounds by criteria: {}", criteria);
        return ResponseEntity.ok().body(roundQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rounds/:id} : get the "id" round.
     *
     * @param id the id of the roundDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roundDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rounds/{id}")
    public ResponseEntity<RoundDTO> getRound(@PathVariable Long id) {
        log.debug("REST request to get Round : {}", id);
        Optional<RoundDTO> roundDTO = roundService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roundDTO);
    }

    /**
     * {@code DELETE  /rounds/:id} : delete the "id" round.
     *
     * @param id the id of the roundDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rounds/{id}")
    public ResponseEntity<Void> deleteRound(@PathVariable Long id) {
        log.debug("REST request to delete Round : {}", id);
        roundService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
