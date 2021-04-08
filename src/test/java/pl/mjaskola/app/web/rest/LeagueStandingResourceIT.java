package pl.mjaskola.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.IntegrationTest;
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.domain.Team;
import pl.mjaskola.app.repository.LeagueStandingRepository;
import pl.mjaskola.app.service.criteria.LeagueStandingCriteria;
import pl.mjaskola.app.service.dto.LeagueStandingDTO;
import pl.mjaskola.app.service.mapper.LeagueStandingMapper;

/**
 * Integration tests for the {@link LeagueStandingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeagueStandingResourceIT {

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final Integer DEFAULT_SCORED_GOALS = 0;
    private static final Integer UPDATED_SCORED_GOALS = 1;
    private static final Integer SMALLER_SCORED_GOALS = 0 - 1;

    private static final Integer DEFAULT_LOST_GOALS = 0;
    private static final Integer UPDATED_LOST_GOALS = 1;
    private static final Integer SMALLER_LOST_GOALS = 0 - 1;

    private static final Integer DEFAULT_WINS = 0;
    private static final Integer UPDATED_WINS = 1;
    private static final Integer SMALLER_WINS = 0 - 1;

    private static final Integer DEFAULT_DRAWS = 0;
    private static final Integer UPDATED_DRAWS = 1;
    private static final Integer SMALLER_DRAWS = 0 - 1;

    private static final Integer DEFAULT_LOSSES = 0;
    private static final Integer UPDATED_LOSSES = 1;
    private static final Integer SMALLER_LOSSES = 0 - 1;

    private static final String ENTITY_API_URL = "/api/league-standings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeagueStandingRepository leagueStandingRepository;

    @Autowired
    private LeagueStandingMapper leagueStandingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeagueStandingMockMvc;

    private LeagueStanding leagueStanding;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeagueStanding createEntity(EntityManager em) {
        LeagueStanding leagueStanding = new LeagueStanding()
            .position(DEFAULT_POSITION)
            .points(DEFAULT_POINTS)
            .scoredGoals(DEFAULT_SCORED_GOALS)
            .lostGoals(DEFAULT_LOST_GOALS)
            .wins(DEFAULT_WINS)
            .draws(DEFAULT_DRAWS)
            .losses(DEFAULT_LOSSES);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        leagueStanding.setTeam(team);
        return leagueStanding;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeagueStanding createUpdatedEntity(EntityManager em) {
        LeagueStanding leagueStanding = new LeagueStanding()
            .position(UPDATED_POSITION)
            .points(UPDATED_POINTS)
            .scoredGoals(UPDATED_SCORED_GOALS)
            .lostGoals(UPDATED_LOST_GOALS)
            .wins(UPDATED_WINS)
            .draws(UPDATED_DRAWS)
            .losses(UPDATED_LOSSES);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        leagueStanding.setTeam(team);
        return leagueStanding;
    }

    @BeforeEach
    public void initTest() {
        leagueStanding = createEntity(em);
    }

    @Test
    @Transactional
    void createLeagueStanding() throws Exception {
        int databaseSizeBeforeCreate = leagueStandingRepository.findAll().size();
        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);
        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeCreate + 1);
        LeagueStanding testLeagueStanding = leagueStandingList.get(leagueStandingList.size() - 1);
        assertThat(testLeagueStanding.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testLeagueStanding.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testLeagueStanding.getScoredGoals()).isEqualTo(DEFAULT_SCORED_GOALS);
        assertThat(testLeagueStanding.getLostGoals()).isEqualTo(DEFAULT_LOST_GOALS);
        assertThat(testLeagueStanding.getWins()).isEqualTo(DEFAULT_WINS);
        assertThat(testLeagueStanding.getDraws()).isEqualTo(DEFAULT_DRAWS);
        assertThat(testLeagueStanding.getLosses()).isEqualTo(DEFAULT_LOSSES);
    }

    @Test
    @Transactional
    void createLeagueStandingWithExistingId() throws Exception {
        // Create the LeagueStanding with an existing ID
        leagueStanding.setId(1L);
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        int databaseSizeBeforeCreate = leagueStandingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setPosition(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setPoints(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScoredGoalsIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setScoredGoals(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLostGoalsIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setLostGoals(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWinsIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setWins(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDrawsIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setDraws(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLossesIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueStandingRepository.findAll().size();
        // set the field null
        leagueStanding.setLosses(null);

        // Create the LeagueStanding, which fails.
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        restLeagueStandingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeagueStandings() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList
        restLeagueStandingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leagueStanding.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].scoredGoals").value(hasItem(DEFAULT_SCORED_GOALS)))
            .andExpect(jsonPath("$.[*].lostGoals").value(hasItem(DEFAULT_LOST_GOALS)))
            .andExpect(jsonPath("$.[*].wins").value(hasItem(DEFAULT_WINS)))
            .andExpect(jsonPath("$.[*].draws").value(hasItem(DEFAULT_DRAWS)))
            .andExpect(jsonPath("$.[*].losses").value(hasItem(DEFAULT_LOSSES)));
    }

    @Test
    @Transactional
    void getLeagueStanding() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get the leagueStanding
        restLeagueStandingMockMvc
            .perform(get(ENTITY_API_URL_ID, leagueStanding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leagueStanding.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.scoredGoals").value(DEFAULT_SCORED_GOALS))
            .andExpect(jsonPath("$.lostGoals").value(DEFAULT_LOST_GOALS))
            .andExpect(jsonPath("$.wins").value(DEFAULT_WINS))
            .andExpect(jsonPath("$.draws").value(DEFAULT_DRAWS))
            .andExpect(jsonPath("$.losses").value(DEFAULT_LOSSES));
    }

    @Test
    @Transactional
    void getLeagueStandingsByIdFiltering() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        Long id = leagueStanding.getId();

        defaultLeagueStandingShouldBeFound("id.equals=" + id);
        defaultLeagueStandingShouldNotBeFound("id.notEquals=" + id);

        defaultLeagueStandingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeagueStandingShouldNotBeFound("id.greaterThan=" + id);

        defaultLeagueStandingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeagueStandingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position equals to DEFAULT_POSITION
        defaultLeagueStandingShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the leagueStandingList where position equals to UPDATED_POSITION
        defaultLeagueStandingShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position not equals to DEFAULT_POSITION
        defaultLeagueStandingShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the leagueStandingList where position not equals to UPDATED_POSITION
        defaultLeagueStandingShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultLeagueStandingShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the leagueStandingList where position equals to UPDATED_POSITION
        defaultLeagueStandingShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position is not null
        defaultLeagueStandingShouldBeFound("position.specified=true");

        // Get all the leagueStandingList where position is null
        defaultLeagueStandingShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position is greater than or equal to DEFAULT_POSITION
        defaultLeagueStandingShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the leagueStandingList where position is greater than or equal to UPDATED_POSITION
        defaultLeagueStandingShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position is less than or equal to DEFAULT_POSITION
        defaultLeagueStandingShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the leagueStandingList where position is less than or equal to SMALLER_POSITION
        defaultLeagueStandingShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position is less than DEFAULT_POSITION
        defaultLeagueStandingShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the leagueStandingList where position is less than UPDATED_POSITION
        defaultLeagueStandingShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where position is greater than DEFAULT_POSITION
        defaultLeagueStandingShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the leagueStandingList where position is greater than SMALLER_POSITION
        defaultLeagueStandingShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points equals to DEFAULT_POINTS
        defaultLeagueStandingShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the leagueStandingList where points equals to UPDATED_POINTS
        defaultLeagueStandingShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points not equals to DEFAULT_POINTS
        defaultLeagueStandingShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the leagueStandingList where points not equals to UPDATED_POINTS
        defaultLeagueStandingShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultLeagueStandingShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the leagueStandingList where points equals to UPDATED_POINTS
        defaultLeagueStandingShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points is not null
        defaultLeagueStandingShouldBeFound("points.specified=true");

        // Get all the leagueStandingList where points is null
        defaultLeagueStandingShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points is greater than or equal to DEFAULT_POINTS
        defaultLeagueStandingShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the leagueStandingList where points is greater than or equal to UPDATED_POINTS
        defaultLeagueStandingShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points is less than or equal to DEFAULT_POINTS
        defaultLeagueStandingShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the leagueStandingList where points is less than or equal to SMALLER_POINTS
        defaultLeagueStandingShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points is less than DEFAULT_POINTS
        defaultLeagueStandingShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the leagueStandingList where points is less than UPDATED_POINTS
        defaultLeagueStandingShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where points is greater than DEFAULT_POINTS
        defaultLeagueStandingShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the leagueStandingList where points is greater than SMALLER_POINTS
        defaultLeagueStandingShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals equals to DEFAULT_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.equals=" + DEFAULT_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals equals to UPDATED_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.equals=" + UPDATED_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals not equals to DEFAULT_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.notEquals=" + DEFAULT_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals not equals to UPDATED_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.notEquals=" + UPDATED_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals in DEFAULT_SCORED_GOALS or UPDATED_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.in=" + DEFAULT_SCORED_GOALS + "," + UPDATED_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals equals to UPDATED_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.in=" + UPDATED_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals is not null
        defaultLeagueStandingShouldBeFound("scoredGoals.specified=true");

        // Get all the leagueStandingList where scoredGoals is null
        defaultLeagueStandingShouldNotBeFound("scoredGoals.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals is greater than or equal to DEFAULT_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.greaterThanOrEqual=" + DEFAULT_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals is greater than or equal to UPDATED_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.greaterThanOrEqual=" + UPDATED_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals is less than or equal to DEFAULT_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.lessThanOrEqual=" + DEFAULT_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals is less than or equal to SMALLER_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.lessThanOrEqual=" + SMALLER_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals is less than DEFAULT_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.lessThan=" + DEFAULT_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals is less than UPDATED_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.lessThan=" + UPDATED_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByScoredGoalsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where scoredGoals is greater than DEFAULT_SCORED_GOALS
        defaultLeagueStandingShouldNotBeFound("scoredGoals.greaterThan=" + DEFAULT_SCORED_GOALS);

        // Get all the leagueStandingList where scoredGoals is greater than SMALLER_SCORED_GOALS
        defaultLeagueStandingShouldBeFound("scoredGoals.greaterThan=" + SMALLER_SCORED_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals equals to DEFAULT_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.equals=" + DEFAULT_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals equals to UPDATED_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.equals=" + UPDATED_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals not equals to DEFAULT_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.notEquals=" + DEFAULT_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals not equals to UPDATED_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.notEquals=" + UPDATED_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals in DEFAULT_LOST_GOALS or UPDATED_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.in=" + DEFAULT_LOST_GOALS + "," + UPDATED_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals equals to UPDATED_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.in=" + UPDATED_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals is not null
        defaultLeagueStandingShouldBeFound("lostGoals.specified=true");

        // Get all the leagueStandingList where lostGoals is null
        defaultLeagueStandingShouldNotBeFound("lostGoals.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals is greater than or equal to DEFAULT_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.greaterThanOrEqual=" + DEFAULT_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals is greater than or equal to UPDATED_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.greaterThanOrEqual=" + UPDATED_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals is less than or equal to DEFAULT_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.lessThanOrEqual=" + DEFAULT_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals is less than or equal to SMALLER_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.lessThanOrEqual=" + SMALLER_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals is less than DEFAULT_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.lessThan=" + DEFAULT_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals is less than UPDATED_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.lessThan=" + UPDATED_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLostGoalsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where lostGoals is greater than DEFAULT_LOST_GOALS
        defaultLeagueStandingShouldNotBeFound("lostGoals.greaterThan=" + DEFAULT_LOST_GOALS);

        // Get all the leagueStandingList where lostGoals is greater than SMALLER_LOST_GOALS
        defaultLeagueStandingShouldBeFound("lostGoals.greaterThan=" + SMALLER_LOST_GOALS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins equals to DEFAULT_WINS
        defaultLeagueStandingShouldBeFound("wins.equals=" + DEFAULT_WINS);

        // Get all the leagueStandingList where wins equals to UPDATED_WINS
        defaultLeagueStandingShouldNotBeFound("wins.equals=" + UPDATED_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins not equals to DEFAULT_WINS
        defaultLeagueStandingShouldNotBeFound("wins.notEquals=" + DEFAULT_WINS);

        // Get all the leagueStandingList where wins not equals to UPDATED_WINS
        defaultLeagueStandingShouldBeFound("wins.notEquals=" + UPDATED_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins in DEFAULT_WINS or UPDATED_WINS
        defaultLeagueStandingShouldBeFound("wins.in=" + DEFAULT_WINS + "," + UPDATED_WINS);

        // Get all the leagueStandingList where wins equals to UPDATED_WINS
        defaultLeagueStandingShouldNotBeFound("wins.in=" + UPDATED_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins is not null
        defaultLeagueStandingShouldBeFound("wins.specified=true");

        // Get all the leagueStandingList where wins is null
        defaultLeagueStandingShouldNotBeFound("wins.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins is greater than or equal to DEFAULT_WINS
        defaultLeagueStandingShouldBeFound("wins.greaterThanOrEqual=" + DEFAULT_WINS);

        // Get all the leagueStandingList where wins is greater than or equal to UPDATED_WINS
        defaultLeagueStandingShouldNotBeFound("wins.greaterThanOrEqual=" + UPDATED_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins is less than or equal to DEFAULT_WINS
        defaultLeagueStandingShouldBeFound("wins.lessThanOrEqual=" + DEFAULT_WINS);

        // Get all the leagueStandingList where wins is less than or equal to SMALLER_WINS
        defaultLeagueStandingShouldNotBeFound("wins.lessThanOrEqual=" + SMALLER_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins is less than DEFAULT_WINS
        defaultLeagueStandingShouldNotBeFound("wins.lessThan=" + DEFAULT_WINS);

        // Get all the leagueStandingList where wins is less than UPDATED_WINS
        defaultLeagueStandingShouldBeFound("wins.lessThan=" + UPDATED_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByWinsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where wins is greater than DEFAULT_WINS
        defaultLeagueStandingShouldNotBeFound("wins.greaterThan=" + DEFAULT_WINS);

        // Get all the leagueStandingList where wins is greater than SMALLER_WINS
        defaultLeagueStandingShouldBeFound("wins.greaterThan=" + SMALLER_WINS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws equals to DEFAULT_DRAWS
        defaultLeagueStandingShouldBeFound("draws.equals=" + DEFAULT_DRAWS);

        // Get all the leagueStandingList where draws equals to UPDATED_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.equals=" + UPDATED_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws not equals to DEFAULT_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.notEquals=" + DEFAULT_DRAWS);

        // Get all the leagueStandingList where draws not equals to UPDATED_DRAWS
        defaultLeagueStandingShouldBeFound("draws.notEquals=" + UPDATED_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws in DEFAULT_DRAWS or UPDATED_DRAWS
        defaultLeagueStandingShouldBeFound("draws.in=" + DEFAULT_DRAWS + "," + UPDATED_DRAWS);

        // Get all the leagueStandingList where draws equals to UPDATED_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.in=" + UPDATED_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws is not null
        defaultLeagueStandingShouldBeFound("draws.specified=true");

        // Get all the leagueStandingList where draws is null
        defaultLeagueStandingShouldNotBeFound("draws.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws is greater than or equal to DEFAULT_DRAWS
        defaultLeagueStandingShouldBeFound("draws.greaterThanOrEqual=" + DEFAULT_DRAWS);

        // Get all the leagueStandingList where draws is greater than or equal to UPDATED_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.greaterThanOrEqual=" + UPDATED_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws is less than or equal to DEFAULT_DRAWS
        defaultLeagueStandingShouldBeFound("draws.lessThanOrEqual=" + DEFAULT_DRAWS);

        // Get all the leagueStandingList where draws is less than or equal to SMALLER_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.lessThanOrEqual=" + SMALLER_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws is less than DEFAULT_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.lessThan=" + DEFAULT_DRAWS);

        // Get all the leagueStandingList where draws is less than UPDATED_DRAWS
        defaultLeagueStandingShouldBeFound("draws.lessThan=" + UPDATED_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByDrawsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where draws is greater than DEFAULT_DRAWS
        defaultLeagueStandingShouldNotBeFound("draws.greaterThan=" + DEFAULT_DRAWS);

        // Get all the leagueStandingList where draws is greater than SMALLER_DRAWS
        defaultLeagueStandingShouldBeFound("draws.greaterThan=" + SMALLER_DRAWS);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses equals to DEFAULT_LOSSES
        defaultLeagueStandingShouldBeFound("losses.equals=" + DEFAULT_LOSSES);

        // Get all the leagueStandingList where losses equals to UPDATED_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.equals=" + UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses not equals to DEFAULT_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.notEquals=" + DEFAULT_LOSSES);

        // Get all the leagueStandingList where losses not equals to UPDATED_LOSSES
        defaultLeagueStandingShouldBeFound("losses.notEquals=" + UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsInShouldWork() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses in DEFAULT_LOSSES or UPDATED_LOSSES
        defaultLeagueStandingShouldBeFound("losses.in=" + DEFAULT_LOSSES + "," + UPDATED_LOSSES);

        // Get all the leagueStandingList where losses equals to UPDATED_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.in=" + UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses is not null
        defaultLeagueStandingShouldBeFound("losses.specified=true");

        // Get all the leagueStandingList where losses is null
        defaultLeagueStandingShouldNotBeFound("losses.specified=false");
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses is greater than or equal to DEFAULT_LOSSES
        defaultLeagueStandingShouldBeFound("losses.greaterThanOrEqual=" + DEFAULT_LOSSES);

        // Get all the leagueStandingList where losses is greater than or equal to UPDATED_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.greaterThanOrEqual=" + UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses is less than or equal to DEFAULT_LOSSES
        defaultLeagueStandingShouldBeFound("losses.lessThanOrEqual=" + DEFAULT_LOSSES);

        // Get all the leagueStandingList where losses is less than or equal to SMALLER_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.lessThanOrEqual=" + SMALLER_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsLessThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses is less than DEFAULT_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.lessThan=" + DEFAULT_LOSSES);

        // Get all the leagueStandingList where losses is less than UPDATED_LOSSES
        defaultLeagueStandingShouldBeFound("losses.lessThan=" + UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLossesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        // Get all the leagueStandingList where losses is greater than DEFAULT_LOSSES
        defaultLeagueStandingShouldNotBeFound("losses.greaterThan=" + DEFAULT_LOSSES);

        // Get all the leagueStandingList where losses is greater than SMALLER_LOSSES
        defaultLeagueStandingShouldBeFound("losses.greaterThan=" + SMALLER_LOSSES);
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);
        Team team = TeamResourceIT.createEntity(em);
        em.persist(team);
        em.flush();
        leagueStanding.setTeam(team);
        leagueStandingRepository.saveAndFlush(leagueStanding);
        Long teamId = team.getId();

        // Get all the leagueStandingList where team equals to teamId
        defaultLeagueStandingShouldBeFound("teamId.equals=" + teamId);

        // Get all the leagueStandingList where team equals to (teamId + 1)
        defaultLeagueStandingShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    @Test
    @Transactional
    void getAllLeagueStandingsByLeagueIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);
        League league = LeagueResourceIT.createEntity(em);
        em.persist(league);
        em.flush();
        leagueStanding.setLeague(league);
        leagueStandingRepository.saveAndFlush(leagueStanding);
        Long leagueId = league.getId();

        // Get all the leagueStandingList where league equals to leagueId
        defaultLeagueStandingShouldBeFound("leagueId.equals=" + leagueId);

        // Get all the leagueStandingList where league equals to (leagueId + 1)
        defaultLeagueStandingShouldNotBeFound("leagueId.equals=" + (leagueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeagueStandingShouldBeFound(String filter) throws Exception {
        restLeagueStandingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leagueStanding.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].scoredGoals").value(hasItem(DEFAULT_SCORED_GOALS)))
            .andExpect(jsonPath("$.[*].lostGoals").value(hasItem(DEFAULT_LOST_GOALS)))
            .andExpect(jsonPath("$.[*].wins").value(hasItem(DEFAULT_WINS)))
            .andExpect(jsonPath("$.[*].draws").value(hasItem(DEFAULT_DRAWS)))
            .andExpect(jsonPath("$.[*].losses").value(hasItem(DEFAULT_LOSSES)));

        // Check, that the count call also returns 1
        restLeagueStandingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeagueStandingShouldNotBeFound(String filter) throws Exception {
        restLeagueStandingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeagueStandingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeagueStanding() throws Exception {
        // Get the leagueStanding
        restLeagueStandingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeagueStanding() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();

        // Update the leagueStanding
        LeagueStanding updatedLeagueStanding = leagueStandingRepository.findById(leagueStanding.getId()).get();
        // Disconnect from session so that the updates on updatedLeagueStanding are not directly saved in db
        em.detach(updatedLeagueStanding);
        updatedLeagueStanding
            .position(UPDATED_POSITION)
            .points(UPDATED_POINTS)
            .scoredGoals(UPDATED_SCORED_GOALS)
            .lostGoals(UPDATED_LOST_GOALS)
            .wins(UPDATED_WINS)
            .draws(UPDATED_DRAWS)
            .losses(UPDATED_LOSSES);
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(updatedLeagueStanding);

        restLeagueStandingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leagueStandingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
        LeagueStanding testLeagueStanding = leagueStandingList.get(leagueStandingList.size() - 1);
        assertThat(testLeagueStanding.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testLeagueStanding.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testLeagueStanding.getScoredGoals()).isEqualTo(UPDATED_SCORED_GOALS);
        assertThat(testLeagueStanding.getLostGoals()).isEqualTo(UPDATED_LOST_GOALS);
        assertThat(testLeagueStanding.getWins()).isEqualTo(UPDATED_WINS);
        assertThat(testLeagueStanding.getDraws()).isEqualTo(UPDATED_DRAWS);
        assertThat(testLeagueStanding.getLosses()).isEqualTo(UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void putNonExistingLeagueStanding() throws Exception {
        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();
        leagueStanding.setId(count.incrementAndGet());

        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeagueStandingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leagueStandingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeagueStanding() throws Exception {
        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();
        leagueStanding.setId(count.incrementAndGet());

        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueStandingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeagueStanding() throws Exception {
        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();
        leagueStanding.setId(count.incrementAndGet());

        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueStandingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeagueStandingWithPatch() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();

        // Update the leagueStanding using partial update
        LeagueStanding partialUpdatedLeagueStanding = new LeagueStanding();
        partialUpdatedLeagueStanding.setId(leagueStanding.getId());

        partialUpdatedLeagueStanding
            .points(UPDATED_POINTS)
            .scoredGoals(UPDATED_SCORED_GOALS)
            .lostGoals(UPDATED_LOST_GOALS)
            .wins(UPDATED_WINS)
            .draws(UPDATED_DRAWS)
            .losses(UPDATED_LOSSES);

        restLeagueStandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeagueStanding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeagueStanding))
            )
            .andExpect(status().isOk());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
        LeagueStanding testLeagueStanding = leagueStandingList.get(leagueStandingList.size() - 1);
        assertThat(testLeagueStanding.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testLeagueStanding.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testLeagueStanding.getScoredGoals()).isEqualTo(UPDATED_SCORED_GOALS);
        assertThat(testLeagueStanding.getLostGoals()).isEqualTo(UPDATED_LOST_GOALS);
        assertThat(testLeagueStanding.getWins()).isEqualTo(UPDATED_WINS);
        assertThat(testLeagueStanding.getDraws()).isEqualTo(UPDATED_DRAWS);
        assertThat(testLeagueStanding.getLosses()).isEqualTo(UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void fullUpdateLeagueStandingWithPatch() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();

        // Update the leagueStanding using partial update
        LeagueStanding partialUpdatedLeagueStanding = new LeagueStanding();
        partialUpdatedLeagueStanding.setId(leagueStanding.getId());

        partialUpdatedLeagueStanding
            .position(UPDATED_POSITION)
            .points(UPDATED_POINTS)
            .scoredGoals(UPDATED_SCORED_GOALS)
            .lostGoals(UPDATED_LOST_GOALS)
            .wins(UPDATED_WINS)
            .draws(UPDATED_DRAWS)
            .losses(UPDATED_LOSSES);

        restLeagueStandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeagueStanding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeagueStanding))
            )
            .andExpect(status().isOk());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
        LeagueStanding testLeagueStanding = leagueStandingList.get(leagueStandingList.size() - 1);
        assertThat(testLeagueStanding.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testLeagueStanding.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testLeagueStanding.getScoredGoals()).isEqualTo(UPDATED_SCORED_GOALS);
        assertThat(testLeagueStanding.getLostGoals()).isEqualTo(UPDATED_LOST_GOALS);
        assertThat(testLeagueStanding.getWins()).isEqualTo(UPDATED_WINS);
        assertThat(testLeagueStanding.getDraws()).isEqualTo(UPDATED_DRAWS);
        assertThat(testLeagueStanding.getLosses()).isEqualTo(UPDATED_LOSSES);
    }

    @Test
    @Transactional
    void patchNonExistingLeagueStanding() throws Exception {
        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();
        leagueStanding.setId(count.incrementAndGet());

        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeagueStandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leagueStandingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeagueStanding() throws Exception {
        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();
        leagueStanding.setId(count.incrementAndGet());

        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueStandingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeagueStanding() throws Exception {
        int databaseSizeBeforeUpdate = leagueStandingRepository.findAll().size();
        leagueStanding.setId(count.incrementAndGet());

        // Create the LeagueStanding
        LeagueStandingDTO leagueStandingDTO = leagueStandingMapper.toDto(leagueStanding);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueStandingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueStandingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeagueStanding in the database
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeagueStanding() throws Exception {
        // Initialize the database
        leagueStandingRepository.saveAndFlush(leagueStanding);

        int databaseSizeBeforeDelete = leagueStandingRepository.findAll().size();

        // Delete the leagueStanding
        restLeagueStandingMockMvc
            .perform(delete(ENTITY_API_URL_ID, leagueStanding.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeagueStanding> leagueStandingList = leagueStandingRepository.findAll();
        assertThat(leagueStandingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
