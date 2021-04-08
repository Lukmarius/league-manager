package pl.mjaskola.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.mjaskola.app.web.rest.TestUtil;

class LeagueStandingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeagueStandingDTO.class);
        LeagueStandingDTO leagueStandingDTO1 = new LeagueStandingDTO();
        leagueStandingDTO1.setId(1L);
        LeagueStandingDTO leagueStandingDTO2 = new LeagueStandingDTO();
        assertThat(leagueStandingDTO1).isNotEqualTo(leagueStandingDTO2);
        leagueStandingDTO2.setId(leagueStandingDTO1.getId());
        assertThat(leagueStandingDTO1).isEqualTo(leagueStandingDTO2);
        leagueStandingDTO2.setId(2L);
        assertThat(leagueStandingDTO1).isNotEqualTo(leagueStandingDTO2);
        leagueStandingDTO1.setId(null);
        assertThat(leagueStandingDTO1).isNotEqualTo(leagueStandingDTO2);
    }
}
