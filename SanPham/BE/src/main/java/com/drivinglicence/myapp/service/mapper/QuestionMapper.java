package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.Question;
import com.drivinglicence.myapp.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {}
