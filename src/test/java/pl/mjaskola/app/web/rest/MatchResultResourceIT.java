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
import pl.mjaskola.app.domain.MatchResult;
import pl.mjaskola.app.repository.MatchResultRepository;
import pl.mjaskola.app.service.dto.MatchResultDTO;
import pl.mjaskola.app.service.mapper.MatchResultMapper;

/**
 * Integration tests for the {@link MatchResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatchResultResourceIT {

    private static final Integer DEFAULT_HOME_TEAM_SCORE = 0;
    private static final Integer UPDATED_HOME_TEAM_SCORE = 1;

    private static final Integer DEFAULT_AWAY_TEAM_SCORE = 0;
    private static final Integer UPDATED_AWAY_TEAM_SCORE = 1;

    private static final String ENTITY_API_URL = "/api/match-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatchResultRepository matchResultRepository;

    @Autowired
    private MatchResultMapper matchResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatchResultMockMvc;

    private MatchResult matchResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchResult createEntity(EntityManager em) {
        MatchResult matchResult = new MatchResult().homeTeamScore(DEFAULT_HOME_TEAM_SCORE).awayTeamScore(DEFAULT_AWAY_TEAM_SCORE);
        return matchResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchResult createUpdatedEntity(EntityManager em) {
        MatchResult matchResult = new MatchResult().homeTeamScore(UPDATED_HOME_TEAM_SCORE).awayTeamScore(UPDATED_AWAY_TEAM_SCORE);
        return matchResult;
    }

    @BeforeEach
    public void initTest() {
        matchResult = createEntity(em);
    }

    @Test
    @Transactional
    void createMatchResult() throws Exception {
        int databaseSizeBeforeCreate = matchResultRepository.findAll().size();
        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);
        restMatchResultMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeCreate + 1);
        MatchResult testMatchResult = matchResultList.get(matchResultList.size() - 1);
        assertThat(testMatchResult.getHomeTeamScore()).isEqualTo(DEFAULT_HOME_TEAM_SCORE);
        assertThat(testMatchResult.getAwayTeamScore()).isEqualTo(DEFAULT_AWAY_TEAM_SCORE);
    }

    @Test
    @Transactional
    void createMatchResultWithExistingId() throws Exception {
        // Create the MatchResult with an existing ID
        matchResult.setId(1L);
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        int databaseSizeBeforeCreate = matchResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchResultMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHomeTeamScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = matchResultRepository.findAll().size();
        // set the field null
        matchResult.setHomeTeamScore(null);

        // Create the MatchResult, which fails.
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        restMatchResultMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAwayTeamScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = matchResultRepository.findAll().size();
        // set the field null
        matchResult.setAwayTeamScore(null);

        // Create the MatchResult, which fails.
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        restMatchResultMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatchResults() throws Exception {
        // Initialize the database
        matchResultRepository.saveAndFlush(matchResult);

        // Get all the matchResultList
        restMatchResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matchResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].homeTeamScore").value(hasItem(DEFAULT_HOME_TEAM_SCORE)))
            .andExpect(jsonPath("$.[*].awayTeamScore").value(hasItem(DEFAULT_AWAY_TEAM_SCORE)));
    }

    @Test
    @Transactional
    void getMatchResult() throws Exception {
        // Initialize the database
        matchResultRepository.saveAndFlush(matchResult);

        // Get the matchResult
        restMatchResultMockMvc
            .perform(get(ENTITY_API_URL_ID, matchResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matchResult.getId().intValue()))
            .andExpect(jsonPath("$.homeTeamScore").value(DEFAULT_HOME_TEAM_SCORE))
            .andExpect(jsonPath("$.awayTeamScore").value(DEFAULT_AWAY_TEAM_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingMatchResult() throws Exception {
        // Get the matchResult
        restMatchResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatchResult() throws Exception {
        // Initialize the database
        matchResultRepository.saveAndFlush(matchResult);

        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();

        // Update the matchResult
        MatchResult updatedMatchResult = matchResultRepository.findById(matchResult.getId()).get();
        // Disconnect from session so that the updates on updatedMatchResult are not directly saved in db
        em.detach(updatedMatchResult);
        updatedMatchResult.homeTeamScore(UPDATED_HOME_TEAM_SCORE).awayTeamScore(UPDATED_AWAY_TEAM_SCORE);
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(updatedMatchResult);

        restMatchResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
        MatchResult testMatchResult = matchResultList.get(matchResultList.size() - 1);
        assertThat(testMatchResult.getHomeTeamScore()).isEqualTo(UPDATED_HOME_TEAM_SCORE);
        assertThat(testMatchResult.getAwayTeamScore()).isEqualTo(UPDATED_AWAY_TEAM_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingMatchResult() throws Exception {
        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();
        matchResult.setId(count.incrementAndGet());

        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatchResult() throws Exception {
        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();
        matchResult.setId(count.incrementAndGet());

        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatchResult() throws Exception {
        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();
        matchResult.setId(count.incrementAndGet());

        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matchResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatchResultWithPatch() throws Exception {
        // Initialize the database
        matchResultRepository.saveAndFlush(matchResult);

        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();

        // Update the matchResult using partial update
        MatchResult partialUpdatedMatchResult = new MatchResult();
        partialUpdatedMatchResult.setId(matchResult.getId());

        partialUpdatedMatchResult.homeTeamScore(UPDATED_HOME_TEAM_SCORE);

        restMatchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchResult))
            )
            .andExpect(status().isOk());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
        MatchResult testMatchResult = matchResultList.get(matchResultList.size() - 1);
        assertThat(testMatchResult.getHomeTeamScore()).isEqualTo(UPDATED_HOME_TEAM_SCORE);
        assertThat(testMatchResult.getAwayTeamScore()).isEqualTo(DEFAULT_AWAY_TEAM_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateMatchResultWithPatch() throws Exception {
        // Initialize the database
        matchResultRepository.saveAndFlush(matchResult);

        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();

        // Update the matchResult using partial update
        MatchResult partialUpdatedMatchResult = new MatchResult();
        partialUpdatedMatchResult.setId(matchResult.getId());

        partialUpdatedMatchResult.homeTeamScore(UPDATED_HOME_TEAM_SCORE).awayTeamScore(UPDATED_AWAY_TEAM_SCORE);

        restMatchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchResult))
            )
            .andExpect(status().isOk());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
        MatchResult testMatchResult = matchResultList.get(matchResultList.size() - 1);
        assertThat(testMatchResult.getHomeTeamScore()).isEqualTo(UPDATED_HOME_TEAM_SCORE);
        assertThat(testMatchResult.getAwayTeamScore()).isEqualTo(UPDATED_AWAY_TEAM_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingMatchResult() throws Exception {
        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();
        matchResult.setId(count.incrementAndGet());

        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matchResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatchResult() throws Exception {
        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();
        matchResult.setId(count.incrementAndGet());

        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatchResult() throws Exception {
        int databaseSizeBeforeUpdate = matchResultRepository.findAll().size();
        matchResult.setId(count.incrementAndGet());

        // Create the MatchResult
        MatchResultDTO matchResultDTO = matchResultMapper.toDto(matchResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(matchResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatchResult in the database
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatchResult() throws Exception {
        // Initialize the database
        matchResultRepository.saveAndFlush(matchResult);

        int databaseSizeBeforeDelete = matchResultRepository.findAll().size();

        // Delete the matchResult
        restMatchResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, matchResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatchResult> matchResultList = matchResultRepository.findAll();
        assertThat(matchResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
