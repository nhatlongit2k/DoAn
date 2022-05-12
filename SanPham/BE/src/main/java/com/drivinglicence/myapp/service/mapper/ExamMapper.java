package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.Exam;
import com.drivinglicence.myapp.service.dto.ExamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {}
