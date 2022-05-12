package com.drivinglicence.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.drivinglicence.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultTest.class);
        ResultTest resultTest1 = new ResultTest();
        resultTest1.setId(1L);
        ResultTest resultTest2 = new ResultTest();
        resultTest2.setId(resultTest1.getId());
        assertThat(resultTest1).isEqualTo(resultTest2);
        resultTest2.setId(2L);
        assertThat(resultTest1).isNotEqualTo(resultTest2);
        resultTest1.setId(null);
        assertThat(resultTest1).isNotEqualTo(resultTest2);
    }
}
