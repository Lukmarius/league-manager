package pl.mjaskola.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.mjaskola.app.web.rest.TestUtil;

class LeagueStandingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeagueStanding.class);
        LeagueStanding leagueStanding1 = new LeagueStanding();
        leagueStanding1.setId(1L);
        LeagueStanding leagueStanding2 = new LeagueStanding();
        leagueStanding2.setId(leagueStanding1.getId());
        assertThat(leagueStanding1).isEqualTo(leagueStanding2);
        leagueStanding2.setId(2L);
        assertThat(leagueStanding1).isNotEqualTo(leagueStanding2);
        leagueStanding1.setId(null);
        assertThat(leagueStanding1).isNotEqualTo(leagueStanding2);
    }
}
