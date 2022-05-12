package com.drivinglicence.myapp.service.mapper;

import com.drivinglicence.myapp.domain.UserApp;
import com.drivinglicence.myapp.service.dto.UserAppDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserApp} and its DTO {@link UserAppDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAppMapper extends EntityMapper<UserAppDTO, UserApp> {}
