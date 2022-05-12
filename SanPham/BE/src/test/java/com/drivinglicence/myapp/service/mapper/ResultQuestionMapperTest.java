package com.drivinglicence.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultQuestionMapperTest {

    private ResultQuestionMapper resultQuestionMapper;

    @BeforeEach
    public void setUp() {
        resultQuestionMapper = new ResultQuestionMapperImpl();
    }
}
