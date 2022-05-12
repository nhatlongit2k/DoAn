package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.*;
import com.drivinglicence.myapp.repository.*;
import com.drivinglicence.myapp.service.ExamService;
import com.drivinglicence.myapp.service.UserService;
import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.ExamDTO;
import com.drivinglicence.myapp.service.dto.QuestionDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import com.drivinglicence.myapp.service.mapper.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exam}.
 */
@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    private final CustomExamRepository customExamRepository;

    private final UserService userService;

    //1
    private final ResultTestRepository resultTestRepository;

    private final ResultTestMapper resultTestMapper;

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    private final ResultQuestionRepository resultQuestionRepository;

    private final ResultQuestionMapper resultQuestionMapper;

    public ExamServiceImpl(ExamRepository examRepository, ExamMapper examMapper, CustomExamRepository customExamRepository, UserService userService, ResultTestRepository resultTestRepository, ResultTestMapper resultTestMapper, QuestionRepository questionRepository, QuestionMapper questionMapper, AnswerRepository answerRepository, AnswerMapper answerMapper, ResultQuestionRepository resultQuestionRepository, ResultQuestionMapper resultQuestionMapper) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.customExamRepository = customExamRepository;
        this.userService = userService;
        this.resultTestRepository = resultTestRepository;
        this.resultTestMapper = resultTestMapper;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
        this.resultQuestionRepository = resultQuestionRepository;
        this.resultQuestionMapper = resultQuestionMapper;
    }

    @Override
    public ExamDTO save(ExamDTO examDTO) {
        log.debug("Request to save Exam : {}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    @Override
    public ExamDTO update(ExamDTO examDTO) {
        log.debug("Request to save Exam : {}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    @Override
    public Optional<ExamDTO> partialUpdate(ExamDTO examDTO) {
        log.debug("Request to partially update Exam : {}", examDTO);

        return examRepository
            .findById(examDTO.getId())
            .map(existingExam -> {
                examMapper.partialUpdate(existingExam, examDTO);

                return existingExam;
            })
            .map(examRepository::save)
            .map(examMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exams");
        return examRepository.findAll(pageable).map(examMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamDTO> findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        return examRepository.findById(id).map(examMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }

    @Override
    public List<ExamDTO> getExamByTypeId(Long id) {
        List<Exam> exams = examRepository.findExamByTypeId(id);
        List<ExamDTO> examDTOs = new ArrayList<>();
        for(Exam exam: exams){
            ExamDTO examDTO = examMapper.toDto(exam);
            examDTOs.add(examDTO);
        }
        return examDTOs;
    }

    @Override
    public List<ExamDTO> getExamForUser() {
        List<ExamDTO> res = new ArrayList<>();
        Optional<User> u = userService.getUserWithAuthorities();
        res = customExamRepository.getListExamForUser(u.get());
        return res;
    }

    @Override
    public List<ExamDTO> getExamForUserByTypeId(Long id) {
        List<ExamDTO> res = new ArrayList<>();
        Optional<User> u = userService.getUserWithAuthorities();
        res = customExamRepository.getListExamForUserWithTypeId(u.get(), id);
        return res;
    }

    @Override
    public Result deleteExam(Long id) {
        List<ResultTest> resultTests = resultTestRepository.findResultByExamId(id);
        Result result = new Result("Xóa thành công");
        if(!resultTests.isEmpty()){
            result.setMessange("Xóa không thành công do đã có người làm đề này");
            return result;
//            return "Xóa không thành công do đã có người làm đề này";
        }
        List<Question> questions = questionRepository.findQuestionByExamId(id);
        for (Question question: questions){
            List<ResultQuestion> resultQuestions = resultQuestionRepository.findResultQuestionByQuestionId(question.getId());
            if(!resultQuestions.isEmpty()){
                result.setMessange("Không thể xóa do câu trả lời đã được ghi vào kết quả của người dùng");
                return result;
//                return "Không thể xóa do câu trả lời đã được ghi vào kết quả của người dùng";
            }
            answerRepository.deleteAllByQuestionId(question.getId());
        }
        questionRepository.deleteAllByExamId(id);
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
        return result;
//        return "Xóa thành công";
    }

    @Override
    public String addAnswersQuestionsExam(String name, Long numberOfQuestion, Long time, Long typeId, String createBy, List<QuestionDTO> questionDTOList, List<AnswerDTO> answerDTOList) {
        log.debug("Request to save Exam : {}");
        Exam exam = new Exam();
        exam.name(name);
        exam.numberOfQuestion(numberOfQuestion);
        exam.time(time);
        exam.maxScore(10.0F);
        exam.typeId(typeId);
        exam.createBy(createBy);
        exam.createTime(ZonedDateTime.now());
        exam = examRepository.save(exam);
//        examMapper.toDto(exam);
        List<Question> questions = questionMapper.toEntity(questionDTOList);
        questions = questionRepository.saveAll(questions);
        List<Answer> answers = answerMapper.toEntity(answerDTOList);
        answers = answerRepository.saveAll(answers);
        return "Thêm thành công";
    }
}
