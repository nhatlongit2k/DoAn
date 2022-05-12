package com.drivinglicence.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.drivinglicence.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultTestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultTestDTO.class);
        ResultTestDTO resultTestDTO1 = new ResultTestDTO();
        resultTestDTO1.setId(1L);
        ResultTestDTO resultTestDTO2 = new ResultTestDTO();
        assertThat(resultTestDTO1).isNotEqualTo(resultTestDTO2);
        resultTestDTO2.setId(resultTestDTO1.getId());
        assertThat(resultTestDTO1).isEqualTo(resultTestDTO2);
        resultTestDTO2.setId(2L);
        assertThat(resultTestDTO1).isNotEqualTo(resultTestDTO2);
        resultTestDTO1.setId(null);
        assertThat(resultTestDTO1).isNotEqualTo(resultTestDTO2);
    }
}
