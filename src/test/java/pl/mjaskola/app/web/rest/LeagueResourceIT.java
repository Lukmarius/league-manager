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
import pl.mjaskola.app.repository.LeagueRepository;
import pl.mjaskola.app.service.criteria.LeagueCriteria;
import pl.mjaskola.app.service.dto.LeagueDTO;
import pl.mjaskola.app.service.mapper.LeagueMapper;

/**
 * Integration tests for the {@link LeagueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeagueResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leagues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private LeagueMapper leagueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeagueMockMvc;

    private League league;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static League createEntity(EntityManager em) {
        League league = new League().name(DEFAULT_NAME);
        return league;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static League createUpdatedEntity(EntityManager em) {
        League league = new League().name(UPDATED_NAME);
        return league;
    }

    @BeforeEach
    public void initTest() {
        league = createEntity(em);
    }

    @Test
    @Transactional
    void createLeague() throws Exception {
        int databaseSizeBeforeCreate = leagueRepository.findAll().size();
        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);
        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isCreated());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeCreate + 1);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createLeagueWithExistingId() throws Exception {
        // Create the League with an existing ID
        league.setId(1L);
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        int databaseSizeBeforeCreate = leagueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueRepository.findAll().size();
        // set the field null
        league.setName(null);

        // Create the League, which fails.
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isBadRequest());

        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeagues() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(league.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get the league
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL_ID, league.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(league.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getLeaguesByIdFiltering() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        Long id = league.getId();

        defaultLeagueShouldBeFound("id.equals=" + id);
        defaultLeagueShouldNotBeFound("id.notEquals=" + id);

        defaultLeagueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeagueShouldNotBeFound("id.greaterThan=" + id);

        defaultLeagueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeagueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaguesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList where name equals to DEFAULT_NAME
        defaultLeagueShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the leagueList where name equals to UPDATED_NAME
        defaultLeagueShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeaguesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList where name not equals to DEFAULT_NAME
        defaultLeagueShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the leagueList where name not equals to UPDATED_NAME
        defaultLeagueShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeaguesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLeagueShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the leagueList where name equals to UPDATED_NAME
        defaultLeagueShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeaguesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList where name is not null
        defaultLeagueShouldBeFound("name.specified=true");

        // Get all the leagueList where name is null
        defaultLeagueShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaguesByNameContainsSomething() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList where name contains DEFAULT_NAME
        defaultLeagueShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the leagueList where name contains UPDATED_NAME
        defaultLeagueShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLeaguesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList where name does not contain DEFAULT_NAME
        defaultLeagueShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the leagueList where name does not contain UPDATED_NAME
        defaultLeagueShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeagueShouldBeFound(String filter) throws Exception {
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(league.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeagueShouldNotBeFound(String filter) throws Exception {
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeague() throws Exception {
        // Get the league
        restLeagueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league
        League updatedLeague = leagueRepository.findById(league.getId()).get();
        // Disconnect from session so that the updates on updatedLeague are not directly saved in db
        em.detach(updatedLeague);
        updatedLeague.name(UPDATED_NAME);
        LeagueDTO leagueDTO = leagueMapper.toDto(updatedLeague);

        restLeagueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leagueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leagueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeagueWithPatch() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league using partial update
        League partialUpdatedLeague = new League();
        partialUpdatedLeague.setId(league.getId());

        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeague.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeague))
            )
            .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLeagueWithPatch() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league using partial update
        League partialUpdatedLeague = new League();
        partialUpdatedLeague.setId(league.getId());

        partialUpdatedLeague.name(UPDATED_NAME);

        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeague.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeague))
            )
            .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leagueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeDelete = leagueRepository.findAll().size();

        // Delete the league
        restLeagueMockMvc
            .perform(delete(ENTITY_API_URL_ID, league.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
