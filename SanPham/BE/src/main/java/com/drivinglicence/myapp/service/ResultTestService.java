package com.drivinglicence.myapp.service;

import com.drivinglicence.myapp.service.dto.ResultTestDTO;

import java.util.List;
import java.util.Optional;

import com.drivinglicence.myapp.service.dto.custom.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.drivinglicence.myapp.domain.ResultTest}.
 */
public interface ResultTestService {
    /**
     * Save a resultTest.
     *
     * @param resultTestDTO the entity to save.
     * @return the persisted entity.
     */
    ResultTestDTO save(ResultTestDTO resultTestDTO);

    /**
     * Updates a resultTest.
     *
     * @param resultTestDTO the entity to update.
     * @return the persisted entity.
     */
    ResultTestDTO update(ResultTestDTO resultTestDTO);

    /**
     * Partially updates a resultTest.
     *
     * @param resultTestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResultTestDTO> partialUpdate(ResultTestDTO resultTestDTO);

    /**
     * Get all the resultTests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResultTestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" resultTest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResultTestDTO> findOne(Long id);

    /**
     * Delete the "id" resultTest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Result saveAllResult(ResultTestDTO resultTestDTO);

    Result deleteResult(Long id);
}
