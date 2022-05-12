package com.drivinglicence.myapp.service;

import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.ResultQuestionDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.drivinglicence.myapp.domain.ResultQuestion}.
 */
public interface ResultQuestionService {
    /**
     * Save a resultQuestion.
     *
     * @param resultQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    ResultQuestionDTO save(ResultQuestionDTO resultQuestionDTO);

    /**
     * Updates a resultQuestion.
     *
     * @param resultQuestionDTO the entity to update.
     * @return the persisted entity.
     */
    ResultQuestionDTO update(ResultQuestionDTO resultQuestionDTO);

    /**
     * Partially updates a resultQuestion.
     *
     * @param resultQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResultQuestionDTO> partialUpdate(ResultQuestionDTO resultQuestionDTO);

    /**
     * Get all the resultQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResultQuestionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" resultQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResultQuestionDTO> findOne(Long id);

    /**
     * Delete the "id" resultQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ResultQuestionDTO> getResultQuestionByResultTestId(Long id);
}
