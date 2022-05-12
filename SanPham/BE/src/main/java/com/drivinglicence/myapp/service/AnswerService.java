package com.drivinglicence.myapp.service;

import com.drivinglicence.myapp.service.dto.AnswerDTO;

import java.util.List;
import java.util.Optional;

import com.drivinglicence.myapp.service.dto.custom.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.drivinglicence.myapp.domain.Answer}.
 */
public interface AnswerService {
    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save.
     * @return the persisted entity.
     */
    AnswerDTO save(AnswerDTO answerDTO);

    /**
     * Updates a answer.
     *
     * @param answerDTO the entity to update.
     * @return the persisted entity.
     */
    AnswerDTO update(AnswerDTO answerDTO);

    /**
     * Partially updates a answer.
     *
     * @param answerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnswerDTO> partialUpdate(AnswerDTO answerDTO);

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnswerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" answer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnswerDTO> findOne(Long id);

    /**
     * Delete the "id" answer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<AnswerDTO> getAnswerByQuestionId(Long id);

    List<AnswerDTO> saveAllAnswers(List<AnswerDTO> answerDTOList);

    Result deleteAnswerByQuestionId(Long id);

    Result deleteAnswer(Long id);
}
