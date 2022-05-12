package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.ResultTest;
import com.drivinglicence.myapp.service.dto.ResultTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResultTest} and its DTO {@link ResultTestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResultTestMapper extends EntityMapper<ResultTestDTO, ResultTest> {}
