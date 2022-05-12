package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.ResultQuestion;
import com.drivinglicence.myapp.service.dto.ResultQuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResultQuestion} and its DTO {@link ResultQuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResultQuestionMapper extends EntityMapper<ResultQuestionDTO, ResultQuestion> {}
