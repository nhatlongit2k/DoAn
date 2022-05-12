package com.drivinglicence.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.drivinglicence.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultQuestionDTO.class);
        ResultQuestionDTO resultQuestionDTO1 = new ResultQuestionDTO();
        resultQuestionDTO1.setId(1L);
        ResultQuestionDTO resultQuestionDTO2 = new ResultQuestionDTO();
        assertThat(resultQuestionDTO1).isNotEqualTo(resultQuestionDTO2);
        resultQuestionDTO2.setId(resultQuestionDTO1.getId());
        assertThat(resultQuestionDTO1).isEqualTo(resultQuestionDTO2);
        resultQuestionDTO2.setId(2L);
        assertThat(resultQuestionDTO1).isNotEqualTo(resultQuestionDTO2);
        resultQuestionDTO1.setId(null);
        assertThat(resultQuestionDTO1).isNotEqualTo(resultQuestionDTO2);
    }
}
