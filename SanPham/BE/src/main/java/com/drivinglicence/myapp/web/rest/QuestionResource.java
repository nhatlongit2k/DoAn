package com.drivinglicence.myapp.web.rest;

import com.drivinglicence.myapp.repository.QuestionRepository;
import com.drivinglicence.myapp.service.QuestionService;
import com.drivinglicence.myapp.service.dto.QuestionDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import com.drivinglicence.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
 * REST controller for managing {@link com.drivinglicence.myapp.domain.Question}.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;

    private final QuestionRepository questionRepository;

    public QuestionResource(QuestionService questionService, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param questionDTO the questionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionDTO, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity
            .created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Question : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionDTO result = questionService.update(questionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionDTO> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Question partially : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionDTO> result = questionService.partialUpdate(questionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Questions");
        Page<QuestionDTO> page = questionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        Optional<QuestionDTO> questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionDTO);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    public Result deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        Result result = questionService.deleteQuestion(id);
        return result;
//        questionService.delete(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
//            .build();
    }

    @GetMapping("/questions/getQuestionByExamId/{id}")
    public ResponseEntity<?> getQuestionByExamId(@PathVariable Long id){
        List<QuestionDTO> questionsDTOs = new ArrayList<>();
        questionsDTOs = questionService.getQuestionByExamId(id);
        return ResponseEntity.ok(questionsDTOs);
    }

    @GetMapping("/questions/getAllQuestionWithAnswerInExam/{id}")
    public ResponseEntity<?> getAllQuestionWithAnswerInExam(@PathVariable Long id){
        List<QuestionDTO> questionsDTOs = new ArrayList<>();
        questionsDTOs = questionService.getAllQuestionWithAnswerInExam(id);
        return ResponseEntity.ok(questionsDTOs);
    }

    @PostMapping("/questions/saveAllQuestions")
    public ResponseEntity<List<QuestionDTO>> saveAllQuestions(@RequestBody List<QuestionDTO> questionDTOList) throws URISyntaxException {
        log.debug("REST request to save Questions : {}", questionDTOList);
        List<QuestionDTO> result = questionService.saveAllQuestions(questionDTOList);
        return ResponseEntity
            .created(new URI("/api/questions/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME,"1"))//kro ne dat la 1
            .body(result);
    }

    @PostMapping("/questions/saveAllQuestionsAndAnswers")
    public ResponseEntity<List<QuestionDTO>> saveAllQuestionsAndAnswer(@RequestBody List<QuestionDTO> questionDTOList) throws URISyntaxException {
        log.debug("REST request to save Questions : {}", questionDTOList);
        List<QuestionDTO> result = questionService.saveAllQuestionsAndAnswers(questionDTOList);
        return ResponseEntity
            .created(new URI("/api/questions/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME,"1"))//kro ne dat la 1
            .body(result);
    }

    @PutMapping("question/updateQuestionWithListAnswer")
    public Result updateQuestionWithListAnswer(@RequestBody QuestionDTO questionDTO){
        log.debug("REST request to save Question: ");
        Result result = questionService.updateQuestionWithListAnswer(questionDTO);
        return result;
    }

    @PostMapping("question/saveQuestionWithListAnswer")
    public Result saveQuestionWithListAnswer(@RequestBody QuestionDTO questionDTO){
        Result result = questionService.saveQuestionWithListAnswer(questionDTO);
        return result;
    }
}
