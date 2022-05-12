package com.drivinglicence.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.drivinglicence.myapp.IntegrationTest;
import com.drivinglicence.myapp.domain.ResultQuestion;
import com.drivinglicence.myapp.repository.ResultQuestionRepository;
import com.drivinglicence.myapp.service.dto.ResultQuestionDTO;
import com.drivinglicence.myapp.service.mapper.ResultQuestionMapper;
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

/**
 * Integration tests for the {@link ResultQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResultQuestionResourceIT {

    private static final Long DEFAULT_ID_ANSWER = 1L;
    private static final Long UPDATED_ID_ANSWER = 2L;

    private static final Boolean DEFAULT_IS_CORRECT = false;
    private static final Boolean UPDATED_IS_CORRECT = true;

    private static final Long DEFAULT_RESULTTEST_ID = 1L;
    private static final Long UPDATED_RESULTTEST_ID = 2L;

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/result-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResultQuestionRepository resultQuestionRepository;

    @Autowired
    private ResultQuestionMapper resultQuestionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultQuestionMockMvc;

    private ResultQuestion resultQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultQuestion createEntity(EntityManager em) {
        ResultQuestion resultQuestion = new ResultQuestion()
            .idAnswer(DEFAULT_ID_ANSWER)
            .isCorrect(DEFAULT_IS_CORRECT)
            .resulttestId(DEFAULT_RESULTTEST_ID)
            .questionId(DEFAULT_QUESTION_ID);
        return resultQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultQuestion createUpdatedEntity(EntityManager em) {
        ResultQuestion resultQuestion = new ResultQuestion()
            .idAnswer(UPDATED_ID_ANSWER)
            .isCorrect(UPDATED_IS_CORRECT)
            .resulttestId(UPDATED_RESULTTEST_ID)
            .questionId(UPDATED_QUESTION_ID);
        return resultQuestion;
    }

    @BeforeEach
    public void initTest() {
        resultQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createResultQuestion() throws Exception {
        int databaseSizeBeforeCreate = resultQuestionRepository.findAll().size();
        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);
        restResultQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        ResultQuestion testResultQuestion = resultQuestionList.get(resultQuestionList.size() - 1);
        assertThat(testResultQuestion.getIdAnswer()).isEqualTo(DEFAULT_ID_ANSWER);
        assertThat(testResultQuestion.getIsCorrect()).isEqualTo(DEFAULT_IS_CORRECT);
        assertThat(testResultQuestion.getResulttestId()).isEqualTo(DEFAULT_RESULTTEST_ID);
        assertThat(testResultQuestion.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
    }

    @Test
    @Transactional
    void createResultQuestionWithExistingId() throws Exception {
        // Create the ResultQuestion with an existing ID
        resultQuestion.setId(1L);
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        int databaseSizeBeforeCreate = resultQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResultQuestions() throws Exception {
        // Initialize the database
        resultQuestionRepository.saveAndFlush(resultQuestion);

        // Get all the resultQuestionList
        restResultQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAnswer").value(hasItem(DEFAULT_ID_ANSWER.intValue())))
            .andExpect(jsonPath("$.[*].isCorrect").value(hasItem(DEFAULT_IS_CORRECT.booleanValue())))
            .andExpect(jsonPath("$.[*].resulttestId").value(hasItem(DEFAULT_RESULTTEST_ID.intValue())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getResultQuestion() throws Exception {
        // Initialize the database
        resultQuestionRepository.saveAndFlush(resultQuestion);

        // Get the resultQuestion
        restResultQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, resultQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultQuestion.getId().intValue()))
            .andExpect(jsonPath("$.idAnswer").value(DEFAULT_ID_ANSWER.intValue()))
            .andExpect(jsonPath("$.isCorrect").value(DEFAULT_IS_CORRECT.booleanValue()))
            .andExpect(jsonPath("$.resulttestId").value(DEFAULT_RESULTTEST_ID.intValue()))
            .andExpect(jsonPath("$.questionId").value(DEFAULT_QUESTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingResultQuestion() throws Exception {
        // Get the resultQuestion
        restResultQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResultQuestion() throws Exception {
        // Initialize the database
        resultQuestionRepository.saveAndFlush(resultQuestion);

        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();

        // Update the resultQuestion
        ResultQuestion updatedResultQuestion = resultQuestionRepository.findById(resultQuestion.getId()).get();
        // Disconnect from session so that the updates on updatedResultQuestion are not directly saved in db
        em.detach(updatedResultQuestion);
        updatedResultQuestion
            .idAnswer(UPDATED_ID_ANSWER)
            .isCorrect(UPDATED_IS_CORRECT)
            .resulttestId(UPDATED_RESULTTEST_ID)
            .questionId(UPDATED_QUESTION_ID);
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(updatedResultQuestion);

        restResultQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
        ResultQuestion testResultQuestion = resultQuestionList.get(resultQuestionList.size() - 1);
        assertThat(testResultQuestion.getIdAnswer()).isEqualTo(UPDATED_ID_ANSWER);
        assertThat(testResultQuestion.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
        assertThat(testResultQuestion.getResulttestId()).isEqualTo(UPDATED_RESULTTEST_ID);
        assertThat(testResultQuestion.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingResultQuestion() throws Exception {
        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();
        resultQuestion.setId(count.incrementAndGet());

        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultQuestion() throws Exception {
        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();
        resultQuestion.setId(count.incrementAndGet());

        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultQuestion() throws Exception {
        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();
        resultQuestion.setId(count.incrementAndGet());

        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultQuestionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResultQuestionWithPatch() throws Exception {
        // Initialize the database
        resultQuestionRepository.saveAndFlush(resultQuestion);

        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();

        // Update the resultQuestion using partial update
        ResultQuestion partialUpdatedResultQuestion = new ResultQuestion();
        partialUpdatedResultQuestion.setId(resultQuestion.getId());

        partialUpdatedResultQuestion.isCorrect(UPDATED_IS_CORRECT).questionId(UPDATED_QUESTION_ID);

        restResultQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
        ResultQuestion testResultQuestion = resultQuestionList.get(resultQuestionList.size() - 1);
        assertThat(testResultQuestion.getIdAnswer()).isEqualTo(DEFAULT_ID_ANSWER);
        assertThat(testResultQuestion.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
        assertThat(testResultQuestion.getResulttestId()).isEqualTo(DEFAULT_RESULTTEST_ID);
        assertThat(testResultQuestion.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    void fullUpdateResultQuestionWithPatch() throws Exception {
        // Initialize the database
        resultQuestionRepository.saveAndFlush(resultQuestion);

        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();

        // Update the resultQuestion using partial update
        ResultQuestion partialUpdatedResultQuestion = new ResultQuestion();
        partialUpdatedResultQuestion.setId(resultQuestion.getId());

        partialUpdatedResultQuestion
            .idAnswer(UPDATED_ID_ANSWER)
            .isCorrect(UPDATED_IS_CORRECT)
            .resulttestId(UPDATED_RESULTTEST_ID)
            .questionId(UPDATED_QUESTION_ID);

        restResultQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
        ResultQuestion testResultQuestion = resultQuestionList.get(resultQuestionList.size() - 1);
        assertThat(testResultQuestion.getIdAnswer()).isEqualTo(UPDATED_ID_ANSWER);
        assertThat(testResultQuestion.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
        assertThat(testResultQuestion.getResulttestId()).isEqualTo(UPDATED_RESULTTEST_ID);
        assertThat(testResultQuestion.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingResultQuestion() throws Exception {
        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();
        resultQuestion.setId(count.incrementAndGet());

        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultQuestion() throws Exception {
        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();
        resultQuestion.setId(count.incrementAndGet());

        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultQuestion() throws Exception {
        int databaseSizeBeforeUpdate = resultQuestionRepository.findAll().size();
        resultQuestion.setId(count.incrementAndGet());

        // Create the ResultQuestion
        ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultQuestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultQuestion in the database
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResultQuestion() throws Exception {
        // Initialize the database
        resultQuestionRepository.saveAndFlush(resultQuestion);

        int databaseSizeBeforeDelete = resultQuestionRepository.findAll().size();

        // Delete the resultQuestion
        restResultQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultQuestion> resultQuestionList = resultQuestionRepository.findAll();
        assertThat(resultQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
