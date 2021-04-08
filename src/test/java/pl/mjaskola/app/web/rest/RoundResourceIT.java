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
import pl.mjaskola.app.domain.Round;
import pl.mjaskola.app.repository.RoundRepository;
import pl.mjaskola.app.service.criteria.RoundCriteria;
import pl.mjaskola.app.service.dto.RoundDTO;
import pl.mjaskola.app.service.mapper.RoundMapper;

/**
 * Integration tests for the {@link RoundResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoundResourceIT {

    private static final Integer DEFAULT_ROUND_NUMBER = 1;
    private static final Integer UPDATED_ROUND_NUMBER = 2;
    private static final Integer SMALLER_ROUND_NUMBER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/rounds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private RoundMapper roundMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoundMockMvc;

    private Round round;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Round createEntity(EntityManager em) {
        Round round = new Round().roundNumber(DEFAULT_ROUND_NUMBER);
        return round;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Round createUpdatedEntity(EntityManager em) {
        Round round = new Round().roundNumber(UPDATED_ROUND_NUMBER);
        return round;
    }

    @BeforeEach
    public void initTest() {
        round = createEntity(em);
    }

    @Test
    @Transactional
    void createRound() throws Exception {
        int databaseSizeBeforeCreate = roundRepository.findAll().size();
        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);
        restRoundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isCreated());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeCreate + 1);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getRoundNumber()).isEqualTo(DEFAULT_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void createRoundWithExistingId() throws Exception {
        // Create the Round with an existing ID
        round.setId(1L);
        RoundDTO roundDTO = roundMapper.toDto(round);

        int databaseSizeBeforeCreate = roundRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRounds() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList
        restRoundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(round.getId().intValue())))
            .andExpect(jsonPath("$.[*].roundNumber").value(hasItem(DEFAULT_ROUND_NUMBER)));
    }

    @Test
    @Transactional
    void getRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get the round
        restRoundMockMvc
            .perform(get(ENTITY_API_URL_ID, round.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(round.getId().intValue()))
            .andExpect(jsonPath("$.roundNumber").value(DEFAULT_ROUND_NUMBER));
    }

    @Test
    @Transactional
    void getRoundsByIdFiltering() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        Long id = round.getId();

        defaultRoundShouldBeFound("id.equals=" + id);
        defaultRoundShouldNotBeFound("id.notEquals=" + id);

        defaultRoundShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoundShouldNotBeFound("id.greaterThan=" + id);

        defaultRoundShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoundShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber equals to DEFAULT_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.equals=" + DEFAULT_ROUND_NUMBER);

        // Get all the roundList where roundNumber equals to UPDATED_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.equals=" + UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber not equals to DEFAULT_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.notEquals=" + DEFAULT_ROUND_NUMBER);

        // Get all the roundList where roundNumber not equals to UPDATED_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.notEquals=" + UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsInShouldWork() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber in DEFAULT_ROUND_NUMBER or UPDATED_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.in=" + DEFAULT_ROUND_NUMBER + "," + UPDATED_ROUND_NUMBER);

        // Get all the roundList where roundNumber equals to UPDATED_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.in=" + UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber is not null
        defaultRoundShouldBeFound("roundNumber.specified=true");

        // Get all the roundList where roundNumber is null
        defaultRoundShouldNotBeFound("roundNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber is greater than or equal to DEFAULT_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.greaterThanOrEqual=" + DEFAULT_ROUND_NUMBER);

        // Get all the roundList where roundNumber is greater than or equal to UPDATED_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.greaterThanOrEqual=" + UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber is less than or equal to DEFAULT_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.lessThanOrEqual=" + DEFAULT_ROUND_NUMBER);

        // Get all the roundList where roundNumber is less than or equal to SMALLER_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.lessThanOrEqual=" + SMALLER_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber is less than DEFAULT_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.lessThan=" + DEFAULT_ROUND_NUMBER);

        // Get all the roundList where roundNumber is less than UPDATED_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.lessThan=" + UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void getAllRoundsByRoundNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where roundNumber is greater than DEFAULT_ROUND_NUMBER
        defaultRoundShouldNotBeFound("roundNumber.greaterThan=" + DEFAULT_ROUND_NUMBER);

        // Get all the roundList where roundNumber is greater than SMALLER_ROUND_NUMBER
        defaultRoundShouldBeFound("roundNumber.greaterThan=" + SMALLER_ROUND_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoundShouldBeFound(String filter) throws Exception {
        restRoundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(round.getId().intValue())))
            .andExpect(jsonPath("$.[*].roundNumber").value(hasItem(DEFAULT_ROUND_NUMBER)));

        // Check, that the count call also returns 1
        restRoundMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoundShouldNotBeFound(String filter) throws Exception {
        restRoundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoundMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRound() throws Exception {
        // Get the round
        restRoundMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Update the round
        Round updatedRound = roundRepository.findById(round.getId()).get();
        // Disconnect from session so that the updates on updatedRound are not directly saved in db
        em.detach(updatedRound);
        updatedRound.roundNumber(UPDATED_ROUND_NUMBER);
        RoundDTO roundDTO = roundMapper.toDto(updatedRound);

        restRoundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roundDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roundDTO))
            )
            .andExpect(status().isOk());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getRoundNumber()).isEqualTo(UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();
        round.setId(count.incrementAndGet());

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roundDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();
        round.setId(count.incrementAndGet());

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();
        round.setId(count.incrementAndGet());

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoundMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoundWithPatch() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Update the round using partial update
        Round partialUpdatedRound = new Round();
        partialUpdatedRound.setId(round.getId());

        restRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRound.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRound))
            )
            .andExpect(status().isOk());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getRoundNumber()).isEqualTo(DEFAULT_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateRoundWithPatch() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Update the round using partial update
        Round partialUpdatedRound = new Round();
        partialUpdatedRound.setId(round.getId());

        partialUpdatedRound.roundNumber(UPDATED_ROUND_NUMBER);

        restRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRound.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRound))
            )
            .andExpect(status().isOk());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getRoundNumber()).isEqualTo(UPDATED_ROUND_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();
        round.setId(count.incrementAndGet());

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roundDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();
        round.setId(count.incrementAndGet());

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();
        round.setId(count.incrementAndGet());

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoundMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        int databaseSizeBeforeDelete = roundRepository.findAll().size();

        // Delete the round
        restRoundMockMvc
            .perform(delete(ENTITY_API_URL_ID, round.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
