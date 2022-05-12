package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.Answer;
import com.drivinglicence.myapp.service.dto.AnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {}
