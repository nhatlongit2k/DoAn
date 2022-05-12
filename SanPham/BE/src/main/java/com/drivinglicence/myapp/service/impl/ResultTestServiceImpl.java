package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.ResultQuestion;
import com.drivinglicence.myapp.domain.ResultTest;
import com.drivinglicence.myapp.repository.ResultQuestionRepository;
import com.drivinglicence.myapp.repository.ResultTestRepository;
import com.drivinglicence.myapp.service.ResultTestService;
import com.drivinglicence.myapp.service.dto.ResultQuestionDTO;
import com.drivinglicence.myapp.service.dto.ResultTestDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import com.drivinglicence.myapp.service.mapper.ResultQuestionMapper;
import com.drivinglicence.myapp.service.mapper.ResultTestMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ResultTest}.
 */
@Service
@Transactional
public class ResultTestServiceImpl implements ResultTestService {

    private final Logger log = LoggerFactory.getLogger(ResultTestServiceImpl.class);

    private final ResultTestRepository resultTestRepository;

    private final ResultTestMapper resultTestMapper;

    private final ResultQuestionRepository resultQuestionRepository;

    private final ResultQuestionMapper resultQuestionMapper;

    public ResultTestServiceImpl(ResultTestRepository resultTestRepository, ResultTestMapper resultTestMapper, ResultQuestionRepository resultQuestionRepository, ResultQuestionMapper resultQuestionMapper) {
        this.resultTestRepository = resultTestRepository;
        this.resultTestMapper = resultTestMapper;
        this.resultQuestionRepository = resultQuestionRepository;
        this.resultQuestionMapper = resultQuestionMapper;
    }

    @Override
    public ResultTestDTO save(ResultTestDTO resultTestDTO) {
        log.debug("Request to save ResultTest : {}", resultTestDTO);
        ResultTest resultTest = resultTestMapper.toEntity(resultTestDTO);
        resultTest = resultTestRepository.save(resultTest);
        return resultTestMapper.toDto(resultTest);
    }

    @Override
    public ResultTestDTO update(ResultTestDTO resultTestDTO) {
        log.debug("Request to save ResultTest : {}", resultTestDTO);
        ResultTest resultTest = resultTestMapper.toEntity(resultTestDTO);
        resultTest = resultTestRepository.save(resultTest);
        return resultTestMapper.toDto(resultTest);
    }

    @Override
    public Optional<ResultTestDTO> partialUpdate(ResultTestDTO resultTestDTO) {
        log.debug("Request to partially update ResultTest : {}", resultTestDTO);

        return resultTestRepository
            .findById(resultTestDTO.getId())
            .map(existingResultTest -> {
                resultTestMapper.partialUpdate(existingResultTest, resultTestDTO);

                return existingResultTest;
            })
            .map(resultTestRepository::save)
            .map(resultTestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResultTestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResultTests");
        return resultTestRepository.findAll(pageable).map(resultTestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResultTestDTO> findOne(Long id) {
        log.debug("Request to get ResultTest : {}", id);
        return resultTestRepository.findById(id).map(resultTestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResultTest : {}", id);
        resultTestRepository.deleteById(id);
    }

    @Override
    public Result saveAllResult(ResultTestDTO resultTestDTO) {
        ResultTest resultTest = resultTestMapper.toEntity(resultTestDTO);
        resultTest = resultTestRepository.save(resultTest);
        List<ResultQuestionDTO> resultQuestionDTOList = resultTestDTO.getResultQuestionDTOList();
        List<ResultQuestion> resultQuestions = resultQuestionMapper.toEntity(resultQuestionDTOList);
        for(ResultQuestion resultQuestion: resultQuestions){
            resultQuestion.setResulttestId(resultTest.getId());
        }
        resultQuestions = resultQuestionRepository.saveAll(resultQuestions);

        Result result = new Result("Thành công");
        return result;
    }

    @Override
    public Result deleteResult(Long id) {
        resultQuestionRepository.deleteAllByResulttestId(id);
        resultTestRepository.deleteById(id);
        Result result = new Result("Xóa thành công");
        return result;
    }
}
