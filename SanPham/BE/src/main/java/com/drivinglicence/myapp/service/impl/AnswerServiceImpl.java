package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.Answer;
import com.drivinglicence.myapp.repository.AnswerRepository;
import com.drivinglicence.myapp.service.AnswerService;
import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import com.drivinglicence.myapp.service.mapper.AnswerMapper;

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
 * Service Implementation for managing {@link Answer}.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    public AnswerDTO save(AnswerDTO answerDTO) {
        log.debug("Request to save Answer : {}", answerDTO);
        Answer answer = answerMapper.toEntity(answerDTO);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }

    @Override
    public AnswerDTO update(AnswerDTO answerDTO) {
        log.debug("Request to save Answer : {}", answerDTO);
        Answer answer = answerMapper.toEntity(answerDTO);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }

    @Override
    public Optional<AnswerDTO> partialUpdate(AnswerDTO answerDTO) {
        log.debug("Request to partially update Answer : {}", answerDTO);

        return answerRepository
            .findById(answerDTO.getId())
            .map(existingAnswer -> {
                answerMapper.partialUpdate(existingAnswer, answerDTO);

                return existingAnswer;
            })
            .map(answerRepository::save)
            .map(answerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answerRepository.findAll(pageable).map(answerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerDTO> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id).map(answerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }

    @Override
    public List<AnswerDTO> getAnswerByQuestionId(Long id) {
        List<Answer> answers = answerRepository.findAnswerByQuestionId(id);
        List<AnswerDTO> answerDTOs = new ArrayList<>();
        for(Answer answer: answers){
            AnswerDTO answerDTO = answerMapper.toDto(answer);
            answerDTOs.add(answerDTO);
        }
        return answerDTOs;
    }

    @Override
    public List<AnswerDTO> saveAllAnswers(List<AnswerDTO> answerDTOList) {
        log.debug("Request to save Questions : {}", answerDTOList);
        List<Answer> answers = answerMapper.toEntity(answerDTOList);
        answers = answerRepository.saveAll(answers);
        return answerMapper.toDto(answers);
    }

    @Override
    public Result deleteAnswerByQuestionId(Long id) {
        answerRepository.deleteAllByQuestionId(id);
        Result result = new Result("Xóa thành công");
        return result;
//        return "Xóa thành công";
    }

    @Override
    public Result deleteAnswer(Long id) {
        Result result = new Result("Xóa thành công");
        answerRepository.deleteById(id);
        return result;
    }
}
