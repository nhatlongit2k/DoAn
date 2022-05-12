package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.Type;
import com.drivinglicence.myapp.service.dto.TypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Type} and its DTO {@link TypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeMapper extends EntityMapper<TypeDTO, Type> {}
