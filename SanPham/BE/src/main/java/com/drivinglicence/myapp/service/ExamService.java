package com.drivinglicence.myapp.service;

import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.ExamDTO;

import java.util.List;
import java.util.Optional;

import com.drivinglicence.myapp.service.dto.QuestionDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.drivinglicence.myapp.domain.Exam}.
 */
public interface ExamService {
    /**
     * Save a exam.
     *
     * @param examDTO the entity to save.
     * @return the persisted entity.
     */
    ExamDTO save(ExamDTO examDTO);

    /**
     * Updates a exam.
     *
     * @param examDTO the entity to update.
     * @return the persisted entity.
     */
    ExamDTO update(ExamDTO examDTO);

    /**
     * Partially updates a exam.
     *
     * @param examDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExamDTO> partialUpdate(ExamDTO examDTO);

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamDTO> findOne(Long id);

    /**
     * Delete the "id" exam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ExamDTO> getExamByTypeId(Long id);

    List<ExamDTO> getExamForUser();

    List<ExamDTO> getExamForUserByTypeId(Long id);

    Result deleteExam(Long id);

    String addAnswersQuestionsExam(String name, Long numberOfQuestion, Long time, Long typeId, String createBy, List<QuestionDTO> questionDTOList, List<AnswerDTO> answerDTOList);
}
