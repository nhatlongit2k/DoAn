package com.drivinglicence.myapp.service;

import com.drivinglicence.myapp.domain.Answer;
import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.QuestionDTO;

import java.util.List;
import java.util.Optional;

import com.drivinglicence.myapp.service.dto.custom.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.drivinglicence.myapp.domain.Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     * Updates a question.
     *
     * @param questionDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionDTO update(QuestionDTO questionDTO);

    /**
     * Partially updates a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuestionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionDTO> findOne(Long id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<QuestionDTO> getQuestionByExamId(Long id);

    List<QuestionDTO> getAllQuestionWithAnswerInExam(Long id);

    List<QuestionDTO> saveAllQuestions(List<QuestionDTO> questionDTOList);

    List<QuestionDTO> saveAllQuestionsAndAnswers(List<QuestionDTO> questionDTOList);

    Result deleteQuestion(Long id);

    Result updateQuestionWithListAnswer(QuestionDTO questionDTO);

    Result saveQuestionWithListAnswer(QuestionDTO questionDTO);
}
