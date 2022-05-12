package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.Answer;
import com.drivinglicence.myapp.domain.ResultQuestion;
import com.drivinglicence.myapp.repository.ResultQuestionRepository;
import com.drivinglicence.myapp.service.ResultQuestionService;
import com.drivinglicence.myapp.service.dto.AnswerDTO;
import com.drivinglicence.myapp.service.dto.ResultQuestionDTO;
import com.drivinglicence.myapp.service.mapper.ResultQuestionMapper;

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
 * Service Implementation for managing {@link ResultQuestion}.
 */
@Service
@Transactional
public class ResultQuestionServiceImpl implements ResultQuestionService {

    private final Logger log = LoggerFactory.getLogger(ResultQuestionServiceImpl.class);

    private final ResultQuestionRepository resultQuestionRepository;

    private final ResultQuestionMapper resultQuestionMapper;

    public ResultQuestionServiceImpl(ResultQuestionRepository resultQuestionRepository, ResultQuestionMapper resultQuestionMapper) {
        this.resultQuestionRepository = resultQuestionRepository;
        this.resultQuestionMapper = resultQuestionMapper;
    }

    @Override
    public ResultQuestionDTO save(ResultQuestionDTO resultQuestionDTO) {
        log.debug("Request to save ResultQuestion : {}", resultQuestionDTO);
        ResultQuestion resultQuestion = resultQuestionMapper.toEntity(resultQuestionDTO);
        resultQuestion = resultQuestionRepository.save(resultQuestion);
        return resultQuestionMapper.toDto(resultQuestion);
    }

    @Override
    public ResultQuestionDTO update(ResultQuestionDTO resultQuestionDTO) {
        log.debug("Request to save ResultQuestion : {}", resultQuestionDTO);
        ResultQuestion resultQuestion = resultQuestionMapper.toEntity(resultQuestionDTO);
        resultQuestion = resultQuestionRepository.save(resultQuestion);
        return resultQuestionMapper.toDto(resultQuestion);
    }

    @Override
    public Optional<ResultQuestionDTO> partialUpdate(ResultQuestionDTO resultQuestionDTO) {
        log.debug("Request to partially update ResultQuestion : {}", resultQuestionDTO);

        return resultQuestionRepository
            .findById(resultQuestionDTO.getId())
            .map(existingResultQuestion -> {
                resultQuestionMapper.partialUpdate(existingResultQuestion, resultQuestionDTO);

                return existingResultQuestion;
            })
            .map(resultQuestionRepository::save)
            .map(resultQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResultQuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResultQuestions");
        return resultQuestionRepository.findAll(pageable).map(resultQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResultQuestionDTO> findOne(Long id) {
        log.debug("Request to get ResultQuestion : {}", id);
        return resultQuestionRepository.findById(id).map(resultQuestionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResultQuestion : {}", id);
        resultQuestionRepository.deleteById(id);
    }

    @Override
    public List<ResultQuestionDTO> getResultQuestionByResultTestId(Long id) {
        List<ResultQuestion> resultQuestions = resultQuestionRepository.findAllByResulttestId(id);
        List<ResultQuestionDTO> resultQuestionDTOs = new ArrayList<>();
        for(ResultQuestion resultQuestion: resultQuestions){
            ResultQuestionDTO resultQuestionDTO = resultQuestionMapper.toDto(resultQuestion);
            resultQuestionDTOs.add(resultQuestionDTO);
        }
        return resultQuestionDTOs;
    }
}
