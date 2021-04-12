package pl.mjaskola.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mjaskola.app.repository.LeagueRepository;
import pl.mjaskola.app.service.LeagueCreator;
import pl.mjaskola.app.service.LeagueQueryService;
import pl.mjaskola.app.service.dto.LeagueDTO;
import pl.mjaskola.app.web.rest.errors.BadRequestAlertException;
import pl.mjaskola.app.web.websocket.dto.LeagueCreationRequest;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link pl.mjaskola.app.domain.League}.
 */
@RestController
@RequestMapping("/api")
public class LeagueCreationResource {

    private final Logger log = LoggerFactory.getLogger(LeagueCreationResource.class);

    private static final String ENTITY_NAME = "league";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeagueCreator leagueService;

    private final LeagueRepository leagueRepository;

    private final LeagueQueryService leagueQueryService;

    public LeagueCreationResource(LeagueCreator leagueService, LeagueRepository leagueRepository, LeagueQueryService leagueQueryService) {
        this.leagueService = leagueService;
        this.leagueRepository = leagueRepository;
        this.leagueQueryService = leagueQueryService;
    }

    /**
     * {@code POST  /leagues} : Create a new league.
     *
     * @param request the leagueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leagueDTO, or with status {@code 400 (Bad Request)} if the league has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leagues/create")
    public ResponseEntity<LeagueDTO> createLeague(@Valid @RequestBody LeagueCreationRequest request) throws URISyntaxException {
        log.debug("REST request to create League : {}", request);
        if (request.getId() != null) {
            throw new BadRequestAlertException("A new league cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeagueDTO result = leagueService.create(request);
        return ResponseEntity
            .created(new URI("/api/leagues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
