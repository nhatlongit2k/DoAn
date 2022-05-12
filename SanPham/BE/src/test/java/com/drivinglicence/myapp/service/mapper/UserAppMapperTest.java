package com.drivinglicence.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAppMapperTest {

    private UserAppMapper userAppMapper;

    @BeforeEach
    public void setUp() {
        userAppMapper = new UserAppMapperImpl();
    }
}
