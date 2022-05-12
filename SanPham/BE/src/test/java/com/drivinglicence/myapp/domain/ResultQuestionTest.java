package com.drivinglicence.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.drivinglicence.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultQuestion.class);
        ResultQuestion resultQuestion1 = new ResultQuestion();
        resultQuestion1.setId(1L);
        ResultQuestion resultQuestion2 = new ResultQuestion();
        resultQuestion2.setId(resultQuestion1.getId());
        assertThat(resultQuestion1).isEqualTo(resultQuestion2);
        resultQuestion2.setId(2L);
        assertThat(resultQuestion1).isNotEqualTo(resultQuestion2);
        resultQuestion1.setId(null);
        assertThat(resultQuestion1).isNotEqualTo(resultQuestion2);
    }
}
