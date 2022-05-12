package com.drivinglicence.myapp.service.impl;

import com.drivinglicence.myapp.domain.Exam;
import com.drivinglicence.myapp.domain.Type;
import com.drivinglicence.myapp.repository.ExamRepository;
import com.drivinglicence.myapp.repository.TypeRepository;
import com.drivinglicence.myapp.service.TypeService;
import com.drivinglicence.myapp.service.dto.TypeDTO;
import com.drivinglicence.myapp.service.dto.custom.Result;
import com.drivinglicence.myapp.service.mapper.ExamMapper;
import com.drivinglicence.myapp.service.mapper.TypeMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Type}.
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeServiceImpl.class);

    private final TypeRepository typeRepository;

    private final TypeMapper typeMapper;

    //1
    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper, ExamRepository examRepository, ExamMapper examMapper) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
        this.examRepository = examRepository;
        this.examMapper = examMapper;
    }

    @Override
    public TypeDTO save(TypeDTO typeDTO) {
        log.debug("Request to save Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        return typeMapper.toDto(type);
    }

    @Override
    public TypeDTO update(TypeDTO typeDTO) {
        log.debug("Request to save Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        return typeMapper.toDto(type);
    }

    @Override
    public Optional<TypeDTO> partialUpdate(TypeDTO typeDTO) {
        log.debug("Request to partially update Type : {}", typeDTO);

        return typeRepository
            .findById(typeDTO.getId())
            .map(existingType -> {
                typeMapper.partialUpdate(existingType, typeDTO);

                return existingType;
            })
            .map(typeRepository::save)
            .map(typeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typeRepository.findAll(pageable).map(typeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDTO> findOne(Long id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id).map(typeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
    }

    @Override
    public Result deleteType(Long id) {
        log.debug("Request to delete Type : {}", id);
        List<Exam> exams = examRepository.findExamByTypeId(id);
        Result result = new Result("Xóa thành công");
        if(!exams.isEmpty()){
            result.setMessange("Không thể xóa vì loại đề đã có các đề rồi!");
            return result;
//            return "Không thể xóa vì loại đề đã có các đề rồi!";
        }
        typeRepository.deleteById(id);
//        return "Xóa thành công";
        return result;
    }
}
