package com.drivinglicence.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.drivinglicence.myapp.IntegrationTest;
import com.drivinglicence.myapp.domain.ResultTest;
import com.drivinglicence.myapp.repository.ResultTestRepository;
import com.drivinglicence.myapp.service.dto.ResultTestDTO;
import com.drivinglicence.myapp.service.mapper.ResultTestMapper;
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
 * Integration tests for the {@link ResultTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResultTestResourceIT {

    private static final Float DEFAULT_SCORE = 1F;
    private static final Float UPDATED_SCORE = 2F;

    private static final Long DEFAULT_NUMBER_CORRECT_QUESTION = 1L;
    private static final Long UPDATED_NUMBER_CORRECT_QUESTION = 2L;

    private static final Boolean DEFAULT_IS_PASS = false;
    private static final Boolean UPDATED_IS_PASS = true;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_EXAM_ID = 1L;
    private static final Long UPDATED_EXAM_ID = 2L;

    private static final String ENTITY_API_URL = "/api/result-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResultTestRepository resultTestRepository;

    @Autowired
    private ResultTestMapper resultTestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultTestMockMvc;

    private ResultTest resultTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultTest createEntity(EntityManager em) {
        ResultTest resultTest = new ResultTest()
            .score(DEFAULT_SCORE)
            .numberCorrectQuestion(DEFAULT_NUMBER_CORRECT_QUESTION)
            .isPass(DEFAULT_IS_PASS)
            .userId(DEFAULT_USER_ID)
            .examId(DEFAULT_EXAM_ID);
        return resultTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultTest createUpdatedEntity(EntityManager em) {
        ResultTest resultTest = new ResultTest()
            .score(UPDATED_SCORE)
            .numberCorrectQuestion(UPDATED_NUMBER_CORRECT_QUESTION)
            .isPass(UPDATED_IS_PASS)
            .userId(UPDATED_USER_ID)
            .examId(UPDATED_EXAM_ID);
        return resultTest;
    }

    @BeforeEach
    public void initTest() {
        resultTest = createEntity(em);
    }

    @Test
    @Transactional
    void createResultTest() throws Exception {
        int databaseSizeBeforeCreate = resultTestRepository.findAll().size();
        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);
        restResultTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultTestDTO)))
            .andExpect(status().isCreated());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeCreate + 1);
        ResultTest testResultTest = resultTestList.get(resultTestList.size() - 1);
        assertThat(testResultTest.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testResultTest.getNumberCorrectQuestion()).isEqualTo(DEFAULT_NUMBER_CORRECT_QUESTION);
        assertThat(testResultTest.getIsPass()).isEqualTo(DEFAULT_IS_PASS);
        assertThat(testResultTest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testResultTest.getExamId()).isEqualTo(DEFAULT_EXAM_ID);
    }

    @Test
    @Transactional
    void createResultTestWithExistingId() throws Exception {
        // Create the ResultTest with an existing ID
        resultTest.setId(1L);
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        int databaseSizeBeforeCreate = resultTestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResultTests() throws Exception {
        // Initialize the database
        resultTestRepository.saveAndFlush(resultTest);

        // Get all the resultTestList
        restResultTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].numberCorrectQuestion").value(hasItem(DEFAULT_NUMBER_CORRECT_QUESTION.intValue())))
            .andExpect(jsonPath("$.[*].isPass").value(hasItem(DEFAULT_IS_PASS.booleanValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].examId").value(hasItem(DEFAULT_EXAM_ID.intValue())));
    }

    @Test
    @Transactional
    void getResultTest() throws Exception {
        // Initialize the database
        resultTestRepository.saveAndFlush(resultTest);

        // Get the resultTest
        restResultTestMockMvc
            .perform(get(ENTITY_API_URL_ID, resultTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultTest.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.numberCorrectQuestion").value(DEFAULT_NUMBER_CORRECT_QUESTION.intValue()))
            .andExpect(jsonPath("$.isPass").value(DEFAULT_IS_PASS.booleanValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.examId").value(DEFAULT_EXAM_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingResultTest() throws Exception {
        // Get the resultTest
        restResultTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResultTest() throws Exception {
        // Initialize the database
        resultTestRepository.saveAndFlush(resultTest);

        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();

        // Update the resultTest
        ResultTest updatedResultTest = resultTestRepository.findById(resultTest.getId()).get();
        // Disconnect from session so that the updates on updatedResultTest are not directly saved in db
        em.detach(updatedResultTest);
        updatedResultTest
            .score(UPDATED_SCORE)
            .numberCorrectQuestion(UPDATED_NUMBER_CORRECT_QUESTION)
            .isPass(UPDATED_IS_PASS)
            .userId(UPDATED_USER_ID)
            .examId(UPDATED_EXAM_ID);
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(updatedResultTest);

        restResultTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultTestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultTestDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
        ResultTest testResultTest = resultTestList.get(resultTestList.size() - 1);
        assertThat(testResultTest.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testResultTest.getNumberCorrectQuestion()).isEqualTo(UPDATED_NUMBER_CORRECT_QUESTION);
        assertThat(testResultTest.getIsPass()).isEqualTo(UPDATED_IS_PASS);
        assertThat(testResultTest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testResultTest.getExamId()).isEqualTo(UPDATED_EXAM_ID);
    }

    @Test
    @Transactional
    void putNonExistingResultTest() throws Exception {
        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();
        resultTest.setId(count.incrementAndGet());

        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultTestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultTest() throws Exception {
        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();
        resultTest.setId(count.incrementAndGet());

        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultTest() throws Exception {
        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();
        resultTest.setId(count.incrementAndGet());

        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultTestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResultTestWithPatch() throws Exception {
        // Initialize the database
        resultTestRepository.saveAndFlush(resultTest);

        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();

        // Update the resultTest using partial update
        ResultTest partialUpdatedResultTest = new ResultTest();
        partialUpdatedResultTest.setId(resultTest.getId());

        partialUpdatedResultTest.numberCorrectQuestion(UPDATED_NUMBER_CORRECT_QUESTION).isPass(UPDATED_IS_PASS).userId(UPDATED_USER_ID);

        restResultTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultTest))
            )
            .andExpect(status().isOk());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
        ResultTest testResultTest = resultTestList.get(resultTestList.size() - 1);
        assertThat(testResultTest.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testResultTest.getNumberCorrectQuestion()).isEqualTo(UPDATED_NUMBER_CORRECT_QUESTION);
        assertThat(testResultTest.getIsPass()).isEqualTo(UPDATED_IS_PASS);
        assertThat(testResultTest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testResultTest.getExamId()).isEqualTo(DEFAULT_EXAM_ID);
    }

    @Test
    @Transactional
    void fullUpdateResultTestWithPatch() throws Exception {
        // Initialize the database
        resultTestRepository.saveAndFlush(resultTest);

        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();

        // Update the resultTest using partial update
        ResultTest partialUpdatedResultTest = new ResultTest();
        partialUpdatedResultTest.setId(resultTest.getId());

        partialUpdatedResultTest
            .score(UPDATED_SCORE)
            .numberCorrectQuestion(UPDATED_NUMBER_CORRECT_QUESTION)
            .isPass(UPDATED_IS_PASS)
            .userId(UPDATED_USER_ID)
            .examId(UPDATED_EXAM_ID);

        restResultTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultTest))
            )
            .andExpect(status().isOk());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
        ResultTest testResultTest = resultTestList.get(resultTestList.size() - 1);
        assertThat(testResultTest.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testResultTest.getNumberCorrectQuestion()).isEqualTo(UPDATED_NUMBER_CORRECT_QUESTION);
        assertThat(testResultTest.getIsPass()).isEqualTo(UPDATED_IS_PASS);
        assertThat(testResultTest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testResultTest.getExamId()).isEqualTo(UPDATED_EXAM_ID);
    }

    @Test
    @Transactional
    void patchNonExistingResultTest() throws Exception {
        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();
        resultTest.setId(count.incrementAndGet());

        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultTestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultTest() throws Exception {
        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();
        resultTest.setId(count.incrementAndGet());

        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultTest() throws Exception {
        int databaseSizeBeforeUpdate = resultTestRepository.findAll().size();
        resultTest.setId(count.incrementAndGet());

        // Create the ResultTest
        ResultTestDTO resultTestDTO = resultTestMapper.toDto(resultTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultTestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resultTestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultTest in the database
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResultTest() throws Exception {
        // Initialize the database
        resultTestRepository.saveAndFlush(resultTest);

        int databaseSizeBeforeDelete = resultTestRepository.findAll().size();

        // Delete the resultTest
        restResultTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultTest> resultTestList = resultTestRepository.findAll();
        assertThat(resultTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
