package com.drivinglicence.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultTestMapperTest {

    private ResultTestMapper resultTestMapper;

    @BeforeEach
    public void setUp() {
        resultTestMapper = new ResultTestMapperImpl();
    }
}
