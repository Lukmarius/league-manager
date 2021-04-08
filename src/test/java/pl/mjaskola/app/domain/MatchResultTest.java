package pl.mjaskola.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.mjaskola.app.web.rest.TestUtil;

class MatchResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchResult.class);
        MatchResult matchResult1 = new MatchResult();
        matchResult1.setId(1L);
        MatchResult matchResult2 = new MatchResult();
        matchResult2.setId(matchResult1.getId());
        assertThat(matchResult1).isEqualTo(matchResult2);
        matchResult2.setId(2L);
        assertThat(matchResult1).isNotEqualTo(matchResult2);
        matchResult1.setId(null);
        assertThat(matchResult1).isNotEqualTo(matchResult2);
    }
}
