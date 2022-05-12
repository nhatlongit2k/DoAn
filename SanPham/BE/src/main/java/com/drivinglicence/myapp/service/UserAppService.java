package com.drivinglicence.myapp.service;

import com.drivinglicence.myapp.service.dto.UserAppDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.drivinglicence.myapp.domain.UserApp}.
 */
public interface UserAppService {
    /**
     * Save a userApp.
     *
     * @param userAppDTO the entity to save.
     * @return the persisted entity.
     */
    UserAppDTO save(UserAppDTO userAppDTO);

    /**
     * Updates a userApp.
     *
     * @param userAppDTO the entity to update.
     * @return the persisted entity.
     */
    UserAppDTO update(UserAppDTO userAppDTO);

    /**
     * Partially updates a userApp.
     *
     * @param userAppDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAppDTO> partialUpdate(UserAppDTO userAppDTO);

    /**
     * Get all the userApps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAppDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userApp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAppDTO> findOne(Long id);

    /**
     * Delete the "id" userApp.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
