package com.drivinglicence.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.drivinglicence.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAppDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAppDTO.class);
        UserAppDTO userAppDTO1 = new UserAppDTO();
        userAppDTO1.setId(1L);
        UserAppDTO userAppDTO2 = new UserAppDTO();
        assertThat(userAppDTO1).isNotEqualTo(userAppDTO2);
        userAppDTO2.setId(userAppDTO1.getId());
        assertThat(userAppDTO1).isEqualTo(userAppDTO2);
        userAppDTO2.setId(2L);
        assertThat(userAppDTO1).isNotEqualTo(userAppDTO2);
        userAppDTO1.setId(null);
        assertThat(userAppDTO1).isNotEqualTo(userAppDTO2);
    }
}
