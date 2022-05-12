package com.drivinglicence.myapp.web.rest;

import com.drivinglicence.myapp.repository.ResultTestRepository;
import com.drivinglicence.myapp.service.ResultTestService;
import com.drivinglicence.myapp.service.dto.ResultTestDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
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
 * REST controller for managing {@link com.drivinglicence.myapp.domain.ResultTest}.
 */
@RestController
@RequestMapping("/api")
public class ResultTestResource {

    private final Logger log = LoggerFactory.getLogger(ResultTestResource.class);

    private static final String ENTITY_NAME = "resultTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultTestService resultTestService;

    private final ResultTestRepository resultTestRepository;

    public ResultTestResource(ResultTestService resultTestService, ResultTestRepository resultTestRepository) {
        this.resultTestService = resultTestService;
        this.resultTestRepository = resultTestRepository;
    }

    /**
     * {@code POST  /result-tests} : Create a new resultTest.
     *
     * @param resultTestDTO the resultTestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultTestDTO, or with status {@code 400 (Bad Request)} if the resultTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/result-tests")
    public ResponseEntity<ResultTestDTO> createResultTest(@RequestBody ResultTestDTO resultTestDTO) throws URISyntaxException {
        log.debug("REST request to save ResultTest : {}", resultTestDTO);
        if (resultTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new resultTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResultTestDTO result = resultTestService.save(resultTestDTO);
        return ResponseEntity
            .created(new URI("/api/result-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /result-tests/:id} : Updates an existing resultTest.
     *
     * @param id the id of the resultTestDTO to save.
     * @param resultTestDTO the resultTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultTestDTO,
     * or with status {@code 400 (Bad Request)} if the resultTestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/result-tests/{id}")
    public ResponseEntity<ResultTestDTO> updateResultTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultTestDTO resultTestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResultTest : {}, {}", id, resultTestDTO);
        if (resultTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResultTestDTO result = resultTestService.update(resultTestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resultTestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /result-tests/:id} : Partial updates given fields of an existing resultTest, field will ignore if it is null
     *
     * @param id the id of the resultTestDTO to save.
     * @param resultTestDTO the resultTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultTestDTO,
     * or with status {@code 400 (Bad Request)} if the resultTestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resultTestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resultTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/result-tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResultTestDTO> partialUpdateResultTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultTestDTO resultTestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResultTest partially : {}, {}", id, resultTestDTO);
        if (resultTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResultTestDTO> result = resultTestService.partialUpdate(resultTestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resultTestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /result-tests} : get all the resultTests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultTests in body.
     */
    @GetMapping("/result-tests")
    public ResponseEntity<List<ResultTestDTO>> getAllResultTests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ResultTests");
        Page<ResultTestDTO> page = resultTestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /result-tests/:id} : get the "id" resultTest.
     *
     * @param id the id of the resultTestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultTestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/result-tests/{id}")
    public ResponseEntity<ResultTestDTO> getResultTest(@PathVariable Long id) {
        log.debug("REST request to get ResultTest : {}", id);
        Optional<ResultTestDTO> resultTestDTO = resultTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resultTestDTO);
    }

    /**
     * {@code DELETE  /result-tests/:id} : delete the "id" resultTest.
     *
     * @param id the id of the resultTestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/result-tests/{id}")
    public ResponseEntity<Void> deleteResultTest(@PathVariable Long id) {
        log.debug("REST request to delete ResultTest : {}", id);
        resultTestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/result-tests/saveAllResult")
    public Result saveAllResult(@RequestBody ResultTestDTO resultTestDTO){
        Result result = resultTestService.saveAllResult(resultTestDTO);
        return result;
    }

    @DeleteMapping("/result-tests/deleteResult/{id}")
    public Result deleteResult(@PathVariable Long id){
        Result result = resultTestService.deleteResult(id);
        return result;
    }
}
