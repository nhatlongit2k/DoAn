package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.Answer;
import com.drivinglicence.myapp.domain.Question;
import com.drivinglicence.myapp.domain.ResultQuestion;
import com.drivinglicence.myapp.repository.AnswerRepository;
import com.drivinglicence.myapp.repository.QuestionRepository;
import com.drivinglicence.myapp.repository.ResultQuestionRepository;
import com.drivinglicence.myapp.service.QuestionService;
import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.QuestionDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import com.drivinglicence.myapp.service.mapper.AnswerMapper;
import com.drivinglicence.myapp.service.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.drivinglicence.myapp.service.mapper.ResultQuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    //1
    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    private final ResultQuestionRepository resultQuestionRepository;

    private final ResultQuestionMapper resultQuestionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper, AnswerRepository answerRepository, AnswerMapper answerMapper, ResultQuestionRepository resultQuestionRepository, ResultQuestionMapper resultQuestionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
        this.resultQuestionRepository = resultQuestionRepository;
        this.resultQuestionMapper = resultQuestionMapper;
    }

    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    @Override
    public QuestionDTO update(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    @Override
    public Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .map(questionRepository::save)
            .map(questionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable).map(questionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id).map(questionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }

    @Override
    public List<QuestionDTO> getQuestionByExamId(Long id) {
        List<Question> questions = questionRepository.findQuestionByExamId(id);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        for(Question question: questions){
            QuestionDTO questionDTO = questionMapper.toDto(question);
            questionDTOs.add(questionDTO);
        }
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> getAllQuestionWithAnswerInExam(Long id) {
        List<Question> questions = questionRepository.findQuestionByExamId(id);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        for(Question question: questions){
            QuestionDTO questionDTO = questionMapper.toDto(question);
            List<Answer> a = answerRepository.findAnswerByQuestionId(question.getId());
            questionDTO.setAnswerDTOList(answerMapper.toDto(a));
            questionDTOs.add(questionDTO);
        }
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> saveAllQuestions(List<QuestionDTO> questionDTOList) {
        log.debug("Request to save Questions : {}", questionDTOList);
        List<Question> questions = questionMapper.toEntity(questionDTOList);
        questions = questionRepository.saveAll(questions);
        return questionMapper.toDto(questions);
    }

    //day nha
    @Override
    public List<QuestionDTO> saveAllQuestionsAndAnswers(List<QuestionDTO> questionDTOList) {
//        List<AnswerDTO> answerDTOList = new ArrayList<>();
        for(QuestionDTO questionDTO : questionDTOList){
//            List<AnswerDTO> answersOfQues = new ArrayList<>(questionDTO.getAnswerDTOList());
//            answerDTOList.addAll(answersOfQues);
            saveQuestionWithListAnswer(questionDTO);
        }
//        List<Question> questions = questionMapper.toEntity(questionDTOList);
//        questions = questionRepository.saveAll(questions);
//        List<Answer> answers = answerMapper.toEntity(answerDTOList);
//        answers = answerRepository.saveAll(answers);
        return questionDTOList;
    }

    @Override
    public Result deleteQuestion(Long id) {
        List<ResultQuestion> resultQuestions = resultQuestionRepository.findResultQuestionByQuestionId(id);
        Result result = new Result("Xóa thành công");
        if(!resultQuestions.isEmpty()){
            result.setMessange("Không thể xóa do câu trả lời đã được ghi vào kết quả của người dùng");
            return result;
//            return "Không thể xóa do câu trả lời đã được ghi vào kết quả của người dùng";
        }
        answerRepository.deleteAllByQuestionId(id);
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
        return result;
//        return "Xóa thành công";
    }

    @Override
    public Result updateQuestionWithListAnswer(QuestionDTO questionDTO) {
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        List<AnswerDTO> answerDTOList = questionDTO.getAnswerDTOList();
        List<Answer> answers = answerMapper.toEntity(answerDTOList);
        for(Answer answer: answers){
            answer.setQuestionId(question.getId());
        }
        answers = answerRepository.saveAll(answers);
//        List<Answer> answerList = answerMapper.toEntity(answerDTOList);
//        for (Answer answer : answerList){
//            answer = answerRepository.save(answer);
//        }
        Result result = new Result("Update thành công");
        return result;
    }

    @Override
    public Result saveQuestionWithListAnswer(QuestionDTO questionDTO) {
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        List<AnswerDTO> answerDTOList = questionDTO.getAnswerDTOList();
        int sl = answerDTOList.size();
        List<Answer> answers = answerMapper.toEntity(answerDTOList);
        for(Answer answer: answers){
            answer.setQuestionId(question.getId());
        }
        answers = answerRepository.saveAll(answers);

//        List<Answer> answerList = answerMapper.toEntity(answerDTOList);
//        for (Answer answer : answerList){
//            answer.setQuestionId(question.getId());
//            answer = answerRepository.save(answer);
//        }
        Result result = new Result("Thêm thành công");
        return result;
    }
}
