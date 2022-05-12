package com.drivinglicence.myapp.web.rest;

import com.drivinglicence.myapp.repository.UserAppRepository;
import com.drivinglicence.myapp.service.UserAppService;
import com.drivinglicence.myapp.service.dto.UserAppDTO;
import com.drivinglicence.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.drivinglicence.myapp.domain.UserApp}.
 */
@RestController
@RequestMapping("/api")
public class UserAppResource {

    private final Logger log = LoggerFactory.getLogger(UserAppResource.class);

    private static final String ENTITY_NAME = "userApp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAppService userAppService;

    private final UserAppRepository userAppRepository;

    public UserAppResource(UserAppService userAppService, UserAppRepository userAppRepository) {
        this.userAppService = userAppService;
        this.userAppRepository = userAppRepository;
    }

    /**
     * {@code POST  /user-apps} : Create a new userApp.
     *
     * @param userAppDTO the userAppDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAppDTO, or with status {@code 400 (Bad Request)} if the userApp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-apps")
    public ResponseEntity<UserAppDTO> createUserApp(@RequestBody UserAppDTO userAppDTO) throws URISyntaxException {
        log.debug("REST request to save UserApp : {}", userAppDTO);
        if (userAppDTO.getId() != null) {
            throw new BadRequestAlertException("A new userApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAppDTO result = userAppService.save(userAppDTO);
        return ResponseEntity
            .created(new URI("/api/user-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-apps/:id} : Updates an existing userApp.
     *
     * @param id the id of the userAppDTO to save.
     * @param userAppDTO the userAppDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAppDTO,
     * or with status {@code 400 (Bad Request)} if the userAppDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAppDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-apps/{id}")
    public ResponseEntity<UserAppDTO> updateUserApp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAppDTO userAppDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserApp : {}, {}", id, userAppDTO);
        if (userAppDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAppDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAppRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAppDTO result = userAppService.update(userAppDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAppDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-apps/:id} : Partial updates given fields of an existing userApp, field will ignore if it is null
     *
     * @param id the id of the userAppDTO to save.
     * @param userAppDTO the userAppDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAppDTO,
     * or with status {@code 400 (Bad Request)} if the userAppDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAppDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAppDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-apps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAppDTO> partialUpdateUserApp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAppDTO userAppDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserApp partially : {}, {}", id, userAppDTO);
        if (userAppDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAppDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAppRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAppDTO> result = userAppService.partialUpdate(userAppDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAppDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-apps} : get all the userApps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userApps in body.
     */
    @GetMapping("/user-apps")
    public ResponseEntity<List<UserAppDTO>> getAllUserApps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserApps");
        Page<UserAppDTO> page = userAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-apps/:id} : get the "id" userApp.
     *
     * @param id the id of the userAppDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAppDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-apps/{id}")
    public ResponseEntity<UserAppDTO> getUserApp(@PathVariable Long id) {
        log.debug("REST request to get UserApp : {}", id);
        Optional<UserAppDTO> userAppDTO = userAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAppDTO);
    }

    /**
     * {@code DELETE  /user-apps/:id} : delete the "id" userApp.
     *
     * @param id the id of the userAppDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-apps/{id}")
    public ResponseEntity<Void> deleteUserApp(@PathVariable Long id) {
        log.debug("REST request to delete UserApp : {}", id);
        userAppService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
