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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjaskola.app.repository.MatchResultRepository;
import pl.mjaskola.app.service.MatchResultService;
import pl.mjaskola.app.service.dto.MatchResultDTO;
import pl.mjaskola.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.mjaskola.app.domain.MatchResult}.
 */
@RestController
@RequestMapping("/api")
public class MatchResultResource {

    private final Logger log = LoggerFactory.getLogger(MatchResultResource.class);

    private static final String ENTITY_NAME = "matchResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatchResultService matchResultService;

    private final MatchResultRepository matchResultRepository;

    public MatchResultResource(MatchResultService matchResultService, MatchResultRepository matchResultRepository) {
        this.matchResultService = matchResultService;
        this.matchResultRepository = matchResultRepository;
    }

    /**
     * {@code POST  /match-results} : Create a new matchResult.
     *
     * @param matchResultDTO the matchResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matchResultDTO, or with status {@code 400 (Bad Request)} if the matchResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/match-results")
    public ResponseEntity<MatchResultDTO> createMatchResult(@Valid @RequestBody MatchResultDTO matchResultDTO) throws URISyntaxException {
        log.debug("REST request to save MatchResult : {}", matchResultDTO);
        if (matchResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new matchResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatchResultDTO result = matchResultService.save(matchResultDTO);
        return ResponseEntity
            .created(new URI("/api/match-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /match-results/:id} : Updates an existing matchResult.
     *
     * @param id the id of the matchResultDTO to save.
     * @param matchResultDTO the matchResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchResultDTO,
     * or with status {@code 400 (Bad Request)} if the matchResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matchResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/match-results/{id}")
    public ResponseEntity<MatchResultDTO> updateMatchResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatchResultDTO matchResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MatchResult : {}, {}", id, matchResultDTO);
        if (matchResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matchResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matchResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MatchResultDTO result = matchResultService.save(matchResultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matchResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /match-results/:id} : Partial updates given fields of an existing matchResult, field will ignore if it is null
     *
     * @param id the id of the matchResultDTO to save.
     * @param matchResultDTO the matchResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchResultDTO,
     * or with status {@code 400 (Bad Request)} if the matchResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matchResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matchResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/match-results/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MatchResultDTO> partialUpdateMatchResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatchResultDTO matchResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MatchResult partially : {}, {}", id, matchResultDTO);
        if (matchResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matchResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matchResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatchResultDTO> result = matchResultService.partialUpdate(matchResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matchResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /match-results} : get all the matchResults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matchResults in body.
     */
    @GetMapping("/match-results")
    public List<MatchResultDTO> getAllMatchResults() {
        log.debug("REST request to get all MatchResults");
        return matchResultService.findAll();
    }

    /**
     * {@code GET  /match-results/:id} : get the "id" matchResult.
     *
     * @param id the id of the matchResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matchResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/match-results/{id}")
    public ResponseEntity<MatchResultDTO> getMatchResult(@PathVariable Long id) {
        log.debug("REST request to get MatchResult : {}", id);
        Optional<MatchResultDTO> matchResultDTO = matchResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matchResultDTO);
    }

    /**
     * {@code DELETE  /match-results/:id} : delete the "id" matchResult.
     *
     * @param id the id of the matchResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/match-results/{id}")
    public ResponseEntity<Void> deleteMatchResult(@PathVariable Long id) {
        log.debug("REST request to delete MatchResult : {}", id);
        matchResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
