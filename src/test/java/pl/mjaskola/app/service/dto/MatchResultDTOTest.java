package pl.mjaskola.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.mjaskola.app.web.rest.TestUtil;

class MatchResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchResultDTO.class);
        MatchResultDTO matchResultDTO1 = new MatchResultDTO();
        matchResultDTO1.setId(1L);
        MatchResultDTO matchResultDTO2 = new MatchResultDTO();
        assertThat(matchResultDTO1).isNotEqualTo(matchResultDTO2);
        matchResultDTO2.setId(matchResultDTO1.getId());
        assertThat(matchResultDTO1).isEqualTo(matchResultDTO2);
        matchResultDTO2.setId(2L);
        assertThat(matchResultDTO1).isNotEqualTo(matchResultDTO2);
        matchResultDTO1.setId(null);
        assertThat(matchResultDTO1).isNotEqualTo(matchResultDTO2);
    }
}
